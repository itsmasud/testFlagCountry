package com.fieldnation.topics;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 12/12/2014.
 */
public class TopicService extends Service {
    private static final String TAG = "TopicService";

    private Hashtable<String, List<TopicClient>> _clients;
    private Hashtable<String, Bundle> _lastSent;


    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();
        _clients = new Hashtable<>();
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
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        TopicClient.clearAll();
        _lastSent = null;
        _clients = null;
        super.onDestroy();
    }

    private List<TopicClient> getClients(String topic) {
        if (!_clients.containsKey(topic)) {
            _clients.put(topic, new LinkedList<TopicClient>());
        }
        return _clients.get(topic);
    }

    private void register(Intent intent) {
        Log.v(TAG, "register");
        String topicId = intent.getStringExtra(TopicConstants.PARAM_TOPIC_ID);
        ResultReceiver receiver = intent.getParcelableExtra(TopicConstants.PARAM_TOPIC_RECEIVER);
        int resultCode = intent.getIntExtra(TopicConstants.PARAM_RESULT_CODE, 0);

        List<TopicClient> clients = getClients(topicId);

        TopicClient c = new TopicClient();
        c.resultCode = resultCode;
        c.receiver = receiver;
        c.topicId = topicId;

        clients.add(c);

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_REGISTER_LISTENER);
        bundle.putInt(TopicConstants.PARAM_CLIENT_ID, c.id);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);
        receiver.send(resultCode, bundle);

        if (_lastSent.containsKey(topicId)) {
            bundle = new Bundle();
            bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_DISPATCH_EVENT);
            bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);
            bundle.putBundle(TopicConstants.PARAM_TOPIC_PARCEL, _lastSent.get(topicId));
            receiver.send(resultCode, bundle);
        }
    }

    private void unregister(Intent intent) {
        Log.v(TAG, "unregister");
        TopicReceiver receiver = intent.getParcelableExtra(TopicConstants.PARAM_TOPIC_RECEIVER);
        int resultCode = intent.getIntExtra(TopicConstants.PARAM_RESULT_CODE, 0);
        int uid = intent.getIntExtra(TopicConstants.PARAM_CLIENT_ID, 0);

        TopicClient c = TopicClient.remove(uid);

        List<TopicClient> clients = getClients(c.topicId);

        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).id == uid) {
                clients.remove(i);
                break;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_UNREGISTER_LISTENER);
        bundle.putInt(TopicConstants.PARAM_CLIENT_ID, uid);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, c.topicId);

        receiver.send(resultCode, bundle);
    }

    private void dispatch(Intent intent) {
        Log.v(TAG, "unregister");
        Bundle parcel = intent.getBundleExtra(TopicConstants.PARAM_TOPIC_PARCEL);
        String topicId = intent.getStringExtra(TopicConstants.PARAM_TOPIC_ID);

        Bundle bundle = new Bundle();
        bundle.putString(TopicConstants.ACTION, TopicConstants.ACTION_DISPATCH_EVENT);
        bundle.putString(TopicConstants.PARAM_TOPIC_ID, topicId);

        List<TopicClient> clients = getClients(topicId);
        for (int i = 0; i < clients.size(); i++) {
            TopicClient c = clients.get(i);
            bundle.putBundle(TopicConstants.PARAM_TOPIC_PARCEL, parcel);
            c.receiver.send(c.resultCode, bundle);
        }

        _lastSent.put(topicId, parcel);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static void registerListener(Context context, int resultCode, String topicId, TopicReceiver topicReceiver) {
        Intent intent = new Intent(context, TopicService.class);
        intent.setAction(TopicConstants.ACTION_REGISTER_LISTENER);
        intent.putExtra(TopicConstants.PARAM_TOPIC_ID, topicId);
        intent.putExtra(TopicConstants.PARAM_TOPIC_RECEIVER, topicReceiver);
        intent.putExtra(TopicConstants.PARAM_RESULT_CODE, resultCode);
        context.startService(intent);
    }

    public static void unRegisterListener(Context context, int resultCode, int uid) {
        Intent intent = new Intent(context, TopicService.class);

        intent.setAction(TopicConstants.ACTION_UNREGISTER_LISTENER);
        intent.putExtra(TopicConstants.PARAM_CLIENT_ID, uid);
        intent.putExtra(TopicConstants.PARAM_RESULT_CODE, resultCode);
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
