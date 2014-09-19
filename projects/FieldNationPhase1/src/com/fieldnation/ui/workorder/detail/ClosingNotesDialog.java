package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.utils.misc;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ClosingNotesDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.ClosingNotesDialog";

	// UI
	private EditText _edit_text;
	private Button _save_button;

	// Data
	private Listener _listener;

	/*-*****************************-*/
	/*-			Life Cycle			-*/
	/*-*****************************-*/
	public ClosingNotesDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	protected ClosingNotesDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public ClosingNotesDialog(Context context) {
		super(context);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_closing_notes);

		_edit_text = (EditText) findViewById(R.id.notes_edittext);
		_save_button = (Button) findViewById(R.id.save_button);
		_save_button.setOnClickListener(_save_onClick);

		setTitle(R.string.closing_notes);
	}

	public void show(String notes, Listener listener) {
		if (!misc.isEmptyOrNull(notes)) {
			_edit_text.setText(notes);
		}
		_listener = listener;
		show();
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private View.OnClickListener _save_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_listener != null) {
				_listener.onOk(_edit_text.getText().toString());
			}
			ClosingNotesDialog.this.dismiss();
		}
	};

	public interface Listener {
		public void onOk(String message);
	}
}
