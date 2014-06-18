package com.fieldnation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fieldnation.json.JsonArray;
import com.fieldnation.service.rpc.WebRpc;
import com.fieldnation.service.rpc.WebRpcResultReciever;
import com.fieldnation.service.rpc.WorkorderRpc;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
	private BaseActivity _activity;
	private JsonArray _workorders = null;
	private WaitForFieldAsyncTask _waitForField;
	private WorkorderRpc _workorderRpc;
	private Method _rpcMethod;
	private boolean _viable;
	private boolean _allowCache = true;

	/**
	 * 
	 * @param context
	 * @param type
	 *            should be one of the TYPE_* strings
	 * @throws NoSuchMethodException
	 */
	public WorkorderListAdapter(BaseActivity activity, String type) throws NoSuchMethodException {
		_activity = activity;
		_gs = (GlobalState) activity.getApplicationContext();

		_viable = true;

		_rpcMethod = WorkorderRpc.class.getDeclaredMethod(type,
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

	public boolean isViable() {
		return _viable;
	}

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
		if (_gs.accessToken == null) {
			Log.v(TAG, "Waiting for accessToken");

			_workorderRpc = null;
			_waitForField = new WaitForFieldAsyncTask(
					_waitForAccessToken_listener);
			_waitForField.execute(_gs, "accessToken");
		} else {
			Log.v(TAG, "Have accessToken");
			if (_workorderRpc == null) {
				_workorderRpc = new WorkorderRpc(_activity, _gs.username,
						_gs.accessToken, _webRpcReceiver);
			}
			try {
				Intent intent = callRpc(RPC_GET, 1, _allowCache);

				intent.putExtra(KEY_PARAM_PAGE, 1);
				_activity.startService(intent);
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

	private WaitForFieldAsyncTask.Listener _waitForAccessToken_listener = new WaitForFieldAsyncTask.Listener() {
		@Override
		public void onSuccess(Object value) {
			update(_allowCache);
		}

		@Override
		public void onFail(Exception ex) {
			update(_allowCache);
		}
	};

	private WebRpcResultReciever _webRpcReceiver = new WebRpcResultReciever(
			new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			int page = resultData.getInt(KEY_PARAM_PAGE);
			String data = new String(
					resultData.getByteArray(WebRpc.KEY_RESPONSE_DATA));

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
					_activity.startService(intent);

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
			// AccountManager.get(_activity).invalidateAuthToken(_gs.accountType,
			// _gs.accessToken);
			// _workorderRpc = null;
			// _gs.username = null;
			// _gs.accessToken = null;
			notifyDataSetChanged();
		}
	};

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

}
