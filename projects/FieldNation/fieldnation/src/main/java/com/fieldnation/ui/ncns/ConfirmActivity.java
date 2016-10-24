package com.fieldnation.ui.ncns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.v2.ListEnvelope;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.data.v2.actions.Action;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.nav.NavActivity;
import com.fieldnation.ui.search.SearchResultScreen;

import java.util.List;

/**
 * Created by Michael on 10/3/2016.
 */

public class ConfirmActivity extends AuthSimpleActivity {
    private static final String TAG = "NavActivity";

    private static final String STATE_CURRENT_SEARCH = "STATE_CURRENT_SEARCH";

    // Ui
    private SearchResultScreen _recyclerView;
    private Toolbar _toolbar;
    private Button _doneButton;

    // Data
    private SavedSearchParams _currentSearch = null;

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

        _doneButton = (Button) findViewById(R.id.done_button);
        _doneButton.setOnClickListener(_doneButton_onClick);

        _recyclerView = (SearchResultScreen) findViewById(R.id.recyclerView);
        _recyclerView.setOnWorkOrderListReceivedListener(_workOrderList_listener);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CURRENT_SEARCH)) {
            _currentSearch = savedInstanceState.getParcelable(STATE_CURRENT_SEARCH);
        }

        if (_currentSearch == null) {
            _currentSearch = new SavedSearchParams()
                    .type(WorkOrderListType.CONFIRM_TOMORROW.getType())
                    .status(WorkOrderListType.CONFIRM_TOMORROW.getStatuses())
                    .title("Confirm Tomorrow's Work");
        }

        setTitle("Please confirm tomorrow's work orders");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_currentSearch != null)
            outState.putParcelable(STATE_CURRENT_SEARCH, _currentSearch);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(TAG, "onRestoreInstanceState");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_CURRENT_SEARCH)) {
                _currentSearch = savedInstanceState.getParcelable(STATE_CURRENT_SEARCH);
                _recyclerView.startSearch(_currentSearch);
                ConfirmActivity.this.setTitle(misc.capitalize(_currentSearch.title));
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        _recyclerView.startSearch(_currentSearch);
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
    public void onBackPressed() {
        // do nothing, you're stuck here.... muhahahah
    }

    private final SearchResultScreen.OnWorkOrderListReceivedListener _workOrderList_listener = new SearchResultScreen.OnWorkOrderListReceivedListener() {
        @Override
        public void OnWorkOrderListReceived(ListEnvelope envelope, List<WorkOrder> workOrders) {
            _doneButton.setVisibility(View.VISIBLE);

            if (envelope == null || envelope.getTotal() == 0) {
                _doneButton.setVisibility(View.VISIBLE);
                return;
            }

            for (WorkOrder wo : workOrders) {
                Action[] actions = wo.getPrimaryActions();
                if (actions != null) {
                    for (Action a : actions) {
                        if (a.getType() == Action.ActionType.CONFIRM) {
                            _doneButton.setVisibility(View.GONE);
                            return;
                        }
                    }
                }
            }
        }
    };

    private final View.OnClickListener _doneButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
