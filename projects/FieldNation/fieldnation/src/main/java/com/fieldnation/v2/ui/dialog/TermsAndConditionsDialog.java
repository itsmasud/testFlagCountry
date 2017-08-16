package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntools.KeyedDispatcher;

/**
 * Created by mc on 7/21/17.
 */

public class TermsAndConditionsDialog extends SimpleDialog {
    private static final String TAG = "TermsAndConditionsDialog";

    // Ui
    private Button _reviewTermsOfServiceButton;
    private Button _acceptButton;

    public TermsAndConditionsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_toc, container, false);

        _reviewTermsOfServiceButton = v.findViewById(R.id.review_tos_button);
        _acceptButton = v.findViewById(R.id.accept_button);

        return v;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        _reviewTermsOfServiceButton.setOnClickListener(_reviewTermsOfServices_onClick);
        _acceptButton.setOnClickListener(_accept_onClick);
    }

    public static void show(Context context, String uid) {
        Controller.show(context, uid, TermsAndConditionsDialog.class, null);
    }

    /*-*****************************-*/
    /*-				Events			-*/
    /*-*****************************-*/
    private final View.OnClickListener _reviewTermsOfServices_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=qualityassurance"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };

    private final View.OnClickListener _accept_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            App.get().setToCAccepted();
            _onOkDispatcher.dispatch(getUid());
            dismiss(true);
        }
    };

    /*-*********************-*/
    /*-         Ok          -*/
    /*-*********************-*/
    public interface OnOkListener {
        void onOk();
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk();
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
}
