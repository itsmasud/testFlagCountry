package com.fieldnation.ui.workorder.detail;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderFragment;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NotificationFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.NotificationFragment";
    private int WEB_LIST_NOTIFICATIONS = 1;

    // UI
    private OverScrollListView _listview;
    private TextView _emptyTextView;
    private RefreshView _refreshView;

    // Data
    private GlobalState _gs;
    private Workorder _workorder;
    private Random _rand = new Random();
    private WorkorderService _service;
    private List<Notification> _notes;
    private NotificationListAdapter _adapter;

	/*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workorder_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onCreateView");

        _gs = (GlobalState) getActivity().getApplicationContext();

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _listview = (OverScrollListView) view.findViewById(R.id.listview);
        _listview.setOnOverScrollListener(_refreshView);

        _emptyTextView = (TextView) view.findViewById(R.id.empty_textview);

        _gs.requestAuthentication(_authclient);
    }

    @Override
    public void update() {
//        getNotifications();
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        Log.v(TAG, "setWorkorder: wokorder==null:" + (workorder == null)
                + " _service==null:" + (_service == null)
                + " _gs==null:" + (_gs == null));

        _workorder = workorder;

        populateUi();
        getNotifications();
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (_refreshView != null) {
            if (isLoading) {
                _refreshView.startRefreshing();
            } else {
                _refreshView.refreshComplete();
            }
        }
    }

    public void populateUi() {
        Log.v(TAG, "populateUi, _notes:" + (_notes == null) + " _workorder:" + (_workorder == null));
        if (_notes == null)
            return;
        if (_workorder == null)
            return;

        Log.v(TAG, "populateUi");

        if (getAdapter() != null)
            getAdapter().setNotifications(_notes);

        if (_notes.size() == 0) {
            _emptyTextView.setVisibility(View.VISIBLE);
        }

        _refreshView.refreshComplete();
    }

    private void getNotifications() {
        Log.v(TAG, "getNotifications, _service:" + (_service == null) + " _workorder:" + (_workorder == null));
        if (_service == null)
            return;

        if (_workorder == null)
            return;
        Log.v(TAG, "getNotifications");

        _refreshView.startRefreshing();
        _notes = null;
        WEB_LIST_NOTIFICATIONS = _rand.nextInt();
        _emptyTextView.setVisibility(View.GONE);
        try {
            _gs.startService(_service.listNotifications(WEB_LIST_NOTIFICATIONS, _workorder.getWorkorderId(), false));
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v(TAG, "BP");
        }
    }

    @Override
    public void doAction(Bundle bundle) {
        // TODO Method Stub: doAction()
        Log.v(TAG, "Method Stub: doAction()");
    }

    private NotificationListAdapter getAdapter() {
        if (this.getActivity() == null)
            return null;
        try {
            if (_adapter == null) {
                _adapter = new NotificationListAdapter();
                _listview.setAdapter(_adapter);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return _adapter;
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            getNotifications();
        }
    };


    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/

    private AuthenticationClient _authclient = new AuthenticationClient() {
        @Override
        public void onAuthenticationFailed(Exception ex) {
            Log.v(TAG, "onAuthenticationFailed");
            _gs.requestAuthenticationDelayed(_authclient);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            Log.v(TAG, "onAuthentication");
            _service = new WorkorderService(_gs, username, authToken, _resultReceiver);
            getNotifications();
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebResultReceiver _resultReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            Log.v(TAG, "onSuccess");

            if (resultCode == WEB_LIST_NOTIFICATIONS) {
                Log.v(TAG, "onSuccess2");
                try {
                    JsonArray ja = new JsonArray(new String(
                            resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));
                    _notes = new LinkedList<Notification>();

                    for (int i = 0; i < ja.size(); i++) {
                        JsonObject obj = ja.getJsonObject(i);
                        _notes.add(Notification.fromJson(obj));
                    }

                    populateUi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_service != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authclient);
            Toast.makeText(getActivity(), "Could not complete request", Toast.LENGTH_LONG).show();
            _refreshView.refreshComplete();
        }

    };

}
