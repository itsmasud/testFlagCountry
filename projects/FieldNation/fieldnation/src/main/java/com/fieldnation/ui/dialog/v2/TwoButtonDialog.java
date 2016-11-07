package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fndialog.Dialog;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntools.misc;

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
    private static final String PARAM_EXTRA_DATA = "extraData";
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
    private Parcelable _extraData = null;

    public TwoButtonDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
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

        String body = payload.getString(PARAM_BODY);
        try {
            _bodyTextView.setText(misc.linkifyHtml(body, Linkify.ALL));
            _bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception ex) {
            _bodyTextView.setText(body);
        }
        _primaryButton.setText(payload.getString(PARAM_PRIMARY_BUTTON));
        _secondaryButton.setText(payload.getString(PARAM_SECONDARY_BUTTON));
        _isCancelable = payload.getBoolean(PARAM_CANCELABLE);
        _extraData = payload.getParcelable(PARAM_EXTRA_DATA);

        super.show(payload, animate);
    }

    public Parcelable getExtraData() {
        return _extraData;
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

    private final View.OnClickListener _primary_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_PRIMARY);
            onResult(response);
            if (onPrimaryClick())
                dismiss(true);
        }
    };

    private final View.OnClickListener _secondary_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESPONSE, PARAM_RESPONSE_SECONDARY);
            onResult(response);
            if (onSecondaryClick())
                dismiss(true);
        }
    };

    public boolean onCancel() {
        return true;
    }

    public boolean onPrimaryClick() {
        return true;
    }

    public boolean onSecondaryClick() {
        return true;
    }

    public static class Controller extends com.fieldnation.fndialog.Controller {
        public Controller(Context context, String uid) {
            super(context, TwoButtonDialog.class, uid);
        }

        public Controller(Context context, Class<? extends Dialog> klass, String uid) {
            super(context, klass, uid);
        }

        public static void show(Context context, String uid, int titleResId, int bodyResId, int primaryButtonResId,
                                int secondaryButtonResId, boolean isCancelable, Parcelable extraData) {

            show(context, uid, context.getString(titleResId), context.getString(bodyResId), context.getString(primaryButtonResId),
                    context.getString(secondaryButtonResId), isCancelable, extraData);
        }

        public static void show(Context context, String uid, String title, String body, String primaryButton,
                                String secondaryButton, boolean isCancelable, Parcelable extraData) {

            Bundle params = new Bundle();
            params.putString(PARAM_TITLE, title);
            params.putString(PARAM_BODY, body);
            params.putString(PARAM_PRIMARY_BUTTON, primaryButton);
            params.putString(PARAM_SECONDARY_BUTTON, secondaryButton);
            params.putBoolean(PARAM_CANCELABLE, isCancelable);
            params.putParcelable(PARAM_EXTRA_DATA, extraData);

            show(context, uid, TwoButtonDialog.class, params);
        }

        public static void dismiss(Context context, String uid) {
            dismiss(context, uid);
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
