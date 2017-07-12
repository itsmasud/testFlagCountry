package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontTextView;
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
import com.fieldnation.v2.ui.PayView;
import com.fieldnation.v2.ui.ScheduleView;
import com.fieldnation.v2.ui.TwoLineActionTile;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends FullScreenDialog {
    private static final String TAG = "CounterOfferDialog";

    // Dialogs
    private static final String DIALOG_UID_EXPENSE = TAG + ".expenseDialog";
    private static final String DIALOG_UID_SCHEDULE = TAG + ".scheduleDialog";
    private static final String DIALOG_UID_PAY = TAG + ".payDialog";
    private static final String DIALOG_UID_EXPIRE = TAG + ".expireDailog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private RefreshView _refreshView;
    private View _emptyView;

    // Ui - Pay
    private RelativeLayout _payLayout;
    private IconFontTextView _payMenu;
    private PopupMenu _payPopUp;
    private PayView _payView;

    // Ui - Schedule
    private RelativeLayout _scheduleLayout;
    private KeyValuePairView _scheduleTypeView;
    private IconFontTextView _scheduleMenu;
    private PopupMenu _schedulePopUp;
    private ScheduleView _scheduleView;

    // Ui - Expenses
    private RelativeLayout _expenseLayout;
    private LinearLayout _expensesList;
    private View _expenseMenuClickedView = null;

    // Ui - Expires
    private RelativeLayout _expiresLayout;
    private IconFontTextView _expiresMenu;
    private KeyValuePairView _expiresView;
    private PopupMenu _expiresPopUp;

    // Ui - Reason
    private RelativeLayout _reasonLayout;
    private IconFontTextView _reasonsMenu;
    private TextView _reasonTextView;
    private EditText _reasonEditText;
    private TextView _disclaimerTextView;
    private PopupMenu _reasonPopUp;

    private TextView _termsWarningTextView;

    // Bottom Sheet
    private Button _floatingActionButton;
    private View _bottomSheetBackground;
    private LinearLayout _bottomSheet;
    private TextView _changePayButton;
    private TextView _changeScheduleButton;
    private TextView _addExpenseButton;
    private TextView _addExpirationButton;
    private TextView _addReasonButton;

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
    private Pay _pay = null;
    private Schedule _schedule = null;
    private final List<Expense> _expenses = new LinkedList<>();
    private long _expiresMilliSeconds = -1;
    private String _expiresTitle = null;
    private String _reason = null;

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

        _emptyView = v.findViewById(R.id.empty_view);

        _payLayout = v.findViewById(R.id.pay_layout);
        _payMenu = v.findViewById(R.id.pay_menu);
        _payView = v.findViewById(R.id.pay_view);

        _scheduleLayout = v.findViewById(R.id.schedule_layout);
        _scheduleTypeView = v.findViewById(R.id.scheduleType_view);
        _scheduleMenu = v.findViewById(R.id.schedule_menu);
        _scheduleView = v.findViewById(R.id.schedule_view);

        _expenseLayout = v.findViewById(R.id.expense_layout);
        _expensesList = v.findViewById(R.id.expenses_list);

        _expiresLayout = v.findViewById(R.id.expires_layout);
        _expiresMenu = v.findViewById(R.id.expires_menu);
        _expiresView = v.findViewById(R.id.expires_kvp);

        _reasonLayout = v.findViewById(R.id.reason_layout);
        _reasonsMenu = v.findViewById(R.id.reasons_menu);
        _reasonTextView = v.findViewById(R.id.reason_textview);
        _reasonEditText = v.findViewById(R.id.reason_edittext);
        _disclaimerTextView = v.findViewById(R.id.disclaimer_textview);

        _termsWarningTextView = v.findViewById(R.id.termswarning_textview);

        // Bottom Sheet
        _floatingActionButton = v.findViewById(R.id.action_button);
        _bottomSheetBackground = v.findViewById(R.id.bottomSheet_background);
        _bottomSheet = v.findViewById(R.id.bottomsheet);
        _changePayButton = v.findViewById(R.id.changePay_button);
        _changeScheduleButton = v.findViewById(R.id.changeSchedule_button);
        _addExpenseButton = v.findViewById(R.id.addExpense_button);
        _addExpirationButton = v.findViewById(R.id.addexpiration_button);
        _addReasonButton = v.findViewById(R.id.addreason_button);

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

        _payView.setOnRenderListener(_pay_onRender);
        _payView.setOnClickListener(_pay_onClick);
        _payMenu.setOnClickListener(_payMenu_onClick);
        _payPopUp = new PopupMenu(getContext(), _payMenu);
        _payPopUp.inflate(R.menu.edit_delete);
        _payPopUp.setOnMenuItemClickListener(_payPopUp_onClick);

        _scheduleView.setOnRenderListener(_schedule_onRender);
        _scheduleView.setOnClickListener(_schedule_onClick);
        _scheduleMenu.setOnClickListener(_scheduleMenu_onClick);
        _schedulePopUp = new PopupMenu(getContext(), _scheduleMenu);
        _schedulePopUp.inflate(R.menu.edit_delete);
        _schedulePopUp.setOnMenuItemClickListener(_schedulePopUp_onClick);

        _expiresMenu.setOnClickListener(_expiresMenu_onClick);
        _expiresView.setOnClickListener(_expiresView_onClick);
        _expiresPopUp = new PopupMenu(getContext(), _expiresMenu);
        _expiresPopUp.inflate(R.menu.edit_delete);
        _expiresPopUp.setOnMenuItemClickListener(_expiresPopUp_onClick);

        _reasonsMenu.setOnClickListener(_reasonsMenu_onClick);
        _reasonTextView.setOnClickListener(_reasonTextView_onClick);
        _reasonEditText.setOnFocusChangeListener(_reasonEditText_onFocusChange);
        _reasonEditText.addTextChangedListener(_reasonEditText_onChanged);
        _reasonPopUp = new PopupMenu(getContext(), _reasonsMenu);
        _reasonPopUp.inflate(R.menu.edit_delete);
        _reasonPopUp.setOnMenuItemClickListener(_reasonPopUp_onClick);

        _floatingActionButton.setOnClickListener(_fab_onClick);
        _changePayButton.setOnClickListener(_changePay_onClick);
        _changeScheduleButton.setOnClickListener(_changeSchedule_onClick);
        _addExpenseButton.setOnClickListener(_addExpense_onClick);
        _addExpirationButton.setOnClickListener(_addExpirationButton_onClick);
        _addReasonButton.setOnClickListener(_addReasonButton_onClick);
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

        _workOrderApi = new WorkordersWebApi(_workOrdersWebApi_listener);
        _workOrderApi.connect(App.get());

        ExpenseDialog.addOnOkListener(DIALOG_UID_EXPENSE, _expenseDialog_onOk);
        ScheduleDialog.addOnCompleteListener(DIALOG_UID_SCHEDULE, _scheduleDialog_onOk);
        PayDialog.addOnCompleteListener(DIALOG_UID_PAY, _payDialog_onOk);
        ExpireDialog.addOnOkListener(DIALOG_UID_EXPIRE, _expireDialog_onOk);
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
        Log.v(TAG, "onRestoreDialogState");

        if (savedState.containsKey("_pay"))
            _pay = savedState.getParcelable("_pay");

        if (savedState.containsKey("_schedule"))
            _schedule = savedState.getParcelable("_schedule");

        if (savedState.containsKey("_expenses")) {
            Parcelable[] parcels = savedState.getParcelableArray("_expenses");
            for (Parcelable parcel : parcels) {
                _expenses.add((Expense) parcel);
            }
        }

        if (savedState.containsKey("_expiresMilliSeconds"))
            _expiresMilliSeconds = savedState.getLong("_expiresMilliSeconds");

        if (savedState.containsKey("_expiresTitle"))
            _expiresTitle = savedState.getString("_expiresTitle");

        if (savedState.containsKey("_reason"))
            _reason = savedState.getString("_reason");

        populateUi();
    }

    @Override
    public void onStop() {
        ExpenseDialog.removeOnOkListener(DIALOG_UID_EXPENSE, _expenseDialog_onOk);
        ScheduleDialog.removeOnCompleteListener(DIALOG_UID_SCHEDULE, _scheduleDialog_onOk);
        PayDialog.removeOnCompleteListener(DIALOG_UID_PAY, _payDialog_onOk);
        ExpireDialog.removeOnOkListener(DIALOG_UID_EXPIRE, _expireDialog_onOk);
        if (_workOrderApi != null) _workOrderApi.disconnect(App.get());
        super.onStop();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");

        if (_pay != null)
            outState.putParcelable("_pay", _pay);

        if (_schedule != null)
            outState.putParcelable("_schedule", _schedule);

        if (_expenses.size() > 0) {
            Expense[] expenses = _expenses.toArray(new Expense[_expenses.size()]);
            outState.putParcelableArray("_expenses", expenses);
        }

        if (_expiresMilliSeconds != -1)
            outState.putLong("_expiresMilliSeconds", _expiresMilliSeconds);

        if (_expiresTitle != null)
            outState.putString("_expiresTitle", _expiresTitle);

        if (!misc.isEmptyOrNull(_reason))
            outState.putString("_reason", _reason);
    }

    private void populateUi() {
        if (_payLayout == null)
            return;

        // Pay
        if (_pay != null) {
            _payLayout.setVisibility(View.VISIBLE);
            _payView.set(_pay);
        } else {
            _payLayout.setVisibility(View.GONE);
        }

        // schedule
        if (_schedule != null) {
            _scheduleLayout.setVisibility(View.VISIBLE);
            if (_schedule.getServiceWindow().getMode() != null) {
                switch (_schedule.getServiceWindow().getMode()) {
                    case BETWEEN:
                        _scheduleTypeView.set("Type", "Ranged");
                        break;
                    case EXACT:
                        _scheduleTypeView.set("Type", "Exact");
                        break;
                    case HOURS:
                        _scheduleTypeView.set("Type", "Business Hours");
                        break;
                }
            }
            _scheduleView.set(_schedule);
        } else {
            _scheduleLayout.setVisibility(View.GONE);
        }

        // expenses
        if (_expensesList != null && _expenses.size() > 0) {
            _expenseLayout.setVisibility(View.VISIBLE);
            _expensesList.removeAllViews();

            for (int i = 0; i < _expenses.size(); i++) {
                TwoLineActionTile v = new TwoLineActionTile(getContext());
                v.set(_expenses.get(i).getDescription(), misc.toCurrency(_expenses.get(i).getAmount()));
                v.setActionString(getContext().getString(R.string.icon_overflow));
                v.setOnActionClickedListener(_expense_onClick);
                v.setTag(i);
                _expensesList.addView(v);
            }
        } else {
            _expenseLayout.setVisibility(View.GONE);
        }

        // expiration
        if (_expiresMilliSeconds != -1) {
            _expiresLayout.setVisibility(View.VISIBLE);
            _expiresView.set("Expire Offer In", _expiresTitle);
        } else {
            _expiresLayout.setVisibility(View.GONE);
        }

        // reason
        if (!misc.isEmptyOrNull(_reason)) {
            _reasonLayout.setVisibility(View.VISIBLE);
            _reasonTextView.setText(_reason);
            _reasonEditText.setText(_reason);
            _reasonTextView.setVisibility(View.VISIBLE);
            _reasonEditText.setVisibility(View.GONE);
            _disclaimerTextView.setVisibility(View.GONE);
        } else {
            _reasonLayout.setVisibility(View.GONE);
            _reasonEditText.setText("");
            _reasonTextView.setText("");
            _reasonTextView.setVisibility(View.GONE);
            _reasonEditText.setVisibility(View.VISIBLE);
            _disclaimerTextView.setVisibility(View.VISIBLE);
        }

        if (misc.isEmptyOrNull(_reason) && _expiresMilliSeconds == -1 && _expenses.size() == 0 && _schedule == null && _pay == null) {
            _emptyView.setVisibility(View.VISIBLE);
            _termsWarningTextView.setVisibility(View.GONE);
        } else {
            _emptyView.setVisibility(View.GONE);
            _termsWarningTextView.setVisibility(View.VISIBLE);
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _fab_onClick = new View.OnClickListener() {
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

                if (_pay != null) {
                    request.pay(_pay);
                }

                if (_schedule != null)
                    request.schedule(_schedule);

                if (exp != null)
                    request.expenses(exp);

                if (_expiresMilliSeconds > -1)
                    request.expires(new Date(System.currentTimeMillis() + _expiresMilliSeconds));

                if (!misc.isEmptyOrNull(_reason))
                    request.counterNotes(_reason);

                if (_pay == null && _schedule == null && !(exp != null && exp.length > 0)) {
                    ToastClient.toast(App.get(), App.get().getString(R.string.toast_empty_counter_offer), Toast.LENGTH_SHORT);
                    _refreshView.refreshComplete();
                    return false;
                }

                SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                uiContext.page += " - Counter Offer Dialog";
                WorkordersWebApi.request(App.get(), _workOrder.getId(), request, uiContext);

                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.COUNTER_OFFER,
                        WorkOrderTracker.Action.COUNTER_OFFER, _workOrder.getId());

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

    /*-*********************-*/
    /*-         Pay         -*/
    /*-*********************-*/
    private final View.OnClickListener _pay_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PayDialog.show(App.get(), DIALOG_UID_PAY, _pay != null ? _pay : _workOrder.getPay(), false);
        }
    };

    private final View.OnClickListener _changePay_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            PayDialog.show(App.get(), DIALOG_UID_PAY, _pay != null ? _pay : _workOrder.getPay(), false);
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _payMenu_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _payPopUp.show();
        }
    };

    private final PayDialog.OnCompleteListener _payDialog_onOk = new PayDialog.OnCompleteListener() {
        @Override
        public void onComplete(Pay pay, String explanation) {
            _pay = pay;
            populateUi();
        }
    };

    private final PopupMenu.OnMenuItemClickListener _payPopUp_onClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.edit_menu) {
                PayDialog.show(App.get(), DIALOG_UID_PAY, _pay != null ? _pay : _workOrder.getPay(), false);
            } else if (item.getItemId() == R.id.delete_menu) {
                _pay = null;
                populateUi();
            }
            return true;
        }
    };

    private final PayView.OnRenderListener _pay_onRender = new PayView.OnRenderListener() {
        @Override
        public void onRender(boolean success) {
            if (!success) {
                ToastClient.toast(App.get(), "There was a problem with pay, please try again", Toast.LENGTH_SHORT);
                _pay = null;
                _payLayout.setVisibility(View.GONE);
            } else {
                _payLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    /*-*****************************-*/
    /*-         Schedule            -*/
    /*-*****************************-*/
    private final View.OnClickListener _changeSchedule_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            ScheduleDialog.show(App.get(), DIALOG_UID_SCHEDULE, _schedule != null ? _schedule : _workOrder.getSchedule());
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final ScheduleView.OnRenderListener _schedule_onRender = new ScheduleView.OnRenderListener() {
        @Override
        public void onRender(boolean success) {
            if (!success) {
                ToastClient.toast(App.get(), "There was a problem with the schedule, please try setting it again", Toast.LENGTH_SHORT);
                _schedule = null;
                _scheduleLayout.setVisibility(View.GONE);
            } else {
                _scheduleLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private final ScheduleDialog.OnCompleteListener _scheduleDialog_onOk = new ScheduleDialog.OnCompleteListener() {
        @Override
        public void onComplete(Schedule schedule) {
            if (schedule != null) {
                _schedule = schedule;
                populateUi();
            }
        }
    };

    private final View.OnClickListener _schedule_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ScheduleDialog.show(App.get(), DIALOG_UID_SCHEDULE, _schedule != null ? _schedule : _workOrder.getSchedule());
        }
    };

    private final View.OnClickListener _scheduleMenu_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _schedulePopUp.show();
        }
    };

    private final PopupMenu.OnMenuItemClickListener _schedulePopUp_onClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.edit_menu) {
                ScheduleDialog.show(App.get(), DIALOG_UID_SCHEDULE, _schedule != null ? _schedule : _workOrder.getSchedule());
            } else if (item.getItemId() == R.id.delete_menu) {
                _schedule = null;
                populateUi();
            }
            return true;
        }
    };

    /*-*************************-*/
    /*-         Expense         -*/
    /*-*************************-*/

    private final View.OnClickListener _addExpense_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            ExpenseDialog.show(App.get(), DIALOG_UID_EXPENSE, false);
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };


    private final ExpenseDialog.OnOkListener _expenseDialog_onOk = new ExpenseDialog.OnOkListener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            Expense expense = null;
            if (_expenseMenuClickedView != null) {
                int index = (Integer) _expenseMenuClickedView.getTag();
                expense = _expenses.get(index);
                try {
                    expense.setAmount(amount);
                    expense.setDescription(description);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            } else {
                expense = new Expense(description, amount);
                _expenses.add(expense);
            }
            populateUi();
        }
    };

    private final TwoLineActionTile.OnActionClickListener _expense_onClick = new TwoLineActionTile.OnActionClickListener() {
        @Override
        public void onClick(View twoLineActionTile, View actionView) {
            _expenseMenuClickedView = twoLineActionTile;
            PopupMenu menu = new PopupMenu(getContext(), actionView);
            menu.inflate(R.menu.edit_delete);
            menu.setOnMenuItemClickListener(_expenseMenu_onClick);
            menu.setOnDismissListener(_expensesMenu_onDismess);
            menu.show();
        }
    };

    private final PopupMenu.OnMenuItemClickListener _expenseMenu_onClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int index = (Integer) _expenseMenuClickedView.getTag();
            Expense expense = _expenses.get(index);
            if (item.getItemId() == R.id.edit_menu) {
                ExpenseDialog.show(App.get(), DIALOG_UID_EXPENSE, expense.getDescription(), expense.getAmount(), false);
            } else if (item.getItemId() == R.id.delete_menu) {
                _expenses.remove(index);
                populateUi();
            }
            return true;
        }
    };

    private final PopupMenu.OnDismissListener _expensesMenu_onDismess = new PopupMenu.OnDismissListener() {
        @Override
        public void onDismiss(PopupMenu menu) {
            _expenseMenuClickedView = null;
        }
    };

    /*-*****************************-*/
    /*-         Expiration          -*/
    /*-*****************************-*/
    private final View.OnClickListener _addExpirationButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            ExpireDialog.show(getContext(), DIALOG_UID_EXPIRE);
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _expiresMenu_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _expiresPopUp.show();
        }
    };

    private final View.OnClickListener _expiresView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ExpireDialog.show(getContext(), DIALOG_UID_EXPIRE);
        }
    };

    private final PopupMenu.OnMenuItemClickListener _expiresPopUp_onClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.edit_menu) {
                ExpireDialog.show(getContext(), DIALOG_UID_EXPIRE);
            } else if (item.getItemId() == R.id.delete_menu) {
                _expiresMilliSeconds = -1;
                populateUi();
            }
            return true;
        }
    };

    private final ExpireDialog.OnOkListener _expireDialog_onOk = new ExpireDialog.OnOkListener() {
        @Override
        public void onOk(String title, int milliseconds) {
            _expiresMilliSeconds = milliseconds;
            _expiresTitle = title;
            populateUi();
        }
    };

    /*-*************************-*/
    /*-         Reason          -*/
    /*-*************************-*/
    private final View.OnClickListener _reasonsMenu_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _reasonPopUp.show();
        }
    };

    private final View.OnClickListener _reasonTextView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _reasonTextView.setVisibility(View.GONE);
            _reasonEditText.setVisibility(View.VISIBLE);
            _disclaimerTextView.setVisibility(View.VISIBLE);
        }
    };

    private View.OnFocusChangeListener _reasonEditText_onFocusChange = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                _reason = _reasonEditText.getText().toString();
                _reasonTextView.setText(_reason);
                _reasonTextView.setVisibility(View.VISIBLE);
                _reasonEditText.setVisibility(View.GONE);
                _disclaimerTextView.setVisibility(View.GONE);
            }
        }
    };

    private final TextWatcher _reasonEditText_onChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            _reason = editable.toString();
        }
    };

    private PopupMenu.OnMenuItemClickListener _reasonPopUp_onClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.edit_menu) {
                _reasonTextView.setVisibility(View.GONE);
                _reasonEditText.setVisibility(View.VISIBLE);
                _disclaimerTextView.setVisibility(View.VISIBLE);
            } else if (item.getItemId() == R.id.delete_menu) {
                _reasonLayout.setVisibility(View.GONE);
                _reason = null;
                _reasonEditText.setText("");
                _reasonTextView.setText("");
                _reasonTextView.setVisibility(View.GONE);
                _reasonEditText.setVisibility(View.VISIBLE);
                _disclaimerTextView.setVisibility(View.VISIBLE);
            }
            return false;
        }
    };

    private View.OnClickListener _addReasonButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _reasonLayout.setVisibility(View.VISIBLE);
            _reasonTextView.setVisibility(View.GONE);
            _reasonEditText.setVisibility(View.VISIBLE);
            _disclaimerTextView.setVisibility(View.VISIBLE);
            _bottomSheet_onCancel.onClick(view);
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

                    dismiss(true);

                    _refreshView.refreshComplete();
                }
            }
        }
    };

    public static void show(Context context, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable("workOrder", workOrder);

        Controller.show(context, null, CounterOfferDialog.class, params);
    }
}