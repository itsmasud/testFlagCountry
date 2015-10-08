package com.fieldnation.ui.dialog;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.widget.EditText;

public class EditTextAlertDialog extends Builder {

	private final EditText _editText;

	public EditTextAlertDialog(Context context, String title, String message) {
		super(context);
		setTitle(title);
		setMessage(message);

		_editText = new EditText(context);
		setView(_editText);
	}

	public String getInput() {
		return _editText.getText().toString();
	}

}
