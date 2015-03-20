package com.fieldnation.service.data.profile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.fieldnation.Log;
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
    public static final String TAG = "ProfileDataService";

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

    private void getMyUserInformation(Context context, Intent intent) {
        Log.v(TAG, "getMyUserInformation");
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
                                    .method("GET")
                                    .path("/api/rest/v1/profile")
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // get stored object
        StoredObject obj = StoredObject.get(context, "Profile", "Me");
        // if exists, then pass it back
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putByteArray(PARAM_DATA, obj.getData());
            TopicService.dispatchEvent(context, TOPIC_ID_HAVE_PROFILE, bundle, true);
            return;
        }
    }

    private void getAllNotifications(Context context, Intent intent) {
        Log.v(TAG, "getAllNotifications");
        int page = intent.getIntExtra(PARAM_PAGE, 0);

        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.LOW)
                    .handler(ProfileWebTransactionHandler.class)
                    .handlerParams(ProfileWebTransactionHandler.getnerateGetAllNotificationsParams(page))
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

        StoredObject obj = StoredObject.get(context, "NotificationPage", page + "");
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putByteArray(PARAM_DATA, obj.getData());
            bundle.putInt(PARAM_PAGE, page);
            bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_ALL_NOTIFICATIONS);
            TopicService.dispatchEvent(context, TOPIC_ID_ALL_NOTIFICATION_LIST, bundle, false);
            return;
        }

    }

    private void getAllMessages(Context context, Intent intent) {
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
