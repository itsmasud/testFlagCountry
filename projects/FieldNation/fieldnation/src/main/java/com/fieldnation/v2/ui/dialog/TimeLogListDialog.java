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
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
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
import java.util.UUID;

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
    private String _dialogTitle;
    private WorkOrder _workOrder;
    private TimeLogsAdapter _adapter = new TimeLogsAdapter();
    private String _uiUUID = null;

    public TimeLogListDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_timelog_list, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);

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
        _dialogTitle = params.getString("dialogTitle");
        _uiUUID = params.getString("uiUUID");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Time Log List Dialog"))
                .build());

        AppMessagingClient.setLoading(true);
        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
    }

    private void populateUi() {
        _finishMenu.setVisibility(View.GONE);
        if (_list == null) return;
        if (_workOrder == null) return;

        if (!misc.isEmptyOrNull(_dialogTitle))
            _toolbar.setTitle(_dialogTitle);

        if (_workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.ADD)) {
            _finishMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Time Log List Dialog"))
                .build());

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

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new ApatheticOnMenuItemClickListener() {
        @Override
        public boolean onSingleMenuItemClick(MenuItem item) {
            WorkOrderTracker.onAddEvent(
                    App.get(),
                    WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED,
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Time Log List Dialog - add")
                    }
            );

            boolean showdevice = false;
            try {
                showdevice = _workOrder.getPay().getType() == Pay.TypeEnum.DEVICE;
            } catch (Exception ex) {
            }

            WorkLogDialog.show(App.get(), DIALOG_WORKLOG, _uiUUID, null, showdevice);
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

            WorkOrderTracker.onEditEvent(
                    App.get(),
                    WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED,
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Time Log List Dialog - edit")
                    }
            );
            WorkLogDialog.show(App.get(), DIALOG_WORKLOG, _uiUUID, timeLog, showdevices);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteTimelog = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setLoading(true);
            WorkOrderTracker.onDeleteEvent(
                    App.get(),
                    WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED,
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Time Log List Dialog - delete")
                    }
            );
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
                WorkOrderTracker.onAddEvent(
                        App.get(),
                        WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED,
                        new EventContext[]{
                                new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                                new SpStackContext(DebugUtils.getStackTraceElement()),
                                new SpStatusContext(SpStatusContext.Status.INFO, "Time Log List Dialog - add")
                        }
                );
                WorkordersWebApi.addTimeLog(App.get(), new UUIDGroup(_uiUUID, UUID.randomUUID().toString()),
                        _workOrderId, newTimeLog, App.get().getSpUiContext());

            } else {
                WorkOrderTracker.onEditEvent(
                        App.get(),
                        WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED,
                        new EventContext[]{
                                new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                                new SpStackContext(DebugUtils.getStackTraceElement()),
                                new SpStatusContext(SpStatusContext.Status.INFO, "Time Log List Dialog - edit")
                        }
                );
                WorkordersWebApi.updateTimeLog(App.get(), new UUIDGroup(_uiUUID, UUID.randomUUID().toString()),
                        _workOrderId, timeLog.getId(), newTimeLog, App.get().getSpUiContext());
            }
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.toLowerCase().contains("workorder") || methodName.contains("TimeLog");
        }

        @Override
        public boolean onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
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
            return super.onComplete(transactionParams, methodName, successObject, success, failObject);
        }
    };

    public static void show(Context context, String uid, String uiUUID, int workOrderId, String dialogTitle) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("dialogTitle", dialogTitle);
        params.putString("uiUUID", uiUUID);

        Controller.show(context, uid, TimeLogListDialog.class, params);
    }
}
