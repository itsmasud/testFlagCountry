package com.fieldnation.fndialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Michael on 9/6/2016.
 */
public abstract class DialogClient implements DialogConstants {

    public DialogClient() {
    }

    public void register(Context context) {
        IntentFilter intentFilter = new IntentFilter(ACTION_DIALOG_COMPLETE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(context).registerReceiver(_receiver, intentFilter);
    }

    public void unregister(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(_receiver);
    }

    public void show(Context context, Bundle payload) {
        Intent intent = new Intent(ACTION_SHOW_DIALOG);
        intent.putExtra(PARAM_DIALOG_CLASS_NAME, this.getClass().getName());
        intent.putExtra(PARAM_DIALOG_PAYLOAD, payload);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void dismuss(Context context) {
        Intent intent = new Intent(ACTION_DISMISS_DIALOG);
        intent.putExtra(PARAM_DIALOG_CLASS_NAME, this.getClass().getName());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private BroadcastReceiver _receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}
