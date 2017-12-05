package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fntools.KeyedDispatcher;

/**
 * Created by Michael on 9/23/2016.
 */

public class WhatsNewDialog extends WebViewDialog {
    private static final String TAG = "WhatsNewDialog";

    public WhatsNewDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    public static void show(Context context, String uid) {
        Bundle params = new Bundle();
        params.putString("title", (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
        params.putString("html", context.getString(R.string.added_new_feature));
        params.putBoolean("skipFormatting", true);

        Controller.show(context, uid, WhatsNewDialog.class, params);
    }

    public static void show(Context context) {
        Bundle params = new Bundle();
        params.putString("title", (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
        params.putString("html", context.getString(R.string.added_new_feature));
        params.putBoolean("skipFormatting", true);

        Controller.show(context, null, WhatsNewDialog.class, params);
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/

    @Override
    public void dismiss(boolean animate) {
        _onClosedDispatcher.dispatch(getUid());
        super.dismiss(animate);
    }

    /*-*************************-*/
    /*-         Closed          -*/
    /*-*************************-*/
    public interface OnClosedListener {
        void onClosed();
    }

    private static KeyedDispatcher<OnClosedListener> _onClosedDispatcher = new KeyedDispatcher<OnClosedListener>() {
        @Override
        public void onDispatch(OnClosedListener listener, Object... parameters) {
            listener.onClosed();
        }
    };

    public static void addOnClosedListener(String uid, OnClosedListener onClosedListener) {
        _onClosedDispatcher.add(uid, onClosedListener);
    }

    public static void removeOnClosedListener(String uid, OnClosedListener onClosedListener) {
        _onClosedDispatcher.remove(uid, onClosedListener);
    }

    public static void removeAllOnClosedListener(String uid) {
        _onClosedDispatcher.removeAll(uid);
    }
}
