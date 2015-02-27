package com.fieldnation.service.objectstore;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Michael Carver on 2/26/2015.
 * <p/>
 * This service provides an interface to the database to other processes
 */
class ObjectStoreService extends Service implements ObjectStoreConstants {

    private Messenger _me = new Messenger(new IncomeHandler(this));

    @Override
    public IBinder onBind(Intent intent) {
        return _me.getBinder();
    }

    private void deleteObject(Bundle bundle, Messenger replyTo) {
        boolean success = false;
        if (bundle.containsKey(PARAM_ID)) {
            long id = bundle.getLong(PARAM_ID);
            success = StoredObject.delete(this, id);

        } else if (bundle.containsKey(PARAM_OBJECT_TYPE) && bundle.containsKey(PARAM_OBJECT_KEY)) {
            String objName = bundle.getString(PARAM_OBJECT_TYPE);
            String key = bundle.getString(PARAM_OBJECT_KEY);
            success = StoredObject.delete(this, objName, key);
        }

        try {
            if (replyTo != null) {
                Message msg = Message.obtain();
                msg.what = WHAT_DELETE_OBJECT;
                msg.arg1 = success ? 1 : 0;
                msg.setData(bundle);
                replyTo.send(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getObject(Bundle bundle, Messenger replyTo) {
        StoredObject obj = null;
        if (bundle.containsKey(PARAM_ID)) {
            long id = bundle.getLong(PARAM_ID);
            obj = StoredObject.get(this, id);
        } else if (bundle.containsKey(PARAM_OBJECT_TYPE) && bundle.containsKey(PARAM_OBJECT_KEY)) {
            String objType = bundle.getString(PARAM_OBJECT_TYPE);
            String key = bundle.getString(PARAM_OBJECT_KEY);
            obj = StoredObject.get(this, objType, key);
        }

        try {
            if (replyTo != null) {
                Message msg = Message.obtain();
                msg.what = WHAT_GET_OBJECT;
                if (obj != null) {
                    msg.setData(obj.toBundle());
                }
                replyTo.send(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void putObject(Bundle bundle, Messenger replyTo) {
        StoredObject obj = null;
        if (bundle.containsKey(PARAM_ID)) {
            obj = new StoredObject(bundle);
            obj.save(this);
        } else if (bundle.getBoolean(PARAM_IS_FILE)) {
            obj = StoredObject.put(this,
                    bundle.getString(PARAM_OBJECT_TYPE),
                    bundle.getString(PARAM_OBJECT_KEY),
                    bundle.getByteArray(PARAM_META_DATA),
                    (File) bundle.getSerializable(PARAM_FILE));
        } else {
            obj = StoredObject.put(this,
                    bundle.getString(PARAM_OBJECT_TYPE),
                    bundle.getString(PARAM_OBJECT_KEY),
                    bundle.getByteArray(PARAM_META_DATA),
                    bundle.getByteArray(PARAM_DATA));
        }
        try {
            if (replyTo != null) {
                Message msg = Message.obtain();
                msg.what = WHAT_PUT_OBJECT;
                if (obj != null) {
                    msg.setData(obj.toBundle());
                }
                replyTo.send(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void listObjects(Bundle bundle, Messenger replyTo) {
        List<StoredObject> list = null;

        String objName = bundle.getString(PARAM_OBJECT_TYPE);
        if (bundle.containsKey(PARAM_OBJECT_KEY)) {
            String[] keys = bundle.getStringArray(PARAM_OBJECT_KEY);
            list = StoredObject.list(this, objName, keys);
        } else {
            list = StoredObject.list(this, objName);
        }

        try {
            if (replyTo != null) {
                Parcelable[] parcels = new Parcelable[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    parcels[i] = list.get(i);
                }

                Bundle output = new Bundle();
                output.putParcelableArray(PARAM_OBJECT_LIST, parcels);
                Message msg = Message.obtain();
                msg.what = WHAT_LIST_OBJECTS;
                msg.setData(output);

                replyTo.send(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /*-**********************************-*/
    /*-              Plumbing            -*/
    /*-**********************************-*/
    private static class IncomeHandler extends Handler {
        private WeakReference<ObjectStoreService> _oss;

        public IncomeHandler(ObjectStoreService oss) {
            _oss = new WeakReference<>(oss);
        }

        @Override
        public void handleMessage(Message msg) {
            ObjectStoreService svc = _oss.get();
            if (svc == null) {
                super.handleMessage(msg);
                return;
            }

            switch (msg.what) {
                case WHAT_DELETE_OBJECT:
                    svc.deleteObject(msg.getData(), msg.replyTo);
                    break;
                case WHAT_GET_OBJECT:
                    svc.getObject(msg.getData(), msg.replyTo);
                    break;
                case WHAT_PUT_OBJECT:
                    svc.putObject(msg.getData(), msg.replyTo);
                    break;
                case WHAT_LIST_OBJECTS:
                    svc.listObjects(msg.getData(), msg.replyTo);
                    break;
            }
            super.handleMessage(msg);
        }
    }


}
