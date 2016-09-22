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
public class DialogManager extends FrameLayout implements Constants {
    private static final String TAG = "DialogManager";

    // Service
    private Server _dialogReceiver;

    // Stores the instantiated dialogs
    private DialogHolder _lastDialog = null;
    private Hashtable<String, DialogHolder> _dialogs = new Hashtable<>();

    public DialogManager(Context context) {
        super(context);
        init();
    }

    public DialogManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialogManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    /**
     * Saves the state of the last displayed dialog. We don't keep the non-visible dialogs
     *
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Log.v(TAG, "onSaveInstanceState");

        Bundle savedInstance = new Bundle();
        savedInstance.putParcelable("super", super.onSaveInstanceState());
        if (_lastDialog != null && _lastDialog.dialog.getView().getVisibility() == VISIBLE)
            savedInstance.putBundle("lastDialog", _lastDialog.saveState());

        return savedInstance;
    }

    /**
     * Should be called tby the activity that owns this view
     *
     * @return true if the button event was handled, false if not
     */
    public boolean onBackPressed() {
        if (_lastDialog != null) {
            if (_lastDialog.dialog.isCancelable() && _lastDialog.dialog.getView().getVisibility() == VISIBLE) {
                _lastDialog.dialog.cancel();
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.v(TAG, "onRestoreInstanceState");
        Bundle savedInstance = (Bundle) state;
        super.onRestoreInstanceState(savedInstance.getParcelable("super"));

        if (savedInstance.containsKey("lastDialog")) {
            Bundle lastDialogState = savedInstance.getBundle("lastDialog");
            Parcelable dialogSavedState = lastDialogState.getParcelable("savedState");
            String className = lastDialogState.getString("className");
            ClassLoader classLoader = lastDialogState.getClassLoader();
            Bundle params = lastDialogState.getBundle("params");

            DialogHolder holder = getDialogHolder(className, classLoader);
            if (holder != null) {
                holder.params = params;
                _lastDialog = holder;
                _dialogs.put(className, holder);
                holder.dialog.show(params, false);
                if (dialogSavedState != null)
                    holder.dialog.onRestoreDialogState(dialogSavedState);
            }
        }
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

    private DialogHolder getDialogHolder(String className, ClassLoader classLoader) {
        try {
            Class<?> clazz = classLoader.loadClass(className);

            // check if we have it already, if so, update the params, and return
            if (_dialogs.containsKey(className)) {
                return _dialogs.get(className);
            }

            Object object = clazz.getConstructor(Context.class).newInstance(getContext());
            if (!(object instanceof Dialog)) {
                return null;
            }

            Dialog dialog = (Dialog) object;
            addView(dialog.getView());
            dialog.getView().setVisibility(GONE);

            return new DialogHolder(dialog);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_dialogReceiver != null && _dialogReceiver.isConnected()) {
            _dialogReceiver.disconnect(ContextProvider.get());
        }
        super.onDetachedFromWindow();
    }

    private Server.Listener _dialogReceiver_listener = new Server.Listener() {
        @Override
        public Server getClient() {
            return _dialogReceiver;
        }

        @Override
        public void onShowDialog(String className, ClassLoader classLoader, Bundle params) {
            try {
                DialogHolder holder = getDialogHolder(className, classLoader);

                if (holder != null) {
                    holder.dialog.show(params, true);
                    holder.params = params;
                    _lastDialog = holder;
                    _dialogs.put(className, _lastDialog);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onDismissDialog(String className) {
            if (_dialogs.containsKey(className)) {
                _dialogs.get(className).dialog.dismiss(true);
            }
        }
    };

    private static class DialogHolder {
        public Dialog dialog;
        public Bundle params;

        DialogHolder(Dialog dialog) {
            this.dialog = dialog;
        }

        Bundle saveState() {
            Bundle savedState = new Bundle();
            savedState.putParcelable("savedState", dialog.onSaveDialogState());
            savedState.putString("className", dialog.getClass().getName());
            savedState.setClassLoader(dialog.getClass().getClassLoader());
            savedState.putBundle("params", params);
            return savedState;
        }
    }
}
