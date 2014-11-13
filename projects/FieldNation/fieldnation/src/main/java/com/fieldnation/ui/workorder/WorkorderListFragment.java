package com.fieldnation.ui.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cocosw.undobar.UndoBarController;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.utils.ISO8601;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.State;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class WorkorderListFragment extends Fragment {
    private static final String TAG = "ui.workorder.WorkorderListFragment";

    // web states
    private static final int WEB_REMOVING_WORKRODER = 100;
    private static final int WEB_CHANGING_WORKORDER = 101;
    private static final int WEB_CHECKING_IN = 102;

    // UI
    private PullToRefreshListView _listView;
    private SmoothProgressBar _loadingBar;

    //private PayDialog _payDialog;
    private ExpiresDialog _expiresDialog;
    private ConfirmDialog _confirmDialog;
    private DeviceCountDialog _deviceCountDialog;
    private CounterOfferDialog _counterOfferDialog;

    // Data
    private GlobalState _gs;
    private WorkorderListAdapter _adapter;
    private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public WorkorderListFragment setDisplayType(WorkorderDataSelector displayView) {
        _displayView = displayView;
        return this;
    }

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

            if (savedInstanceState.containsKey("WORKORDERS")) {
                Parcelable[] works = savedInstanceState.getParcelableArray("WORKORDERS");

                if (works != null && works.length > 0) {
                    List<Workorder> work = new LinkedList<Workorder>();
                    for (int i = 0; i < works.length; i++) {
                        work.add((Workorder) works[i]);
                    }
                    try {
                        _adapter = new WorkorderListAdapter(this.getActivity(), _displayView, work);
                        _adapter.setLoadingListener(_workorderAdapter_listener);
                        _adapter.setWorkorderCardViewListener(_workordercard_listener);
                        _loadingBar.setVisibility(View.GONE);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Log.v(TAG, "Display Type: " + _displayView.getCall());

        //_payDialog = new PayDialog(activity);
        _expiresDialog = new ExpiresDialog(getActivity());

        _confirmDialog = ConfirmDialog.getInstance(getFragmentManager(), TAG);
        _confirmDialog.setListener(_confirm_listener);

        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog.setListener(_deviceCountListener);

        _counterOfferDialog = CounterOfferDialog.getInstance(getFragmentManager(), TAG);
        _counterOfferDialog.setListener(_counterOfferDialog_listener);

        _gs.requestAuthentication(_authClient);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("_displayView", _displayView.name());

        if (_adapter != null) {
            List<Workorder> work = _adapter.getObjects();
            if (work != null && work.size() > 0) {
                Workorder[] works = new Workorder[work.size()];
                for (int i = 0; i < work.size(); i++) {
                    works[i] = work.get(i);
                }
                outState.putParcelableArray("WORKORDERS", works);
            }
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        if (_listView != null && getAdapter() != null && _listView.getAdapter() == null) {
            _listView.setAdapter(getAdapter());
        }
        super.onStart();
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause()");
        if (getAdapter() != null) {
            getAdapter().onPause();
        }
        super.onPause();
    }

    // @Override
    // public void onStop() {
    // Log.v(TAG, "Method Stub: onStop()");
    // super.onStop();
    // if (getAdapter() != null) {
    // getAdapter().onStop();
    // _adapter = null;
    // }
    // }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
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

    private WorkorderCardView.Listener _workordercard_listener = new WorkorderCardView.Listener() {
        @Override
        public void actionRequest(WorkorderCardView view, Workorder workorder) {
            final Workorder _workorder = workorder;
            _expiresDialog.show(getActivity().getSupportFragmentManager(), _expiresDialog_listener);
        }

        @Override
        public void actionCheckout(WorkorderCardView view, Workorder workorder) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(woCardViewObj.MODE_DOING_WORK);

            Pay pay = workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(TAG, workorder, pay.getMaxDevice());
            } else {
                Intent intent = _workorderService.checkout(WEB_CHANGING_WORKORDER, workorder.getWorkorderId());
                intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
                getContext().startService(intent);
                _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
            }
        }


        @Override
        public void actionCheckin(WorkorderCardView view, Workorder workorder) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(woCardViewObj.MODE_DOING_WORK);

            Intent intent = _workorderService.checkin(WEB_CHECKING_IN, workorder.getWorkorderId());
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getContext().startService(intent);
            _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
        }

        @Override
        public void actionAssignment(WorkorderCardView view, Workorder workorder) {
            final Workorder _workorder = workorder;
            _confirmDialog.show(TAG, workorder.getSchedule());
        }

        @Override
        public void actionAcknowledgeHold(WorkorderCardView view, Workorder workorder) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);

            Intent intent = _workorderService.acknowledgeHold(WEB_CHANGING_WORKORDER, workorder.getWorkorderId());
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getContext().startService(intent);
            _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
        }

        @Override
        public void viewCounter(WorkorderCardView view, Workorder workorder) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);

            _counterOfferDialog.show(TAG, workorder);
        }

        @Override
        public void onLongClick(WorkorderCardView view, Workorder workorder) {
            if (_selectedWorkorders.containsKey(workorder.getWorkorderId())) {
                _selectedWorkorders.remove(workorder.getWorkorderId());
                view.setDisplayMode(WorkorderCardView.MODE_NORMAL);
                if (_actionMode != null && _selectedWorkorders.size() == 0) {
                    _actionMode.finish();
                    _actionMode = null;
                }
            } else {
                _selectedWorkorders.put(workorder.getWorkorderId(), workorder);
                view.setDisplayMode(WorkorderCardView.MODE_SELECTED);
                if (_actionMode == null) {
                    _actionMode = ((ActionBarActivity) getActivity()).startSupportActionMode(_actionMode_Callback);
                }
            }
        }

        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
            if (view.isBundle()) {
                Intent intent = new Intent(getContext(), WorkorderBundleDetailActivity.class);
                intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
                intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, workorder.getBundleId());
                getContext().startActivity(intent);

            } else {
                Intent intent = new Intent(getContext(), WorkorderActivity.class);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
                if (workorder.getStatus().getWorkorderStatus() == WorkorderStatus.INPROGRESS || workorder.getStatus().getWorkorderStatus() == WorkorderStatus.ASSIGNED) {
                    intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_TASKS);
                } else {
                    intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
                }
                getContext().startActivity(intent);
            }
        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);

            // TODO Method Stub: onViewPayments()
            Log.v(TAG, "Method Stub: onViewPayments()");

        }
    };

    private WorkorderListAdapter.Listener<Workorder> _workorderAdapter_listener = new WorkorderListAdapter.Listener<Workorder>() {

        @Override
        public void onLoading() {
            _listView.setRefreshing();
            _loadingBar.progressiveStart();
        }

        @Override
        public void onLoadComplete() {
            _listView.onRefreshComplete();
            _loadingBar.progressiveStop();
        }
    };

    private PullToRefreshListView.OnRefreshListener _listView_onRefreshListener = new PullToRefreshListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // _adapter.update(false);
            // _loadingBar.setIndeterminate(true);
            // _loadingBar.progressiveStart();
        }
    };

    private WorkorderUndoListener.Listener _undoListener = new WorkorderUndoListener.Listener() {
        @Override
        public void onComplete(List<Workorder> success, List<Workorder> failed) {
            new Exception().printStackTrace();
            _pendingNotInterestedWorkorders.clear();
            update(false);
        }

        @Override
        public void onUndo() {
            new Exception().printStackTrace();
            _pendingNotInterestedWorkorders.clear();
            update(false);
        }
    };

    private ActionMode.Callback _actionMode_Callback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.workorder_card, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            _actionMode = null;
            _selectedWorkorders.clear();
            notifyDataSetChanged();
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.notinterested_action) {
                Enumeration<Workorder> e = _selectedWorkorders.elements();
                List<Workorder> list = new LinkedList<Workorder>();
                while (e.hasMoreElements()) {
                    Workorder wo = e.nextElement();
                    _pendingNotInterestedWorkorders.put(wo.getWorkorderId(), wo);
                    list.add(wo);
                }
                _selectedWorkorders.clear();

                _wosumUndoListener = new WorkorderUndoListener(list, getContext(), getUsername(), getAuthToken(),
                        _undoListener);
                UndoBarController.UndoBar undo = new UndoBarController.UndoBar(getActivity());
                undo.message("Undo Not Interested");
                undo.listener(_wosumUndoListener);
                undo.duration(5000);
                undo.show();

                notifyDataSetChanged();

                return true;
            }
            return false;
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
                if (getAdapter() != null)
                    getAdapter().update(false);
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

    /*-*****************************************-*/
    /*-             Event Dialogs               -*/
    /*-*****************************************-*/
    private ConfirmDialog.Listener _confirm_listener = new ConfirmDialog.Listener() {
        @Override
        public void onOk(String startDate, long durationMilliseconds) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(woCardViewObj.MODE_DOING_WORK);

            try {
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                Intent intent = _workorderService.confirmAssignment(WEB_CHANGING_WORKORDER,
                        _workorder.getWorkorderId(), startDate, ISO8601.fromUTC(end));
                intent.putExtra(KEY_WORKORDER_ID, _workorder.getWorkorderId());
                getContext().startService(intent);
                _requestWorkingWorkorders.put(_workorder.getWorkorderId(), _workorder);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void termsOnClick() {
            // TODO STUB .termsOnClick()
            Log.v(TAG, "STUB .termsOnClick()");

        }

        @Override
        public void getDateTime(Calendar start) {
            // TODO STUB .getDateTime()
            Log.v(TAG, "STUB .getDateTime()");

        }

        @Override
        public void getTime(Calendar start) {
            // TODO STUB .getTime()
            Log.v(TAG, "STUB .getTime()");

        }

        @Override
        public void getDuration(long duration) {
            // TODO STUB .getDuration()
            Log.v(TAG, "STUB .getDuration()");

        }
    };

    private ExpiresDialog.Listener _expiresDialog_listener = new ExpiresDialog.Listener() {

        @Override
        public void onOk(String dateTime) {
            long time = -1;
            if (dateTime != null) {
                try {
                    time = (ISO8601.toUtc(dateTime) - System.currentTimeMillis()) / 1000;
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(woCardViewObj.MODE_DOING_WORK);

            Intent intent = _workorderService.request(WEB_CHANGING_WORKORDER, _workorder.getWorkorderId(), time);
            intent.putExtra(KEY_WORKORDER_ID, _workorder.getWorkorderId());
            getContext().startService(intent);
            _requestWorkingWorkorders.put(_workorder.getWorkorderId(), _workorder);
        }
    };


    private DeviceCountDialog.Listener _deviceCountListener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            Intent intent = _workorderService.checkout(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), count);
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getContext().startService(intent);
            _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
        }
    };

    private CounterOfferDialog.Listener _counterOfferDialog_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires, int expirationInSeconds, Pay pay, Schedule schedule, AdditionalExpense[] expenses) {
            getActivity().startService(
                    _workorderService.setCounterOffer(WEB_CHANGING_WORKORDER,
                            workorder.getWorkorderId(), expires, reason, expirationInSeconds, pay,
                            schedule, expenses));


        }
    };


    /*-*********************************-*/
    /*-				Util				-*/
    /*-*********************************-*/
    public void update() {
        if (getAdapter() != null)
            getAdapter().update(false);
        _listView.setRefreshing();
    }

    public void hiding() {
        if (getAdapter() != null)
            getAdapter().onStop();
    }

    private WorkorderListAdapter getAdapter() {
        if (this.getActivity() == null)
            return null;
        try {
            if (_adapter == null) {
                _adapter = new WorkorderListAdapter(this.getActivity(), _displayView);
                _adapter.setLoadingListener(_workorderAdapter_listener);
                _adapter.setWorkorderCardViewListener(_workordercard_listener);
            }

            if (!_adapter.isViable()) {
                _adapter = new WorkorderListAdapter(this.getActivity(), _displayView);
                _adapter.setWorkorderCardViewListener(_workordercard_listener);
                _adapter.setLoadingListener(_workorderAdapter_listener);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return _adapter;
    }

    /*-*********************************-*/
    /*-             Web Events          -*/
    /*-*********************************-*/
    private AuthenticationClient _authClient = new AuthenticationClient() {
        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            _service = new WorkorderService(getActivity(), username, authToken,
                    _resultReceiver);
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
            if (resultCode == WEB_REMOVING_WORKRODER) {
                long workorderId = resultData.getLong(KEY_WORKORDER_ID);
                _pendingNotInterestedWorkorders.remove(workorderId);
                _requestWorkingWorkorders.remove(workorderId);
                update(false);
            } else if (resultCode == WEB_CHANGING_WORKORDER) {
                long workorderId = resultData.getLong(KEY_WORKORDER_ID);
                _requestWorkingWorkorders.remove(workorderId);
                update(false);
            } else if (resultCode == WEB_CHECKING_IN) {
                long workorderId = resultData.getLong(KEY_WORKORDER_ID);
                _requestWorkingWorkorders.remove(workorderId);
                update(false);
                Intent intent = new Intent(getContext(), WorkorderActivity.class);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorderId);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_TASKS);
                getContext().startActivity(intent);
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            if (_service != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authClient);
            // TODO, toast failure, put ui in wait mode
        }
    };

}
