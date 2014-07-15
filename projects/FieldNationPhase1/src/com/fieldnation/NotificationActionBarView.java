package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActionBarView extends RelativeLayout {
	private static final String TAG = "NotificationActionBarView";
	private TextView _countTextView;
	
	private GlobalState _gs;
	private MyAuthClient _authclient;
	private ProfileService _profileService;
	private int _nextPage = 1;
	private int _notificationCount = 0;
	
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

		_authclient = new MyAuthClient(context);
		_gs.requestAuthentication(_authclient);
	}

	private View.OnClickListener _this_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");

		}
	};
	
	private class MyAuthClient extends AuthenticationClient {

		public MyAuthClient(Context context) {
			super(context);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_profileService = new ProfileService(getContext(), username, authToken, _resultReciever);
			getContext().startService(_profileService.getAllNotifications(0, _gs.USER_ID, _nextPage, true));
			_nextPage++;
			
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			Log.v(TAG, "onAuthenticationFailed(), delayed re-request");
			_gs.requestAuthenticationDelayed(_authclient);

		}

	}
	
	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "WebServiceResultReceiver.onSuccess");
			try {
				JsonArray ja = new JsonArray(new String(
						resultData.getByteArray((WebServiceConstants.KEY_RESPONSE_DATA))));
				int count = ja.size();
				
				_notificationCount += count;
				if (_notificationCount >= 99) {
					setCount(_notificationCount, false);
				} else if (count == 25) {
					setCount(_notificationCount, true);
					getContext().startService(_profileService.getAllNotifications(0, _gs.USER_ID, _nextPage, true));
					_nextPage++;
				} else {
					setCount(_notificationCount, false);
				}
			} catch (ParseException e) {
				e.printStackTrace();

			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");

		}
	};
	
	public void setCount(int count, boolean stillLoading) {
		if (count == 0) {
			_countTextView.setVisibility(GONE);
		} else {
			_countTextView.setVisibility(VISIBLE);
			if (count >= 99) {
				_countTextView.setText("!!");
			} else {
				if (stillLoading) {
					// TODO figure out why "+" is not showing up
					_countTextView.setText(count + "+");
				} else {
					_countTextView.setText(count + "");
				}
			}
		}
	}
}
