package com.fieldnation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MyWorkActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, 1000);
    }

    @Override
    public void onAuthenticationFailed(boolean networkDown) {
        AuthTopicService.requestAuthentication(this);
    }

    @Override
    public void onAuthenticationInvalidated() {
        AuthTopicService.requestAuthentication(this);
    }


    @Override
    public void onRefresh() {
    }
}
