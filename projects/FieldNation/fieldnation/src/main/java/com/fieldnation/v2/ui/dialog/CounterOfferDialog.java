package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.ReasonCoView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.ExpenseCategory;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.KeyValuePairView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends FullScreenDialog {
    private static final String TAG = "CounterOfferDialog";

    // Dialogs
    private static final String DIALOG_UID_EXPENSE = TAG + ".expenseDialog";
    private static final String DIALOG_UID_SCHEDULE = TAG + ".scheduleDialog";
    private static final String DIALOG_UID_PAY = TAG + ".payDialog";

    // State
    private static final String STATE_EXPENSES = "STATE_EXPENSES";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private RefreshView _refreshView;

    private RelativeLayout _payLayout;
    private KeyValuePairView _pay1View;
    private KeyValuePairView _pay2View;
    private KeyValuePairView _pay3View;
    private KeyValuePairView _pay4View;
    private KeyValuePairView _pay5View;

    private RelativeLayout _scheduleLayout;
    private KeyValuePairView _scheduleView1;
    private KeyValuePairView _scheduleView2;

    private RelativeLayout _expenseLayout;
    private LinearLayout _expensesList;

    private ReasonCoView _reasonView;
    private TextView _termsWarningTextView;

    // Bottom Sheet
    private Button _floatingActionButton;
    private View _bottomSheetBackground;
    private LinearLayout _bottomSheet;
    private TextView _changePayButton;
    private TextView _changeScheduleButton;
    private TextView _addExpenseButton;


    // Animations
    private Animation _fadeIn;
    private Animation _fadeOut;
    private Animation _bsSlideIn;
    private Animation _bsSlideOut;
    private Animation _fabSlideOut;
    private Animation _fabSlideIn;

    // Data
    private WorkOrder _workOrder;
    private WorkordersWebApi _workOrderApi;

    // User Data
    private final List<Expense> _expenses = new LinkedList<>();
    private Schedule _schedule = null;
    private Pay _pay = null;

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

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle(App.get().getString(R.string.counter_offer));

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_submit);

        _payLayout = v.findViewById(R.id.pay_layout);
        _pay1View = v.findViewById(R.id.pay1_view);
        _pay2View = v.findViewById(R.id.pay2_view);
        _pay3View = v.findViewById(R.id.pay3_view);
        _pay4View = v.findViewById(R.id.pay4_view);
        _pay5View = v.findViewById(R.id.pay5_view);

        _scheduleLayout = v.findViewById(R.id.schedule_layout);
        _scheduleView1 = v.findViewById(R.id.scheduleView1);
        _scheduleView2 = v.findViewById(R.id.scheduleView2);

        _expenseLayout = v.findViewById(R.id.expense_layout);
        _expensesList = v.findViewById(R.id.expenses_list);

        _reasonView = v.findViewById(R.id.reasons_view);

        _termsWarningTextView = v.findViewById(R.id.termswarning_textview);

        // Bottom Sheet
        _floatingActionButton = v.findViewById(R.id.action_button);
        _bottomSheetBackground = v.findViewById(R.id.bottomSheet_background);
        _bottomSheet = v.findViewById(R.id.bottomsheet);
        _changePayButton = v.findViewById(R.id.changePay_button);
        _changeScheduleButton = v.findViewById(R.id.changeSchedule_button);
        _addExpenseButton = v.findViewById(R.id.addExpense_button);

        _refreshView = v.findViewById(R.id.refresh_view);

        _fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        _fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        _bsSlideIn = AnimationUtils.loadAnimation(context, R.anim.fg_slide_in_bottom);
        _bsSlideOut = AnimationUtils.loadAnimation(context, R.anim.fg_slide_out_bottom);
        _fabSlideIn = AnimationUtils.loadAnimation(context, R.anim.fg_slide_in_right);
        _fabSlideOut = AnimationUtils.loadAnimation(context, R.anim.fg_slide_out_right);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _termsWarningTextView.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString spanned = new SpannableString("By countering this work order, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
        spanned.setSpan(_standardTerms_onClick, 91, 131, spanned.getSpanFlags(_standardTerms_onClick));
        spanned.setSpan(_pqap_onClick, 140, 173, spanned.getSpanFlags(_pqap_onClick));
        _termsWarningTextView.setText(spanned);

        _workOrderApi = new WorkordersWebApi(_workOrdersWebApi_listener);
        _workOrderApi.connect(App.get());

        _floatingActionButton.setOnClickListener(_actionButton_onClick);
        _changePayButton.setOnClickListener(_changePay_onClick);
        _changeScheduleButton.setOnClickListener(_changeSchedule_onClick);
        _addExpenseButton.setOnClickListener(_addExpense_onClick);
        _bottomSheetBackground.setOnClickListener(_bottomSheet_onCancel);

        _fadeIn.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _bottomSheetBackground.setVisibility(View.VISIBLE);
            }
        });

        _fadeOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _bottomSheetBackground.setVisibility(View.GONE);
            }
        });

        _bsSlideIn.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _bottomSheet.setVisibility(View.VISIBLE);
            }
        });

        _bsSlideOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _bottomSheet.setVisibility(View.GONE);
            }
        });

        _fabSlideIn.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _floatingActionButton.setVisibility(View.VISIBLE);
            }
        });

        _fabSlideOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _floatingActionButton.setVisibility(View.GONE);
            }
        });

        _bottomSheet.clearAnimation();
        _bottomSheet.startAnimation(_bsSlideOut);

        ExpenseDialog.addOnOkListener(DIALOG_UID_EXPENSE, _expenseDialog_onOk);
        ScheduleDialog.addOnCompleteListener(DIALOG_UID_SCHEDULE, _scheduleDialog_onOk);
        PayDialog.addOnCompleteListener(DIALOG_UID_PAY, _payDialog_onOk);
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
            _expenses.clear();
            for (Parcelable aParc : parc) {
                _expenses.add((Expense) aParc);
            }
        }

        populateUi();
    }

    @Override
    public void onStop() {
        ExpenseDialog.removeOnOkListener(DIALOG_UID_EXPENSE, _expenseDialog_onOk);
        ScheduleDialog.removeOnCompleteListener(DIALOG_UID_SCHEDULE, _scheduleDialog_onOk);
        PayDialog.addOnCompleteListener(DIALOG_UID_PAY, _payDialog_onOk);
        if (_workOrderApi != null) _workOrderApi.disconnect(App.get());
        super.onStop();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");

        if (_expenses != null && _expenses.size() > 0) {
            Expense[] exs = new Expense[_expenses.size()];

            for (int i = 0; i < _expenses.size(); i++) {
                exs[i] = _expenses.get(i);
            }

            outState.putParcelableArray(STATE_EXPENSES, exs);
        }
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_expensesList == null)
            return;

        if (_expenses.size() > 0) {
            _expenseLayout.setVisibility(View.VISIBLE);
            _expensesList.removeAllViews();
            ForLoopRunnable r = new ForLoopRunnable(_expenses.size(), new Handler()) {

                @Override
                public void next(int i) throws Exception {
                    KeyValuePairView v = new KeyValuePairView(getContext());
                    v.set(_expenses.get(i).getDescription(), misc.toCurrency(_expenses.get(i).getAmount()));
                    _expensesList.addView(v);
                }
            };
            _expensesList.postDelayed(r, new Random().nextInt(100));
        } else {
            _expenseLayout.setVisibility(View.GONE);
        }

        if (_schedule != null) {
            _scheduleLayout.setVisibility(View.VISIBLE);

            try {
                DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
                symbols.setAmPmStrings(getContext().getResources().getStringArray(R.array.schedule_capital_case_am_pm_array));

                switch (_schedule.getServiceWindow().getMode()) {
                    case BETWEEN: {
                        Calendar sCal = _schedule.getServiceWindow().getStart().getCalendar();
                        Calendar eCal = _schedule.getServiceWindow().getEnd().getCalendar();

                        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d, yyyy @ h:mm a", Locale.getDefault());
                        sdf.setDateFormatSymbols(symbols);

                        _scheduleView1.set("Start After", sdf.format(sCal.getTime()));
                        _scheduleView2.set("Finish By", sdf.format(eCal.getTime()));
                        break;
                    }
                    case EXACT: {
                        Calendar sCal = _schedule.getServiceWindow().getStart().getCalendar();

                        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d, yyyy", Locale.getDefault());
                        sdf.setDateFormatSymbols(symbols);

                        _scheduleView1.set("Date", sdf.format(sCal.getTime()));

                        sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        sdf.setDateFormatSymbols(symbols);

                        _scheduleView2.set("Time", sdf.format(sCal.getTime()));
                        break;
                    }
                    case HOURS: {
                        Calendar sCal = _schedule.getServiceWindow().getStart().getCalendar();
                        Calendar eCal = _schedule.getServiceWindow().getEnd().getCalendar();

                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                        sdf.setDateFormatSymbols(symbols);

                        _scheduleView1.set("Between", sdf.format(sCal.getTime()) + " - " + sdf.format(eCal.getTime()));

                        sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        sdf.setDateFormatSymbols(symbols);

                        _scheduleView2.set("During", sdf.format(sCal.getTime()) + " - " + sdf.format(eCal.getTime()));
                        break;
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
                ToastClient.toast(App.get(), "There was a problem with the schedule, please try setting it again", Toast.LENGTH_SHORT);
                _scheduleLayout.setVisibility(View.GONE);
                _schedule = null;
            }
        } else {
            _scheduleLayout.setVisibility(View.GONE);
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/

    private final View.OnClickListener _actionButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            misc.hideKeyboard(v);
            _bottomSheetBackground.clearAnimation();
            _bottomSheetBackground.startAnimation(_fadeIn);
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideIn);
            _floatingActionButton.clearAnimation();
            _floatingActionButton.startAnimation(_fabSlideOut);
        }
    };

    private final View.OnClickListener _bottomSheet_onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _bottomSheetBackground.clearAnimation();
            _bottomSheetBackground.startAnimation(_fadeOut);
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            _floatingActionButton.clearAnimation();
            _floatingActionButton.startAnimation(_fabSlideIn);
        }
    };

    private final View.OnClickListener _changePay_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PayDialog.show(App.get(), DIALOG_UID_PAY, _pay != null ? _pay : _workOrder.getPay(), false);
            _bottomSheet_onCancel.onClick(view);
        }
    };

    private final View.OnClickListener _changeSchedule_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ScheduleDialog.show(App.get(), DIALOG_UID_SCHEDULE,
                    _schedule != null ? _schedule : _workOrder.getSchedule());
            _bottomSheet_onCancel.onClick(view);
        }
    };

    private final View.OnClickListener _addExpense_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ExpenseDialog.show(App.get(), DIALOG_UID_EXPENSE, false);
            _bottomSheet_onCancel.onClick(view);
        }
    };

    private final ExpenseDialog.OnOkListener _expenseDialog_onOk = new ExpenseDialog.OnOkListener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            _expenses.add(new Expense(description, amount));
            populateUi();
        }
    };

    private final ScheduleDialog.OnCompleteListener _scheduleDialog_onOk = new ScheduleDialog.OnCompleteListener() {
        @Override
        public void onComplete(Schedule schedule) {
            _schedule = schedule;
            populateUi();
        }
    };

    private final PayDialog.OnCompleteListener _payDialog_onOk = new PayDialog.OnCompleteListener() {
        @Override
        public void onComplete(Pay pay, String explanation) {

        }
    };

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

            Expense[] exp = new Expense[_expenses.size()];
            for (int i = 0; i < _expenses.size(); i++) {
                Expense e = _expenses.get(i);


                if (!misc.isEmptyOrNull(e.getDescription()) && e.getAmount() != null)
                    exp[i] = _expenses.get(i);
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

                if (_schedule != null)
                    request.schedule(_schedule);

                if (exp != null)
                    request.expenses(exp);

                if (_reasonView.getExpiryTime() > 0)
                    request.expires(new Date(_reasonView.getExpiryTime()));

                if (!misc.isEmptyOrNull(_reasonView.getReason()))
                    request.counterNotes(_reasonView.getReason());

                if (!_paymentView.isValidPay() && _schedule == null && !(exp != null && exp.length > 0)) {
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
                    Expense[] exp = new Expense[_expenses.size()];
                    for (int i = 0; i < _expenses.size(); i++) {
                        exp[i] = _expenses.get(i);
                    }

                    _onOkDispatcher.dispatch(getUid(), _workOrder, _reasonView.getReason(), _reasonView.getExpiryTime(), _paymentView.getPay(), _schedule, exp);
                    dismiss(true);

                    _refreshView.refreshComplete();
                }
            }
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