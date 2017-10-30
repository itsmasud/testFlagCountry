package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.CheckInOut;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.TimeLogs;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.TimeLogsAdapter;

import java.util.Calendar;

/**
 * Created by Shoaib on 10/20/17.
 */

public class TimeLogListDialog extends FullScreenDialog {
    private static final String TAG = "TimeLogListDialog";

    private static final String DIALOG_DELETE_WORKLOG = TAG + ".deleteWorkLogDialog";
    private static final String DIALOG_WORKLOG = TAG + ".worklogDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId;
    private WorkOrder _workOrder;
    private TimeLogsAdapter _adapter = new TimeLogsAdapter();

    public TimeLogListDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_timelog_list, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle(getContext().getString(R.string.time_logged));

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_add);

        _list = v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _list.setAdapter(_adapter);

        _adapter.setListener(_timelogs_listener);

        _workOrdersApi.sub();

        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_WORKLOG, _twoButtonDialog_deleteTimelog);
        WorkLogDialog.addOnOkListener(DIALOG_WORKLOG, _worklogDialog_listener);

    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        _workOrderId = params.getInt("workOrderId");
        AppMessagingClient.setLoading(true);
        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
    }

    private void populateUi() {
        _finishMenu.setVisibility(View.GONE);
        if (_list == null) return;
        if (_workOrder == null) return;
        if (_workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.ADD)) {
            _finishMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        _workOrdersApi.unsub();
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_WORKLOG, _twoButtonDialog_deleteTimelog);
        WorkLogDialog.removeOnOkListener(DIALOG_WORKLOG, _worklogDialog_listener);
        super.onStop();
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);

            boolean showdevice = false;
            try {
                showdevice = _workOrder.getPay().getType() == Pay.TypeEnum.DEVICE;
            } catch (Exception ex) {
            }

            WorkLogDialog.show(App.get(), DIALOG_WORKLOG, null, showdevice);
            return false;
        }
    };

    private final TimeLogsAdapter.Listener _timelogs_listener = new TimeLogsAdapter.Listener() {
        @Override
        public void onLongClick(View v, TimeLog timeLog) {
            TwoButtonDialog.show(App.get(), DIALOG_DELETE_WORKLOG,
                    R.string.dialog_delete_worklog_title,
                    R.string.dialog_delete_worklog_body,
                    R.string.btn_yes,
                    R.string.btn_no, true, timeLog);
        }

        @Override
        public void onClick(View v, TimeLog timeLog) {
            boolean showdevices = false;
            try {
                showdevices = _workOrder.getPay().getType() == Pay.TypeEnum.DEVICE;
            } catch (Exception ex) {
            }

            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
            WorkLogDialog.show(App.get(), DIALOG_WORKLOG, timeLog, showdevices);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteTimelog = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setLoading(true);
            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
            WorkordersWebApi.deleteTimeLog(App.get(), _workOrderId, ((TimeLog) extraData).getId(), App.get().getSpUiContext());
        }
    };

    private final WorkLogDialog.OnOkListener _worklogDialog_listener = new WorkLogDialog.OnOkListener() {
        @Override
        public void onOk(TimeLog timeLog, Calendar start, Calendar end, int deviceCount) {
            AppMessagingClient.setLoading(true);
            TimeLog newTimeLog = new TimeLog();
            try {
                newTimeLog.in(new CheckInOut().created(new Date(start)));
                newTimeLog.out(new CheckInOut().created(new Date(end)));
                if (deviceCount > 0)
                    newTimeLog.devices((double) deviceCount);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (timeLog == null) {
                WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
                WorkordersWebApi.addTimeLog(App.get(), _workOrderId, newTimeLog, App.get().getSpUiContext());

            } else {
                WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
                WorkordersWebApi.updateTimeLog(App.get(), _workOrderId, timeLog.getId(), newTimeLog, App.get().getSpUiContext());
            }
        }
    };


    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.toLowerCase().contains("workorder") || methodName.contains("TimeLog");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (success && successObject != null && successObject instanceof WorkOrder) {
                WorkOrder workOrder = (WorkOrder) successObject;
                _workOrder = workOrder;
                _adapter.setWorkOrder(workOrder);
                populateUi();
                AppMessagingClient.setLoading(false);
            } else {
                AppMessagingClient.setLoading(true);
                WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, false);
            }
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, TimeLogListDialog.class, params);
    }
}
