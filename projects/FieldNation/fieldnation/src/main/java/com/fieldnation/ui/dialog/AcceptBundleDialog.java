package com.fieldnation.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;

import java.util.List;

public class AcceptBundleDialog extends DialogFragment {
    private static final String TAG = "ui.dialog.AcceptBundleDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_TAG = "STATE_TAG";

    // UI
    private TextView _acceptWOText;
    private Button _okButton;
    private Button _cancelButton;
    private Button _bundleButton;

    // Data
    private Listener _listener;
    private Workorder _workorder;
    private FragmentManager _fm;
    private String _tag;


    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    public static AcceptBundleDialog getInstance(FragmentManager fm, String tag) {
        AcceptBundleDialog d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (frag instanceof AcceptBundleDialog && frag.getTag().equals(tag)) {
                    d = (AcceptBundleDialog) frag;
                    break;
                }
            }
        }
        if (d == null) {
            d = new AcceptBundleDialog();
        }
        d._fm = fm;
        d._tag = tag;
        return d;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TAG))
                _tag = savedInstanceState.getString(STATE_TAG);
        }
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        if (_tag != null)
            outState.putString(STATE_TAG, _tag);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_accept_bundle_workorder, container, false);

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

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_acceptWOText == null)
            return;

        _acceptWOText.setText("This workorder is part of a bundle of " + _workorder.getBundleCount().toString() + " workorders. By accepting this workorder you are accepting all of them.");
    }

    public void show(Workorder workorder) {
        if (workorder == null)
            return;

        _workorder = workorder;

        show(_fm, _tag);
    }


    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            Intent intent = new Intent(getActivity(), WorkorderBundleDetailActivity.class);
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, _workorder.getBundleId());
            getActivity().startActivity(intent);
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onOk(_workorder);
            }
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public interface Listener {
        public void onOk(Workorder workorder);
    }
}
