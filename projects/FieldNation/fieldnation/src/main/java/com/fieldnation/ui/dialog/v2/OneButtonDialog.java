package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fndialog.Dialog;

/**
 * Created by Michael on 9/19/2016.
 */
public class OneButtonDialog extends FrameLayout implements Dialog {
    private static final String TAG = "OneButtonDialog";

    private static final String PARAM_TITLE = "title";
    private static final String PARAM_BODY = "body";
    private static final String PARAM_BUTTON = "button";
    private static final String PARAM_RESPONSE = "response";
    private static final int PARAM_RESPONSE_OK = 0;
    private static final int PARAM_RESPONSE_DISMISS = 1;

    // Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private Button _button;

    public OneButtonDialog(Context context) {
        super(context);
        init();
    }

    public OneButtonDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OneButtonDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_v2_one_button, this);

        if (isInEditMode())
            return;

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _bodyTextView = (TextView) findViewById(R.id.body_textview);
        _button = (Button) findViewById(R.id.button);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void show(Bundle payload) {
        _titleTextView.setText(payload.getString(PARAM_TITLE));
        _bodyTextView.setText(payload.getString(PARAM_BODY));
        _button.setText(payload.getString(PARAM_BUTTON));
        setVisibility(VISIBLE);
    }

    @Override
    public void onRestoreDialogState(Parcelable savedState) {
    }

    @Override
    public Parcelable onSaveDialogState() {
        return Bundle.EMPTY;
    }

    @Override
    public void dismiss() {
        setVisibility(GONE);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public static class Controller extends com.fieldnation.fndialog.Controller {

        private ControllerListener _listener;

        public Controller(Context context) {
            super(context, OneButtonDialog.class);
        }

        public void setControllerListener(ControllerListener listener) {
            _listener = listener;
        }

        public static void show(Context context, String title, String body, String button) {
            Bundle params = new Bundle();
            params.putString(PARAM_TITLE, title);
            params.putString(PARAM_BODY, body);
            params.putString(PARAM_BUTTON, button);

            show(context, OneButtonDialog.class, params);
        }

        public static void dismiss(Context context) {
            dismiss(context, OneButtonDialog.class);
        }

        @Override
        protected void onComplete(Bundle response) {
            if (_listener != null) {
                switch (response.getInt(PARAM_RESPONSE)) {
                    case PARAM_RESPONSE_OK:
                        _listener.onOk();
                        break;
                    case PARAM_RESPONSE_DISMISS:
                        _listener.onDismiss();
                        break;
                }
            }
        }
    }

    public interface ControllerListener {
        void onOk();

        void onDismiss();
    }
}
