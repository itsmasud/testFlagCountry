package com.fieldnation.ui.workorder;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class WorkorderBundleDetailActivity extends BaseActivity {
	private static final String TAG = "ui.workorder.WorkorderBundleDetailActivity";

	public static final String INTENT_FIELD_WORKORDER_ID = "com.fieldnation.ui.workorder.WorkorderBundleDetailActivity:workorder_id";

	private static final int WEB_GET_BUNDLE = 1;

	// UI
	// Data
	private GlobalState _gs;
	private long _workorderId = 0;
	private WorkorderService _service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bundle_detail);

		_gs = (GlobalState) getApplicationContext();

		Intent intent = getIntent();

		if (intent == null) {
			finish();
			return;
		}

		if (intent.hasExtra(INTENT_FIELD_WORKORDER_ID)) {
			_workorderId = intent.getLongExtra(INTENT_FIELD_WORKORDER_ID, -1);
		} else {
			finish();
			return;
		}

		if (_workorderId == -1) {
			finish();
			return;
		}

		_gs.requestAuthentication(_authclient);
		// TODO put into wait mode
	}

	private AuthenticationClient _authclient = new AuthenticationClient() {
		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authclient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(WorkorderBundleDetailActivity.this, username, authToken, _resultReciever);
			startService(_service.getBundle(WEB_GET_BUNDLE, _workorderId, false));
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			// TODO Method Stub: onSuccess()
			Log.v(TAG, "Method Stub: onSuccess()");
			Log.v(TAG, resultData.toString());
			byte[] data = resultData.getByteArray(WorkorderService.KEY_RESPONSE_DATA);
			Log.v(TAG, new String(data));
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");
			Log.v(TAG, resultData.toString());
		}
	};

	@Override
	public void onRefresh() {
		// TODO Method Stub: onRefresh()
		Log.v(TAG, "Method Stub: onRefresh()");
		
	}

}
