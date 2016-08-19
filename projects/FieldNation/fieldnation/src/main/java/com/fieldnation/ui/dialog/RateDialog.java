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
import android.widget.CheckBox;
import android.widget.TextView;

import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.ui.StarView;

/**
 * Created by Michael Carver on 6/12/2015.
 */
public class RateDialog extends DialogFragmentBase {
    private static final String TAG = "RateDialog";

    // Ui
    private StarView _rateStarView;
    private CheckBox _isClearExceptionCheckBox;
    private CheckBox _isProfessionalCheckBox;
    private Button _continueButton;

    // Data
    private Listener _listener;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static RateDialog getInstance(FragmentManager fm, String tag) {
        Log.v(TAG, "getInstance");
        return getInstance(fm, tag, RateDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_rate_me, container, false);

        _rateStarView = (StarView) v.findViewById(R.id.stars);
//        _rateStarView.setOnClickListener(_onClick_listener);
        _isClearExceptionCheckBox = (CheckBox) v.findViewById(R.id.chk_is_clear_exception);
        _isClearExceptionCheckBox.setOnClickListener(_isClearException_onClick);
        _isProfessionalCheckBox = (CheckBox) v.findViewById(R.id.chk_is_professional);
        _isProfessionalCheckBox.setOnClickListener(_isProfessional_listener);
        _continueButton = (Button) v.findViewById(R.id.continue_button);
        _continueButton.setOnClickListener(_continue_onClick);



        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private final TextView.OnEditorActionListener _onEditor_listener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                _continue_onClick.onClick(null);
                handled = true;
            }
            return handled;
        }
    };

    private final View.OnClickListener _isClearException_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            dismiss();
//            if (_listener != null) {
//                _listener.onOk(_messageEditText.getText().toString());
//            }
        }
    };

    private final View.OnClickListener _isProfessional_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            dismiss();
//            if (_listener != null) {
//                _listener.onOk(_messageEditText.getText().toString());
//            }
        }
    };

    private final View.OnClickListener _continue_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            dismiss();
//            if (_listener != null) {
//                _listener.onOk(_messageEditText.getText().toString());
//            }
        }
    };




    public interface Listener {
        void onOk(int rating, boolean clearExpectations, boolean professional);

        void onCancel();
    }


}
