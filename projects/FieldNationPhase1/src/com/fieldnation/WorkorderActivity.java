package com.fieldnation;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

public class WorkorderActivity extends BaseActivity {
	private static final String TAG = "WorkorderActivity";

	private static final int RPC_GET_DETAIL = 1;

	// UI
	private EditText _detailEditText;

	// Data
	private GlobalState _gs;
	private String _username;
	private String _authToken;
	private long _workorderId = 0;

	// Services
	private MyAuthenticationClient _authClient;
	private WorkorderService _woRpc;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workorder);

		Intent intent = getIntent();
		if (intent != null) {
			if (intent.hasExtra("workorder_id")) {
				_workorderId = intent.getLongExtra("workorder_id", 0);
			}
		}

		if (_workorderId == 0) {
			// epic fail!
			Log.e(TAG, "must have a workorder id!");
			finish();
		}

		_detailEditText = (EditText) findViewById(R.id.detail_edittext);

		_authClient = new MyAuthenticationClient(this);
		_gs = (GlobalState) getApplicationContext();
		_gs.requestAuthentication(_authClient);

	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private class MyAuthenticationClient extends AuthenticationClient {
		public MyAuthenticationClient(Context context) {
			super(context);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_username = username;
			_authToken = authToken;

			_woRpc = new WorkorderService(WorkorderActivity.this, username, authToken, _rpcReciever);

			startService(_woRpc.getDetails(RPC_GET_DETAIL, _workorderId, false));
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			// TODO Method Stub: onAuthenticationFailed()
			Log.v(TAG, "Method Stub: onAuthenticationFailed()");

		}
	}

	private WebServiceResultReceiver _rpcReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "onSuccess()");
			Log.v(TAG, resultData.toString());

			try {
				_detailEditText.setText(new JsonObject(new String(
						resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA))).display());

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");

		}
	};

}
