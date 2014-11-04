package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import com.fieldnation.R;
import com.fieldnation.utils.misc;

import java.util.List;

public class ClosingNotesDialog extends DialogFragment {
    private static final String TAG = "ui.dialog.ClosingNotesDialog";

    // UI
    private EditText _editText;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private String _notes;
    private Listener _listener;
    private FragmentManager _fm;

    public static ClosingNotesDialog getInstance(FragmentManager fm, String tag) {
        ClosingNotesDialog d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (frag instanceof ClosingNotesDialog && frag.getTag().equals(tag)) {
                    d = (ClosingNotesDialog) frag;
                    break;
                }
            }
        }
        if (d == null)
            d = new ClosingNotesDialog();
        d._fm = fm;
        return d;
    }


    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_closing_notes, container, false);

        _editText = (EditText) v.findViewById(R.id.notes_edittext);
        _editText.setOnEditorActionListener(_onEditor_listener);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().setTitle(R.string.closing_notes);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(String tag, String notes, Listener listener) {
        if (!misc.isEmptyOrNull(notes)) {
            _notes = notes;
            populateUi();
        }
        _listener = listener;
        show(_fm, tag);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    private void populateUi() {
        if (_editText == null)
            return;
        if (_notes == null)
            return;

        if (!misc.isEmptyOrNull(_notes))
            _editText.setText(_notes);
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
            if (_listener != null) {
                _listener.onCancel();
            }
        }
    };

    public interface Listener {
        public void onOk(String message);

        public void onCancel();

    }
}
