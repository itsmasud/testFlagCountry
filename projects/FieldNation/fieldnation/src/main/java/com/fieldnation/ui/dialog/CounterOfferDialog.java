package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.Toast;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CounterOfferInfo;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.v2.ui.dialog.ExpenseDialog;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends DialogFragmentBase {
    private static final String TAG = "CounterOfferDialog";

    // Dialogs
    private static final String DIALOG_EXPENSE = TAG + ".expenseDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_COUNTER_PAY = "STATE_COUNTER_PAY";
    private static final String STATE_EXPENSES = "STATE_EXPENSES";
    private static final String STATE_COUNTER_SCHEDULE = "STATE_COUNTER_SCHEDULE";
    private static final String STATE_COUNTER_REASON = "STATE_COUNTER_REASON";
    private static final String STATE_EXPIRES = "STATE_EXPIRES";
    private static final String STATE_EXPIRATION_IN_SECOND = "STATE_EXPIRATION_IN_SECOND";
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

    private PayDialog _payDialog;
    private ScheduleDialog _scheduleDialog;
    private TermsDialog _termsDialog;

    // Data State
    private final List<Expense> _expenses = new LinkedList<>();

    private Workorder _workorder;
    private Pay _counterPay;
    private Schedule _counterSchedule;
    private String _counterReason;
    private boolean _expires = false;
    //    private String _expirationDate;
    private int _expiresAfterInSecond = -1;
    private int _expireDuration = -1;

    // Data
    private boolean _tacAccpet;
    private Listener _listener;


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static CounterOfferDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, CounterOfferDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_COUNTER_PAY))
                _counterPay = savedInstanceState.getParcelable(STATE_COUNTER_PAY);

            if (savedInstanceState.containsKey(STATE_EXPENSES)) {
                Parcelable[] parc = savedInstanceState.getParcelableArray(STATE_EXPENSES);
                _expenses.clear();
                for (Parcelable aParc : parc) {
                    _expenses.add((Expense) aParc);
                }
            }

            if (savedInstanceState.containsKey(STATE_COUNTER_SCHEDULE))
                _counterSchedule = savedInstanceState.getParcelable(STATE_COUNTER_SCHEDULE);

            if (savedInstanceState.containsKey(STATE_COUNTER_REASON))
                _counterReason = savedInstanceState.getString(STATE_COUNTER_REASON);

            if (savedInstanceState.containsKey(STATE_EXPIRES))
                _expires = savedInstanceState.getBoolean(STATE_EXPIRES);

            if (savedInstanceState.containsKey(STATE_EXPIRATION_IN_SECOND))
                _expiresAfterInSecond = savedInstanceState.getInt(STATE_EXPIRATION_IN_SECOND);

            if (savedInstanceState.containsKey(STATE_TAC))
                _tacAccpet = savedInstanceState.getBoolean(STATE_TAC);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putBoolean(STATE_EXPIRES, _expires);
        outState.putBoolean(STATE_TAC, _tacAccpet);

        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

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
            Log.e(TAG, "_reasonView.getExpiration(): " + _reasonView.getExpiration());
            outState.putString(STATE_COUNTER_REASON, _reasonView.getReason());
            outState.putInt(STATE_EXPIRATION_IN_SECOND, _reasonView.getExpiration());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_counter_offer, container, false);

        _tabHost = (TabHost) v.findViewById(R.id.tabhost);
        _tabHost.setup();

        TabHost.TabSpec tab1 = _tabHost.newTabSpec("start");
        tab1.setIndicator("Pay");
        tab1.setContent(R.id.scrollview1);
        _tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = _tabHost.newTabSpec("mid1");
        tab2.setIndicator("Schedule");
        tab2.setContent(R.id.scrollview2);
        _tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = _tabHost.newTabSpec("mid2");
        tab3.setIndicator("Expense");
        tab3.setContent(R.id.scrollview3);
        _tabHost.addTab(tab3);

        TabHost.TabSpec tab4 = _tabHost.newTabSpec("end");
        tab4.setIndicator("Reason");
        tab4.setContent(R.id.scrollview4);
        _tabHost.addTab(tab4);

        _tabHost.setOnTabChangedListener(_tab_changeListener);

        for (int i = 0; i < 4; i++) {
            _tabHost.getTabWidget().getChildAt(i).setFocusableInTouchMode(true);
        }

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _backButton = (Button) v.findViewById(R.id.back_button);
        _backButton.setOnClickListener(_back_onClick);
        _backButton.setVisibility(View.GONE);

        _paymentView = (PaymentCoView) v.findViewById(R.id.payment_view);
        _paymentView.setListener(_payment_listener);

        _scheduleView = (ScheduleCoView) v.findViewById(R.id.schedule_view);
        _scheduleView.setListener(_scheduleView_listener);

        _expenseView = (ExpenseCoView) v.findViewById(R.id.expenses_view);
//TODO        _expenseView.setListener(_expenseView_listener);

        _reasonView = (ReasonCoView) v.findViewById(R.id.reasons_view);
        _reasonView.setListener(getFragmentManager(), _reason_listener);

        _payDialog = PayDialog.getInstance(getFragmentManager(), TAG);
        _payDialog.setListener(_payDialog_listener);

        _scheduleDialog = ScheduleDialog.getInstance(getFragmentManager(), TAG);
        _scheduleDialog.setListener(_scheduleDialog_listener);

        _tabScrollView = (HorizontalScrollView) v.findViewById(R.id.tabscroll_view);

        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        ExpenseDialog.addOnOkListener(DIALOG_EXPENSE, _expenseDialog_onOk);
    }

    @Override
    public void onStop() {
        super.onStop();

        ExpenseDialog.removeOnOkListener(DIALOG_EXPENSE, _expenseDialog_onOk);
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        Dialog d = getDialog();
        if (d == null)
            return;

        Window window = d.getWindow();

        Display display = window.getWindowManager().getDefaultDisplay();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.setLayout((display.getWidth() * 9) / 10, (display.getHeight() * 7) / 10);
        } else {
            window.setLayout((display.getWidth() * 9) / 10, (display.getHeight() * 9) / 10);
        }

        populateUi();
    }

    @Override
    public void init() {
        Log.v(TAG, "init");
    }

    @Override
    public void reset() {
        setTabPos(0);
    }

    private void populateUi() {
        Log.v(TAG, "populateUi maybe!");
        if (_workorder == null)
            return;

        if (_paymentView == null)
            return;

        if (_scheduleView == null)
            return;

        Log.v(TAG, "populateUi yes!");

        if (_counterPay != null)
            _paymentView.setPay(_counterPay, true);
        else
            _paymentView.setPay(_workorder.getPay(), false);

        if (_counterSchedule != null) {
            _scheduleView.setSchedule(_counterSchedule, true);
        } else {
            _scheduleView.setSchedule(_workorder.getSchedule(), false);
        }

//TODO        _expenseView.setData(_workorder, _expenses);

        _reasonView.setCounterOffer(_counterReason, _expires, _expiresAfterInSecond);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    // this will only be called once.. will not be called on redraw
    public void show(Workorder workorder) {
        Log.v(TAG, "show");
        _workorder = workorder;

        CounterOfferInfo info = _workorder.getCounterOfferInfo();

        _counterPay = null;
        _counterSchedule = null;
        _counterReason = null;
        _expires = false;
//        _expirationDate = null;

        if (info != null) {
            if (info.getPay() != null) {
                _counterPay = info.getPay();
            }
            if (info.getSchedule() != null) {
                _counterSchedule = info.getSchedule();
            }

            if (info.getExpense() != null && info.getExpense().length > 0) {
                Expense[] exp = info.getExpense();

                _expenses.clear();
                Collections.addAll(_expenses, exp);
            }

            _counterReason = info.getExplanation();
            _expires = info.getExpires();
            if (_expires) {
//                try {
//                    _expirationDate = info.getExpiresAfter();
                _expiresAfterInSecond = info.getExpiresAfterInSecond();
//                } catch (Exception ex) {
//                    Log.v(TAG, ex);
//                }
            }
        }

        super.show();
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
            _termsDialog.show(getString(R.string.dialog_terms_title), getString(R.string.dialog_terms_body));
        }

        @Override
        public void onTacChange(boolean isChecked) {
            _tacAccpet = isChecked;
        }

        @Override
        public void onExpirationChange(boolean expires, int second) {
            _expires = expires;
            _expireDuration = second;
        }
    };

/*
TODO    private final ExpenseCoView.Listener _expenseView_listener = new ExpenseCoView.Listener() {
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
            CounterOfferInfo info = _workorder.getCounterOfferInfo();
            _expenses.clear();
            if (info != null && info.getExpense() != null && info.getExpense().length > 0) {
                Expense[] exp = info.getExpense();
                Collections.addAll(_expenses, exp);
            }
            populateUi();
        }

        @Override
        public void editExpense(Expense expense) {
            // TODO editExpense
        }
    };
*/

    private final ExpenseDialog.OnOkListener _expenseDialog_onOk = new ExpenseDialog.OnOkListener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            _expenses.add(new Expense(description, amount, category));
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
            _scheduleDialog.show(schedule);
        }
    };

    private final ScheduleDialog.Listener _scheduleDialog_listener = new ScheduleDialog.Listener() {
        @Override
        public void onComplete(Schedule schedule) {
            _counterSchedule = schedule;
            populateUi();
        }

        @Override
        public void onCancel() {
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
            _payDialog.show(pay);
        }
    };

    private final PayDialog.Listener _payDialog_listener = new PayDialog.Listener() {
        @Override
        public void onComplete(Pay pay, String explanation) {
            _counterPay = pay;
            populateUi();
        }

        @Override
        public void onNothing() {
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
                    Toast.makeText(getActivity(), "Please accept the terms and conditions to continue", Toast.LENGTH_LONG).show();
                    return;
                }

                _counterReason = _reasonView.getReason();

//                if (misc.isEmptyOrNull(_counterReason)) {
//                    Toast.makeText(getActivity(), "Counter offer reason cannot be null. Please enter a reason.", Toast.LENGTH_LONG).show();
//                    return;
//                }

                // Todo need to do some data validation
                if (_listener != null) {
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

                    Log.e(TAG, "_expireDuration: " + _expireDuration);

                    _listener.onOk(_workorder, _counterReason, _expires, _expireDuration, _counterPay, _counterSchedule, exp);
                    _tacAccpet = false;
                    dismiss();
                }
            }
        }
    };

    private final View.OnClickListener _back_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setTabPos(_tabHost.getCurrentTab() - 1);
        }
    };

    public interface Listener {
        void onOk(Workorder workorder, String reason, boolean expires, int expirationInSeconds, Pay pay, Schedule schedule, Expense[] expenses);
    }

}
