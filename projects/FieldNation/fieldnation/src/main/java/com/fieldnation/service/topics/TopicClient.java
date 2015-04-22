package com.fieldnation.service.topics;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import com.fieldnation.GlobalState;
import com.fieldnation.Log;

import java.lang.ref.WeakReference;

/**
 * Created by Michael Carver on 2/27/2015.
 */
public class TopicClient implements TopicConstants {
    public static final String TAG = "TopicClient";

    private boolean _isConnected = false;
    private Messenger _rcvService = new Messenger(new IncomeHandler(this));
    private Messenger _sndService = null;
    private Listener _listener;


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public TopicClient(Listener listener) {
        _listener = listener;
    }

    public void connect(Context context) {
//        Log.v(TAG, "connect");
        context.bindService(new Intent(context, TopicService.class), _serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnect(Context context, String userTag) {
        delete(userTag);
        context.unbindService(_serviceConnection);
    }

    public boolean isConnected() {
        return _isConnected;
    }

    /*-*****************************-*/
    /*-         Commands            -*/
    /*-*****************************-*/
    public boolean register(String topicId, String userTag) {
        Log.v(TAG, "register(" + topicId + ", " + userTag + ")");
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_TOPIC_ID, topicId);
            bundle.putString(PARAM_USER_TAG, userTag);

            Message msg = Message.obtain();
            msg.what = WHAT_REGISTER_LISTENER;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean unregister(String topicId, String userTag) {
        Log.v(TAG, "unregister");
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_TOPIC_ID, topicId);
            bundle.putString(PARAM_USER_TAG, userTag);

            Message msg = Message.obtain();
            msg.what = WHAT_UNREGISTER_LISTENER;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean delete(String userTag) {
        Log.v(TAG, "delete");
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_USER_TAG, userTag);

            Message msg = Message.obtain();
            msg.what = WHAT_DELETE_CLIENT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Fires off an event. only works when connected to the service.
     *
     * @param topicId
     * @param payload
     * @param keepLast
     * @return
     */
    public boolean dispatchEvent(String topicId, Parcelable payload, boolean keepLast) {
        Log.v(TAG, "dispatchEvent");
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_TOPIC_ID, topicId);
            if (payload == null)
                bundle.putParcelable(PARAM_TOPIC_PARCELABLE, new Bundle());
            else
                bundle.putParcelable(PARAM_TOPIC_PARCELABLE, payload);
            bundle.putBoolean(PARAM_KEEP_LAST, keepLast);

            Message msg = Message.obtain();
            msg.what = WHAT_DISPATCH_EVENT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Fires off an event, fire and forget. Can be called at any time as long as the context is valid.
     *
     * @param context
     * @param topicId
     * @param payload
     * @param keepLast
     */
    public static void dispatchEvent(Context context, String topicId, Parcelable payload, boolean keepLast) {
        TopicService.dispatchEvent(context, topicId, payload, keepLast);
    }

    /*-**********************************-*/
    /*-              Plumbing            -*/
    /*-**********************************-*/
    private final ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            _sndService = new Messenger(service);
            _isConnected = true;
            if (_listener != null)
                _listener.onConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, "onServiceDisconnected");
            _rcvService = null;
            _isConnected = false;
            if (_listener != null)
                _listener.onDisconnected();
        }
    };

    private static class IncomeHandler extends Handler {
        private WeakReference<TopicClient> _client;

        public IncomeHandler(TopicClient client) {
            _client = new WeakReference<>(client);
        }

        @Override
        public void handleMessage(Message msg) {
//            Log.v(TAG, "handleMessage");
            TopicClient client = _client.get();
            if (client == null) {
                super.handleMessage(msg);
                return;
            }

            if (client._listener == null) {
                super.handleMessage(msg);
                return;
            }

            if (msg.getData() != null) {
                msg.getData().setClassLoader(GlobalState.getContext().getClassLoader());
            }

            switch (msg.what) {
                case WHAT_REGISTER_LISTENER:
                    client._listener.onRegistered(
                            msg.getData()
                                    .getString(PARAM_TOPIC_ID));
                    break;
                case WHAT_DISPATCH_EVENT: {
                    Bundle payload = msg.getData();
                    client._listener.onEvent(payload.getString(PARAM_TOPIC_ID),
                            payload.getParcelable(PARAM_TOPIC_PARCELABLE));
                    break;
                }
            }
            super.handleMessage(msg);
        }
    }

    public static abstract class Listener {

        public abstract void onConnected();

        public void onDisconnected() {
        }

        public void onRegistered(String topicId) {
        }

        public abstract void onEvent(String topicId, Parcelable payload);
    }
}
