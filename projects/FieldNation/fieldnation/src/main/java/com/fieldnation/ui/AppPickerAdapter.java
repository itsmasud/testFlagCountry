package com.fieldnation.ui;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AppPickerAdapter extends BaseAdapter {
	private static String TAG = "com.fieldnation.ui.AppPickerAdapter";

	private List<AppPickerPackage> _appList;

	public AppPickerAdapter(List<AppPickerPackage> list) {
		super();
		_appList = list;
	}

	@Override
	public int getCount() {
		return _appList.size();
	}

	@Override
	public Object getItem(int position) {
		return _appList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppPickerRowView view = null;
		if (convertView == null) {
			view = new AppPickerRowView(parent.getContext());
		} else if (convertView instanceof AppPickerRowView) {
			view = (AppPickerRowView) convertView;
		} else {
			view = new AppPickerRowView(parent.getContext());
		}

		AppPickerPackage pack = _appList.get(position);

		view.setInfo(pack);

		return view;
	}

}
