package com.fieldnation.rpc.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.utils.misc;

import java.lang.ref.WeakReference;
import java.text.ParseException;

/**
 * Created by Michael Carver on 3/11/2015.
 */
public class WebService extends Service implements WebServiceConstants {
    private static final String TAG = "WebService";

    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new HttpProcessor(intent, this, startId)).start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class HttpProcessor implements Runnable {
        private WeakReference<WebService> wr;
        private Intent intent;

        public HttpProcessor(Intent intent, WebService service, int startId) {
            wr = new WeakReference<>(service);
            this.intent = intent;
        }

        @Override
        public void run() {
            WebService svc = wr.get();
            synchronized (LOCK) {
                COUNT++;
                Log.v(TAG, "Thread Count: " + COUNT);
            }
            try {
                String authToken = intent.getStringExtra(KEY_PARAM_AUTH_TOKEN);
                String username = intent.getStringExtra(KEY_PARAM_USERNAME);
                int resultCode = intent.getIntExtra(KEY_PARAM_RESULT_CODE, 0);
                ResultReceiver rr = intent.getParcelableExtra(KEY_PARAM_CALLBACK);
                JsonObject request = null;
                String errorMessage = null;
                String errorType = ERROR_NONE;

                try {
                    request = new JsonObject(intent.getByteArrayExtra(KEY_PARAM_REQUEST));
                    Log.v(TAG, request.display());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (misc.isEmptyOrNull(username)) {
                    doHttpError(rr, intent.getExtras(), resultCode, ERROR_NO_USERNAME, "No username supplied!");
                    return;
                }

                AuthToken auth = null;
                // look up user in the cache
                AuthCache authCache = AuthCache.get(svc, username);

                // not found
                if (authCache == null) {
                    errorType = ERROR_USER_NOT_FOUND;
                    errorMessage = "Could not find the user.";
                } else {
                    // validate session
                    if (authToken != null) {
                        // Log.v(TAG, authToken);
                    } else {
                        // Log.v(TAG, "null authToken");
                    }
                    if (authCache.validateSessionHash(authToken)) {
                        try {
                            auth = AuthToken.fromCache(authCache.getOAuthBlob());
                        } catch (ParseException e) {
                            // this is handled below when we check for auth != null
                            e.printStackTrace();
                        }
                    } else {
                        errorType = ERROR_SESSION_INVALID;
                        errorMessage = "The authtoken is invalid or has expired.";
                    }
                }

                if (auth == null) {
                    doHttpError(rr, intent.getExtras(), resultCode, errorType, errorMessage);
                    return;
                }

                Log.v(TAG, "Atempting web request");
                Bundle bundle = intent.getExtras();
                try {
                    String urlParams = null;
                    if (request.has(HttpJsonBuilder.PARAM_WEB_URL_PARAMS))
                        urlParams = request.getString(HttpJsonBuilder.PARAM_WEB_URL_PARAMS);
                    else
                        urlParams = "";

                    urlParams = auth.applyToUrlOptions(urlParams);
                    request.put(HttpJsonBuilder.PARAM_WEB_URL_PARAMS, urlParams);
                    request.put(HttpJsonBuilder.PARAM_WEB_HOST, auth.getHostname());
                    HttpResult result = HttpJson.run(svc, request);

                    if (result.getResponseCode() / 100 != 2) {
                        Log.v(TAG, "Error response: " + result.getResponseCode());
                        bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                        bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_HTTP_ERROR);
                        bundle.putString(KEY_RESPONSE_ERROR, result.getResponseMessage());
                        bundle.putByteArray(KEY_RESPONSE_DATA, result.getResultsAsByteArray());

                    } else {

                        try {
                            // happy path
                            bundle.putByteArray(KEY_RESPONSE_DATA, result.getResultsAsByteArray());
                            bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                            bundle.putBoolean(KEY_RESPONSE_CACHED, false);
                            bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
                            Log.v(TAG, "web request success");
                        } catch (Exception ex) {
                            try {
                                // unhappy, but http error
                                bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                                bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_HTTP_ERROR);
                                bundle.putString(KEY_RESPONSE_ERROR, result.getResponseMessage());
                                bundle.putByteArray(KEY_RESPONSE_DATA, result.getResultsAsByteArray());
                            } catch (Exception ex1) {
                                // sad path
                                bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
                                bundle.putString(KEY_RESPONSE_ERROR, ex1.getMessage());
                                bundle.putByteArray(KEY_RESPONSE_DATA, result.getResultsAsByteArray());
                            }
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.v(TAG, "web request fail");
                    bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NETWORK_ERROR);
                    bundle.putString(KEY_RESPONSE_ERROR, ex.getMessage());
                }

                try {
                    rr.send(resultCode, bundle);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            } finally {
                synchronized (LOCK) {
                    COUNT--;

                    if (COUNT == 0) {
                        svc.stopSelf();
                    }
                }
            }
        }
    }

    private void doHttpError(ResultReceiver rr, Bundle bundle, int resultCode, String errorType, String message) {
        bundle.putString(KEY_RESPONSE_ERROR_TYPE, errorType);
        bundle.putString(KEY_RESPONSE_ERROR, message);
        rr.send(resultCode, bundle);
    }

}
