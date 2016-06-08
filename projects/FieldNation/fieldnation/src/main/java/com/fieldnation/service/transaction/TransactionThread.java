package com.fieldnation.service.transaction;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteFullException;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJson;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.DebugUtils;
import com.fieldnation.utils.misc;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLProtocolException;

/**
 * Created by itssh on 3/23/2016.
 * <p/>
 * This class executes requests that are stored in the transaction queue
 */
public class TransactionThread extends ThreadManager.ManagedThread {
    private String TAG = UniqueTag.makeTag("TransactionThread");
    private final Object SYNC_LOCK = new Object();

    private WebTransactionService _service;

    private boolean _syncThread = false;
    private boolean _allowSync = true;
    private long _syncCheckCoolDown = 0;

    public boolean _isFirstThread = false;

    private static JsonObject TEST_QUERY;

    static {
        try {
            TEST_QUERY = new HttpJsonBuilder().path("http://www.fieldnation.com").build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public TransactionThread(ThreadManager manager, WebTransactionService service, boolean syncThread) {
        super(manager);
        setName(TAG);
        _service = service;
        _syncThread = syncThread;
        start();
    }

    @Override
    public boolean doWork() {
        //if (_isFirstThread) {
        //    Log.v(TAG, "Trans Count: " + WebTransaction.count());
        //    Log.v(TAG, "Wifi Req Trans Count: " + WebTransaction.countWifiRequired());
        //}

        // try to get a transaction
        if (!App.get().isConnected()) {
            Log.v(TAG, "Testing connection");
            try {
                HttpJson.run(TEST_QUERY);
                GlobalTopicClient.networkConnected(_service);
                Log.v(TAG, "Testing connection... success!");
            } catch (Exception e) {
                Log.v(TAG, "Testing connection... failed!");
                GlobalTopicClient.networkDisconnected(_service);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                }
                return false;
            }
        }

        WebTransaction trans = null;
        try {
            trans = WebTransaction.getNext(_syncThread && allowSync(), _service.isAuthenticated(), _syncThread ? Priority.LOW : Priority.NORMAL);
        } catch (SQLiteFullException ex) {
            ToastClient.toast(App.get(), "Your device is full. Please free up space.", Toast.LENGTH_LONG);
            return false;
        }

        // if failed, then exit
        if (trans == null) {
            return false;
        }

        // debug if have key, output
        if (!misc.isEmptyOrNull(trans.getKey())) {
            Log.v(TAG, "Key: " + trans.getKey());
        }

        // Load the request, and apply authentication
        JsonObject request = null;
        try {
            request = new JsonObject(trans.getRequestString());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        if (request == null) {
            // should never happen!
            WebTransaction.delete(trans.getId());
            return false;
        }

        String handlerName = null;
        HttpResult result = null;

        int notifId = 0;
        NotificationDefinition notifStart = null;
        NotificationDefinition notifSuccess = null;
        NotificationDefinition notifFailed = null;
        NotificationDefinition notifRetry = null;

        try {
            // apply authentication if needed
            if (trans.useAuth()) {
                OAuth auth = _service.getAuth();

                if (auth == null) {
                    AuthTopicClient.requestCommand(App.get());
                    trans.requeue();
                    return false;
                }

                if (auth.getAccessToken() == null) {
                    Log.v(TAG, "accessToken is null");
                    AuthTopicClient.invalidateCommand(App.get());
                    trans.requeue();
                    return false;
                }

                if (!_service.isAuthenticated()) {
                    Log.v(TAG, "skip no auth");
                    trans.requeue();
                    return false;
                }

                if (!request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
                    request.put(HttpJsonBuilder.PARAM_WEB_HOST, auth.getHost());
                }
                request.put(HttpJsonBuilder.PARAM_WEB_PROTOCOL, "https");
                auth.applyToRequest(request);
            }


            if (request.has(HttpJsonBuilder.PARAM_NOTIFICATION_ID)) {
                notifId = request.getInt(HttpJsonBuilder.PARAM_NOTIFICATION_ID);
                notifStart = NotificationDefinition.fromJson(request.getJsonObject(HttpJsonBuilder.PARAM_NOTIFICATION_START));
                notifSuccess = NotificationDefinition.fromJson(request.getJsonObject(HttpJsonBuilder.PARAM_NOTIFICATION_SUCCESS));
                notifFailed = NotificationDefinition.fromJson(request.getJsonObject(HttpJsonBuilder.PARAM_NOTIFICATION_FAILED));
                notifRetry = NotificationDefinition.fromJson(request.getJsonObject(HttpJsonBuilder.PARAM_NOTIFICATION_RETRY));
                generateNotification(notifId, notifStart);
            }

            Log.v(TAG, request.display());

            handlerName = trans.getHandlerName();

            if (!misc.isEmptyOrNull(handlerName)) {
                WebTransactionHandler.Result wresult = WebTransactionHandler.startTransaction(_service, handlerName, trans);
            }

            // **** perform request ****
            result = HttpJson.run(request);

            // debug output
            try {
                Log.v(TAG, "ResponseCode: " + result.getResponseCode());
                Log.v(TAG, "ResponseMessage: " + result.getResponseMessage());
                // this can cause OOM errors, only uncomment when trying to debug something
                //if (!result.isFile() && result.getString() != null) {
                //Log.v(TAG, "Result: " + result.getString());
                //}
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            // **** Error handling ****
            // check for invalid auth
            if (!result.isFile()
                    && (result.getString() != null && result.getString().contains("You must provide a valid OAuth token to make a request"))) {
                Log.v(TAG, "Reauth");
                AuthTopicClient.invalidateCommand(_service);
                transRequeueNetworkDown(trans, notifId, notifRetry);
                AuthTopicClient.requestCommand(_service);
                return true;

            } else if (result.getResponseCode() == 400) {
                // Bad request
                // need to report this
                // need to re-auth?
                if (result.getString() != null && result.getString().contains("You don't have permission to see this workorder")) {
                    WebTransactionHandler.failTransaction(_service, handlerName, trans, result, null);
                    WebTransaction.delete(trans.getId());
                    return true;
                } else if (result.getResponseMessage().contains("Bad Request")) {
                    WebTransactionHandler.failTransaction(_service, handlerName, trans, result, null);
                    WebTransaction.delete(trans.getId());
                    return true;
                } else {
                    Log.v(TAG, "1");
                    AuthTopicClient.invalidateCommand(_service);
                    transRequeueNetworkDown(trans, notifId, notifRetry);
                    AuthTopicClient.requestCommand(_service);
                    return true;
                }

            } else if (result.getResponseCode() == 401) {
                // 401 usually means bad auth token
                if (HttpJsonBuilder.isFieldNation(request)) {
                    Log.v(TAG, "Reauth 2");
                    AuthTopicClient.invalidateCommand(_service);
                    transRequeueNetworkDown(trans, notifId, notifRetry);
                    AuthTopicClient.requestCommand(_service);
                    return true;
                } else {
                    WebTransactionHandler.failTransaction(_service, handlerName, trans, result, null);
                    WebTransaction.delete(trans.getId());
                    generateNotification(notifId, notifFailed);
                    return true;
                }

            } else if (result.getResponseCode() == 404) {
                // not found?... error
                WebTransactionHandler.failTransaction(_service, handlerName, trans, result, null);
                WebTransaction.delete(trans.getId());
                generateNotification(notifId, notifFailed);
                return true;

            } else if (result.getResponseCode() == 413) {
                ToastClient.toast(_service, "File too large to upload", Toast.LENGTH_LONG);
                WebTransactionHandler.failTransaction(_service, handlerName, trans, result, null);
                WebTransaction.delete(trans.getId());
                generateNotification(notifId, notifFailed);
                return true;

                // usually means code is being updated on the server
            } else if (result.getResponseCode() == 502) {
                Log.v(TAG, "2");
                transRequeueNetworkDown(trans, notifId, notifRetry);
                AuthTopicClient.requestCommand(_service);
                return true;

            } else if (result.getResponseCode() / 100 != 2) {
                Log.v(TAG, "3");
                WebTransactionHandler.failTransaction(_service, handlerName, trans, result, null);
                WebTransaction.delete(trans.getId());
                generateNotification(notifId, notifFailed);
                return true;
            }

            Log.v(TAG, "Passed response error checks");

            GlobalTopicClient.networkConnected(_service);

            if (!misc.isEmptyOrNull(handlerName)) {
                WebTransactionHandler.Result wresult = WebTransactionHandler.completeTransaction(
                        _service, handlerName, trans, result);

                switch (wresult) {
                    case DELETE:
                        generateNotification(notifId, notifFailed);
                        WebTransactionHandler.failTransaction(_service, handlerName, trans, result, null);
                        WebTransaction.delete(trans.getId());
                        break;
                    case CONTINUE:
                        generateNotification(notifId, notifSuccess);
                        WebTransaction.delete(trans.getId());
                        break;
                    case REQUEUE:
                        Log.v(TAG, "3");
                        transRequeueNetworkDown(trans, notifId, notifRetry);
                        break;
                }
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            Log.v(TAG, "4");
            Log.v(TAG, ex);
            WebTransactionHandler.failTransaction(_service, handlerName, trans, result, ex);
            WebTransaction.delete(trans.getId());
            generateNotification(notifId, notifFailed);

        } catch (SecurityException ex) {
            Log.v(TAG, "4b");
            Log.v(TAG, ex);
            WebTransactionHandler.failTransaction(_service, handlerName, trans, result, ex);
            WebTransaction.delete(trans.getId());
            generateNotification(notifId, notifFailed);

        } catch (SSLProtocolException | UnknownHostException | ConnectException | SocketTimeoutException | EOFException ex) {
            Log.v(TAG, "5");
            Log.v(TAG, ex);
            transRequeueNetworkDown(trans, notifId, notifRetry);

        } catch (SSLException ex) {
            Log.v(TAG, ex);
            if (ex.getMessage().contains("Broken pipe")) {
                Log.v(TAG, "6");
                transRequeueNetworkDown(trans, notifId, notifRetry);
            } else {
                Log.v(TAG, "7");
                transRequeueNetworkDown(trans, notifId, notifRetry);
            }

        } catch (IOException ex) {
            Log.v(TAG, "8");
            Log.v(TAG, ex);
            transRequeueNetworkDown(trans, notifId, notifRetry);

        } catch (Exception ex) {
            Log.v(TAG, "9");
            Log.v(TAG, ex);
            if (ex.getMessage() != null && ex.getMessage().contains("ETIMEDOUT")) {
                transRequeueNetworkDown(trans, notifId, notifRetry);
            } else {
                // no freaking clue
                Debug.logException(ex);
                Log.v(TAG, ex);
                WebTransactionHandler.failTransaction(_service, handlerName, trans, result, ex);
                WebTransaction.delete(trans.getId());
                generateNotification(notifId, notifFailed);
            }
        }
        Log.v(TAG, "10");
        return true;
    }

    private void transRequeueNetworkDown(WebTransaction trans, int notifId, NotificationDefinition notif) {
        Log.v(TAG, "transRequeueNetworkDown");
        DebugUtils.printStackTrace("transRequeueNetworkDown");
        generateNotification(notifId, notif);
        GlobalTopicClient.networkDisconnected(_service);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        trans.requeue();
    }

    protected static void generateNotification(int notifyId, NotificationDefinition notif) {
        if (notif == null)
            return;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(App.get())
                .setLargeIcon(null)
                .setSmallIcon(notif.icon)
                .setContentTitle(notif.title)
                .setTicker(notif.ticker)
                .setContentText(notif.body);

        NotificationManager mNotifyMgr = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);

//        Log.v(TAG, "notification created");

        mNotifyMgr.notify(notifyId, mBuilder.build());
    }

    private boolean allowSync() {
        synchronized (SYNC_LOCK) {
            // TODO calculate by collecting config information and compare to phone state
            if (_syncCheckCoolDown < System.currentTimeMillis()) {
                Log.v(TAG, "Running allowSync");
                _allowSync = true;

                SharedPreferences settings = App.get().getSharedPreferences();

                boolean requireWifi = settings.getBoolean(App.get().getString(R.string.pref_key_sync_require_wifi), true);
                boolean requirePower = settings.getBoolean(App.get().getString(R.string.pref_key_sync_require_power), true);
                boolean haveWifi = App.get().haveWifi();

                if (requireWifi && !haveWifi) {
                    _allowSync = false;
                } else {
                    boolean pluggedIn = App.get().isCharging();
                    if (requirePower && !pluggedIn) {
                        _allowSync = false;
                    }
                }
                _syncCheckCoolDown = System.currentTimeMillis() + 60000;
            }
            return _allowSync;
        }
    }
}