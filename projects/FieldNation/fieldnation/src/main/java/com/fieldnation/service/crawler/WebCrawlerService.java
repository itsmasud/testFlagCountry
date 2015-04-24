package com.fieldnation.service.crawler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.photo.PhotoDataClient;
import com.fieldnation.service.data.profile.ProfileDataClient;
import com.fieldnation.service.data.workorder.WorkorderDataClient;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.util.List;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class WebCrawlerService extends Service {
    private static final String TAG = "WebCrawlerService";

    private ProfileDataClient _profileClient;
    private WorkorderDataClient _workorderClient;

    private boolean _haveProfile = false;

    public WebCrawlerService() {
        super();
        Log.v(TAG, "WebCrawlerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

/*
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                misc.flushLogs(WebCrawlerService.this, 86400000); // 1 day
                StoredObject.flush(WebCrawlerService.this, 604800000); // 1 week
                return null;
            }
        }.executeEx();
*/

        startStuff();
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _workorderClient.disconnect(this);
        _profileClient.disconnect(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        // TODO set up a clock
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startStuff() {
        Log.v(TAG, "startStuff");
        _profileClient = new ProfileDataClient(_profileClient_listener);
        _profileClient.connect(this);

        _workorderClient = new WorkorderDataClient(_workorderClient_listener);
        _workorderClient.connect(this);
    }

    private final ProfileDataClient.Listener _profileClient_listener = new ProfileDataClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_profileClient_listener.onConnected");
            _profileClient.registerAllMessages(true);
            _profileClient.registerAllNotifications(true);
            _profileClient.registerProfile(true);

            ProfileDataClient.getProfile(WebCrawlerService.this, true);
            ProfileDataClient.getAllMessages(WebCrawlerService.this, 0, true);
            ProfileDataClient.getAllNotifications(WebCrawlerService.this, 0, true);
        }

        @Override
        public void onProfile(Profile profile) {
            Log.v(TAG, "onProfile " + _haveProfile);
            if (!_haveProfile) {
                Log.v(TAG, "onProfile");
                PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, profile.getPhoto().getLarge(), true, true);
                PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, profile.getPhoto().getThumb(), true, true);
                _haveProfile = true;
            }
        }

        @Override
        public void onAllMessagesPage(List<Message> list, int page) {
            Log.v(TAG, "onAllMessagesPage");

            if (list == null || list.size() == 0) {
                return;
            }
            Log.v(TAG, "onAllMessagesPage(" + list.size() + "," + page + ")");

            ProfileDataClient.getAllMessages(WebCrawlerService.this, page + 1, true);

            for (int i = 0; i < list.size(); i++) {
                Message message = list.get(i);

                PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
            }
        }

        @Override
        public void onAllNotificationPage(List<Notification> list, int page) {
            Log.v(TAG, "onAllNotificationPage");
            if (list == null || list.size() == 0) {
                return;
            }
            Log.v(TAG, "onAllNotificationPage(" + list.size() + "," + page + ")");

            ProfileDataClient.getAllNotifications(WebCrawlerService.this, page + 1, true);
        }
    };


    private final WorkorderDataClient.Listener _workorderClient_listener = new WorkorderDataClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderClient_listener.onConnected");
            _workorderClient.registerList(true);
            _workorderClient.registerDetails(true);

            WorkorderDataClient.requestList(WebCrawlerService.this, WorkorderDataSelector.ASSIGNED, 0, true);
            WorkorderDataClient.requestList(WebCrawlerService.this, WorkorderDataSelector.CANCELED, 0, true);
            WorkorderDataClient.requestList(WebCrawlerService.this, WorkorderDataSelector.COMPLETED, 0, true);
        }

        @Override
        public void onWorkorderList(List<Workorder> list, WorkorderDataSelector selector, int page) {
            Log.v(TAG, "onWorkorderList");
            if (list == null || list.size() == 0) {
                return;
            }
            Log.v(TAG, "onWorkorderList(" + list.size() + "," + selector.getCall() + "," + page + ")");

            WorkorderDataClient.requestList(WebCrawlerService.this, selector, page + 1, true);

            Log.v(TAG, "onWorkorderList, Request details");
            for (int i = 0; i < list.size(); i++) {
                Workorder workorder = list.get(i);

                WorkorderDataClient.requestDetails(WebCrawlerService.this, workorder.getWorkorderId(), true);
                WorkorderDataClient.requestBundle(WebCrawlerService.this, workorder.getWorkorderId(), true);
            }
            Log.v(TAG, "onWorkorderList, done");
        }

        @Override
        public void onDetails(Workorder workorder) {
            Log.v(TAG, "onDetails " + workorder.getWorkorderId());
            // get signatures
            Signature[] sigs = workorder.getSignatureList();
            if (sigs != null && sigs.length > 0) {
                for (int i = 0; i < sigs.length; i++) {
                    Log.v(TAG, "getSignature");
                    WorkorderDataClient.requestGetSignature(WebCrawlerService.this, workorder.getWorkorderId(), sigs[i].getSignatureId(), true);
                }
            }

            // get bundle
            if (workorder.getBundleId() != null && workorder.getBundleId() > 0) {
                WorkorderDataClient.requestBundle(WebCrawlerService.this, workorder.getBundleId(), true);
            }

            // get notifications
            // get messages
            // get deliverables
            // get tasks
        }
    };
}
