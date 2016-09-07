package com.fieldnation.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.ui.ActionBarDrawerView;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.dialog.OneButtonDialog;

/**
 * Created by Michael on 7/7/2016.
 */
public class EditSearchActivity extends AuthActionBarActivity {
    private static final String TAG = "EditSearchActivity";

    // Ui
    private SearchEditScreen _searchEditScreen;

    // Dialog
    private OneButtonDialog _notAvailableDialog;

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

        _searchEditScreen = (SearchEditScreen) findViewById(R.id.searchEdit_screen);
        _searchEditScreen.setListener(_searchEditScreen_listener);

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
        _searchEditScreen.reset();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _notAvailableDialog.setData(getString(R.string.not_available),
                getString(R.string.workorder_not_found),
                getString(R.string.btn_close), null);
    }

    private final SearchEditScreen.Listener _searchEditScreen_listener = new SearchEditScreen.Listener() {
        @Override
        public void showNotAvailableDialog() {
            _notAvailableDialog.show();
        }
    };

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
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
