package com.fieldnation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReciever;

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

/**
 * A list adapter that loads work order lists.
 * 
 * @author michael.carver
 * 
 */
public class WorkorderListAdapter extends BaseAdapter {
	private static final String TAG = "WorkorderListAdapter";

	private static final int RPC_GET = 1;

	public static final String KEY_PARAM_PAGE = "PARAM_PAGE";

	private GlobalState _gs;
	private Context _context;
	private JsonArray _workorders = null;
	private WorkorderService _workorderRpc;
	private Method _rpcMethod;
	private boolean _viable;
	private boolean _allowCache = true;
	private MyAuthenticationClient _authClient;
	private String _authToken;
	private String _username;
	private DataSelector _dataSelection;
	private int _nextPage;
	private boolean _atEndOfList;
	private Listener _listener = null;

	/*-*****************************-*/
	/*-			Lifecycle			-*/
	/*-*****************************-*/

	public WorkorderListAdapter(Context context, DataSelector selection) throws NoSuchMethodException {
		// Log.v(TAG, "WorkorderListAdapter");
		_context = context;
		_gs = (GlobalState) context.getApplicationContext();
		_authClient = new MyAuthenticationClient(context);
		_gs.requestAuthentication(_authClient);
		_dataSelection = selection;

		_viable = true;

		_rpcMethod = WorkorderService.class.getDeclaredMethod(
				selection.getCall(),
				new Class<?>[] { int.class, int.class, boolean.class });
		_rpcMethod.setAccessible(true);
		_nextPage = 0;
		_atEndOfList = false;

	}

	/**
	 * Will stop the task from looking up work orders
	 */
	public void onStop() {
		// Log.v(TAG, "onStop");
		notifyDataSetInvalidated();
		_viable = false;
	}

	/*-*********************************-*/
	/*-			Getters/Setters			-*/
	/*-*********************************-*/
	public boolean isViable() {
		// Log.v(TAG, "isViable");
		return _viable;
	}

	@Override
	public int getCount() {
		if (_workorders == null || _workorders.size() == 0) {
			// Log.v(TAG, "getCount=0");
			return 0;
		}

		if (_atEndOfList) {
			// Log.v(TAG, "getCount=_workorders.size()");
			return _workorders.size();
		} else {
			// Log.v(TAG, "getCount=_workorders.size() + 1");
			return _workorders.size() + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		if (position >= _workorders.size()) {
			// Log.v(TAG, "getItem(" + position + ")=null");
			return null;
		} else {
			// Log.v(TAG, "getItem(" + position +
			// ")=_workorders.get(position)");
			return _workorders.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= _workorders.size()) {
			// Log.v(TAG, "getView(), next page");
			getNextPage();
			return new ProgressBar(_context);
		} else {
			// Log.v(TAG, "getView(), make view");
			WorkorderSummaryView wosum = null;

			if (convertView == null) {
				wosum = new WorkorderSummaryView(parent.getContext());
			} else if (convertView instanceof WorkorderSummaryView) {
				wosum = (WorkorderSummaryView) convertView;
			} else {
				wosum = new WorkorderSummaryView(parent.getContext());
			}

			wosum.setWorkorder(_dataSelection,
					_workorders.getJsonObject(position));

			return wosum;
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
	private class MyAuthenticationClient extends AuthenticationClient {

		public MyAuthenticationClient(Context context) {
			super(context);
		}

		@Override
		public void onAuthentication(String username, String accessToken) {
			if (!_viable) {
				Log.v(TAG,
						"MyAuthenticationClient.onAuthentication(), not viable");
				return;
			}
			_username = username;
			_authToken = accessToken;
			_workorderRpc = new WorkorderService(_context, _username,
					_authToken, _webRpcReceiver);
			Log.v(TAG,
					"onAuthentication(" + username + ", " + accessToken + ")");
			update(false);
		}
	}

	private WebServiceResultReciever _webRpcReceiver = new WebServiceResultReciever(
			new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "WebServiceResultReciever.onSuccess");
			if (!_viable) {
				Log.v(TAG, "WebServiceResultReciever.onSuccess(), not viable");
				return;
			}
			_nextPage++;
			// int page = resultData.getInt(KEY_PARAM_PAGE);
			String data = new String(
					resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));

			JsonArray orders = null;
			try {
				orders = new JsonArray(data);
			} catch (Exception ex) {
				// TODO report problem?
				Log.v(TAG, data);
				ex.printStackTrace();
			}
			if (orders == null) {
				Log.v(TAG, "orders is null");
				notifyDataSetChanged();
				return;
			}

			_workorders.merge(orders);

			if (orders.size() < 25) {
				Log.v(TAG, "_atEndOfList");
				_atEndOfList = true;
			}
			notifyDataSetChanged();
			dispatchOnLoadComplete();
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (!_viable) {
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
		if (!_viable) {
			Log.v(TAG, "not running!");
			return;
		}

		Log.v(TAG, "Starting query: " + _rpcMethod.getName());
		_allowCache = allowCache;

		_nextPage = 0;
		_atEndOfList = false;
		_workorders = new JsonArray();
		getNextPage();
	}

	public void getNextPage() {
		if (!_viable) {
			Log.v(TAG, "not running![2]");
			return;
		}

		dispatchOnLoading();
		if (_authToken == null) {
			Log.v(TAG, "Waiting for accessToken");
			_workorderRpc = null;
			_gs.requestAuthentication(_authClient);
		} else {
			Log.v(TAG, "Have accessToken");
			if (_workorderRpc == null) {
				_workorderRpc = new WorkorderService(_context, _username,
						_authToken, _webRpcReceiver);
			}
			try {
				Intent intent = callRpc(RPC_GET, _nextPage, _allowCache);
				intent.putExtra(KEY_PARAM_PAGE, _nextPage);
				_context.startService(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Intent callRpc(int resultCode, int page, boolean allowCache) {
		try {
			return (Intent) _rpcMethod.invoke(_workorderRpc, resultCode, page,
					allowCache);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
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
