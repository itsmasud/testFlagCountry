package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

/**
 * Created by shoaib.ahmed on 07/28/2016.
 */
public class RateBuyerModal extends DialogFragmentBase {
    private static final String TAG = "RateBuyerModal";

    // State
    private static final String STATE_WORKORDER = "RateBuyerDialog:STATE_WORKORDER";

    // UI
    private TextView _bodyTextView;
    private Button _continueButton;
    private Button _remindMeLaterButton;
    private RateBuyerDialog _rateBuyerDialog;

    // Data
    private Workorder _workorder;
    private Listener _listener;
    private boolean _clear = false;


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static RateBuyerModal getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, RateBuyerModal.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);

        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.modal_rate_buyer, container);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);

        _continueButton = (Button) v.findViewById(R.id.continue_button);
        _continueButton.setOnClickListener(_continue_onClick);

        _remindMeLaterButton = (Button) v.findViewById(R.id.remindMeLter_button);
        _remindMeLaterButton.setOnClickListener(_remindMeLater_onClick);

        _rateBuyerDialog = RateBuyerDialog.getInstance(getFragmentManager(), TAG);


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
        Log.e(TAG, "onResume");
        super.onResume();
        populateUi();
    }

    @Override
    public void dismiss() {
//        Log.e(TAG, "dismiss");
        _clear = true;
        super.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
//        Log.e(TAG, "onDismiss");
        super.onDismiss(dialog);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(Workorder workorder) {
        _workorder = workorder;
        super.show();
    }

    private void populateUi() {
        if (_workorder == null) return;
        if (!misc.isEmptyOrNull(_workorder.getCompanyName())) {
            _bodyTextView.setText(getResources().getString(R.string.modal_rate_buyer_body, _workorder.getCompanyName()));
        }
    }


    private final View.OnClickListener _continue_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _rateBuyerDialog.show(_workorder);
            dismiss();
        }
    };

    private final View.OnClickListener _remindMeLater_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


    public interface Listener {
        void onContine();

        void onRemindMeLater();
    }
}
