package com.fieldnation.service.objectstore;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Michael Carver on 2/26/2015.
 */
public class ObjectStoreClient implements ObjectStoreConstants {
    public static final String TAG = "ObjectStoreClient";

    private boolean _isConnected = false;
    private Messenger _rcvService = new Messenger(new IncomeHandler(this));
    private Messenger _sndService = null;
    private Listener _listener;

    public ObjectStoreClient(Listener listener) {
        _listener = listener;
    }

    public void connect(Activity activity) {
        activity.bindService(new Intent(activity, ObjectStoreService.class), _serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnect(Activity activity) {
        activity.unbindService(_serviceConnection);
    }

    public boolean isConnected() {
        return _isConnected;
    }

    public boolean put(int resultCode, String objectTypeName, long id, String data) {
        return put(resultCode, objectTypeName, id, data.getBytes());
    }

    public boolean put(int resultCode, String objectTypeName, long id, byte[] data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putLong(PARAM_OBJECT_ID, id);
            bundle.putBoolean(PARAM_IS_FILE, false);
            bundle.putByteArray(PARAM_DATA, data);
            bundle.putInt(PARAM_RESULT_CODE, resultCode);

            Message msg = Message.obtain();
            msg.what = WHAT_PUT_OBJECT;
            msg.setData(bundle);
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean put(int resultCode, String objectTypeName, long id, File file) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putLong(PARAM_OBJECT_ID, id);
            bundle.putBoolean(PARAM_IS_FILE, true);
            bundle.putSerializable(PARAM_FILE, file);
            bundle.putInt(PARAM_RESULT_CODE, resultCode);

            Message msg = Message.obtain();
            msg.what = WHAT_PUT_OBJECT;
            msg.setData(bundle);
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean get(int resultCode, String objectTypeName, long id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putLong(PARAM_OBJECT_ID, id);
            bundle.putInt(PARAM_RESULT_CODE, resultCode);

            Message msg = Message.obtain();
            msg.what = WHAT_GET_OBJECT;
            msg.setData(bundle);
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean list(int resultCode, String objectTypeName) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putInt(PARAM_RESULT_CODE, resultCode);

            Message msg = Message.obtain();
            msg.what = WHAT_LIST_OBJECTS;
            msg.setData(bundle);
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int resultCode, String objectTypeName, long id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putLong(PARAM_OBJECT_ID, id);
            bundle.putInt(PARAM_RESULT_CODE, resultCode);

            Message msg = Message.obtain();
            msg.what = WHAT_DELETE_OBJECT;
            msg.setData(bundle);
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*-**********************************-*/
    /*-              Plumbing            -*/
    /*-**********************************-*/

    private void connectReceiver() {
        try {
            Message msg = Message.obtain();
            msg.what = WHAT_REPLY_SERVICE;
            msg.replyTo = _rcvService;
            _sndService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private final ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _sndService = new Messenger(service);
            _isConnected = true;
            connectReceiver();
            if (_listener != null)
                _listener.onConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            _rcvService = null;
            _isConnected = false;
            if (_listener != null)
                _listener.onDisconnected();
        }
    };

    private static class IncomeHandler extends Handler {
        private WeakReference<ObjectStoreClient> _client;

        public IncomeHandler(ObjectStoreClient client) {
            _client = new WeakReference<>(client);
        }

        @Override
        public void handleMessage(Message msg) {
            ObjectStoreClient client = _client.get();
            if (client == null) {
                super.handleMessage(msg);
                return;
            }

            if (client._listener == null) {
                super.handleMessage(msg);
                return;
            }

            switch (msg.what) {
                case WHAT_DELETE_OBJECT:
                    client._listener.onDelete();
                    break;
                case WHAT_PUT_OBJECT:
                    client._listener.onPut();
                    break;
                case WHAT_GET_OBJECT:
                    client._listener.onGet();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public interface Listener {

        public void onConnected();

        public void onDisconnected();

        public void onDelete();

        public void onPut();

        public void onGet();

        public void onList();
    }
}
