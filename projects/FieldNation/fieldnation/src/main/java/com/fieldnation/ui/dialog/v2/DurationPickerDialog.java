package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.ui.FnNumberPicker;
import com.fieldnation.ui.KeyedDispatcher;

/**
 * Created by Shoaib on 1/1/17.
 */

public class DurationPickerDialog extends SimpleDialog {
    private static final String TAG = "DurationPickerDialog";

    // Dialog uids
    private static final String DIALOG_RATE_YESNO = TAG + ".rateBuyerYesNoDialog";

    // State
//    private static final String PARAM_WORKORDER = "workOrder";

    // Ui
    private FnNumberPicker _hourFnNumberPicker;
    private FnNumberPicker _minuteFnNumberPicker;
    private Button _continueButton;
    private Button _cancelButton;

    // Data
    private Workorder _workorder;

    public DurationPickerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_duration_picker, container, false);

        _hourFnNumberPicker = (FnNumberPicker) v.findViewById(R.id.hourNumberPicker);
        _minuteFnNumberPicker = (FnNumberPicker) v.findViewById(R.id.minuteNumberumberPicker);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _continueButton = (Button) v.findViewById(R.id.continue_button);


        _hourFnNumberPicker.setMinValue(10);
        _hourFnNumberPicker.setMaxValue(100);
        _hourFnNumberPicker.setWrapSelectorWheel(false);
        _hourFnNumberPicker.setDividerColor(-1);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _cancelButton.setOnClickListener(_cancel_onClick);
        _continueButton.setOnClickListener(_continue_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
//        _workorder = payload.getParcelable(PARAM_WORKORDER);

        super.show(payload, animate);

        populateUi();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_continueButton == null)
            return;

    }
    public static void show(Context context, String uid) {
        Bundle payload = new Bundle();
//        payload.putParcelable(PARAM_WORKORDER, workorder);
        Controller.show(context, uid, DurationPickerDialog.class, payload);
    }

    /*-*****************************-*/
    /*-             Events          -*/
    /*-*****************************-*/
    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final View.OnClickListener _continue_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _onContinueClickDispatcher.dispatch(getUid(), null);
            dismiss(true);
            if (App.get().getProfile().canRequestWorkOnMarketplace() && !_workorder.isW2Workorder() && _workorder.getBuyerRatingInfo().getRatingId() == null) {
                RateBuyerYesNoDialog.show(App.get(), DIALOG_RATE_YESNO, _workorder, _workorder.getCompanyName());
            }
        }
    };

    /*-***********************************-*/
    /*-         Signature Click           -*/
    /*-***********************************-*/
//    public interface OnSignatureClickListener {
//        void onSignatureClick();
//    }
//
//    private static KeyedDispatcher<OnSignatureClickListener> _onSignatureClickDispatcher = new KeyedDispatcher<OnSignatureClickListener>() {
//        @Override
//        public void onDispatch(OnSignatureClickListener listener, Object... parameters) {
//            listener.onSignatureClick();
//        }
//    };
//
//    public static void addOnSignatureClickListener(String uid, OnSignatureClickListener onSignatureClickListener) {
//        _onSignatureClickDispatcher.add(uid, onSignatureClickListener);
//    }
//
//    public static void removeOnSignatureClickListener(String uid, OnSignatureClickListener onSignatureClickListener) {
//        _onSignatureClickDispatcher.remove(uid, onSignatureClickListener);
//    }
//
//    public static void removeAllOnSignatureClickListener(String uid) {
//        _onSignatureClickDispatcher.removeAll(uid);
//    }


    /*-**********************************-*/
    /*-         Continue Click           -*/
    /*-**********************************-*/
    public interface OnContinueClickListener {
        void onContinueClick();
    }

    private static KeyedDispatcher<OnContinueClickListener> _onContinueClickDispatcher = new KeyedDispatcher<OnContinueClickListener>() {
        @Override
        public void onDispatch(OnContinueClickListener listener, Object... parameters) {
            listener.onContinueClick();
        }
    };

    public static void addOnContinueClickListener(String uid, OnContinueClickListener onContinueClickListener) {
        _onContinueClickDispatcher.add(uid, onContinueClickListener);
    }

    public static void removeOnContinueClickListener(String uid, OnContinueClickListener onContinueClickListener) {
        _onContinueClickDispatcher.remove(uid, onContinueClickListener);
    }

    public static void removeAllOnContinueClickListener(String uid) {
        _onContinueClickDispatcher.removeAll(uid);
    }
}
