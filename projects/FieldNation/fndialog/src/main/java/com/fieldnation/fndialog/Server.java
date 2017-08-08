package com.fieldnation.fndialog;

import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;

/**
 * Created by Michael on 9/19/2016.
 */
abstract class Server extends Pigeon implements Constants {
    private static final String TAG = "Server";

    public void sub() {
        PigeonRoost.register(this, TOPIC_ID_SHOW_DIALOG);
        PigeonRoost.register(this, TOPIC_ID_DISMISS_DIALOG);
    }

    public void unSub() {
        PigeonRoost.unregister(this, TOPIC_ID_SHOW_DIALOG);
        PigeonRoost.unregister(this, TOPIC_ID_DISMISS_DIALOG);
    }

    @Override
    public void onTopic(String topicId, Parcelable payload) {

        Bundle bundle = (Bundle) payload;
        if (topicId.startsWith(TOPIC_ID_SHOW_DIALOG)) {
            PigeonRoost.clearTopic(TOPIC_ID_SHOW_DIALOG);
            onShowDialog(
                    bundle.getString(PARAM_DIALOG_UID),
                    bundle.getString(PARAM_DIALOG_CLASS_NAME),
                    bundle.getClassLoader(),
                    bundle.getBundle(PARAM_DIALOG_PARAMS));

        } else if (topicId.startsWith(TOPIC_ID_DISMISS_DIALOG)) {
            onDismissDialog(bundle.getString(PARAM_DIALOG_UID));
        }
    }

    abstract void onShowDialog(String uid, String className, ClassLoader classLoader, Bundle params);

    abstract void onDismissDialog(String uid);
}

