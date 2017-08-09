package com.fieldnation.fndialog;

import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;

/**
 * Created by Michael on 9/19/2016.
 */
abstract class Server extends Pigeon implements Constants {
    private static final String TAG = "Server";

    public void sub() {
        Log.v(TAG, "sub");
        PigeonRoost.sub(this, ADDRESS_SHOW_DIALOG);
        PigeonRoost.sub(this, ADDRESS_DISMISS_DIALOG);
    }

    public void unSub() {
        Log.v(TAG, "unSub");
        PigeonRoost.unregister(this, ADDRESS_SHOW_DIALOG);
        PigeonRoost.unregister(this, ADDRESS_DISMISS_DIALOG);
    }

    @Override
    public void onMessage(String address, Parcelable message) {

        Bundle bundle = (Bundle) message;
        if (address.startsWith(ADDRESS_SHOW_DIALOG)) {
            PigeonRoost.clearAddressCache(ADDRESS_SHOW_DIALOG);
            onShowDialog(
                    bundle.getString(PARAM_DIALOG_UID),
                    bundle.getString(PARAM_DIALOG_CLASS_NAME),
                    bundle.getClassLoader(),
                    bundle.getBundle(PARAM_DIALOG_PARAMS));

        } else if (address.startsWith(ADDRESS_DISMISS_DIALOG)) {
            onDismissDialog(bundle.getString(PARAM_DIALOG_UID));
        }
    }

    abstract void onShowDialog(String uid, String className, ClassLoader classLoader, Bundle params);

    abstract void onDismissDialog(String uid);
}

