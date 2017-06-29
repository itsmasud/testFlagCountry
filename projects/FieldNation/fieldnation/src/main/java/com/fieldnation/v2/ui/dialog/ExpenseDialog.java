package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.v2.data.model.ExpenseCategories;
import com.fieldnation.v2.data.model.ExpenseCategory;

public class ExpenseDialog extends FullScreenDialog {
    private static final String TAG = "ExpenseDialog";

    // State
    private static final String STATE_CATEGORY_SELECTION = "STATE_CATEGORY_SELECTION";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private EditText _amountEditText;
    private EditText _descriptionEditText;
    private HintSpinner _categorySpinner;
    private LinearLayout _categoryLayout;

    // Data
    private ExpenseCategory[] _categories;
    private HintArrayAdapter _adapter;
    private InputMethodManager _imm;
    private boolean _showCategories = true;
    private int _itemSelectedPosition = -1;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public ExpenseDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_expense, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle("Expense");

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_ok);

        _amountEditText = v.findViewById(R.id.amount_edittext);
        _categoryLayout = v.findViewById(R.id.category_layout);
        _descriptionEditText = v.findViewById(R.id.description_edittext);
        _categorySpinner = v.findViewById(R.id.category_spinner);
        _imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        return v;
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _showCategories = payload.getBoolean("showCategories");
        super.show(payload, animate);
        populateUi();
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _amountEditText.setOnEditorActionListener(_oneditor_listener);
        _categorySpinner.setOnItemSelectedListener(_spinner_selected);
    }

    @Override
    public void onResume() {
        new ExpenseCategories(App.get()).setListener(_categoriesListener);
        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState != null) {
            if (savedState.containsKey(STATE_CATEGORY_SELECTION)) {
                _itemSelectedPosition = savedState.getInt(STATE_CATEGORY_SELECTION);
            }
        }
        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_itemSelectedPosition != -1)
            outState.putInt(STATE_CATEGORY_SELECTION, _itemSelectedPosition);

        super.onSaveDialogState(outState);
    }

    public void populateUi() {
        if (_showCategories) {
            _categoryLayout.setVisibility(View.VISIBLE);
        } else {
            _categoryLayout.setVisibility(View.GONE);
        }
    }

    private void setCategories(ExpenseCategory[] categories) {
        try {
            _categories = categories;
            _adapter = HintArrayAdapter.createFromArray(getView().getContext(), categories,
                    R.layout.view_spinner_item);

            _adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _categorySpinner.setAdapter(_adapter);

            if (_itemSelectedPosition != -1)
                _categorySpinner.setSelection(_itemSelectedPosition);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                return (ExpenseCategory) _adapter.getItem(_itemSelectedPosition);
            else
                return null;
        } catch (Exception ex) {
            return null;
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
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

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (misc.isEmptyOrNull(_descriptionEditText.getText().toString())) {
                ToastClient.toast(App.get(), R.string.toast_must_enter_description, Toast.LENGTH_LONG);
                return false;
            }

            if (misc.isEmptyOrNull(_amountEditText.getText().toString()) || getAmount() < 0.1) {
                ToastClient.toast(App.get(), R.string.toast_minimum_payable_amount, Toast.LENGTH_SHORT);
                return false;
            }

            if (_showCategories && _itemSelectedPosition == -1) {
                ToastClient.toast(App.get(), R.string.toast_must_select_category, Toast.LENGTH_LONG);
                return false;
            }

            _onOkDispatcher.dispatch(getUid(), getDescription(), getAmount(), getCategory());

            ExpenseDialog.this.dismiss(true);
            return true;
        }
    };

    private final AdapterView.OnItemSelectedListener _spinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _itemSelectedPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _itemSelectedPosition = -1;
        }
    };

    public static void show(Context context, String uid, boolean showCategories) {
        Bundle params = new Bundle();
        params.putBoolean("showCategories", showCategories);
        Controller.show(context, uid, ExpenseDialog.class, params);
    }

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(String description, double amount, ExpenseCategory category);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((String) parameters[0], (Double) parameters[1], (ExpenseCategory) parameters[2]);
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }

    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }
}
