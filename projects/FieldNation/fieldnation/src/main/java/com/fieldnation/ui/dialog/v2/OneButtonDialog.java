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
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.Dialog;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.KeyedDispatcher;

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
    public void onStart() {
        super.onStart();
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
        _onPrimaryDispatcher.dispatch(getUid(), null);
        super.cancel();

        if (onCancel())
            dismiss(true);
    }

    private final View.OnClickListener _primaryButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "_primaryButton_onClick");
            _onCanceledDispatcher.dispatch(getUid(), null);
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

    public static void show(Context context, String uid, int titleResId, int bodyResId, int buttonResId, boolean isCancelable) {
        show(context, uid, OneButtonDialog.class, context.getString(titleResId), context.getString(bodyResId),
                context.getString(buttonResId), isCancelable);
    }

    public static void show(Context context, String uid, Class<? extends Dialog> klass, int titleResId, int bodyResId, int buttonResId, boolean isCancelable) {
        show(context, uid, klass, context.getString(titleResId), context.getString(bodyResId),
                context.getString(buttonResId), isCancelable);
    }

    public static void show(Context context, String uid, String title, String body, String button, boolean isCancelable) {
        show(context, uid, OneButtonDialog.class, title, body, button, isCancelable);
    }

    public static void show(Context context, String uid, Class<? extends Dialog> klass, String title, String body, String button, boolean isCancelable) {
        Bundle params = new Bundle();
        params.putString(PARAM_TITLE, title);
        params.putString(PARAM_BODY, body);
        params.putString(PARAM_BUTTON, button);
        params.putBoolean(PARAM_CANCELABLE, isCancelable);

        Controller.show(context, uid, klass, params);
    }

    public static void dismiss(Context context, String uid) {
        Controller.dismiss(context, uid);
    }

    /*-************************************-*/
    /*-         Primary Listener           -*/
    /*-************************************-*/
    public interface OnPrimaryListener {
        void onPrimary();
    }

    private static KeyedDispatcher<OnPrimaryListener> _onPrimaryDispatcher = new KeyedDispatcher<OnPrimaryListener>() {
        @Override
        public void onDispatch(OnPrimaryListener listener, Object... parameters) {
            listener.onPrimary();
        }
    };

    public static void addOnPrimaryListener(String uid, OnPrimaryListener onPrimaryListener) {
        _onPrimaryDispatcher.add(uid, onPrimaryListener);
    }

    public static void removeOnPrimaryListener(String uid, OnPrimaryListener onPrimaryListener) {
        _onPrimaryDispatcher.remove(uid, onPrimaryListener);
    }

    public static void removeAllOnPrimaryListener(String uid) {
        _onPrimaryDispatcher.removeAll(uid);
    }

    /*-*************************************-*/
    /*-         Canceled Listener           -*/
    /*-*************************************-*/
    public interface OnCanceledListener {
        void onCanceled();
    }

    private static KeyedDispatcher<OnCanceledListener> _onCanceledDispatcher = new KeyedDispatcher<OnCanceledListener>() {
        @Override
        public void onDispatch(OnCanceledListener listener, Object... parameters) {
            listener.onCanceled();
        }
    };

    public static void addOnCanceledListener(String uid, OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.add(uid, onCanceledListener);
    }

    public static void removeOnCanceledListener(String uid, OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.remove(uid, onCanceledListener);
    }

    public static void removeAllOnCanceledListener(String uid) {
        _onCanceledDispatcher.removeAll(uid);
    }

}
