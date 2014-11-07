package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;

public class AcceptBundleDialog extends Dialog {
    private static final String TAG = "ui.workorder.detail.AcceptBundleDialog";

    // UI
    private TextView _acceptWOText;
    private TextView _viewBundle;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private Workorder _workorder;
    private Workorder[] _workorders;


    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/

    public AcceptBundleDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_accept_bundle_workorder);

        _acceptWOText = (TextView) findViewById(R.id.accept_description);
        _viewBundle = (TextView) findViewById(R.id.view_bundle);
        _viewBundle.setOnClickListener(_viewBundle_onClick);
        _okButton = (Button) findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        setTitle(R.string.activity_bundle_detail);
    }

    public void show(Workorder workorder, Listener listener) {
        if (workorder == null)
            return;

        _workorder = workorder;
        _listener = listener;

        _acceptWOText.setText("This workorder is part of a bundle of " + _workorder.getBundleCount().toString() + " workorders. By accepting this workorder you are accepting all of them.");
        show();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private View.OnClickListener _viewBundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            Intent intent = new Intent(getContext(), WorkorderBundleDetailActivity.class);
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, _workorder.getBundleId());
            getContext().startActivity(intent);
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onOk(_workorder);
            }
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public interface Listener {
        public void onOk(Workorder workorder);
    }
}
