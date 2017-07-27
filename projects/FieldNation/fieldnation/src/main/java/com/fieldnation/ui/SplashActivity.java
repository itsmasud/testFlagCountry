package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.ncns.ConfirmActivity;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.nav.NavActivity;

/**
 * Created by michael.carver on 12/18/2014.
 */
public class SplashActivity extends AuthSimpleActivity {
    private static final String TAG = "SplashActivity";

    private static final String STATE_PROFILE = "STATE_PROFILE";
    private static final String STATE_IS_AUTH = "STATE_IS_AUTH";
    private static final String STATE_CONFIRM = "STATE_CONFIRM";

    private Profile _profile = null;
    private boolean _isAuth = false;
    private boolean _calledMyWork = false;
    private AuthTopicClient _authClient;
    private WorkordersWebApi _workOrdersApi;
    private boolean _gotConfirmList = false;

    public SplashActivity() {
        super();
        Log.v(TAG, "Construct");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_IS_AUTH)) {
                _isAuth = savedInstanceState.getBoolean(STATE_IS_AUTH);
            }
            if (savedInstanceState.containsKey(STATE_PROFILE)) {
                _profile = savedInstanceState.getParcelable(STATE_PROFILE);
            }
        }

        final ImageView fnLogo = (ImageView) findViewById(R.id.logo_imageview);
        final int reqHeight = fnLogo.getLayoutParams().height;
        fnLogo.setImageBitmap(MemUtils.getMemoryEfficientBitmap(this, R.drawable.fn_logo, reqHeight));

        Log.v(TAG, "onFinishCreate");
    }

    @Override
    public int getToolbarId() {
        return 0;
    }

    @Override
    public DialogManager getDialogManager() {
        return null;
    }

    @Override
    public boolean doPermissionsChecks() {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_IS_AUTH, _isAuth);
        if (_profile != null) {
            outState.putParcelable(STATE_PROFILE, _profile);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProfile(Profile profile) {
        Log.e(TAG, "SplashActivity#onProfile");

        if (profile != null)
            Log.v(TAG, profile.toJson().display());

        if (!profile.isProvider()) {
            Toast.makeText(SplashActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
            AuthTopicClient.removeCommand(SplashActivity.this);
            return;
        }
        _profile = profile;
        doNextStep();
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        _calledMyWork = false;
        startService(new Intent(this, AuthTopicService.class));

        _authClient = new AuthTopicClient(_authTopic_listener);
        _authClient.connect(App.get());

        _workOrdersApi = new WorkordersWebApi(_workOrdersApi_listener);
        _workOrdersApi.connect(App.get());

        AuthTopicClient.requestCommand(App.get());
    }

    @Override
    protected void onStop() {
        if (_authClient != null) _authClient.disconnect(App.get());
        if (_workOrdersApi != null) _workOrdersApi.disconnect(App.get());
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            _authClient.subAuthStateChange();
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            Log.v(TAG, "onAuthenticated");
            _isAuth = true;
            doNextStep();
        }

        @Override
        public void onNotAuthenticated() {
            Log.v(TAG, "onNotAuthenticated");
            AuthTopicClient.requestCommand(SplashActivity.this);
        }
    };

    private void doNextStep() {
        Log.v(TAG, "doNextStep 1");
        if (!_isAuth)
            return;

        Log.v(TAG, "doNextStep 2");
        if (_profile == null) {
            ProfileClient.get(this);
            return;
        }

        Log.v(TAG, "doNextStep 3");

        if (_profile.isProvider() && _gotConfirmList && !_calledMyWork) {
            Log.v(TAG, "doNextStep 4");
            if (App.get().needsConfirmation()) {
                Log.v(TAG, "doNextStep 5a");
                _calledMyWork = true;
                ConfirmActivity.startNew(this);
                finish();

            } else {
                Log.v(TAG, "doNextStep 5b");
                _calledMyWork = true;
                NavActivity.startNew(this);
                finish();
            }
        }
    }

    private final WorkordersWebApi.Listener _workOrdersApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            _workOrdersApi.subWorkordersWebApi();

            GetWorkOrdersOptions opts = new GetWorkOrdersOptions();
            opts.setPerPage(25);
            opts.setList("workorders_assignments");
            opts.setFFlightboardTomorrow(true);
            opts.setPage(1);

            WorkordersWebApi.getWorkOrders(App.get(), opts, false, false);
        }

        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrders");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            Log.v(TAG, "onComplete");

            if (methodName.equals("getWorkOrders")
                    && success
                    && successObject != null
                    && successObject instanceof WorkOrders) {
                Log.v(TAG, "onComplete getWorkOrders");

                WorkOrders workOrders = (WorkOrders) successObject;

                if (!"workorders_assignments".equals(workOrders.getMetadata().getList())) {
                    return;
                }
                _gotConfirmList = true;
                if (workOrders.getMetadata().getTotal() != null
                        && workOrders.getMetadata().getTotal() > 0) {
                    Log.v(TAG, "onComplete setNeedsConfirmation");
                    App.get().setNeedsConfirmation(true);
                }
                doNextStep();
            }
        }
    };

    public static void startNew(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}