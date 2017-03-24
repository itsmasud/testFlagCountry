package com.fieldnation.ui.ncns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;
import com.fieldnation.v2.ui.nav.NavActivity;
import com.fieldnation.v2.ui.search.SearchResultScreen;
import com.fieldnation.v2.ui.worecycler.WoPagingAdapter;

/**
 * Created by Michael on 10/3/2016.
 */

public class ConfirmActivity extends AuthSimpleActivity {
    private static final String TAG = "ConfirmActivity";

    // Dialogs
    private static final String DIALOG_REMIND_ME = TAG + ".remindMeDialog";

    // Ui
    private SearchResultScreen _recyclerView;
    private Toolbar _toolbar;

    // Data
    private SavedList _savedList;
    private boolean _needsConfirm = false;
    private GetWorkOrdersOptions _options =
            new GetWorkOrdersOptions()
                    .fFlightboardTomorrow(true);

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

        _recyclerView = (SearchResultScreen) findViewById(R.id.recyclerView);
        _recyclerView.setOnWorkOrderListReceivedListener(_workOrderList_listener);

        setTitle("Confirm Work");

        try {
            _savedList = new SavedList().id("workorders_assignments");
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
    protected void onStart() {
        super.onStart();

        TwoButtonDialog.addOnPrimaryListener(DIALOG_REMIND_ME, _remindMe_onOk);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _recyclerView.startSearch(_savedList, _options);
    }

    @Override
    protected void onStop() {
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_REMIND_ME, _remindMe_onOk);

        super.onStop();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remindme_menuitem:
                NavActivity.startNew(App.get());
                finish();
                break;
            case R.id.done_menuitem:
                if (!_needsConfirm) {
                    TwoButtonDialog.show(App.get(), DIALOG_REMIND_ME, "Remind Me", "You will be reminded in 30 minutes to confirm your work orders.", "OK", "CANCEL", true, null);
                } else {
                    ToastClient.toast(App.get(), "Please confirm and set ETAs before continuing", Toast.LENGTH_SHORT);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // do nothing, you're stuck here.... muhahahah
    }

    private final SearchResultScreen.OnWorkOrderListReceivedListener _workOrderList_listener = new SearchResultScreen.OnWorkOrderListReceivedListener() {
        @Override
        public void OnWorkOrderListReceived(WorkOrders workOrders) {
            if (workOrders == null
                    || workOrders.getResults() == null
                    || workOrders.getResults().length == 0) {
                return;
            }
            scanWorkOrders();
        }
    };

    private void scanWorkOrders() {
        Log.v(TAG, "scanWorkOrders");
        WoPagingAdapter adapter = _recyclerView.getAdapter();
        _needsConfirm = false;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            Object obj = adapter.getObject(i);
            if (obj instanceof WorkOrder) {
                WorkOrder wo = (WorkOrder) obj;
                if ((wo.getEta() != null && wo.getEta().getActionsSet().contains(ETA.ActionsEnum.CONFIRM))
                        || (wo.getEta() != null && wo.getEta().getActionsSet().contains(ETA.ActionsEnum.ADD))) {
                    _needsConfirm = true;
                    return;
                }
            }
        }
    }

    private final TwoButtonDialog.OnPrimaryListener _remindMe_onOk = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary() {
            App.get().setNeedsConfirmation(false);
            NavActivity.startNew(App.get());
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