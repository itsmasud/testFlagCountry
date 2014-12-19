package com.fieldnation.ui.workorder.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.AcceptBundleDialog;
import com.fieldnation.ui.dialog.ClosingNotesDialog;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.DiscountDialog;
import com.fieldnation.ui.dialog.ExpenseDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.ISO8601;

import java.text.ParseException;

public class DetailFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.DetailFragment";

    private static final int WEB_CHANGE = 5;

    // UI
    private SummaryView _sumView;
    private LocationView _locView;
    private ScheduleSummaryView _scheduleView;
    private PaymentView _payView;
    private TaskSumView _taskView;
    private ActionView _actionView;
    private ActionBarTopView _topBar;
    private ExpectedPaymentView _exView;
    private TextView _bundleWarningTextView;
    private RefreshView _refreshView;
    private OverScrollView _scrollView;

    // Dialogs
    private ExpiresDialog _expiresDialog;
    private ConfirmDialog _confirmDialog;
    private ClosingNotesDialog _closingDialog;
    private AcceptBundleDialog _acceptBundleWOConfirmDialog;
    private AcceptBundleDialog _acceptBundleWOExpiresDialog;
    private DeviceCountDialog _deviceCountDialog;
    private ExpenseDialog _expenseDialog;
    private DiscountDialog _discountDialog;
    private CounterOfferDialog _counterOfferDialog;
    private TermsDialog _termsDialog;

    // Data
    private Workorder _workorder;
    private WorkorderService _service;

	/*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workorder_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _sumView = (SummaryView) view.findViewById(R.id.summary_view);
        _sumView.setListener(_summaryView_listener);

        _taskView = (TaskSumView) view.findViewById(R.id.tasksum_view);
        _taskView.setListener(_taskSum_listener);

        _locView = (LocationView) view.findViewById(R.id.location_view);
        _scheduleView = (ScheduleSummaryView) view
                .findViewById(R.id.schedule_view);

        _payView = (PaymentView) view.findViewById(R.id.payment_view);
        _payView.showDetails(true);
        _payView.setListener(_paymentView_listener);

        _actionView = (ActionView) view.findViewById(R.id.action_view);
        _actionView.setListener(_actionView_listener);

        _topBar = (ActionBarTopView) view.findViewById(R.id.actiontop_view);
        _topBar.setListener(_actionbartop_listener);

        _exView = (ExpectedPaymentView) view.findViewById(R.id.expected_pay_view);

        _bundleWarningTextView = (TextView) view.findViewById(R.id.bundlewarning2_textview);
        _bundleWarningTextView.setOnClickListener(_bundle_onClick);

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _scrollView = (OverScrollView) view.findViewById(R.id.scroll_view);
        _scrollView.setOnOverScrollListener(_refreshView);

        _closingDialog = ClosingNotesDialog.getInstance(getFragmentManager(), TAG);
        _closingDialog.setListener(_closingNotes_onOk);

        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog.setListener(_deviceCountListener);

        _acceptBundleWOConfirmDialog = AcceptBundleDialog.getInstance(getFragmentManager(), TAG + "._acceptBundleWOConfirmDialog");
        _acceptBundleWOConfirmDialog.setListener(_acceptBundleDialogConfirmListener);

        _acceptBundleWOExpiresDialog = AcceptBundleDialog.getInstance(getFragmentManager(), TAG + "._acceptBundleWOExpiresDialog");
        _acceptBundleWOExpiresDialog.setListener(_acceptBundleDialogExpiresListener);

        _expenseDialog = ExpenseDialog.getInstance(getFragmentManager(), TAG);
        _expenseDialog.setListener(_expenseDialog_listener);

        _counterOfferDialog = CounterOfferDialog.getInstance(getFragmentManager(), TAG);
        _counterOfferDialog.setListener(_counterOffer_listener);

        _confirmDialog = ConfirmDialog.getInstance(getFragmentManager(), TAG);
        _confirmDialog.setListener(_confirmListener);

        _discountDialog = DiscountDialog.getInstance(getFragmentManager(), TAG);
        _discountDialog.setListener(_discountDialog_listener);

        _expiresDialog = ExpiresDialog.getInstance(getFragmentManager(), TAG);
        _expiresDialog.setListener(_expiresDialog_listener);

        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);

        if (_workorder != null) {
            setWorkorder(_workorder, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AuthTopicService.subscribeAuthState(getActivity(), 0, TAG, _authReceiver);
    }

    @Override
    public void onPause() {
        TopicService.delete(getActivity(), TAG);
        super.onPause();
    }

    @Override
    public void update() {
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;

        if (_sumView != null) {
            _sumView.setWorkorder(_workorder, isCached);
        }

        if (_taskView != null) {
            _taskView.setWorkorder(_workorder, isCached);
        }

        if (_locView != null) {
            _locView.setWorkorder(_workorder, isCached);
        }

        if (_scheduleView != null) {
            _scheduleView.setWorkorder(_workorder, isCached);
        }

        if (_payView != null) {
            _payView.setWorkorder(_workorder, isCached);
        }

        if (_actionView != null) {
            _actionView.setWorkorder(_workorder, isCached);
        }

        if (_topBar != null) {
            _topBar.setWorkorder(_workorder, isCached);
        }

        if (_exView != null) {
            _exView.setWorkorder(_workorder, isCached);
        }

        if (workorder == null)
            return;


        if (!isCached)
            setLoading(false);

        if (_bundleWarningTextView != null) {
            if (_workorder.getBundleId() != null && _workorder.getBundleId() > 0) {
                _bundleWarningTextView.setVisibility(View.VISIBLE);
                _bundleWarningTextView.setText("This is part of a bundle of " + _workorder.getBundleCount() + " work orders.");
            } else {
                _bundleWarningTextView.setVisibility(View.GONE);
            }
        }
    }

    private void getData() {
        if (_workorder != null) {
            Log.v(TAG, "getData.startRefreshing");
            setLoading(true);
            _workorder.dispatchOnChange();

        }
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

    @Override
    public void doAction(Bundle bundle) {
        // TODO Method Stub: doAction()
        Log.v(TAG, "Method Stub: doAction()");

    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            getData();
        }
    };

    private SummaryView.Listener _summaryView_listener = new SummaryView.Listener() {
        @Override
        public void showConfidentialInfo(String body) {
            _termsDialog.show("Confidential Information", body);
        }

        @Override
        public void showCustomerPolicies(String body) {
            _termsDialog.show("Policies And Procedures", body);
        }
    };

    private View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), WorkorderBundleDetailActivity.class);
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, _workorder.getBundleId());
            getActivity().startActivity(intent);
        }
    };

    private TaskSumView.Listener _taskSum_listener = new TaskSumView.Listener() {
        @Override
        public void onShowTasksTab() {
            pageRequestListener.requestPage(TasksFragment.class, null);
        }
    };

    private ActionBarTopView.Listener _actionbartop_listener = new ActionBarTopView.Listener() {
        @Override
        public void onComplete() {
            getActivity().startService(
                    _service.complete(WEB_CHANGE, _workorder.getWorkorderId()));

        }

        @Override
        public void onCheckOut() {
            Pay pay = _workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(_workorder, pay.getMaxDevice());
            } else {
                getActivity().startService(
                        _service.checkout(WEB_CHANGE, _workorder.getWorkorderId()));

            }
        }

        @Override
        public void onCheckIn() {
            getActivity().startService(
                    _service.checkin(WEB_CHANGE, _workorder.getWorkorderId()));

        }

        @Override
        public void onAcknowledge() {
            getActivity().startService(
                    _service.acknowledgeHold(WEB_CHANGE,
                            _workorder.getWorkorderId()));

        }

        @Override
        public void onConfirm() {
            if (_workorder.isBundle()) {
                _acceptBundleWOConfirmDialog.show(_workorder);
            } else {
                _confirmDialog.show(_workorder, _workorder.getSchedule());
            }
        }

        @Override
        public void onEnterClosingNotes() {
            if (_workorder.canChangeClosingNotes()) {
                _closingDialog.show(_workorder.getClosingNotes());
            }
        }
    };

    private PaymentView.Listener _paymentView_listener = new PaymentView.Listener() {

        @Override
        public void onDeleteExpense(Workorder workorder,
                                    Expense expense) {
            getActivity().startService(_service.deleteExpense(WEB_CHANGE,
                    _workorder.getWorkorderId(),
                    expense.getExpenseId()));

        }

        @Override
        public void onShowAddDiscountDialog() {
            _discountDialog.show("Add Discount");
        }

        @Override
        public void onShowAddExpenseDialog() {
            _expenseDialog.show(true);
        }

        @Override
        public void onShowCounterOfferDialog() {
            _counterOfferDialog.show(_workorder);
        }

        @Override
        public void onShowTerms() {
            _termsDialog.show();
        }

        @Override
        public void onDeleteDiscount(Workorder workorder, int discountId) {
            getActivity().startService(
                    _service.deleteDiscount(WEB_CHANGE,
                            _workorder.getWorkorderId(), discountId));

        }
    };

    private ActionView.Listener _actionView_listener = new ActionView.Listener() {

        @Override
        public void onRequest(Workorder workorder) {
            if (workorder.isBundle()) {
                _acceptBundleWOExpiresDialog.show(workorder);
            } else {
                _expiresDialog.show(workorder);
            }
        }

        @Override
        public void onShowCounterOfferDialog(Workorder workorder) {
            _counterOfferDialog.show(workorder);
        }

        @Override
        public void onNotInterested(Workorder workorder) {
            getActivity().startService(
                    _service.decline(WEB_CHANGE, _workorder.getWorkorderId()));

        }

        @Override
        public void onConfirmAssignment(Workorder workorder) {
            if (workorder.isBundle()) {
                _acceptBundleWOConfirmDialog.show(workorder);
            } else {
                _confirmDialog.show(_workorder, workorder.getSchedule());
            }
        }

        @Override
        public void onComplete(Workorder workorder) {
            getActivity().startService(
                    _service.complete(WEB_CHANGE, workorder.getWorkorderId()));

        }
    };

    /*-*****************************************-*/
    /*-				Dialog Events				-*/
    /*-*****************************************-*/
    private ExpenseDialog.Listener _expenseDialog_listener = new ExpenseDialog.Listener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            getActivity().startService(
                    _service.addExpense(WEB_CHANGE, _workorder.getWorkorderId(),
                            description, amount, category));

        }

        @Override
        public void onCancel() {
        }
    };

    private CounterOfferDialog.Listener _counterOffer_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires, int expirationInSeconds,
                         Pay pay, Schedule schedule, Expense[] expenses) {
            getActivity().startService(
                    _service.setCounterOffer(WEB_CHANGE, workorder.getWorkorderId(), expires, reason,
                            expirationInSeconds, pay, schedule, expenses));

        }
    };

    private DiscountDialog.Listener _discountDialog_listener = new DiscountDialog.Listener() {
        @Override
        public void onOk(String description, double amount) {
            getActivity().startService(
                    _service.addDiscount(WEB_CHANGE,
                            _workorder.getWorkorderId(), amount, description));

        }

        @Override
        public void onCancel() {
        }
    };


    private ClosingNotesDialog.Listener _closingNotes_onOk = new ClosingNotesDialog.Listener() {
        @Override
        public void onOk(String message) {
            getActivity().startService(_service.closingNotes(WEB_CHANGE, _workorder.getWorkorderId(), message));

        }

        @Override
        public void onCancel() {
        }
    };


    private DeviceCountDialog.Listener _deviceCountListener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            getActivity().startService(
                    _service.checkout(WEB_CHANGE, _workorder.getWorkorderId(), count));

        }
    };

    private ConfirmDialog.Listener _confirmListener = new ConfirmDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            try {
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                getActivity().startService(_service.confirmAssignment(WEB_CHANGE,
                        _workorder.getWorkorderId(), startDate, ISO8601.fromUTC(end)));

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

    private AcceptBundleDialog.Listener _acceptBundleDialogConfirmListener = new AcceptBundleDialog.Listener() {

        @Override
        public void onOk(Workorder workorder) {
            _confirmDialog.show(_workorder, workorder.getSchedule());
        }
    };
    private AcceptBundleDialog.Listener _acceptBundleDialogExpiresListener = new AcceptBundleDialog.Listener() {
        @Override
        public void onOk(Workorder workorder) {
            _expiresDialog.show(workorder);
        }
    };

    private ExpiresDialog.Listener _expiresDialog_listener = new ExpiresDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String dateTime) {
            try {
                long seconds;
                seconds = (ISO8601.toUtc(dateTime) - System.currentTimeMillis()) / 1000;
                getActivity().startService(
                        _service.request(WEB_CHANGE,
                                _workorder.getWorkorderId(), seconds));

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };


    // Web
    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onNoNetwork() {
            _service = null;
        }

        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_service == null || isNew)
                _service = new WorkorderService(getActivity(), username, authToken, _resultReceiver);
        }

        @Override
        public void onAuthenticationFailed() {
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

    private WebResultReceiver _resultReceiver = new WebResultReceiver(
            new Handler()) {

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_CHANGE) {
                getData();
            } else {

            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            _service = null;
            AuthTopicService.requestAuthInvalid(getActivity());
            Toast.makeText(getActivity(), "Could not complete request", Toast.LENGTH_LONG).show();
        }
    };
}
