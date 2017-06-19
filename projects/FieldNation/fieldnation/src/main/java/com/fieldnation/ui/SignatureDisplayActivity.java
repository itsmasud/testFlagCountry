package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by michael.carver on 12/9/2014.
 */
public class SignatureDisplayActivity extends AuthSimpleActivity {
    private static final String TAG = "SignatureDisplayActivity";

    // State
    private static final String STATE_SIGNATURE = "STATE_SIGNATURE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_SIGNATURE_ID = "STATE_SIGNATURE_ID";

    // Intent Params
    public static final String INTENT_PARAM_SIGNATURE = "ui.SignatureDisplayActivity:INTENT_PARAM_SIGNATURE";
    public static final String INTENT_PARAM_WORKORDER = "ui.SignatureDisplayActivity:INTENT_PARAM_WORKORDER";

    // Ui
    private SignatureView _signatureView;

    // Data
    private Signature _signature;
    private WorkOrder _workOrder;
    private long _signatureId = -1;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_signature_display;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        _signatureView = (SignatureView) findViewById(R.id.signature_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(INTENT_PARAM_SIGNATURE))
                _signatureId = extras.getLong(INTENT_PARAM_SIGNATURE);

            if (extras.containsKey(INTENT_PARAM_WORKORDER))
                _workOrder = extras.getParcelable(INTENT_PARAM_WORKORDER);

            populateUi();
        } else if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_SIGNATURE))
                _signature = savedInstanceState.getParcelable(STATE_SIGNATURE);

            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workOrder = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_SIGNATURE_ID))
                _signatureId = savedInstanceState.getLong(STATE_SIGNATURE_ID);

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
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_signature != null)
            outState.putParcelable(STATE_SIGNATURE, _signature);

        if (_workOrder != null)
            outState.putParcelable(STATE_WORKORDER, _workOrder);

        outState.putLong(STATE_SIGNATURE_ID, _signatureId);

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

        if (_workOrder == null)
            return;

        if (_workOrder.getSignatures().getResults() == null
                || _workOrder.getSignatures().getResults().length == 0)
            return;

        for (Signature signature : _workOrder.getSignatures().getResults()) {
            if (signature.getId() == _signatureId) {
                _signature = signature;
                break;
            }
        }

        if (_signature.getFormat().equals("svg")) {
            _signatureView.setSignatureSvg(_signature.getData(), true);
        } else {
            _signatureView.setSignatureJson(_signature.getData(), true);
        }
    }

    /*-*************************************-*/
    /*-                 Events              -*/
    /*-*************************************-*/
    public static void startIntent(Context context, long signatureId, WorkOrder workOrder) {
        Intent intent = new Intent(context, SignatureDisplayActivity.class);
        intent.setAction("DUMMY");
        intent.putExtra(INTENT_PARAM_SIGNATURE, signatureId);
        intent.putExtra(INTENT_PARAM_WORKORDER, workOrder);
        intent.setExtrasClassLoader(WorkOrder.class.getClassLoader());
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}