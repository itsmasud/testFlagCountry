package com.fieldnation.ui.workorder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fieldnation.DelayedCall;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.CancelCategory;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.ui.PagingListAdapter;
import com.fieldnation.ui.payment.PayDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private PayDialog _payDialog;
	private ActionMode _actionMode = null;
	private Set<Long> _selectedWorkorders = new HashSet<Long>();

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

		_payDialog = new PayDialog(activity);
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

		if (_pendingNotInterestedWorkorders.contains(object.getWorkorderId())) {
			wosum.setDisplayMode(WorkorderCardView.MODE_UNDO_NOT_INTERESTED);
		} else if (_requestWorkingWorkorders.contains(object.getWorkorderId())) {
			wosum.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
		} else {
			wosum.setDisplayMode(WorkorderCardView.MODE_NORMAL);
		}

		if (_selectedWorkorders.contains(object.getWorkorderId())) {
			wosum.setSelected(true);
		} else {
			wosum.setSelected(false);
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
	public void invalidateWebService() {
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
			super.update(false);
		} else if (resultCode == WEB_CHANGING_WORKORDER) {
			long workorderId = resultData.getLong(KEY_WORKORDER_ID);
			_requestWorkingWorkorders.remove(workorderId);
			super.update(false);
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
		public void actionRequest(Workorder workorder) {
			// TODO ask user for time.
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

		@Override
		public void viewCounter(Workorder workorder) {
			// TODO this is not correct;
			_payDialog.show();
		}

		@Override
		public void onLongClick(WorkorderCardView view, Workorder workorder) {
			if (_selectedWorkorders.contains(workorder.getWorkorderId())) {
				_selectedWorkorders.remove(workorder.getWorkorderId());

				view.setSelected(false);

				if (_actionMode != null && _selectedWorkorders.size() == 0) {
					_actionMode.finish();
					_actionMode = null;
				}
			} else {
				_selectedWorkorders.add(workorder.getWorkorderId());

				view.setSelected(true);

				if (_actionMode == null) {
					_actionMode = ((ActionBarActivity) getActivity()).startSupportActionMode(_actionMode_Callback);
				}
			}
		}

		@Override
		public void onClick(WorkorderCardView view, Workorder workorder) {
			if (view.isBundle()) {
				Intent intent = new Intent(getContext(), WorkorderBundleDetailActivity.class);
				intent.putExtra("workorder_id", workorder.getWorkorderId());
				getContext().startActivity(intent);

			} else {
				Intent intent = new Intent(getContext(), WorkorderActivity.class);
				intent.putExtra("workorder_id", workorder.getWorkorderId());
				getContext().startActivity(intent);
			}
		}
	};

	private ActionMode.Callback _actionMode_Callback = new ActionMode.Callback() {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.workorder_card, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			_actionMode = null;
			_selectedWorkorders.clear();
			notifyDataSetChanged();
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Method Stub: onActionItemClicked()
			Log.v(TAG, "Method Stub: onActionItemClicked()");
			if (item != null) {
				Log.v(TAG, item.getItemId() + ":" + R.id.notinterested_action);
			} else {
				Log.v(TAG, "No item");
			}
			return false;
		}
	};

	public void onPause() {
		if (_actionMode != null) {
			_actionMode.finish();
			_actionMode = null;
			_selectedWorkorders.clear();
		}
	}

}
