package com.fieldnation.service.topics;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import com.fieldnation.Log;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Michael Carver on 2/27/2015.
 */
public class TopicService extends Service implements TopicConstants {
    private static final String TAG = "TopicService";

    private Messenger _me = new Messenger(new IncomeHandler(this));
    private Hashtable<String, Parcelable> _lastSent;
    private int _bindCount = 0;
    private int _lastStartId = -1;
    private Handler _handler;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();
        _lastSent = new Hashtable<>();
        _handler = new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        _lastStartId = startId;

        if (intent != null && intent.getExtras() != null) {
            dispatchEvent(intent.getExtras());
        }

        if (_bindCount == 0) {
            stopSelf(startId);
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        _bindCount++;
        Log.v(TAG, "onBind:" + _bindCount);
        return _me.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        _bindCount--;
        Log.v(TAG, "onUnbind:" + _bindCount);
        if (_bindCount == 0 && _lastStartId != -1) {
            stopSelf(_lastStartId);
            _lastStartId = -1;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        TopicUser.reset();
        _lastSent = new Hashtable<>();
        super.onDestroy();
    }

    /*-*************************************-*/
    /*-         Command Handlers            -*/
    /*-*************************************-*/
    private void sendEvent(final Messenger messenger, final int what, final Bundle bundle, final String userTag) {
        _handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    msg.what = what;
                    msg.setData(bundle);
                    msg.replyTo = _me;
                    messenger.send(msg);
                } catch (Exception ex) {
                    Log.e(TAG, userTag);
                    ex.printStackTrace();
                    synchronized (TAG) {
                        TopicUser.deleteUser(userTag);
                    }
                }

            }
        });
    }

    private void register(Bundle bundle, Messenger replyTo) {
        String topicId = bundle.getString(PARAM_TOPIC_ID);
        String userTag = bundle.getString(PARAM_USER_TAG);

        Log.v(TAG, "register(" + userTag + ", " + topicId + ")");

        TopicUser c = null;
        synchronized (TAG) {
            c = TopicUser.getUser(userTag);
            c.messenger = replyTo;
            c.addTopic(topicId);
        }

        Bundle response = new Bundle();
        response.putString(PARAM_TOPIC_ID, topicId);
        sendEvent(replyTo, WHAT_REGISTER_LISTENER, response, c.userTag);

        if (_lastSent.containsKey(topicId)) {
            Log.v(TAG, "lastsent " + topicId);
            bundle = new Bundle();
            bundle.putString(PARAM_TOPIC_ID, topicId);
            bundle.putParcelable(PARAM_TOPIC_PARCELABLE, _lastSent.get(topicId));
            sendEvent(replyTo, WHAT_DISPATCH_EVENT, bundle, c.userTag);
        }
    }

    private void unregister(Bundle bundle) {
        String topicId = bundle.getString(PARAM_TOPIC_ID);
        String userTag = bundle.getString(PARAM_USER_TAG);

        Log.v(TAG, "unregister " + userTag + ":" + topicId);

        synchronized (TAG) {
            TopicUser.unregisterUser(userTag, topicId);
        }
    }

    private void deleteUser(Bundle bundle) {
        String userTag = bundle.getString(PARAM_USER_TAG);

        Log.v(TAG, "deleteUser " + userTag);

        synchronized (TAG) {
            TopicUser.deleteUser(userTag);
        }
    }

    private void dispatchEvent(Bundle bundle) {
        String topicId = bundle.getString(PARAM_TOPIC_ID);
        boolean keepLast = bundle.getBoolean(PARAM_KEEP_LAST);
        Parcelable payload = bundle.getParcelable(PARAM_TOPIC_PARCELABLE);

        Log.v(TAG, "dispatch(" + topicId + ", " + keepLast + ")");

        Bundle response = new Bundle();
        //response.putString(TopicConstants.ACTION, TopicConstants.ACTION_DISPATCH_EVENT);
        response.putString(PARAM_TOPIC_ID, topicId);

        synchronized (TAG) {
            Set<TopicUser> users = null;
            Iterator<TopicUser> iter = null;
            users = TopicUser.getUsers(topicId);
            iter = users.iterator();
            Log.v(TAG, "Topic: " + topicId);
            Log.v(TAG, "Users: " + users.size());
            while (iter.hasNext()) {
                TopicUser c = iter.next();
                //Log.v(TAG, "Client: " + c.tag);
                bundle.putParcelable(PARAM_TOPIC_PARCELABLE, payload);
                sendEvent(c.messenger, WHAT_DISPATCH_EVENT, bundle, c.userTag);
            }
        }

        if (keepLast)
            _lastSent.put(topicId, payload);

        // Todo shutdown?
//        if (topicId.equals(.TOPIC_SHUTDOWN)) {
//            shutdown();
//        }
    }

    public static void dispatchEvent(Context context, String topicId, Parcelable payload, boolean keepLast) {
        Intent intent = new Intent(context, TopicService.class);
        intent.putExtra(PARAM_TOPIC_ID, topicId);
        if (payload != null)
            intent.putExtra(PARAM_TOPIC_PARCELABLE, (Parcelable) payload);
        else
            intent.putExtra(PARAM_TOPIC_PARCELABLE, (Parcelable) new Bundle());
        intent.putExtra(PARAM_KEEP_LAST, keepLast);

        context.startService(intent);
    }

    /*-**********************************-*/
    /*-              Plumbing            -*/
    /*-**********************************-*/
    private static class IncomeHandler extends Handler {
        private WeakReference<TopicService> _wr;

        public IncomeHandler(TopicService oss) {
            _wr = new WeakReference<>(oss);
        }

        @Override
        public void handleMessage(Message msg) {
            TopicService svc = _wr.get();
            if (svc == null) {
                super.handleMessage(msg);
                return;
            }

            switch (msg.what) {
                case WHAT_REGISTER_LISTENER:
                    svc.register(msg.getData(), msg.replyTo);
                    break;
                case WHAT_DELETE_CLIENT:
                    svc.deleteUser(msg.getData());
                    break;
                case WHAT_DISPATCH_EVENT:
                    svc.dispatchEvent(msg.getData());
                    break;
                case WHAT_UNREGISTER_LISTENER:
                    svc.unregister(msg.getData());
                    break;
            }

            super.handleMessage(msg);
        }
    }
}
