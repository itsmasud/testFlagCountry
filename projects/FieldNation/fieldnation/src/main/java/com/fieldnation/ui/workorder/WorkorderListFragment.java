package com.fieldnation.ui.workorder;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fngps.GpsLocationService;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.LeavingActivity;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.PagingAdapter;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.TabActionBarFragmentActivity;
import com.fieldnation.ui.UnavailableCardView;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.LocationDialog;
import com.fieldnation.ui.dialog.MarkIncompleteDialog;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.v2.AcceptBundleDialog;
import com.fieldnation.ui.dialog.v2.CheckInOutDialog;
import com.fieldnation.ui.dialog.v2.EtaDialog;
import com.fieldnation.ui.dialog.v2.ReportProblemDialog;
import com.fieldnation.ui.payment.PaymentDetailActivity;
import com.fieldnation.ui.payment.PaymentListActivity;

import java.util.LinkedList;
import java.util.List;

public class WorkorderListFragment extends Fragment implements TabActionBarFragmentActivity.TabFragment {
    private static final String TAG_BASE = "WorkorderListFragment";
    private String TAG = TAG_BASE;

    // State
    private static final String STATE_DISPLAY = TAG_BASE + ".STATE_DISPLAY";
    private static final String STATE_CURRENT_WORKORDER = TAG_BASE + ".STATE_CURRENT_WORKORDER";
    private static final String STATE_DEVICE_COUNT = TAG_BASE + ".STATE_DEVICE_COUNT";
    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // Dialog tags
    private static final String DIALOG_CHECK_IN_CHECK_OUT = "DIALOG_CHECK_IN_CHECK_OUT";

    // UI
    private OverScrollListView _listView;
    private RefreshView _loadingView;
    private UnavailableCardView _emptyView;

    // Dialogs
    private EtaDialog _etaDialog;
    private DeviceCountDialog _deviceCountDialog;
    private CounterOfferDialog _counterOfferDialog;
    private TermsDialog _termsDialog;
    private LocationDialog _locationDialog;
    private OneButtonDialog _locationLoadingDialog;
    private TwoButtonDialog _yesNoDialog;
    private MarkIncompleteDialog _markIncompleteDialog;

    // Data
    private WorkorderClient _workorderClient;
    private GpsLocationService _gpsLocationService;
    private final List<Runnable> _onAdded = new LinkedList<>();

    // state data
    private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;
    private Workorder _currentWorkorder;
    private int _deviceCount = -1;


    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public WorkorderListFragment() {
        super();
        _adapter.setRateMeAllowed(true);
    }

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
                _displayView = WorkorderDataSelector.values()[savedInstanceState.getInt(STATE_DISPLAY)];
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
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    private GpsLocationService getLocationService() {
        if (_gpsLocationService == null) {
            _gpsLocationService = new GpsLocationService(App.get());
        }

        return _gpsLocationService;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");

        _loadingView = (RefreshView) view.findViewById(R.id.loading_view);
        _loadingView.setListener(_refreshViewListener);

        _adapter.setOnLoadingCompleteListener(_adapterListener);

        _listView = (OverScrollListView) view.findViewById(R.id.listview);
        _listView.setDivider(null);
        _listView.setOnOverScrollListener(_loadingView);
        _listView.setAdapter(_adapter);

        _emptyView = (UnavailableCardView) view.findViewById(R.id.empty_view);

        _counterOfferDialog = CounterOfferDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _locationDialog = LocationDialog.getInstance(getFragmentManager(), TAG);
        _locationLoadingDialog = OneButtonDialog.getInstance(getFragmentManager(), TAG);
        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);
        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);
        _markIncompleteDialog = MarkIncompleteDialog.getInstance(getFragmentManager(), TAG);

        Log.v(TAG, "Display Type: " + _displayView.getCall());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_DISPLAY)) {
                Log.v(TAG, "Restoring state");
                _displayView = WorkorderDataSelector.values()[savedInstanceState.getInt(STATE_DISPLAY)];
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
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putInt(STATE_DISPLAY, _displayView.ordinal());
        outState.putString(STATE_TAG, TAG);

        if (_currentWorkorder != null)
            outState.putParcelable(STATE_CURRENT_WORKORDER, _currentWorkorder);

        if (_deviceCount > -1) {
            outState.putInt(STATE_DEVICE_COUNT, _deviceCount);
        }

        super.onSaveInstanceState(outState);
    }


    public WorkorderListFragment setDisplayType(WorkorderDataSelector displayView) {
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

        _locationLoadingDialog.setData(getString(R.string.dialog_location_loading_title),
                getString(R.string.dialog_location_loading_body),
                getString(R.string.dialog_location_loading_button),
                _locationLoadingDialog_listener);

        _deviceCountDialog.setListener(_deviceCountDialog_listener);
        _counterOfferDialog.setListener(_counterOfferDialog_listener);
        _markIncompleteDialog.setListener(_markIncompleteDialog_listener);

        checkProfile();
    }

    private void checkProfile() {
        Profile profile = App.get().getProfile();
        if (profile == null) {
            Log.v(TAG, "profile is null");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkProfile();
                }
            }, 100);
        } else if (profile.getMarketplaceStatusOn()) {
            requestList(0, true);
        }
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause()");

        getLocationService().stopLocationUpdates();

        if (_locationLoadingDialog != null && _locationLoadingDialog.isVisible()) {
            ToastClient.toast(App.get(), R.string.aborted, Toast.LENGTH_LONG);
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
            _displayView = WorkorderDataSelector.values()[bundle.getInt(STATE_DISPLAY)];
        }

        _workorderClient = new WorkorderClient(_workorderData_listener);
        _workorderClient.connect(App.get());

        while (_onAdded.size() > 0) {
            _onAdded.remove(0).run();
        }
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

    public void addPage(int page, List<Workorder> list) {
        Log.v(TAG, "addPage: page:" + page + " view:" + _displayView.getCall());
        if (page == 0 && (list == null || list.size() == 0)) {
            _emptyView.setData(_displayView);
            _emptyView.setVisibility(View.VISIBLE);
        } else {
            _emptyView.setVisibility(View.GONE);
        }

        if (list != null && list.size() == 0) {
            _adapter.setNoMorePages();
        }
        _adapter.setPage(page, list);
    }

    public void setPageAtAdapter(int page, List<Workorder> list) {

        if (page == 0 && (list == null || list.size() == 0)) {
            _emptyView.setData(_displayView);
            _emptyView.setVisibility(View.VISIBLE);
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

        if (!isAdded()) {
            _onAdded.add(new Runnable() {
                @Override
                public void run() {
                    startCheckin();
                }
            });
            return;
        }
        getLocationService().setListener(_gps_checkInListener);
        if (!getLocationService().isLocationServicesEnabled()) {
            _locationDialog.show(_currentWorkorder.getIsGpsRequired(),
                    _locationDialog_checkInListener);
        } else if (getLocationService().hasLocation()) {
            doCheckin();
        } else if (getLocationService().isRunning()) {
            _locationLoadingDialog.show();
        } else if (getLocationService().isLocationServicesEnabled()) {
            _locationLoadingDialog.show();
            getLocationService().startLocation();
        } else {
            // location is disabled, or failed. ask for them to be enabled
            Log.v(TAG, "Should not be here");
        }
        setLoading(true);
    }

    private void startCheckOut() {
        Log.v(TAG, "startCheckOut");
        if (!isAdded()) {
            _onAdded.add(new Runnable() {
                @Override
                public void run() {
                    startCheckOut();
                }
            });
            return;
        }
        getLocationService().setListener(_gps_checkOutListener);
        if (!getLocationService().isLocationServicesEnabled()) {
            _locationDialog.show(_currentWorkorder.getIsGpsRequired(),
                    _locationDialog_checkOutListener);
        } else if (getLocationService().hasLocation()) {
            doCheckOut();
        } else if (getLocationService().isRunning() && _locationDialog.isAdded()) {
            _locationLoadingDialog.show();
        } else if (getLocationService().isLocationServicesEnabled()) {
            _locationLoadingDialog.show();
            getLocationService().startLocation();
        } else {
            // location is disabled, or failed. ask for them to be enabled
            Log.v(TAG, "Should not be here");
        }
        setLoading(true);
    }

    private void doCheckin() {
        Log.v(TAG, "doCheckin()");
        getLocationService().setListener(null);
        setLoading(true);
        _adapter.notifyDataSetChanged();
        if (getLocationService().hasLocation()) {
            CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _currentWorkorder.getWorkorderId(), getLocationService().getLocation(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
        } else {
            CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _currentWorkorder.getWorkorderId(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
        }
//        _adapter.refreshPages();
    }

    private void doCheckOut() {
        Log.v(TAG, "doCheckOut()");
        setLoading(true);

        getLocationService().setListener(null);

        _adapter.notifyDataSetChanged();

        if (getLocationService().hasLocation()) {
            if (_deviceCount > -1) {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _currentWorkorder.getWorkorderId(), getLocationService().getLocation(), _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _currentWorkorder.getWorkorderId(), getLocationService().getLocation(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            }

        } else {
            if (_deviceCount > -1) {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _currentWorkorder.getWorkorderId(), _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _currentWorkorder.getWorkorderId(), _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            }
//            _adapter.refreshPages();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKIN) {
                startCheckin();
            } else if (requestCode == ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKOUT) {
                startCheckOut();
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*************************************************-*/
    /*-				Events Workorder Card				-*/
    /*-*************************************************-*/
    private final MarkIncompleteDialog.Listener _markIncompleteDialog_listener = new MarkIncompleteDialog.Listener() {

        // TODO: I am not pretty sure about the following method
        @Override
        public void onContinueClick() {
            WorkorderClient.actionIncomplete(App.get(), _currentWorkorder.getWorkorderId());

            setLoading(true);
        }
    };

    private final OneButtonDialog.Listener _locationLoadingDialog_listener = new OneButtonDialog.Listener() {
        @Override
        public void onButtonClick() {
            Log.v(TAG, "_locationLoadingDialog_listener.onButtonClick()");
            getLocationService().stopLocationUpdates();
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
                startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKIN);
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
            startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKOUT);
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
                AcceptBundleDialog.Controller.show(App.get(), workorder.getBundleId(),
                        workorder.getBundleCount(), workorder.getWorkorderId(), AcceptBundleDialog.TYPE_REQUEST);
            } else {
/*
                EtaDialog.show(App.get(), workorder.getWorkorderId(),
                        workorder.getScheduleV2(), EtaDialog.PARAM_DIALOG_TYPE_REQUEST);
*/
            }
        }

        @Override
        public void actionWithdrawRequest(WorkorderCardView view, final Workorder workorder) {
            _yesNoDialog.setData(getString(R.string.dialog_withdraw_title),
                    getString(R.string.dialog_withdraw_body), getString(R.string.btn_yes),
                    getString(R.string.btn_no), new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
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
//            EtaDialog.show(App.get(), workorder.getWorkorderId(),
//                    workorder.getScheduleV2(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
        }

        @Override
        public void actionAcknowledgeHold(WorkorderCardView view, Workorder workorder) {
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
            WorkorderActivity.startNew(App.get(), workorder.getWorkorderId());
            view.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {
            if (workorder.getPaymentId() != null) {
                PaymentDetailActivity.startNew(App.get(), workorder.getPaymentId());
            } else {
                PaymentListActivity.startNew(App.get());
            }
        }

        @Override
        public void actionReadyToGo(WorkorderCardView view, Workorder workorder) {
            WorkorderClient.actionReadyToGo(App.get(), workorder.getWorkorderId());
        }

        @Override
        public void actionConfirm(WorkorderCardView view, Workorder workorder) {
            _currentWorkorder = workorder;
//            EtaDialog.show(App.get(), workorder.getWorkorderId(),
//                    workorder.getScheduleV2(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
        }

        @Override
        public void actionMap(WorkorderCardView view, Workorder workorder) {
            com.fieldnation.data.workorder.Location location = workorder.getLocation();
            if (location != null) {
                try {
                    String _fullAddress = misc.escapeForURL(location.getFullAddressOneLine());
                    String _uriString = "geo:0,0?q=" + _fullAddress;
                    Uri _uri = Uri.parse(_uriString);
                    Intent _intent = new Intent(Intent.ACTION_VIEW);
                    _intent.setData(_uri);
                    if (_intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        getActivity().startActivity(_intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void actionReportProblem(WorkorderCardView view, Workorder workorder) {
            _currentWorkorder = workorder;
//            ReportProblemDialog.Controller.show(App.get(), workorder);
        }

        @Override
        public void actionMarkIncomplete(WorkorderCardView view, Workorder workorder) {
            _currentWorkorder = workorder;
            _markIncompleteDialog.show(workorder);
        }

        @Override
        public void actionUpdatePaymentInfo(WorkorderCardView view, Workorder workorder) {
            LeavingActivity.start(getActivity(), R.string.update_your_payment_info, R.string.currently_to_edit_your, Uri.parse("https://app.fieldnation.com/"));
        }
    };

    /*-*****************************************-*/
    /*-				Events Dialogs				-*/
    /*-*****************************************-*/
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

    private final CounterOfferDialog.Listener _counterOfferDialog_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires,
                         int expirationInSeconds, Pay pay, Schedule schedule, Expense[] expenses) {
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

            if (getLocationService().getLocation() != null)
                v.setWorkorder(object, getLocationService().getLocation());
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

