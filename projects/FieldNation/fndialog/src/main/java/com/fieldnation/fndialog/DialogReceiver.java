package com.fieldnation.fndialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.fieldnation.fnlog.Log;

import java.util.Hashtable;

/**
 * Created by Michael on 9/6/2016.
 */
public class DialogReceiver extends FrameLayout implements DialogConstants {
    private static final String TAG = "DialogReceiver";

    private Hashtable<String, Dialog> _dialogs = new Hashtable<>();

    public DialogReceiver(Context context) {
        super(context);
    }

    public DialogReceiver(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogReceiver(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SHOW_DIALOG);
        intentFilter.addAction(ACTION_DISMISS_DIALOG);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(_broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(_broadcastReceiver);
    }

    private BroadcastReceiver _broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_SHOW_DIALOG:
                    showDialog(intent);
                    break;
                case ACTION_DISMISS_DIALOG:
                    hideDialog(intent);
                    break;
                default:
                    break;
            }
        }
    };

    private void showDialog(Intent intent) {
        try {
            Bundle command = intent.getExtras();

            String className = command.getString(PARAM_DIALOG_CLASS_NAME);
            Class<?> clazz = command.getClassLoader().loadClass(className);

            if (_dialogs.containsKey(className)) {
                _dialogs.get(className).onShow(command.getBundle(PARAM_DIALOG_PAYLOAD));
                return;
            }

            Object object = clazz.getConstructor(Context.class).newInstance(getContext());

            if (!(object instanceof View) || !(object instanceof Dialog)) {
                return;
            }
            ((Dialog) object).onShow(command.getBundle(PARAM_DIALOG_PAYLOAD));

            _dialogs.put(className, (Dialog) object);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private void hideDialog(Intent intent) {
        Bundle command = intent.getExtras();

        String className = command.getString(PARAM_DIALOG_CLASS_NAME);

        if (_dialogs.containsKey(className)) {
            _dialogs.get(className).onDismiss();
        }
    }
}
