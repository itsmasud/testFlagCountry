package com.fieldnation.ui.workorder.detail;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ActionView extends RelativeLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ActionView";

	private static final int WEB_REQUEST = 0;

	// UI
	private Button _requestButton;
	private Button _notInterestedButton;
	private Button _counterOfferButton;
	private Button _completeButton;
	private LinearLayout _buttonLayout;
	private ProgressBar _progressBar;

	// DATA
	private GlobalState _gs;
	private WorkorderService _service;
	private Workorder _workorder;

	/*-*****************************-*/
	/*-			Life Cycle			-*/
	/*-*****************************-*/
	public ActionView(Context context) {
		super(context);
		init();
	}

	public ActionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ActionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_actions, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) getContext().getApplicationContext();

		_requestButton = (Button) findViewById(R.id.request_button);
		_requestButton.setOnClickListener(_request_onClick);

		_notInterestedButton = (Button) findViewById(R.id.not_interested_button);
		_notInterestedButton.setOnClickListener(_notInterested_onClick);

		_counterOfferButton = (Button) findViewById(R.id.counteroffer_button);
		_counterOfferButton.setOnClickListener(_counteroffer_onClick);

		_completeButton = (Button) findViewById(R.id.complete_button);
		_completeButton.setOnClickListener(_complete_onClick);

		_buttonLayout = (LinearLayout) findViewById(R.id.button_layout);
		_progressBar = (ProgressBar) findViewById(R.id.progressBar);

		_gs.requestAuthentication(_authClient);
		setLoading(true);
	}

	private void setLoading(boolean isLoading) {
		if (isLoading) {
			_progressBar.setVisibility(View.VISIBLE);
			_buttonLayout.setVisibility(View.GONE);
		} else {
			_progressBar.setVisibility(View.GONE);
			_buttonLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		Log.v(TAG, "Method Stub: setWorkorder()");

		_workorder = workorder;

		WorkorderStatus stat = workorder.getStatus().getWorkorderStatus();

		_requestButton.setVisibility(View.GONE);
		_notInterestedButton.setVisibility(View.GONE);
		_counterOfferButton.setVisibility(View.GONE);
		_completeButton.setVisibility(View.GONE);

		switch (stat) {
		case APPROVED:
			break;
		case ASSIGNED:
			_completeButton.setVisibility(View.VISIBLE);
			break;
		case AVAILABLE:
			_requestButton.setVisibility(View.VISIBLE);
			_notInterestedButton.setVisibility(View.VISIBLE);
			_counterOfferButton.setVisibility(View.VISIBLE);
			break;
		case CANCELLED:
			break;
		case COMPLETED:
			break;
		case INPROGRESS:
			_completeButton.setVisibility(View.VISIBLE);
			break;
		case PAID:
			break;
		case NA:
			break;
		default:
			break;

		}
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private AuthenticationClient _authClient = new AuthenticationClient() {

		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getContext(), username, authToken, _resultReceiver);
			setLoading(false);
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			setLoading(false);
			_workorder.dispatchOnChange();
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			setLoading(false);
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authClient);
		}
	};

	private View.OnClickListener _request_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setLoading(true);
			// TODO get request timeout from client
			_gs.startService(_service.request(WEB_REQUEST, _workorder.getWorkorderId(), 0));
		}
	};

	private View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setLoading(true);
			_gs.startService(_service.decline(WEB_REQUEST, _workorder.getWorkorderId()));
		}
	};

	private View.OnClickListener _complete_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setLoading(true);
			_gs.startService(_service.complete(WEB_REQUEST, _workorder.getWorkorderId()));
		}
	};

	private View.OnClickListener _counteroffer_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setLoading(true);
			// TODO show counter offer dialog
		}
	};

}
