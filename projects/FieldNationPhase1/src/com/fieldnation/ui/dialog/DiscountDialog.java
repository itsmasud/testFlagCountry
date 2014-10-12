package com.fieldnation.ui.dialog;

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

public class DiscountDialog extends Dialog {
	private static String TAG = "ui.DiscountDialog";

	// UI
	private Button _okButton;
	private Button _cancelButton;
	private EditText _amountEditText;
	private EditText _descriptionEditText;

	// Data
	private Listener _listener;

	public DiscountDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_discount);

		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_okButton_onClick);
		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancelButton_onClick);
		_amountEditText = (EditText) findViewById(R.id.amount_edittext);
		_descriptionEditText = (EditText) findViewById(R.id.description_edittext);
		_descriptionEditText.setOnEditorActionListener(_oneditor_listener);

		setTitle("Add Discount");
	}

	public void show(String title, Listener listener) {
		_listener = listener;

		setTitle(title);
		show();
	}

	private String getDescription() {
		return _descriptionEditText.getText().toString();
	}

	private Double getAmount() {
		return Double.parseDouble(_amountEditText.getText().toString());
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

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
			DiscountDialog.this.dismiss();
			if (_listener != null)
				_listener.onOk(getDescription(), getAmount());
		}
	};

	private View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener != null)
				_listener.onCacnel();
		}
	};

	public interface Listener {
		public void onOk(String description, double amount);

		public void onCacnel();
	}
}
