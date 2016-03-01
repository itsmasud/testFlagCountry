package com.fieldnation.ui.workorder.detail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.data.profile.Notification;

import java.util.LinkedList;
import java.util.List;

/**
 * A list adapter that loads work order lists.
 * 
 * @author michael.carver
 * 
 */
public class NotificationListAdapter extends BaseAdapter {
	private static final String TAG = "NotificationListAdapter";

	// data
	private List<Notification> _notes = new LinkedList<>();

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
