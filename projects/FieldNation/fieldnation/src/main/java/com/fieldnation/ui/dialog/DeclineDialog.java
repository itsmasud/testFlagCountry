package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;

/**
 * Created by Michael Carver on 1/15/2015.
 */
public class DeclineDialog extends DialogFragmentBase {
    private final String TAG = UniqueTag.makeTag("ui.dialog.DeclineDialog");

    // Ui
    private CheckBox _blockCheckBox;
    private LinearLayout _blockLayout;
    private Spinner _blockSpinner;
    private EditText _blockEditText;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static DeclineDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, DeclineDialog.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_decline, container, false);

        _blockCheckBox = (CheckBox) v.findViewById(R.id.block_checkbox);
        _blockCheckBox.setOnCheckedChangeListener(_blockCheckBox_onChecked);

        _blockLayout = (LinearLayout) v.findViewById(R.id.block_layout);

        _blockSpinner = (Spinner) v.findViewById(R.id.block_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.dialog_block_reasons,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _blockSpinner.setAdapter(adapter);

        _blockEditText = (EditText) v.findViewById(R.id.blockdetails_edittext);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        return v;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                if (_blockCheckBox.isChecked()) {
                    _listener.onOk(true, (String) _blockSpinner.getSelectedItem(), _blockEditText.getText().toString());
                } else {
                    _listener.onOk(false, null, null);
                }
            }
        }
    };
    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onCancel();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener _blockCheckBox_onChecked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                _blockLayout.setVisibility(View.VISIBLE);
            else
                _blockLayout.setVisibility(View.GONE);
        }
    };

    public interface Listener {
        public void onOk(boolean blockBuyer, String reason, String details);

        public void onCancel();
    }

}
