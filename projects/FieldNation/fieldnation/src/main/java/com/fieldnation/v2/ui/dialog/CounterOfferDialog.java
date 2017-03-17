package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.ExpenseCategory;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends SimpleDialog {
    private static final String TAG = "CounterOfferDialog";

    // Dialogs
    private static final String DIALOG_EXPENSE = TAG + ".expenseDialog";
    private static final String DIALOG_PAY = TAG + ".payDialog";
    private static final String DIALOG_SCHEDULE = TAG + ".scheduleDialog";
    private static final String DIALOG_TERMS = TAG + ".termsDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_COUNTER_PAY = "STATE_COUNTER_PAY";
    private static final String STATE_EXPENSES = "STATE_EXPENSES";
    private static final String STATE_COUNTER_SCHEDULE = "STATE_COUNTER_SCHEDULE";
    private static final String STATE_COUNTER_REASON = "STATE_COUNTER_REASON";
    private static final String STATE_EXPIRES = "STATE_EXPIRES";
    private static final String STATE_TAC = "STATE_TAC";

    // Ui
    private TabHost _tabHost;
    private Button _backButton;
    private Button _okButton;
    private HorizontalScrollView _tabScrollView;

    private PaymentCoView _paymentView;
    private ScheduleCoView _scheduleView;
    private ExpenseCoView _expenseView;
    private ReasonCoView _reasonView;

    // Data State
    private final List<Expense> _expenses = new LinkedList<>();

    private WorkOrder _workOrder;
    private Pay _counterPay;
    private Schedule _counterSchedule;
    private String _counterReason;
    private long _expires = 0;

    // Data
    private boolean _tacAccpet;

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

        _tabHost = (TabHost) v.findViewById(R.id.tabhost);
        _tabHost.setup();

        TabHost.TabSpec tab1 = _tabHost.newTabSpec("start");
        tab1.setIndicator("Pay");
        tab1.setContent(R.id.payment_view);
        _tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = _tabHost.newTabSpec("mid1");
        tab2.setIndicator("Schedule");
        tab2.setContent(R.id.schedule_view);
        _tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = _tabHost.newTabSpec("mid2");
        tab3.setIndicator("Expense");
        tab3.setContent(R.id.expenses_view);
        _tabHost.addTab(tab3);

        TabHost.TabSpec tab4 = _tabHost.newTabSpec("end");
        tab4.setIndicator("Reason");
        tab4.setContent(R.id.reasons_view);
        _tabHost.addTab(tab4);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _backButton = (Button) v.findViewById(R.id.back_button);
        _backButton.setVisibility(View.GONE);
        _paymentView = (PaymentCoView) v.findViewById(R.id.payment_view);
        _scheduleView = (ScheduleCoView) v.findViewById(R.id.schedule_view);
        _expenseView = (ExpenseCoView) v.findViewById(R.id.expenses_view);
        _reasonView = (ReasonCoView) v.findViewById(R.id.reasons_view);
        _tabScrollView = (HorizontalScrollView) v.findViewById(R.id.tabscroll_view);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _okButton.setOnClickListener(_ok_onClick);
        _backButton.setOnClickListener(_back_onClick);
        _paymentView.setListener(_payment_listener);
        _scheduleView.setListener(_scheduleView_listener);
        _expenseView.setListener(_expenseView_listener);
        _reasonView.setListener(_reason_listener);

        _tabHost.setOnTabChangedListener(_tab_changeListener);

        for (int i = 0; i < 4; i++) {
            _tabHost.getTabWidget().getChildAt(i).setFocusableInTouchMode(true);
        }

        PayDialog.addOnCompleteListener(DIALOG_PAY, _payDialog_onComplete);
        ExpenseDialog.addOnOkListener(DIALOG_EXPENSE, _expenseDialog_onOk);
        ScheduleDialog.addOnCompleteListener(DIALOG_SCHEDULE, _scheduleDialog_onComplete);
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        populateUi();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        Log.v(TAG, "show");
        _workOrder = payload.getParcelable("workOrder");

        Request coRequest = null;

        if (_workOrder.getRequests() != null)
            coRequest = _workOrder.getRequests().getCounterOffer();

        _counterPay = null;
        _counterSchedule = null;
        _counterReason = null;
        _expires = 0;

        if (coRequest != null) {
            if (coRequest.getPay() != null) {
                _counterPay = coRequest.getPay();
            }
            if (coRequest.getSchedule() != null) {
                _counterSchedule = coRequest.getSchedule();
            }

            if (coRequest.getExpenses() != null && coRequest.getExpenses().length > 0) {
                _expenses.clear();
                Expense[] exp = coRequest.getExpenses();
                Collections.addAll(_expenses, exp);
            }

            _counterReason = coRequest.getCounterNotes();
            try {
                _expires = coRequest.getExpires() == null ? 0 : coRequest.getExpires().getUtcLong();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onCreate");
        if (savedState != null) {
            if (savedState.containsKey(STATE_COUNTER_PAY))
                _counterPay = savedState.getParcelable(STATE_COUNTER_PAY);

            if (savedState.containsKey(STATE_EXPENSES)) {
                Parcelable[] parc = savedState.getParcelableArray(STATE_EXPENSES);
                _expenses.clear();
                for (Parcelable aParc : parc) {
                    _expenses.add((Expense) aParc);
                }
            }

            if (savedState.containsKey(STATE_COUNTER_SCHEDULE))
                _counterSchedule = savedState.getParcelable(STATE_COUNTER_SCHEDULE);

            if (savedState.containsKey(STATE_COUNTER_REASON))
                _counterReason = savedState.getString(STATE_COUNTER_REASON);

            if (savedState.containsKey(STATE_EXPIRES))
                _expires = savedState.getLong(STATE_EXPIRES);

            if (savedState.containsKey(STATE_TAC))
                _tacAccpet = savedState.getBoolean(STATE_TAC);
        }
        populateUi();
    }

    @Override
    public void onStop() {
        super.onStop();

        PayDialog.removeOnCompleteListener(DIALOG_PAY, _payDialog_onComplete);
        ExpenseDialog.removeOnOkListener(DIALOG_EXPENSE, _expenseDialog_onOk);
        ScheduleDialog.removeOnCompleteListener(DIALOG_SCHEDULE, _scheduleDialog_onComplete);
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");
        outState.putLong(STATE_EXPIRES, _expires);
        outState.putBoolean(STATE_TAC, _tacAccpet);

        if (_counterPay != null)
            outState.putParcelable(STATE_COUNTER_PAY, _counterPay);

        if (_expenses != null && _expenses.size() > 0) {
            Expense[] exs = new Expense[_expenses.size()];

            for (int i = 0; i < _expenses.size(); i++) {
                exs[i] = _expenses.get(i);
            }

            outState.putParcelableArray(STATE_EXPENSES, exs);
        }

        if (_counterSchedule != null)
            outState.putParcelable(STATE_COUNTER_SCHEDULE, _counterSchedule);

        if (_reasonView != null) {
            outState.putString(STATE_COUNTER_REASON, _reasonView.getReason());
        }
    }

    private void populateUi() {
        Log.v(TAG, "populateUi maybe!");
        if (_workOrder == null)
            return;

        if (_paymentView == null)
            return;

        if (_scheduleView == null)
            return;

        Log.v(TAG, "populateUi yes!");

        if (_counterPay != null)
            _paymentView.setPay(_counterPay, true);
        else
            _paymentView.setPay(_workOrder.getPay(), false);

        if (_counterSchedule != null) {
            _scheduleView.setSchedule(_counterSchedule, true);
        } else {
            _scheduleView.setSchedule(_workOrder.getSchedule(), false);
        }

        _expenseView.setData(_workOrder, _expenses);

        _reasonView.setCounterOffer(_counterReason, _expires);
    }

    private void setTabPos(int pos) {
        Log.v(TAG, "setTabPos: " + pos);
        if (pos == 0) {
            _tabScrollView.post(new Runnable() {
                @Override
                public void run() {
                    _tabScrollView.fullScroll(View.FOCUS_LEFT);
                    _tabHost.setCurrentTab(0);
                }
            });
        } else if (pos > 0 && pos < 4) {
            _tabHost.setCurrentTab(pos);
        } else {
            _tabScrollView.post(new Runnable() {
                @Override
                public void run() {
                    _tabScrollView.fullScroll(View.FOCUS_RIGHT);
                    _tabHost.setCurrentTab(4);
                }
            });
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final ReasonCoView.Listener _reason_listener = new ReasonCoView.Listener() {
        @Override
        public void onTacClick() {
            TermsDialog.show(App.get(), DIALOG_TERMS, App.get().getString(R.string.dialog_terms_title),
                    App.get().getString(R.string.dialog_terms_body));
        }

        @Override
        public void onTacChange(boolean isChecked) {
            _tacAccpet = isChecked;
        }

        @Override
        public void onExpirationChange(long expires) {
            _expires = expires;
        }
    };

    private final ExpenseCoView.Listener _expenseView_listener = new ExpenseCoView.Listener() {
        @Override
        public void addExpense() {
            ExpenseDialog.show(App.get(), DIALOG_EXPENSE, false);
        }

        @Override
        public void removeExpense(Expense expense) {
            _expenses.remove(expense);
            populateUi();
        }

        @Override
        public void reset() {
            Request co = null;

            if (_workOrder.getRequests() != null)
                co = _workOrder.getRequests().getCounterOffer();

            _expenses.clear();
            if (co != null && co.getExpenses() != null && co.getExpenses().length > 0) {
                Expense[] exp = co.getExpenses();
                Collections.addAll(_expenses, exp);
            }
            populateUi();
        }

        @Override
        public void editExpense(Expense expense) {
            // TODO editExpense
        }
    };

    private final ExpenseDialog.OnOkListener _expenseDialog_onOk = new ExpenseDialog.OnOkListener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            try {
                _expenses.add(new Expense()
                        .description(description)
                        .amount(amount));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            populateUi();
        }
    };

    private final ScheduleCoView.Listener _scheduleView_listener = new ScheduleCoView.Listener() {
        @Override
        public void onClear() {
            _counterSchedule = null;
            populateUi();
        }

        @Override
        public void onChange(Schedule schedule) {
            ScheduleDialog.show(App.get(), DIALOG_SCHEDULE, schedule);
        }
    };

    private final ScheduleDialog.OnCompleteListener _scheduleDialog_onComplete = new ScheduleDialog.OnCompleteListener() {
        @Override
        public void onComplete(Schedule schedule) {
            _counterSchedule = schedule;
            populateUi();
        }
    };

    private final PaymentCoView.Listener _payment_listener = new PaymentCoView.Listener() {
        @Override
        public void onClearClick() {
            _counterPay = null;
            populateUi();
        }

        @Override
        public void onChangeClick(Pay pay) {
            PayDialog.show(App.get(), DIALOG_PAY, pay, false);
        }
    };

    private final PayDialog.OnCompleteListener _payDialog_onComplete = new PayDialog.OnCompleteListener() {
        @Override
        public void onComplete(Pay pay, String explanation) {
            _counterPay = pay;
            populateUi();
        }
    };

    private final TabHost.OnTabChangeListener _tab_changeListener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            _backButton.setVisibility(View.VISIBLE);
            _okButton.setText("NEXT");
            if (tabId.equals("start")) {
                _backButton.setVisibility(View.GONE);
            } else if (tabId.startsWith("mid")) {
            } else if (tabId.equals("end")) {
                _okButton.setText("FINISH");
            }
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // start?
            if (_tabHost.getCurrentTabTag().equals("start")) {
                setTabPos(_tabHost.getCurrentTab() + 1);
            } else if (_tabHost.getCurrentTabTag().startsWith("mid")) {
                setTabPos(_tabHost.getCurrentTab() + 1);
            } else if (_tabHost.getCurrentTabTag().equals("end")) {
                if (!_tacAccpet) {
                    ToastClient.toast(App.get(), "Please accept the terms and conditions to continue", Toast.LENGTH_LONG);
                    return;
                }

                _counterReason = _reasonView.getReason();

//                if (misc.isEmptyOrNull(_counterReason)) {
//                    Toast.makeText(getActivity(), "Counter offer reason cannot be null. Please enter a reason.", Toast.LENGTH_LONG).show();
//                    return;
//                }

                // Todo need to do some data validation
                Expense[] exp = new Expense[_expenses.size()];
                for (int i = 0; i < _expenses.size(); i++) {
                    exp[i] = _expenses.get(i);
                }

//                    int seconds = -1;
//                    if (_expires) {
//                        try {
//                            seconds = (int) (ISO8601.toUtc(_expirationDate)
//                                    - System.currentTimeMillis()) / 1000;
//                        } catch (Exception ex) {
//                            Log.v(TAG, ex);
//                        }
//                    }

                Log.e(TAG, "_expireDuration: " + _expires);

                _onOkDispatcher.dispatch(getUid(), _workOrder, _counterReason, _expires, _counterPay, _counterSchedule, exp);
                _tacAccpet = false;
                dismiss(true);
            }
        }
    };

    private final View.OnClickListener _back_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setTabPos(_tabHost.getCurrentTab() - 1);
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