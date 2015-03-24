package com.fieldnation.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;

import java.util.List;

/**
 * Created by michael.carver on 12/1/2014.
 */
public abstract class ItemListActivity<O> extends DrawerActivity {
    private static final String TAG = "ui.ItemListActivity";

    // UI
    private OverScrollListView _listView;
    private RefreshView _refreshView;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        _adapter.setListener(_adapter_lsitener);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _listView = (OverScrollListView) findViewById(R.id.items_listview);
        _listView.setOnOverScrollListener(_refreshView);
        _listView.setAdapter(_adapter);

        addActionBarAndDrawer(R.id.container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _adapter.refreshPages();
    }

    @Override
    public void onRefresh() {
        if (_refreshView != null) {
            _refreshView.startRefreshing();
        }

        _adapter.refreshPages();
    }

    private void getData(int page) {
        if (_listView == null)
            return;

        _refreshView.startRefreshing();
        requestData(page);
    }

    public abstract void requestData(int page);


    public void addPage(int page, List<O> list) {
        if (list == null || list.size() == 0) {
            _adapter.setNoMorePages();
        }

        _adapter.setPage(page, list);
    }

    private PagingAdapter<O> _adapter = new PagingAdapter<O>() {
        @Override
        public View getView(int page, int position, O object, View convertView, ViewGroup parent) {
            return ItemListActivity.this.getView(object, convertView, parent);
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            getData(page);
        }
    };

    public abstract View getView(O object, View convertView, ViewGroup parent);

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            _adapter.refreshPages();
        }
    };

    private PagingAdapter.Listener _adapter_lsitener = new PagingAdapter.Listener() {
        @Override
        public void onLoadingComplete() {
            _refreshView.refreshComplete();
        }
    };
}
