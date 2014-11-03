package com.fieldnation.ui.workorder.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.CounterOfferInfo;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.LoadingView;
import com.fieldnation.ui.dialog.ExpenseDialog;
import com.fieldnation.ui.dialog.PayDialog;
import com.fieldnation.ui.dialog.ScheduleDialog;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CounterOfferActivity extends ActionBarActivity {
    private static final String TAG = "ui.workorder.detail.CounterOfferActivity";

    // Key
    public static final String INTENT_WORKORDER = "com.fieldnation.ui.workorder.detail:WORKORDER";
    public static final String INTENT_WORKORDER_ID = "com.fieldnation.ui.workorder.detail:WORKORDER_ID";
    public static final String INTENT_COUNTER_PAY = "com.fieldnation.ui.workorder.detail:COUNTER_PAY";
    public static final String INTENT_COUNTER_SCHEDULE = "com.fieldnation.ui.workorder.detail:COUNTER_SCHEDULE";
    public static final String INTENT_COUNTER_EXPENSES = "com.fieldnation.ui.workorder.detail:COUNTER_EXPENSES";
    public static final String INTENT_DELETE_COUNTER_EXPENSES = "com.fieldnation.ui.workorder.detail:DELETE_COUNTER_EXPENSES";

    // WEB
    private static final int WEB_CHANGE = 1;
    private static final int WEB_GOT_WORKORDER = 2;
    private static final int WEB_SENDING_COUNTER = 3;

    // UI
    // labels
    private TextView _labelHourlyRateTextView;
    private TextView _labelMaxHoursTextView;
    // data
    private TextView _basisOldTextView;
    private TextView _basisNewTextView;
    private TextView _hourlyOldTextView;
    private TextView _hourlyNewTextView;
    private TextView _maxOldTextView;
    private TextView _maxNewTextView;
    private LinearLayout _editPaymentCounterLayout;
    private TextView _editPaymentCounterTextView;
    private ImageView _editPaymentCounterImageView;

    private TextView _scheduleTypeOldTextView;
    private TextView _scheduleTypeNewTextView;
    private TextView _scheduleDateOldTextView;
    private TextView _scheduleDateNewTextView;
    private TextView _scheduleTimeOldTextView;
    private TextView _scheduleTimeNewTextView;
    private LinearLayout _editScheduleLayout;
    private TextView _editScheduleTextView;
    private ImageView _editScheduleImageView;

    private TextView _noExpensesTextView;

    private LinearLayout _expensesListLayout;
    private LinearLayout _addExpenseLayout;
    private TextView _addExpenseTextView;

    private LinearLayout _reasonsLayout;

    private EditText _requestReasonEditText;
    private CheckBox _deleteNotAcceptedCheckbox;
    private Button _offerTimeButton;

    private Button _cancelButton;
    private Button _sendButton;

    private PayDialog _payDialog;
    private ScheduleDialog _scheduleDialog;
    private ExpenseDialog _expenseDialog;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    private LoadingView _loadingView;

    // Data
    private GlobalState _gs;
    private Workorder _workorder;
    private Calendar _offerTime;
    private Pay _counterPay;
    private Schedule _counterSchedule;
    private List<AdditionalExpense> _counterExpenses;
    private List<AdditionalExpense> _deleteCounterExpenses;
    private WorkorderService _service;
    private long _workorderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_offer);

        _gs = (GlobalState) getApplicationContext();

        _basisOldTextView = (TextView) findViewById(R.id.basis_old_textview);
        _basisNewTextView = (TextView) findViewById(R.id.basis_new_textview);
        _hourlyOldTextView = (TextView) findViewById(R.id.hourly_old_textview);
        _hourlyNewTextView = (TextView) findViewById(R.id.hourly_new_textview);
        _maxOldTextView = (TextView) findViewById(R.id.max_old_textview);
        _maxNewTextView = (TextView) findViewById(R.id.max_new_textview);
        _editPaymentCounterLayout = (LinearLayout) findViewById(R.id.edit_payment_counter_layout);
        _editPaymentCounterLayout.setOnClickListener(_editPaymentLayout_onClick);
        _editPaymentCounterTextView = (TextView) findViewById(R.id.edit_payment_counter_textview);
        _editPaymentCounterImageView = (ImageView) findViewById(R.id.edit_payment_counter_imageview);

        _labelHourlyRateTextView = (TextView) findViewById(R.id.hourly_rate_label_textview);
        _labelMaxHoursTextView = (TextView) findViewById(R.id.max_hours_label_textview);

        _scheduleTypeOldTextView = (TextView) findViewById(R.id.schedule_type_old_textview);
        _scheduleTypeNewTextView = (TextView) findViewById(R.id.schedule_type_new_textview);
        _scheduleDateOldTextView = (TextView) findViewById(R.id.schedule_date_old_textview);
        _scheduleDateNewTextView = (TextView) findViewById(R.id.schedule_date_new_textview);
        _scheduleTimeOldTextView = (TextView) findViewById(R.id.schedule_time_old_textview);
        _scheduleTimeNewTextView = (TextView) findViewById(R.id.schedule_time_new_textview);

        _editScheduleLayout = (LinearLayout) findViewById(R.id.edit_schedule_layout);
        _editScheduleLayout.setOnClickListener(_editScheduleLayout_onClick);
        _editScheduleTextView = (TextView) findViewById(R.id.edit_schedule_textview);
        _editScheduleImageView = (ImageView) findViewById(R.id.edit_schedule_imageview);

        _noExpensesTextView = (TextView) findViewById(R.id.no_expenses_textview);

        _expensesListLayout = (LinearLayout) findViewById(R.id.expenses_list_layout);
        _addExpenseLayout = (LinearLayout) findViewById(R.id.add_expense_layout);
        _addExpenseLayout.setOnClickListener(_addExpenseLayout_onClick);
        _addExpenseTextView = (TextView) findViewById(R.id.add_expense_textview);

        _reasonsLayout = (LinearLayout) findViewById(R.id.reasons_layout);

        _requestReasonEditText = (EditText) findViewById(R.id.request_reason_edittext);
        _deleteNotAcceptedCheckbox = (CheckBox) findViewById(R.id.delete_not_accepted_checkbox);
        _deleteNotAcceptedCheckbox.setOnCheckedChangeListener(_deleteCheck_onChange);

        _offerTimeButton = (Button) findViewById(R.id.offer_time_button);
        _offerTimeButton.setOnClickListener(_offerTimeButton_onClick);

        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancelButton_onClick);

        _sendButton = (Button) findViewById(R.id.send_button);
        _sendButton.setOnClickListener(_sendButton_onClick);

        _loadingView = (LoadingView) findViewById(R.id.loading_view);

        _scheduleDialog = ScheduleDialog.getInstance(getSupportFragmentManager(), TAG);
        _scheduleDialog.setListener(_schedule_listener);

        _payDialog = new PayDialog(this);
        _expenseDialog = new ExpenseDialog(this);

        _counterPay = null;
        _counterSchedule = null;
        _counterExpenses = new LinkedList<AdditionalExpense>();
        _deleteCounterExpenses = new LinkedList<AdditionalExpense>();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INTENT_WORKORDER)) {
                _workorder = savedInstanceState.getParcelable(INTENT_WORKORDER);
            }
            if (savedInstanceState.containsKey(INTENT_COUNTER_PAY)) {
                _counterPay = savedInstanceState.getParcelable(INTENT_COUNTER_PAY);
            }
            if (savedInstanceState.containsKey(INTENT_COUNTER_SCHEDULE)) {
                _counterSchedule = savedInstanceState.getParcelable(INTENT_COUNTER_SCHEDULE);
            }
            if (savedInstanceState.containsKey(INTENT_COUNTER_EXPENSES)) {
                Parcelable[] parc = savedInstanceState.getParcelableArray(INTENT_COUNTER_EXPENSES);
                for (int i = 0; i < parc.length; i++) {
                    _counterExpenses.add((AdditionalExpense) parc[i]);
                }
            }
            if (savedInstanceState.containsKey(INTENT_DELETE_COUNTER_EXPENSES)) {
                Parcelable[] parc = savedInstanceState.getParcelableArray(INTENT_DELETE_COUNTER_EXPENSES);
                for (int i = 0; i < parc.length; i++) {
                    _deleteCounterExpenses.add((AdditionalExpense) parc[i]);
                }
            }
        }

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            if (extras.containsKey(INTENT_WORKORDER_ID)) {
                _workorderId = extras.getLong(INTENT_WORKORDER_ID);
            }

            if (extras.containsKey(INTENT_WORKORDER)) {
                _workorder = extras.getParcelable(INTENT_WORKORDER);
            }
            if (extras.containsKey(INTENT_COUNTER_PAY)) {
                _counterPay = extras.getParcelable(INTENT_COUNTER_PAY);
            }
            if (extras.containsKey(INTENT_COUNTER_SCHEDULE)) {
                _counterSchedule = extras.getParcelable(INTENT_COUNTER_SCHEDULE);
            }
            if (extras.containsKey(INTENT_COUNTER_EXPENSES)) {
                Parcelable[] parc = savedInstanceState.getParcelableArray(INTENT_COUNTER_EXPENSES);
                for (int i = 0; i < parc.length; i++) {
                    _counterExpenses.add((AdditionalExpense) parc[i]);
                }
            }
            if (extras.containsKey(INTENT_DELETE_COUNTER_EXPENSES)) {
                Parcelable[] parc = savedInstanceState.getParcelableArray(INTENT_DELETE_COUNTER_EXPENSES);
                for (int i = 0; i < parc.length; i++) {
                    _deleteCounterExpenses.add((AdditionalExpense) parc[i]);
                }
            }
        }

        showPayCounter(false);
        showScheduleCounter(false);
        showReason(false);

        if (_workorder != null) {
            _workorder.addListener(_workorder_listener);
            populateUi();
        }

        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

        _offerTime = Calendar.getInstance();

        _gs.requestAuthentication(_authClient);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_workorder != null) {
            outState.putParcelable(INTENT_WORKORDER, _workorder);
        }
        if (_counterPay != null) {
            outState.putParcelable(INTENT_COUNTER_PAY, _counterPay);
        }
        if (_counterSchedule != null) {
            outState.putParcelable(INTENT_COUNTER_SCHEDULE, _counterSchedule);
        }
        if (_counterExpenses != null && _counterExpenses.size() > 0) {
            Parcelable[] parc = new Parcelable[_counterExpenses.size()];
            for (int i = 0; i < _counterExpenses.size(); i++) {
                parc[i] = _counterExpenses.get(i);
            }
            outState.putParcelableArray(INTENT_COUNTER_EXPENSES, parc);
        }
        if (_deleteCounterExpenses != null && _deleteCounterExpenses.size() > 0) {
            Parcelable[] parc = new Parcelable[_deleteCounterExpenses.size()];
            for (int i = 0; i < _deleteCounterExpenses.size(); i++) {
                parc[i] = _deleteCounterExpenses.get(i);
            }
            outState.putParcelableArray(INTENT_DELETE_COUNTER_EXPENSES, parc);
        }
        super.onSaveInstanceState(outState);
    }

    private void getData() {
        if (_service == null)
            return;

        if (_workorder != null)
            return;

        if (_loadingView != null)
            _loadingView.setVisibility(View.VISIBLE);

        startService(_service.getDetails(WEB_GOT_WORKORDER, _workorderId, false));
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        showReason(false);
        CounterOfferInfo info = _workorder.getCounterOfferInfo();
        Pay pay = _workorder.getPay();
        // pay section
        _basisOldTextView.setText(pay.getPayRateBasis());
        _labelMaxHoursTextView.setText(R.string.max_hours);
        _labelHourlyRateTextView.setText(R.string.hourly_rate);

        // fixed rate
        if (pay.isFixedRate()) {
            _labelMaxHoursTextView.setText(" ");
            _labelHourlyRateTextView.setText(R.string.total_amount);
            _hourlyOldTextView.setText(misc.toCurrencyTrim(pay.getFixedAmount()));
            _maxOldTextView.setText(" ");
        }

        // blended
        if (pay.isBlendedRate()) {
            _hourlyOldTextView.setText(misc.toCurrencyTrim(pay.getBlendedStartRate()) + " - " + pay.getBlendedFirstHours() + " Hours");
            _maxOldTextView.setText(misc.toCurrencyTrim(pay.getBlendedAdditionalRate()) + " - " + pay.getBlendedAdditionalHours() + " Hours");
        }

        // hourly
        if (pay.isHourlyRate()) {
            _hourlyOldTextView.setText(misc.toCurrencyTrim(pay.getPerHour()) + " per hour");
            _maxOldTextView.setText(pay.getMaxHour() + " Hours Max");
        }

        // per device
        if (pay.isPerDeviceRate()) {
            _hourlyOldTextView.setText(misc.toCurrencyTrim(pay.getPerDevice()) + " per device");
            _maxOldTextView.setText(pay.getMaxDevice() + " Devices Max");
            _labelMaxHoursTextView.setText("Device Rate");
            _labelHourlyRateTextView.setText("Max Devices");
        }

        // schedule
        Schedule schedule = _workorder.getSchedule();

        if (schedule.isExact()) {
            try {
                _scheduleTypeOldTextView.setText("Exact");
                Calendar cal = ISO8601.toCalendar(schedule.getStartTime());
                _scheduleDateOldTextView.setText(misc.formatDateLong(cal));
                _scheduleTimeOldTextView.setText(misc.formatTime(cal, false));

                // _scheduleDateOldTextView.setText(misc);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                _scheduleTypeOldTextView.setText("Range");
                Calendar cal = ISO8601.toCalendar(schedule.getStartTime());
                Calendar cal2 = ISO8601.toCalendar(schedule.getEndTime());

                _scheduleDateOldTextView.setText(String.format(Locale.US, "%tB", cal) + " " + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal2.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR));

                _scheduleTimeOldTextView.setText(misc.formatTime2(cal) + "-" + misc.formatTime(cal2, false));

                // _scheduleDateOldTextView.setText(misc);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // expenses
        if (info != null) {
            AdditionalExpense[] aes = info.getExpense();
            if (aes != null && aes.length > 0) {
                _expensesListLayout.removeAllViews();
                for (int i = 0; i < aes.length; i++) {
                    AdditionalExpense expense = aes[i];
                    if (_deleteCounterExpenses.contains(expense)) {
                        continue;
                    }
                    ExpenseView v = new ExpenseView(this);
                    v.setAdditionalExpense(expense, _expensesListLayout.getChildCount() + 1);
                    v.setListener(_expenseView_listener);
                    _expensesListLayout.addView(v);
                }

            }
        }
        if (_counterExpenses != null && _counterExpenses.size() > 0) {
            for (int i = 0; i < _counterExpenses.size(); i++) {
                AdditionalExpense expense = _counterExpenses.get(i);
                ExpenseView v = new ExpenseView(this);
                v.setAdditionalExpense(expense, _expensesListLayout.getChildCount() + 1);
                v.setListener(_expenseView_listener);
                _expensesListLayout.addView(v);
            }
        }

        if (_expensesListLayout.getChildCount() == 0) {
            _noExpensesTextView.setVisibility(View.VISIBLE);
        } else {
            _noExpensesTextView.setVisibility(View.GONE);
        }

        // pay counter
        if (_counterPay != null) {
            showPayCounter(true);
            // pay section
            _basisNewTextView.setText(_counterPay.getPayRateBasis());

            // fixed rate
            if (_counterPay.isFixedRate()) {
                _hourlyNewTextView.setText(misc.toCurrencyTrim(_counterPay.getFixedAmount()));
                _maxNewTextView.setText(" ");
            }

            // blended
            if (_counterPay.isBlendedRate()) {
                _hourlyNewTextView.setText(misc.toCurrencyTrim(_counterPay.getBlendedStartRate()) + " - " + _counterPay.getBlendedFirstHours() + " Hours");
                _maxNewTextView.setText(misc.toCurrencyTrim(_counterPay.getBlendedAdditionalRate()) + " - " + _counterPay.getBlendedAdditionalHours() + " Hours");
            }

            // hourly
            if (_counterPay.isHourlyRate()) {
                _hourlyNewTextView.setText(misc.toCurrencyTrim(_counterPay.getPerHour()) + " per hour");
                _maxNewTextView.setText(_counterPay.getMaxHour() + " Hours Max");
            }

            // per device
            if (_counterPay.isPerDeviceRate()) {
                _hourlyNewTextView.setText(misc.toCurrencyTrim(_counterPay.getPerDevice()) + " per device");
                _maxNewTextView.setText(_counterPay.getMaxDevice() + " Devices Max");
            }
        } else if (info != null && info.getPay() != null) {
            Pay counter = info.getPay();
            showPayCounter(true);
            // pay section
            _basisNewTextView.setText(counter.getPayRateBasis());

            // fixed rate
            if (counter.isFixedRate()) {
                _hourlyNewTextView.setText(misc.toCurrencyTrim(counter.getFixedAmount()));
                _maxNewTextView.setText(" ");
            }

            // blended
            if (counter.isBlendedRate()) {
                _hourlyNewTextView.setText(misc.toCurrencyTrim(counter.getBlendedStartRate()) + " - " + counter.getBlendedFirstHours() + " Hours");
                _maxNewTextView.setText(misc.toCurrencyTrim(counter.getBlendedAdditionalRate()) + " - " + counter.getBlendedAdditionalHours() + " Hours");
            }

            // hourly
            if (counter.isHourlyRate()) {
                _hourlyNewTextView.setText(misc.toCurrencyTrim(counter.getPerHour()) + " per hour");
                _maxNewTextView.setText(counter.getMaxHour() + " Hours Max");
            }

            // per device
            if (counter.isPerDeviceRate()) {
                _hourlyNewTextView.setText(misc.toCurrencyTrim(counter.getPerDevice()) + " per device");
                _maxNewTextView.setText(counter.getMaxDevice() + " Devices Max");
            }
        } else {
            showPayCounter(false);
        }

        // schedule counter
        if (_counterSchedule != null) {
            showScheduleCounter(true);

            if (_counterSchedule.isExact()) {
                try {
                    _scheduleTypeNewTextView.setText("Exact");
                    Calendar cal = ISO8601.toCalendar(_counterSchedule.getStartTime());
                    _scheduleDateNewTextView.setText(misc.formatDateLong(cal));
                    _scheduleTimeNewTextView.setText(misc.formatTime(cal, false));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    _scheduleTypeNewTextView.setText("Range");
                    Calendar cal = ISO8601.toCalendar(_counterSchedule.getStartTime());
                    Calendar cal2 = ISO8601.toCalendar(_counterSchedule.getEndTime());
                    _scheduleDateNewTextView.setText(String.format(Locale.US, "%tB", cal) + " " + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal2.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR));
                    _scheduleTimeNewTextView.setText(misc.formatTime2(cal) + "-" + misc.formatTime(cal2, false));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (info != null && info.getSchedule() != null) {
            Schedule sched = info.getSchedule();
            showScheduleCounter(true);

            if (sched.isExact()) {
                try {
                    _scheduleTypeNewTextView.setText("Exact");
                    Calendar cal = ISO8601.toCalendar(sched.getStartTime());
                    _scheduleDateNewTextView.setText(misc.formatDateLong(cal));
                    _scheduleTimeNewTextView.setText(misc.formatTime(cal, false));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    _scheduleTypeNewTextView.setText("Range");
                    Calendar cal = ISO8601.toCalendar(sched.getStartTime());
                    Calendar cal2 = ISO8601.toCalendar(sched.getEndTime());
                    _scheduleDateNewTextView.setText(String.format(Locale.US, "%tB", cal) + " " + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal2.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR));
                    _scheduleTimeNewTextView.setText(misc.formatTime2(cal) + "-" + misc.formatTime(cal2, false));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

        if (isDirty()) {
            _sendButton.setVisibility(View.VISIBLE);
            showReason(true);
        } else {
            _sendButton.setVisibility(View.GONE);
        }

        if (info != null) {
            if (info.getExplanation() != null) {
                showReason(true);
                _requestReasonEditText.setText(info.getExplanation());
            }

            _deleteNotAcceptedCheckbox.setChecked(info.getExpires());
            if (info.getExpires()) {
                // TODO need to format the time
                _offerTimeButton.setText(info.getExpiresAfter() + "");
                _offerTimeButton.setVisibility(View.VISIBLE);
            } else {
                _offerTimeButton.setVisibility(View.GONE);
            }
        }
        _loadingView.setVisibility(View.GONE);
    }

    private void showPayCounter(boolean show) {
        if (show) {
            _basisNewTextView.setVisibility(View.VISIBLE);
            _hourlyNewTextView.setVisibility(View.VISIBLE);
            _maxNewTextView.setVisibility(View.VISIBLE);
            _editPaymentCounterTextView.setText(R.string.edit_payment_counter);
            _editPaymentCounterImageView.setBackgroundResource(R.drawable.ic_edit_12);
        } else {
            _basisNewTextView.setVisibility(View.GONE);
            _hourlyNewTextView.setVisibility(View.GONE);
            _maxNewTextView.setVisibility(View.GONE);
            _editPaymentCounterTextView.setText(R.string.request_payment_change);
            _editPaymentCounterImageView.setBackgroundResource(R.drawable.ic_wo_detail_counter_offer);
        }
    }

    private void showScheduleCounter(boolean show) {
        if (show) {
            _scheduleTypeNewTextView.setVisibility(View.VISIBLE);
            _scheduleDateNewTextView.setVisibility(View.VISIBLE);
            _scheduleTimeNewTextView.setVisibility(View.VISIBLE);
            _editScheduleTextView.setText(R.string.edit_schedule_counter);
            _editScheduleImageView.setBackgroundResource(R.drawable.ic_edit_12);
        } else {
            _scheduleTypeNewTextView.setVisibility(View.GONE);
            _scheduleDateNewTextView.setVisibility(View.GONE);
            _scheduleTimeNewTextView.setVisibility(View.GONE);
            _editScheduleTextView.setText(R.string.request_schedule_change);
            _editScheduleImageView.setBackgroundResource(R.drawable.ic_clock_large);
        }
    }

    private void showReason(boolean show) {
        if (show)
            _reasonsLayout.setVisibility(View.VISIBLE);
        else
            _reasonsLayout.setVisibility(View.GONE);
    }

    private boolean isDirty() {
        return (_counterExpenses != null && _counterExpenses.size() > 0) || _counterPay != null || _counterSchedule != null || _deleteCounterExpenses.size() > 0;
    }

    /*-*****************************-*/
    /*-			UI Events			-*/
    /*-*****************************-*/
    private ExpenseView.Listener _expenseView_listener = new ExpenseView.Listener() {
        @Override
        public void onDelete(ExpenseView view, AdditionalExpense expense) {
            if (_counterExpenses.contains(expense)) {
                _counterExpenses.remove(expense);
            } else if (_workorder != null && _workorder.getAdditionalExpenses() != null) {
                AdditionalExpense[] aes = _workorder.getAdditionalExpenses();
                for (int i = 0; i < aes.length; i++) {
                    if (aes[i] == expense) {
                        _deleteCounterExpenses.add(expense);
                    }
                }
            }
            populateUi();
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

    private ScheduleDialog.Listener _schedule_listener = new ScheduleDialog.Listener() {

        @Override
        public void onRange(String startDateTime, String endDateTime) {
            _counterSchedule = new Schedule(startDateTime, endDateTime);
            populateUi();
        }

        @Override
        public void onExact(String startDateTime) {
            _counterSchedule = new Schedule(startDateTime);
            populateUi();
        }

        @Override
        public void onCancel() {
        }
    };

    private ExpenseDialog.Listener _expense_listener = new ExpenseDialog.Listener() {

        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            AdditionalExpense ae = new AdditionalExpense(description, amount, category);
            _counterExpenses.add(ae);
            populateUi();
        }

        @Override
        public void onCancel() {
        }
    };

    private View.OnClickListener _sendButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean expires = _deleteNotAcceptedCheckbox.isChecked();
            long min = 0;

            String reason = _requestReasonEditText.getText().toString();

            if (expires) {
                min = _offerTime.getTimeInMillis() - System.currentTimeMillis() / 60000;
            }
            AdditionalExpense[] expenses = new AdditionalExpense[_counterExpenses.size()];
            for (int i = 0; i < _counterExpenses.size(); i++) {
                expenses[i] = _counterExpenses.get(i);
            }

            startService(
                    _service.setCounterOffer(WEB_SENDING_COUNTER, _workorder.getWorkorderId(),
                            expires, reason, (int) min, _counterPay, _counterSchedule, expenses));

            _loadingView.setVisibility(View.VISIBLE);
        }
    };

    private View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CounterOfferActivity.this.finish();
        }
    };
    private CompoundButton.OnCheckedChangeListener _deleteCheck_onChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                _offerTimeButton.setVisibility(View.VISIBLE);
            else
                _offerTimeButton.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener _offerTimeButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(getSupportFragmentManager(), "");
        }
    };

    private View.OnClickListener _editPaymentLayout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _payDialog.show(_workorder.getPay(), _payDialog_listener);
        }
    };

    private View.OnClickListener _editScheduleLayout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _scheduleDialog.show(TAG, _workorder.getSchedule());
        }
    };

    private View.OnClickListener _addExpenseLayout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _expenseDialog.show("Add Additional Expense", _expense_listener);
        }
    };

    private DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            _offerTime.set(year, month, day);
            _timePicker.show(getSupportFragmentManager(), datePickerDialog.getTag());
        }
    };

    private TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
            _offerTime.set(_offerTime.get(Calendar.YEAR), _offerTime.get(Calendar.MONTH),
                    _offerTime.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
            _offerTimeButton.setText(misc.formatDateTimeLong(_offerTime));
        }
    };

    private Workorder.Listener _workorder_listener = new Workorder.Listener() {
        @Override
        public void onChange(Workorder workorder) {
            getData();
        }
    };

    private AuthenticationClient _authClient = new AuthenticationClient() {

        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            _service = new WorkorderService(CounterOfferActivity.this, username, authToken, _resultReceiver);
            getData();
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_CHANGE) {
                _workorder.dispatchOnChange();
            } else if (resultCode == WEB_GOT_WORKORDER) {
                try {
                    String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
                    Log.v(TAG, data);
                    _workorder = Workorder.fromJson(new JsonObject(data));
                    _workorder.addListener(_workorder_listener);

                    populateUi();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (resultCode == WEB_SENDING_COUNTER) {
                CounterOfferActivity.this.finish();
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            if (_service != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authClient);
        }
    };
}
