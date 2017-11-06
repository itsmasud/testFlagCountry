package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.PayModifier;

public class DiscountDialog extends SimpleDialog {
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
    private String _title;
    private int _workOrderId;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public DiscountDialog(Context context, ViewGroup container) {
        super(context, container);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_discount, container, false);

        _titleTextView = v.findViewById(R.id.title_textview);
        _okButton = v.findViewById(R.id.ok_button);
        _cancelButton = v.findViewById(R.id.cancel_button);
        _amountEditText = v.findViewById(R.id.amount_edittext);
        _descriptionEditText = v.findViewById(R.id.description_edittext);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _okButton.setOnClickListener(_okButton_onClick);
        _cancelButton.setOnClickListener(_cancelButton_onClick);
        _descriptionEditText.setOnEditorActionListener(_oneditor_listener);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);
        _title = payload.getString("title");
        _workOrderId = payload.getInt("workOrderId");
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

    private final View.OnClickListener _okButton_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {

            if (misc.isEmptyOrNull(_descriptionEditText.getText().toString())) {
                ToastClient.toast(App.get(), R.string.toast_missing_description, Toast.LENGTH_SHORT);
                return;
            }
            // convert to penies
            if ((int) (getAmount() * 100) < 10) {
                ToastClient.toast(App.get(), R.string.toast_minimum_payable_amount, Toast.LENGTH_SHORT);
                return;
            }

            dismiss(true);

            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
            try {
                PayModifier discount = new PayModifier();
                discount.setAmount(getAmount());
                discount.setDescription(getDescription());

                SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                uiContext.page += " - Discount Dialog";

                WorkordersWebApi.addDiscount(App.get(), _workOrderId, discount, uiContext);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            AppMessagingClient.setLoading(true);
        }
    };

    private final View.OnClickListener _cancelButton_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
        }
    };

    public static void show(Context context, String uid, int workOrderId, String title) {
        Bundle params = new Bundle();
        params.putString("title", title);
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, DiscountDialog.class, params);
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
