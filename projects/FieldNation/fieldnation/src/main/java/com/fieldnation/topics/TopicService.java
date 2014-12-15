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
import java.util.LinkedList;
import java.util.List;
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

    @Override
    public void onDestroy() {
        _lastSent = null;
        super.onDestroy();
    }

    private void register(Intent intent) {
        Log.v(TAG, "register");
        String topicId = intent.getStringExtra(TopicConstants.PARAM_TOPIC_ID);
        ResultReceiver receiver = intent.getParcelableExtra(TopicConstants.PARAM_TOPIC_RECEIVER);
        int resultCode = intent.getIntExtra(TopicConstants.PARAM_RESULT_CODE, 0);
        String tag = intent.getStringExtra(TopicConstants.PARAM_TAG);

        TopicClient c = TopicClient.get(tag);
        c.resultCode = resultCode;
        c.receiver = receiver;
        c.addTopic(topicId);

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_REGISTER_LISTENER);
        bundle.putString(TopicConstants.PARAM_TAG, c.tag);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);

        receiver.send(resultCode, bundle);

        if (_lastSent.containsKey(topicId)) {
            bundle = new Bundle();
            bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_DISPATCH_EVENT);
            bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);
            bundle.putString(TopicConstants.PARAM_TAG, c.tag);
            bundle.putBundle(TopicConstants.PARAM_TOPIC_PARCEL, _lastSent.get(topicId));

            receiver.send(resultCode, bundle);
        }
    }

    private void unregister(Intent intent) {
        Log.v(TAG, "unregister");
        int resultCode = intent.getIntExtra(TopicConstants.PARAM_RESULT_CODE, 0);
        String tag = intent.getStringExtra(TopicConstants.PARAM_TAG);
        String topicId = intent.getStringExtra(TopicConstants.PARAM_TOPIC_ID);

        TopicClient c = TopicClient.get(tag);
        TopicClient.unregister(tag, topicId);

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_UNREGISTER_LISTENER);
        bundle.putString(TopicConstants.PARAM_TAG, tag);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);

        c.receiver.send(resultCode, bundle);
    }

    private void delete(Intent intent) {
        Log.v(TAG, "delete");

        int resultCode = intent.getIntExtra(TopicConstants.PARAM_RESULT_CODE, 0);
        String tag = intent.getStringExtra(TopicConstants.PARAM_TAG);

        TopicClient c = TopicClient.get(tag);
        TopicClient.delete(tag);

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_DELETE_CLIENT);
        bundle.putString(TopicConstants.PARAM_TAG, tag);

        c.receiver.send(resultCode, bundle);
    }

    private void dispatch(Intent intent) {
        Log.v(TAG, "unregister");
        Bundle parcel = intent.getBundleExtra(TopicConstants.PARAM_TOPIC_PARCEL);
        String topicId = intent.getStringExtra(TopicConstants.PARAM_TOPIC_ID);

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_DISPATCH_EVENT);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);

        Set<TopicClient> clients = TopicClient.getSet(topicId);
        Iterator<TopicClient> iter = clients.iterator();
        while (iter.hasNext()) {
            TopicClient c = iter.next();
            bundle.putBundle(TopicConstants.PARAM_TOPIC_PARCEL, parcel);
            c.receiver.send(c.resultCode, bundle);
        }

        _lastSent.put(topicId, parcel);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static void registerListener(Context context, int resultCode, String tag, String topicId, TopicReceiver topicReceiver) {
        Intent intent = new Intent(context, TopicService.class);
        intent.setAction(TopicConstants.ACTION_REGISTER_LISTENER);
        intent.putExtra(TopicConstants.PARAM_TOPIC_ID, topicId);
        intent.putExtra(TopicConstants.PARAM_TOPIC_RECEIVER, topicReceiver);
        intent.putExtra(TopicConstants.PARAM_TAG, tag);
        intent.putExtra(TopicConstants.PARAM_RESULT_CODE, resultCode);
        context.startService(intent);
    }

    public static void unRegisterListener(Context context, int resultCode, String tag, String topic) {
        Intent intent = new Intent(context, TopicService.class);

        intent.setAction(TopicConstants.ACTION_UNREGISTER_LISTENER);
        intent.putExtra(TopicConstants.PARAM_TAG, tag);
        intent.putExtra(TopicConstants.PARAM_TOPIC_ID, topic);
        intent.putExtra(TopicConstants.PARAM_RESULT_CODE, resultCode);
        context.startService(intent);
    }

    public static void delete(Context context, int resultCode, String tag) {
        Intent intent = new Intent(context, TopicService.class);

        intent.setAction(TopicConstants.ACTION_DELETE_CLIENT);
        intent.putExtra(TopicConstants.PARAM_RESULT_CODE, resultCode);
        intent.putExtra(TopicConstants.PARAM_TAG, tag);
        context.startService(intent);
    }

    public static void dispatchTopic(Context context, String topicId, Bundle parcel) {
        Intent intent = new Intent(context, TopicService.class);

        intent.setAction(TopicConstants.ACTION_DISPATCH_EVENT);
        intent.putExtra(TopicConstants.PARAM_TOPIC_ID, topicId);
        intent.putExtra(TopicConstants.PARAM_TOPIC_PARCEL, parcel == null ? new Bundle() : parcel);
        context.startService(intent);
    }


}
