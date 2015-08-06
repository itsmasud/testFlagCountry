package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.misc;

import java.util.LinkedList;
import java.util.List;

public class NotificationFragment extends WorkorderFragment {
    private static final String TAG = "NotificationFragment";

    // UI
    private OverScrollListView _listview;
    private TextView _emptyTextView;
    private RefreshView _refreshView;

    // Data
    private Workorder _workorder;
    private WorkorderClient _workorderClient;
    private List<Notification> _notes = new LinkedList<>();
    private NotificationListAdapter _adapter;
    private boolean _isSubbed = false;

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

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _listview = (OverScrollListView) view.findViewById(R.id.listview);
        _listview.setOnOverScrollListener(_refreshView);

        _emptyTextView = (TextView) view.findViewById(R.id.empty_textview);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _isSubbed = false;
        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(activity);
    }

    @Override
    public void onDetach() {
        _workorderClient.disconnect(getActivity());
        _workorderClient = null;
        _isSubbed = false;
        super.onDetach();
    }

    @Override
    public void update() {
        Log.v(TAG, "update");
        if (getActivity() != null && _workorder != null)
            WorkorderClient.listAlerts(getActivity(), _workorder.getWorkorderId(), false);
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        Log.v(TAG, "setWorkorder");
        _workorder = workorder;
        subscribeData();
        getNotifications();
        populateUi();
    }

    private void getNotifications() {
        Log.v(TAG, "getNotifications, _workorder:" + (_workorder == null));

        if (_workorder == null)
            return;

        if (_emptyTextView == null)
            return;

        if (getActivity() == null)
            return;

        Log.v(TAG, "getNotifications");

        _refreshView.startRefreshing();
        _emptyTextView.setVisibility(View.GONE);

        WorkorderClient.listAlerts(getActivity(), _workorder.getWorkorderId(), false);
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

        misc.hideKeyboard(getView());

        if (_notes == null)
            return;

        if (_workorder == null)
            return;

        if (_refreshView == null)
            return;

        Log.v(TAG, "populateUi");

        if (getAdapter() != null)
            getAdapter().setNotifications(_notes);

        if (_notes.size() == 0) {
            _emptyTextView.setVisibility(View.VISIBLE);
        } else {
            _emptyTextView.setVisibility(View.GONE);
        }

        _refreshView.refreshComplete();
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
    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            getNotifications();
        }
    };


    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/

    private void subscribeData() {
        if (_workorder == null)
            return;

        if (_workorderClient == null)
            return;

        if (!_workorderClient.isConnected())
            return;

        if (_isSubbed)
            return;

        Log.v(TAG, "subscribeData " + _workorderClient.subListAlerts(_workorder.getWorkorderId(), false));

        _isSubbed = true;
    }

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            subscribeData();
        }

        @Override
        public void onAlertList(long workorderId, List<Notification> alerts, boolean failed) {
            Log.v(TAG, "onAlertList");
            _notes = alerts;
            populateUi();
        }
    };
}
