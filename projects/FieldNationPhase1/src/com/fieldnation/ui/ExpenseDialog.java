package com.fieldnation.ui;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.data.workorder.ExpenseCategory;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ExpenseDialog extends Dialog {
	private static String TAG = "ui.ExpenseDialog";
	private Button _okButton;
	private Button _cancelButton;
	private EditText _amountEditText;
	private EditText _descriptionEditText;
	private Spinner _categorySpinner;
	private Listener _listener;
	private ExpenseCategory[] _categories;
	private ArrayAdapter<ExpenseCategory> _adapter;

	public ExpenseDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_expense);

		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_okButton_onClick);
		_amountEditText = (EditText) findViewById(R.id.amount_edittext);
		_descriptionEditText = (EditText) findViewById(R.id.description_edittext);
		_descriptionEditText.setOnEditorActionListener(_oneditor_listener);
		_categorySpinner = (Spinner) findViewById(R.id.category_spinner);
		_cancelButton.setOnClickListener(_cancelButton_onClick);
		ExpenseCategories.getInstance(getContext()).setListener(_categoriesListener);
		setTitle("Add Expense");
	}

	public void show(String title, String description, double amount, Listener listener) {
		_listener = listener;

		setTitle(title);
		_amountEditText.setText(amount + "");
		_descriptionEditText.setText(description);
		show();
	}

	public String getDescription() {
		return _descriptionEditText.getText().toString();
	}

	public Double getAmount() {
		return Double.parseDouble(_amountEditText.getText().toString());
	}

	public ExpenseCategory getCategory() {
		return _adapter.getItem(_categorySpinner.getSelectedItemPosition());
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			ExpenseDialog.this.dismiss();
		}
	};
	private ExpenseCategories.Listener _categoriesListener = new ExpenseCategories.Listener() {
		@Override
		public void onHaveCategories(ExpenseCategory[] categories) {
			_categories = categories;

			_adapter = new ArrayAdapter<ExpenseCategory>(getContext(), android.R.layout.simple_spinner_dropdown_item,
					categories);

			_categorySpinner.setAdapter(_adapter);
		}
	};

	private TextView.OnEditorActionListener _oneditor_listener = new TextView.OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			boolean handled = false;
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				_okButton_onClick.onClick(null);
				handled = true;
			}
			return handled;
		}
	};

	private View.OnClickListener _okButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_listener.onOk(getDescription(), getAmount(), getCategory());
			ExpenseDialog.this.dismiss();
		}
	};

	public interface Listener {
		public void onOk(String description, double amount, ExpenseCategory category);
	}
}
