package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.utils.misc;

public class ClosingNotesDialog extends DialogFragmentBase {
    private static final String TAG = "ClosingNotesDialog";

    // State
    private static final String STATE_NOTES = "ClosingNotesDialog:STATE_NOTES";

    // UI
    private EditText _editText;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private String _notes;
    private Listener _listener;

    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    public static ClosingNotesDialog getInstance(FragmentManager fm, String tag) {
        Log.v(TAG, "getInstance");
        return getInstance(fm, tag, ClosingNotesDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_NOTES))
                _notes = savedInstanceState.getString(STATE_NOTES);
        }

        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_NOTES))
                _notes = savedInstanceState.getString(STATE_NOTES);
        }

        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_editText != null && !misc.isEmptyOrNull(_editText.getText().toString())) {
            _notes = _editText.getText().toString();
            outState.putString(STATE_NOTES, _notes);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_closing_notes, container, false);

        _editText = (EditText) v.findViewById(R.id.notes_edittext);
        _editText.setOnEditorActionListener(_onEditor_listener);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void reset() {
        Log.v(TAG, "reset");
        super.reset();
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        if (!misc.isEmptyOrNull(_notes))
            _editText.setText(_notes);
        else
            _editText.setText("");
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(String notes) {
        Log.v(TAG, "show");
        _notes = notes;
        super.show();
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final TextView.OnEditorActionListener _onEditor_listener = new TextView.OnEditorActionListener() {

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

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onOk(_editText.getText().toString());
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onCancel();
            }
        }
    };

    public interface Listener {
        void onOk(String message);

        void onCancel();
    }
}
