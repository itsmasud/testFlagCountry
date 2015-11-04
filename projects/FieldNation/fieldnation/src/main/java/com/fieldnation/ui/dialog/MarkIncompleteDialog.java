package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

/**
 * Created by michael.carver on 12/16/2014.
 */
public class MarkIncompleteDialog extends DialogFragmentBase {
    private static final String TAG = "MarkIncompleteDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // Ui
    private Button _continueButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private Workorder _workorder;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/

    public static MarkIncompleteDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, MarkIncompleteDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
        }

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null) {
            outState.putParcelable(STATE_WORKORDER, _workorder);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_mark_incomplete, container, false);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _continueButton = (Button) v.findViewById(R.id.continue_button);
        _continueButton.setOnClickListener(_continue_onClick);

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
        populateUi();
    }

    public void show(Workorder workorder) {
        _workorder = workorder;

        super.show();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_continueButton == null)
            return;

        boolean hasSignature = _workorder.isSignatureCollected();

    }

    /*-*****************************-*/
    /*-             Events          -*/
    /*-*****************************-*/

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private final View.OnClickListener _continue_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onContinueClick();
                dismiss();
            }
        }
    };

    public interface Listener {

        void onContinueClick();
    }
}
