package com.fieldnation.v2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.AuthSimpleActivity;

/**
 * Created by michaelcarver on 2/19/18.
 */

public class UnsyncedActivity extends AuthSimpleActivity {
    private static final String TAG = "UnsyncedActivity";

    // UI
    private Toolbar _toolbar;
    private UnsyncedScreen _unsyncedScreen;
    private ActionMenuItemView _finishMenu;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_unsynced;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setTitle("Unsynced Activity");
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setNavigationOnClickListener(_toolbarNavication_listener);

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText("SYNC ALL");

        _unsyncedScreen = (UnsyncedScreen) findViewById(R.id.unsyncedScreen);

        setTitle("Unsynced Activity");
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    @Override
    public int getToolbarId() {
        return 0;
    }

    @Override
    public int getOfflineBarId() {
        return 0;
    }

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new ApatheticOnMenuItemClickListener() {
        @Override
        public boolean onSingleMenuItemClick(MenuItem item) {
            return false;
        }
    };

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    public DialogManager getDialogManager() {
        return null;
    }

    @Override
    public void onProfile(Profile profile) {

    }

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, UnsyncedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
