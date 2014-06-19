package com.fieldnation;

import java.net.PasswordAuthentication;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.rpc.WebRpc;
import com.fieldnation.service.rpc.WebRpcResultReciever;
import com.fieldnation.service.rpc.WorkorderRpc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.widget.EditText;

public class WorkorderDetailActivity extends BaseActivity {
	private static final String TAG = "WorkOrderDetailActivity";

	private static final int RPC_GET_DETAIL = 1;

	// UI
	private ActionBarDrawerToggle _drawerToggle;
	private DrawerLayout _drawerLayout;
	private EditText _detailEditText;

	// Data
	private GlobalState _gs;
	private String _username;
	private String _authToken;
	private long _workorderId = 0;

	// Services
	private MyAuthenticationClient _authClient;
	private WorkorderRpc _woRpc;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workorder_detail);

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

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);

		buildDrawer();

		_authClient = new MyAuthenticationClient(this);
		_gs = (GlobalState) getApplicationContext();
		_gs.requestAuthentication(_authClient);
	}

	private void buildDrawer() {
		_drawerLayout = (DrawerLayout) findViewById(R.id.container);
		_drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.launcher_open,
				R.string.launcher_open);

		_drawerLayout.setDrawerListener(_drawerToggle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		_drawerToggle.syncState();
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

			_woRpc = new WorkorderRpc(WorkorderDetailActivity.this, username,
					authToken, _rpcReciever);

			startService(_woRpc.getDetails(RPC_GET_DETAIL, _workorderId, false));
			// TODO Method Stub: onAuthentication()
			Log.v(TAG, "Method Stub: onAuthentication()");
		}
	}

	private WebRpcResultReciever _rpcReciever = new WebRpcResultReciever(
			new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			// TODO Method Stub: onSuccess()
			Log.v(TAG, "Method Stub: onSuccess()");
			Log.v(TAG, resultData.toString());

			try {
				_detailEditText.setText(new JsonObject(new String(
						resultData.getByteArray(WebRpc.KEY_RESPONSE_DATA))).display());
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
