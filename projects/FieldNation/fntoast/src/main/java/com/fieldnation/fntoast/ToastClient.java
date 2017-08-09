package com.fieldnation.fntoast;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fntools.UniqueTag;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 7/17/2015.
 */
public abstract class ToastClient extends Pigeon {
    private static final String STAG = "ToastClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static final String ADDRESS_SNACKBAR = "ToastClient:ADDRESS_SNACKBAR";
    private static final String ADDRESS_TOAST = "ToastClient:ADDRESS_TOAST";

    private static final String PARAM_ACTION = "PARAM_ACTION";
    private static final String PARAM_ACTION_SNACKBAR = "PARAM_ACTION_SNACKBAR";
    private static final String PARAM_ACTION_DISMISS_SNACKBAR = "PARAM_ACTION_DISMISS_SNACKBAR";
    private static final String PARAM_ACTION_TOAST = "PARAM_ACTION_TOAST";

    private static final String PARAM_TITLE = "PARAM_TITLE";
    private static final String PARAM_BUTTON_TEXT = "PARAM_BUTTON_TEXT";

    private static final String PARAM_BUTTON_INTENT = "PARAM_BUTTON_INTENT";
    private static final String PARAM_DURATION = "PARAM_DURATION";
    private static final String PARAM_MESSAGE_ID = "PARAM_MESSAGE_ID";

    private static final SecureRandom _random = new SecureRandom();

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
        Bundle message = new Bundle();
        message.putString(PARAM_ACTION, PARAM_ACTION_SNACKBAR);
        message.putString(PARAM_TITLE, title);
        message.putInt(PARAM_DURATION, duration);
        message.putString(PARAM_BUTTON_TEXT, buttonText);
        message.putParcelable(PARAM_BUTTON_INTENT, buttonIntent);
        message.putLong(PARAM_MESSAGE_ID, id);

        PigeonRoost.sendMessage(ADDRESS_SNACKBAR, message, Sticky.NONE);
    }

    public static void dismissSnackbar(Context context, long id) {
        Bundle message = new Bundle();
        message.putString(PARAM_ACTION, PARAM_ACTION_DISMISS_SNACKBAR);
        message.putLong(PARAM_MESSAGE_ID, id);
        PigeonRoost.sendMessage(ADDRESS_SNACKBAR, message, Sticky.NONE);
    }

    public void subSnackbar() {
        PigeonRoost.sub(this, ADDRESS_SNACKBAR);
    }

    public void unSubSnackbar() {
        PigeonRoost.unregister(this, ADDRESS_SNACKBAR);
    }

    public static void toast(Context context, String title, int duration) {
        Bundle message = new Bundle();
        message.putString(PARAM_ACTION, PARAM_ACTION_TOAST);
        message.putString(PARAM_TITLE, title);
        message.putInt(PARAM_DURATION, duration);

        PigeonRoost.sendMessage(ADDRESS_TOAST, message, Sticky.NONE);
    }

    public static void toast(Context context, int resId, int duration) {
        Bundle message = new Bundle();
        message.putString(PARAM_ACTION, PARAM_ACTION_TOAST);
        message.putString(PARAM_TITLE, context.getString(resId));
        message.putInt(PARAM_DURATION, duration);

        PigeonRoost.sendMessage(ADDRESS_TOAST, message, Sticky.NONE);
    }

    public void subToast() {
        PigeonRoost.sub(this, ADDRESS_TOAST);
    }

    public void unSubToast() {
        PigeonRoost.unregister(this, ADDRESS_TOAST);
    }

    private Snackbar _snackbar = null;
    private long _lastId = 0;

    public abstract Activity getActivity();

    @Override
    public void onMessage(String address, Parcelable message) {

        switch (address) {
            case ADDRESS_SNACKBAR:
                String action = ((Bundle) message).getString(PARAM_ACTION);
                switch (action) {
                    case PARAM_ACTION_DISMISS_SNACKBAR:
                        dismissSnackBar(((Bundle) message).getLong(PARAM_MESSAGE_ID));
                        break;
                    default:
                        preShowSnackBar((Bundle) message);
                        break;
                }
                break;
            case ADDRESS_TOAST:
                preShowToast((Bundle) message);
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

    public abstract int getSnackbarTextId();

    private View findCoordinatorLayoutView() {
        int limit = 20;
        List<View> views = new LinkedList<>();
        View selected = getActivity().findViewById(android.R.id.content);
        views.add(selected);

        while (views.size() > 0) {
            View test = views.remove(0);
            limit--;

            if (limit <= 0)
                return selected;
            if (test instanceof CoordinatorLayout) {
                Log.v(TAG, "CoordinatorLayout found in " + (20 - limit));
                return test;
            } else if (test instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) test).getChildCount(); i++) {
                    views.add(((ViewGroup) test).getChildAt(i));
                }
            }
        }
        return selected;
    }

    public void showSnackBar(long id, String title, String buttonText, final PendingIntent buttonIntent, int duration) {
        Log.v(TAG, "showSnackBar(" + title + ")");

        if (id > 0 && id == _lastId)
            return;

        if (getActivity().findViewById(android.R.id.content) == null) {
            Log.v(TAG, "showSnackBar.findViewById() == null");
            return;
        }

        Snackbar snackbar = Snackbar.make(findCoordinatorLayoutView(), title, duration);
        TextView tv = (TextView) snackbar.getView().findViewById(getSnackbarTextId());
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
                        buttonIntent.send(getActivity(), _random.nextInt(), new Intent());
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
        Log.v(TAG, "showToast " + title);
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
