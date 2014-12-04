package com.fieldnation.ui.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.PagingAdapter;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.utils.ISO8601;

import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WorkorderListFragment extends Fragment {
    private static final String TAG = "ui.workorder.WorkorderListFragment";

    // State
    private static final String STATE_DISPLAY = "STATE_DISPLAY";

    // WEB
    private static final int WEB_GET_LIST = 0;
    //private static final int WEB_REMOVING_WORKRODER = 1;
    private static final int WEB_CHANGING_WORKORDER = 2;
    private static final int WEB_CHECKING_IN = 3;

    // Request Key
    private static final String KEY_PAGE_NUM = "WorkorderListFragment.PAGE_NUM";
    private static final String KEY_WORKORDER_ID = "WorkorderListFragment.WORKORDER_ID";

    // UI
    private OverScrollListView _listView;
    private RefreshView _loadingView;

    // Dialogs
    private ExpiresDialog _expiresDialog;
    private ConfirmDialog _confirmDialog;
    private DeviceCountDialog _deviceCountDialog;
    private CounterOfferDialog _counterOfferDialog;
    private TermsDialog _termsDialog;

    // Data
    private GlobalState _gs;
    private String _username;
    private String _authToken;
    private WorkorderService _service;
    private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;
    //private ActionMode _actionMode = null;
    private WorkorderUndoListener _wosumUndoListener;

    private Set<Long> _pendingNotInterested = new HashSet<Long>();
    private Set<Long> _requestWorking = new HashSet<Long>();
    private Set<Long> _selected = new HashSet<Long>();


	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_DISPLAY)) {
                Log.v(TAG, "Restoring state");
                _displayView = WorkorderDataSelector.fromName(savedInstanceState.getString(STATE_DISPLAY));
            }
        }

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

        _loadingView = (RefreshView) view.findViewById(R.id.loading_view);
        _loadingView.setListener(_refreshViewListener);

        _adapter.setListener(_adapterListener);

        _listView = (OverScrollListView) view.findViewById(R.id.workorders_listview);
        _listView.setDivider(null);
        _listView.setOnOverScrollListener(_loadingView);
        _listView.setAdapter(_adapter);

        _expiresDialog = ExpiresDialog.getInstance(getFragmentManager(), TAG);
        _expiresDialog.setListener(_expiresDialog_listener);

        _confirmDialog = ConfirmDialog.getInstance(getFragmentManager(), TAG);
        _confirmDialog.setListener(_confirmDialog_listener);

        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog.setListener(_deviceCountDialog_listener);

        _counterOfferDialog = CounterOfferDialog.getInstance(getFragmentManager(), TAG);
        _counterOfferDialog.setListener(_counterOfferDialog_listener);

        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);

        Log.v(TAG, "Display Type: " + _displayView.getCall());
        _gs.requestAuthentication(_authClient);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_DISPLAY, _displayView.name());
        super.onSaveInstanceState(outState);
    }

    public WorkorderListFragment setDisplayType(WorkorderDataSelector displayView) {
        _displayView = displayView;
        return this;
    }

    public WorkorderDataSelector getDisplayType() {
        return _displayView;
    }

    @Override
    public void onResume() {
        super.onResume();
        _adapter.refreshPages();
        _loadingView.startRefreshing();
    }

    public void update() {
        //_loadingView.startRefreshing();
        _adapter.refreshPages();
    }

    private void requestList(int page, boolean allowCache) {
        if (_service == null)
            return;

        _loadingView.startRefreshing();
        Intent intent = _service.getList(WEB_GET_LIST, page, _displayView, allowCache);
        intent.putExtra(KEY_PAGE_NUM, page);
        if (getActivity() != null)
            getActivity().startService(intent);
    }

    private void addPage(int page, List<Workorder> list, boolean isCached) {
        if (list.size() == 0) {
            _adapter.setNoMorePages();
        }
        _adapter.setPage(page, list, isCached);

        if (!isCached) {
            _pendingNotInterested.clear();
            _requestWorking.clear();
            _selected.clear();
        }
    }


    /*-*************************************************-*/
    /*-				Events Workorder Card				-*/
    /*-*************************************************-*/
    private WorkorderCardView.Listener _wocv_listener = new WorkorderCardView.Listener() {
        @Override
        public void actionRequest(WorkorderCardView view, Workorder workorder) {
            _expiresDialog.show(workorder);
        }

        @Override
        public void actionCheckout(WorkorderCardView view, Workorder workorder) {
            Pay pay = workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(workorder, pay.getMaxDevice());
            } else {
                Intent intent = _service.checkout(WEB_CHANGING_WORKORDER, workorder.getWorkorderId());
                intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
                getActivity().startService(intent);
                _requestWorking.add(workorder.getWorkorderId());
                _adapter.notifyDataSetChanged();
            }
        }


        @Override
        public void actionCheckin(WorkorderCardView view, Workorder workorder) {
            Intent intent = _service.checkin(WEB_CHECKING_IN, workorder.getWorkorderId());
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getActivity().startService(intent);
            _requestWorking.add(workorder.getWorkorderId());
            _adapter.notifyDataSetChanged();
        }

        @Override
        public void actionAssignment(WorkorderCardView view, Workorder workorder) {
            _confirmDialog.show(workorder, workorder.getSchedule());
        }

        @Override
        public void actionAcknowledgeHold(WorkorderCardView view, Workorder workorder) {
            //TODO set loading mode
            Intent intent = _service.acknowledgeHold(WEB_CHANGING_WORKORDER, workorder.getWorkorderId());
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getActivity().startService(intent);
            _requestWorking.add(workorder.getWorkorderId());
            _adapter.notifyDataSetChanged();
        }

        @Override
        public void viewCounter(WorkorderCardView view, Workorder workorder) {
            //TODO set loading mode
            _counterOfferDialog.show(workorder);
        }

        @Override
        public void onLongClick(WorkorderCardView view, Workorder workorder) {
            if (_selected.contains(workorder.getWorkorderId())) {
                _selected.remove(workorder.getWorkorderId());
                view.setDisplayMode(WorkorderCardView.MODE_NORMAL);
//                if (_actionMode != null && _selected.size() == 0) {
//                    _actionMode.finish();
//                    _actionMode = null;
//                }
            } else {
                _selected.add(workorder.getWorkorderId());
                view.setDisplayMode(WorkorderCardView.MODE_SELECTED);
//                if (_actionMode == null) {
//                    _actionMode = ((ActionBarActivity) getActivity()).startSupportActionMode(_actionMode_Callback);
//                }
            }
        }

        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
            if (view.isBundle()) {
                Intent intent = new Intent(getActivity(), WorkorderBundleDetailActivity.class);
                intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
                intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, workorder.getBundleId());
                getActivity().startActivity(intent);

            } else {
                Intent intent = new Intent(getActivity(), WorkorderActivity.class);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
                if (workorder.getStatus().getWorkorderStatus() == WorkorderStatus.INPROGRESS || workorder.getStatus().getWorkorderStatus() == WorkorderStatus.ASSIGNED) {
                    intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_TASKS);
                } else {
                    intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
                }
                getActivity().startActivity(intent);
            }
        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {
            //set  loading mode
            view.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
            // TODO Method Stub: onViewPayments()
            Log.v(TAG, "Method Stub: onViewPayments()");
        }
    };

    /*-*****************************************-*/
    /*-				Events Dialogs				-*/
    /*-*****************************************-*/
    private DeviceCountDialog.Listener _deviceCountDialog_listener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            Intent intent = _service.checkout(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), count);
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getActivity().startService(intent);
            _requestWorking.add(workorder.getWorkorderId());
            _adapter.notifyDataSetChanged();
        }
    };

    private ExpiresDialog.Listener _expiresDialog_listener = new ExpiresDialog.Listener() {

        @Override
        public void onOk(Workorder workorder, String dateTime) {
            long time = -1;
            if (dateTime != null) {
                try {
                    time = (ISO8601.toUtc(dateTime) - System.currentTimeMillis()) / 1000;
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // request the workorder
            Intent intent = _service.request(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), time);
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getActivity().startService(intent);

            // notify the UI
            _requestWorking.add(workorder.getWorkorderId());
            _adapter.notifyDataSetChanged();
        }
    };

    private ConfirmDialog.Listener _confirmDialog_listener = new ConfirmDialog.Listener() {
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            //set  loading mode
            try {
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                Intent intent = _service.confirmAssignment(WEB_CHANGING_WORKORDER,
                        workorder.getWorkorderId(), startDate, ISO8601.fromUTC(end));
                intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
                getActivity().startService(intent);
                _requestWorking.add(workorder.getWorkorderId());
                _adapter.notifyDataSetChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onCancel(Workorder workorder) {
        }

        @Override
        public void termsOnClick(Workorder workorder) {
            _termsDialog.show();
        }

    };

    private CounterOfferDialog.Listener _counterOfferDialog_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires, int expirationInSeconds, Pay pay, Schedule schedule, AdditionalExpense[] expenses) {
            getActivity().startService(
                    _service.setCounterOffer(WEB_CHANGING_WORKORDER,
                            workorder.getWorkorderId(), expires, reason, expirationInSeconds, pay,
                            schedule, expenses));
            _requestWorking.add(workorder.getWorkorderId());
            _adapter.notifyDataSetChanged();
        }
    };

    /*-*************************************-*/
    /*-				Events UI				-*/
    /*-*************************************-*/

//    private ActionMode.Callback _actionMode_Callback = new ActionMode.Callback() {
//
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.workorder_card, menu);
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            _actionMode = null;
//            _selected.clear();
//            _adapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            if (item.getItemId() == R.id.notinterested_action) {
//                Iterator<Long> iter = _selected.iterator();
//                List<Long> list = new LinkedList<Long>();
//                while (iter.hasNext()) {
//                    Long woId = iter.next();
//                    _pendingNotInterested.add(woId);
//                    list.add(woId);
//                }
//                _selected.clear();
//
//                _wosumUndoListener = new WorkorderUndoListener(list, getActivity(), _username, _authToken, _undoListener);
//                UndoBarController.UndoBar undo = new UndoBarController.UndoBar(getActivity());
//                undo.message("Undo Not Interested");
//                undo.listener(_wosumUndoListener);
//                undo.duration(5000);
//                undo.show();
//
//                notifyDataSetChanged();
//
//                return true;
//            }
//            return false;
//        }
//    };

//    private WorkorderUndoListener.Listener _undoListener = new WorkorderUndoListener.Listener() {
//        @Override
//        public void onComplete(List<Workorder> success, List<Workorder> failed) {
//            new Exception().printStackTrace();
//            _pendingNotInterestedWorkorders.clear();
//            update(false);
//        }
//
//        @Override
//        public void onUndo() {
//            new Exception().printStackTrace();
//            _pendingNotInterestedWorkorders.clear();
//            update(false);
//        }
//    };

    private RefreshView.Listener _refreshViewListener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            _adapter.refreshPages();
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

            if (_pendingNotInterested.contains(object.getWorkorderId())) {
                // wosum.setDisplayMode(WorkorderCardView.MODE_UNDO_NOT_INTERESTED);
                return new View(getActivity());
            } else if (_requestWorking.contains(object.getWorkorderId())) {
                v.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
            } else if (_selected.contains(object.getWorkorderId())) {
                v.setDisplayMode(WorkorderCardView.MODE_SELECTED);
            } else {
                v.setDisplayMode(WorkorderCardView.MODE_NORMAL);
            }

            v.setWorkorder(object);
            v.setWorkorderSummaryListener(_wocv_listener);

            return v;
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            Log.v(TAG, "requestPage(), " + _displayView.getCall() + " " + page);
            requestList(page, allowCache);
        }
    };

    private PagingAdapter.Listener _adapterListener = new PagingAdapter.Listener() {
        @Override
        public void onLoadingComplete() {
            _loadingView.refreshComplete();
            _requestWorking.clear();
            _pendingNotInterested.clear();
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
            _username = username;
            _authToken = authToken;
            if (getActivity() != null) {
                _service = new WorkorderService(getActivity(), username, authToken, _resultReciever);
                requestList(0, true);
            }
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebResultReceiver _resultReciever = new WebResultReceiver(new Handler()) {

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
                        e.printStackTrace();
                    }
                }

                addPage(page, list, cached);
                _loadingView.refreshComplete();
            } else if (resultCode == WEB_CHANGING_WORKORDER) {
                long woId = resultData.getLong(KEY_WORKORDER_ID);
                //_requestWorking.remove(woId);
                //_pendingNotInterested.remove(woId);
                // this workorder might now be gone... need to refresh the list
//                _loadingView.startRefreshing();
                _adapter.refreshPages();
                _loadingView.refreshComplete();
                //_loadingView.refreshComplete();
            } else if (resultCode == WEB_CHECKING_IN) {
                long woId = resultData.getLong(KEY_WORKORDER_ID);
                _requestWorking.remove(woId);
                _pendingNotInterested.remove(woId);
                Intent intent = new Intent(getActivity(), WorkorderActivity.class);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, woId);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_TASKS);
                getActivity().startActivity(intent);
                _adapter.refreshPages();
                _loadingView.refreshComplete();
            } else {
                _loadingView.refreshComplete();
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_service != null || _service.getAuthToken() != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());

            }
            _gs.requestAuthenticationDelayed(_authClient);
            _loadingView.refreshFailed();
            Toast.makeText(getActivity(), "Request failed please try again.", Toast.LENGTH_LONG).show();
        }
    };


}
