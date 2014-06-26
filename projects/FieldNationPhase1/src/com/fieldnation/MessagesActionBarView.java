package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.json.JsonArray;
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

public class MessagesActionBarView extends RelativeLayout {
	private static final String TAG = "MessagesActionBarView";
	private TextView _countTextView;

	private GlobalState _gs;
	private MyAuthClient _authclient;
	private ProfileService _profileService;
	private int _nextPage = 0;
	private int _messageCount = 0;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public MessagesActionBarView(Context context) {
		this(context, null, -1);
	}

	public MessagesActionBarView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MessagesActionBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_messages_action_bar, this);

		_countTextView = (TextView) findViewById(R.id.count_textview);

		if (isInEditMode())
			return;

		_gs = (GlobalState) context.getApplicationContext();

		setOnClickListener(_this_onClickListener);

		_authclient = new MyAuthClient(context);
		_gs.requestAuthentication(_authclient);
	}

	private View.OnClickListener _this_onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), MessageListActivity.class);
			getContext().startActivity(intent);
		}
	};

	private class MyAuthClient extends AuthenticationClient {

		public MyAuthClient(Context context) {
			super(context);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_profileService = new ProfileService(getContext(), username,
					authToken, _resultReciever);
			getContext().startService(
					_profileService.getAllMessages(0, 1, _nextPage, true));
			_nextPage++;

		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			// TODO Method Stub: onAuthenticationFailed()
			Log.v(TAG, "Method Stub: onAuthenticationFailed()");

		}

	}

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(
			new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "WebServiceResultReceiver.onSuccess");
			try {
				JsonArray ja = new JsonArray(
						new String(
								resultData.getByteArray((WebServiceConstants.KEY_RESPONSE_DATA))));
				int count = ja.size();

				_messageCount += count;
				if (_messageCount >= 99) {
					setCount(_messageCount, false);
				} else if (count == 25) {
					setCount(_messageCount, true);
					getContext().startService(
							_profileService.getAllMessages(0, 1, _nextPage,
									true));
					_nextPage++;
				} else {
					setCount(_messageCount, false);
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
