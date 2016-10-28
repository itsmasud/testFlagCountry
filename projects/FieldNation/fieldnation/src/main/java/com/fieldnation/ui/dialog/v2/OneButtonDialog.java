package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller.Listener;
import com.fieldnation.fndialog.Dialog;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

/**
 * Created by Michael on 9/19/2016.
 */
public class OneButtonDialog extends SimpleDialog {
    private static final String TAG = "OneButtonDialog";

    private static final String PARAM_TITLE = "title";
    private static final String PARAM_BODY = "body";
    private static final String PARAM_BUTTON = "button";
    private static final String PARAM_RESPONSE = "response";
    private static final String PARAM_CANCELABLE = "cancelable";
    private static final int PARAM_RESPONSE_PRIMARY = 0;
    private static final int PARAM_RESPONSE_CANCEL = 1;

    // Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private Button _primaryButton;

    // Data
    private boolean _isCancelable = true;

    public OneButtonDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup parent) {
        View v = inflater.inflate(R.layout.dialog_v2_one_button, parent, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);
        _primaryButton = (Button) v.findViewById(R.id.primary1_button);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        _primaryButton.setOnClickListener(_primaryButton_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _titleTextView.setText(payload.getString(PARAM_TITLE));

        String body = payload.getString(PARAM_BODY);
        try {
            _bodyTextView.setText(misc.linkifyHtml(body, Linkify.ALL));
            _bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception ex) {
            _bodyTextView.setText(body);
        }
        _primaryButton.setText(payload.getString(PARAM_BUTTON));
        _isCancelable = payload.getBoolean(PARAM_CANCELABLE);

        super.show(payload, animate);
    }

    @Override
    public boolean isCancelable() {
        return _isCancelable;
    }

    @Override
    public void cancel() {
        Bundle response = new Bundle();
        response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_CANCEL);
        onResult(response);
        super.cancel();

        if (onCancel())
            dismiss(true);
    }

    private final View.OnClickListener _primaryButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "_primaryButton_onClick");
            Bundle response = new Bundle();
            response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_PRIMARY);
            onResult(response);
            if (onPrimaryClick())
                dismiss(true);
        }
    };

    public boolean onPrimaryClick() {
        return true;
    }

    public boolean onCancel() {
        return true;
    }

    public static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context, String uid) {
            super(context, OneButtonDialog.class, uid);
        }

        public Controller(Context context, Class<? extends Dialog> klass, String uid) {
            super(context, klass, uid);
        }

        public static void show(Context context, String uid, int titleResId, int bodyResId, int buttonResId, boolean isCancelable) {
            show(context, uid, context.getString(titleResId), context.getString(bodyResId),
                    context.getString(buttonResId), isCancelable);
        }

        public static void show(Context context, String uid, String title, String body, String button, boolean isCancelable) {
            Bundle params = new Bundle();
            params.putString(PARAM_TITLE, title);
            params.putString(PARAM_BODY, body);
            params.putString(PARAM_BUTTON, button);
            params.putBoolean(PARAM_CANCELABLE, isCancelable);

            show(context, uid, OneButtonDialog.class, params);
        }

        public static void dismiss(Context context, String uid) {
            dismiss(context, uid);
        }
    }

    public static abstract class ControllerListener implements Listener {
        @Override
        public void onComplete(Bundle response) {
            switch (response.getInt(PARAM_RESPONSE)) {
                case PARAM_RESPONSE_PRIMARY:
                    onPrimary();
                    break;
                case PARAM_RESPONSE_CANCEL:
                    onCancel();
                    break;
                default:
                    break;
            }
        }

        public abstract void onPrimary();

        public abstract void onCancel();
    }
}
