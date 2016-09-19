package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ContextProvider;

import java.util.Hashtable;

/**
 * Created by Michael on 9/6/2016.
 * <p/>
 * This class is the heart of the dialog system it has the following responsibilities
 * # Receive dialog show and cancel requests
 * # Instantiate dialogs as needed and manage their state
 */
public class DialogReceiver extends FrameLayout implements Constants {
    private static final String TAG = "DialogReceiver";

    // Service
    private Server _dialogReceiver;

    // Stores the instantiated dialogs
    private Hashtable<String, Dialog> _dialogs = new Hashtable<>();

    public DialogReceiver(Context context) {
        super(context);
        init();
    }

    public DialogReceiver(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialogReceiver(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Log.v(TAG, "onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.v(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (_dialogReceiver != null && _dialogReceiver.isConnected()) {
            _dialogReceiver.disconnect(ContextProvider.get());
        }

        _dialogReceiver = new Server(_dialogReceiver_listener);
        _dialogReceiver.connect(ContextProvider.get());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (_dialogReceiver != null && _dialogReceiver.isConnected()) {
            _dialogReceiver.disconnect(ContextProvider.get());
        }
    }

    private Server.Listener _dialogReceiver_listener = new Server.Listener() {
        @Override
        public Server getClient() {
            return _dialogReceiver;
        }

        @Override
        public void onShowDialog(String className, ClassLoader classLoader, Bundle params) {
            try {
                Class<?> clazz = classLoader.loadClass(className);

                if (_dialogs.containsKey(className)) {
                    _dialogs.get(className).show(params);
                    return;
                }

                Object object = clazz.getConstructor(Context.class).newInstance(getContext());

                if (!(object instanceof Dialog)) {
                    return;
                }

                Dialog dialog = (Dialog) object;

                addView(dialog.getView());
                dialog.getView().setVisibility(GONE);

                ((Dialog) object).show(params);

                _dialogs.put(className, (Dialog) object);

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onDismissDialog(String className) {
            if (_dialogs.containsKey(className)) {
                Dialog v = _dialogs.get(className);
                v.dismiss();
                // Todo need to hide self, once dialog is done with its animation
            }
        }
    };

    private static class DialogHolder {
        public Dialog dialog;
        public Bundle params;

        public DialogHolder(Dialog dialog, Bundle params) {
            this.dialog = dialog;
            this.params = params;
        }

        public Bundle saveState() {
            Bundle savedState = new Bundle();
            savedState.putParcelable("savedState", dialog.onSaveDialogState());
            savedState.putString("className", dialog.getClass().getName());
            savedState.setClassLoader(dialog.getClass().getClassLoader());
            savedState.putBundle("params", params);
            return savedState;
        }
    }
}
