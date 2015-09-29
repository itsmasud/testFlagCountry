package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.GpsLocationService;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.payment.PaymentDetailActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderCardView;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaib.ahmed on Sept/08/2015.
 */
public class ShareRequestActivity extends AuthFragmentActivity {
    private static final String TAG = "ShareRequestActivity";

    private static final String STATE_PROFILE = "STATE_PROFILE";
    private static final String STATE_IS_AUTH = "STATE_IS_AUTH";
    private static final String STATE_SHOWING_DIALOG = "STATE_SHOWING_DIALOG";

    // UI
    private OverScrollListView _listView;
    private RefreshView _refreshView;
    private EmptyWoListView _emptyView;


    // Data
    private WorkorderClient _workorderClient;
    private GpsLocationService _gpsLocationService;


    // state data
    private WorkorderDataSelector _displayView = WorkorderDataSelector.ASSIGNED;

    private Profile _profile = null;
    private boolean _isAuth = false;
    private boolean _calledMyWork = false;

    public ShareRequestActivity() {
        super();
        Log.v(TAG, "Construct");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_request);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);


        _adapter.setOnLoadingCompleteListener(_adapterListener);

        _listView = (OverScrollListView) findViewById(R.id.workorders_listview);
        _listView.setDivider(null);
        _listView.setOnOverScrollListener(_refreshView);
        _listView.setAdapter(_adapter);


        _emptyView = (EmptyWoListView) findViewById(R.id.empty_view);


        _workorderClient = new WorkorderClient(_workorderData_listener);
        _workorderClient.connect(this);


        Log.v(TAG, "onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_IS_AUTH, _isAuth);
        if (_profile != null) {
            outState.putParcelable(STATE_PROFILE, _profile);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        setLoading(true);
        _gpsLocationService = new GpsLocationService(this);

    }

    @Override
    protected void onPause() {
        if (_gpsLocationService != null)
            _gpsLocationService.stopLocationUpdates();

        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setLoading(boolean loading) {
        Log.v(TAG, "setLoading()");
        if (_refreshView != null) {
            if (loading) {
                _refreshView.startRefreshing();
            } else {
                _refreshView.refreshComplete();
            }
        }
    }


//    private void populateUi() {
//
//        if (_profile == null)
//            return;
//
//
//        Stopwatch stopwatch = new Stopwatch(true);
//        final UploadingDocument[] docs = _workorder.getDocuments();
//        if (docs != null && docs.length > 0) {
//            Log.v(TAG, "_reviewList.getChildCount() " + _reviewList.getChildCount());
//            Log.v(TAG, "docs.length " + docs.length);
//
//            if (_reviewList.getChildCount() > docs.length) {
//                _reviewList.removeViews(docs.length - 1, _reviewList.getChildCount() - docs.length);
//            }
//
//            ForLoopRunnable r = new ForLoopRunnable(docs.length, new Handler()) {
//                private final UploadingDocument[] _docs = docs;
//
//                @Override
//                public void next(int i) throws Exception {
//                    ShareRequestRowView v = null;
//                    if (i < _reviewList.getChildCount()) {
//                        v = (ShareRequestRowView) _reviewList.getChildAt(i);
//                    } else {
//                        v = new ShareRequestRowView(ShareRequestActivity.this);
//                        _reviewList.addView(v);
//                    }
//                    UploadingDocument doc = _docs[i];
//                    v.setData(doc);
//                }
//            };
//            _reviewList.postDelayed(r, new Random().nextInt(1000));
//        } else {
//            _reviewList.removeAllViews();
//        }
//        Log.v(TAG, "pop docs time " + stopwatch.finish());
//    }


    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
//            if (_workorder != null) {
//                _workorder.dispatchOnChange();
//            }
        }
    };


    private void requestList(int page, boolean allowCache) {
        Log.v(TAG, "requestList " + page);
        if (page == 0)
            setLoading(true);
        WorkorderClient.list(App.get(), _displayView, page, false, allowCache);
    }

    private void addPage(int page, List<Workorder> list) {
        if (page == 0 && list.size() == 0 && _displayView.shouldShowGoToMarketplace()) {
            _emptyView.setVisibility(View.VISIBLE);
        } else if (page == 0 && list.size() > 0 || !_displayView.shouldShowGoToMarketplace()) {
            _emptyView.setVisibility(View.GONE);

        }

        if (list.size() == 0) {
            _adapter.setNoMorePages();
        }

        _adapter.setPage(page, list);
    }


    private final AdapterView.OnItemSelectedListener _workorder_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Todo: Populate _tasksSpinner when the
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    private final AdapterView.OnItemSelectedListener _tasks_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Todo: not sure
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGotProfile(Profile profile) {
            Log.v(TAG, "_globalTopic_listener.onGotProfile");
            if (profile != null)
                Log.v(TAG, profile.toJson().display());
            _profile = profile;
//            doNextStep();
        }
    };

    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            _isAuth = true;
//            doNextStep();
        }

        @Override
        public void onNotAuthenticated() {
            //Todo: If application is not logged-in, need to show login screen
        }
    };


    private final RefreshView.Listener _refreshViewListener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
//            Log.v(TAG, "_refreshViewListener.onStartRefresh()");
            _adapter.refreshPages();
        }
    };

    private final PagingAdapter<Workorder> _adapter = new PagingAdapter<Workorder>() {
        @Override
        public View getView(Workorder object, View convertView, ViewGroup parent) {
            WorkorderCardView v = null;
            if (convertView == null) {
                v = new WorkorderCardView(parent.getContext());
            } else if (convertView instanceof WorkorderCardView) {
                v = (WorkorderCardView) convertView;
            } else {
                v = new WorkorderCardView(parent.getContext());
            }

            if (_gpsLocationService != null && _gpsLocationService.getLocation() != null)
                v.setWorkorder(object, _gpsLocationService.getLocation());
            else
                v.setWorkorder(object, null);
            v.setWorkorderSummaryListener(_wocv_listener);
            v.setDisplayMode(WorkorderCardView.MODE_NORMAL);
            v.makeButtonsGone();

            return v;
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            requestList(page, allowCache);
        }
    };

    private final PagingAdapter.OnLoadingCompleteListener _adapterListener = new PagingAdapter.OnLoadingCompleteListener() {
        @Override
        public void onLoadingComplete() {
//            Log.v(TAG, "_adapterListener.onLoadingComplete");
            setLoading(false);
        }
    };


    private final WorkorderCardView.Listener _wocv_listener = new WorkorderCardView.Listener() {
        @Override
        public void actionRequest(WorkorderCardView view, Workorder workorder) {

        }

        @Override
        public void actionWithdrawRequest(WorkorderCardView view, final Workorder workorder) {

        }

        @Override
        public void actionCheckout(WorkorderCardView view, Workorder workorder) {

        }

        @Override
        public void actionCheckin(WorkorderCardView view, Workorder workorder) {

        }

        @Override
        public void actionAssignment(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void actionAcknowledgeHold(WorkorderCardView view, Workorder workorder) {

        }

        @Override
        public void viewCounter(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
//            Intent intent = new Intent(getActivity(), WorkorderActivity.class);
//            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
////            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER, workorder);
//            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
//            getActivity().startActivity(intent);
//            view.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
                        Log.e(TAG, "onClick");

        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {

        }

        @Override
        public void actionReadyToGo(WorkorderCardView view, Workorder workorder) {

        }
    };


    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/
    private final WorkorderClient.Listener _workorderData_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderData_listener.onConnected");
            _workorderClient.subList(_displayView);
            _workorderClient.subGet(false);
            _workorderClient.subActions();
            _adapter.refreshPages();
        }

        @Override
        public void onList(List<Workorder> list, WorkorderDataSelector selector, int page, boolean failed) {
            Log.v(TAG, "_workorderData_listener.onList");
            if (!selector.equals(_displayView))
                return;
            if (list != null)
                addPage(page, list);
        }

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            _adapter.refreshPages();
        }
    };

    public static void startNew(Context context) {
        Intent intent = new Intent(context, ShareRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
