package com.fieldnation.fndialog;

import android.os.Bundle;
import android.os.Parcelable;

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
        Bundle payload = new Bundle();
        payload.putString(PARAM_DIALOG_CLASS_NAME, className);
        payload.putBundle(PARAM_DIALOG_PARAMS, params);
        payload.putString(PARAM_DIALOG_UID, uid);
        payload.setClassLoader(classLoader);

        PigeonRoost.dispatchEvent(TOPIC_ID_SHOW_DIALOG, payload, Sticky.TEMP);
    }

    public static void show(String uid, Class<? extends Dialog> klass, Bundle params) {
        show(uid, klass.getName(), klass.getClassLoader(), params);
    }

    // uid cannot be null when calling this method
    public static void dismiss(String uid) {
        Bundle payload = new Bundle();
        payload.putString(PARAM_DIALOG_UID, uid);

        PigeonRoost.dispatchEvent(TOPIC_ID_DISMISS_DIALOG, payload, Sticky.TEMP);
    }

    public void sub() {
        PigeonRoost.register(this, TOPIC_ID_DIALOG_COMPLETE + "/" + getDialogClass().getName());
    }

    public void unsub() {
        PigeonRoost.unregister(this, TOPIC_ID_DIALOG_COMPLETE + "/" + getDialogClass().getName());
    }

    abstract Class<? extends Dialog> getDialogClass();

    abstract String getUid();

    @Override
    public void onTopic(String topicId, Parcelable payload) {
        Bundle bundle = (Bundle) payload;

        if (getUid() != null && bundle.containsKey(PARAM_DIALOG_UID) && bundle.getString(PARAM_DIALOG_UID) != null) {
            String uid = bundle.getString(PARAM_DIALOG_UID);
            if (!uid.equals(getUid()))
                return;
        }

        if (topicId.startsWith(TOPIC_ID_DIALOG_COMPLETE)) {
            onComplete(bundle.getBundle(PARAM_DIALOG_RESPONSE));
        }
    }

    abstract void onComplete(Bundle response);
}
