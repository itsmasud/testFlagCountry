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


    public Client(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static void show(Context context, String className, ClassLoader classLoader, Bundle params) {
        Bundle payload = new Bundle();
        payload.putString(PARAM_DIALOG_CLASS_NAME, className);
        payload.putBundle(PARAM_DIALOG_PARAMS, params);
        payload.setClassLoader(classLoader);

        TopicService.dispatchEvent(context, TOPIC_ID_SHOW_DIALOG, payload, Sticky.NONE);
    }

    public static void show(Context context, Class<? extends Dialog> klass, Bundle params) {
        show(context, klass.getName(), klass.getClassLoader(), params);
    }

    public static void dismiss(Context context, String className) {
        Bundle payload = new Bundle();
        payload.putString(PARAM_DIALOG_CLASS_NAME, className);

        TopicService.dispatchEvent(context, TOPIC_ID_DISMISS_DIALOG, payload, Sticky.NONE);
    }

    public static void dismiss(Context context, Class<? extends Dialog> klass) {
        dismiss(context, klass.getName());
    }


    public static abstract class Listener extends TopicClient.Listener {
        private static final String TAG = "Client.Listener";

        public abstract Class<? extends Dialog> getDialogClass();

        public abstract Client getClient();

        @Override
        public void onConnected() {
            // TODO probably need to make the filter more precice
            getClient().register(TOPIC_ID_DIALOG_COMPLETE + "/" + getDialogClass().getName());
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(TOPIC_ID_DIALOG_COMPLETE)) {
                onComplete(((Bundle) payload).getBundle(PARAM_DIALOG_RESPONSE));
            }
        }

        public abstract void onComplete(Bundle response);
    }
}
