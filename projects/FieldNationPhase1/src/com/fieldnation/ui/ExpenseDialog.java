package com.fieldnation.ui;

import com.fieldnation.R;
import com.fieldnation.data.workorder.AdditionalExpense;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ExpenseDialog extends Dialog {
	private static String TAG = "ui.ExpenseDialog";
	private Button _okButton;
	private EditText _amountEditText;
	private EditText _descriptionEditText;

	private Listener _listener;

	public ExpenseDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_expense);

		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_okButton_onClick);
		_amountEditText = (EditText) findViewById(R.id.amount_edittext);
		_descriptionEditText = (EditText) findViewById(R.id.description_edittext);

	}

	private View.OnClickListener _okButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_listener.onOk(getDescription(), getAmount());
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

	public interface Listener {
		public void onOk(String description, double amount);
	}
}
