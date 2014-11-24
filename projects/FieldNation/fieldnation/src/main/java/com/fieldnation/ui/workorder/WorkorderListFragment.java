package com.fieldnation.ui.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.PagingAdapter;

import java.util.LinkedList;
import java.util.List;

public class WorkorderListFragment extends Fragment {
    private static final String TAG = "ui.workorder.WorkorderListFragment";


    // WEB
    private static final int WEB_GET_LIST = 0;

    // Request Key
    private static final String KEY_PAGE_NUM = "PAGE_NUM";

    // UI
    private OverScrollListView _listView;
    private ImageView _loadingView;
    //private SmoothProgressBar _loadingBar;

    // Animations
    private Animation _rotateAnim;

    // Data
//    private WorkorderListAdapter _adapter;
    private int _loadingStartMargin;
    private GlobalState _gs;
    private WorkorderService _service;
    private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;
    private boolean _isLoading = false;

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

        _loadingView = (ImageView) view.findViewById(R.id.loading_view);
        _loadingStartMargin = ((RelativeLayout.LayoutParams) _loadingView.getLayoutParams()).topMargin;

        _rotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_spingear_cw);

        _listView = (OverScrollListView) view.findViewById(R.id.workorders_listview);
        _listView.setDivider(null);
//        _listView.setOnRefreshListener(_listView_onRefreshListener);
//        _listView.setStateListener(_listview_onPullListener);
        _listView.setOnOverScrollListener(_listView_OverScroll);
        _listView.setAdapter(_adapter);

//        _loadingBar = (SmoothProgressBar) view.findViewById(R.id.loading_progress);
//        _loadingBar.setSmoothProgressDrawableCallbacks(_progressCallback);
//        _loadingBar.setMax(100);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("_displayView")) {
                Log.v(TAG, "Restoring state");
                _displayView = WorkorderDataSelector.fromName(savedInstanceState.getString("_displayView"));
            }

        }
        Log.v(TAG, "Display Type: " + _displayView.getCall());
        _gs.requestAuthentication(_authClient);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("_displayView", _displayView.name());
        super.onSaveInstanceState(outState);
    }

    public WorkorderListFragment setDisplayType(WorkorderDataSelector displayView) {
        _displayView = displayView;
        return this;
    }

    public WorkorderDataSelector getDisplayType() {
        return _displayView;
    }

    public void update() {
        //_listView.setRefreshing();
        _adapter.refreshPages();
    }

    private void requestList(int page, boolean allowCache) {
        if (_service == null)
            return;

        Intent intent = _service.getList(WEB_GET_LIST, page, _displayView, allowCache);
        intent.putExtra(KEY_PAGE_NUM, page);
        getActivity().startService(intent);
    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private OverScrollListView.OnOverScrollListener _listView_OverScroll = new OverScrollListView.OnOverScrollListener() {
        @Override
        public void onOverScrolled(OverScrollListView view, int pixelsX, int pixelsY) {

            // clamp
            if (pixelsY < -350) {
                pixelsY = -350;
            }
            if (pixelsY > 0) {
                pixelsY = 0;
            }

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) _loadingView.getLayoutParams();
            lp.topMargin = _loadingStartMargin + (-pixelsY);
            _loadingView.setLayoutParams(lp);
            if (pixelsY < -300 && !_isLoading) {
                _isLoading = true;
                _loadingView.startAnimation(_rotateAnim);
            } else if (pixelsY > -300 && _isLoading) {
                _isLoading = false;
                _loadingView.clearAnimation();
            }
        }

        @Override
        public void onOverScrollComplete(OverScrollListView view, int pixelsX, int pixelsY) {
            if (_isLoading) {
                _adapter.refreshPages();
            } else {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) _loadingView.getLayoutParams();
                lp.topMargin = _loadingStartMargin;
                _loadingView.setLayoutParams(lp);
                _loadingView.clearAnimation();
            }
        }
    };

    private PagingAdapter<Workorder> _adapter = new PagingAdapter<Workorder>() {
        @Override
        public View getView(int page, int position, Workorder object, View convertView, ViewGroup parent) {
            Log.v(TAG, "getView()");
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
        public void requestPage(int page, boolean allowCache) {
            Log.v(TAG, "requestPage(), " + _displayView.getCall() + " " + page);
            requestList(page, allowCache);
        }
    };


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
            requestList(0, true);
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
                boolean cached = resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED);

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

                if (list.size() == 0) {
                    _adapter.setNoMorePages();
                }

                _adapter.setPage(page, list, cached);
                if (_isLoading) {
                    _isLoading = false;
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) _loadingView.getLayoutParams();
                    lp.topMargin = _loadingStartMargin;
                    _loadingView.setLayoutParams(lp);
                    _loadingView.clearAnimation();
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
