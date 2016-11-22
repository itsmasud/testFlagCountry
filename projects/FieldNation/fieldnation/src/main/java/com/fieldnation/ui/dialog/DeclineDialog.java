package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

/**
 * Created by Michael Carver on 1/15/2015.
 */
public class DeclineDialog extends DialogFragmentBase {
    private final String TAG = UniqueTag.makeTag("DeclineDialog");

    private static final String STATE_BLOCK_SPINNER = "STATE_BLOCK_SPINNER";
    private static final String STATE_DECLINE_SPINNER = "STATE_DECLINE_SPINNER";
    private static final String STATE_BLOCK_CHECKBOX= "STATE_BLOCK_CHECKBOX";

    // Ui
    private HintSpinner _declineSpinner;
    private EditText _declineEditText;
    private CheckBox _blockCheckBox;
    private LinearLayout _blockLayout;
    private HintSpinner _blockSpinner;
    private EditText _blockEditText;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private int[] _declineReasonIds;
    private int[] _blockReasonIds;
    private int _selectedPosition_declineSpinner = -1;
    private int _selectedPosition_blockSpinner = -1;
    private static int DECLINE_REASON_OTHER = 7;
    private boolean _clear = false;

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

        _declineSpinner = (HintSpinner) v.findViewById(R.id.decline_spinner);
        _declineSpinner.setOnItemSelectedListener(_spinnerDecline_selected);

        _declineReasonIds = v.getContext().getResources().getIntArray(R.array.dialog_decline_reason_ids);

        _declineEditText = (EditText) v.findViewById(R.id.declineDetails_edittext);

        _blockCheckBox = (CheckBox) v.findViewById(R.id.block_checkbox);
        _blockCheckBox.setOnCheckedChangeListener(_blockCheckBox_onChecked);

        _blockLayout = (LinearLayout) v.findViewById(R.id.block_layout);

        _blockSpinner = (HintSpinner) v.findViewById(R.id.block_spinner);
        _blockSpinner.setOnItemSelectedListener(_spinnerBlock_selected);

        _blockReasonIds = v.getContext().getResources().getIntArray(R.array.dialog_block_reason_ids);

        _blockEditText = (EditText) v.findViewById(R.id.blockDetails_edittext);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_selectedPosition_blockSpinner != -1) {
            outState.putInt(STATE_BLOCK_SPINNER, _selectedPosition_blockSpinner);
        }
        if (_selectedPosition_declineSpinner != -1) {
            outState.putInt(STATE_DECLINE_SPINNER, _selectedPosition_declineSpinner);
        }
            outState.putBoolean(STATE_BLOCK_CHECKBOX, _blockCheckBox.isChecked());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_DECLINE_SPINNER)) {
                _selectedPosition_declineSpinner = savedInstanceState.getInt(STATE_DECLINE_SPINNER);
                getDeclineSpinner().setSelection(_selectedPosition_declineSpinner);
            }
            if (savedInstanceState.containsKey(STATE_BLOCK_SPINNER)) {
                _selectedPosition_blockSpinner = savedInstanceState.getInt(STATE_BLOCK_SPINNER);
                getBlockSpinner().setSelection(_selectedPosition_blockSpinner);
            }
            if (savedInstanceState.containsKey(STATE_BLOCK_CHECKBOX)) {
                _blockCheckBox_onChecked.onCheckedChanged(_blockCheckBox, savedInstanceState.getBoolean(STATE_BLOCK_CHECKBOX));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDeclineSpinner();
        getBlockSpinner();

        if (_clear) {
            _clear = false;
            _declineEditText.setText("");
            _blockEditText.setText("");
            _blockCheckBox.setChecked(false);
            _blockCheckBox_onChecked.onCheckedChanged(_blockCheckBox, false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _clear = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        _clear = true;
    }

    @Override
    public void dismiss() {
        _clear = true;
        super.dismiss();
    }

    public void show() {
        _clear = true;
        super.show();
    }


    private HintSpinner getDeclineSpinner() {
        if (_declineSpinner != null && _declineSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    _declineSpinner.getContext(),
                    R.array.dialog_decline_reasons,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _declineSpinner.setAdapter(adapter);
        }
        return _declineSpinner;
    }

    private HintSpinner getBlockSpinner() {
        if (_blockSpinner != null && _blockSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    _blockSpinner.getContext(),
                    R.array.dialog_block_reasons,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _blockSpinner.setAdapter(adapter);
        }
        return _blockSpinner;
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
            if (_selectedPosition_declineSpinner != -1 &&
                    _declineReasonIds[_selectedPosition_declineSpinner] == DECLINE_REASON_OTHER
                    && misc.isEmptyOrNull(_declineEditText.getText().toString())) {
                ToastClient.toast(App.get(), getString(R.string.toast_missing_decline_explanation), Toast.LENGTH_LONG);
                return;
            }

            if (_listener != null) {
                if (_blockCheckBox.isChecked()) {
                    if (_selectedPosition_blockSpinner == -1) {
                        ToastClient.toast(App.get(), getString(R.string.toast_missing_blocking_reason), Toast.LENGTH_LONG);
                        return;
                    }

                    if (_selectedPosition_declineSpinner == -1)
                        _listener.onOk(true, _blockReasonIds[_selectedPosition_blockSpinner], _blockEditText.getText().toString());
                    else
                        _listener.onOk(true, _declineReasonIds[_selectedPosition_declineSpinner], _declineEditText.getText().toString(),
                                _blockReasonIds[_selectedPosition_blockSpinner], _blockEditText.getText().toString());
                } else {
                    if (_selectedPosition_declineSpinner == -1)
                        _listener.onOk();
                    else
                        _listener.onOk(_declineReasonIds[_selectedPosition_declineSpinner], _declineEditText.getText().toString());

                }
            }

            dismiss();

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

    private final AdapterView.OnItemSelectedListener _spinnerDecline_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _selectedPosition_declineSpinner = position;
            if (_declineReasonIds[_selectedPosition_declineSpinner] == DECLINE_REASON_OTHER) {
                _declineEditText.setVisibility(View.VISIBLE);
                _declineEditText.requestFocus();
            } else _declineEditText.setVisibility(View.GONE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final AdapterView.OnItemSelectedListener _spinnerBlock_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _selectedPosition_blockSpinner = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public interface Listener {
        void onOk();

        void onOk(boolean blockBuyer, int blockingReasonId, String blockingExplanation);

        void onOk(boolean blockBuyer, int declineReasonId, String declineExplanation, int blockReasonId, String blockingExplanation);

        void onOk(int declineReasonId, String declineExplanation);

        void onCancel();
    }

}
