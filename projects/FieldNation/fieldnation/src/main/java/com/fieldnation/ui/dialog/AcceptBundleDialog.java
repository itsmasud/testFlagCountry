package com.fieldnation.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;

public class AcceptBundleDialog extends DialogFragmentBase {
    private static final String TAG = "AcceptBundleDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // UI
    private TextView _titleTextView;
    private TextView _acceptWOText;
    private Button _okButton;
    private Button _cancelButton;
    private Button _bundleButton;

    // Data
    private Listener _listener;
    private Workorder _workorder;

    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    public static AcceptBundleDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, AcceptBundleDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
        }
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_accept_bundle_workorder, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);

        _acceptWOText = (TextView) v.findViewById(R.id.accept_description);

        _bundleButton = (Button) v.findViewById(R.id.bundle_button);
        _bundleButton.setOnClickListener(_bundle_onClick);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        populateUi();
    }

    public void show(Workorder workorder) {
        _workorder = workorder;
        super.show();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_acceptWOText == null)
            return;

        if (_workorder.getWorkorderSubstatus() == WorkorderSubstatus.ROUTED) {
            _titleTextView.setText(R.string.accept_bundle);
        } else {
            _titleTextView.setText(R.string.request_bundle);
        }

        _acceptWOText.setText(String.format(getString(R.string.workorder_bundle_warning), _workorder.getBundleCount()));
    }


    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            Intent intent = new Intent(getActivity(), WorkorderBundleDetailActivity.class);
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, _workorder.getBundleId());
            getActivity().startActivity(intent);
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onOk(_workorder);
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public interface Listener {
        void onOk(Workorder workorder);
    }
}
