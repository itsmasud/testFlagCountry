package com.fieldnation.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.ActionBarDrawerView;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;

/**
 * Created by Michael on 7/7/2016.
 */
public class EditSearchActivity extends AuthActionBarActivity {
    private static final String TAG = "EditSearchActivity";

    // UI
    private SearchEditText _searchEditText;
    private RefreshView _loadingView;
    private Button _actionButton;

    // Dialog
    private OneButtonDialog _notAvailableDialog;

    // Services
    private WorkorderClient _workorderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarDrawerView actionBarView = (ActionBarDrawerView) findViewById(R.id.actionbardrawerview);
        Toolbar toolbar = actionBarView.getToolbar();
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(_toolbarNavication_listener);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_edit_search;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        setTitle(R.string.work_order_search);
        _searchEditText = (SearchEditText) findViewById(R.id.searchedittext);
        _searchEditText.setListener(_searchEditText_listener);

        _loadingView = (RefreshView) findViewById(R.id.loading_view);

        _actionButton = (Button) findViewById(R.id.action_button);
        _actionButton.setOnClickListener(_action_onClick);

        _notAvailableDialog = OneButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":notAvailableDialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        _searchEditText.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        _notAvailableDialog.setData(getString(R.string.not_available),
                getString(R.string.workorder_not_found),
                getString(R.string.btn_close), null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());
    }

    private final View.OnClickListener _action_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                _workorderClient.subGet(Long.parseLong(_searchEditText.getText()));
                WorkorderClient.get(App.get(), Long.parseLong(_searchEditText.getText()), false);
                _loadingView.startRefreshing();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final SearchEditText.Listener _searchEditText_listener = new SearchEditText.Listener() {
        @Override
        public void startSearch(String searchString) {
            try {
                _workorderClient.subGet(Long.parseLong(searchString));
                WorkorderClient.get(App.get(), Long.parseLong(searchString), false);
                _loadingView.startRefreshing();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(Workorder workorder, boolean failed, boolean isCached) {
            _loadingView.refreshComplete();
            if (workorder == null || failed) {
                _notAvailableDialog.show();
            } else {
                _workorderClient.unsubGet(workorder.getWorkorderId());
                startActivity(
                        WorkorderActivity.makeIntentShow(EditSearchActivity.this, workorder.getWorkorderId()));
            }
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, EditSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_right, 0);
        }
    }
}
