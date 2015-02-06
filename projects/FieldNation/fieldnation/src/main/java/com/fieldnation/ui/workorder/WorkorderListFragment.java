package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.GaTopic;
import com.fieldnation.ui.EmptyWoListView;
import com.fieldnation.ui.GpsLocationService;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.PagingAdapter;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.AcceptBundleDialog;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.dialog.LocationDialog;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.payment.PaymentDetailActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WorkorderListFragment extends Fragment {
    private final String TAG = UniqueTag.makeTag("ui.workorder.WorkorderListFragment");

    // State
    private static final String STATE_DISPLAY = "STATE_DISPLAY";
    private static final String STATE_CURRENT_WORKORDER = "STATE_CURRENT_WORKORDER";

    private static final int RESULT_CODE_ENABLE_GPS_CHECKIN = 1;
    private static final int RESULT_CODE_ENABLE_GPS_CHECKOUT = 2;

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
    private EmptyWoListView _emptyView;

    // Dialogs
    private ExpiresDialog _expiresDialog;
    private ConfirmDialog _confirmDialog;
    private DeviceCountDialog _deviceCountDialog;
    private CounterOfferDialog _counterOfferDialog;
    private TermsDialog _termsDialog;
    private AcceptBundleDialog _acceptBundleDialog;
    private LocationDialog _locationDialog;
    private OneButtonDialog _locationLoadingDialog;

    // Data
    private String _username;
    private String _authToken;
    private WorkorderService _service;
    private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;
    //private ActionMode _actionMode = null;
    private Set<Long> _pendingNotInterested = new HashSet<Long>();
    private Set<Long> _requestWorking = new HashSet<Long>();
    private Set<Long> _selected = new HashSet<Long>();
    private GpsLocationService _gpsLocationService;
    private Workorder _currentWorkorder;

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

            if (savedInstanceState.containsKey(STATE_CURRENT_WORKORDER))
                _currentWorkorder = savedInstanceState.getParcelable(STATE_CURRENT_WORKORDER);
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
        Log.v(TAG, "onViewCreated");

        _loadingView = (RefreshView) view.findViewById(R.id.loading_view);
        _loadingView.setListener(_refreshViewListener);

        _adapter.setListener(_adapterListener);

        _listView = (OverScrollListView) view.findViewById(R.id.workorders_listview);
        _listView.setDivider(null);
        _listView.setOnOverScrollListener(_loadingView);
        _listView.setAdapter(_adapter);

        _emptyView = (EmptyWoListView) view.findViewById(R.id.empty_view);

        _expiresDialog = ExpiresDialog.getInstance(getFragmentManager(), TAG);
        _expiresDialog.setListener(_expiresDialog_listener);

        _confirmDialog = ConfirmDialog.getInstance(getFragmentManager(), TAG);
        _confirmDialog.setListener(_confirmDialog_listener);

        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog.setListener(_deviceCountDialog_listener);

        _counterOfferDialog = CounterOfferDialog.getInstance(getFragmentManager(), TAG);
        _counterOfferDialog.setListener(_counterOfferDialog_listener);

        _acceptBundleDialog = AcceptBundleDialog.getInstance(getFragmentManager(), TAG);
        _acceptBundleDialog.setListener(_acceptBundleDialog_listener);

        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);

        _locationDialog = LocationDialog.getInstance(getFragmentManager(), TAG);

        _locationLoadingDialog = OneButtonDialog.getInstance(getFragmentManager(), TAG);
        _locationLoadingDialog.setData(getString(R.string.dialog_location_loading_title),
                getString(R.string.dialog_location_loading_body),
                getString(R.string.dialog_location_loading_button),
                _locationLoadingDialog_listener);

        Log.v(TAG, "Display Type: " + _displayView.getCall());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putString(STATE_DISPLAY, _displayView.name());
        if (_currentWorkorder != null)
            outState.putParcelable(STATE_CURRENT_WORKORDER, _currentWorkorder);

        super.onSaveInstanceState(outState);
    }

    public WorkorderListFragment setDisplayType(WorkorderDataSelector displayView) {
        Log.v(TAG, "setDisplayType");
        _displayView = displayView;
        return this;
    }

    public WorkorderDataSelector getDisplayType() {
        return _displayView;
    }

    public String getGaLabel() {
        return "Work" + misc.capitalize(_displayView.getCall()) + "List";
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        _adapter.refreshPages();
        setLoading(true);
        AuthTopicService.subscribeAuthState(getActivity(), 0, TAG, _topicReceiver);
        GaTopic.dispatchScreenView(getActivity(), getGaLabel());
        _gpsLocationService = new GpsLocationService(getActivity());
    }

    @Override
    public void onPause() {
        _gpsLocationService.stopLocationUpdates();
        super.onPause();
    }

    private void setLoading(boolean loading) {
        if (_loadingView != null) {
            if (loading) {
                _loadingView.startRefreshing();
            } else {
                _loadingView.refreshComplete();
            }
        }
    }

    public void update() {
        _adapter.refreshPages();
    }

    private void requestList(int page, boolean allowCache) {
        if (_service == null)
            return;

        setLoading(true);
        Intent intent = _service.getList(WEB_GET_LIST, page, _displayView, allowCache);
        intent.putExtra(KEY_PAGE_NUM, page);
        if (getActivity() != null)
            getActivity().startService(intent);
    }

    private void addPage(int page, List<Workorder> list, boolean isCached) {
        if (page == 0 && list.size() == 0 && _displayView.shouldShowGoToMarketplace()) {
            _emptyView.setVisibility(View.VISIBLE);
        } else if (page == 0 && list.size() > 0 || !_displayView.shouldShowGoToMarketplace()) {
            _emptyView.setVisibility(View.GONE);
        }

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

    private void startCheckin() {
        Log.v(TAG, "startCheckin");
        // everything is awsome. checkin
        _gpsLocationService.setListener(_gps_checkInListener);
        if (!_gpsLocationService.isLocationServicesEnabled()) {
            _locationDialog.show(_currentWorkorder.getIsGpsRequired(), _locationDialog_checkInListener);
        } else if (_gpsLocationService.hasLocation()) {
            doCheckin();
        } else if (_gpsLocationService.isRunning()) {
            _locationLoadingDialog.show();
        } else if (_gpsLocationService.isLocationServicesEnabled()) {
            _locationLoadingDialog.show();
            _gpsLocationService.startLocation();
        } else {
            // location is disabled, or failed. ask for them to be enabled
            Log.v(TAG, "Should not be here");
        }
        setLoading(true);
    }

    private void startCheckOut() {
        Log.v(TAG, "startCheckOut");
        _gpsLocationService.setListener(_gps_checkOutListener);
        if (!_gpsLocationService.isLocationServicesEnabled()) {
            _locationDialog.show(_currentWorkorder.getIsGpsRequired(), _locationDialog_checkOutListener);
        } else if (_gpsLocationService.hasLocation()) {
            doCheckOut();
        } else if (_gpsLocationService.isRunning()) {
            _locationLoadingDialog.show();
        } else if (_gpsLocationService.isLocationServicesEnabled()) {
            _locationLoadingDialog.show();
            _gpsLocationService.startLocation();
        } else {
            // location is disabled, or failed. ask for them to be enabled
            Log.v(TAG, "Should not be here");
        }
        setLoading(true);
    }

    private void doCheckin() {
        _gpsLocationService.setListener(null);
        GaTopic.dispatchEvent(getActivity(), getGaLabel(), GaTopic.ACTION_CHECKIN, "WorkorderCardView", 1);
        if (_gpsLocationService.hasLocation()) {
            getActivity().startService(
                    _service.checkin(WEB_CHANGING_WORKORDER, _currentWorkorder.getWorkorderId(), _gpsLocationService.getLocation()));
        } else {
            getActivity().startService(
                    _service.checkin(WEB_CHANGING_WORKORDER, _currentWorkorder.getWorkorderId()));
        }

    }

    private void doCheckOut() {
        _gpsLocationService.setListener(null);
        GaTopic.dispatchEvent(getActivity(), getGaLabel(), GaTopic.ACTION_CHECKOUT, "WorkorderCardView", 1);
        if (_gpsLocationService.hasLocation()) {
            getActivity().startService(
                    _service.checkout(WEB_CHANGING_WORKORDER, _currentWorkorder.getWorkorderId(), _gpsLocationService.getLocation()));
        } else {
            getActivity().startService(
                    _service.checkout(WEB_CHANGING_WORKORDER, _currentWorkorder.getWorkorderId()));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_ENABLE_GPS_CHECKIN) {
            startCheckin();
        } else if (requestCode == RESULT_CODE_ENABLE_GPS_CHECKOUT) {
            startCheckOut();
        }
    }

    /*-*************************************************-*/
    /*-				Events Workorder Card				-*/
    /*-*************************************************-*/
    private OneButtonDialog.Listener _locationLoadingDialog_listener = new OneButtonDialog.Listener() {
        @Override
        public void onButtonClick() {
            _gpsLocationService.stopLocationUpdates();
            setLoading(false);
        }

        @Override
        public void onDismiss() {
            setLoading(false);
        }
    };
    private final GpsLocationService.Listener _gps_checkInListener = new GpsLocationService.Listener() {
        @Override
        public void onLocation(Location location) {
            Log.v(TAG, "_gps_checkInListener.onLocation");
            startCheckin();
            _locationLoadingDialog.dismiss();
        }
    };
    private final GpsLocationService.Listener _gps_checkOutListener = new GpsLocationService.Listener() {
        @Override
        public void onLocation(Location location) {
            Log.v(TAG, "_gps_checkOutListener.onLocation");
            startCheckOut();
            _locationLoadingDialog.dismiss();
        }
    };
    private LocationDialog.Listener _locationDialog_checkInListener = new LocationDialog.Listener() {
        @Override
        public void onOk() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, RESULT_CODE_ENABLE_GPS_CHECKIN);
        }

        @Override
        public void onCancel() {
            if (_currentWorkorder.getIsGpsRequired()) {
                // todo pop dialog, gps required... could not complete check in
            } else {
                doCheckin();
            }
            setLoading(false);
        }

        @Override
        public void onDismiss() {
            if (_currentWorkorder.getIsGpsRequired()) {
                // todo pop dialog, gps required... could not complete check in
            } else {
                doCheckin();
            }
            setLoading(false);
        }
    };

    private LocationDialog.Listener _locationDialog_checkOutListener = new LocationDialog.Listener() {
        @Override
        public void onOk() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, RESULT_CODE_ENABLE_GPS_CHECKOUT);
        }

        @Override
        public void onCancel() {
            if (_currentWorkorder.getIsGpsRequired()) {
                // todo pop dialog, gps required... could not complete check out
            } else {
                doCheckOut();
            }
            setLoading(false);
        }

        @Override
        public void onDismiss() {
            if (_currentWorkorder.getIsGpsRequired()) {
                // todo pop dialog, gps required... could not complete check in
            } else {
                doCheckOut();
            }
            setLoading(false);
        }
    };

    private WorkorderCardView.Listener _wocv_listener = new WorkorderCardView.Listener() {
        @Override
        public void actionRequest(WorkorderCardView view, Workorder workorder) {
            if (workorder.isBundle()) {
                _acceptBundleDialog.show(workorder);
            } else {
                _expiresDialog.show(workorder);
            }
        }

        @Override
        public void actionCheckout(WorkorderCardView view, Workorder workorder) {
            _currentWorkorder = workorder;
            Pay pay = workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(workorder, pay.getMaxDevice());
            } else {
                startCheckOut();
                _requestWorking.add(workorder.getWorkorderId());
                _adapter.notifyDataSetChanged();
            }
        }


        @Override
        public void actionCheckin(WorkorderCardView view, Workorder workorder) {
            _currentWorkorder = workorder;
            startCheckin();
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
            GaTopic.dispatchEvent(getActivity(),
                    getGaLabel(), GaTopic.ACTION_LONG_CLICK, "WorkorderCardView", 1);

//            if (_selected.contains(workorder.getWorkorderId())) {
//                _selected.remove(workorder.getWorkorderId());
//                view.setDisplayMode(WorkorderCardView.MODE_NORMAL);
//                if (_actionMode != null && _selected.size() == 0) {
//                    _actionMode.finish();
//                    _actionMode = null;
//                }
//            } else {
//                _selected.add(workorder.getWorkorderId());
//                view.setDisplayMode(WorkorderCardView.MODE_SELECTED);
//                if (_actionMode == null) {
//                    _actionMode = ((ActionBarActivity) getActivity()).startSupportActionMode(_actionMode_Callback);
//                }
//            }
        }

        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
//            if (view.isBundle()) {
//                Intent intent = new Intent(getActivity(), WorkorderBundleDetailActivity.class);
//                intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
//                intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, workorder.getBundleId());
//                getActivity().startActivity(intent);
//                view.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
//            } else {
            Intent intent = new Intent(getActivity(), WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
/*
                if (workorder.getStatus().getWorkorderStatus() == WorkorderStatus.INPROGRESS || workorder.getStatus().getWorkorderStatus() == WorkorderStatus.ASSIGNED) {
                    intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_TASKS);
                } else {
*/
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
//                }
            getActivity().startActivity(intent);
            view.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
//            }
        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: onViewPayments()
            if (workorder.getPaymentId() != null) {
                Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
                intent.putExtra(PaymentDetailActivity.INTENT_KEY_PAYMENT_ID, workorder.getPaymentId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), PaymentListActivity.class);
                startActivity(intent);
            }
        }
    };

    /*-*****************************************-*/
    /*-				Events Dialogs				-*/
    /*-*****************************************-*/
    private AcceptBundleDialog.Listener _acceptBundleDialog_listener = new AcceptBundleDialog.Listener() {
        @Override
        public void onOk(Workorder workorder) {
            _expiresDialog.show(workorder);
        }
    };

    private DeviceCountDialog.Listener _deviceCountDialog_listener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            startCheckOut();
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
            GaTopic.dispatchEvent(getActivity(), getGaLabel(), GaTopic.ACTION_REQUEST_WORK, "WorkorderCardView", 1);
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
                GaTopic.dispatchEvent(getActivity(), getGaLabel(), GaTopic.ACTION_CONFIRM_ASSIGN, "WorkorderCardView", 1);
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
        public void onOk(Workorder workorder, String reason, boolean expires, int expirationInSeconds, Pay pay, Schedule schedule, Expense[] expenses) {
            GaTopic.dispatchEvent(getActivity(), getGaLabel(), GaTopic.ACTION_COUNTER, "WorkorderCardView", 1);
            getActivity().startService(
                    _service.setCounterOffer(WEB_CHANGING_WORKORDER,
                            workorder.getWorkorderId(), expires, reason, expirationInSeconds, pay,
                            schedule, expenses));
            _requestWorking.add(workorder.getWorkorderId());
            setLoading(true);
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
//            Log.v(TAG, "getView()");
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
            setLoading(false);
            _requestWorking.clear();
            _pendingNotInterested.clear();
        }
    };


    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/
    private AuthTopicReceiver _topicReceiver = new AuthTopicReceiver(new Handler()) {

        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            Log.v(TAG, "onAuthentication");
            if (_service == null || isNew) {
                _username = username;
                _authToken = authToken;
                if (getActivity() != null) {
                    _service = new WorkorderService(getActivity(), username, authToken, _resultReciever);
                    requestList(0, true);
                }
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _service = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _service = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(getActivity());
        }
    };

    private class WorkorderParseAsync extends AsyncTaskEx<Bundle, Object, List<Workorder>> {
        private int page;
        private boolean cached;

        @Override
        protected List<Workorder> doInBackground(Bundle... params) {
            Bundle resultData = params[0];

            page = resultData.getInt(KEY_PAGE_NUM);
            String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
            cached = resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED);

            JsonArray objects = null;
            try {
                objects = new JsonArray(data);
            } catch (Exception ex) {
                ex.printStackTrace();
                if (cached)
                    requestList(page, false);
                return null;
            }

            List<Workorder> list = new LinkedList<Workorder>();
            for (int i = 0; i < objects.size(); i++) {
                try {
                    list.add(Workorder.fromJson(objects.getJsonObject(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Workorder> workorders) {
            super.onPostExecute(workorders);
            if (workorders != null)
                addPage(page, workorders, cached);
            setLoading(false);
        }
    }

    private WebResultReceiver _resultReciever = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_GET_LIST) {
                new WorkorderParseAsync().executeEx(resultData);

            } else if (resultCode == WEB_CHANGING_WORKORDER) {
                _adapter.refreshPages();

            } else if (resultCode == WEB_CHECKING_IN) {
                long woId = resultData.getLong(KEY_WORKORDER_ID);
                _requestWorking.remove(woId);
                _pendingNotInterested.remove(woId);
                Intent intent = new Intent(getActivity(), WorkorderActivity.class);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, woId);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
                getActivity().startActivity(intent);
                _adapter.refreshPages();
                setLoading(false);
            } else {
                setLoading(false);
            }

        }

        @Override
        public Context getContext() {
            return WorkorderListFragment.this.getActivity();
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            _service = null;
            AuthTopicService.requestAuthInvalid(getActivity());
            setLoading(false);
            //Toast.makeText(getActivity(), "Request failed please try again.", Toast.LENGTH_LONG).show();
        }
    };


}
