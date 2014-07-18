package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.PaymentView";

	// UI
	// TODO need to grab the description views at the top
	private TextView _pay1TextView;
	private TextView _pay2TextView;
	private Button _termsButton;
	private LinearLayout _addExpenseLayout;
	private LinearLayout _addDiscountLayout;
	private TextView _expensesLabelTextView;
	private LinearLayout _expensesLinearLayout;
	private TextView _discountsLabelTextView;
	private LinearLayout _discountsLinearLayout;

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public PaymentView(Context context) {
		this(context, null);
	}

	public PaymentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_payment, this);

		if (isInEditMode())
			return;

		_pay1TextView = (TextView) findViewById(R.id.pay1_textview);
		_pay2TextView = (TextView) findViewById(R.id.pay2_textview);
		_termsButton = (Button) findViewById(R.id.terms_button);
		_addExpenseLayout = (LinearLayout) findViewById(R.id.addexpense_layout);
		_addDiscountLayout = (LinearLayout) findViewById(R.id.adddiscount_layout);
		_expensesLabelTextView = (TextView) findViewById(R.id.expenseslabel_textview);
		_expensesLinearLayout = (LinearLayout) findViewById(R.id.expenses_linearlayout);
		_discountsLabelTextView = (TextView) findViewById(R.id.discountslabel_textview);
		_discountsLinearLayout = (LinearLayout) findViewById(R.id.discounts_linearlayout);

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _terms_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

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
		if (expenses == null || expenses.length == 0) {
			_expensesLabelTextView.setVisibility(GONE);
			_expensesLinearLayout.setVisibility(GONE);
		} else {
			_expensesLabelTextView.setVisibility(VISIBLE);
			_expensesLinearLayout.setVisibility(VISIBLE);
			_expensesLinearLayout.removeAllViews();

			for (int i = 0; i < expenses.length; i++) {
				AdditionalExpense expense = expenses[i];
				ExpenseView v = new ExpenseView(getContext());
				_expensesLinearLayout.addView(v);
				v.setAdditionalExpense(expense);
			}
		}

		Discount[] discounts = _workorder.getDiscounts();
		if (discounts == null || discounts.length == 0) {
			_discountsLabelTextView.setVisibility(GONE);
			_discountsLinearLayout.setVisibility(GONE);
		} else {
			_discountsLabelTextView.setVisibility(VISIBLE);
			_discountsLinearLayout.setVisibility(VISIBLE);
			_discountsLinearLayout.removeAllViews();

			for (int i = 0; i < discounts.length; i++) {
				Discount discount = discounts[i];
				DiscountView v = new DiscountView(getContext());
				_discountsLinearLayout.addView(v);
				v.setDiscount(discount);
			}
		}

	}

}
