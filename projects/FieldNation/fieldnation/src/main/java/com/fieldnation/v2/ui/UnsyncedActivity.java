package com.fieldnation.v2.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.FnToolBarView;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.menu.DoneMenuButton;
import com.fieldnation.ui.menu.RemindMeMenuButton;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;

/**
 * Created by michaelcarver on 2/19/18.
 */

public class UnsyncedActivity extends AuthSimpleActivity {
    private static final String TAG = "UnsyncedActivity";

    // Dialogs
    private static final String DIALOG_SYNC_WARNING = TAG + ".DIALOG_SYNC_WARNING";
    private static final String DIALOG_DELETE_UNSYNCED = TAG + ".DIALOG_DELETE_UNSYNCED";

    // UI
    private FnToolBarView _fnToolbarView;
    private OverScrollRecyclerView _recyclerView;
    private ActionMenuItemView _finishMenu;

    // Data
    private UnsyncedAdapter _unsyncedAdapter = new UnsyncedAdapter();

    private boolean _runTimer = false;
    private boolean _isRunning = false;
    private Handler _handler = new Handler();

    @Override
    public int getLayoutResource() {
        return R.layout.activity_unsynced;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _fnToolbarView = (FnToolBarView) findViewById(R.id.fnToolbar);
//        _fnToolbarView.getToolbar().inflateMenu(R.menu.dialog);
        _fnToolbarView.getToolbar().setOnMenuItemClickListener(_menu_onClick);
        _fnToolbarView.getToolbar().setTitle("Unsynced Activity");
        _fnToolbarView.getToolbar().setNavigationIcon(R.drawable.back_arrow);
        _fnToolbarView.getToolbar().setNavigationOnClickListener(_toolbarNavication_listener);

        _finishMenu = _fnToolbarView.getToolbar().findViewById(R.id.primary_menu);
//        _finishMenu.setText("SYNC ALL");

        _recyclerView = (OverScrollRecyclerView) findViewById(R.id.recyclerView);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        _recyclerView.setAdapter(_unsyncedAdapter);

        setTitle("Unsynced Activity");
        _runTimer = true;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startTimer();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCAST_ON_CHANGE));
        _unsyncedAdapter.refresh();
        _unsyncedAdapter.setListener(_unsynced_onLongClick);
        _appMessagingClient.subOfflineMode();
        TwoButtonDialog.addOnPrimaryListener(DIALOG_SYNC_WARNING, _syncWarning_onPrimary);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_UNSYNCED, _twoButtonDialog_deleteUnsynced);

        startTimer();
    }

    @Override
    protected void onResume() {
        startTimer();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCAST_ON_CHANGE));
        _appMessagingClient.unsubOfflineMode();
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_SYNC_WARNING, _syncWarning_onPrimary);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_UNSYNCED, _twoButtonDialog_deleteUnsynced);
        _runTimer = false;
        super.onStop();
    }

    private void startTimer() {
        if (_runTimer && !_isRunning) {
            _isRunning = true;
            _handler.postDelayed(_timer, 500);
        }
    }

    private final Runnable _timer = new Runnable() {
        @Override
        public void run() {
            if (!_runTimer)
                return;

            _unsyncedAdapter.refresh();

            _isRunning = false;
        }
    };

    @Override
    public int getFnToolbarViewId() {
        return R.id.fnToolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.unsynced, menu);
        return true;
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

    private final UnsyncedAdapter.Listener _unsynced_onLongClick = new UnsyncedAdapter.Listener() {
        @Override
        public void onLongClick(View v) {
            String activityName = (String) v.getTag(R.string.const_activity_name);
            WebTransaction webTransaction = (WebTransaction) v.getTag(R.string.const_web_transaction);
            if (misc.isEmptyOrNull(activityName) || webTransaction == null) return;

            TwoButtonDialog.show(App.get(), DIALOG_DELETE_UNSYNCED,
                    getString(R.string.dialog_delete_unsynced_title),
                    getString(R.string.dialog_delete_unsynced_body, activityName),
                    getString(R.string.btn_delete), getString(R.string.btn_cancel), true, webTransaction);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteUnsynced = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            if (extraData == null) return;
            WebTransaction.delete(((WebTransaction) extraData).getId());
        }
    };


    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, UnsyncedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
