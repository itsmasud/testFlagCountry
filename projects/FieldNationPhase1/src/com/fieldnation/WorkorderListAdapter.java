package com.fieldnation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fieldnation.json.JsonArray;
import com.fieldnation.service.rpc.WorkorderRpc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class WorkorderListAdapter extends BaseAdapter {
	private static final String TAG = "WorkorderListAdapter";
	private static final int RPC_GET = 1;

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
	private WorkorderRpc _workorderRpc;
	private Method _rpcMethod;
	private boolean _running;
	private boolean _allowCache = true;

	public WorkorderListAdapter(Context context, String type) throws NoSuchMethodException {
		_context = context;
		_gs = (GlobalState) context.getApplicationContext();

		_running = true;

		_rpcMethod = WorkorderRpc.class.getDeclaredMethod(type,
				new Class<?>[] { int.class, int.class, boolean.class });
		_rpcMethod.setAccessible(true);

	}

	public void onStop() {
		notifyDataSetInvalidated();
		_running = false;
	}

	public void update(boolean allowCache) {
		if (!_running)
			return;

		Log.v(TAG, "Starting query: " + _rpcMethod.getName());
		_allowCache = allowCache;

		_workorders = new JsonArray();
		if (_gs.oAuth == null) {
			Log.v(TAG, "Waiting for accessToken");

			_workorderRpc = null;
			_waitForField = new WaitForFieldAsyncTask(_waitForAccessToken_listener);
			_waitForField.execute(_gs, "accessToken");
		} else {
			Log.v(TAG, "Have accessToken");
			if (_workorderRpc == null) {
				_workorderRpc = new WorkorderRpc(_context, _gs.oAuth, _rpcReceiver);
			}
			try {
				Intent intent = callRpc(RPC_GET, 1, _allowCache);

				intent.putExtra("PARAM_PAGE", 1);
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

	private ResultReceiver _rpcReceiver = new ResultReceiver(new Handler()) {
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (!_running)
				return;

			if (resultCode == RPC_GET) {
				int page = resultData.getInt("PARAM_PAGE");
				String data = new String(resultData.getByteArray("RESPONSE_DATA"));

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

						intent.putExtra("PARAM_PAGE", page + 1);
						_context.startService(intent);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// chain is complete, re-enable caching
					_allowCache = true;
				}
			}
		};
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
