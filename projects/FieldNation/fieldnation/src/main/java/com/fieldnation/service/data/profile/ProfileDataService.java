package com.fieldnation.service.data.profile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionBuilder;

import java.lang.ref.WeakReference;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileDataService extends Service implements ProfileConstants {
    private static final String TAG = "ProfileDataService";

    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (intent != null) {
            new Thread(new ProfileProcessingRunnable(this, intent)).start();
        }
        return START_STICKY;
    }

    private static void getMyUserInformation(Context context, Intent intent) {
        Log.v(TAG, "getMyUserInformation");

        // get stored object
        StoredObject obj = StoredObject.get(context, PSO_PROFILE, PSO_MY_PROFILE_KEY);
        // if exists, then pass it back
        if (obj != null) {
            try {
                ProfileDataDispatch.myUserInformation(context, new JsonObject(obj.getData()));
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (obj == null || (obj.getLastUpdated() + 30000 < System.currentTimeMillis())) {
            // send request (we always ask for an update)
            try {
                WebTransactionBuilder.builder(context)
                        .priority(WebTransaction.Priority.HIGH)
                        .handler(ProfileWebTransactionHandler.class)
                        .handlerParams(ProfileWebTransactionHandler.generateGetProfileParams())
                        .key("ProfileGet")
                        .useAuth()
                        .request(
                                new HttpJsonBuilder()
                                        .protocol("https")
                                        .method("GET")
                                        .path("/api/rest/v1/profile")
                        ).send();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getAllNotifications(Context context, Intent intent) {
        Log.v(TAG, "getAllNotifications");
        int page = intent.getIntExtra(PARAM_PAGE, 0);

        StoredObject obj = StoredObject.get(context, PSO_NOTIFICATION_PAGE, page + "");
        if (obj != null) {
            try {
                ProfileDataDispatch.allNotifications(context, new JsonArray(obj.getData()), page);
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (obj == null || (obj.getLastUpdated() + 30000 < System.currentTimeMillis())) {
            try {
                WebTransactionBuilder.builder(context)
                        .priority(WebTransaction.Priority.LOW)
                        .handler(ProfileWebTransactionHandler.class)
                        .handlerParams(ProfileWebTransactionHandler.generateGetAllNotificationsParams(page))
                        .key("NotificationPage" + page)
                        .useAuth()
                        .request(
                                new HttpJsonBuilder()
                                        .method("GET")
                                        .path("/api/rest/v1/profile/notifications/")
                                        .urlParams("?page=" + page)
                        ).send();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getAllMessages(Context context, Intent intent) {
        Log.v(TAG, "getAllMessages");
        int page = intent.getIntExtra(PARAM_PAGE, 0);

        StoredObject obj = StoredObject.get(context, PSO_MESSAGE_PAGE, page);
        if (obj != null) {
            try {
                ProfileDataDispatch.allMessages(context, new JsonArray(obj.getData()), page);
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (obj == null || (obj.getLastUpdated() + 30000 < System.currentTimeMillis())) {
            try {
                WebTransactionBuilder.builder(context)
                        .priority(WebTransaction.Priority.LOW)
                        .handler(ProfileWebTransactionHandler.class)
                        .handlerParams(ProfileWebTransactionHandler.generateGetAllMessagesParams(page))
                        .key("MessagePage" + page)
                        .useAuth()
                        .request(
                                new HttpJsonBuilder()
                                        .method("GET")
                                        .path("/api/rest/v1/profile/messages/")
                                        .urlParams("?page=" + page)
                        ).send();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addBlockedCompany(Context context, Intent intent) {
    }


    private class ProfileProcessingRunnable implements Runnable {
        private WeakReference<Context> _context;
        private Intent _intent;

        ProfileProcessingRunnable(Context context, Intent intent) {
            _context = new WeakReference<Context>(context);
            _intent = intent;
        }

        @Override
        public void run() {
            synchronized (LOCK) {
                COUNT++;
            }
            Context context = _context.get();
            if (context != null) {
                String action = _intent.getStringExtra(PARAM_ACTION);
                if (action.equals(PARAM_ACTION_GET_MY_PROFILE)) {
                    getMyUserInformation(context, _intent);
                } else if (action.equals(PARAM_ACTION_GET_ALL_NOTIFICATIONS)) {
                    getAllNotifications(context, _intent);
                } else if (action.equals(PARAM_ACTION_GET_ALL_MESSAGES)) {
                    getAllMessages(context, _intent);
                }
            }
            synchronized (LOCK) {
                COUNT--;
                if (COUNT == 0) {
                    stopSelf();
                }
            }
        }

    }
}
