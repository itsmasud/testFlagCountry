package com.fieldnation.fndialog;

import android.os.Bundle;

import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

/**
 * Created by Michael on 9/6/2016.
 * <p/>
 * This is the base class for any client that wants to show dialogs and receive their events.
 */
abstract class Client extends Pigeon implements Constants {
    private static final String TAG = "Client";


    public static void show(String uid, String className, ClassLoader classLoader, Bundle params) {
        Bundle message = new Bundle();
        message.putString(PARAM_DIALOG_CLASS_NAME, className);
        message.putBundle(PARAM_DIALOG_PARAMS, params);
        message.putString(PARAM_DIALOG_UID, uid);
        message.setClassLoader(classLoader);

        PigeonRoost.sendMessage(ADDRESS_SHOW_DIALOG, message, Sticky.TEMP);
    }

    public static void show(String uid, Class<? extends Dialog> klass, Bundle params) {
        show(uid, klass.getName(), klass.getClassLoader(), params);
    }

    // uid cannot be null when calling this method
    public static void dismiss(String uid) {
        Bundle message = new Bundle();
        message.putString(PARAM_DIALOG_UID, uid);

        PigeonRoost.sendMessage(ADDRESS_DISMISS_DIALOG, message, Sticky.TEMP);
    }

    public void sub() {
        PigeonRoost.sub(this, ADDRESS_DIALOG_COMPLETE + "/" + getDialogClass().getName());
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_DIALOG_COMPLETE + "/" + getDialogClass().getName());
    }

    abstract Class<? extends Dialog> getDialogClass();

    abstract String getUid();

    @Override
    public void onMessage(String address, Object message) {
        Bundle bundle = (Bundle) message;

        if (getUid() != null && bundle.containsKey(PARAM_DIALOG_UID) && bundle.getString(PARAM_DIALOG_UID) != null) {
            String uid = bundle.getString(PARAM_DIALOG_UID);
            if (!uid.equals(getUid()))
                return;
        }

        if (address.startsWith(ADDRESS_DIALOG_COMPLETE)) {
            onComplete(bundle.getBundle(PARAM_DIALOG_RESPONSE));
        }
    }

    abstract void onComplete(Bundle response);
}
