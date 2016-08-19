package com.fieldnation.service.toast;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.UniqueTag;

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
    private static final String PARAM_MESSAGE_ID = "PARAM_MESSAGE_ID";


    public ToastClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static void snackbar(Context context, long id, String title, int duration) {
        snackbar(context, id, title, null, null, duration);
    }

    public static void snackbar(Context context, long id, int titleResId, int duration) {
        snackbar(context, id, context.getString(titleResId), duration);
    }

    public static void snackbar(Context context, long id, int titleResId, int buttonTextResId, PendingIntent buttonIntent, int duration) {
        snackbar(context, id, context.getString(titleResId), context.getString(buttonTextResId), buttonIntent, duration);
    }

    public static void snackbar(Context context, String title, int duration) {
        snackbar(context, 0, title, null, null, duration);
    }

    public static void snackbar(Context context, int titleResId, int duration) {
        snackbar(context, 0, context.getString(titleResId), duration);
    }

    public static void snackbar(Context context, int titleResId, int buttonTextResId, PendingIntent buttonIntent, int duration) {
        snackbar(context, 0, context.getString(titleResId), context.getString(buttonTextResId), buttonIntent, duration);
    }

    public static void snackbar(Context context, String title, String buttonText, PendingIntent buttonIntent, int duration) {
        snackbar(context, 0, title, buttonText, buttonIntent, duration);
    }

    public static void snackbar(Context context, long id, String title, String buttonText, PendingIntent buttonIntent, int duration) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_SNACKBAR);
        bundle.putString(PARAM_TITLE, title);
        bundle.putInt(PARAM_DURATION, duration);
        bundle.putString(PARAM_BUTTON_TEXT, buttonText);
        bundle.putParcelable(PARAM_BUTTON_INTENT, buttonIntent);
        bundle.putLong(PARAM_MESSAGE_ID, id);

        dispatchEvent(context, TOPIC_ID_SNACKBAR, bundle, Sticky.NONE);
    }


    public static void dismissSnackbar(Context context, long id) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DISMISS_SNACKBAR);
        bundle.putLong(PARAM_MESSAGE_ID, id);
        dispatchEvent(context, TOPIC_ID_SNACKBAR, bundle, Sticky.NONE);
    }

    public boolean subSnackbar() {
        return register(TOPIC_ID_SNACKBAR);
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
        return register(TOPIC_ID_TOAST);
    }

    public static abstract class Listener extends TopicClient.Listener {
        private static final String TAG = "ToastClient.Listener";
        private Snackbar _snackbar = null;
        private long _lastId = 0;

        public abstract Activity getActivity();

        public abstract ToastClient getToastClient();

        @Override
        public void onConnected() {
            getToastClient().subSnackbar();
            getToastClient().subToast();
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            switch (topicId) {
                case TOPIC_ID_SNACKBAR:
                    String action = ((Bundle) payload).getString(PARAM_ACTION);
                    switch (action) {
                        case PARAM_ACTION_DISMISS_SNACKBAR:
                            dismissSnackBar(((Bundle) payload).getLong(PARAM_MESSAGE_ID));
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
            showSnackBar(bundle.getLong(PARAM_MESSAGE_ID),
                    bundle.getString(PARAM_TITLE),
                    bundle.getString(PARAM_BUTTON_TEXT),
                    (PendingIntent) bundle.getParcelable(PARAM_BUTTON_INTENT),
                    bundle.getInt(PARAM_DURATION));
        }

        public void showSnackBar(long id, String title, String buttonText, final PendingIntent buttonIntent, int duration) {
            Log.v(TAG, "showSnackBar(" + title + ")");

            if (id > 0 && id == _lastId)
                return;

            if (getActivity().findViewById(android.R.id.content) == null) {
                Log.v(TAG, "showSnackBar.findViewById() == null");
                return;
            }

            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), title, duration);
            TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(getActivity().getResources().getColor(R.color.fn_white_text));
            snackbar.setActionTextColor(getActivity().getResources().getColor(R.color.fn_clickable_text));

            if (buttonText == null)
                buttonText = "DISMISS";

            snackbar.setAction(buttonText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_snackbar != null) {
                        _snackbar.dismiss();
                        _snackbar = null;
                        _lastId = 0;
                    }

                    if (buttonIntent != null) {
                        try {
                            buttonIntent.send(getActivity(), 0, new Intent());
                        } catch (PendingIntent.CanceledException e) {
                            Log.v(TAG, e);
                        }
                    }
                }
            });

            snackbar.show();
            _snackbar = snackbar;
            _lastId = id;
            Log.v(TAG, "snackbar.show()");
        }

        private void preShowToast(Bundle bundle) {
            showToast(bundle.getString(PARAM_TITLE), bundle.getInt(PARAM_DURATION));
        }

        public void showToast(String title, int duration) {
            Log.v(TAG, "showToast");
            Toast.makeText(getActivity(), title, duration).show();
        }

        public void dismissSnackBar(long id) {
            Log.v(TAG, "dismissSnackBar");
            if (_snackbar == null)
                return;

            if (_lastId != id)
                return;

            try {
                _snackbar.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            _snackbar = null;
            _lastId = 0;
        }
    }
}
