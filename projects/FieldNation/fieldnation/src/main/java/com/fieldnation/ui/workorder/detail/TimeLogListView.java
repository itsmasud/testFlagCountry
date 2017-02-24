package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.TimeLogs;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.Random;

public class TimeLogListView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "TimeLoggedView";

    // Ui
    private LinearLayout _logList;
    private TextView _noTimeTextView;
    private Button _logTimeButton;


    // Data
    private Listener _listener;
    private WorkOrder _workOrder;

    public TimeLogListView(Context context) {
        super(context);
        init();
    }

    public TimeLogListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeLogListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_time_log_list, this);

        if (isInEditMode())
            return;

        _logList = (LinearLayout) findViewById(R.id.log_list);
        _noTimeTextView = (TextView) findViewById(R.id.notime_textview);
        _logTimeButton = (Button) findViewById(R.id.logTime_button);
        _logTimeButton.setOnClickListener(_addLog_onClick);


        setVisibility(GONE);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        ppulateUi();
    }


    public void ppulateUi() {
        if (_workOrder == null)
            return;

        final TimeLog[] logs = _workOrder.getTimeLogs().getResults();

        if (logs == null)
            return;

        if (_workOrder.getTimeLogs() != null
                && _workOrder.getTimeLogs().getActionsSet() != null
                && _workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.ADD)) {
            _logTimeButton.setVisibility(View.VISIBLE);
        } else {
            if (logs == null || logs.length == 0) {
                setVisibility(GONE);
                return;
            }
            _logTimeButton.setVisibility(View.GONE);
        }

        setVisibility(View.VISIBLE);

        if (logs == null || logs.length == 0) {
            _noTimeTextView.setVisibility(View.VISIBLE);
        } else {
            _noTimeTextView.setVisibility(View.GONE);
        }


        if (logs == null || logs.length == 0) {
            _logList.removeAllViews();
        } else if (_logList.getChildCount() > logs.length) {
            _logList.removeViews(logs.length - 1, _logList.getChildCount() - logs.length);
        }

        if (logs != null && logs.length > 0) {
            ForLoopRunnable r = new ForLoopRunnable(logs.length, new Handler()) {
                private final TimeLog[] _logs = logs;

                @Override
                public void next(int i) throws Exception {
                    TimeLogRowView v = null;
                    if (i < _logList.getChildCount()) {
                        v = (TimeLogRowView) _logList.getChildAt(i);
                    } else {
                        v = new TimeLogRowView(getContext());
                        _logList.addView(v);
                    }
                    TimeLog log = _logs[i];
                    v.setListener(_scheduleDetailView_listener);
                    v.setData(_workOrder, log);
                }
            };
            postDelayed(r, new Random().nextInt(1000));
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final TimeLogRowView.Listener _scheduleDetailView_listener = new TimeLogRowView.Listener() {
        @Override
        public void editTimeLog(WorkOrder workOrder, TimeLog timeLog, boolean showDeviceCount) {
            if (_listener != null
                    && timeLog.getActionsSet() != null
                    && timeLog.getActionsSet().contains(TimeLog.ActionsEnum.EDIT)) {
                _listener.editWorklog(workOrder, timeLog, showDeviceCount);
            }
        }

        @Override
        public void deleteTimeLog(final TimeLogRowView view, final WorkOrder workOrder, final TimeLog timeLog) {
            if (_listener != null
                    && timeLog.getActionsSet() != null
                    && timeLog.getActionsSet().contains(TimeLog.ActionsEnum.REMOVE)) {
                _listener.deleteWorklog(workOrder, timeLog);
            }
        }
    };



    private final View.OnClickListener _addLog_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean showdevices = false;
            try {
                    showdevices = _workOrder.getPay().getType().equals(Pay.TypeEnum.DEVICE);
            } catch (Exception ex) {
            }

                if (_listener != null
                        && _workOrder.getTimeLogs() != null
                        && _workOrder.getTimeLogs().getActionsSet() != null
                        && _workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.ADD)){
                    _listener.addWorklog(showdevices);
            }
        }
    };


    public interface Listener {
        void addWorklog(boolean showdevice);

            void editWorklog(WorkOrder workOrder, TimeLog timeLog, boolean showDeviceCount);

            void deleteWorklog(WorkOrder workOrder, TimeLog timeLog);

    }
}
