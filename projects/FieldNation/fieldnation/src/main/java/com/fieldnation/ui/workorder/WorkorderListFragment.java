package com.fieldnation.ui.workorder;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.GpsLocationService;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.EmptyWoListView;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.PagingAdapter;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.TabActionBarFragmentActivity;
import com.fieldnation.ui.dialog.AcceptBundleDialog;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.dialog.LocationDialog;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.payment.PaymentDetailActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.ParseException;
import java.util.List;

public class WorkorderListFragment extends Fragment implements TabActionBarFragmentActivity.TabFragment {
    private static final String TAG_BASE = "WorkorderListFragment";
    private String TAG = TAG_BASE;

    // State
    private static final String STATE_DISPLAY = TAG_BASE + ".STATE_DISPLAY";
    private static final String STATE_CURRENT_WORKORDER = TAG_BASE + ".STATE_CURRENT_WORKORDER";
    private static final String STATE_DEVICE_COUNT = TAG_BASE + ".STATE_DEVICE_COUNT";
    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    private static final int RESULT_CODE_ENABLE_GPS_CHECKIN = 1;
    private static final int RESULT_CODE_ENABLE_GPS_CHECKOUT = 2;

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
    private TwoButtonDialog _yesNoDialog;

    // Data
    private WorkorderClient _workorderClient;
    private GpsLocationService _gpsLocationService;

    // state data
    private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;
    private Workorder _currentWorkorder;
    private int _deviceCount = -1;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TAG)) {
                TAG = savedInstanceState.getString(STATE_TAG);
            } else {
                TAG = UniqueTag.makeTag(TAG_BASE);
            }

            if (savedInstanceState.containsKey(STATE_DISPLAY)) {
                Log.v(TAG, "Restoring state");
                _displayView = WorkorderDataSelector.fromName(savedInstanceState.getString(STATE_DISPLAY));
            }

            if (savedInstanceState.containsKey(STATE_CURRENT_WORKORDER))
                _currentWorkorder = savedInstanceState.getParcelable(STATE_CURRENT_WORKORDER);
        }

        if (TAG_BASE.equals(TAG)) {
            TAG = UniqueTag.makeTag(TAG_BASE);
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

        _adapter.setOnLoadingCompleteListener(_adapterListener);

        _listView = (OverScrollListView) view.findViewById(R.id.workorders_listview);
        _listView.setDivider(null);
        _listView.setOnOverScrollListener(_loadingView);
        _listView.setAdapter(_adapter);

        _emptyView = (EmptyWoListView) view.findViewById(R.id.empty_view);

        _acceptBundleDialog = AcceptBundleDialog.getInstance(getFragmentManager(), TAG);
        _confirmDialog = ConfirmDialog.getInstance(getFragmentManager(), TAG);
        _counterOfferDialog = CounterOfferDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _expiresDialog = ExpiresDialog.getInstance(getFragmentManager(), TAG);
        _locationDialog = LocationDialog.getInstance(getFragmentManager(), TAG);
        _locationLoadingDialog = OneButtonDialog.getInstance(getFragmentManager(), TAG);
        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);
        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);

        Log.v(TAG, "Display Type: " + _displayView.getCall());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_DISPLAY)) {
                Log.v(TAG, "Restoring state");
                _displayView = WorkorderDataSelector.fromName(savedInstanceState.getString(STATE_DISPLAY));
            }

            if (savedInstanceState.containsKey(STATE_DEVICE_COUNT)) {
                _deviceCount = savedInstanceState.getInt(STATE_DEVICE_COUNT);
            }
        }
        Log.v(TAG, "onActivityCreated: " + WorkorderListFragment.this.toString() + "/" + _displayView.getCall());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putString(STATE_DISPLAY, _displayView.name());
        outState.putString(STATE_TAG, TAG);

        if (_currentWorkorder != null)
            outState.putParcelable(STATE_CURRENT_WORKORDER, _currentWorkorder);

        if (_deviceCount > -1) {
            outState.putInt(STATE_DEVICE_COUNT, _deviceCount);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
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

        if (_workorderClient != null && _workorderClient.isConnected())
            _adapter.refreshPages();

        setLoading(true);

        GoogleAnalyticsTopicClient.dispatchScreenView(App.get(), getGaLabel());

        _gpsLocationService = new GpsLocationService(getActivity());

        _locationLoadingDialog.setData(getString(R.string.dialog_location_loading_title),
                getString(R.string.dialog_location_loading_body),
                getString(R.string.dialog_location_loading_button),
                _locationLoadingDialog_listener);

        _expiresDialog.setListener(_expiresDialog_listener);
        _confirmDialog.setListener(_confirmDialog_listener);
        _deviceCountDialog.setListener(_deviceCountDialog_listener);
        _counterOfferDialog.setListener(_counterOfferDialog_listener);
        _acceptBundleDialog.setListener(_acceptBundleDialog_listener);

        requestList(0, true);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause()");

        if (_gpsLocationService != null)
            _gpsLocationService.stopLocationUpdates();

        if (_locationLoadingDialog != null && _locationLoadingDialog.isVisible()) {
            Toast.makeText(getActivity(), "Aborted", Toast.LENGTH_LONG).show();
            _locationLoadingDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            _displayView = WorkorderDataSelector.fromName(bundle.getString(STATE_DISPLAY));
        }

        _workorderClient = new WorkorderClient(_workorderData_listener);
        _workorderClient.connect(App.get());
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach()");

        _workorderClient.disconnect(App.get());

        super.onDetach();
    }

    @Override
    public void isShowing() {
        Log.v(TAG, "isShowing");
        GoogleAnalyticsTopicClient.dispatchScreenView(App.get(), getGaLabel());
    }

    private void setLoading(boolean loading) {
        Log.v(TAG, "setLoading()");
        // misc.printStackTrace("setLoading(" + loading + ")");
        if (_loadingView != null) {
            if (loading) {
                _loadingView.startRefreshing();
            } else {
                _loadingView.refreshComplete();
            }
        }
    }

    public void update() {
        Log.v(TAG, "update()");
        _adapter.refreshPages();
    }

    private void requestList(int page, boolean allowCache) {
        Log.v(TAG, "requestList " + page);
        if (page == 0)
            setLoading(true);
        WorkorderClient.list(App.get(), _displayView, page, false, allowCache);
    }

    private void addPage(int page, List<Workorder> list) {
        Log.v(TAG, "addPage: page:" + page + " view:" + _displayView.getCall());
        if (page == 0 && list != null) {
            if (list.size() == 0 && _displayView.shouldShowGoToMarketplace()) {
                _emptyView.setVisibility(View.VISIBLE);
            } else {
                _emptyView.setVisibility(View.GONE);
            }
        } else {
            _emptyView.setVisibility(View.GONE);
        }

        if (list != null && list.size() == 0) {
            _adapter.setNoMorePages();
        }
        _adapter.setPage(page, list);
    }

    private void startCheckin() {
        Log.v(TAG, "startCheckin");

        if (_gpsLocationService != null)
        _gpsLocationService.setListener(_gps_checkInListener);

        if (!_gpsLocationService.isLocationServicesEnabled()) {
            _locationDialog.show(_currentWorkorder.getIsGpsRequired(),
                    _locationDialog_checkInListener);
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
        if (_gpsLocationService != null)
            _gpsLocationService.setListener(_gps_checkOutListener);
        if (!_gpsLocationService.isLocationServicesEnabled()) {
            _locationDialog.show(_currentWorkorder.getIsGpsRequired(),
                    _locationDialog_checkOutListener);
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
        Log.v(TAG, "doCheckin()");
        if (_gpsLocationService != null)
        _gpsLocationService.setListener(null);
        setLoading(true);
        _adapter.notifyDataSetChanged();
        GoogleAnalyticsTopicClient.dispatchEvent(App.get(), getGaLabel(), GoogleAnalyticsTopicClient.EventAction.CHECKIN, "WorkorderCardView", 1);
        if (_gpsLocationService.hasLocation()) {
            WorkorderClient.actionCheckin(App.get(), _currentWorkorder.getWorkorderId(), _gpsLocationService.getLocation());
        } else {
            WorkorderClient.actionCheckin(App.get(), _currentWorkorder.getWorkorderId());
        }
//        _adapter.refreshPages();
    }

    private void doCheckOut() {
        Log.v(TAG, "doCheckOut()");
        setLoading(true);

        if (_gpsLocationService != null)
        _gpsLocationService.setListener(null);

        _adapter.notifyDataSetChanged();
        GoogleAnalyticsTopicClient.dispatchEvent(
                App.get(),
                getGaLabel(),
                GoogleAnalyticsTopicClient.EventAction.CHECKOUT,
                "WorkorderCardView", 1);

        if (_gpsLocationService.hasLocation()) {
            if (_deviceCount > -1) {
                WorkorderClient.actionCheckout(App.get(),
                        _currentWorkorder.getWorkorderId(),
                        _deviceCount,
                        _gpsLocationService.getLocation());
            } else {
                WorkorderClient.actionCheckout(App.get(),
                        _currentWorkorder.getWorkorderId(),
                        _gpsLocationService.getLocation());
            }

        } else {
            if (_deviceCount > -1) {
                WorkorderClient.actionCheckout(App.get(), _currentWorkorder.getWorkorderId(), _deviceCount);
            } else {
                WorkorderClient.actionCheckout(App.get(), _currentWorkorder.getWorkorderId());
            }
//            _adapter.refreshPages();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_CODE_ENABLE_GPS_CHECKIN) {
                startCheckin();
            } else if (requestCode == RESULT_CODE_ENABLE_GPS_CHECKOUT) {
                startCheckOut();
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*************************************************-*/
    /*-				Events Workorder Card				-*/
    /*-*************************************************-*/
    private final OneButtonDialog.Listener _locationLoadingDialog_listener = new OneButtonDialog.Listener() {
        @Override
        public void onButtonClick() {
            Log.v(TAG, "_locationLoadingDialog_listener.onButtonClick()");
            _gpsLocationService.stopLocationUpdates();
            setLoading(false);
        }

        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    private final GpsLocationService.Listener _gps_checkInListener = new GpsLocationService.Listener() {
        @Override
        public void onLocation(Location location) {
            Log.v(TAG, "_gps_checkInListener.onLocation");
            startCheckin();
            if (_locationLoadingDialog != null && _locationLoadingDialog.isVisible()) {
                _locationLoadingDialog.dismiss();
            }
        }
    };

    private final GpsLocationService.Listener _gps_checkOutListener = new GpsLocationService.Listener() {
        @Override
        public void onLocation(Location location) {
            Log.v(TAG, "_gps_checkOutListener.onLocation");
            startCheckOut();
            if (_locationLoadingDialog != null && _locationLoadingDialog.isVisible()) {
                _locationLoadingDialog.dismiss();
            }
        }
    };

    private final LocationDialog.Listener _locationDialog_checkInListener = new LocationDialog.Listener() {
        @Override
        public void onOk() {
            try {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, RESULT_CODE_ENABLE_GPS_CHECKIN);
            } catch (Exception ex) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onOk();
                    }
                }, 500);
            }
        }

        @Override
        public void onNotNow() {
            try {
                doCheckin();
                setLoading(false);
            } catch (Exception ex) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onNotNow();
                    }
                }, 500);
            }
        }

        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    private final LocationDialog.Listener _locationDialog_checkOutListener = new LocationDialog.Listener() {
        @Override
        public void onOk() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, RESULT_CODE_ENABLE_GPS_CHECKOUT);
        }

        @Override
        public void onNotNow() {
            doCheckOut();
            setLoading(false);
        }

        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    private final WorkorderCardView.Listener _wocv_listener = new WorkorderCardView.Listener() {
        @Override
        public void actionRequest(WorkorderCardView view, Workorder workorder) {
            if (workorder.isBundle()) {
                _acceptBundleDialog.show(workorder);
            } else {
                _expiresDialog.show(workorder);
            }
        }

        @Override
        public void actionWithdrawRequest(WorkorderCardView view, final Workorder workorder) {
            _yesNoDialog.setData(getString(R.string.dialog_withdraw_title),
                    getString(R.string.dialog_withdraw_body), getString(R.string.btn_yes),
                    getString(R.string.btn_no), new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            GoogleAnalyticsTopicClient.dispatchEvent(App.get(), getGaLabel(),
                                    GoogleAnalyticsTopicClient.EventAction.WITHDRAW_REQUEST,
                                    "WorkorderCardView", 1);
                            WorkorderClient.actionWithdrawRequest(App.get(), workorder.getWorkorderId());
                        }

                        @Override
                        public void onNegative() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            _yesNoDialog.show();
        }

        @Override
        public void actionCheckout(WorkorderCardView view, Workorder workorder) {
            _currentWorkorder = workorder;
            Pay pay = workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(workorder, pay.getMaxDevice());
            } else {
                startCheckOut();
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
            GoogleAnalyticsTopicClient.dispatchEvent(App.get(), getGaLabel(),
                    GoogleAnalyticsTopicClient.EventAction.ACK_HOLD, "WorkorderCardView", 1);
            WorkorderClient.actionAcknowledgeHold(App.get(), workorder.getWorkorderId());
            _adapter.refreshPages();
        }

        @Override
        public void viewCounter(WorkorderCardView view, Workorder workorder) {
            _counterOfferDialog.show(workorder);
        }

        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
            setLoading(true);
            Intent intent = new Intent(getActivity(), WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
//            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER, workorder);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
            getActivity().startActivity(intent);
            view.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {
            GoogleAnalyticsTopicClient.dispatchEvent(App.get(), getGaLabel(),
                    GoogleAnalyticsTopicClient.EventAction.VIEW_PAY, "WorkorderCardView", 1);

            if (workorder.getPaymentId() != null) {
                Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
                intent.putExtra(PaymentDetailActivity.INTENT_KEY_PAYMENT_ID, workorder.getPaymentId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), PaymentListActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void actionReadyToGo(WorkorderCardView view, Workorder workorder) {
            GoogleAnalyticsTopicClient.dispatchEvent(App.get(), getGaLabel(),
                    GoogleAnalyticsTopicClient.EventAction.READY_TO_GO, "WorkorderCardView", 1);

            WorkorderClient.actionReadyToGo(App.get(), workorder.getWorkorderId());
        }
    };

    /*-*****************************************-*/
    /*-				Events Dialogs				-*/
    /*-*****************************************-*/
    private final AcceptBundleDialog.Listener _acceptBundleDialog_listener = new AcceptBundleDialog.Listener() {
        @Override
        public void onOk(Workorder workorder) {
            _expiresDialog.show(workorder);
        }
    };

    private final DeviceCountDialog.Listener _deviceCountDialog_listener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            Log.v(TAG, "_deviceCountDialog_listener");
            _currentWorkorder = workorder;
            _deviceCount = count;
            _adapter.notifyDataSetChanged();
            setLoading(true);
            startCheckOut();
        }

        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    private final ExpiresDialog.Listener _expiresDialog_listener = new ExpiresDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String dateTime) {
            long time = -1;
            if (dateTime != null) {
                try {
                    time = (ISO8601.toUtc(dateTime) - System.currentTimeMillis()) / 1000;
                } catch (ParseException e) {
                    Log.v(TAG, e);
                }
            }
            // request the workorder
            GoogleAnalyticsTopicClient.dispatchEvent(App.get(), getGaLabel(),
                    GoogleAnalyticsTopicClient.EventAction.REQUEST_WORK, "WorkorderCardView", 1);
            WorkorderClient.actionRequest(App.get(), workorder.getWorkorderId(), time);

            // notify the UI
            _adapter.refreshPages();
        }
    };

    private final ConfirmDialog.Listener _confirmDialog_listener = new ConfirmDialog.Listener() {
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            //set  loading mode
            try {
                GoogleAnalyticsTopicClient.dispatchEvent(App.get(), getGaLabel(), GoogleAnalyticsTopicClient.EventAction.CONFIRM_ASSIGN, "WorkorderCardView", 1);
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                WorkorderClient.actionConfirmAssignment(App.get(),
                        workorder.getWorkorderId(), startDate, ISO8601.fromUTC(end));
                _adapter.refreshPages();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onCancel(Workorder workorder) {
        }

        @Override
        public void termsOnClick(Workorder workorder) {
            _termsDialog.show(getString(R.string.dialog_terms_title), getString(R.string.dialog_terms_body));
        }

    };

    private final CounterOfferDialog.Listener _counterOfferDialog_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires,
                         int expirationInSeconds, Pay pay, Schedule schedule, Expense[] expenses) {

            GoogleAnalyticsTopicClient.dispatchEvent(App.get(), getGaLabel(),
                    GoogleAnalyticsTopicClient.EventAction.COUNTER, "WorkorderCardView", 1);

            WorkorderClient.actionCounterOffer(App.get(), workorder.getWorkorderId(),
                    expires, reason, expirationInSeconds, pay, schedule, expenses);

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

    private final RefreshView.Listener _refreshViewListener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            Log.v(TAG, "_refreshViewListener.onStartRefresh()");
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
        public void onList(List<Workorder> list, WorkorderDataSelector selector, int page, boolean failed, boolean isCached) {
            Log.v(TAG, "_workorderData_listener.onList, " + selector + ", " + page + ", " + failed + ", " + isCached);
            if (!selector.equals(_displayView))
                return;

            addPage(page, list); // done
        }

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            _adapter.refreshPages();
        }
    };
}

