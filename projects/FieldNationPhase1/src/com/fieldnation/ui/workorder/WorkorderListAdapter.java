package com.fieldnation.ui.workorder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.fieldnation.DelayedCall;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.CancelCategory;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.ui.PagingListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * A list adapter that loads work order lists.
 * 
 * @author michael.carver
 * 
 */
public class WorkorderListAdapter extends PagingListAdapter<Workorder> {
	private static final String TAG = "ui.workorder.WorkorderListAdapter";

	private static final int WEB_REMOVING_WORKRODER = 100;
	private static final int WEB_CHANGING_WORKORDER = 101;
	private static final String KEY_WORKORDER_ID = "com.fieldnation.ui.workorder.WorkorderListAdapter.WORKORDER_ID";

	private WorkorderService _workorderService = null;
	private Method _rpcMethod;
	private WorkorderDataSelector _dataSelection;
	private Set<Long> _pendingNotInterestedWorkorders = new HashSet<Long>();
	private Set<Long> _requestWorkingWorkorders = new HashSet<Long>();

	/*-*****************************-*/
	/*-			Lifecycle			-*/
	/*-*****************************-*/

	public WorkorderListAdapter(Activity activity, WorkorderDataSelector selection) throws NoSuchMethodException {
		super(activity, Workorder.class);
		_dataSelection = selection;

		_rpcMethod = WorkorderService.class.getDeclaredMethod(selection.getCall(), new Class<?>[] {
				int.class,
				int.class,
				boolean.class });
		_rpcMethod.setAccessible(true);
	}

	@Override
	public View getView(Workorder object, View convertView, ViewGroup parent) {
		WorkorderCardView wosum = null;

		if (convertView == null) {
			wosum = new WorkorderCardView(parent.getContext());
		} else if (convertView instanceof WorkorderCardView) {
			wosum = (WorkorderCardView) convertView;
		} else {
			wosum = new WorkorderCardView(parent.getContext());
		}

		// TODO if pending delete, alert the view
		if (_pendingNotInterestedWorkorders.contains(object.getWorkorderId())) {

		}

		// TODO notify view that the request has been sent
		if (_requestWorkingWorkorders.contains(object.getWorkorderId())) {

		}

		wosum.setWorkorder(_dataSelection, object);
		wosum.setWorkorderSummaryListener(_wocv_listener);

		return wosum;
	}

	@Override
	public Workorder fromJson(JsonObject obj) {
		return Workorder.fromJson(obj, _dataSelection);
	}

	@Override
	public void invalidateWebervice() {
		_workorderService = null;
	}

	@Override
	public void getWebService(Context context, String username, String authToken, ResultReceiver resultReceiver) {
		if (_workorderService == null) {
			_workorderService = new WorkorderService(context, username, authToken, resultReceiver);
		}
	}

	@Override
	public void rebuildWebService(Context context, String username, String authToken, ResultReceiver resultReceiver) {
		_workorderService = new WorkorderService(context, username, authToken, resultReceiver);
	}

	@Override
	public void executeWebService(int resultCode, int page, boolean allowCache) {
		try {
			if (!_dataSelection.allowCache())
				allowCache = false;
			// TODO need to query available and requested for market view
			getContext().startService((Intent) _rpcMethod.invoke(_workorderService, resultCode, page, allowCache));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	@Override
	public void handleWebResult(int resultCode, Bundle resultData) {
		if (resultCode == WEB_REMOVING_WORKRODER) {
			long workorderId = resultData.getLong(KEY_WORKORDER_ID);
			_pendingNotInterestedWorkorders.remove(workorderId);
			_requestWorkingWorkorders.remove(workorderId);
			notifyDataSetChanged();
		} else if (resultCode == WEB_CHANGING_WORKORDER) {
			long workorderId = resultData.getLong(KEY_WORKORDER_ID);
			_requestWorkingWorkorders.remove(workorderId);
			notifyDataSetChanged();
		}
	}

	private DelayedCall.Listener<Workorder> _workorderDelayedDelete = new DelayedCall.Listener<Workorder>() {
		@Override
		public void onComplete(DelayedCall<Workorder> delayedCall, Workorder workorder) {
			if (_pendingNotInterestedWorkorders.contains(workorder.getWorkorderId())) {
				_requestWorkingWorkorders.add(workorder.getWorkorderId());
				_pendingNotInterestedWorkorders.remove(workorder.getWorkorderId());
				switch (workorder.getNotInterestedAction()) {
				case Workorder.NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT: {
					// TODO, need to ask user for category/reason
					Intent intent = _workorderService.cancelAssignment(WEB_REMOVING_WORKRODER,
							workorder.getWorkorderId(), CancelCategory.OTHER, "This is a test");
					intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
					getContext().startService(intent);
					break;
				}
				case Workorder.NOT_INTERESTED_ACTION_DECLINE: {
					Intent intent = _workorderService.decline(WEB_REMOVING_WORKRODER, workorder.getWorkorderId());
					intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
					getContext().startService(intent);
					break;
				}
				case Workorder.NOT_INTERESTED_ACTION_WITHDRAW_REQUEST: {
					Intent intent = _workorderService.withdrawRequest(WEB_REMOVING_WORKRODER,
							workorder.getWorkorderId());
					intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
					getContext().startService(intent);
					break;
				}
				}
			}
		}
	};

	private WorkorderCardView.Listener _wocv_listener = new WorkorderCardView.Listener() {

		@Override
		public void startRemove(Workorder wo) {
			_pendingNotInterestedWorkorders.add(wo.getWorkorderId());
			new DelayedCall<Workorder>().execute(10000, _workorderDelayedDelete, wo);
		}

		@Override
		public void cancelRemove(Workorder wo) {
			_pendingNotInterestedWorkorders.remove(wo.getWorkorderId());
			notifyDataSetChanged();
		}

		@Override
		public void notifyDataSetChanged() {
			WorkorderListAdapter.this.notifyDataSetChanged();
		}

		@Override
		public void actionRequest(Workorder workorder) {
			// TODO as user for time.
			Intent intent = _workorderService.request(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), 600);
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.add(workorder.getWorkorderId());
		}

		@Override
		public void actionCheckout(Workorder workorder) {
			Intent intent = _workorderService.checkout(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(),
					System.currentTimeMillis());
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.add(workorder.getWorkorderId());
		}

		@Override
		public void actionCheckin(Workorder workorder) {
			Intent intent = _workorderService.checkin(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(),
					System.currentTimeMillis());
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.add(workorder.getWorkorderId());
		}

		@Override
		public void actionAssignment(Workorder workorder) {
			// TODO, get start/end time?
			Intent intent = _workorderService.confirmAssignment(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), 0,
					0);
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.add(workorder.getWorkorderId());
		}

		@Override
		public void actionAcknowledgeHold(Workorder workorder) {
			Intent intent = _workorderService.acknowledgeHold(WEB_CHANGING_WORKORDER, workorder.getWorkorderId());
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.add(workorder.getWorkorderId());
		}
	};
}
