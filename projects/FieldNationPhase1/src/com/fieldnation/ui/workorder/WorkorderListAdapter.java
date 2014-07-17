package com.fieldnation.ui.workorder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import com.fieldnation.data.workorder.Label;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.ui.PagingListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

	private WorkorderService _workorderService = null;
	private Method _rpcMethod;
	private WorkorderDataSelector _dataSelection;
	private Hashtable<Long, Workorder> _startRemoveTable = new Hashtable<Long, Workorder>();

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
		if (_startRemoveTable.containsKey(object.getWorkorderId()) && _startRemoveTable.get(object.getWorkorderId()) != null) {
			// TODO think about caching this, this could get expensive
			return new View(parent.getContext());
		}

		WorkorderCardView wosum = null;

		if (convertView == null) {
			wosum = new WorkorderCardView(parent.getContext());
		} else if (convertView instanceof WorkorderCardView) {
			wosum = (WorkorderCardView) convertView;
		} else {
			wosum = new WorkorderCardView(parent.getContext());
		}

		wosum.setWorkorder(_dataSelection, object);
		wosum.setWorkorderSummaryListener(_wosum_listener);

		return wosum;
	}

	private WorkorderCardView.Listener _wosum_listener = new WorkorderCardView.Listener() {

		@Override
		public void startRemove(Workorder wo) {
			_startRemoveTable.put(wo.getWorkorderId(), wo);
			WorkorderListAdapter.this.notifyDataSetChanged();
		}

		@Override
		public void finishRemove(Workorder wo) {
			// TODO Method Stub: finishRemove()
			Log.v(TAG, "Method Stub: finishRemove()");
			_startRemoveTable.clear();
			update(false);
		}

		@Override
		public void cancelRemove(Workorder wo) {
			_startRemoveTable.remove(wo.getWorkorderId());
			// TODO find a way to trigger animation
			WorkorderListAdapter.this.notifyDataSetChanged();
		}

		@Override
		public void notifyDataSetChanged() {
			_startRemoveTable.clear();
			update(false);
		}
	};

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

}
