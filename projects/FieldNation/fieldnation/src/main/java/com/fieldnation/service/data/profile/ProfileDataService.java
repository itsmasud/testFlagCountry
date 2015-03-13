package com.fieldnation.service.data.profile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

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
    public static final String TAG = "service.data.ProfileDataService";

    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void getMyUserInformation(Context context, Intent intent) {
        StoredObject obj = StoredObject.get(context, "Profile", "Me");

        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(ProfileWebTransactionHandler.class)
                    .handlerParams(ProfileWebTransactionHandler.generateParams(PARAM_ACTION_GET_MY_PROFILE))
                    .key("ProfileGet")
                    .useAuth()
                    .request(
                            new HttpJsonBuilder()
                                    .method("POST")
                                    .path("/api/rest/v1/profile")
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putByteArray(PARAM_PROFILE, obj.getData());
            TopicService.dispatchEvent(context, TOPIC_ID_HAVE_PROFILE, bundle, true);
            return;
        }
    }

    private void getAllNotifications(Context context, Intent intent) {

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
                if (_intent.getStringExtra(PARAM_ACTION).equals(PARAM_ACTION_GET_MY_PROFILE)) {
                    getMyUserInformation(context, _intent);
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
