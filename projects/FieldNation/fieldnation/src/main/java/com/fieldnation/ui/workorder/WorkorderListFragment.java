package com.fieldnation.ui.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.PagingAdapter;

import java.util.LinkedList;
import java.util.List;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.State;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class WorkorderListFragment extends Fragment {
    private static final String TAG = "ui.workorder.WorkorderListFragment";


    // WEB
    private static final int WEB_GET_LIST = 0;

    // Request Key
    private static final String KEY_PAGE_NUM = "PAGE_NUM";

    // UI
    private PullToRefreshListView _listView;
    private SmoothProgressBar _loadingBar;

    // Data
//    private WorkorderListAdapter _adapter;
    private GlobalState _gs;
    private WorkorderService _service;
    private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate: " + WorkorderListFragment.this.toString() + "/" + _displayView.getCall());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workorder_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _gs = (GlobalState) getActivity().getApplicationContext();


        _listView = (PullToRefreshListView) view.findViewById(R.id.workorders_listview);
        _listView.setDivider(null);
        _listView.setOnRefreshListener(_listView_onRefreshListener);
        _listView.setStateListener(_listview_onPullListener);

        _loadingBar = (SmoothProgressBar) view.findViewById(R.id.loading_progress);
        _loadingBar.setSmoothProgressDrawableCallbacks(_progressCallback);
        _loadingBar.setMax(100);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("_displayView")) {
                Log.v(TAG, "Restoring state");
                _displayView = WorkorderDataSelector.fromName(savedInstanceState.getString("_displayView"));
            }

//            if (savedInstanceState.containsKey("WORKORDERS")) {
//                Parcelable[] works = savedInstanceState.getParcelableArray("WORKORDERS");
//
//                if (works != null && works.length > 0) {
//                    List<Workorder> work = new LinkedList<Workorder>();
//                    for (int i = 0; i < works.length; i++) {
//                        work.add((Workorder) works[i]);
//                    }
//                    try {
//                        _adapter = new WorkorderListAdapter(this.getActivity(), _displayView, work);
//                        _adapter.setLoadingListener(_workorderAdapter_listener);
//                        _loadingBar.setVisibility(View.GONE);
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
        }
        Log.v(TAG, "Display Type: " + _displayView.getCall());
        _gs.requestAuthentication(_authClient);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("_displayView", _displayView.name());

//        if (_adapter != null) {
//            List<Workorder> work = _adapter.getObjects();
//            if (work != null && work.size() > 0) {
//                Workorder[] works = new Workorder[work.size()];
//                for (int i = 0; i < work.size(); i++) {
//                    works[i] = work.get(i);
//                }
//                outState.putParcelableArray("WORKORDERS", works);
//            }
//        }

        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onStart() {
//        Log.v(TAG, "onStart");
//        if (_listView != null && getAdapter() != null && _listView.getAdapter() == null) {
//            _listView.setAdapter(getAdapter());
//        }
//        super.onStart();
//    }

    public WorkorderListFragment setDisplayType(WorkorderDataSelector displayView) {
        _displayView = displayView;
        return this;
    }

    public WorkorderDataSelector getDisplayType() {
        return _displayView;
    }

    public void update() {
//        if (getAdapter() != null)
//            getAdapter().update(false);
        _listView.setRefreshing();
    }

    public void hiding() {
//        if (getAdapter() != null)
//            getAdapter().onStop();
    }

//    private WorkorderListAdapter getAdapter() {
//        if (this.getActivity() == null)
//            return null;
//        try {
//            if (_adapter == null) {
//                _adapter = new WorkorderListAdapter(this.getActivity(), _displayView);
//                _adapter.setLoadingListener(_workorderAdapter_listener);
//            }
//
//            if (!_adapter.isViable()) {
//                _adapter = new WorkorderListAdapter(this.getActivity(), _displayView);
//                _adapter.setLoadingListener(_workorderAdapter_listener);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//
//        return _adapter;
//    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private PagingAdapter<Workorder> _adapter = new PagingAdapter<Workorder>() {
        @Override
        public View getView(int page, int position, Workorder object, View convertView, ViewGroup parent) {
            WorkorderCardView v = null;
            if (convertView == null) {
                v = new WorkorderCardView(parent.getContext());
            } else if (convertView instanceof WorkorderCardView) {
                v = (WorkorderCardView) convertView;
            } else {
                v = new WorkorderCardView(parent.getContext());
            }

            v.setWorkorder(object);

            return v;
        }

        @Override
        public void requestPage(int page) {
            requestList(page, true);
        }
    };

    private SmoothProgressDrawable.Callbacks _progressCallback = new SmoothProgressDrawable.Callbacks() {

        @Override
        public void onStop() {
            _loadingBar.setVisibility(View.GONE);
        }

        @Override
        public void onStart() {
            _loadingBar.setVisibility(View.VISIBLE);
        }

    };

//    private WorkorderListAdapter.Listener<Workorder> _workorderAdapter_listener = new WorkorderListAdapter.Listener<Workorder>() {
//
//        @Override
//        public void onLoading() {
//            _listView.setRefreshing();
//            _loadingBar.progressiveStart();
//        }
//
//        @Override
//        public void onLoadComplete() {
//            _listView.onRefreshComplete();
//            _loadingBar.progressiveStop();
//        }
//    };

    private PullToRefreshListView.OnRefreshListener _listView_onRefreshListener = new PullToRefreshListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // _adapter.update(false);
            // _loadingBar.setIndeterminate(true);
            // _loadingBar.progressiveStart();
        }
    };

    private PullToRefreshListView.StateListener _listview_onPullListener = new PullToRefreshListView.StateListener() {
        @Override
        public void onPull(int pullPercent) {
            if (_listView.getState() == PullToRefreshListView.State.PULL_TO_REFRESH) {
                float sep = 4f - 4 * Math.abs(pullPercent) / 100f;
                if (sep < 0)
                    sep = 0f;
                _loadingBar.setSmoothProgressDrawableSpeed(sep);
            }
        }

        @Override
        public void onStopPull() {
            _loadingBar.setSmoothProgressDrawableSpeed(2f);
            _loadingBar.setSmoothProgressDrawableReversed(true);
            _loadingBar.setSmoothProgressDrawableSectionsCount(1);
            _loadingBar.progressiveStop();
            _loadingBar.setVisibility(View.GONE);
        }

        @Override
        public void onStateChange(State state) {
            if (state == State.RELEASE_TO_REFRESH) {
//                if (getAdapter() != null)
//                    getAdapter().update(false);
                _loadingBar.progressiveStart();
            }
        }

        @Override
        public void onStartPull() {
            _loadingBar.setSmoothProgressDrawableSectionsCount(1);
            _loadingBar.setSmoothProgressDrawableReversed(true);
            _loadingBar.progressiveStart();
        }

    };

    private void requestList(int page, boolean allowCache) {
        if (_service == null)
            return;

        Intent intent = _service.getList(WEB_GET_LIST, page, _displayView, allowCache);
        intent.putExtra(KEY_PAGE_NUM, page);
        getActivity().startService(intent);
    }


    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/

    private AuthenticationClient _authClient = new AuthenticationClient() {

        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            _service = new WorkorderService(getActivity(), username, authToken, _resultReciever);
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {

            if (resultCode == WEB_GET_LIST) {
                int page = resultData.getInt(KEY_PAGE_NUM);

                String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));

                JsonArray objects = null;
                try {
                    objects = new JsonArray(data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }

                List<Workorder> list = new LinkedList<Workorder>();
                for (int i = 0; i < objects.size(); i++) {
                    try {
                        list.add(Workorder.fromJson(objects.getJsonObject(i)));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                _adapter.addPage(page, list);
                _listView.setAdapter(_adapter);

                if (list.size() < 25) {
                    _adapter.setNoMorePages();
                }
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_service != null || _service.getAuthToken() != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());

            }
            _gs.requestAuthenticationDelayed(_authClient);
        }
    };


}
