package com.fieldnation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;

import java.util.List;

/**
 * Created by michael.carver on 12/1/2014.
 */
public abstract class ItemListActivity<O> extends DrawerActivity {
    private static final String TAG = "ui.ItemListActivity";

    // WEB
    private final static int WEB_LIST = 1;

    // WEB param
    private final static String PARAM_PAGE_NUM = "ItemListActivity.PARAM_PAGE_NUM";

    // UI
    private OverScrollListView _listView;
    private RefreshView _refreshView;

    // Data
    private String _authToken;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        _gs = (GlobalState) getApplicationContext();

        _adapter.setListener(_adapter_lsitener);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _listView = (OverScrollListView) findViewById(R.id.items_listview);
        _listView.setOnOverScrollListener(_refreshView);
        _listView.setAdapter(_adapter);

        _gs.requestAuthentication(_authClient);

        addActionBarAndDrawer(R.id.container);
    }

    @Override
    public void onRefresh() {
        if (_refreshView != null) {
            _refreshView.startRefreshing();
        }

        _adapter.refreshPages();
    }

    private void getData(int page, boolean allowCache) {
        if (_listView == null)
            return;

        _refreshView.startRefreshing();
        Intent intent = requestData(WEB_LIST, page, allowCache);

        if (intent == null)
            return;

        intent.putExtra(PARAM_PAGE_NUM, page);
        startService(intent);
    }

    public abstract Intent requestData(int resultCode, int page, boolean allowCache);


    public void addPage(int page, List<O> list, boolean isCached) {
        if (list == null) {
            _adapter.setNoMorePages();
            return;
        }

        if (list.size() == 0) {
            _adapter.setNoMorePages();
        }
        _adapter.setPage(page, list, isCached);
    }

    private PagingAdapter<O> _adapter = new PagingAdapter<O>() {
        @Override
        public View getView(int page, int position, O object, View convertView, ViewGroup parent) {
            return ItemListActivity.this.getView(object, convertView, parent);
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            getData(page, allowCache);
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


    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private AuthenticationClient _authClient = new AuthenticationClient() {
        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            _authToken = authToken;
            ItemListActivity.this.onAuthentication(username, authToken, _resultReceiver);
            getData(0, true);
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    public abstract void onAuthentication(String username, String authToken, ResultReceiver resultReceiver);

    private WebResultReceiver _resultReceiver = new WebResultReceiver(
            new Handler()) {

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_LIST) {
                int page = resultData.getInt(PARAM_PAGE_NUM);

                String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
                boolean isCached = resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED);

                List<O> list = onParseData(page, isCached, resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));

                addPage(page, list, isCached);
                _refreshView.refreshComplete();
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            invalidateService();
            _gs.invalidateAuthToken(_authToken);
            _gs.requestAuthenticationDelayed(_authClient);
            Toast.makeText(ItemListActivity.this, "Could not complete request", Toast.LENGTH_LONG).show();
            _refreshView.refreshComplete();
        }

    };

    public abstract List<O> onParseData(int page, boolean isCached, byte[] data);

    public abstract void invalidateService();


}
