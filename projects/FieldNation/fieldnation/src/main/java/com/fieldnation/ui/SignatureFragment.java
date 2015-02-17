package com.fieldnation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.utils.Stopwatch;
import com.fieldnation.utils.misc;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class SignatureFragment extends FragmentBase {
    private static final String TAG = "ui.SignatureFragment";

    // State
    private static final String STATE_NAME = "STATE_NAME_EDIT_TEXT";
    private static final String STATE_SIGNATURE = "STATE_SIGNATURE";

    // Ui
    private EditText _nameEditText;
    private SignatureView _signatureView;
    private Button _clearButton;
    private Button _backButton;
    private Button _submitButton;

    // Data
    private Listener _listener;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static SignatureFragment getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, SignatureFragment.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (_nameEditText != null)
            outState.putParcelable(STATE_NAME, _nameEditText.onSaveInstanceState());
        if (_signatureView != null)
            outState.putParcelable(STATE_SIGNATURE, _signatureView.onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Stopwatch stopwatch = new Stopwatch();
        View v = inflater.inflate(R.layout.fragment_signature, container, false);

        _nameEditText = (EditText) v.findViewById(R.id.name_edittext);
        _signatureView = (SignatureView) v.findViewById(R.id.signature_view);

        _clearButton = (Button) v.findViewById(R.id.clear_button);
        _clearButton.setOnClickListener(_clear_onClick);

        _backButton = (Button) v.findViewById(R.id.back_button);
        _backButton.setOnClickListener(_back_onClick);

        _submitButton = (Button) v.findViewById(R.id.submit_button);
        _submitButton.setOnClickListener(_submit_onClick);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_NAME))
                _nameEditText.onRestoreInstanceState(savedInstanceState.getParcelable(STATE_NAME));

            if (savedInstanceState.containsKey(STATE_SIGNATURE))
                _signatureView.onRestoreInstanceState(savedInstanceState.getParcelable(STATE_SIGNATURE));
        }


        Log.v(TAG, "onCreate time " + stopwatch.finish());
        return v;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private View.OnClickListener _clear_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _signatureView.clear();
        }
    };

    private View.OnClickListener _back_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onBack();
        }
    };

    private View.OnClickListener _submit_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                if (misc.isEmptyOrNull(_nameEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    // TODO flash name thing
                    _nameEditText.requestFocus();
                } else {
                    _listener.onSubmit(_nameEditText.getText().toString(), _signatureView.getSignatureJson());
                }
            }
        }
    };

    public interface Listener {
        public void onBack();

        public void onSubmit(String name, String signatureJson);
    }
}
