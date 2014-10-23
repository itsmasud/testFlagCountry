package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.Calendar;

public class ScheduleView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.ScheduleView";

    private static final int WEB_SUBMIT_WORKLOG = 1;

    // UI
    private Button _addButton;
    private LinearLayout _workLogLinearLayout;
    private WorkLogDialog _workLogDialog;

    // Data
    private GlobalState _gs;
    private Workorder _workorder;
    private WorkorderService _service;
    private FragmentManager _fm;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public ScheduleView(Context context) {
        super(context);
        init();
    }

    public ScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_schedule, this);

        if (isInEditMode())
            return;
        _gs = (GlobalState) getContext().getApplicationContext();
        _gs.requestAuthentication(_authClient);

        _workLogDialog = new WorkLogDialog(getContext());

        _workLogLinearLayout = (LinearLayout) findViewById(R.id.worklog_linearlayout);
        _addButton = (Button) findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);
    }

    public void setFragmentManager(FragmentManager fm) {
        _fm = fm;
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        LoggedWork[] loggedWork = _workorder.getLoggedWork();

        _workLogLinearLayout.removeAllViews();

        if (loggedWork != null) {
            for (int i = 0; i < loggedWork.length; i++) {
                ScheduleDetailView v = new ScheduleDetailView(getContext());
                _workLogLinearLayout.addView(v);
                v.setData(_workorder, loggedWork[i]);
            }
        }
    }

    /*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
    private View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean showdevices = false;
            try {
                showdevices = _workorder.getPay().isPerDeviceRate();
            } catch (Exception ex) {

            }
            _workLogDialog.show(_fm, "Add a worklog", null, showdevices, _worklogdialog_onOk);
        }
    };

    private WorkLogDialog.Listener _worklogdialog_onOk = new WorkLogDialog.Listener() {
        @Override
        public void onOk(Calendar start, Calendar end, int deviceCount) {
            getContext().startService(
                    _service.logTime(WEB_SUBMIT_WORKLOG, _workorder.getWorkorderId(), start.getTimeInMillis(),
                            end.getTimeInMillis()));
        }

        @Override
        public void onCancel() {
        }
    };

    public interface Listener {
        public void onAddWorklog(Calendar start, Calendar end, int deviceCount, boolean isOnSiteTime);
    }

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
