package com.fieldnation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.ui.workorder.MyWorkActivity;

/**
 * Created by michael.carver on 12/18/2014.
 */
public class SplashActivity extends AuthFragmentActivity {
    private static final String TAG = "ui.SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        Log.v(TAG, "onAuthentication");
        Intent intent = new Intent(SplashActivity.this, MyWorkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAuthenticationFailed(boolean networkDown) {
        Log.v(TAG, "onAuthenticationFailed");
        if (!networkDown)
            AuthTopicService.requestAuthentication(this);
    }

    @Override
    public void onAuthenticationInvalidated() {
        Log.v(TAG, "onAuthenticationInvalidated");
        AuthTopicService.requestAuthentication(this);
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        AuthTopicService.requestAuthInvalid(this);
    }

    @Override
    public void onRefresh() {
    }
}
