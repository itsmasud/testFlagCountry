package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Dialog;
import com.fieldnation.fndialog.EventDispatch;

/**
 * Created by Michael on 9/19/2016.
 */
public class TestDialog extends FrameLayout implements Dialog {
    private static final String TAG = "TestDialog";

    private static final String PARAM_TITLE = "title";
    private static final String PARAM_RESPONSE = "response";
    private static final int PARAM_RESPONSE_OK = 0;
    private static final int PARAM_RESPONSE_CANCEL = 1;
    private static final int PARAM_RESPONSE_DISMISS = 2;

    // Ui
    private TextView _titleTextView;

    public TestDialog(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_v2_test, this);

        if (isInEditMode())
            return;

        setOnClickListener(_this_onClick);
        findViewById(R.id.cancel_button).setOnClickListener(_cancel_onClick);
        findViewById(R.id.ok_button).setOnClickListener(_ok_onClick);

        _titleTextView = (TextView) findViewById(R.id.title_textview);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void show(Bundle payload) {
        _titleTextView.setText(payload.getString(PARAM_TITLE));
        setVisibility(VISIBLE);
    }

    @Override
    public void onRestoreDialogState(Parcelable savedState) {
        _titleTextView.setText(((Bundle) savedState).getString(PARAM_TITLE));
    }

    @Override
    public Parcelable onSaveDialogState() {
        Bundle savedState = new Bundle();
        savedState.putString(PARAM_TITLE, (String) _titleTextView.getText());
        return savedState;
    }

    @Override
    public void dismiss() {
        Bundle response = new Bundle();
        response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_DISMISS);
        EventDispatch.dialogComplete(App.get(), TestDialog.this, response);
        setVisibility(GONE);
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_DISMISS);
            EventDispatch.dialogComplete(App.get(), TestDialog.this, response);
            setVisibility(GONE);
        }
    };

    private final View.OnClickListener _ok_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_OK);
            EventDispatch.dialogComplete(App.get(), TestDialog.this, response);
            setVisibility(GONE);
        }
    };

    private final View.OnClickListener _cancel_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_CANCEL);
            EventDispatch.dialogComplete(App.get(), TestDialog.this, response);
            setVisibility(GONE);
        }
    };

    /**
     * The controller handles all communications with the dialog.
     */
    public static class Controller extends com.fieldnation.fndialog.Controller {

        private ControllerListener _dialogListener;

        public Controller(Context context) {
            super(context, TestDialog.class);
        }

        public void setDialogListener(ControllerListener dialogListener) {
            _dialogListener = dialogListener;
        }

        @Override
        protected void onComplete(Bundle response) {
            if (_dialogListener != null) {
                switch (response.getInt(PARAM_RESPONSE)) {
                    case PARAM_RESPONSE_OK:
                        _dialogListener.onOk();
                        break;
                    case PARAM_RESPONSE_CANCEL:
                        _dialogListener.onCancel();
                        break;
                    case PARAM_RESPONSE_DISMISS:
                        _dialogListener.onDismiss();
                        break;
                }
            }
        }

        public static void show(Context context, String title) {
            Bundle params = new Bundle();
            params.putString(PARAM_TITLE, title);

            show(context, TestDialog.class, params);
        }

        public static void dismiss(Context context) {
            dismiss(context, TestDialog.class);
        }
    }

    /**
     * The listener that lets the user know when events happen
     */
    public interface ControllerListener {
        void onOk();

        void onCancel();

        void onDismiss();
    }
}