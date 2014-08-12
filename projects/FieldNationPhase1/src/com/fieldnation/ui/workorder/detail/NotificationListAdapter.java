package com.fieldnation.ui.workorder.detail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarController.AdvancedUndoListener;
import com.cocosw.undobar.UndoBarController.UndoBar;
import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
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
public class NotificationListAdapter extends PagingListAdapter<Notification> {
	private static final String TAG = "ui.workorder.NotificationListAdapter";

	private WorkorderService _workorderService = null;

	/*-*****************************-*/
	/*-			Lifecycle			-*/
	/*-*****************************-*/

	public NotificationListAdapter(Activity activity) throws NoSuchMethodException {
		super(activity, Notification.class);
	}

	@Override
	public View getView(int position, Notification object, View convertView, ViewGroup parent) {
		NotificationView note = null;

		if (convertView == null) {
			note = new NotificationView(parent.getContext());
		} else if (convertView instanceof NotificationView) {
			note = (NotificationView) convertView;
		} else {
			note = new NotificationView(parent.getContext());
		}

		note.setNotification(object);

		return note;
	}

	@Override
	public Notification fromJson(JsonObject obj) {
		return Notification.fromJson(obj);
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
		// TODO, don't have call for this yet
		// _workorderService.get
	}

	@Override
	public void update(boolean allowCache) {
		super.update(allowCache);
	}

	// happens on page flip
	public void onPause() {
		UndoBarController.clear(getActivity());
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	@Override
	public void handleWebResult(int resultCode, Bundle resultData) {
		// TODO handleWebResult
	}

}
