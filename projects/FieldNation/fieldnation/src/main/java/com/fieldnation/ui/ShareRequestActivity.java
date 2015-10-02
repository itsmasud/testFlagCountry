package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.GpsLocationService;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.workorder.WorkorderCardView;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.ui.workorder.detail.TaskListView;

import java.util.LinkedList;
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
    private OverScrollListView _workorderListView;
    private TaskListView _taskList;
    private OverScrollListView _fileList;
    private RefreshView _refreshView;
    private EmptyWoListView _emptyView;


    // Data
    private WorkorderClient _workorderClient;
    private GpsLocationService _gpsLocationService;
    private Workorder _workorder;
    private List<Task> _tasks = null;
    private Task _currentTask;


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

        _workorderListView = (OverScrollListView) findViewById(R.id.workorders_listview);
        _workorderListView.setDivider(null);
        _workorderListView.setOnOverScrollListener(_refreshView);
        _workorderListView.setAdapter(_adapter);


        _emptyView = (EmptyWoListView) findViewById(R.id.empty_view);


        _workorderClient = new WorkorderClient(_workorderData_listener);
        _workorderClient.connect(this);


        _taskList = (TaskListView) findViewById(R.id.tasks_listview);
        _taskList.setTaskListViewListener(_taskListView_listener);

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

        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(this);

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


//    public void getUploadSlotData(boolean allowCache) {
//        Log.v(TAG, "getData");
//        setLoading(true);
////        WorkorderClient.get(ShareRequestActivity.this, _workorder.getWorkorderId(), allowCache);
//        WorkorderClient.get(App.get(), _workorder.getWorkorderId(), false);
////        final UploadSlot[] slots = _workorder.getUploadSlots();
//        final UploadSlot[] slots = _workorder.getUploadSlots();
//        Log.v(TAG, "slots: " + _workorder.getUploadSlots().length);
//
//    }



    public void getData(boolean allowCache) {
        Log.v(TAG, "getData");
        setLoading(true);
            WorkorderClient.get(this, _workorder.getWorkorderId(), allowCache);
    }


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
            Log.e(TAG, "onClick_WorkorderCardView");
            _workorder = workorder;
            _workorder.addListener(_workorder_listener);

            getData(true);
            _workorderListView.setVisibility(View.GONE);

            UploadSlot[] slot = _workorder.getUploadSlots();
            int uploadSlots = slot.length;

            setLoading(true);

        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {

        }

        @Override
        public void actionReadyToGo(WorkorderCardView view, Workorder workorder) {

        }
    };


    private Workorder.Listener _workorder_listener = new Workorder.Listener() {
        @Override
        public void onChange(Workorder workorder) {
            Log.v(TAG, "_workorder_listener");
            getData(false);
        }
    };


            private final TaskListView.Listener _taskListView_listener = new TaskListView.Listener() {
        @Override
        public void onCheckin(Task task) {
        }

        @Override
        public void onCheckout(Task task) {
        }

        @Override
        public void onCloseOutNotes(Task task) {
        }

        @Override
        public void onConfirmAssignment(Task task) {
        }

        @Override
        public void onCustomField(Task task) {
        }

        @Override
        public void onDownload(Task task) {

        }

        @Override
        public void onEmail(Task task) {
        }

        @Override
        public void onPhone(Task task) {

        }

        @Override
        public void onShipment(Task task) {

        }

        @Override
        public void onSignature(Task task) {

        }

        @Override
        public void onUploadFile(Task task) {
            _currentTask = task;
        }

        @Override
        public void onUploadPicture(Task task) {
            _currentTask = task;
        }

        @Override
        public void onUniqueTask(Task task) {
            if (task.getCompleted())
                return;
            WorkorderClient.actionCompleteTask(App.get(),
                    _workorder.getWorkorderId(), task.getTaskId());
            setLoading(true);
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
            _workorderClient.subDeliverableUpload();
            _adapter.refreshPages();
//            getData(true);
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
            Log.v(TAG, "_workorderClient_listener.onAction " + workorderId + "/" + action);
            getData(false);
            _adapter.refreshPages();
        }



        @Override
        public void onTaskList(long workorderId, List<Task> tasks, boolean failed) {
        }

        @Override
        public void onUploadDeliverable(long workorderId, long slotId, String filename, boolean isComplete, boolean failed) {
            getData(false);
        }

    };





}
