package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.CustomEvent;
import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.Debug;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.data.help.HelpClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.FnSpinner;
import com.fieldnation.utils.misc;

/**
 * Created by Shoaib on 4/27/2016.
 */
public class ContactUsDialog extends DialogFragmentBase {
    private static final String TAG = "ContactUsDialog";

    // State
    private static final String STATE_MESSAGE = "ContactUsDialog:Message";
    private static final String STATE_SOURCE = "ContactUsDialog:Source";
    private static final String STATE_SPINNER_SELECTION = "ContactUsDialog:SpinnerSelection";

    // Spinner-case
    private final static int ITEM_APP_FEEDBACK = 0;
    private final static int ITEM_WORK_ORDER_ISSUES = 1;
    private final static int ITEM_ACCOUNT_ISSUE = 2;

    // Ui
    private FnSpinner _reasonSpinnre;
    private EditText _explanationEditText;
    private TextView _additionalHelpTextView;
    private Button _sendButton;
    private Button _cancelButton;

    // Data
    private String _message;
    private Listener _listener;
    private String _source;
    private int _spinnerPosition = -1;
    private String _internalTeamParam;
    private boolean _clear = false;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static ContactUsDialog getInstance(FragmentManager fm, String tag) {
        Log.v(TAG, "getInstance");
        return getInstance(fm, tag, ContactUsDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_MESSAGE))
                _message = savedInstanceState.getString(STATE_MESSAGE);
            if (savedInstanceState.containsKey(STATE_SOURCE))
                _source = savedInstanceState.getString(STATE_SOURCE);
            if (savedInstanceState.containsKey(STATE_SPINNER_SELECTION))
                _spinnerPosition = savedInstanceState.getInt(STATE_SPINNER_SELECTION);
        }
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_MESSAGE))
                _message = savedInstanceState.getString(STATE_MESSAGE);
            if (savedInstanceState.containsKey(STATE_SOURCE))
                _source = savedInstanceState.getString(STATE_SOURCE);
            if (savedInstanceState.containsKey(STATE_SPINNER_SELECTION))
                _spinnerPosition = savedInstanceState.getInt(STATE_SPINNER_SELECTION);
        }else{
            _clear = true;
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_explanationEditText != null && !misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
            _message = _explanationEditText.getText().toString();
            outState.putString(STATE_MESSAGE, _message);
        }
        if (!misc.isEmptyOrNull(_source))
            outState.putString(STATE_SOURCE, _source);
        if (_spinnerPosition != -1)
            outState.putInt(STATE_SPINNER_SELECTION, _spinnerPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_contact_us, container, false);

        _reasonSpinnre = (FnSpinner) v.findViewById(R.id.reason_spinner);
        _reasonSpinnre.setOnItemClickListener(_reasonSpinner_onItemClick);
        _explanationEditText = (EditText) v.findViewById(R.id.explanation_edittext);
        _explanationEditText.addTextChangedListener(_textEditText_watcherListener);
        _additionalHelpTextView = (TextView) v.findViewById(R.id.additionalHelp_textview);
        _sendButton = (Button) v.findViewById(R.id.send_button);
        _sendButton.setOnClickListener(_send_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        populateSpinners();
        if (_clear){
            _reasonSpinnre.setText("");
            _explanationEditText.setText("");
            _explanationEditText.setHint(getString(R.string.dialog_explanation_default));
            _spinnerPosition = -1;
            _clear = false;
        }else {
            if (!misc.isEmptyOrNull(_message))
                _explanationEditText.setText(_message);

            onSpinnerSelection(_spinnerPosition);
        }
        _textEditText_watcherListener.onTextChanged(
                _explanationEditText.getText().toString(),
                0, _explanationEditText.getText().toString().length(),
                _explanationEditText.getText().toString().length());


    }

    @Override
    public void onDismiss(DialogInterface dialogFragment){
//        Log.e(TAG, "onDismiss");
        super.onDismiss(dialogFragment);
    }

    public void show(String source) {
        _source = source;
        _clear = true;
        super.show();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void onSpinnerSelection(int position) {
        _spinnerPosition = position;
        switch (position) {
            case ITEM_APP_FEEDBACK:
                _internalTeamParam = "usability";
                _additionalHelpTextView.setVisibility(View.GONE);
                break;

            case ITEM_WORK_ORDER_ISSUES:
                _internalTeamParam = "support";
                _additionalHelpTextView.setVisibility(View.VISIBLE);
                break;

            case ITEM_ACCOUNT_ISSUE:
                _internalTeamParam = "csd";
                _additionalHelpTextView.setVisibility(View.GONE);
                break;
        }
        if (position != -1)
            _explanationEditText.requestFocus();

    }

    private FnSpinner populateSpinners() {
        if (_reasonSpinnre != null && _reasonSpinnre.getAdapter() == null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.reason_list,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _reasonSpinnre.setAdapter(adapter);
        }
        return _reasonSpinnre;
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

    private final AdapterView.OnItemClickListener _reasonSpinner_onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onSpinnerSelection(position);
        }
    };

    private final View.OnClickListener _send_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
                ToastClient.toast(App.get(), getString(R.string.toast_empty_feedback), Toast.LENGTH_SHORT);
                return;
            }

            dismiss();
            if (_listener != null) {
                _listener.onOk(_explanationEditText.getText().toString());
            }

            Debug.logCustom(new CustomEvent("ContactUsDialog")
                    .putCustomAttribute("Source", _source));

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
