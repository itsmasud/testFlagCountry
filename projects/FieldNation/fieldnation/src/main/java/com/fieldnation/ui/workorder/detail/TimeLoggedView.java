package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.dialog.WorkLogDialog;

import java.util.Arrays;
import java.util.Calendar;

public class TimeLoggedView extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.TimeLoggedView";

    private static final int WEB_SUBMIT_WORKLOG = 1;

    // Ui
    private LinearLayout _logList;
    private TextView _totalTimeTextView;
    private LinearLayout _addLogLinearLayout;
    private TextView _noTimeTextView;
    private WorkLogDialog _dialog;

    // Data
    private GlobalState _gs;
    private Workorder _workorder;
    private FragmentManager _fm;
    private WorkorderService _service;
    private Integer[] woStatus = { 5, 6, 7 }; //work order status approved, paid, canceled

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
        _totalTimeTextView = (TextView) findViewById(R.id.totaltime_textview);
        _noTimeTextView = (TextView) findViewById(R.id.notime_textview);
        _addLogLinearLayout = (LinearLayout) findViewById(R.id.addlog_linearlayout);
        _addLogLinearLayout.setOnClickListener(_addLog_onClick);

        _dialog = new WorkLogDialog(getContext());

        _gs = (GlobalState) getContext().getApplicationContext();
        _gs.requestAuthentication(_authClient);
    }

    public void setFragmentManager(FragmentManager fm) {
        _fm = fm;
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

        LoggedWork[] logs = _workorder.getLoggedWork();

        if (logs == null || logs.length == 0) {
            _noTimeTextView.setVisibility(View.VISIBLE);
            return;
        }
        _noTimeTextView.setVisibility(View.GONE);

        _logList.removeAllViews();
        for (int i = 0; i < logs.length; i++) {
            LoggedWork log = logs[i];
            ScheduleDetailView v = new ScheduleDetailView(getContext());
            v.setFragmentManager(_fm);
            v.setData(_workorder, log);
            _logList.addView(v);
        }
    }

	/*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/

    private View.OnClickListener _addLog_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean showdevices = false;
            try {
                showdevices = _workorder.getPay().isPerDeviceRate();
            } catch (Exception ex) {
            }

            if(!Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                _dialog.show(_fm, "Add Worklog", null, showdevices, _worklogdialog_listener);
            }
        }
    };

    private WorkLogDialog.Listener _worklogdialog_listener = new WorkLogDialog.Listener() {
        @Override
        public void onOk(Calendar start, Calendar end, int deviceCount) {
            if (deviceCount <= 0) {
                getContext().startService(_service.logTime(WEB_SUBMIT_WORKLOG, _workorder.getWorkorderId(), start.getTimeInMillis(),
                        end.getTimeInMillis()));
            } else {
                getContext().startService(_service.logTime(WEB_SUBMIT_WORKLOG, _workorder.getWorkorderId(), start.getTimeInMillis(),
                        end.getTimeInMillis(), deviceCount));
            }
        }

        @Override
        public void onCancel() {
        }
    };

    private AuthenticationClient _authClient = new AuthenticationClient() {

        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            _service = new WorkorderService(getContext(), username, authToken, _resultReceiver);
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_SUBMIT_WORKLOG) {
                Toast.makeText(getContext(), "Success!", Toast.LENGTH_LONG).show();
                _workorder.dispatchOnChange();
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            Log.v(TAG, "onError()");
            Log.v(TAG, resultData.getString(WebServiceConstants.KEY_RESPONSE_ERROR));
        }
    };
}
