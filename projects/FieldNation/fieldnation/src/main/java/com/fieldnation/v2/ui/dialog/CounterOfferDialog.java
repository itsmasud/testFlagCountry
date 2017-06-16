package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.PaymentCoView;
import com.fieldnation.ui.ReasonCoView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.ScheduleCoView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.ui.ExpensesCoCardView;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends FullScreenDialog {
    private static final String TAG = "CounterOfferDialog";

    // State
    private static final String STATE_EXPENSES = "STATE_EXPENSES";
    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private RefreshView _refreshView;

    private PaymentCoView _paymentView;
    private ScheduleCoView _scheduleView;
    private LinearLayout _expensesList;
    private ReasonCoView _reasonView;
    private TextView _termsWarningTextView;
    private IconFontTextView _addExpense;

    // Data
    private final List<ExpensesCoCardView> _expensesViews = new LinkedList<>();
    private WorkOrder _workOrder;
    private WorkordersWebApi _workOrderApi;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public CounterOfferDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_counter_offer, container, false);


        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle(App.get().getString(R.string.counter_offer));

        _finishMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setTitle(App.get().getString(R.string.btn_submit));

        _paymentView = (PaymentCoView) v.findViewById(R.id.payment_view);
        _scheduleView = (ScheduleCoView) v.findViewById(R.id.schedule_view);

        _expensesList = (LinearLayout) v.findViewById(R.id.expenses_list);
        _addExpense = (IconFontTextView) v.findViewById(R.id.add_expense);

        _reasonView = (ReasonCoView) v.findViewById(R.id.reasons_view);

        _termsWarningTextView = (TextView) v.findViewById(R.id.termswarning_textview);

        _refreshView = (RefreshView) v.findViewById(R.id.refresh_view);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _addExpense.setOnClickListener(_actionButton_onClick);

        _termsWarningTextView.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString spanned = new SpannableString("By countering this work order, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
        spanned.setSpan(_standardTerms_onClick, 91, 131, spanned.getSpanFlags(_standardTerms_onClick));
        spanned.setSpan(_pqap_onClick, 140, 173, spanned.getSpanFlags(_pqap_onClick));
        _termsWarningTextView.setText(spanned);

        _workOrderApi = new WorkordersWebApi(_workOrdersWebApi_listener);
        _workOrderApi.connect(App.get());
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        Log.v(TAG, "show");
        _workOrder = payload.getParcelable("workOrder");
        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onCreate");

        if (savedState.containsKey(STATE_EXPENSES)) {
            Parcelable[] parc = savedState.getParcelableArray(STATE_EXPENSES);
            _expensesViews.clear();
            for (Parcelable aParc : parc) {
                ExpensesCoCardView v = new ExpensesCoCardView(getContext());
                v.setExpense((Expense) aParc);
                _expensesViews.add(v);
            }
        }

        populateUi();
    }

    @Override
    public void onStop() {
        if (_workOrderApi != null) _workOrderApi.disconnect(App.get());
        super.onStop();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");

        if (_expensesViews != null && _expensesViews.size() > 0) {
            Expense[] exs = new Expense[_expensesViews.size()];

            for (int i = 0; i < _expensesViews.size(); i++) {
                exs[i] = _expensesViews.get(i).getExpense();
            }

            outState.putParcelableArray(STATE_EXPENSES, exs);

        }
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_paymentView == null)
            return;

        if (_scheduleView == null)
            return;

        _expensesList.removeAllViews();

        ForLoopRunnable r = new ForLoopRunnable(_expensesViews.size(), new Handler()) {

            @Override
            public void next(int i) throws Exception {
                ExpensesCoCardView v = null;
                if (i < _expensesList.getChildCount()) {
                    v = (ExpensesCoCardView) _expensesList.getChildAt(i);
                } else {
                    v = _expensesViews.get(i);
                    _expensesList.addView(v);
                }
            }
        };
        _expensesList.postDelayed(r, new Random().nextInt(100));

    }


    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            _refreshView.startRefreshing();

            Expense[] exp = new Expense[_expensesViews.size()];
            for (int i = 0; i < _expensesViews.size(); i++) {
                if (_expensesViews.get(i).isValidExpense())
                    exp[i] = _expensesViews.get(i).getExpense();
                else {
                    ToastClient.toast(App.get(), R.string.toast_must_enter_amount_and_description, Toast.LENGTH_LONG);
                    _refreshView.refreshComplete();
                    return false;
                }
            }

            try {
                Request request = new Request();
                request.counter(true);

                if (_paymentView.getPay() != null && _paymentView.isValidPay()) {
                    if (!_paymentView.isValidAmount()) {
                        ToastClient.toast(App.get(), R.string.please_enter_a_value_atleast_one_dollar, Toast.LENGTH_SHORT);
                        _refreshView.refreshComplete();
                        return false;
                    }
                    if (!_paymentView.isValidTotalAmount()) {
                        ToastClient.toast(App.get(), App.get().getString(R.string.toast_minimum_accumulated_payable_amount), Toast.LENGTH_SHORT);
                        _refreshView.refreshComplete();
                        return false;
                    }
                    request.pay(_paymentView.getPay());
                }

                if (_scheduleView.getSchedule() != null)
                    request.schedule(_scheduleView.getSchedule());

                if (exp != null)
                    request.expenses(exp);

                if (_reasonView.getExpiryTime() > 0)
                    request.expires(new Date(_reasonView.getExpiryTime()));

                if (!misc.isEmptyOrNull(_reasonView.getReason()))
                    request.counterNotes(_reasonView.getReason());

                if (!_paymentView.isValidPay() && !_scheduleView.isValidSchedule() && !(exp != null && exp.length > 0)) {
                    ToastClient.toast(App.get(), App.get().getString(R.string.toast_empty_counter_offer), Toast.LENGTH_SHORT);
                    _refreshView.refreshComplete();
                    return false;
                }


                SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                uiContext.page += " - Counter Offer Dialog";
                WorkordersWebApi.request(App.get(), _workOrder.getId(), request, uiContext);

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }


            return true;
        }
    };

    private final ClickableSpan _standardTerms_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=workorder"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };

    private final ClickableSpan _pqap_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=qualityassurance"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };


    private final WorkordersWebApi.Listener _workOrdersWebApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderApi.subWorkordersWebApi();
        }

        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("request")
                    || methodName.equals("deleteRequest");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("request")) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (success) {
                    Expense[] exp = new Expense[_expensesViews.size()];
                    for (int i = 0; i < _expensesViews.size(); i++) {
                        exp[i] = _expensesViews.get(i).getExpense();
                    }

                    _onOkDispatcher.dispatch(getUid(), _workOrder, _reasonView.getReason(), _reasonView.getExpiryTime(), _paymentView.getPay(), _scheduleView.getSchedule(), exp);
                    dismiss(true);

                    _refreshView.refreshComplete();
                }
            }
        }
    };

    private final View.OnClickListener _actionButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ExpensesCoCardView view = new ExpensesCoCardView(getContext());
            _expensesViews.add(view);
            populateUi();
        }
    };


    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable("workOrder", workOrder);

        Controller.show(context, uid, CounterOfferDialog.class, params);
    }


    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(WorkOrder workorder, String reason, long expires, Pay pay, Schedule schedule, Expense[] expenses);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((WorkOrder) parameters[0], (String) parameters[1], (Long) parameters[2],
                    (Pay) parameters[3], (Schedule) parameters[4], (Expense[]) parameters[5]
            );
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }
}