package com.fieldnation.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.ui.AuthSimpleActivity;

/**
 * Gives the user the ability to configure the app
 *
 * @author michael.carver
 */
public class SettingsActivity extends AuthSimpleActivity {
    private static final String TAG = "SettingsActivity";

    @Override
    public int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(_toolbarNavication_listener);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        if (getIntent() != null && getIntent().hasExtra("SHOW_LEGAL")) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content, new LegalSettingsFragment())
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content, new GeneralSettingsFragment())
                    .commit();
        }
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public static void startNew(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    public static void startNewLegal(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("SHOW_LEGAL", true);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}
