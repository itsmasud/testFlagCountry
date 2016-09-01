package com.fieldnation.ui.nav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.AuthSimpleActivity;

/**
 * Created by Michael on 9/1/2016.
 */
public class AdditionalOptionsActivity extends AuthSimpleActivity {
    private static final String TAG = "AdditionalOptionsActivity";

    @Override
    public int getLayoutResource() {
        return R.layout.activity_additional_options;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(_toolbarNavication_listener);

        setTitle("Additional Options");
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onProfile(Profile profile) {

    }

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, AdditionalOptionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_right, 0);
        }
    }

}
