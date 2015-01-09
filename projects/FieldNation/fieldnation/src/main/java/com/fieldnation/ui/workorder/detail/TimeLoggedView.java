package com.fieldnation.ui.workorder.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;

public class TimeLoggedView extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.TimeLoggedView";

    // Ui
    private LinearLayout _logList;
    private LinearLayout _addLogLinearLayout;
    private TextView _noTimeTextView;
    private TextView _devicesTextView;

    // Data
    private Listener _listener;
    private Workorder _workorder;

    public TimeLoggedView(Context context) {
        super(context);
        init();
    }

    public TimeLoggedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeLoggedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_time_logged, this);

        if (isInEditMode())
            return;
        _logList = (LinearLayout) findViewById(R.id.log_list);
        _noTimeTextView = (TextView) findViewById(R.id.notime_textview);
        _addLogLinearLayout = (LinearLayout) findViewById(R.id.addlog_linearlayout);
        _addLogLinearLayout.setOnClickListener(_addLog_onClick);
        _devicesTextView = (TextView) findViewById(R.id.devices_textview);
    }

    private void enableDevices(boolean enabled) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _devicesTextView.getLayoutParams();
        if (enabled) {
            params.weight = 1;
            _devicesTextView.setLayoutParams(params);
            _devicesTextView.setVisibility(View.VISIBLE);
        } else {
            params.weight = 0;
            _devicesTextView.setLayoutParams(params);
            _devicesTextView.setVisibility(View.GONE);
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }


    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;

        final LoggedWork[] logs = _workorder.getLoggedWork();

        if (_workorder.canModifyTimeLog()) {
            _addLogLinearLayout.setVisibility(View.VISIBLE);
        } else {
            if (logs == null || logs.length == 0) {
                setVisibility(GONE);
                return;
            }
            _addLogLinearLayout.setVisibility(View.GONE);
        }

        setVisibility(View.VISIBLE);

        if (logs == null || logs.length == 0) {
            _noTimeTextView.setVisibility(View.VISIBLE);
            return;
        }
        _noTimeTextView.setVisibility(View.GONE);

        if (_workorder != null && _workorder.getPay() != null)
            enableDevices(_workorder.getPay().isPerDeviceRate());

        //_logList.removeAllViews();

//        for (int i = 0; i < logs.length; i++) {
//            LoggedWork log = logs[i];
//            ScheduleDetailView v = new ScheduleDetailView(getContext());
//            v.setListener(_scheduleDetailView_listener);
//            v.setData(_workorder, log);
//            _logList.addView(v);
//        }

        ForLoopRunnable r = new ForLoopRunnable(logs.length, new Handler()) {
            private LoggedWork[] _logs = logs;

            @Override
            public void next(int i) throws Exception {
                ScheduleDetailView v = null;
                if (i < _logList.getChildCount()) {
                    v = (ScheduleDetailView) _logList.getChildAt(i);
                } else {
                    v = new ScheduleDetailView(getContext());
                    _logList.addView(v);
                }
                LoggedWork log = _logs[i];
                v.setListener(_scheduleDetailView_listener);
                v.setData(_workorder, log);
            }

            @Override
            public void finish(int count) throws Exception {
                if (_logList.getChildCount() > count) {
                    _logList.removeViews(count - 1, _logList.getChildCount() - count);
                }
            }
        };
        post(r);
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private ScheduleDetailView.Listener _scheduleDetailView_listener = new ScheduleDetailView.Listener() {
        @Override
        public void editWorklog(Workorder workorder, LoggedWork loggedWork, boolean showDeviceCount) {
            if (_workorder.canModifyTimeLog()) {
                if (_listener != null)
                    _listener.editWorklog(workorder, loggedWork, showDeviceCount);
            }
        }

        @Override
        public void deleteWorklog(final ScheduleDetailView view, final Workorder workorder, final LoggedWork loggedWork) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are you sure you want to delete this work log?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    _logList.removeView(view);
                    if (_listener != null)
                        _listener.deleteWorklog(workorder, loggedWork);
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    };

    private View.OnClickListener _addLog_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean showdevices = false;
            try {
                showdevices = _workorder.getPay().isPerDeviceRate();
            } catch (Exception ex) {
            }

            if (_workorder.canModifyTimeLog()) {
                if (_listener != null)
                    _listener.addWorklog(showdevices);
            }
        }
    };

//    private WorkLogDialog.Listener _worklogdialog_listener = new WorkLogDialog.Listener() {
//        @Override
//        public void onOk(Calendar start, Calendar end, int deviceCount) {
//            if (deviceCount <= 0) {
//                getContext().startService(_service.logTime(WEB_SUBMIT_WORKLOG, _workorder.getWorkorderId(), start.getTimeInMillis(),
//                        end.getTimeInMillis()));
//            } else {
//                getContext().startService(_service.logTime(WEB_SUBMIT_WORKLOG, _workorder.getWorkorderId(), start.getTimeInMillis(),
//                        end.getTimeInMillis(), deviceCount));
//            }
//        }
//
//        @Override
//        public void onCancel() {
//        }
//    };

    public interface Listener {
        public void addWorklog(boolean showdevice);

        public void editWorklog(Workorder workorder, LoggedWork loggedWork, boolean showDeviceCount);

        public void deleteWorklog(Workorder workorder, LoggedWork loggedWork);

    }

//    private AuthenticationClient _authClient = new AuthenticationClient() {
//
//        @Override
//        public void onAuthenticationFailed(Exception ex) {
//            _gs.requestAuthenticationDelayed(_authClient);
//        }
//
//        @Override
//        public void onAuthentication(String username, String authToken) {
//            _service = new WorkorderService(getContext(), username, authToken, _resultReceiver);
//        }
//
//        @Override
//        public GlobalState getGlobalState() {
//            return _gs;
//        }
//    };
//
//    private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {
//
//        @Override
//        public void onSuccess(int resultCode, Bundle resultData) {
//            if (resultCode == WEB_SUBMIT_WORKLOG) {
//                Toast.makeText(getContext(), "Success!", Toast.LENGTH_LONG).show();
//                _workorder.dispatchOnChange();
//            }
//        }
//
//        @Override
//        public void onError(int resultCode, Bundle resultData, String errorType) {
//            Log.v(TAG, "onError()");
//            Log.v(TAG, resultData.getString(WebServiceConstants.KEY_RESPONSE_ERROR));
//        }
//    };
}
