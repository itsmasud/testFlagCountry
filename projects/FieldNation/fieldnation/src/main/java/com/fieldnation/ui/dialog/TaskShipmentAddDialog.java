package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.detail.ShipmentRowView;

public class TaskShipmentAddDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.TaskShipmentAddDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_TASKID = "STATE_TASKID";
    private static final String STATE_TITLE = "STATE_TITLE";

    // UI
    private TextView _titleTextView;
    private Button _addButton;
    private Button _cancelButton;
    private LinearLayout _shipmentsLayout;
    private ShipmentAddDialog _addDialog;

    // Data
    private Listener _listener;
    private Workorder _workorder;
    private String _title;
    private long _taskId;


    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    public static TaskShipmentAddDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, TaskShipmentAddDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TASKID))
                _taskId = savedInstanceState.getLong(STATE_TASKID);

            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_TITLE))
                _title = savedInstanceState.getString(STATE_TITLE);
        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        if (_taskId != 0)
            outState.putLong(STATE_TASKID, _taskId);

        if (_title != null)
            outState.putString(STATE_TITLE, _title);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_task_add_shipment, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _shipmentsLayout = (LinearLayout) v.findViewById(R.id.shipments_linearlayout);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _addButton = (Button) v.findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);

        _addDialog = ShipmentAddDialog.getInstance(_fm, TAG);
        _addDialog.setListener(_addDialog_listener);

        populateUi();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_title != null)
            _titleTextView.setText(_title);

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(String title, Workorder workorder, long taskId) {
        _workorder = workorder;
        _title = title;
        _taskId = taskId;
        show();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        try {
            ShipmentTracking[] shipments = _workorder.getShipmentTracking();
            _shipmentsLayout.removeAllViews();

            if (shipments == null)
                return;

            for (int i = 0; i < shipments.length; i++) {
                ShipmentRowView view = new ShipmentRowView(getActivity());
                _shipmentsLayout.addView(view);
                view.setData(_workorder, shipments[i]);
                view.hideForTaskShipmentDialog();
                view.setListener(_summaryListener);
            }

        } catch (Exception ex) {
        }
    }


    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final ShipmentAddDialog.Listener _addDialog_listener = new ShipmentAddDialog.Listener() {
        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            if (_listener != null) {
                _listener.onAddShipmentDetails(_workorder, trackingId, carrier, carrierName, description, shipToSite);
            }
        }

        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId) {
            if (_listener != null) {
                _listener.onAddShipmentDetails(_workorder, trackingId, carrier, carrierName, description, shipToSite, taskId);
            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onScan() {
            if (_listener != null) {
                _listener.onScan();
            }


        }
    };

    private final ShipmentRowView.Listener _summaryListener = new ShipmentRowView.Listener() {
        @Override
        public void onDelete(ShipmentTracking shipment) {
            if (_listener != null) {
                _listener.onDelete(_workorder, shipment);
            }
        }

        @Override
        public void onEdit(ShipmentTracking shipment) {
            // TODO need to present an edit dialog
            dismiss();
            if (_listener != null) {
                _listener.onAssign(_workorder, shipment.getWorkorderShipmentId(), _taskId);
            }
        }
    };

    private final View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onAddShipment(_taskId);
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onCancel();
        }
    };

    public interface Listener {
        void onCancel();

        void onDelete(Workorder workorder, ShipmentTracking shipment);

        void onAssign(Workorder workorder, int shipmentId, long taskId);

        void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite);

        void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId);

        void onScan();

        void onAddShipment(long taskId);
    }

}
