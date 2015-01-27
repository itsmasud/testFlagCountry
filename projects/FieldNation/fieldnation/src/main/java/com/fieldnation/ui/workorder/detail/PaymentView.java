package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;

public class PaymentView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.PaymentView";

    // UI
    // TODO need to grab the description views at the top
    private TextView _pay1TextView;
    private TextView _pay2TextView;
    private TextView _termsTextView;
    private LinearLayout _addExpenseLayout;
    private LinearLayout _addDiscountLayout;
    private TextView _expensesLabelTextView;
    private LinearLayout _expensesLinearLayout;
    private TextView _discountsLabelTextView;
    private LinearLayout _discountsLinearLayout;
    private LinearLayout _counterOfferLayout;
    private LinearLayout _detailLayout;
    private TextView _counterOfferTextView;
    private TextView _co1TextView;
    private TextView _co2TextView;

    // Data
    private Workorder _workorder;
    private Listener _listener;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public PaymentView(Context context) {
        super(context);
        init();
    }

    public PaymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_payment, this);

        if (isInEditMode())
            return;

        _pay1TextView = (TextView) findViewById(R.id.pay1_textview);
        _pay2TextView = (TextView) findViewById(R.id.pay2_textview);
        _termsTextView = (TextView) findViewById(R.id.terms_textview);
        _termsTextView.setOnClickListener(_terms_onClick);
        _addExpenseLayout = (LinearLayout) findViewById(R.id.addexpense_layout);
        _addExpenseLayout.setOnClickListener(_addExpense_onClick);

        _addDiscountLayout = (LinearLayout) findViewById(R.id.adddiscount_layout);
        _addDiscountLayout.setOnClickListener(_addDiscount_onClick);

        _expensesLabelTextView = (TextView) findViewById(R.id.expenseslabel_textview);
        _expensesLinearLayout = (LinearLayout) findViewById(R.id.expenses_linearlayout);
        _discountsLabelTextView = (TextView) findViewById(R.id.discountslabel_textview);
        _discountsLinearLayout = (LinearLayout) findViewById(R.id.discounts_linearlayout);

        _counterOfferLayout = (LinearLayout) findViewById(R.id.counteroffer_layout);
        _counterOfferLayout.setOnClickListener(_counterOffer_onClick);
        _detailLayout = (LinearLayout) findViewById(R.id.detail_layout);

        _counterOfferTextView = (TextView) findViewById(R.id.counteroffer_textview);
        _co1TextView = (TextView) findViewById(R.id.co1_textview);
        _co2TextView = (TextView) findViewById(R.id.co2_textview);

        setVisibility(View.GONE);
    }

	/*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void showDetails(boolean enabled) {
        if (enabled) {
            _detailLayout.setVisibility(View.VISIBLE);
        } else {
            _detailLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {

        Pay pay = _workorder.getPay();
        if (pay != null) {
            String[] paytext = pay.toDisplayStringLong();

            if (paytext[0] != null) {
                _pay1TextView.setText(paytext[0]);
                _pay1TextView.setVisibility(VISIBLE);
            } else {
                _pay1TextView.setVisibility(GONE);
            }

            if (paytext[1] != null) {
                _pay2TextView.setText(paytext[1]);
                _pay2TextView.setVisibility(VISIBLE);
            } else {
                _pay2TextView.setVisibility(GONE);
            }
        } else {
            _pay1TextView.setVisibility(GONE);
            _pay2TextView.setVisibility(GONE);
        }

        if (_workorder.getCounterOfferInfo() != null && _workorder.getCounterOfferInfo().getPay() != null) {
            Pay co = _workorder.getCounterOfferInfo().getPay();
            String[] paytext = co.toDisplayStringLong();
            _counterOfferTextView.setVisibility(View.GONE);

            if (paytext[0] != null) {
                _co1TextView.setText(paytext[0]);
                _co1TextView.setVisibility(VISIBLE);
                _counterOfferTextView.setVisibility(View.VISIBLE);
            } else {
                _co1TextView.setVisibility(GONE);
            }

            if (paytext[1] != null) {
                _co2TextView.setText(paytext[1]);
                _co2TextView.setVisibility(VISIBLE);
                _counterOfferTextView.setVisibility(View.VISIBLE);
            } else {
                _co2TextView.setVisibility(GONE);
            }
        } else {
            _counterOfferTextView.setVisibility(View.GONE);
            _co1TextView.setVisibility(GONE);
            _co2TextView.setVisibility(GONE);
        }


        if (_workorder.getStatus().getWorkorderStatus() == WorkorderStatus.AVAILABLE) {
            _detailLayout.setVisibility(View.GONE);
        } else {
            _detailLayout.setVisibility(View.VISIBLE);

            Expense[] expenses = _workorder.getAdditionalExpenses();

            if (expenses != null && expenses.length > 0) {
                _expensesLabelTextView.setVisibility(VISIBLE);
                _expensesLinearLayout.setVisibility(VISIBLE);
                _expensesLinearLayout.removeAllViews();
                for (int i = 0; i < expenses.length; i++) {
                    Expense expense = expenses[i];
                    ExpenseView v = new ExpenseView(getContext());
                    v.setListener(_expenseView_listener);
                    _expensesLinearLayout.addView(v);
                    v.setData(_workorder, expense);
                }
            } else {
                _expensesLabelTextView.setVisibility(View.GONE);
                _expensesLinearLayout.setVisibility(View.GONE);
            }

            Discount[] discounts = _workorder.getDiscounts();

            if (discounts != null && discounts.length > 0) {
                _discountsLabelTextView.setVisibility(VISIBLE);
                _discountsLinearLayout.setVisibility(VISIBLE);
                _discountsLinearLayout.removeAllViews();
                for (int i = 0; i < discounts.length; i++) {
                    Discount discount = discounts[i];
                    DiscountView v = new DiscountView(getContext());
                    v.setListener(_discount_listener);
                    _discountsLinearLayout.addView(v);
                    v.setDiscount(discount);
                }
            } else {
                _discountsLabelTextView.setVisibility(View.GONE);
                _discountsLinearLayout.setVisibility(View.GONE);
            }
        }

        if (_workorder.canCounterOffer()) {
            _counterOfferLayout.setVisibility(View.VISIBLE);
        } else {
            _counterOfferLayout.setVisibility(View.GONE);
        }
        setVisibility(View.VISIBLE);

        if (!_workorder.canCounterOffer())
            _counterOfferLayout.setVisibility(View.GONE);
        else
            _counterOfferLayout.setVisibility(View.VISIBLE);


        if (!_workorder.canChangeExpenses())
            _addExpenseLayout.setVisibility(View.GONE);
        else
            _addExpenseLayout.setVisibility(View.VISIBLE);

        if (!_workorder.canChangeDiscounts())
            _addDiscountLayout.setVisibility(View.GONE);
        else
            _addDiscountLayout.setVisibility(View.VISIBLE);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private View.OnClickListener _counterOffer_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onShowCounterOfferDialog();
        }
    };

    private View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onShowTerms();
        }
    };

    private View.OnClickListener _addExpense_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _listener.onShowAddExpenseDialog();
        }
    };

    private ExpenseView.Listener _expenseView_listener = new ExpenseView.Listener() {

        @Override
        public void onDelete(ExpenseView view, Expense expense) {
            if (_workorder.canChangeExpenses()) {
                if (_listener != null)
                    _listener.onDeleteExpense(_workorder, expense);
            }
        }
    };

    private View.OnClickListener _addDiscount_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onShowAddDiscountDialog();
        }
    };

    private DiscountView.Listener _discount_listener = new DiscountView.Listener() {
        @Override
        public void onDelete(Discount discount) {
            if (_workorder.canChangeDiscounts()) {
                _listener.onDeleteDiscount(_workorder, discount.getDiscountId());
            }
        }
    };

    public interface Listener {
        public void onDeleteDiscount(Workorder workorder, int discountId);

        public void onDeleteExpense(Workorder workorder, Expense expense);

        public void onShowAddDiscountDialog();

        public void onShowAddExpenseDialog();

        public void onShowCounterOfferDialog();

        public void onShowTerms();
    }
}
