package com.fieldnation.ui.workorder.detail;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.payment.PayDialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ClosingNotesView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ClosingNotesView";

	private static final int WEB_SAVE_NOTES = 1;

	// UI
	private EditText _notesEditText;
	private Button _saveButton;
	private Button _requestButton;
	private Button _notInterestedButton;
	private Button _completeButton;
	private Button _counterOfferButton;
	private PayDialog _payDialog;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private WorkorderService _service;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ClosingNotesView(Context context) {
		this(context, null);
	}

	public ClosingNotesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_closing_notes, this);

		if (isInEditMode())
			return;
		_gs = (GlobalState) context.getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_notesEditText = (EditText) findViewById(R.id.notes_edittext);
		_saveButton = (Button) findViewById(R.id.save_button);
		_saveButton.setOnClickListener(_save_onClick);
		_requestButton = (Button) findViewById(R.id.request_button);
		_requestButton.setOnClickListener(_request_onClick);
		_notInterestedButton = (Button) findViewById(R.id.not_interested_button);
		_notInterestedButton.setOnClickListener(_notInterested_onClick);
		_completeButton = (Button) findViewById(R.id.complete_button);
		_completeButton.setOnClickListener(_complete_onClick);
		_counterOfferButton = (Button) findViewById(R.id.counteroffer_button);
		_counterOfferButton.setOnClickListener(_counter_onClick);

		_payDialog = new PayDialog(getContext());

		// TODO put UI into loading state
		_saveButton.setText("Loading...");

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _counter_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_payDialog.show();
		}
	};
	private View.OnClickListener _request_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _complete_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};
	private View.OnClickListener _save_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			getContext().startService(
					_service.closingNotes(WEB_SAVE_NOTES, _workorder.getWorkorderId(),
							_notesEditText.getText().toString()));
		}
	};

	private AuthenticationClient _authClient = new AuthenticationClient() {
		@Override
		public void onAuthenticationFailed(Exception ex) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getContext(), username, authToken, _resultReceiver);
			_saveButton.setText("Save");
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {
		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			_workorder.dispatchOnChange();
			// TODO Method Stub: onSuccess()
			Log.v(TAG, "Method Stub: onSuccess()");
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthentication(_authClient);
			// TODO put UI into loading state
		}
	};

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		refresh();
	}

	private void refresh() {
		_notesEditText.setText(_workorder.getClosingNotes());

	}

}
