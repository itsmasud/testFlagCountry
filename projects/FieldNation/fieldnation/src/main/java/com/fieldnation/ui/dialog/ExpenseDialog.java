package com.fieldnation.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.misc;

public class ExpenseDialog extends DialogFragmentBase {
    private static String TAG = "ExpenseDialog";

    // State
    private static final String STATE_SHOW_CATEGORIES = "STATE_SHOW_CATEGORIES";

    // Ui
    private Button _okButton;
    private Button _cancelButton;
    private EditText _amountEditText;
    private EditText _descriptionEditText;
    private Spinner _categorySpinner;
    private LinearLayout _categoryLayout;

    // Data
    private Listener _listener;
    private ExpenseCategory[] _categories;
    private ArrayAdapter<ExpenseCategory> _adapter;
    private InputMethodManager _imm;
    private boolean _reset = false;
    private boolean _showCategories = true;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static ExpenseDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ExpenseDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_SHOW_CATEGORIES))
                _showCategories = savedInstanceState.getBoolean(STATE_SHOW_CATEGORIES);
        }
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SHOW_CATEGORIES, _showCategories);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_expense, container, false);

        _amountEditText = (EditText) v.findViewById(R.id.amount_edittext);
        _amountEditText.setOnEditorActionListener(_oneditor_listener);

        _categoryLayout = (LinearLayout) v.findViewById(R.id.category_layout);

        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);

        _categorySpinner = (Spinner) v.findViewById(R.id.category_spinner);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancelButton_onClick);

        _imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        new ExpenseCategories(getActivity()).setListener(_categoriesListener);

        populateUi();
    }

    private void setCategories(ExpenseCategory[] categories) {
        try {
            _categories = categories;
            _adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.view_spinner_item,
                    categories);
            _adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);
            _categorySpinner.setAdapter(_adapter);
            _categorySpinner.setSelection(0);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public void populateUi() {
        if (!_reset)
            return;

        if (_descriptionEditText != null)
            _descriptionEditText.setText("");

        if (_amountEditText != null)
            _amountEditText.setText("");

        if (_categorySpinner != null)
            _categorySpinner.setSelection(0);

        if (_showCategories) {
            _categoryLayout.setVisibility(View.VISIBLE);
        } else {
            _categoryLayout.setVisibility(View.GONE);
        }
    }


    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(boolean showCategories) {
        _showCategories = showCategories;
        _reset = true;
        super.show();
    }

    public String getDescription() {
        return _descriptionEditText.getText().toString();
    }

    public Double getAmount() {
        try {
            return Double.parseDouble(_amountEditText.getText().toString());
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public ExpenseCategory getCategory() {
        try {
            if (_showCategories)
                return _adapter.getItem(_categorySpinner.getSelectedItemPosition());
            else
                return null;
        } catch (Exception ex) {
            return null;
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private final ExpenseCategories.Listener _categoriesListener = new ExpenseCategories.Listener() {
        @Override
        public void onHaveCategories(ExpenseCategory[] categories) {
            setCategories(categories);
        }
    };

    private final TextView.OnEditorActionListener _oneditor_listener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                _imm.hideSoftInputFromWindow(_amountEditText.getWindowToken(), 0);
                handled = true;
            }
            return handled;
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (misc.isEmptyOrNull(_amountEditText.getText().toString())
                    || misc.isEmptyOrNull(_descriptionEditText.getText().toString()))
                return;

            if (getAmount() < 0.1) {
                ToastClient.toast(App.get(), getResources().getString(R.string.toast_minimum_payable_amount), Toast.LENGTH_SHORT);
                return;
            }
            if (_listener != null)
                _listener.onOk(getDescription(), getAmount(), getCategory());

            ExpenseDialog.this.dismiss();
        }
    };

    public interface Listener {
        void onOk(String description, double amount, ExpenseCategory category);

        void onCancel();
    }
}
