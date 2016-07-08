package com.fieldnation.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderActivity;

/**
 * Created by Michael on 7/7/2016.
 */
public class EditSearchActivity extends AuthActionBarActivity {
    private static final String TAG = "EditSearchActivity";

    // UI
    private SearchEditText _searchEditText;
    private RefreshView _loadingView;

    // Services
    private WorkorderClient _workorderClient;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_edit_search;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        _searchEditText = (SearchEditText) findViewById(R.id.searchedittext);
        _searchEditText.setListener(_searchEditText_listener);

        _loadingView = (RefreshView) findViewById(R.id.loading_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());
    }

    private final SearchEditText.Listener _searchEditText_listener = new SearchEditText.Listener() {
        @Override
        public void startSearch(String searchString) {
            _loadingView.startRefreshing();
            _workorderClient.subGet(Long.parseLong(searchString));
            WorkorderClient.get(App.get(), Long.parseLong(searchString), false);
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
                ToastClient.toast(App.get(), "Couldn't find work order, please try again.", Toast.LENGTH_SHORT);
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
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_right, 0);
        }
    }
}
