package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ContextProvider;

import java.util.LinkedList;
import java.util.List;

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
    private List<DialogHolder> _dialogStack = new LinkedList<>();

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
        if (_dialogStack.size() > 0) {
            Bundle[] bundles = new Bundle[_dialogStack.size()];
            for (int i = 0; i < _dialogStack.size(); i++) {
                bundles[i] = _dialogStack.get(i).saveState();
            }
            savedInstance.putParcelableArray("dialogs", bundles);
        }
        return savedInstance;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.v(TAG, "onRestoreInstanceState");
        Bundle savedInstance = (Bundle) state;
        super.onRestoreInstanceState(savedInstance.getParcelable("super"));

        if (savedInstance.containsKey("dialogs")) {
            Parcelable[] bundles = savedInstance.getParcelableArray("dialogs");

            for (int i = 0; i < bundles.length; i++) {
                Bundle bundle = (Bundle) bundles[i];
                Parcelable dialogSavedState = bundle.getParcelable("savedState");
                String className = bundle.getString("className");
                ClassLoader classLoader = bundle.getClassLoader();
                Bundle params = bundle.getBundle("params");

                DialogHolder holder = getDialogHolder(className, classLoader);
                if (holder != null) {
                    holder.params = params;
                    holder.savedState = dialogSavedState;
                }
            }

            if (_dialogStack.size() > 0) {
                showDialog(_dialogStack.get(_dialogStack.size() - 1));
            }
        }
    }

    private void showDialog(DialogHolder dialogHolder) {
        dialogHolder.dialog.show(dialogHolder.params, false);
        if (dialogHolder.savedState != null)
            dialogHolder.dialog.onRestoreDialogState(dialogHolder.savedState);

        dialogHolder.dialog.setDismissListener(_dismissListener);
        _dialogStack.add(dialogHolder);
    }

    /**
     * Should be called tby the activity that owns this view
     *
     * @return true if the button event was handled, false if not
     */
    public boolean onBackPressed() {
        if (_dialogStack.size() > 0) {
            DialogHolder dh = _dialogStack.get(_dialogStack.size() - 1);
            if (dh.dialog.isCancelable() && dh.dialog.getView().getVisibility() == VISIBLE) {
                dh.dialog.cancel();
                _dialogStack.remove(_dialogStack.size() - 1);
                // TODO need to remove the view from the stack
                if (_dialogStack.size() > 0) {
                    showDialog(_dialogStack.get(_dialogStack.size() - 1));
                }
                return true;
            }
        }
        return false;
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

            Object object = clazz.getConstructor(Context.class, ViewGroup.class).newInstance(getContext(), this);
            if (!(object instanceof Dialog)) {
                return null;
            }

            Dialog dialog = (Dialog) object;
            addView(dialog.getView());
            dialog.getView().setVisibility(GONE);
            dialog.onAdded();

            DialogHolder holder = new DialogHolder(dialog);

            return holder;
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
                    holder.params = params;
                    showDialog(holder);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onDismissDialog(String className) {
//            if (_dialogs.containsKey(className)) {
//                _dialogs.get(className).dialog.dismiss(true);
//            }
        }
    };

    private final Dialog.DismissListener _dismissListener = new Dialog.DismissListener() {
        @Override
        public void onDismissed(Dialog dialog) {
            for (int i = 0; i < _dialogStack.size(); i++) {
                DialogHolder holder = _dialogStack.get(i);
                if (holder.dialog == dialog) {
                    _dialogStack.remove(i);
                    removeView(holder.dialog.getView());
                }
            }
        }
    };

    private static class DialogHolder {
        public Dialog dialog;
        public Bundle params;

        public Parcelable savedState;

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
