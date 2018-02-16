package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by michael.carver on 12/9/2014.
 */
public class SignatureDisplayActivity extends AuthSimpleActivity {
    private static final String TAG = "SignatureDisplayActivity";

    // Ui
    private SignatureView _signatureView;

    // Data
    private Signature _signature;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_signature_display;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        _signatureView = (SignatureView) findViewById(R.id.signature_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("signature"))
                _signature = extras.getParcelable("signature");

            populateUi();
        } else if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("signature"))
                _signature = savedInstanceState.getParcelable("signature");

            populateUi();
        } else {
            finish();
        }
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public int getOfflineBarId() {
        return R.id.offline_bar_textview;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_signature != null)
            outState.putParcelable("signature", _signature);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateUi();
    }

    private void populateUi() {
        if (_signatureView == null)
            return;

        if (_signature == null)
            return;

        if (_signature.getFormat().equals("svg")) {
            _signatureView.setSignatureSvg(_signature.getData(), true);
        } else {
            _signatureView.setSignatureJson(_signature.getData(), true);
        }
    }

    /*-*************************************-*/
    /*-                 Events              -*/
    /*-*************************************-*/
    public static void startIntent(Context context, Signature signature) {
        Intent intent = new Intent(context, SignatureDisplayActivity.class);
        intent.setAction("DUMMY");
        intent.putExtra("signature", signature);
        intent.setExtrasClassLoader(WorkOrder.class.getClassLoader());
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ActivityClient.startActivity(intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}