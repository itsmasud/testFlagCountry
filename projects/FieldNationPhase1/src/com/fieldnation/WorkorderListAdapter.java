package com.fieldnation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fieldnation.json.JsonArray;
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

	// available method calls
	public static final String TYPE_REQUESTED = "getRequested";
	public static final String TYPE_AVAILABLE = "getAvailable";
	public static final String TYPE_PENDING_APPROVAL = "getPendingApproval";
	public static final String TYPE_ASSIGNED = "getAssigned";
	public static final String TYPE_COMPLETED = "getCompleted";
	public static final String TYPE_CANCELED = "getCanceled";

	private GlobalState _gs;
	private Context _context;
	private JsonArray _workorders = null;
	private WaitForFieldAsyncTask _waitForField;
	private WorkorderService _workorderRpc;
	private Method _rpcMethod;
	private boolean _viable;
	private boolean _allowCache = true;
	private MyAuthenticationClient _authClient;
	private String _authToken;
	private String _username;

	/*-*****************************-*/
	/*-			Lifecycle			-*/
	/*-*****************************-*/
	/**
	 * 
	 * @param context
	 * @param type
	 *            should be one of the TYPE_* strings
	 * @throws NoSuchMethodException
	 */
	public WorkorderListAdapter(Context context, String type) throws NoSuchMethodException {
		_context = context;
		_gs = (GlobalState) context.getApplicationContext();
		_authClient = new MyAuthenticationClient(context);
		_gs.requestAuthentication(_authClient);

		_viable = true;

		_rpcMethod = WorkorderService.class.getDeclaredMethod(type,
				new Class<?>[] { int.class, int.class, boolean.class });
		_rpcMethod.setAccessible(true);

	}

	/**
	 * Will stop the task from looking up work orders
	 */
	public void onStop() {
		notifyDataSetInvalidated();
		_viable = false;
	}

	/*-*********************************-*/
	/*-			Getters/Setters			-*/
	/*-*********************************-*/
	public boolean isViable() {
		return _viable;
	}

	@Override
	public int getCount() {
		if (_workorders == null)
			return 0;

		return _workorders.size();
	}

	@Override
	public Object getItem(int position) {
		return _workorders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WorkorderSummaryView wosum = null;

		if (convertView == null) {
			wosum = new WorkorderSummaryView(parent.getContext());
		} else if (convertView instanceof WorkorderSummaryView) {
			wosum = (WorkorderSummaryView) convertView;
		} else {
			wosum = new WorkorderSummaryView(parent.getContext());
		}

		wosum.setWorkorder(_workorders.getJsonObject(position));

		return wosum;
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
			_username = username;
			_authToken = accessToken;
			// TODO Method Stub: onAuthentication()
			_workorderRpc = new WorkorderService(_context, _username,
					_authToken, _webRpcReceiver);
			Log.v(TAG,
					"Method Stub: onAuthentication(" + username + ", " + accessToken + ")");
			update(false);
		}
	}

	private WebServiceResultReciever _webRpcReceiver = new WebServiceResultReciever(
			new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			int page = resultData.getInt(KEY_PARAM_PAGE);
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
				notifyDataSetChanged();
				return;
			}

			_workorders.merge(orders);

			notifyDataSetChanged();

			if (orders.size() == 25) {
				try {
					Intent intent = callRpc(RPC_GET, page + 1, _allowCache);

					intent.putExtra(KEY_PARAM_PAGE, page + 1);
					_context.startService(intent);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// chain is complete, re-enable caching
				_allowCache = true;
			}

		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
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

		_workorders = new JsonArray();
		if (_authToken == null) {
			Log.v(TAG, "Waiting for accessToken");

			_workorderRpc = null;
			// _waitForField = new WaitForFieldAsyncTask(
			// _waitForAccessToken_listener);
			// _waitForField.execute(_gs, "accessToken");
			_gs.requestAuthentication(_authClient);
		} else {
			Log.v(TAG, "Have accessToken");
			if (_workorderRpc == null) {
				_workorderRpc = new WorkorderService(_context, _username,
						_authToken, _webRpcReceiver);
			}
			try {
				Intent intent = callRpc(RPC_GET, 1, _allowCache);

				intent.putExtra(KEY_PARAM_PAGE, 1);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
