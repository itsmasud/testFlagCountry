package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.dialog.DiscountDialog;
import com.fieldnation.ui.dialog.ExpenseDialog;

import java.util.Arrays;

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
    private ExpenseDialog _expenseDialog;
    private LinearLayout _counterOfferLayout;
    private LinearLayout _detailLayout;
    private DiscountDialog _discountDialog;

    // Data
    private Workorder _workorder;
    private Listener _listener;
    private Integer[] woStatus = {5, 6, 7}; //work order status approved, paid, canceled

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

        _expenseDialog = new ExpenseDialog(getContext());
        _counterOfferLayout = (LinearLayout) findViewById(R.id.counteroffer_layout);
        _counterOfferLayout.setOnClickListener(_counterOffer_onClick);
        _detailLayout = (LinearLayout) findViewById(R.id.detail_layout);

        _discountDialog = new DiscountDialog(getContext());
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
    public void setWorkorder(Workorder workorder) {
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

        AdditionalExpense[] expenses = _workorder.getAdditionalExpenses();

        if (expenses != null && expenses.length > 0) {
            _expensesLabelTextView.setVisibility(VISIBLE);
            _expensesLinearLayout.setVisibility(VISIBLE);
            _expensesLinearLayout.removeAllViews();
            for (int i = 0; i < expenses.length; i++) {
                AdditionalExpense expense = expenses[i];
                ExpenseView v = new ExpenseView(getContext());
                v.setListener(_expenseView_listener);
                _expensesLinearLayout.addView(v);
                v.setAdditionalExpense(expense, i + 1);
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

        if (_workorder.canCounterOffer()) {
            _counterOfferLayout.setVisibility(View.VISIBLE);
        } else {
            _counterOfferLayout.setVisibility(View.GONE);
        }
        setVisibility(View.VISIBLE);
    }

    /*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
    private View.OnClickListener _counterOffer_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), CounterOfferActivity.class);
            intent.putExtra(CounterOfferActivity.INTENT_WORKORDER, _workorder);
            getContext().startActivity(intent);
        }
    };

    private View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Method Stub: onClick()
            Log.v(TAG, "Method Stub: onClick()");
        }
    };

    private View.OnClickListener _addExpense_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                _expenseDialog.show("Add Expense", _addExpense_listener);
            }
        }
    };

    private ExpenseDialog.Listener _addExpense_listener = new ExpenseDialog.Listener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            if (_listener != null)
                _listener.onAddExpense(_workorder, description, amount, category);
        }

        @Override
        public void onCancel() {
            // TODO Method Stub: onCancel()
            Log.v(TAG, "Method Stub: onCancel()");
        }
    };

    private ExpenseView.Listener _expenseView_listener = new ExpenseView.Listener() {

        @Override
        public void onDelete(ExpenseView view, AdditionalExpense expense) {
            if (_listener != null)
                _listener.onDeleteExpense(_workorder, expense);
        }
    };

    private View.OnClickListener _addDiscount_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                _discountDialog.show("Add Discount", _addDiscount_listener);
            }
        }
    };

    private DiscountDialog.Listener _addDiscount_listener = new DiscountDialog.Listener() {
        @Override
        public void onOk(String description, double amount) {
            if (_listener != null)
                _listener.onAddDiscount(_workorder, amount, description);
        }

        @Override
        public void onCacnel() {
        }
    };

    private DiscountView.Listener _discount_listener = new DiscountView.Listener() {
        @Override
        public void onDelete(Discount discount) {
            if (_listener != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                _listener.onDeleteDiscount(_workorder, discount.getDiscountId());
            }
        }
    };

    public interface Listener {
        public void onDeleteDiscount(Workorder workorder, int discountId);

        public void onAddDiscount(Workorder workorder, Double amount, String description);

        public void onDeleteExpense(Workorder workorder, AdditionalExpense expense);

        public void onAddExpense(Workorder workorder, String description, double amount, ExpenseCategory category);
    }
}
