package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by Michael on 9/6/2016.
 * <p/>
 * This is the base class for any client that wants to show dialogs and receive their events.
 */
class Client extends TopicClient implements Constants {
    private static final String STAG = "Client";
    private final String TAG = UniqueTag.makeTag(STAG);


    Client(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static void show(Context context, String uid, String className, ClassLoader classLoader, Bundle params) {
        Bundle payload = new Bundle();
        payload.putString(PARAM_DIALOG_CLASS_NAME, className);
        payload.putBundle(PARAM_DIALOG_PARAMS, params);
        payload.putString(PARAM_DIALOG_UID, uid);
        payload.setClassLoader(classLoader);

        TopicService.dispatchEvent(context, TOPIC_ID_SHOW_DIALOG, payload, Sticky.NONE);
    }

    public static void show(Context context, String uid, Class<? extends Dialog> klass, Bundle params) {
        show(context, uid, klass.getName(), klass.getClassLoader(), params);
    }

    // uid cannot be null when calling this method
    public static void dismiss(Context context, String uid) {
        Bundle payload = new Bundle();
        payload.putString(PARAM_DIALOG_UID, uid);

        TopicService.dispatchEvent(context, TOPIC_ID_DISMISS_DIALOG, payload, Sticky.NONE);
    }

    public static abstract class Listener extends TopicClient.Listener {
        private static final String TAG = "Client.Listener";

        public abstract Class<? extends Dialog> getDialogClass();

        public abstract Client getClient();

        public abstract String getUid();

        @Override
        public void onConnected() {
            // TODO probably need to make the filter more precice
            getClient().register(TOPIC_ID_DIALOG_COMPLETE + "/" + getDialogClass().getName());
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
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

        public abstract void onComplete(Bundle response);
    }
}
