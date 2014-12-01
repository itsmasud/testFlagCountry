package com.fieldnation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Message;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import java.util.LinkedList;
import java.util.List;

public class MessageListActivity extends BaseActivity {
    private static final String TAG = "ui.MessageListActivity";

    // WEB
    private final static int WEB_LIST_MESSAGES = 1;

    // WEB param
    private final static String PARAM_PAGE_NUM = "MessageListActivity.PARAM_PAGE_NUM";

    // UI
    private OverScrollListView _listView;
    private RefreshView _refreshView;

    // Data
    private GlobalState _gs;
    private ProfileService _service;


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
    }

    @Override
    public void onRefresh() {
        if (_refreshView != null) {
            _refreshView.startRefreshing();
        }

        _adapter.refreshPages();
    }

    private void getData(int page, boolean allowCache) {
        if (_service == null)
            return;

        if (_listView == null)
            return;

        _refreshView.startRefreshing();
        Intent intent = _service.getAllMessages(WEB_LIST_MESSAGES, page, allowCache);
        intent.putExtra(PARAM_PAGE_NUM, page);
        startService(intent);
    }

    private void addPage(int page, List<Message> list, boolean isCached) {
        if (list.size() == 0) {
            _adapter.setNoMorePages();
        }
        _adapter.setPage(page, list, isCached);
    }

    private PagingAdapter<Message> _adapter = new PagingAdapter<Message>() {
        @Override
        public View getView(int page, int position, Message object, View convertView, ViewGroup parent) {
            MessageCardView v = null;

            if (convertView == null) {
                v = new MessageCardView(parent.getContext());
            } else if (convertView instanceof MessageCardView) {
                v = (MessageCardView) convertView;
            } else {
                v = new MessageCardView(parent.getContext());
            }

            v.setMessage(object);

            return v;
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            getData(page, allowCache);
        }
    };


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
            _service = new ProfileService(MessageListActivity.this, username, authToken, _resultReceiver);
            getData(0, true);
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(
            new Handler()) {

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_LIST_MESSAGES) {
                int page = resultData.getInt(PARAM_PAGE_NUM);

                String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
                boolean isCached = resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED);

                JsonArray objects = null;
                try {
                    objects = new JsonArray(data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }

                List<Message> list = new LinkedList<Message>();
                for (int i = 0; i < objects.size(); i++) {
                    try {
                        list.add(Message.fromJson(objects.getJsonObject(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                addPage(page, list, isCached);
                _refreshView.refreshComplete();
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_service != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authClient);
            Toast.makeText(MessageListActivity.this, "Could not complete request", Toast.LENGTH_LONG).show();
            _refreshView.refreshComplete();
        }
    };


}
