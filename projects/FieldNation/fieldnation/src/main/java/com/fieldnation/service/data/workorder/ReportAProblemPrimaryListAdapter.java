package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;

import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;

/**
 * Created by Michael on 4/13/2016.
 */
public class ReportAProblemPrimaryListAdapter implements Filterable, ListAdapter {

    private WorkorderStatus _workOrderStatus;
    private WorkorderSubstatus _workOrderSubStatus;
    private int _viewResourceId;

    public ReportAProblemPrimaryListAdapter(Workorder workorder, int viewResourceId) {
        _workOrderStatus = workorder.getWorkorderStatus();
        _workOrderSubStatus = workorder.getWorkorderSubstatus();
        _viewResourceId = viewResourceId;
    }

    // List Adapter
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {

        // TODO
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    // Filter
    @Override
    public Filter getFilter() {
        return null;
    }

    private class MyFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    }
}
