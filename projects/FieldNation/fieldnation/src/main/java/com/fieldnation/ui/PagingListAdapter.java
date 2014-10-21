package com.fieldnation.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.cocosw.undobar.UndoBarController;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class PagingListAdapter<T> extends BaseAdapter {
	private static final String TAG = "ui.PagingListAdapter";

	private static final int START_PAGE = 0;

	private int WEB_REQUEST_UPDATE = -1;

	private GlobalState _gs;
	private FragmentActivity _activity;
	private boolean _isViable;
	private int _nextPage = START_PAGE;
	private boolean _atEndOfList;
	private List<T> _objects = null;
	private Class<T> _clazz = null;
	private boolean _allowCache = true;
	private String _authToken;
	private String _username;
	private View _progressBar = null;
	private Listener<T> _listener = null;
	private Random _rand = new Random(System.currentTimeMillis());

	public PagingListAdapter(FragmentActivity activity, Class<T> clazz) {
		_activity = activity;
		_clazz = clazz;

		_gs = (GlobalState) _activity.getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_isViable = true;
		_nextPage = START_PAGE;
		_atEndOfList = false;
	}

	public PagingListAdapter(FragmentActivity activity, Class<T> clazz, List<T> objects) {
		_activity = activity;
		_clazz = clazz;

		_gs = (GlobalState) _activity.getApplicationContext();
		_objects = objects;

		_isViable = true;
		_nextPage = START_PAGE;
		_atEndOfList = false;
	}

	/*-*********************************-*/
	/*-			Getters/Setters			-*/
	/*-*********************************-*/
	public String getUsername() {
		return _username;
	}

	public String getAuthToken() {
		return _authToken;
	}

	public Context getContext() {
		return _activity;
	}

	public FragmentActivity getActivity() {
		return _activity;
	}

	public boolean isViable() {
		return _isViable;
	}

	public List<T> getObjects() {
		return _objects;
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

	protected View getProgressBar(ViewGroup parent) {
		if (_progressBar == null) {
			_progressBar = _activity.getLayoutInflater().inflate(R.layout.view_centered_progressbar, parent, false);
		}
		return _progressBar;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= _objects.size()) {
			getNextPage();
			return getProgressBar(parent);
		} else {
			return getView(position, _objects.get(position), convertView, parent);
		}
	}

	/*-*****************************-*/
	/*-			Dispatchers			-*/
	/*-*****************************-*/
	public void setLoadingListener(Listener<T> listener) {
		_listener = listener;
	}

	private void dispatchOnLoading() {
		if (_listener != null) {
			_listener.onLoading();
		}
	}

	private void dispatchOnLoadComplete() {
		if (_listener != null) {
			_listener.onLoadComplete();
		}
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

	private AuthenticationClient _authClient = new AuthenticationClient() {

		@Override
		public void onAuthenticationFailed(Exception ex) {
			Log.v(TAG, "onAuthenticationFailed(), delayed re-request");
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			if (!_isViable) {
				Log.v(TAG, "MyAuthenticationClient.onAuthentication(), not viable");
				return;
			}
			_username = username;
			_authToken = authToken;
			rebuildWebService(_activity, _username, _authToken, _resultReciever);
			update(false);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {

			if (resultCode == WEB_REQUEST_UPDATE) {
				Log.v(TAG, "WebServiceResultReciever.onSuccess");
				if (!_isViable) {
					Log.v(TAG, "WebServiceResultReciever.onSuccess(), not viable");
					return;
				}
				_nextPage++;
				String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));

				JsonArray objects = null;
				try {
					objects = new JsonArray(data);
				} catch (Exception ex) {
					WEB_REQUEST_UPDATE = -Math.abs(_rand.nextInt());
					executeWebService(WEB_REQUEST_UPDATE, --_nextPage, _allowCache);
					notifyDataSetChanged();
					return;
				}
				for (int i = 0; i < objects.size(); i++) {
					try {
						T o = fromJson(objects.getJsonObject(i));
						if (includeObject(o)) {
							_objects.add(o);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (objects.size() == 0) {
					Log.v(TAG, "_atEndOfList");
					_atEndOfList = true;
				}
				dispatchOnLoadComplete();
				notifyDataSetChanged();
			} else {
				handleWebResult(resultCode, resultData);
			}
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
				// TODO, convert to string resource
				UndoBarController.show(_activity, "Could not get data. Please check your network and try again.");
				dispatchOnLoadComplete();
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

		_nextPage = START_PAGE;
		_atEndOfList = false;
		_objects = new LinkedList<T>();
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
			invalidateWebService();
			_gs.requestAuthentication(_authClient);
		} else {
			Log.v(TAG, "Have accessToken");
			getWebService(_activity, _username, _authToken, _resultReciever);
			WEB_REQUEST_UPDATE = -Math.abs(_rand.nextInt());
			executeWebService(WEB_REQUEST_UPDATE, _nextPage, _allowCache);
		}
	}

	/*-*************************************-*/
	/*-			Abstract Methods			-*/
	/*-*************************************-*/
	public boolean includeObject(T obj) {
		return true;
	}

	/**
	 * Will be called when the resultCode is not a value of 1
	 * 
	 * @param resultCode
	 * @param resultData
	 */
	public void handleWebResult(int resultCode, Bundle resultData) {
	}

	/**
	 * Called when a new view is needed by the listview
	 * 
	 * @param obj
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getView(int position, T obj, View convertView, ViewGroup parent);

	/**
	 * When authentication fails, or some other error happens that requires that
	 * the web sevice be restarted
	 */
	public abstract void invalidateWebService();

	/**
	 * This should create a new webservice if one doesn't already exist. If one
	 * exists, then this can be ignored.
	 * 
	 * @param context
	 * @param username
	 * @param authToken
	 * @param resultReceiver
	 */
	public abstract void getWebService(Context context, String username, String authToken, ResultReceiver resultReceiver);

	/**
	 * rebuild the web service with the new information
	 * 
	 * @param context
	 * @param username
	 * @param authToken
	 * @param resultReceiver
	 */
	public abstract void rebuildWebService(Context context, String username, String authToken,
			ResultReceiver resultReceiver);

	/**
	 * Execute the web service
	 * 
	 * @param resultCode
	 * @param page
	 * @param allowCache
	 */
	public abstract void executeWebService(int resultCode, int page, boolean allowCache);

	public T fromJson(JsonObject obj) {
		try {
			return Serializer.unserializeObject(_clazz, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*-*****************************-*/
	/*-			Listener			-*/
	/*-*****************************-*/
	public interface Listener<T> {
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
