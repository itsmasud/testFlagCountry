package com.fieldnation;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

public abstract class PagingListAdapter extends BaseAdapter {
	private static final String TAG = "PagingListAdapter";

	private GlobalState _gs;
	private Context _context;
	private boolean _isViable;
	private int _nextPage = 0;
	private boolean _atEndOfList;
	private JsonArray _objects = null;
	private boolean _allowCache = true;
	private String _authToken;
	private String _username;
	private ProgressBar _progressBar;
	private MyAuthClient _authClient;
	private Listener _listener = null;

	public PagingListAdapter(Context context) {
		_context = context;

		_progressBar = new ProgressBar(context);
		_authClient = new MyAuthClient(context);
		_gs = (GlobalState) context.getApplicationContext();
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
		if (_objects == null || _objects.size() == 0) {
			return 0;
		}

		if (_atEndOfList) {
			return _objects.size();
		} else {
			return _objects.size() + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		if (position >= _objects.size()) {
			return null;
		} else {
			return _objects.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected View getProgressBar() {
		return _progressBar;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= _objects.size()) {
			getNextPage();
			return getProgressBar();
		} else {
			return getView(_objects.getJsonObject(position), convertView,
					parent);
		}
	}

	public abstract View getView(JsonObject obj, View convertView,
			ViewGroup parent);

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
			rebuildWebService(_context, _username, _authToken, _resultReciever);
			update(false);
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
			Log.v(TAG, "WebServiceResultReciever.onSuccess");
			if (!_isViable) {
				Log.v(TAG, "WebServiceResultReciever.onSuccess(), not viable");
				return;
			}
			_nextPage++;
			String data = new String(
					resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));

			JsonArray objects = null;
			try {
				objects = new JsonArray(data);
			} catch (Exception ex) {
				// TODO report problem?
				Log.v(TAG, data);
				ex.printStackTrace();
			}
			if (objects == null) {
				Log.v(TAG, "objects is null");
				notifyDataSetChanged();
				return;
			}

			_objects.merge(objects);

			if (objects.size() < 25) {
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
		if (!_isViable) {
			Log.v(TAG, "not running!");
			return;
		}

		Log.v(TAG, "Starting query ");
		_allowCache = allowCache;

		_nextPage = 0;
		_atEndOfList = false;
		_objects = new JsonArray();
		getNextPage();
	}

	public void getNextPage() {
		if (!_isViable) {
			Log.v(TAG, "not running![2]");
			return;
		}

		dispatchOnLoading();
		if (_authToken == null) {
			Log.v(TAG, "Waiting for accessToken");
			invalidateWebervice();
			_gs.requestAuthentication(_authClient);
		} else {
			Log.v(TAG, "Have accessToken");
			getWebService(_context, _username, _authToken, _resultReciever);
			executeWebService(_nextPage, _allowCache);
		}
	}

	public abstract void invalidateWebervice();

	public abstract void getWebService(Context context, String username,
			String authToken, ResultReceiver resultReciever);

	public abstract void rebuildWebService(Context context, String username,
			String authToken, ResultReceiver resultReciever);

	public abstract void executeWebService(int page, boolean allowCache);

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
