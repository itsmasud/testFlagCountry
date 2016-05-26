package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.HintArrayAdapter;

/**
 * Created by Michael Carver on 1/15/2015.
 */
public class DeclineDialog extends DialogFragmentBase {
    private final String TAG = UniqueTag.makeTag("DeclineDialog");

    // Ui
    private CheckBox _blockCheckBox;
    private LinearLayout _blockLayout;
    private HintSpinner _blockSpinner;
    private EditText _blockEditText;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private int[] _reasonIds;
    private int _itemSelectedPosition;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static DeclineDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, DeclineDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.dialog_decline, container, false);

        _blockCheckBox = (CheckBox) v.findViewById(R.id.block_checkbox);
        _blockCheckBox.setOnCheckedChangeListener(_blockCheckBox_onChecked);

        _blockLayout = (LinearLayout) v.findViewById(R.id.block_layout);

        _blockSpinner = (HintSpinner) v.findViewById(R.id.block_spinner);
        _blockSpinner.setOnItemSelectedListener(_spinner_selected);

        _reasonIds = v.getContext().getResources().getIntArray(R.array.dialog_block_reason_ids);

        _blockEditText = (EditText) v.findViewById(R.id.blockdetails_edittext);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_blockSpinner != null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    _blockSpinner.getContext(),
                    R.array.dialog_block_reasons,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _blockSpinner.setAdapter(adapter);
        }
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                if (_blockCheckBox.isChecked()) {
                    _listener.onOk(true, _reasonIds[_itemSelectedPosition], _blockEditText.getText().toString());
                } else {
                    _listener.onOk(false, 0, null);
                }
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
    private final CompoundButton.OnCheckedChangeListener _blockCheckBox_onChecked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                _blockLayout.setVisibility(View.VISIBLE);
            } else {
                _blockLayout.setVisibility(View.GONE);
            }
        }
    };

    private final AdapterView.OnItemSelectedListener _spinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _itemSelectedPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public interface Listener {
        void onOk(boolean blockBuyer, int reasonId, String details);

        void onCancel();
    }

}
