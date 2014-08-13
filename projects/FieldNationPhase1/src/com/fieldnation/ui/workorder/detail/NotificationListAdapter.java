package com.fieldnation.ui.workorder.detail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarController.AdvancedUndoListener;
import com.cocosw.undobar.UndoBarController.UndoBar;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.PagingListAdapter;
import com.fieldnation.ui.payment.PayDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * A list adapter that loads work order lists.
 * 
 * @author michael.carver
 * 
 */
public class NotificationListAdapter extends BaseAdapter {
	private static final String TAG = "ui.workorder.NotificationListAdapter";

	// data
	private List<Notification> _notes = new LinkedList<Notification>();

	public NotificationListAdapter() {
	}

	@Override
	public int getCount() {
		return _notes.size();
	}

	@Override
	public Object getItem(int position) {
		return _notes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotificationView nv = null;
		if (convertView == null) {
			nv = new NotificationView(parent.getContext());
		} else if (convertView instanceof NotificationView) {
			nv = (NotificationView) convertView;
		} else {
			nv = new NotificationView(parent.getContext());
		}

		nv.setNotification(_notes.get(position));
		return nv;
	}

	public void setNotifications(List<Notification> notifications) {
		_notes = notifications;

		notifyDataSetChanged();
	}

}
