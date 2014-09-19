package com.fieldnation.ui.workorder;

import com.fieldnation.data.workorder.Bundle;
import com.fieldnation.data.workorder.Workorder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BundleAdapter extends BaseAdapter {
	private static final String TAG = "ui.workorder.BundleAdapter";

	private Workorder[] _workorders;
	private WorkorderCardView.Listener _listener;

	public BundleAdapter(Bundle bundle, WorkorderCardView.Listener listener) {
		super();
		_workorders = bundle.getWorkorder();
		_listener = listener;
	}

	@Override
	public int getCount() {
		return _workorders.length;
	}

	@Override
	public Object getItem(int position) {
		return _workorders[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WorkorderCardView v = null;
		if (convertView == null) {
			v = new WorkorderCardView(parent.getContext());
		} else if (convertView instanceof WorkorderCardView) {
			v = (WorkorderCardView) convertView;
		} else {
			v = new WorkorderCardView(parent.getContext());
		}

		v.setWorkorder(WorkorderDataSelector.ASSIGNED, _workorders[position]);
		v.setIsBundle(false);
		v.setWorkorderSummaryListener(_listener);

		return v;
	}
}
