package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by mc on 1/20/17.
 */

public class MarkCompleteDialog extends SimpleDialog {
    private static final String TAG = "MarkCompleteDialog";

    // Dialog uids
    private static final String DIALOG_RATE_YESNO = TAG + ".rateBuyerYesNoDialog";

    // State
    private static final String PARAM_WORKORDER = "workOrder";

    // Ui
    private LinearLayout _signatureLayout;
    private Button _continueButton;
    private Button _cancelButton;

    // Data
    private WorkOrder _workOrder;

    public MarkCompleteDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_mark_complete, container, false);

        _signatureLayout = (LinearLayout) v.findViewById(R.id.signature_layout);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _continueButton = (Button) v.findViewById(R.id.continue_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _signatureLayout.setOnClickListener(_signature_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _continueButton.setOnClickListener(_continue_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _workOrder = payload.getParcelable(PARAM_WORKORDER);

        super.show(payload, animate);

        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_continueButton == null)
            return;

        if (_workOrder.getSignatures().getMetadata().getTotal() > 0)
            _signatureLayout.setVisibility(View.GONE);
        else
            _signatureLayout.setVisibility(View.VISIBLE);
    }

    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_WORKORDER, workOrder);
        Controller.show(context, uid, MarkCompleteDialog.class, payload);
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _signature_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _onSignatureClickDispatcher.dispatch(getUid());
            dismiss(true);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final View.OnClickListener _continue_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _onContinueClickDispatcher.dispatch(getUid());
            dismiss(true);


            if (App.get().getProfile().canRequestWorkOnMarketplace() && !_workOrder.getW2()) {
                RateBuyerYesNoDialog.show(App.get(), DIALOG_RATE_YESNO, _workOrder, _workOrder.getCompany().getName());
            }
        }
    };

    /*-***********************************-*/
    /*-         Signature Click           -*/
    /*-***********************************-*/
    public interface OnSignatureClickListener {
        void onSignatureClick();
    }

    private static KeyedDispatcher<OnSignatureClickListener> _onSignatureClickDispatcher = new KeyedDispatcher<OnSignatureClickListener>() {
        @Override
        public void onDispatch(OnSignatureClickListener listener, Object... parameters) {
            listener.onSignatureClick();
        }
    };

    public static void addOnSignatureClickListener(String uid, OnSignatureClickListener onSignatureClickListener) {
        _onSignatureClickDispatcher.add(uid, onSignatureClickListener);
    }

    public static void removeOnSignatureClickListener(String uid, OnSignatureClickListener onSignatureClickListener) {
        _onSignatureClickDispatcher.remove(uid, onSignatureClickListener);
    }

    public static void removeAllOnSignatureClickListener(String uid) {
        _onSignatureClickDispatcher.removeAll(uid);
    }


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
