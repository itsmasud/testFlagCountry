package com.fieldnation.topics;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by michael.carver on 12/12/2014.
 */
public class TopicService extends Service {
    private static final String TAG = "topics.TopicService";

    private Hashtable<String, Bundle> _lastSent;

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();
        _lastSent = new Hashtable<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (intent == null)
            return START_STICKY;

        String action = intent.getAction();

        if (TopicConstants.ACTION_REGISTER_LISTENER.equals(action)) {
            register(intent);
        } else if (TopicConstants.ACTION_DISPATCH_EVENT.equals(action)) {
            dispatch(intent);
        } else if (TopicConstants.ACTION_UNREGISTER_LISTENER.equals(action)) {
            unregister(intent);
        } else if (TopicConstants.ACTION_DELETE_CLIENT.equals(action)) {
            delete(intent);
        }

        return START_STICKY;
    }

    private void send(ResultReceiver receiver, int code, Bundle bundle) {
        try {
            receiver.send(code, bundle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _lastSent = null;
        super.onDestroy();
    }

    private void register(Intent intent) {
        String topicId = intent.getStringExtra(TopicConstants.PARAM_TOPIC_ID);
        ResultReceiver receiver = intent.getParcelableExtra(TopicConstants.PARAM_TOPIC_RECEIVER);
        int resultCode = intent.getIntExtra(TopicConstants.PARAM_RESULT_CODE, 0);
        String tag = intent.getStringExtra(TopicConstants.PARAM_TAG);

        Log.v(TAG, "register(" + tag + ", " + topicId + ")");

        TopicClient c = null;
        synchronized (TAG) {
            c = TopicClient.get(tag);
            c.resultCode = resultCode;
            c.receiver = receiver;
            c.addTopic(topicId);
        }

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_REGISTER_LISTENER);
        bundle.putString(TopicConstants.PARAM_TAG, c.tag);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);

        send(receiver, resultCode, bundle);

        if (_lastSent.containsKey(topicId)) {
            bundle = new Bundle();
            bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_DISPATCH_EVENT);
            bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);
            bundle.putString(TopicConstants.PARAM_TAG, c.tag);
            bundle.putBundle(TopicConstants.PARAM_TOPIC_PARCEL, _lastSent.get(topicId));

            send(receiver, resultCode, bundle);
        }
    }

    private void unregister(Intent intent) {
        Log.v(TAG, "unregister");
        int resultCode = intent.getIntExtra(TopicConstants.PARAM_RESULT_CODE, 0);
        String tag = intent.getStringExtra(TopicConstants.PARAM_TAG);
        String topicId = intent.getStringExtra(TopicConstants.PARAM_TOPIC_ID);

        TopicClient c = null;
        synchronized (TAG) {
            c = TopicClient.get(tag);
            TopicClient.unregister(tag, topicId);
        }

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_UNREGISTER_LISTENER);
        bundle.putString(TopicConstants.PARAM_TAG, tag);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);

        send(c.receiver, resultCode, bundle);
    }

    private void delete(Intent intent) {
        Log.v(TAG, "delete");

        String tag = intent.getStringExtra(TopicConstants.PARAM_TAG);

        synchronized (TAG) {
            TopicClient.delete(tag);
        }
    }

    private void dispatch(Intent intent) {
        Log.v(TAG, "dispatch");
        Bundle parcel = intent.getBundleExtra(TopicConstants.PARAM_TOPIC_PARCEL);
        String topicId = intent.getStringExtra(TopicConstants.PARAM_TOPIC_ID);
        boolean doKeep = intent.getBooleanExtra(TopicConstants.PARAM_KEEP_LAST_SENT, true);

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_DISPATCH_EVENT);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);

        Set<TopicClient> clients = null;
        Iterator<TopicClient> iter = null;
        synchronized (TAG) {
            clients = TopicClient.getSet(topicId);
            iter = clients.iterator();
        }
        Log.v(TAG, "Topic: " + topicId);
        Log.v(TAG, "Clients: " + clients.size());
        while (iter.hasNext()) {
            TopicClient c = iter.next();
            Log.v(TAG, "Client: " + c.tag);
            bundle.putBundle(TopicConstants.PARAM_TOPIC_PARCEL, parcel);
            send(c.receiver, c.resultCode, bundle);
        }

        if (doKeep)
            _lastSent.put(topicId, parcel);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static void registerListener(Context context, int resultCode, String tag, String topicId, TopicReceiver topicReceiver) {
        if (context == null)
            return;
        Intent intent = new Intent(context, TopicService.class);
        intent.setAction(TopicConstants.ACTION_REGISTER_LISTENER);
        intent.putExtra(TopicConstants.PARAM_TOPIC_ID, topicId);
        intent.putExtra(TopicConstants.PARAM_TOPIC_RECEIVER, topicReceiver);
        intent.putExtra(TopicConstants.PARAM_TAG, tag);
        intent.putExtra(TopicConstants.PARAM_RESULT_CODE, resultCode);
        context.startService(intent);
    }

    public static void unRegisterListener(Context context, int resultCode, String tag, String topic) {
        if (context == null)
            return;
        Intent intent = new Intent(context, TopicService.class);

        intent.setAction(TopicConstants.ACTION_UNREGISTER_LISTENER);
        intent.putExtra(TopicConstants.PARAM_TAG, tag);
        intent.putExtra(TopicConstants.PARAM_TOPIC_ID, topic);
        intent.putExtra(TopicConstants.PARAM_RESULT_CODE, resultCode);
        context.startService(intent);
    }

    public static void delete(Context context, String tag) {
        if (context == null)
            return;
        Intent intent = new Intent(context, TopicService.class);

        intent.setAction(TopicConstants.ACTION_DELETE_CLIENT);
        intent.putExtra(TopicConstants.PARAM_TAG, tag);
        context.startService(intent);
    }

    public static void dispatchTopic(Context context, String topicId, Bundle parcel) {
        dispatchTopic(context, topicId, parcel, true);
    }

    public static void dispatchTopic(Context context, String topicId, Bundle parcel, boolean keepLastSent) {
        if (context == null)
            return;

        Intent intent = new Intent(context, TopicService.class);

        intent.setAction(TopicConstants.ACTION_DISPATCH_EVENT);
        intent.putExtra(TopicConstants.PARAM_TOPIC_ID, topicId);
        intent.putExtra(TopicConstants.PARAM_KEEP_LAST_SENT, keepLastSent);
        intent.putExtra(TopicConstants.PARAM_TOPIC_PARCEL, parcel == null ? new Bundle() : parcel);
        context.startService(intent);
    }


}
