package com.fieldnation.v2.ui.dialog;

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
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.Dialog;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntools.misc;
import com.fieldnation.fntools.KeyedDispatcher;

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
    private static final String PARAM_EXTRA_DATA = "extraData";

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
    public void onStart() {
        super.onStart();
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
        _onCanceledDispatcher.dispatch(getUid());
        super.cancel();

        if (onCancel())
            dismiss(true);
    }

    private final View.OnClickListener _primary_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _onPrimaryDispatcher.dispatch(getUid());
            if (onPrimaryClick())
                dismiss(true);
        }
    };

    private final View.OnClickListener _secondary_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _onSecondaryDispatcher.dispatch(getUid());
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

    public static void show(Context context, String uid, int titleResId, int bodyResId,
                            int primaryButtonResId, int secondaryButtonResId, boolean isCancelable,
                            Parcelable extraData) {

        show(context, uid, TwoButtonDialog.class, titleResId, bodyResId, primaryButtonResId,
                secondaryButtonResId, isCancelable, extraData);
    }

    public static void show(Context context, String uid, Class<? extends Dialog> klass,
                            int titleResId, int bodyResId, int primaryButtonResId,
                            int secondaryButtonResId, boolean isCancelable, Parcelable extraData) {

        show(context, uid, klass, context.getString(titleResId), context.getString(bodyResId),
                context.getString(primaryButtonResId), context.getString(secondaryButtonResId),
                isCancelable, extraData);
    }

    public static void show(Context context, String uid, String title, String body,
                            String primaryButton, String secondaryButton, boolean isCancelable,
                            Parcelable extraData) {

        show(context, uid, TwoButtonDialog.class, title, body, primaryButton, secondaryButton,
                isCancelable, extraData);
    }

    public static void show(Context context, String uid, Class<? extends Dialog> klass,
                            String title, String body, String primaryButton,
                            String secondaryButton, boolean isCancelable, Parcelable extraData) {

        Bundle params = new Bundle();
        params.putString(PARAM_TITLE, title);
        params.putString(PARAM_BODY, body);
        params.putString(PARAM_PRIMARY_BUTTON, primaryButton);
        params.putString(PARAM_SECONDARY_BUTTON, secondaryButton);
        params.putBoolean(PARAM_CANCELABLE, isCancelable);
        params.putParcelable(PARAM_EXTRA_DATA, extraData);

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

    /*-************************************-*/
    /*-         Secondary Listener           -*/
    /*-************************************-*/
    public interface OnSecondaryListener {
        void onSecondary();
    }

    private static KeyedDispatcher<OnSecondaryListener> _onSecondaryDispatcher = new KeyedDispatcher<OnSecondaryListener>() {
        @Override
        public void onDispatch(OnSecondaryListener listener, Object... parameters) {
            listener.onSecondary();
        }
    };

    public static void addOnSecondaryListener(String uid, OnSecondaryListener onSecondaryListener) {
        _onSecondaryDispatcher.add(uid, onSecondaryListener);
    }

    public static void removeOnSecondaryListener(String uid, OnSecondaryListener onSecondaryListener) {
        _onSecondaryDispatcher.remove(uid, onSecondaryListener);
    }

    public static void removeAllOnSecondaryListener(String uid) {
        _onSecondaryDispatcher.removeAll(uid);
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
