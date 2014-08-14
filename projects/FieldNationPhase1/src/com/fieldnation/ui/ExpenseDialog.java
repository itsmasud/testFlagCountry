package com.fieldnation.ui;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.data.workorder.ExpenseCategory;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ExpenseDialog extends Dialog {
	private static String TAG = "ui.ExpenseDialog";
	private Button _okButton;
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
		_categorySpinner = (Spinner) findViewById(R.id.category_spinner);
		ExpenseCategories.getInstance(getContext()).setListener(_categoriesListener);
	}

	private ExpenseCategories.Listener _categoriesListener = new ExpenseCategories.Listener() {
		@Override
		public void onHaveCategories(ExpenseCategory[] categories) {
			_categories = categories;

			_adapter = new ArrayAdapter<ExpenseCategory>(getContext(), android.R.layout.simple_spinner_dropdown_item,
					categories);

			_categorySpinner.setAdapter(_adapter);
		}
	};

	private View.OnClickListener _okButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_listener.onOk(getDescription(), getAmount(), getCategory());
			ExpenseDialog.this.dismiss();
		}
	};

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

	public interface Listener {
		public void onOk(String description, double amount, ExpenseCategory category);
	}
}
