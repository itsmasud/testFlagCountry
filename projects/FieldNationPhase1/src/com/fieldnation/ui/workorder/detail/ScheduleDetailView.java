package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;
import java.util.Calendar;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.dialog.WorkLogDialog;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleDetailView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.ScheduleSummaryView";
	
	private static final int WEB_SUBMIT_WORKLOG = 1;

	// UI
	private TextView _hoursTextView;
	private TextView _dateTextView;
	private TextView _startTextView;
	private TextView _endTextView;
	private ImageView _editImageView;
	private WorkLogDialog _workLogDialog;
	
	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private FragmentManager _fm;
	private WorkorderService _service;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ScheduleDetailView(Context context) {
		super(context);
		init();
	}

	public ScheduleDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScheduleDetailView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_schedule_detail, this);

		if (isInEditMode())
			return;

		_hoursTextView = (TextView) findViewById(R.id.hours_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
		_startTextView = (TextView) findViewById(R.id.start_textview);
		_endTextView = (TextView) findViewById(R.id.end_textview);
		_editImageView = (ImageView) findViewById(R.id.edit_imageview);
		_editImageView.setOnClickListener(_edit_onClick);
		
		_workLogDialog = new WorkLogDialog(getContext());
	}

	public void setLoggedWork(LoggedWork loggedWork) {
		String startDate = loggedWork.getStartDate();

		String date = "";
		try {
			date = misc.formatDateTime(ISO8601.toCalendar(startDate), false);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		try {
			String endDate = loggedWork.getEndDate();

			endDate = misc.formatDateTime(ISO8601.toCalendar(endDate), false);

			date += " to " + endDate;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// _timeTextView.setText(date);

		// TODO if endtime is not set, then calculate duration if and only if
		// the current time is still the same day as the start time.

		if (loggedWork.getHours() != null) {
			// _durationTextView.setText(loggedWork.getHours().intValue() +
			// "hrs");
		}
		
		//set work logged in work log dialog class
		_workLogDialog.setLoggedWork(loggedWork);
	}
		
	public void setFragmentManager(FragmentManager fm) {
		_fm = fm;
	}
	
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
	}

	private View.OnClickListener _edit_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {			
			_workLogDialog.show(_fm, "Edit Worklog", false, false, _worklogdialog_onOk);
		}
	};
	
	private WorkLogDialog.Listener _worklogdialog_onOk = new WorkLogDialog.Listener() {
		@Override
		public void onOk(Calendar start, Calendar end, int deviceCount, boolean isOnSiteTime) {
		}
		
		@Override
		public void onOk(Calendar start, Calendar end, int deviceCount, boolean isOnSiteTime, Integer workorderHoursId) {	
			Log.v(TAG, " onClick="+workorderHoursId+"="+start.getTimeInMillis()+"="+end.getTimeInMillis());
			//@TODO
			/*getContext().startService(
					_service.updateLogTime(WEB_SUBMIT_WORKLOG, workorderHoursId, start.getTimeInMillis(),
							end.getTimeInMillis(), false));*/
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
