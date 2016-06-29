package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.misc;

public class DiscountDialog extends DialogFragmentBase {
    private static String TAG = "DiscountDialog";

    // State
    private static final String STATE_TITLE = "STATE_TITLE";

    // UI
    private TextView _titleTextView;
    private Button _okButton;
    private Button _cancelButton;
    private EditText _amountEditText;
    private EditText _descriptionEditText;

    // Data
    private Listener _listener;
    private String _title;


    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static DiscountDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, DiscountDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TITLE))
                _title = savedInstanceState.getString(STATE_TITLE);
        }
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_title != null)
            outState.putString(STATE_TITLE, _title);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_discount, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancelButton_onClick);
        _amountEditText = (EditText) v.findViewById(R.id.amount_edittext);
        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _descriptionEditText.setOnEditorActionListener(_oneditor_listener);

        //getDialog().setTitle("Add Discount");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_title != null)
            _titleTextView.setText(_title);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(String title) {
        _title = title;
        super.show();
    }

    private String getDescription() {
        return _descriptionEditText.getText().toString();
    }

    private Double getAmount() {
        try {
            return Double.parseDouble(_amountEditText.getText().toString());
        } catch (Exception ex) {
            return 0.0;
        }
    }

	/*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private final TextView.OnEditorActionListener _oneditor_listener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                _amountEditText.requestFocus();
                handled = true;
            }
            return handled;
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (misc.isEmptyOrNull(_descriptionEditText.getText().toString())){
                ToastClient.toast(App.get(), getString(R.string.toast_missing_description), Toast.LENGTH_SHORT);
                return;
            }
            // convert to penies
            if ((int) (getAmount() * 100) < 10) {
                ToastClient.toast(App.get(), getString(R.string.toast_minimum_payable_amount), Toast.LENGTH_SHORT);
                return;
            }

            DiscountDialog.this.dismiss();
            if (_listener != null)
                _listener.onOk(getDescription(), getAmount());

        }
    };

    private final View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onCancel();
        }
    };

    public interface Listener {
        void onOk(String description, double amount);

        void onCancel();
    }
}
