package com.fieldnation.service.transaction;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import java.lang.ref.WeakReference;

/**
 * Created by Michael Carver on 2/27/2015.
 */
public class TransactionClient implements TransactionConstants {

    public static final String TAG = "ObjectStoreClient";

    private boolean _isConnected = false;
    private Messenger _rcvService = new Messenger(new IncomeHandler(this));
    private Messenger _sndService = null;
    private Listener _listener;

    public TransactionClient(Listener listener) {
        _listener = listener;
    }

    public void connect(Activity activity) {
        activity.bindService(new Intent(activity, TransactionService.class), _serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnect(Activity activity) {
        activity.unbindService(_serviceConnection);
    }

    public boolean isConnected() {
        return _isConnected;
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
        private WeakReference<TransactionClient> _client;

        public IncomeHandler(TransactionClient client) {
            _client = new WeakReference<>(client);
        }

        @Override
        public void handleMessage(Message msg) {
            TransactionClient client = _client.get();
            if (client == null) {
                super.handleMessage(msg);
                return;
            }

            if (client._listener == null) {
                super.handleMessage(msg);
                return;
            }

//            switch (msg.what) {
//                case WHAT_DELETE_OBJECT:
//                    client.handleDelete(msg);
//                    break;
//                case WHAT_PUT_OBJECT:
//                    client.handlePut(msg);
//                    break;
//                case WHAT_GET_OBJECT:
//                    client.handleGet(msg);
//                    break;
//                case WHAT_LIST_OBJECTS:
//                    client.handleList(msg);
//                    break;
//            }
            super.handleMessage(msg);
        }
    }

    public interface Listener {

        public void onConnected();

        public void onDisconnected();
    }


}
