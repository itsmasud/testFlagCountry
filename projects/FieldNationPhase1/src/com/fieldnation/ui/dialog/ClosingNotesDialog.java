package com.fieldnation.ui.dialog;

import com.fieldnation.R;
import com.fieldnation.utils.misc;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClosingNotesDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.ClosingNotesDialog";

	// UI
	private EditText _editText;
	private Button _okButton;
	private Button _cancelButton;

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

		_editText = (EditText) findViewById(R.id.notes_edittext);
		_editText.setOnEditorActionListener(_onEditor_listener);
		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_ok_onClick);
		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancel_onClick);

		setTitle(R.string.closing_notes);
	}

	public void show(String notes, Listener listener) {
		if (!misc.isEmptyOrNull(notes)) {
			_editText.setText(notes);
		}
		_listener = listener;
		show();
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private TextView.OnEditorActionListener _onEditor_listener = new TextView.OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			boolean handled = false;
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				_ok_onClick.onClick(null);
				handled = true;
			}
			return handled;
		}
	};

	private View.OnClickListener _ok_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener != null) {
				_listener.onOk(_editText.getText().toString());
			}
		}
	};

	private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener != null)
				_listener.onCancel();
		}
	};

	public interface Listener {
		public void onOk(String message);

		public void onCancel();
	}
}
