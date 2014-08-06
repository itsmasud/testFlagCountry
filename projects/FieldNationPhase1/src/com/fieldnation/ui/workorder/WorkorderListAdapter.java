package com.fieldnation.ui.workorder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.cocosw.undobar.UndoBarController.AdvancedUndoListener;
import com.cocosw.undobar.UndoBarController.UndoBar;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.ui.PagingListAdapter;
import com.fieldnation.ui.payment.PayDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
	private Hashtable<Long, Workorder> _pendingNotInterestedWorkorders = new Hashtable<Long, Workorder>();
	private Hashtable<Long, Workorder> _requestWorkingWorkorders = new Hashtable<Long, Workorder>();
	private PayDialog _payDialog;
	private ActionMode _actionMode = null;
	private Hashtable<Long, Workorder> _selectedWorkorders = new Hashtable<Long, Workorder>();

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
	public View getView(int position, Workorder object, View convertView, ViewGroup parent) {
		WorkorderCardView wosum = null;

		if (convertView == null) {
			wosum = new WorkorderCardView(parent.getContext());
		} else if (convertView instanceof WorkorderCardView) {
			wosum = (WorkorderCardView) convertView;
		} else {
			wosum = new WorkorderCardView(parent.getContext());
		}

		if (_pendingNotInterestedWorkorders.containsKey(object.getWorkorderId())) {
			wosum.setDisplayMode(WorkorderCardView.MODE_UNDO_NOT_INTERESTED);
		} else if (_requestWorkingWorkorders.containsKey(object.getWorkorderId())) {
			wosum.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
		} else {
			wosum.setDisplayMode(WorkorderCardView.MODE_NORMAL);
		}

		wosum.setWorkorder(_dataSelection, object);
		wosum.setWorkorderSummaryListener(_wocv_listener);

		if (_selectedWorkorders.containsKey(object.getWorkorderId())) {
			((ListView) parent).setItemChecked(position, true);
			// wosum.setSelected(true);
		} else {
			((ListView) parent).setItemChecked(position, false);
			// wosum.setSelected(false);
		}

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

	@Override
	public void update(boolean allowCache) {
		removeActionMode();
		super.update(allowCache);
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
			update(false);
		} else if (resultCode == WEB_CHANGING_WORKORDER) {
			long workorderId = resultData.getLong(KEY_WORKORDER_ID);
			_requestWorkingWorkorders.remove(workorderId);
			update(false);
		}
	}

	private WorkorderCardView.Listener _wocv_listener = new WorkorderCardView.Listener() {

		@Override
		public void actionRequest(Workorder workorder) {
			// TODO ask user for time.
			Intent intent = _workorderService.request(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), 600);
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
		}

		@Override
		public void actionCheckout(Workorder workorder) {
			Intent intent = _workorderService.checkout(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(),
					System.currentTimeMillis());
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
		}

		@Override
		public void actionCheckin(Workorder workorder) {
			Intent intent = _workorderService.checkin(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(),
					System.currentTimeMillis());
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
		}

		@Override
		public void actionAssignment(Workorder workorder) {
			// TODO, get start/end time?
			Intent intent = _workorderService.confirmAssignment(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), 0,
					0);
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
		}

		@Override
		public void actionAcknowledgeHold(Workorder workorder) {
			Intent intent = _workorderService.acknowledgeHold(WEB_CHANGING_WORKORDER, workorder.getWorkorderId());
			intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
			getContext().startService(intent);
			_requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
		}

		@Override
		public void viewCounter(Workorder workorder) {
			// TODO this is not correct;
			_payDialog.show();
		}

		@Override
		public void onLongClick(WorkorderCardView view, Workorder workorder) {
			if (_selectedWorkorders.containsKey(workorder.getWorkorderId())) {
				_selectedWorkorders.remove(workorder.getWorkorderId());
				view.setSelected(false);
				if (_actionMode != null && _selectedWorkorders.size() == 0) {
					_actionMode.finish();
					_actionMode = null;
				}
			} else {
				_selectedWorkorders.put(workorder.getWorkorderId(), workorder);
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

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		// removeActionMode();
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
			if (item.getItemId() == R.id.notinterested_action) {
				Enumeration<Workorder> e = _selectedWorkorders.elements();
				List<Workorder> list = new LinkedList<Workorder>();
				while (e.hasMoreElements()) {
					Workorder wo = e.nextElement();
					_pendingNotInterestedWorkorders.put(wo.getWorkorderId(), wo);
					list.add(wo);
				}
				_selectedWorkorders.clear();

				UndoBar undo = new UndoBar(getActivity());
				undo.message("Undo Not Interested");
				undo.listener(new WorkorderSummaryAdvancedUndoListener(list, getContext(), getUsername(),
						getAuthToken()));
				undo.duration(5000);
				undo.show();

				return true;
			}
			return false;
		}
	};

	public void onPause() {
		removeActionMode();
	}

	private void removeActionMode() {
		if (_actionMode != null) {
			_actionMode.finish();
			_actionMode = null;
			_selectedWorkorders.clear();
		}
	}

}
