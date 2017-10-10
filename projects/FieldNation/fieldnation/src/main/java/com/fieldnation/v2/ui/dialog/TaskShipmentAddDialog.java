package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.ui.workorder.detail.ShipmentRowView;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;
import com.fieldnation.v2.data.model.Task;

import java.util.LinkedList;
import java.util.List;

public class TaskShipmentAddDialog extends SimpleDialog {
    private static final String TAG = "TaskShipmentAddDialog";

    // UI
    private TextView _titleTextView;
    private Button _addButton;
    private Button _cancelButton;
    private LinearLayout _shipmentsLayout;

    // Data
    private int _workOrderId;
    private Shipments _shipments;
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
        View v = inflater.inflate(R.layout.dialog_v2_task_add_shipment, container, false);

        _titleTextView = v.findViewById(R.id.title_textview);
        _shipmentsLayout = v.findViewById(R.id.shipments_linearlayout);

        _cancelButton = v.findViewById(R.id.cancel_button);
        _addButton = v.findViewById(R.id.add_button);

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

        _shipments = payload.getParcelable("shipments");
        _title = payload.getString("title");
        _task = payload.getParcelable("task");
        _workOrderId = payload.getInt("workOrderId");
        populateUi();
    }

    private void populateUi() {
        if (_shipments == null)
            return;

        try {
            _shipmentsLayout.removeAllViews();

            if (_shipments.getResults().length == 0)
                return;

            List<Shipment> shipments = new LinkedList();
            for (Shipment shipment : _shipments.getResults()) {
                if (shipment.getDirection().equals(Shipment.DirectionEnum.FROM_SITE))
                    shipments.add(shipment);
                else if (shipment.getUser().getId().longValue() == App.getProfileId()) // && To Site
                    shipments.add(shipment);
            }

            for (int i = 0; i < shipments.size(); i++) {
                ShipmentRowView view = new ShipmentRowView(getView().getContext());
                _shipmentsLayout.addView(view);
                view.setTag(shipments.get(i));
                view.setData(shipments.get(i));
                view.setOnLongClickListener(_shipment_onLongClick);
                view.setOnClickListener(_shipment_onClick);
                view.hideForTaskShipmentDialog();
                view.setEnabled(_shipments.getActionsSet().contains(Shipments.ActionsEnum.ADD));
            }
        } catch (Exception ex) {
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private View.OnLongClickListener _shipment_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Shipment shipment = (Shipment) view.getTag();
            _onDeleteDispatcher.dispatch(getUid(), _workOrderId, shipment);
            return true;
        }
    };

    private View.OnClickListener _shipment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO need to present an edit dialog
            Shipment shipment = (Shipment) view.getTag();
            dismiss(true);
            _onAddShipmentDispatcher.dispatch(getUid(), _workOrderId, shipment, _task);
        }
    };

    private final View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onAddShipmentDispatcher.dispatch(getUid(), _workOrderId, null, _task);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
        }
    };

    public static void show(Context context, String uid, int workOrderId, Shipments shipments, String title, Task task) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("shipments", shipments);
        params.putString("title", title);
        if (task != null)
            params.putParcelable("task", task);

        Controller.show(context, uid, TaskShipmentAddDialog.class, params);
    }

    /*-*******************************-*/
    /*-         AddShipment           -*/
    /*-*******************************-*/
    public interface OnAddShipmentListener {
        void onAddShipment(int workOrderId, Shipment shipment, Task task);
    }

    private static KeyedDispatcher<OnAddShipmentListener> _onAddShipmentDispatcher = new KeyedDispatcher<OnAddShipmentListener>() {
        @Override
        public void onDispatch(OnAddShipmentListener listener, Object... parameters) {
            listener.onAddShipment(
                    (Integer) parameters[0],
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
        void onDelete(int workOrderId, Shipment shipment);
    }

    private static KeyedDispatcher<OnDeleteListener> _onDeleteDispatcher = new KeyedDispatcher<OnDeleteListener>() {
        @Override
        public void onDispatch(OnDeleteListener listener, Object... parameters) {
            listener.onDelete((Integer) parameters[0], (Shipment) parameters[1]);
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