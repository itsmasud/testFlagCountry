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
import com.fieldnation.utils.ISO8601;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.CounterOfferDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_COUNTER_PAY = "STATE_COUNTER_PAY";
    private static final String STATE_EXPENSES = "STATE_EXPENSES";
    private static final String STATE_COUNTER_SCHEDULE = "STATE_COUNTER_SCHEDULE";
    private static final String STATE_COUNTER_REASON = "STATE_COUNTER_REASON";
    private static final String STATE_EXPIRES = "STATE_EXPIRES";
    private static final String STATE_EXPIRATION_DATE = "STATE_EXPIRATION_DATE";
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
    private ExpenseDialog _expenseDialog;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;
    private TermsDialog _termsDialog;

    // Data State
    private Workorder _workorder;
    private Pay _counterPay;
    private List<Expense> _expenses = new LinkedList<Expense>();
    private Schedule _counterSchedule;
    private String _counterReason;
    private boolean _expires = false;
    private String _expirationDate;

    // Data
    private boolean _tacAccpet;
    private Listener _listener;
    private Calendar _pickerCal;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static CounterOfferDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, CounterOfferDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_COUNTER_PAY))
                _counterPay = savedInstanceState.getParcelable(STATE_COUNTER_PAY);

            if (savedInstanceState.containsKey(STATE_EXPENSES)) {
                Parcelable[] parc = savedInstanceState.getParcelableArray(STATE_EXPENSES);
                _expenses.clear();
                for (int i = 0; i < parc.length; i++) {
                    _expenses.add((Expense) parc[i]);
                }
            }

            if (savedInstanceState.containsKey(STATE_COUNTER_SCHEDULE))
                _counterSchedule = savedInstanceState.getParcelable(STATE_COUNTER_SCHEDULE);

            if (savedInstanceState.containsKey(STATE_COUNTER_REASON))
                _counterReason = savedInstanceState.getString(STATE_COUNTER_REASON);

            if (savedInstanceState.containsKey(STATE_EXPIRES))
                _expires = savedInstanceState.getBoolean(STATE_EXPIRES);

            if (savedInstanceState.containsKey(STATE_EXPIRATION_DATE))
                _expirationDate = savedInstanceState.getString(STATE_EXPIRATION_DATE);

            if (savedInstanceState.containsKey(STATE_TAC))
                _tacAccpet = savedInstanceState.getBoolean(STATE_TAC);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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

        if (_counterReason != null)
            outState.putString(STATE_COUNTER_REASON, _reasonView.getReason());

        if (_expirationDate != null)
            outState.putString(STATE_EXPIRATION_DATE, _reasonView.getExpiration());

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        _expenseView.setListener(_expenseView_listener);

        _reasonView = (ReasonCoView) v.findViewById(R.id.reasons_view);
        _reasonView.setListener(_reason_listener);

        _payDialog = PayDialog.getInstance(getFragmentManager(), TAG);
        _payDialog.setListener(_payDialog_listener);

        _scheduleDialog = ScheduleDialog.getInstance(getFragmentManager(), TAG);
        _scheduleDialog.setListener(_scheduleDialog_listener);

        _expenseDialog = ExpenseDialog.getInstance(getFragmentManager(), TAG);
        _expenseDialog.setListener(_expenseDialog_listener);

        _tabScrollView = (HorizontalScrollView) v.findViewById(R.id.tabscroll_view);

        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);

        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

        _pickerCal = Calendar.getInstance();


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
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
    }

    @Override
    public void init() {
        populateUi();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_paymentView == null)
            return;

        if (_scheduleView == null)
            return;

        if (_counterPay != null)
            _paymentView.setPay(_counterPay, true);
        else
            _paymentView.setPay(_workorder.getPay(), false);

        if (_counterSchedule != null) {
            _scheduleView.setSchedule(_counterSchedule, true);
        } else {
            _scheduleView.setSchedule(_workorder.getSchedule(), false);
        }

        _expenseView.setData(_workorder, _expenses);

        _reasonView.setCounterOffer(_counterReason, _expires, _expirationDate);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    // this will only be called once.. will not be called on redraw
    public void show(Workorder workorder) {
        _workorder = workorder;

        CounterOfferInfo info = _workorder.getCounterOfferInfo();

        _counterPay = null;
        _counterSchedule = null;
        _counterReason = null;
        _expires = false;
        _expirationDate = null;

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
                for (int i = 0; i < exp.length; i++) {
                    _expenses.add(exp[i]);
                }
            }

            _counterReason = info.getExplanation();
            _expires = info.getExpires();
            if (_expires) {
                try {
                    _expirationDate = ISO8601.fromUTC(Calendar.getInstance().getTimeInMillis() + info.getExpiresAfter() * 1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        super.show();
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final ReasonCoView.Listener _reason_listener = new ReasonCoView.Listener() {
        @Override
        public void onTacClick() {
            _termsDialog.show();
        }

        @Override
        public void onTacChange(boolean isChecked) {
            _tacAccpet = isChecked;
        }

        @Override
        public void showDateTimePicker() {
            _datePicker.show(getFragmentManager(), TAG);
        }
    };

    private final ExpenseCoView.Listener _expenseView_listener = new ExpenseCoView.Listener() {
        @Override
        public void addExpense() {
            _expenseDialog.show(false);
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
                for (int i = 0; i < exp.length; i++) {
                    _expenses.add(exp[i]);
                }
            }
            populateUi();
        }
    };

    private final ExpenseDialog.Listener _expenseDialog_listener = new ExpenseDialog.Listener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            _expenses.add(new Expense(description, amount, category));
            populateUi();
        }

        @Override
        public void onCancel() {
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
        public void onComplete(Pay pay) {
            _counterPay = pay;
            populateUi();
        }

        @Override
        public void onNothing() {
        }
    };

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            _pickerCal.set(year, month, day);
            _timePicker.show(_fm, datePickerDialog.getTag());
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
            String tag = view.getTag();
            _pickerCal.set(_pickerCal.get(Calendar.YEAR), _pickerCal.get(Calendar.MONTH),
                    _pickerCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            _expirationDate = ISO8601.fromCalendar(_pickerCal);
            _expires = true;
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
                _tabHost.setCurrentTab(_tabHost.getCurrentTab() + 1);
            } else if (_tabHost.getCurrentTabTag().startsWith("mid")) {
                _tabHost.setCurrentTab(_tabHost.getCurrentTab() + 1);
            } else if (_tabHost.getCurrentTabTag().equals("end")) {
                if (!_tacAccpet) {
                    Toast.makeText(getActivity(), "Please accept the terms and conditions to continue", Toast.LENGTH_LONG).show();
                    return;
                }

                // Todo need to do some data validation
                if (_listener != null) {
                    Expense[] exp = new Expense[_expenses.size()];
                    for (int i = 0; i < _expenses.size(); i++) {
                        exp[i] = _expenses.get(i);
                    }
                    int seconds = 0;

                    _counterReason = _reasonView.getReason();

                    try {
                        seconds = (int) (ISO8601.toUtc(_expirationDate)
                                - Calendar.getInstance().getTimeInMillis()) / 1000;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    _listener.onOk(_workorder, _counterReason, _expires, seconds, _counterPay, _counterSchedule, exp);
                    dismiss();
                }
            }
        }
    };

    private final View.OnClickListener _back_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _tabHost.setCurrentTab(_tabHost.getCurrentTab() - 1);
        }
    };

    public interface Listener {
        public void onOk(Workorder workorder, String reason, boolean expires, int expirationInSeconds, Pay pay, Schedule schedule, Expense[] expenses);
    }

}
