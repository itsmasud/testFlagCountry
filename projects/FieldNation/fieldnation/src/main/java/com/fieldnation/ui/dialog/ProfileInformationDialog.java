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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.analytics.ScreenName;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.help.HelpClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

/**
 * Created by Shoaib on 10/01/2016.
 */
public class ProfileInformationDialog extends DialogFragmentBase {
    private static final String TAG = "ProfileInformationDialog";

    // State
    private static final String STATE_MESSAGE = "ProfileInformationDialog:Message";
    private static final String STATE_SOURCE = "ProfileInformationDialog:Source";

    // Ui
    private EditText _phoneNoEditText;
    private EditText _phoneNoExtEditText;
    private EditText _address1EditText;
    private EditText _address2EditText;
    private EditText _cityEditText;
    private Button _stateButton;
    private EditText _zipCodeEditText;



    // Data
    private Listener _listener;
    private String _source;
    private boolean _clear = false;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static ProfileInformationDialog getInstance(FragmentManager fm, String tag) {
        Log.v(TAG, "getInstance");
        return getInstance(fm, tag, ProfileInformationDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
//        if (_explanationEditText != null && !misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
//            _message = _explanationEditText.getText().toString();
//            outState.putString(STATE_MESSAGE, _message);
//        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_profile_information, container, false);

        _phoneNoEditText = (EditText) v.findViewById(R.id.phone_edittext);
        _phoneNoExtEditText = (EditText) v.findViewById(R.id.phone_ext_edittext);
        _address1EditText = (EditText) v.findViewById(R.id.address_1_edittext);
        _address2EditText = (EditText) v.findViewById(R.id.address_2_edittext);
        _cityEditText = (EditText) v.findViewById(R.id.city_edittext);
        _stateButton = (Button) v.findViewById(R.id.state_button);
        _zipCodeEditText = (EditText) v.findViewById(R.id.zip_code_edittext);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(TAG, "onViewStateRestored");
        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(STATE_MESSAGE))
//                _message = savedInstanceState.getString(STATE_MESSAGE);
//            if (savedInstanceState.containsKey(STATE_SOURCE))
//                _source = savedInstanceState.getString(STATE_SOURCE);
        } else {
            _clear = true;
        }
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
//        if (_clear) {
//            _explanationEditText.setText("");
//            _explanationEditText.setHint(getString(R.string.dialog_explanation_default));
//            _clear = false;
//            return;
//        }

        populateUi();

    }

    @Override
    public void onDismiss(DialogInterface dialogFragment) {
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


    private void populateUi(){


        _phoneNoEditText.post(new Runnable() {
            @Override
            public void run() {
                misc.hideKeyboard(_phoneNoEditText);
            }
        });
    }

    public interface Listener {
        void onOk(String message);

        void onCancel();
    }


}
