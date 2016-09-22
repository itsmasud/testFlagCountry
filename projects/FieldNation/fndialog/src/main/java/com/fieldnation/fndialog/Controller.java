package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Michael on 9/19/2016.
 */
public abstract class Controller {
    private static final String TAG = "Controller";

    private Client _client;
    private Class<? extends Dialog> _klass;
    private Listener _listener;

    public Controller(Context context, Class<? extends Dialog> klass) {
        _klass = klass;
        _client = new Client(_dialog_listener);
        _client.connect(context);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void disconnect(Context context) {
        if (_client != null && _client.isConnected())
            _client.disconnect(context);
    }

    public static void show(Context context, Class<? extends Dialog> klass, Bundle params) {
        Client.show(context, klass, params);
    }

    public static void dismiss(Context context, Class<? extends Dialog> klass) {
        Client.dismiss(context, klass);
    }

    private final Client.Listener _dialog_listener = new Client.Listener() {
        @Override
        public Class<? extends Dialog> getDialogClass() {
            return _klass;
        }

        @Override
        public Client getClient() {
            return _client;
        }

        @Override
        public void onComplete(Bundle response) {
            if (_listener != null)
                _listener.onComplete(response);
        }
    };

    public interface Listener {
        void onComplete(Bundle response);
    }
}
