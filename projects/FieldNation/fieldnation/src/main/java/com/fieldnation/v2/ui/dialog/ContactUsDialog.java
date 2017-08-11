package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.help.HelpClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

/**
 * Created by mc on 8/11/17.
 */

public class ContactUsDialog extends FullScreenDialog {
    private static final String TAG = "ContactUsDialog";

    // State
    private static final String STATE_MESSAGE = "ContactUsDialog:Message";
    private static final String STATE_SOURCE = "ContactUsDialog:Source";
    private static final String STATE_SPINNER_SELECTION = "ContactUsDialog:SpinnerSelection";

    // Spinner-case
    private final static int ITEM_APP_FEEDBACK = 0;
    private final static int ITEM_HELP_SUPPORT = 1;

    // Ui
    private HintSpinner _reasonSpinner;
    private EditText _explanationEditText;
    private TextView _additionalHelpTextView;
    private Button _sendButton;
    private Button _cancelButton;

    // Data
    private String _message;
    private String _source;
    private int _spinnerPosition = -1;
    private String _internalTeamParam;
    private boolean _clear = false;

    /*-*********----------**********-*/
    /*-         Life Cycle          -*/
    /*-*********----------**********-*/
    public ContactUsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_contact_us, container, false);

        _reasonSpinner = v.findViewById(R.id.reason_spinner);
        _explanationEditText = v.findViewById(R.id.explanation_edittext);
        _additionalHelpTextView = v.findViewById(R.id.additionalHelp_textview);
        _sendButton = v.findViewById(R.id.send_button);
        _cancelButton = v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _reasonSpinner.setOnItemSelectedListener(_reasonSpinner_onItemClick);
        _explanationEditText.addTextChangedListener(_textEditText_watcherListener);
        _sendButton.setOnClickListener(_send_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateSpinners();
        if (_clear) {
            _reasonSpinner.clearSelection();
            _explanationEditText.setText("");
            _explanationEditText.setHint(R.string.dialog_explanation_default);
            _reasonSpinner_onItemClick.onItemSelected(null, null, -1, 0);
            _clear = false;
            return;
        }

        if (!misc.isEmptyOrNull(_message))
            _explanationEditText.setText(_message);

        _textEditText_watcherListener.onTextChanged(
                _explanationEditText.getText().toString(),
                0, _explanationEditText.getText().toString().length(),
                _explanationEditText.getText().toString().length());
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _source = params.getString("source");
        super.show(params, animate);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);

        if (savedState != null) {
            if (savedState.containsKey(STATE_MESSAGE))
                _message = savedState.getString(STATE_MESSAGE);
            if (savedState.containsKey(STATE_SOURCE))
                _source = savedState.getString(STATE_SOURCE);
            if (savedState.containsKey(STATE_SPINNER_SELECTION)) {
                populateSpinners();
                _spinnerPosition = savedState.getInt(STATE_SPINNER_SELECTION);
                _reasonSpinner.setSelection(_spinnerPosition);
                onSpinnerSelection(_spinnerPosition);
            }
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_explanationEditText != null && !misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
            _message = _explanationEditText.getText().toString();
            outState.putString(STATE_MESSAGE, _message);
        }
        if (!misc.isEmptyOrNull(_source))
            outState.putString(STATE_SOURCE, _source);

        if (_spinnerPosition != -1)
            outState.putInt(STATE_SPINNER_SELECTION, _spinnerPosition);

        super.onSaveDialogState(outState);
    }


    private void onSpinnerSelection(int position) {
        _spinnerPosition = position;
        switch (position) {
            case ITEM_APP_FEEDBACK:
                _internalTeamParam = "feedback";
                _additionalHelpTextView.setVisibility(View.GONE);
                break;

            case ITEM_HELP_SUPPORT:
                _internalTeamParam = "support";
                _additionalHelpTextView.setVisibility(View.VISIBLE);
                break;
        }
        if (position != -1)
            _explanationEditText.requestFocus();

        _textEditText_watcherListener.onTextChanged(
                _explanationEditText.getText().toString(),
                0, _explanationEditText.getText().toString().length(),
                _explanationEditText.getText().toString().length());
    }

    private HintSpinner populateSpinners() {
        if (_reasonSpinner != null && _reasonSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    getContext(),
                    R.array.reason_list,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _reasonSpinner.setAdapter(adapter);
        }
        return _reasonSpinner;
    }

    private final TextWatcher _textEditText_watcherListener = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (_spinnerPosition < 0) return;
            if (_explanationEditText.getText().toString().trim().length() > 0) {
                _sendButton.setEnabled(true);
            } else {
                _sendButton.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private final AdapterView.OnItemSelectedListener _reasonSpinner_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            onSpinnerSelection(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final View.OnClickListener _send_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
                ToastClient.toast(App.get(), R.string.toast_empty_feedback, Toast.LENGTH_SHORT);
                return;
            }

            dismiss(true);
            _onOkDispatcher.dispatch(getUid(), _explanationEditText.getText().toString());

            try {
                HelpClient.sendContactUsFeedback(App.get(), _explanationEditText.getText().toString(), _internalTeamParam, _source, "Version " +
                        (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim(), null);
            } catch (Exception ex) {
                HelpClient.sendContactUsFeedback(App.get(), _explanationEditText.getText().toString(), _internalTeamParam, null, "Version Unknown", null);
            }

        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
        }
    };


    public static void show(Context context, String uid, String source) {
        Bundle params = new Bundle();
        params.putString("source", source);
        Controller.show(context, uid, ContactListDialog.class, params);
    }

    /*-*********************-*/
    /*-         Ok          -*/
    /*-*********************-*/
    public interface OnOkListener {
        void onOk(String message);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((String) parameters[0]);
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }

    /*-*************************-*/
    /*-         Cancel          -*/
    /*-*************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }

}
