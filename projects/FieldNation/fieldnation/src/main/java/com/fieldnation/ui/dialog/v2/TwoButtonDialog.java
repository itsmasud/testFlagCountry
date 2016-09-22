package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.EventDispatch;
import com.fieldnation.fndialog.SimpleDialog;

/**
 * Created by Michael on 9/21/2016.
 */

public class TwoButtonDialog extends SimpleDialog {
    private static final String TAG = "TwoButtonDialog";

    private static final String PARAM_TITLE = "title";
    private static final String PARAM_BODY = "body";
    private static final String PARAM_PRIMARY_BUTTON = "buttonPrimary";
    private static final String PARAM_SECONDARY_BUTTON = "buttonSecondary";
    private static final String PARAM_CANCELABLE = "cancelable";
    private static final String PARAM_RESPONSE = "response";
    private static final int PARAM_RESPONSE_PRIMARY = 0;
    private static final int PARAM_RESPONSE_SECONDARY = 1;
    private static final int PARAM_RESPONSE_CANCEL = 2;

    // Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private Button _primaryButton;
    private Button _secondaryButton;

    // Data
    private boolean _isCancelable = true;

    public TwoButtonDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(Context context, ViewGroup container) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_v2_two_button, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);
        _primaryButton = (Button) v.findViewById(R.id.primary_button);
        _secondaryButton = (Button) v.findViewById(R.id.secondary_button);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        _primaryButton.setOnClickListener(_primary_onClick);
        _secondaryButton.setOnClickListener(_secondary_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _titleTextView.setText(payload.getString(PARAM_TITLE));
        _bodyTextView.setText(payload.getString(PARAM_BODY));
        _primaryButton.setText(payload.getString(PARAM_PRIMARY_BUTTON));
        _secondaryButton.setText(payload.getString(PARAM_SECONDARY_BUTTON));
        _isCancelable = payload.getBoolean(PARAM_CANCELABLE);
        super.show(payload, animate);
    }

    @Override
    public void cancel() {
        Bundle response = new Bundle();
        response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_CANCEL);
        EventDispatch.dialogComplete(App.get(), TwoButtonDialog.this, response);
        super.cancel();
    }

    @Override
    public boolean isCancelable() {
        return _isCancelable;
    }

    private final View.OnClickListener _primary_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_PRIMARY);
            EventDispatch.dialogComplete(App.get(), TwoButtonDialog.this, response);
        }
    };

    private final View.OnClickListener _secondary_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_SECONDARY);
            EventDispatch.dialogComplete(App.get(), TwoButtonDialog.this, response);
        }
    };

    public static class Controller extends com.fieldnation.fndialog.Controller {
        public Controller(Context context) {
            super(context, TwoButtonDialog.class);
        }

        public static void show(Context context, String title, String body, String primaryButton, String secondaryButton, boolean isCancelable) {
            Bundle params = new Bundle();
            params.putString(PARAM_TITLE, title);
            params.putString(PARAM_BODY, body);
            params.putString(PARAM_PRIMARY_BUTTON, primaryButton);
            params.putString(PARAM_SECONDARY_BUTTON, secondaryButton);
            params.putBoolean(PARAM_CANCELABLE, isCancelable);

            show(context, TwoButtonDialog.class, params);
        }

        public static void dismiss(Context context) {
            dismiss(context, TwoButtonDialog.class);
        }
    }

    public static abstract class ControllerListener implements com.fieldnation.fndialog.Controller.Listener {
        @Override
        public void onComplete(Bundle response) {
            switch (response.getInt(PARAM_RESPONSE)) {
                case PARAM_RESPONSE_PRIMARY:
                    onPrimary();
                    break;
                case PARAM_RESPONSE_SECONDARY:
                    onSecondary();
                    break;
                case PARAM_RESPONSE_CANCEL:
                    onCancel();
                    break;
                default:
                    break;
            }
        }

        public abstract void onPrimary();

        public abstract void onSecondary();

        public abstract void onCancel();
    }
}
