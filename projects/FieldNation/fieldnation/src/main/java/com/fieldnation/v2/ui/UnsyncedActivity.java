package com.fieldnation.v2.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;

/**
 * Created by michaelcarver on 2/19/18.
 */

public class UnsyncedActivity extends AuthSimpleActivity {
    private static final String TAG = "UnsyncedActivity";

    // Dialogs
    private static final String DIALOG_SYNC_WARNING = TAG + ".DIALOG_SYNC_WARNING";

    // UI
    private Toolbar _toolbar;
    private OverScrollRecyclerView _recyclerView;
    private ActionMenuItemView _finishMenu;

    // Data
    private UnsyncedAdapter _unsyncedAdapter = new UnsyncedAdapter();


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

        _recyclerView = (OverScrollRecyclerView) findViewById(R.id.recyclerView);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        _recyclerView.setAdapter(_unsyncedAdapter);

        setTitle("Unsynced Activity");
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            _unsyncedAdapter.refresh();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCASE_ON_CHANGE));
        _unsyncedAdapter.refresh();
        _appMessagingClient.subOfflineMode();
        TwoButtonDialog.addOnPrimaryListener(DIALOG_SYNC_WARNING, _syncWarning_onPrimary);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCASE_ON_CHANGE));
        _appMessagingClient.unsubOfflineMode();
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_SYNC_WARNING, _syncWarning_onPrimary);
        super.onStop();
    }

    @Override
    public int getToolbarId() {
        return 0;
    }

    @Override
    public int getOfflineBarId() {
        return R.id.offline_bar_textview;
    }

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new ApatheticOnMenuItemClickListener() {
        @Override
        public boolean onSingleMenuItemClick(MenuItem item) {
            if (App.get().getOfflineState() == App.OfflineState.OFFLINE) {
                TwoButtonDialog.show(App.get(), DIALOG_SYNC_WARNING, "Sync Activity",
                        "Offline mode will be turned off and your unsynced activity list of " + _unsyncedAdapter.getWorkOrderCount() + " work orders including all attachments will be uploaded. Data rates may apply.",
                        "CONTINUE", "CANCEL", true, null);
            } else if (App.get().getOfflineState() == App.OfflineState.SYNC) {
                TwoButtonDialog.show(App.get(), DIALOG_SYNC_WARNING, "Sync Activity",
                        "You are about to upload your unsynced activity list of " + _unsyncedAdapter.getWorkOrderCount() + " work orders including all attachments. Data rates may apply.",
                        "CONTINUE", "CANCEL", true, null);
            }

            return false;
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _syncWarning_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setOfflineMode(App.OfflineState.UPLOADING);
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
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    public void onProfile(Profile profile) {

    }

    private final AppMessagingClient _appMessagingClient = new AppMessagingClient() {
        @Override
        public void onOfflineMode(App.OfflineState state) {
            if (state == App.OfflineState.NORMAL)
                finish();
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, UnsyncedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
