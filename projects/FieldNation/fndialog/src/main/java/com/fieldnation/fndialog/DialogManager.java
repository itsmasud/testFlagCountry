package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ContextProvider;

import java.util.Hashtable;
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
    private Hashtable<String, DialogHolder> _holderLookup = new Hashtable<>();


    private final int STATE_UNKNOWN = 0;
    private final int STATE_START = 1;
    private final int STATE_RESUME = 2;
    private final int STATE_PAUSED = 3;
    private final int STATE_STOP = 4;

    private int _lastState = STATE_UNKNOWN;

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
                String uid = bundle.getString("uid");

                DialogHolder holder = makeDialogHolder(className, classLoader);
                if (holder != null) {
                    holder.params = params;
                    holder.savedState = dialogSavedState;
                    holder.uid = uid;
                    push(holder);
                }
            }
        }
    }

    // adds a new dialog to the stack
    private void push(DialogHolder dialogHolder) {
        Log.v(TAG, "push");
        // add the new dialog to the stack
        _dialogStack.add(0, dialogHolder);
        if (dialogHolder.uid != null)
            _holderLookup.put(dialogHolder.uid, dialogHolder);
        // add it to the container
        addView(dialogHolder.dialog.getView());

        // move to correct state
        if (_lastState == STATE_START)
            dialogHolder.dialog.onStart();
        else if (_lastState == STATE_RESUME) {
            dialogHolder.dialog.onStart();
            dialogHolder.dialog.onResume();
        }

        // call show
        dialogHolder.dialog.show(dialogHolder.params, true);
        // call restoreDialogState
        if (dialogHolder.savedState != null)
            dialogHolder.dialog.onRestoreDialogState(dialogHolder.savedState);
    }

    // starts the pop process
    private void pop() {
        Log.v(TAG, "pop");
        if (_dialogStack.size() > 0) {
            // remove current view from the stack
            DialogHolder dh = _dialogStack.get(0);
            // remove from the container
            dh.dialog.dismiss(true);
        }
    }

    private void remove(Dialog dialog) {
        Log.v(TAG, "remove");
        // find the holder
        DialogHolder holder = null;
        for (int i = 0; i < _dialogStack.size(); i++) {
            if (_dialogStack.get(i).dialog.equals(dialog)) {
                holder = _dialogStack.get(i);
                if (holder.uid != null)
                    _holderLookup.remove(holder.uid);

                _dialogStack.remove(i);
                break;
            }
        }

        // remove the view
        if (holder != null) {
            // move the dialog into the correct state
            holder.dialog.onPause();
            holder.dialog.onStop();

            // remove from the tree
            removeView(holder.dialog.getView());
        }
    }

    /**
     * Should be called tby the activity that owns this view
     *
     * @return true if the button event was handled, false if not
     */
    public boolean onBackPressed() {
        Log.v(TAG, "onBackPressed");
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

    public void onStart() {
        Log.v(TAG, "onStart");

        _lastState = STATE_START;
        for (DialogHolder holder : _dialogStack) {
            holder.dialog.onStart();
        }
    }

    public void onResume() {
        Log.v(TAG, "onResume");
        _lastState = STATE_RESUME;
        if (_dialogReceiver != null && _dialogReceiver.isConnected()) {
            _dialogReceiver.disconnect(ContextProvider.get());
        }
        _dialogReceiver = new Server(_dialogReceiver_listener);
        _dialogReceiver.connect(ContextProvider.get());

        for (DialogHolder holder : _dialogStack) {
            holder.dialog.onResume();
        }
    }

    public void onPause() {
        Log.v(TAG, "onPause");
        _lastState = STATE_PAUSED;
        for (DialogHolder holder : _dialogStack) {
            holder.dialog.onPause();
        }

        if (_dialogReceiver != null && _dialogReceiver.isConnected()) {
            _dialogReceiver.disconnect(ContextProvider.get());
        }
    }

    public void onStop() {
        Log.v(TAG, "onStop");
        _lastState = STATE_STOP;
        for (DialogHolder holder : _dialogStack) {
            holder.dialog.onStop();
        }
    }

    private DialogHolder makeDialogHolder(String className, ClassLoader classLoader) {
        Log.v(TAG, "makeDialogHolder");
        try {
            Class<?> clazz = classLoader.loadClass(className);

            Object object = clazz.getConstructor(Context.class, ViewGroup.class).newInstance(getContext(), this);
            if (!(object instanceof Dialog)) {
                return null;
            }

            Dialog dialog = (Dialog) object;
            dialog.setDismissListener(_dismissListener);
            dialog.setResultListener(_resultListener);
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
        Log.v(TAG, "onDetachedFromWindow");
        if (_dialogReceiver != null && _dialogReceiver.isConnected()) {
            _dialogReceiver.disconnect(ContextProvider.get());
        }

        removeAllViews();
        super.onDetachedFromWindow();
    }

    private Server.Listener _dialogReceiver_listener = new Server.Listener() {
        @Override
        public Server getClient() {
            return _dialogReceiver;
        }

        @Override
        public void onShowDialog(String uid, String className, ClassLoader classLoader, Bundle params) {
            try {
                DialogHolder holder = makeDialogHolder(className, classLoader);

                if (holder != null) {
                    holder.params = params;
                    holder.uid = uid;
                    push(holder);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onDismissDialog(String uid) {
            if (uid != null && _holderLookup.containsKey(uid)) {
                _holderLookup.get(uid).dialog.dismiss(true);
            }
        }
    };

    private final Dialog.DismissListener _dismissListener = new Dialog.DismissListener() {
        @Override
        public void onDismissed(Dialog dialog) {
            remove(dialog);
        }
    };

    private final Dialog.ResultListener _resultListener = new Dialog.ResultListener() {
        @Override
        public void onResult(Dialog dialog, Bundle response) {
            for (int i = 0; i < _dialogStack.size(); i++) {
                DialogHolder dh = _dialogStack.get(i);
                if (dh.dialog.equals(dialog)) {
                    Client.dialogResult(getContext(), dh.uid, dialog, response);
                    return;
                }
            }
        }
    };


    private static class DialogHolder {
        public Dialog dialog;
        public Bundle params;
        public String uid;
        public Bundle savedState;

        DialogHolder(Dialog dialog) {
            this.dialog = dialog;
        }

        Bundle saveState() {
            Log.v(TAG, "saveState");
            Bundle savedState = new Bundle();
            Bundle dialogState = new Bundle();
            dialog.onSaveDialogState(dialogState);
            savedState.putParcelable("savedState", dialogState);
            savedState.putString("className", dialog.getClass().getName());
            savedState.setClassLoader(dialog.getClass().getClassLoader());
            savedState.putBundle("params", params);
            savedState.putString("uid", uid);
            return savedState;
        }
    }
}
