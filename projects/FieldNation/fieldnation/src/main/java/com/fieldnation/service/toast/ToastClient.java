package com.fieldnation.service.toast;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicClient;

/**
 * Created by Michael Carver on 7/17/2015.
 */
public class ToastClient extends TopicClient {
    private static final String STAG = "ToastClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static final String TOPIC_ID_SNACKBAR = "ToastClient:TOPIC_ID_SNACKBAR";
    private static final String TOPIC_ID_TOAST = "ToastClient:TOPIC_ID_TOAST";

    private static final String PARAM_ACTION = "PARAM_ACTION";
    private static final String PARAM_ACTION_SNACKBAR = "PARAM_ACTION_SNACKBAR";
    private static final String PARAM_ACTION_DISMISS_SNACKBAR = "PARAM_ACTION_DISMISS_SNACKBAR";
    private static final String PARAM_ACTION_TOAST = "PARAM_ACTION_TOAST";

    private static final String PARAM_TITLE = "PARAM_TITLE";
    private static final String PARAM_BUTTON_TEXT = "PARAM_BUTTON_TEXT";

    private static final String PARAM_BUTTON_INTENT = "PARAM_BUTTON_INTENT";
    private static final String PARAM_DURATION = "PARAM_DURATION";

    public ToastClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, STAG);
    }

    public static void snackbar(Context context, String title, int duration) {
        snackbar(context, title, null, null, duration);
    }

    public static void snackbar(Context context, int titleResId, int duration) {
        snackbar(context, context.getString(titleResId), duration);
    }

    public static void snackbar(Context context, String title, String buttonText, PendingIntent buttonIntent, int duration) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_SNACKBAR);
        bundle.putString(PARAM_TITLE, title);
        bundle.putInt(PARAM_DURATION, duration);
        bundle.putString(PARAM_BUTTON_TEXT, buttonText);
        bundle.putParcelable(PARAM_BUTTON_INTENT, buttonIntent);

        dispatchEvent(context, TOPIC_ID_SNACKBAR, bundle, Sticky.NONE);
    }

    public static void dismissSnackbar(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DISMISS_SNACKBAR);
        dispatchEvent(context, TOPIC_ID_SNACKBAR, bundle, Sticky.NONE);
    }

    public static void snackbar(Context context, int titleResId, int buttonTextResId, PendingIntent buttonIntent, int duration) {
        snackbar(context, context.getString(titleResId), context.getString(buttonTextResId), buttonIntent, duration);
    }

    public boolean subSnackbar() {
        return register(TOPIC_ID_SNACKBAR, TAG);
    }

    public static void toast(Context context, String title, int duration) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_TOAST);
        bundle.putString(PARAM_TITLE, title);
        bundle.putInt(PARAM_DURATION, duration);

        dispatchEvent(context, TOPIC_ID_TOAST, bundle, Sticky.NONE);
    }

    public static void toast(Context context, int resId, int duration) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_TOAST);
        bundle.putString(PARAM_TITLE, context.getString(resId));
        bundle.putInt(PARAM_DURATION, duration);

        dispatchEvent(context, TOPIC_ID_TOAST, bundle, Sticky.NONE);
    }

    public boolean subToast() {
        return register(TOPIC_ID_TOAST, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            switch (topicId) {
                case TOPIC_ID_SNACKBAR:
                    String action = ((Bundle) payload).getString(PARAM_ACTION);
                    switch (action) {
                        case PARAM_ACTION_DISMISS_SNACKBAR:
                            dismissSnackBar();
                            break;
                        default:
                            preShowSnackBar((Bundle) payload);
                            break;
                    }
                    break;
                case TOPIC_ID_TOAST:
                    preShowToast((Bundle) payload);
                    break;
            }
        }

        private void preShowSnackBar(Bundle bundle) {
            showSnackBar(bundle.getString(PARAM_TITLE),
                    bundle.getString(PARAM_BUTTON_TEXT),
                    (PendingIntent) bundle.getParcelable(PARAM_BUTTON_INTENT),
                    bundle.getInt(PARAM_DURATION));
        }

        public void showSnackBar(String title, String buttonText, PendingIntent buttonIntent, int duration) {
        }

        private void preShowToast(Bundle bundle) {
            showToast(bundle.getString(PARAM_TITLE), bundle.getInt(PARAM_DURATION));
        }

        public void showToast(String title, int duration) {

        }

        public void dismissSnackBar() {
        }
    }
}
