package com.fieldnation.service.objectstore;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

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

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public ObjectStoreClient(Listener listener) {
        _listener = listener;
    }

    public void connect(Context context) {
        context.bindService(new Intent(context, ObjectStoreService.class), _serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnect(Context context) {
        context.unbindService(_serviceConnection);
    }

    public boolean isConnected() {
        return _isConnected;
    }

    /*-*****************************-*/
    /*-         Commands            -*/
    /*-*****************************-*/

    public boolean put(String objectTypeName, String key, String data) {
        return put(objectTypeName, key, data.getBytes());
    }

    public boolean put(String objectTypeName, String key, byte[] data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putString(PARAM_OBJECT_KEY, key);
            bundle.putBoolean(PARAM_IS_FILE, false);
            bundle.putByteArray(PARAM_DATA, data);

            Message msg = Message.obtain();
            msg.what = WHAT_PUT_OBJECT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean put(String objectTypeName, String key, File file) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putString(PARAM_OBJECT_KEY, key);
            bundle.putBoolean(PARAM_IS_FILE, true);
            bundle.putSerializable(PARAM_FILE, file);

            Message msg = Message.obtain();
            msg.what = WHAT_PUT_OBJECT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean put(StoredObject obj) {
        try {
            Bundle bundle = obj.toBundle();

            Message msg = Message.obtain();
            msg.what = WHAT_PUT_OBJECT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean get(String objectTypeName, String objectKey) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putString(PARAM_OBJECT_KEY, objectKey);

            Message msg = Message.obtain();
            msg.what = WHAT_GET_OBJECT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean get(long id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putLong(PARAM_ID, id);

            Message msg = Message.obtain();
            msg.what = WHAT_GET_OBJECT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean list(String objectTypeName) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);

            Message msg = Message.obtain();
            msg.what = WHAT_LIST_OBJECTS;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean list(String objectTypeName, String[] keys) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putStringArray(PARAM_OBJECT_KEY, keys);

            Message msg = Message.obtain();
            msg.what = WHAT_LIST_OBJECTS;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(String objectTypeName, String objectKey) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_OBJECT_TYPE, objectTypeName);
            bundle.putString(PARAM_OBJECT_KEY, objectKey);

            Message msg = Message.obtain();
            msg.what = WHAT_DELETE_OBJECT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(long id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putLong(PARAM_ID, id);

            Message msg = Message.obtain();
            msg.what = WHAT_DELETE_OBJECT;
            msg.setData(bundle);
            msg.replyTo = _rcvService;
            _sndService.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // callbacks
    private void handleDelete(Message msg) {
        Bundle bundle = msg.getData();
        _listener.onDelete(
                msg.arg1 == 1,
                bundle.getString(PARAM_OBJECT_TYPE),
                bundle.getString(PARAM_OBJECT_KEY));
    }

    private void handlePut(Message msg) {
        Bundle bundle = msg.getData();
        StoredObject obj = null;
        if (bundle != null)
            obj = new StoredObject(bundle);
        _listener.onPut(obj);
    }

    private void handleGet(Message msg) {
        Bundle bundle = msg.getData();
        StoredObject obj = null;
        if (bundle != null)
            obj = new StoredObject(bundle);

        _listener.onGet(obj);
    }

    private void handleList(Message msg) {
        Bundle bundle = msg.getData();
        StoredObject[] objects = null;
        if (bundle != null) {
            objects = (StoredObject[]) bundle.get(PARAM_OBJECT_LIST);
        }

        _listener.onList(objects);
    }

    /*-**********************************-*/
    /*-              Plumbing            -*/
    /*-**********************************-*/
    private final ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _sndService = new Messenger(service);
            _isConnected = true;
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
                    client.handleDelete(msg);
                    break;
                case WHAT_PUT_OBJECT:
                    client.handlePut(msg);
                    break;
                case WHAT_GET_OBJECT:
                    client.handleGet(msg);
                    break;
                case WHAT_LIST_OBJECTS:
                    client.handleList(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public interface Listener {
        void onConnected();

        void onDisconnected();

        void onDelete(boolean success, String objectName, String objectKey);

        void onPut(StoredObject obj);

        void onGet(StoredObject obj);

        void onList(StoredObject[] objects);
    }
}
