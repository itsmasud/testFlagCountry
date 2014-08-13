package com.fieldnation.ui.workorder.detail;

import java.util.LinkedList;
import java.util.List;

import com.fieldnation.data.workorder.Deliverable;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DeliverableListAdapter extends BaseAdapter {
	private static final String TAG = "ui.workorder.detail.DeliverableListAdapter";

	private long _profileId;
	private List<Deliverable> _deliverables;

	public DeliverableListAdapter() {
		super();
		_deliverables = new LinkedList<Deliverable>();
	}

	public void setData(long profileId, List<Deliverable> deliverables) {
		_profileId = profileId;
		_deliverables = deliverables;

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return _deliverables.size();
	}

	@Override
	public Object getItem(int position) {
		return _deliverables.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DeliverableView view = null;

		if (convertView == null) {
			view = new DeliverableView(parent.getContext());
		} else if (convertView instanceof DeliverableView) {
			view = (DeliverableView) convertView;
		} else {
			view = new DeliverableView(parent.getContext());
		}
		view.setDeliverable(_profileId, _deliverables.get(position));
		return view;
	}

}
