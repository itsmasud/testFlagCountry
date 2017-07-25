package com.fieldnation.ui.ncns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpScreenDisplayUiContext;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.menu.DoneMenuButton;
import com.fieldnation.ui.menu.RemindMeMenuButton;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.nav.NavActivity;

/**
 * Created by Michael on 10/3/2016.
 */

public class ConfirmActivity extends AuthSimpleActivity {
    private static final String TAG = "ConfirmActivity";

    // Ui
    private ConfirmResultScreen _recyclerView;
    private Toolbar _toolbar;
    private Button _doneButton;
    private Button _remindMeButton;

    // Data
    private SavedList _savedList;
    private boolean _needsConfirm = false;
    private GetWorkOrdersOptions _options = new GetWorkOrdersOptions().fFlightboardTomorrow(true);

    @Override
    public int getLayoutResource() {
        return R.layout.activity_confirm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(null);

        _recyclerView = (ConfirmResultScreen) findViewById(R.id.recyclerView);
        _recyclerView.setOnWorkOrderListReceivedListener(_workOrderList_listener);

        setTitle("Confirm Work");

        App.get().getSpUiContext().page = "Confirm Work";

        try {
            _savedList = new SavedList()
                    .id("workorders_assignments")
                    .label("assigned");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(TAG, "onRestoreInstanceState");
        if (savedInstanceState != null) {
            _recyclerView.startSearch(_savedList, _options);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        _recyclerView.onResume();
        _recyclerView.startSearch(_savedList, _options);
    }

    @Override
    protected void onPause() {
        _recyclerView.onPause();
        super.onPause();
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
    public void onProfile(Profile profile) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirm, menu);
        _doneButton = ((DoneMenuButton) menu.findItem(R.id.done_menuitem).getActionView()).getButton();
        _doneButton.setOnClickListener(_doneButton_onClick);
        _doneButton.setEnabled(false);
        _remindMeButton = ((RemindMeMenuButton) menu.findItem(R.id.remindme_menuitem).getActionView()).getButton();
        _remindMeButton.setOnClickListener(_remindMeButton_onClick);
        _remindMeButton.setEnabled(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        // do nothing, you're stuck here.... muhahahah
    }

    private final ConfirmResultScreen.OnWorkOrderListReceivedListener _workOrderList_listener = new ConfirmResultScreen.OnWorkOrderListReceivedListener() {
        @Override
        public void OnWorkOrderListReceived(final WorkOrders workOrders) {
            if (workOrders == null) {
                return;
            }

            _doneButton.post(new Runnable() {
                @Override
                public void run() {
                    if (workOrders.getActionsSet().contains(WorkOrders.ActionsEnum.REMIND)) {
                        _doneButton.setEnabled(false);
                        _remindMeButton.setEnabled(true);
                    } else {
                        _doneButton.setEnabled(true);
                        _remindMeButton.setEnabled(false);
                    }
                }
            });
        }
    };

    private final View.OnClickListener _doneButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!_needsConfirm) {
                App.get().setNeedsConfirmation(false);
                NavActivity.startNew(App.get());
                finish();
            } else {
                ToastClient.toast(App.get(), "Please confirm and set ETAs before continuing", Toast.LENGTH_SHORT);
            }
        }
    };

    private final View.OnClickListener _remindMeButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            App.get().setNeedsConfirmation(false);
            RemindMeBroadcastReceiver.registerAlarm(App.get());
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpScreenDisplayUiContext.Builder().page("Confirm Work Screen").build())
                    .addContext(new SpUIContext.Builder()
                            .elementAction("Click")
                            .elementIdentity("Remind Me Action")
                            .elementType("Button")
                            .page("Confirm Work Screen")
                            .build())
                    .build()
            );
            NavActivity.startNew(App.get());
            finish();
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, ConfirmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Intent startNewIntent(Context context) {
        Intent intent = new Intent(context, ConfirmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}