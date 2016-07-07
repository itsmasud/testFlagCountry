package com.fieldnation.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.ui.AuthActionBarActivity;

/**
 * Created by Michael on 7/7/2016.
 */
public class EditSearchActivity extends AuthActionBarActivity {
    private static final String TAG = "EditSearchActivity";

    @Override
    public int getLayoutResource() {
        return R.layout.activity_edit_search;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, EditSearchActivity.class);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_right, 0);
        }
    }
}
