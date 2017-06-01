package com.fieldnation.v2.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class GetFilePackageAdapter extends BaseAdapter {
    private static String TAG = "GetFilePackageAdapter";

    private final List<GetFilePackage> _appList;
    private final GetFilePackageRowView.OnClickListener _listener;

    public GetFilePackageAdapter(List<GetFilePackage> list, GetFilePackageRowView.OnClickListener listener) {
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
        GetFilePackageRowView view = null;
        if (convertView == null) {
            view = new GetFilePackageRowView(parent.getContext());
        } else if (convertView instanceof GetFilePackageRowView) {
            view = (GetFilePackageRowView) convertView;
        } else {
            view = new GetFilePackageRowView(parent.getContext());
        }

        GetFilePackage pack = _appList.get(position);
        view.setInfo(pack);
        view.setListener(_listener);

        return view;
    }

}