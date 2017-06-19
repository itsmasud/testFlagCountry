package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.workorder.detail.ShipmentRowView;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

public class TaskShipmentAddDialog extends SimpleDialog {
    private static final String TAG = "TaskShipmentAddDialog";

    // UI
    private TextView _titleTextView;
    private Button _addButton;
    private Button _cancelButton;
    private LinearLayout _shipmentsLayout;

    // Data
    private WorkOrder _workOrder;
    private String _title;
    private Task _task;

    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    public TaskShipmentAddDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_task_add_shipment, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _shipmentsLayout = (LinearLayout) v.findViewById(R.id.shipments_linearlayout);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _addButton = (Button) v.findViewById(R.id.add_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _cancelButton.setOnClickListener(_cancel_onClick);
        _addButton.setOnClickListener(_add_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_title != null)
            _titleTextView.setText(_title);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        _workOrder = payload.getParcelable("workOrder");
        _title = payload.getString("title");
        _task = payload.getParcelable("task");
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        try {
            _shipmentsLayout.removeAllViews();
            Shipments shipments = _workOrder.getShipments();
            if (shipments.getResults().length == 0)
                return;

            for (int i = 0; i < shipments.getResults().length; i++) {
                ShipmentRowView view = new ShipmentRowView(getView().getContext());
                _shipmentsLayout.addView(view);
                view.setData(_workOrder, shipments.getResults()[i]);
                view.hideForTaskShipmentDialog();
                view.setListener(_summaryListener);
            }
        } catch (Exception ex) {
        }
    }


    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final ShipmentRowView.Listener _summaryListener = new ShipmentRowView.Listener() {
        @Override
        public void onDelete(Shipment shipment) {
            _onDeleteDispatcher.dispatch(getUid(), _workOrder, shipment);
        }

        @Override
        public void onEdit(Shipment shipment) {
            // TODO need to present an edit dialog
            dismiss(true);
            _onAddShipmentDispatcher.dispatch(getUid(), _workOrder, shipment, _task);
        }
    };

    private final View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onAddShipmentDispatcher.dispatch(getUid(), _workOrder, null, _task);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
        }
    };

    public static void show(Context context, String uid, WorkOrder workOrder, String title, Task task) {
        Bundle params = new Bundle();
        params.putParcelable("workOrder", workOrder);
        params.putString("title", title);
        if (task != null)
            params.putParcelable("task", task);

        Controller.show(context, uid, TaskShipmentAddDialog.class, params);
    }

    /*-*******************************-*/
    /*-         AddShipment           -*/
    /*-*******************************-*/
    public interface OnAddShipmentListener {
        void onAddShipment(WorkOrder workorder, Shipment shipment, Task task);
    }

    private static KeyedDispatcher<OnAddShipmentListener> _onAddShipmentDispatcher = new KeyedDispatcher<OnAddShipmentListener>() {
        @Override
        public void onDispatch(OnAddShipmentListener listener, Object... parameters) {
            listener.onAddShipment(
                    (WorkOrder) parameters[0],
                    (Shipment) parameters[1],
                    (Task) parameters[2]);
        }
    };

    public static void addOnAddShipmentListener(String uid, OnAddShipmentListener onAddShipmentListener) {
        _onAddShipmentDispatcher.add(uid, onAddShipmentListener);
    }

    public static void removeOnAddShipmentListener(String uid, OnAddShipmentListener onAddShipmentListener) {
        _onAddShipmentDispatcher.remove(uid, onAddShipmentListener);
    }

    public static void removeAllOnAddShipmentListener(String uid) {
        _onAddShipmentDispatcher.removeAll(uid);
    }


    /*-**************************-*/
    /*-         Delete           -*/
    /*-**************************-*/
    public interface OnDeleteListener {
        void onDelete(WorkOrder workorder, Shipment shipment);
    }

    private static KeyedDispatcher<OnDeleteListener> _onDeleteDispatcher = new KeyedDispatcher<OnDeleteListener>() {
        @Override
        public void onDispatch(OnDeleteListener listener, Object... parameters) {
            listener.onDelete((WorkOrder) parameters[0], (Shipment) parameters[1]);
        }
    };

    public static void addOnDeleteListener(String uid, OnDeleteListener onDeleteListener) {
        _onDeleteDispatcher.add(uid, onDeleteListener);
    }

    public static void removeOnDeleteListener(String uid, OnDeleteListener onDeleteListener) {
        _onDeleteDispatcher.remove(uid, onDeleteListener);
    }

    public static void removeAllOnDeleteListener(String uid) {
        _onDeleteDispatcher.removeAll(uid);
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