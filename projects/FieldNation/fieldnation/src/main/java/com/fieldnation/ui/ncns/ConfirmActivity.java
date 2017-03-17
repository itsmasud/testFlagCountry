package com.fieldnation.ui.ncns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.nav.NavActivity;
import com.fieldnation.v2.ui.search.SearchResultScreen;

/**
 * Created by Michael on 10/3/2016.
 */

public class ConfirmActivity extends AuthSimpleActivity {
    private static final String TAG = "ConfirmActivity";

    // Ui
    private SearchResultScreen _recyclerView;
    private Toolbar _toolbar;

    // Data
    private SavedList _savedList;

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

        setTitle("Tomorrow's Work");

        // TODO fill out _savedList;
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
            _recyclerView.startSearch(_savedList, new GetWorkOrdersOptions().fFlightboardTomorrow(true));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        _recyclerView.startSearch(_savedList, new GetWorkOrdersOptions().fFlightboardTomorrow(true));
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
                break;
            case R.id.done_menuitem:
                App.get().setNeedsConfirmation(false);
                NavActivity.startNew(App.get());
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
                    || workOrders.getResults() != null
                    || workOrders.getResults().length == 0) {
                return;
            }

            if (workOrders.getMetadata().getTotal()
                    > workOrders.getMetadata().getPage() * workOrders.getMetadata().getPerPage()) {
                // Todo need to load all the pages
            }


/*
TODO            for (WorkOrder wo : workOrders.getResults()) {
                Action[] actions = wo.getPrimaryActions();
                if (actions != null) {
                    for (Action a : actions) {
                        if (a.getType() == Action.ActionType.READY) {
                            _doneButton.setVisibility(View.GONE);
                            return;
                        }
                    }
                }
            }
*/
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