package com.fieldnation.service.objectstore;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import java.lang.ref.WeakReference;

/**
 * Created by Michael Carver on 2/26/2015.
 * <p/>
 * This service provides an interface to the database to other processes
 */
public class ObjectStoreService extends Service implements ObjectStoreConstants {

    private Messenger _client = null;
    private Messenger _me = new Messenger(new IncomeHandler(this));

    @Override
    public IBinder onBind(Intent intent) {
        return _me.getBinder();
    }

    private void deleteObject(Bundle bundle) {

    }

    private void getObject(Bundle bundle) {

    }

    private void putObject(Bundle bundle) {

    }

    private void listObjects(Bundle bundle) {

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
                case WHAT_REPLY_SERVICE:
                    svc._client = msg.replyTo;
                    break;
                case WHAT_DELETE_OBJECT:
                    svc.deleteObject(msg.getData());
                    break;
                case WHAT_GET_OBJECT:
                    svc.getObject(msg.getData());
                    break;
                case WHAT_PUT_OBJECT:
                    svc.putObject(msg.getData());
                    break;
                case WHAT_LIST_OBJECTS:
                    svc.listObjects(msg.getData());
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
