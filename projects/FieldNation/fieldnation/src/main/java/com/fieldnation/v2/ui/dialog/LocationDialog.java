package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.ui.KeyedDispatcher;

/**
 * Created by mc on 2/13/17.
 */

public class LocationDialog extends TwoButtonDialog {

    private boolean hard = false;

    public LocationDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        hard = ((Bundle) getExtraData()).getBoolean("hard");
    }

    @Override
    public boolean onPrimaryClick() {
        _onOkDispatcher.dispatch(getUid());
        return super.onPrimaryClick();
    }

    @Override
    public boolean onSecondaryClick() {
        if (hard)
            _onCancelDispatcher.dispatch(getUid());
        else
            _onNotNowDispatcher.dispatch(getUid());
        return super.onSecondaryClick();
    }

    @Override
    public boolean onCancel() {
        _onCancelDispatcher.dispatch(getUid());
        return super.onCancel();
    }

    public static void show(Context context, String uid, boolean hard) {
        Bundle params = new Bundle();
        params.putBoolean("hard", hard);
        if (hard) {
            show(context, uid, R.string.dialog_location_title, R.string.dialog_location_hard,
                    R.string.btn_gps_settings, R.string.btn_cancel, true, params);
        } else {
            show(context, uid, R.string.dialog_location_title, R.string.dialog_location_soft,
                    R.string.btn_gps_settings, R.string.btn_not_now, true, params);
        }
    }

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk();
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk();
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }

    /*-**************************-*/
    /*-         NotNow           -*/
    /*-**************************-*/
    public interface OnNotNowListener {
        void onNotNow();
    }

    private static KeyedDispatcher<OnNotNowListener> _onNotNowDispatcher = new KeyedDispatcher<OnNotNowListener>() {
        @Override
        public void onDispatch(OnNotNowListener listener, Object... parameters) {
            listener.onNotNow();
        }
    };

    public static void addOnNotNowListener(String uid, OnNotNowListener onNotNowListener) {
        _onNotNowDispatcher.add(uid, onNotNowListener);
    }

    public static void removeOnNotNowListener(String uid, OnNotNowListener onNotNowListener) {
        _onNotNowDispatcher.remove(uid, onNotNowListener);
    }

    public static void removeAllOnNotNowListener(String uid) {
        _onNotNowDispatcher.removeAll(uid);
    }

    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }
}
