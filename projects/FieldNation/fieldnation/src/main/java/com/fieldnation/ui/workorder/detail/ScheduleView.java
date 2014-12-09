package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;

public class ScheduleView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.ScheduleView";


    // UI
    private Button _addButton;
    private LinearLayout _workLogLinearLayout;

    // Data
    private Listener _listener;
    private Workorder _workorder;

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

        _workLogLinearLayout = (LinearLayout) findViewById(R.id.worklog_linearlayout);
        _addButton = (Button) findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
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
                v.setListener(_scheduleDetailView_listener);
                v.setData(_workorder, loggedWork[i]);
            }
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private ScheduleDetailView.Listener _scheduleDetailView_listener = new ScheduleDetailView.Listener() {
        @Override
        public void editWorklog(Workorder workorder, LoggedWork loggedWork, boolean showDeviceCount) {
            if (_listener != null)
                _listener.editWorklog(workorder, loggedWork, showDeviceCount);
        }

        @Override
        public void deleteWorklog(Workorder workorder, LoggedWork loggedWork) {
            if (_listener != null)
                _listener.deleteWorklog(workorder, loggedWork);
        }
    };

    private View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean showdevices = false;
            try {
                showdevices = _workorder.getPay().isPerDeviceRate();
            } catch (Exception ex) {

            }

            if (_listener != null)
                _listener.onAddWorklog(_workorder, showdevices);
        }
    };

//    private WorkLogDialog.Listener _worklogdialog_onOk = new WorkLogDialog.Listener() {
//        @Override
//        public void onOk(Calendar start, Calendar end, int deviceCount) {
//            getContext().startService(
//                    _service.logTime(WEB_SUBMIT_WORKLOG, _workorder.getWorkorderId(), start.getTimeInMillis(),
//                            end.getTimeInMillis()));
//        }
//
//        @Override
//        public void onCancel() {
//        }
//    };

    public interface Listener {
        public void onAddWorklog(Workorder workorder, boolean showDeviceCount);

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
//
//            Log.v(TAG, "onError()");
//            Log.v(TAG, resultData.getString(WebServiceConstants.KEY_RESPONSE_ERROR));
//        }
//    };

}
