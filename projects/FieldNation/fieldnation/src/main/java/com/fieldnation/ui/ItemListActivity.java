package com.fieldnation.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;

import java.util.List;

/**
 * Created by michael.carver on 12/1/2014.
 */
public abstract class ItemListActivity<O> extends AuthActionBarActivity {
    private static final String TAG = "ItemListActivity";

    // UI
    private OverScrollListView _listView;
    private RefreshView _refreshView;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    public int getLayoutResource() {
        return R.layout.activity_itemlist;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        _adapter.setOnLoadingCompleteListener(_adapter_onLoadingComplete);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _listView = (OverScrollListView) findViewById(R.id.items_listview);
        _listView.setOnOverScrollListener(_refreshView);
        _listView.setAdapter(_adapter);

        //addActionBarAndDrawer(R.id.container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _adapter.refreshPages();
        _refreshView.startRefreshing();
    }

    private void getData(int page) {
        if (_listView == null)
            return;

        _refreshView.startRefreshing();
        requestData(page);
    }

    public abstract void requestData(int page);


    public void addPage(int page, List<O> list, boolean isCached) {
        if (list == null || list.size() == 0) {
            _adapter.setNoMorePages();
        }

        _adapter.setPage(page, list, isCached);
    }

    private final PagingAdapter<O> _adapter = new PagingAdapter<O>() {
        @Override
        public View getView(O object, View convertView, ViewGroup parent) {
            return ItemListActivity.this.getView(object, convertView, parent);
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            getData(page);
        }
    };

    public abstract View getView(O object, View convertView, ViewGroup parent);

    public void notifyDataSetChanged() {
        _adapter.notifyDataSetChanged();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            _adapter.refreshPages();
        }
    };

    private final PagingAdapter.OnLoadingCompleteListener _adapter_onLoadingComplete = new PagingAdapter.OnLoadingCompleteListener() {
        @Override
        public void onLoadingComplete() {
            _refreshView.refreshComplete();
        }
    };
}

