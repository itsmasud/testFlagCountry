package com.fieldnation.ui;

import java.text.ParseException;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActionBarView extends RelativeLayout {
	private static final String TAG = "ui.NotificationActionBarView";

	// UI
	private TextView _countTextView;

	// data
	private GlobalState _gs;
	private ProfileService _profileService;
	private Profile _profile = null;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public NotificationActionBarView(Context context) {
		this(context, null, -1);
	}

	public NotificationActionBarView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public NotificationActionBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_notification_action_bar, this);

		_countTextView = (TextView) findViewById(R.id.count_textview);

		if (isInEditMode())
			return;

		_gs = (GlobalState) context.getApplicationContext();
		setOnClickListener(_this_onClick);

		_gs.requestAuthentication(_authclient);
	}

	private View.OnClickListener _this_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), NotificationListActivity.class);
			getContext().startActivity(intent);
		}
	};

	private AuthenticationClient _authclient = new AuthenticationClient() {

		@Override
		public void onAuthenticationFailed(Exception ex) {
			Log.v(TAG, "onAuthenticationFailed(), delayed re-request");
			_gs.requestAuthenticationDelayed(_authclient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_profileService = new ProfileService(getContext(), username, authToken, _resultReciever);
			getContext().startService(_profileService.getMyUserInformation(0, true));
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {
		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "WebServiceResultReceiver.onSuccess");
			String raw;
			try {
				raw = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
				Log.v(TAG, raw);
				JsonObject obj = new JsonObject(raw);
				_profile = Profile.fromJson(obj);
				refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");
		}
	};

	private void refresh() {
		if (_profile == null)
			return;

		int count = _profile.getNewNotificationCount();

		if (count == 0) {
			_countTextView.setVisibility(GONE);
		} else {
			_countTextView.setVisibility(VISIBLE);
			if (count >= 99) {
				_countTextView.setText("!!");
			} else {
				_countTextView.setText(count + "");
			}
		}
	}
}