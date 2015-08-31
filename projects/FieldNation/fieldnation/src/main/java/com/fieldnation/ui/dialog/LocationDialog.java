package com.fieldnation.ui.dialog;

import android.support.v4.app.FragmentManager;

import com.fieldnation.App;
import com.fieldnation.R;

/**
 * Created by michael.carver on 2/5/2015.
 */
public class LocationDialog extends TwoButtonDialog {
    private static final String TAG = "LocationDialog";

    // Data
    private boolean _hard;
    private Listener _listener;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static LocationDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, LocationDialog.class);
    }

    public void show(boolean hard, Listener listener) {
        _listener = listener;
        _hard = hard;
        if (_hard) {
            setData(App.get().getString(R.string.dialog_location_title),
                    App.get().getString(R.string.dialog_location_hard),
                    App.get().getString(R.string.btn_gps_settings),
                    App.get().getString(R.string.btn_cancel),
                    _super_listener
            );
        } else {
            setData(App.get().getString(R.string.dialog_location_title),
                    App.get().getString(R.string.dialog_location_soft),
                    App.get().getString(R.string.btn_gps_settings),
                    App.get().getString(R.string.btn_not_now),
                    _super_listener
            );
        }
        super.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    private final TwoButtonDialog.Listener _super_listener = new TwoButtonDialog.Listener() {
        @Override
        public void onPositive() {
            if (_listener != null)
                _listener.onOk();
        }

        @Override
        public void onNegative() {
            if (_listener != null) {
                if (_hard)
                    _listener.onCancel();
                else
                    _listener.onNotNow();
            }

        }

        @Override
        public void onCancel() {
            if (_listener != null)
                _listener.onCancel();
        }
    };

    public interface Listener {
        void onOk();

        void onNotNow();

        void onCancel();
    }

}

