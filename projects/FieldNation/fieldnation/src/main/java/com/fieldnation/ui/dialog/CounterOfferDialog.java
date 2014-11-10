package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;

import com.fieldnation.R;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.CounterOfferInfo;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends DialogFragment {
    private static final String TAG = "ui.dialog.CounterOfferDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_COUNTER_PAY = "STATE_COUNTER_PAY";
    private static final String STATE_EXPENSES = "STATE_EXPENSES";

    // Ui
    private TabHost _tabHost;
    private Button _backButton;
    private Button _okButton;

    private PaymentCoView _paymentView;
    private ScheduleCoView _scheduleView;
    private ExpenseCoView _expenseView;

    private PayDialog _payDialog;
    private ScheduleDialog _scheduleDialog;
    private ExpenseDialog _expenseDialog;

    // Data
    private FragmentManager _fm;
    private Workorder _workorder;
    private Pay _counterPay;
    private Schedule _counterSchedule;
    private List<AdditionalExpense> _expenses = new LinkedList<AdditionalExpense>();


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static CounterOfferDialog getInstance(FragmentManager fm, String tag) {
        CounterOfferDialog d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (frag instanceof CounterOfferDialog && frag.getTag().equals(tag)) {
                    d = (CounterOfferDialog) frag;
                    break;
                }
            }
        }
        if (d == null)
            d = new CounterOfferDialog();
        d._fm = fm;
        return d;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_COUNTER_PAY))
                _counterPay = savedInstanceState.getParcelable(STATE_COUNTER_PAY);

            if (savedInstanceState.containsKey(STATE_EXPENSES)) {
                Parcelable[] parc = savedInstanceState.getParcelableArray(STATE_EXPENSES);
                _expenses.clear();
                for (int i = 0; i < parc.length; i++) {
                    _expenses.add((AdditionalExpense) parc[i]);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        if (_counterPay != null)
            outState.putParcelable(STATE_COUNTER_PAY, _counterPay);

        if (_expenses != null && _expenses.size() > 0) {
            AdditionalExpense[] exs = new AdditionalExpense[_expenses.size()];

            for (int i = 0; i < _expenses.size(); i++) {
                exs[i] = _expenses.get(i);
            }

            outState.putParcelableArray(STATE_EXPENSES, exs);
        }

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

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _backButton = (Button) v.findViewById(R.id.back_button);
        _backButton.setOnClickListener(_back_onClick);

        _paymentView = (PaymentCoView) v.findViewById(R.id.payment_view);
        _paymentView.setListener(_payment_listener);

        _scheduleView = (ScheduleCoView) v.findViewById(R.id.schedule_view);
        _scheduleView.setListener(_scheduleView_listener);

        _expenseView = (ExpenseCoView) v.findViewById(R.id.expenses_view);
        _expenseView.setListener(_expenseView_listener);

        _payDialog = PayDialog.getInstance(getFragmentManager(), TAG);
        _payDialog.setListener(_payDialog_listener);

        _scheduleDialog = ScheduleDialog.getInstance(getFragmentManager(), TAG);
        _scheduleDialog.setListener(_scheduleDialog_listener);

        _expenseDialog = ExpenseDialog.getInstance(getFragmentManager(), TAG);
        _expenseDialog.setListener(_expenseDialog_listener);

        for (int i = 0; i < 4; i++) {
            _tabHost.getTabWidget().getChildAt(i).setFocusableInTouchMode(true);
        }

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

        _expenseView.setExpenses(_expenses);
    }


    public void show(String tag, Workorder workorder) {
        _workorder = workorder;

        CounterOfferInfo info = _workorder.getCounterOfferInfo();

        if (info != null) {
            if (info.getPay() != null) {
                _counterPay = info.getPay();
            }
            if (info.getSchedule() != null) {
                _counterSchedule = info.getSchedule();
            }

            if (info.getExpense() != null && info.getExpense().length > 0) {
                AdditionalExpense[] exp = info.getExpense();

                _expenses.clear();
                for (int i = 0; i < exp.length; i++) {
                    _expenses.add(exp[i]);
                }
            }
        }

        super.show(_fm, tag);
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private ExpenseCoView.Listener _expenseView_listener = new ExpenseCoView.Listener() {
        @Override
        public void addExpense() {
            _expenseDialog.show(TAG);
        }

        @Override
        public void removeExpense(AdditionalExpense expense) {
            _expenses.remove(expense);
            populateUi();
        }

        @Override
        public void reset() {
            CounterOfferInfo info = _workorder.getCounterOfferInfo();
            _expenses.clear();
            if (info != null && info.getExpense() != null && info.getExpense().length > 0) {
                AdditionalExpense[] exp = info.getExpense();
                for (int i = 0; i < exp.length; i++) {
                    _expenses.add(exp[i]);
                }
            }
            populateUi();
        }
    };

    private ExpenseDialog.Listener _expenseDialog_listener = new ExpenseDialog.Listener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            _expenses.add(new AdditionalExpense(description, amount, category));
            populateUi();
        }

        @Override
        public void onCancel() {
        }
    };

    private ScheduleCoView.Listener _scheduleView_listener = new ScheduleCoView.Listener() {
        @Override
        public void onClear() {
            _counterSchedule = null;
            populateUi();
        }

        @Override
        public void onChange(Schedule schedule) {
            _scheduleDialog.show(TAG, schedule);
        }
    };

    private ScheduleDialog.Listener _scheduleDialog_listener = new ScheduleDialog.Listener() {
        @Override
        public void onExact(String startDateTime) {
            _counterSchedule = new Schedule(startDateTime);
            populateUi();
        }

        @Override
        public void onRange(String startDateTime, String endDateTime) {
            _counterSchedule = new Schedule(startDateTime, endDateTime);
            populateUi();
        }

        @Override
        public void onCancel() {
        }
    };

    private PaymentCoView.Listener _payment_listener = new PaymentCoView.Listener() {
        @Override
        public void onClearClick() {
            _counterPay = null;
            populateUi();
        }

        @Override
        public void onChangeClick(Pay pay) {
            _payDialog.show(TAG, pay);
        }
    };

    private PayDialog.Listener _payDialog_listener = new PayDialog.Listener() {
        @Override
        public void onPerDevices(double rate, double max) {
            _counterPay = new Pay(rate, (int) max);
            populateUi();
        }

        @Override
        public void onHourly(double rate, double max) {
            _counterPay = new Pay(rate, max);
            populateUi();
        }

        @Override
        public void onFixed(double amount) {
            _counterPay = new Pay(amount);
            populateUi();
        }

        @Override
        public void onBlended(double rate, double max, double rate2, double max2) {
            _counterPay = new Pay(rate, max, rate2, max2);
            populateUi();
        }

        @Override
        public void onNothing() {
        }
    };


    private TabHost.OnTabChangeListener _tab_changeListener = new TabHost.OnTabChangeListener() {
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

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // start?
            if (_tabHost.getCurrentTabTag().equals("start")) {
                _tabHost.setCurrentTab(_tabHost.getCurrentTab() + 1);
            } else if (_tabHost.getCurrentTabTag().startsWith("mid")) {
                _tabHost.setCurrentTab(_tabHost.getCurrentTab() + 1);
            } else if (_tabHost.getCurrentTabTag().equals("end")) {
                // todo finish
            }
        }
    };

    private View.OnClickListener _back_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _tabHost.setCurrentTab(_tabHost.getCurrentTab() - 1);
        }
    };
}
