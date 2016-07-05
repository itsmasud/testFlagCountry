package com.fieldnation.service.topics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.service.MSService;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael Carver on 2/27/2015.
 */
public class TopicService extends MSService implements TopicConstants {
    private static final String TAG = "TopicService";

    private final Messenger _me = new Messenger(new IncomeHandler(this));
    private Hashtable<String, StickyContainer> _stickies;
    private int _bindCount = 0;

    private Handler _handler;

    private long _pruneTimer = 0;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        synchronized (TAG) {
            _stickies = new Hashtable<>();
        }
        _handler = new Handler();
        startActivityMonitor();
    }

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public void processIntent(Intent intent) {
        //Log.v(TAG, "processIntent start");

        if (intent != null && intent.getExtras() != null) {
            dispatchEvent(intent.getExtras());
        }

        if (_pruneTimer < System.currentTimeMillis()) {
            _pruneTimer = System.currentTimeMillis() + 60000;

            pruneStickies();
        }
    }

    @Override
    public void addIntent(List<Intent> intents, Intent intent) {
        super.addIntent(intents, intent);
        Log.v(TAG, "intents " + intents.size());
    }

    @Override
    public IBinder onBind(Intent intent) {
        _bindCount++;
        Log.v(TAG, "onBind:" + _bindCount);
        return _me.getBinder();
    }

    private void startActivityMonitor() {
        _handler.postDelayed(_activityMonitor_runnable, 60000);
    }

    private final Runnable _activityMonitor_runnable = new Runnable() {
        @Override
        public void run() {
            if (_bindCount == 0) {
                stopSelf();
            } else {
                startActivityMonitor();
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        _bindCount--;
        return super.onUnbind(intent);
    }

    @Override
    public void onLowMemory() {
        Log.v(TAG, "onLowMemory");
        pruneStickies();
        super.onLowMemory();
    }

    private void pruneStickies() {
        Hashtable<String, StickyContainer> ns = new Hashtable<>();
        synchronized (TAG) {
            Set<String> keys = _stickies.keySet();
            for (String key : keys) {
                StickyContainer sc = _stickies.get(key);

                if (sc.stickyType == Sticky.FOREVER) {
                    ns.put(key, sc);
                } else if (sc.stickyType == Sticky.TEMP
                        && sc.createdDate + 60000 > System.currentTimeMillis()) {
                    ns.put(key, sc);
                }
            }
            Log.v(TAG, "Pruning stickies done: " + ns.size() + "/" + _stickies.size());
            _stickies = ns;
        }
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        TopicUser.reset();
        synchronized (TAG) {
            _stickies.clear();
        }
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
                    Log.e(TAG, ex.getLocalizedMessage() + ": " + userTag);
                    synchronized (TAG) {
                        TopicUser.deleteUser(userTag);
                    }
                }
            }
        });
    }

    private void register(Bundle bundle, Messenger replyTo) {
        //Log.v(TAG, "register");
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

        StickyContainer sticky = null;
        synchronized (TAG) {
            if (_stickies.containsKey(topicId)) {
                sticky = _stickies.get(topicId);
            }
        }
        if (sticky != null) {
            Log.v(TAG, "sticky " + topicId);
            bundle = new Bundle();
            bundle.putString(PARAM_TOPIC_ID, topicId);
            bundle.putParcelable(PARAM_TOPIC_PARCELABLE, sticky.parcel);
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
        //Log.v(TAG, "deleteUser");
        String userTag = bundle.getString(PARAM_USER_TAG);

//        Log.v(TAG, "deleteUser " + userTag);
        synchronized (TAG) {
            TopicUser.deleteUser(userTag);
        }
//        Log.v(TAG, "deleteUser end");
    }

    // sends events
    private void dispatchEvent(Bundle bundle) {
        Log.v(TAG, "dispatchEvent(" + bundle.getString(PARAM_TOPIC_ID) + ")");
        String[] topicIdTree = bundle.getString(PARAM_TOPIC_ID).split("/");
//        String rootTopicId = (topicId.contains("/") ? topicId.substring(0, topicId.indexOf("/")) : null);
        Sticky stickyType = (Sticky) bundle.getSerializable(PARAM_STICKY);

        synchronized (TAG) {
            String topicId = topicIdTree[0];
            Log.v(TAG, "dispatchEvent(" + topicId + ", " + stickyType + ")");
            // exact match
            Set<TopicUser> users = TopicUser.getUsers(topicId);
            for (TopicUser c : users) {
                sendEvent(c.messenger, WHAT_DISPATCH_EVENT, bundle, c.userTag);
            }

            if (stickyType != Sticky.NONE) {
                _stickies.put(topicId, new StickyContainer(
                        bundle.getParcelable(PARAM_TOPIC_PARCELABLE),
                        stickyType));
            }

            for (int i = 1; i < topicIdTree.length; i++) {
                topicId += "/" + topicIdTree[i];
                Log.v(TAG, "dispatchEvent(" + topicId + ", " + stickyType + ")");

                users = TopicUser.getUsers(topicId);
                for (TopicUser c : users) {
                    sendEvent(c.messenger, WHAT_DISPATCH_EVENT, bundle, c.userTag);
                }

                if (stickyType != Sticky.NONE) {
                    _stickies.put(topicId,
                            new StickyContainer(bundle.getParcelable(PARAM_TOPIC_PARCELABLE),
                                    stickyType));
                }
            }
        }
    }

    // queues up an event for sending
    public static void dispatchEvent(Context context, String topicId, Parcelable payload, Sticky stickyType) {
        context.startService(dispatchEventIntent(context, topicId, payload, stickyType));
    }

    public static Intent dispatchEventIntent(Context context, String topicId, Parcelable payload, Sticky stickyType) {
        //Log.v(TAG, "dispatchEvent(" + topicId + ")");
        Intent intent = new Intent(context, TopicService.class);
        intent.putExtra(PARAM_TOPIC_ID, topicId);

        if (payload != null) {
            intent.putExtra(PARAM_TOPIC_PARCELABLE, payload);
        } else {
            intent.putExtra(PARAM_TOPIC_PARCELABLE, (Parcelable) new Bundle());
        }

        intent.putExtra(PARAM_STICKY, stickyType);

        return intent;
    }

    public static void dispatchEvent(Context context, Bundle event) {
        //Log.v(TAG, "dispatchEvent(" + event + ")");
        Intent intent = new Intent(context, TopicService.class);
        intent.putExtras(event);
        context.startService(intent);
    }

    /*-**********************************-*/
    /*-              Plumbing            -*/
    /*-**********************************-*/
    // accepts messages from clients
    private static class IncomeHandler extends Handler {
        private final WeakReference<TopicService> _wr;

        public IncomeHandler(TopicService oss) {
            _wr = new WeakReference<>(oss);
        }

        @Override
        public void handleMessage(Message msg) {
            TopicService svc = _wr.get();
            if (svc == null) {
                return;
            }
            //Log.v(TAG, "handleMessage");
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
        }
    }

    private static class StickyContainer {
        public Parcelable parcel;
        public long createdDate;
        public Sticky stickyType;

        public StickyContainer(Parcelable parcel, Sticky stickyType) {
            createdDate = System.currentTimeMillis();
            this.stickyType = stickyType;
            this.parcel = parcel;
        }
    }
}
