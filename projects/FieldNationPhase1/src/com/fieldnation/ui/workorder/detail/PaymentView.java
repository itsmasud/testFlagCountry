package com.fieldnation.ui.workorder.detail;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.dialog.DiscountDialog;
import com.fieldnation.ui.dialog.ExpenseDialog;
import com.fieldnation.ui.dialog.PayDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.PaymentView";

	private static final int RESULT_ADD_EXPENSE = 1;
	private static final int RESULT_DELETE_EXPENSE = 2;
	private static final int RESULT_ADD_DISCOUNT = 3;
	private static final int RESULT_DELETE_DISCOUNT = 4;

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
	private GlobalState _gs;
	private WorkorderService _service;

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
		_gs = (GlobalState) getContext().getApplicationContext();
		_gs.requestAuthentication(_authClient);

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
			_expenseDialog.show("Add Expense", "", 0.0, _addExpense_listener);
		}
	};

	private ExpenseDialog.Listener _addExpense_listener = new ExpenseDialog.Listener() {
		@Override
		public void onOk(String description, double amount, ExpenseCategory category) {
			getContext().startService(
					_service.addExpense(RESULT_ADD_EXPENSE, _workorder.getWorkorderId(), description, amount, category));
		}
	};

	private ExpenseView.Listener _expenseView_listener = new ExpenseView.Listener() {

		@Override
		public void onDelete(ExpenseView view, AdditionalExpense expense) {
			getContext().startService(
					_service.deleteExpense(RESULT_DELETE_EXPENSE, _workorder.getWorkorderId(), expense.getExpenseId()));
		}
	};

	private View.OnClickListener _addDiscount_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_discountDialog.show("Add Discount", "", 0.0, _addDiscount_listener);
		}
	};

	private DiscountDialog.Listener _addDiscount_listener = new DiscountDialog.Listener() {
		@Override
		public void onOk(String description, double amount) {
			getContext().startService(
					_service.addDiscount(RESULT_ADD_DISCOUNT, _workorder.getWorkorderId(), amount, description));
		}
	};

	private DiscountView.Listener _discount_listener = new DiscountView.Listener() {
		@Override
		public void onDelete(Discount discount) {
			getContext().startService(
					_service.deleteDiscount(RESULT_DELETE_DISCOUNT, _workorder.getWorkorderId(),
							discount.getDiscountId()));
		}
	};
	private AuthenticationClient _authClient = new AuthenticationClient() {

		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getContext(), username, authToken, _resultReceiver);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			if (resultCode == RESULT_ADD_EXPENSE || resultCode == RESULT_DELETE_EXPENSE || resultCode == RESULT_ADD_DISCOUNT || resultCode == RESULT_DELETE_DISCOUNT) {
				_workorder.dispatchOnChange();
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authClient);
			// TODO, toast failure, put ui in wait mode
		}
	};

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

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
		_expensesLabelTextView.setVisibility(VISIBLE);
		_expensesLinearLayout.setVisibility(VISIBLE);
		_expensesLinearLayout.removeAllViews();

		if (expenses != null && expenses.length > 0) {
			for (int i = 0; i < expenses.length; i++) {
				AdditionalExpense expense = expenses[i];
				ExpenseView v = new ExpenseView(getContext());
				v.setListener(_expenseView_listener);
				_expensesLinearLayout.addView(v);
				v.setAdditionalExpense(expense);
			}
		}

		Discount[] discounts = _workorder.getDiscounts();
		_discountsLabelTextView.setVisibility(VISIBLE);
		_discountsLinearLayout.setVisibility(VISIBLE);
		_discountsLinearLayout.removeAllViews();

		if (discounts != null && discounts.length > 0) {
			for (int i = 0; i < discounts.length; i++) {
				Discount discount = discounts[i];
				DiscountView v = new DiscountView(getContext());
				v.setListener(_discount_listener);
				_discountsLinearLayout.addView(v);
				v.setDiscount(discount);
			}
		}

	}

}
