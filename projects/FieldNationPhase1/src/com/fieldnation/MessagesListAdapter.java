package com.fieldnation;

import java.lang.reflect.InvocationTargetException;

import com.fieldnation.WorkorderListAdapter.Listener;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MessagesListAdapter extends BaseAdapter {
	private static final String TAG = "MessagesListAdapter";

	private GlobalState _gs;
	private Context _context;
	private MyAuthClient _authClient;
	private boolean _isViable;
	private int _nextPage = 0;
	private boolean _atEndOfList;
	private JsonArray _messages = null;
	private Listener _listener = null;
	private boolean _allowCache = true;
	private String _authToken;
	private String _username;
	private ProfileService _profileService;

	public MessagesListAdapter(Context context) {
		_context = context;
		_gs = (GlobalState) context.getApplicationContext();
		_authClient = new MyAuthClient(_context);
		_gs.requestAuthentication(_authClient);

		_isViable = true;

		_nextPage = 0;
		_atEndOfList = false;
	}

	/*-*********************************-*/
	/*-			Getters/Setters			-*/
	/*-*********************************-*/

	public boolean isViable() {
		return _isViable;
	}

	@Override
	public int getCount() {
		if (_messages == null || _messages.size() == 0) {
			return 0;
		}

		if (_atEndOfList) {
			return _messages.size();
		} else {
			return _messages.size() + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		if (position >= _messages.size()) {
			return null;
		} else {
			return _messages.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= _messages.size()) {
			// Log.v(TAG, "getView(), next page");
			getNextPage();
			return new ProgressBar(_context);
		} else {
			// Log.v(TAG, "getView(), make view");
			MessageView mv = null;

			if (convertView == null) {
				mv = new MessageView(parent.getContext());
			} else if (convertView instanceof MessageView) {
				mv = (MessageView) convertView;
			} else {
				mv = new MessageView(parent.getContext());
			}

			mv.setMessage(_messages.getJsonObject(position));

			return mv;
		}
	}

	/*-*****************************-*/
	/*-			Dispatchers			-*/
	/*-*****************************-*/
	public void setLoadingListener(Listener listener) {
		_listener = listener;
	}

	private void dispatchOnLoading() {
		if (_listener != null)
			_listener.onLoading();
	}

	private void dispatchOnLoadComplete() {
		if (_listener != null)
			_listener.onLoadComplete();
	}

	/*-*********************************-*/
	/*-			Event Handlers			-*/
	/*-*********************************-*/

	/**
	 * Will stop the task from looking up work orders
	 */
	public void onStop() {
		// Log.v(TAG, "onStop");
		notifyDataSetInvalidated();
		_isViable = false;
	}

	private class MyAuthClient extends AuthenticationClient {

		public MyAuthClient(Context context) {
			super(context);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			if (!_isViable) {
				Log.v(TAG,
						"MyAuthenticationClient.onAuthentication(), not viable");
				return;
			}
			_username = username;
			_authToken = authToken;
			_profileService = new ProfileService(_context, _username,
					_authToken, _resultReceiver);
			update(false);
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			// TODO Method Stub: onAuthenticationFailed()
			Log.v(TAG, "Method Stub: onAuthenticationFailed()");

		}

	}

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(
			new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "WebServiceResultReciever.onSuccess");
			if (!_isViable) {
				Log.v(TAG, "WebServiceResultReciever.onSuccess(), not viable");
				return;
			}
			_nextPage++;
			String data = new String(
					resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));

			JsonArray messages = null;
			try {
				messages = new JsonArray(data);
			} catch (Exception ex) {
				// TODO report problem?
				Log.v(TAG, data);
				ex.printStackTrace();
			}
			if (messages == null) {
				Log.v(TAG, "messages is null");
				notifyDataSetChanged();
				return;
			}

			_messages.merge(messages);

			if (messages.size() < 25) {
				Log.v(TAG, "_atEndOfList");
				_atEndOfList = true;
			}
			notifyDataSetChanged();
			dispatchOnLoadComplete();
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (!_isViable) {
				Log.v(TAG, "WebServiceResultReciever.onError(), not viable");
				return;
			}

			Log.v(TAG, errorType);
			Log.v(TAG, resultData.toString());
			_gs.invalidateAuthToken(_authToken);
			_authToken = null;
			_username = null;

			if (WebServiceConstants.ERROR_SESSION_INVALID.equals(errorType)) {
				_gs.requestAuthentication(_authClient);
			} else {
				// TODO, this is a bit dumb, but it works
				Toast.makeText(
						_context,
						"Could not get data. Please check your network and try again.",
						Toast.LENGTH_LONG).show();
			}
			notifyDataSetChanged();
		}
	};

	/*-*****************************-*/
	/*-			Processing			-*/
	/*-*****************************-*/
	/**
	 * Starts getting the list of work orders
	 * 
	 * @param allowCache
	 *            if true, then it will use the local cache if available
	 */
	public void update(boolean allowCache) {
		Log.v(TAG, "update()");
		if (!isViable()) {
			Log.v(TAG, "not running!");
			return;
		}

		Log.v(TAG, "Starting query ");
		_allowCache = allowCache;

		_nextPage = 0;
		_atEndOfList = false;
		_messages = new JsonArray();
		getNextPage();
	}

	public void getNextPage() {
		if (!isViable()) {
			Log.v(TAG, "not running![2]");
			return;
		}

		dispatchOnLoading();
		if (_authToken == null) {
			Log.v(TAG, "Waiting for accessToken");
			_profileService = null;
			_gs.requestAuthentication(_authClient);
		} else {
			Log.v(TAG, "Have accessToken");
			if (_profileService == null) {
				_profileService = new ProfileService(_context, _username,
						_authToken, _resultReceiver);
			}
			try {
				// TODO get userid?
				_context.startService(_profileService.getAllMessages(0, 1,
						_nextPage, _allowCache));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public interface Listener {
		/**
		 * Called when loading a page has started
		 */
		public void onLoading();

		/**
		 * Called when loading complete
		 */
		public void onLoadComplete();
	}

}
