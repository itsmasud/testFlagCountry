package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Workorder;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.Dialog;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;

/**
 * Created by mc on 10/27/16.
 */

public class AcceptBundleDialog extends SimpleDialog {
    private static final String TAG = "AcceptBundleDialog";

    // Dialog TAGs
    private static final String DIALOG_DURATION = TAG + ".durationDialog";

    // State
    private static final String STATE_EXPIRATION = "expiration";

    // Type
    private static final String PARAM_TYPE = "type";
    public static final int TYPE_ACCEPT = 0;
    public static final int TYPE_REQUEST = 1;

    private static final String PARAM_WORK_ORDER_ID = "workOrderId";
    private static final String PARAM_BUNDLE_ID = "bundleId";
    private static final String PARAM_BUNDLE_SIZE = "bundleSize";

    // Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private TextView _expiresTextView;
    private Button _expirationButton;
    private View _dividerView;
    private TextView _termsWarningTextView;
    private Button _termsButton;
    private Button _viewBundleButton;
    private Button _cancelButton;
    private Button _okButton;

    // Dialogs
    private DurationDialog.Controller _durationDialog;

    // Data
    private int _type = TYPE_ACCEPT;
    private long _bundleId = 0;
    private int _bundleSize = 0;
    private long _workOrderId = 0;

    private long _expiration = -1;

    public AcceptBundleDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_accept_bundle, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);
        _expiresTextView = (TextView) v.findViewById(R.id.expires_textview);
        _dividerView = v.findViewById(R.id.divider);
        _expirationButton = (Button) v.findViewById(R.id.expiration_button);
        _termsWarningTextView = (TextView) v.findViewById(R.id.termswarning_textview);
        _termsButton = (Button) v.findViewById(R.id.terms_button);
        _viewBundleButton = (Button) v.findViewById(R.id.viewbundle_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _okButton = (Button) v.findViewById(R.id.ok_button);

        return v;
    }

    @Override
    public void onAdded() {
        _durationDialog = new DurationDialog.Controller(App.get(), DIALOG_DURATION);
        _durationDialog.setListener(_durationDialog_listener);

        super.onAdded();

        _termsButton.setOnClickListener(_terms_onClick);
        _viewBundleButton.setOnClickListener(_viewBundle_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton.setOnClickListener(_ok_onClick);
        _expirationButton.setOnClickListener(_expiration_okClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _type = payload.getInt(PARAM_TYPE);
        _bundleId = payload.getLong(PARAM_BUNDLE_ID);
        _bundleSize = payload.getInt(PARAM_BUNDLE_SIZE);
        _workOrderId = payload.getLong(PARAM_WORK_ORDER_ID);

        super.show(payload, animate);

        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        _expiration = savedState.getLong(STATE_EXPIRATION);
        super.onRestoreDialogState(savedState);
        populateUi();
    }

    private void populateUi() {
        switch (_type) {
            case TYPE_ACCEPT:
                _titleTextView.setText("Accept Bundle");
                _bodyTextView.setText("This is a bundle of " + _bundleSize + " work orders. If you accept this bundle you are accepting all " + _bundleSize + " work orders.");
                setExpirationVisibility(false);
                _termsWarningTextView.setText("By accepting this bundle you are agreeing to our");
                _okButton.setText(R.string.btn_accept);
                break;
            case TYPE_REQUEST:
                _titleTextView.setText("Request Bundle");
                _bodyTextView.setText("This is a bundle of " + _bundleSize + " work orders. If you request this bundle you are requesting all " + _bundleSize + " work orders.");
                setExpirationVisibility(true);
                _termsWarningTextView.setText("By requesting this bundle you are agreeing to our");
                _okButton.setText(R.string.btn_request);


                if (_expiration > 0) {
                    _expirationButton.setText(misc.convertMsToHuman(_expiration));
                } else {
                    _expirationButton.setText("NEVER");
                }
                break;
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        outState.putLong(STATE_EXPIRATION, _expiration);

        super.onSaveDialogState(outState);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (_durationDialog != null) _durationDialog.disconnect(App.get());
    }

    private void setExpirationVisibility(boolean visible) {
        _expirationButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        _dividerView.setVisibility(visible ? View.VISIBLE : View.GONE);
        _expiresTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private final View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OneButtonDialog.Controller.show(App.get(), null, R.string.dialog_terms_title,
                    R.string.dialog_terms_body, R.string.btn_ok, true);
        }
    };

    private final View.OnClickListener _viewBundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderBundleDetailActivity.startNew(App.get(), _workOrderId, _bundleId);
            dismiss(true);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (_type) {
                case TYPE_ACCEPT:
                    WorkorderClient.actionConfirmAssignment(App.get(), _workOrderId, null, null, null, false);
                    break;
                case TYPE_REQUEST:
                    if (_expiration > -1) {
                        WorkorderClient.actionRequest(App.get(), _workOrderId, _expiration / 1000);
                    } else {
                        WorkorderClient.actionRequest(App.get(), _workOrderId, _expiration);
                    }
                    break;
            }

            dismiss(true);
        }
    };

    private final View.OnClickListener _expiration_okClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DurationDialog.Controller.show(App.get(), DIALOG_DURATION);
        }
    };

    private final DurationDialog.ControllerListener _durationDialog_listener = new DurationDialog.ControllerListener() {
        @Override
        public void onOk(long milliseconds) {
            _expiration = milliseconds;
            populateUi();
        }

        @Override
        public void onCancel() {
            // dont' care
        }
    };

    public static abstract class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, AcceptBundleDialog.class, null);
        }

        /**
         * @param context     Application context
         * @param bundleId    The id of the bundle we're worried about
         * @param bundleSize  The number of work orders in the bundle
         * @param workOrderId An id of one of the work orders in the bundle.
         * @param type        One of {@link #TYPE_ACCEPT} or {@link #TYPE_REQUEST}
         */
        public static void show(Context context, long bundleId, int bundleSize, long workOrderId, int type) {
            Bundle bundle = new Bundle();
            bundle.putLong(PARAM_BUNDLE_ID, bundleId);
            bundle.putLong(PARAM_WORK_ORDER_ID, workOrderId);
            bundle.putInt(PARAM_BUNDLE_SIZE, bundleSize);
            bundle.putInt(PARAM_TYPE, type);
            show(context, null, AcceptBundleDialog.class, bundle);
        }
    }
}
