package com.fieldnation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;

/**
 * A list adapter that loads work order lists.
 * 
 * @author michael.carver
 * 
 */
public class WorkorderListAdapter extends PagingListAdapter {
	private static final String TAG = "WorkorderListAdapter";

	private WorkorderService _workorderService = null;
	private Method _rpcMethod;
	private WorkorderDataSelector _dataSelection;

	/*-*****************************-*/
	/*-			Lifecycle			-*/
	/*-*****************************-*/

	public WorkorderListAdapter(Context context, WorkorderDataSelector selection) throws NoSuchMethodException {
		super(context);
		_dataSelection = selection;

		_rpcMethod = WorkorderService.class.getDeclaredMethod(
				selection.getCall(),
				new Class<?>[] { int.class, int.class, boolean.class });
		_rpcMethod.setAccessible(true);
	}

	@Override
	public View getView(JsonObject object, View convertView, ViewGroup parent) {
		WorkorderSummaryView wosum = null;

		if (convertView == null) {
			wosum = new WorkorderSummaryView(parent.getContext());
		} else if (convertView instanceof WorkorderSummaryView) {
			wosum = (WorkorderSummaryView) convertView;
		} else {
			wosum = new WorkorderSummaryView(parent.getContext());
		}

		wosum.setWorkorder(_dataSelection, object);

		return wosum;

	}

	@Override
	public void invalidateWebervice() {
		_workorderService = null;
	}

	@Override
	public void getWebService(Context context, String username,
			String authToken, ResultReceiver resultReceiver) {
		if (_workorderService == null) {
			_workorderService = new WorkorderService(context, username,
					authToken, resultReceiver);
		}
	}

	@Override
	public void rebuildWebService(Context context, String username,
			String authToken, ResultReceiver resultReceiver) {
		_workorderService = new WorkorderService(context, username, authToken,
				resultReceiver);
	}

	@Override
	public void executeWebService(int resultCode, int page, boolean allowCache) {
		try {
			getContext().startService(
					(Intent) _rpcMethod.invoke(_workorderService, resultCode,
							page, allowCache));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
