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
                bundles[_dialogStack.size() - i - 1] = _dialogStack.get(i).saveState();
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
                Bundle dialogSavedState = bundle.getBundle("savedState");
                String className = bundle.getString("className");
                ClassLoader classLoader = bundle.getClassLoader();
                Bundle params = bundle.getBundle("params");

                DialogHolder holder = makeDialogHolder(className, classLoader);
                if (holder != null) {
                    holder.params = params;
                    holder.savedState = dialogSavedState;
                    push(holder);
                }
            }
        }
    }

    // adds a new dialog to the stack
    private void push(DialogHolder dialogHolder) {
        // add the new dialog to the stack
        _dialogStack.add(0, dialogHolder);
        // add it to the container
        addView(dialogHolder.dialog.getView());
        dialogHolder.dialog.onAdded();
        // call show
        dialogHolder.dialog.show(dialogHolder.params, true);
        // call restoreDialogState
        if (dialogHolder.savedState != null)
            dialogHolder.dialog.onRestoreDialogState(dialogHolder.savedState);
    }

    // starts the pop process
    private void pop() {
        if (_dialogStack.size() > 0) {
            // remove current view from the stack
            DialogHolder dh = _dialogStack.get(0);
            // remove from the container
            dh.dialog.dismiss(true);
        }

    }

    private void remove(Dialog dialog) {
        // find the holder
        DialogHolder holder = null;
        for (int i = 0; i < _dialogStack.size(); i++) {
            if (_dialogStack.get(i).dialog.equals(dialog)) {
                holder = _dialogStack.get(i);
                _dialogStack.remove(i);
                break;
            }
        }

        // remove the view
        if (holder != null) {
            removeView(holder.dialog.getView());
        }

        // find the top view
        if (_dialogStack.size() > 0) {
            DialogHolder dh = _dialogStack.get(0);
            dh.dialog.show(dh.params, false);
        }
    }

    /**
     * Should be called tby the activity that owns this view
     *
     * @return true if the button event was handled, false if not
     */
    public boolean onBackPressed() {
        if (_dialogStack.size() > 0) {
            DialogHolder dh = _dialogStack.get(0);
            if (dh.dialog.isCancelable() && dh.dialog.getView().getVisibility() == VISIBLE) {
                dh.dialog.cancel();
                pop();
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

    private DialogHolder makeDialogHolder(String className, ClassLoader classLoader) {
        try {
            Class<?> clazz = classLoader.loadClass(className);

            Object object = clazz.getConstructor(Context.class, ViewGroup.class).newInstance(getContext(), this);
            if (!(object instanceof Dialog)) {
                return null;
            }

            Dialog dialog = (Dialog) object;
            dialog.setDismissListener(_dismissListener);
            dialog.getView().setVisibility(GONE);
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
                DialogHolder holder = makeDialogHolder(className, classLoader);

                if (holder != null) {
                    holder.params = params;
                    push(holder);
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
            remove(dialog);
        }
    };

    private static class DialogHolder {
        public Dialog dialog;
        public Bundle params;

        public Bundle savedState;

        DialogHolder(Dialog dialog) {
            this.dialog = dialog;
        }

        Bundle saveState() {
            Bundle savedState = new Bundle();
            Bundle dialogState = new Bundle();
            dialog.onSaveDialogState(dialogState);
            savedState.putParcelable("savedState", dialogState);
            savedState.putString("className", dialog.getClass().getName());
            savedState.setClassLoader(dialog.getClass().getClassLoader());
            savedState.putBundle("params", params);
            return savedState;
        }
    }
}
