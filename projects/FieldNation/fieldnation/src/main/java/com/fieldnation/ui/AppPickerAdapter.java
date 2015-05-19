package com.fieldnation.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class AppPickerAdapter extends BaseAdapter {
    private static String TAG = "AppPickerAdapter";

    private final List<AppPickerPackage> _appList;
    private final AppPickerRowView.OnClickListener _listener;

    public AppPickerAdapter(List<AppPickerPackage> list, AppPickerRowView.OnClickListener listener) {
        super();
        _appList = list;
        _listener = listener;
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
        view.setListener(_listener);

        return view;
    }

}
