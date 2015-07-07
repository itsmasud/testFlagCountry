package com.fieldnation.ui.dialog;

import android.os.Bundle;
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

import com.fieldnation.GlobalState;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.data.help.HelpClient;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 6/12/2015.
 */
public class FeedbackDialog extends DialogFragmentBase {
    private static final String TAG = "FeedbackDialog";

    // State
    private static final String STATE_NOTES = "FeedbackDialog:message";

    // Ui
    private EditText _messageEditText;
    private Button _sendButton;
    private Button _cancelButton;

    // Data
    private String _message;
    private Listener _listener;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static FeedbackDialog getInstance(FragmentManager fm, String tag) {
        Log.v(TAG, "getInstance");
        return getInstance(fm, tag, FeedbackDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_NOTES))
                _message = savedInstanceState.getString(STATE_NOTES);
        }
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_NOTES))
                _message = savedInstanceState.getString(STATE_NOTES);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_messageEditText != null && !misc.isEmptyOrNull(_messageEditText.getText().toString())) {
            _message = _messageEditText.getText().toString();
            outState.putString(STATE_NOTES, _message);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_feedback, container, false);

        _messageEditText = (EditText) v.findViewById(R.id.message_edittext);
        _messageEditText.setOnEditorActionListener(_onEditor_listener);
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

        if (!misc.isEmptyOrNull(_message))
            _messageEditText.setText(_message);
        else
            _messageEditText.setText("");
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private final TextView.OnEditorActionListener _onEditor_listener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                _send_onClick.onClick(null);
                handled = true;
            }
            return handled;
        }
    };

    private final View.OnClickListener _send_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onOk(_messageEditText.getText().toString());
                HelpClient.sendFeedback(GlobalState.getContext(), "android", _messageEditText.getText().toString(), null, null, null);
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
