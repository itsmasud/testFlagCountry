package com.fieldnation.ui.workorder;

import java.text.ParseException;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
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
	private static final int WEB_GET_DETAILS = 2;

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
			startService(_service.getDetails(WEB_GET_DETAILS, _workorderId, false));
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
			byte[] data = resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA);
			Log.v(TAG, new String(data));
			if (resultCode == WEB_GET_DETAILS) {

			} else if (resultCode == WEB_GET_BUNDLE) {
				try {
					Workorder workorder = Workorder.fromJson(new JsonObject(new String(data)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authclient);
		}
	};

	@Override
	public void onRefresh() {
		// TODO Method Stub: onRefresh()
		Log.v(TAG, "Method Stub: onRefresh()");

	}

}
