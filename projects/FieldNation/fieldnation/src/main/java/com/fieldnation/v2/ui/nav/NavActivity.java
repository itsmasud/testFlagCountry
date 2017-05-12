package com.fieldnation.v2.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.SavedSearchTracker;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.gcm.MyGcmListenerService;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.nav.SearchToolbarView;
import com.fieldnation.ui.ncns.ConfirmActivity;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.ui.nav.SavedSearchList.OnSavedListChangeListener;
import com.fieldnation.v2.ui.search.SearchResultScreen;

/**
 * Created by Michael on 8/19/2016.
 */
public class NavActivity extends AuthSimpleActivity {
    private static final String TAG = "NavActivity";

    private static final String STATE_CURRENT_SEARCH = "STATE_CURRENT_SEARCH";

    // Ui
    private SearchResultScreen _recyclerView;
    private Toolbar _toolbar;
    private SavedSearchList _searchesView;
    private IconFontTextView _arrowTextView;
    private CoordinatorLayout _layout;
    private AppBarLayout _appBarLayout;
    private SearchToolbarView _searchToolbarView;

    // Animations
    private Animation _ccw;
    private Animation _cw;

    // Data
    private SavedList _savedList = null;
    private WorkordersWebApi _workOrderClient;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_v3_nav;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        _layout = (CoordinatorLayout) findViewById(R.id.main_content);

        _appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(null);
        _toolbar.setOnClickListener(_toolbar_onClick);

        _searchToolbarView = (SearchToolbarView) findViewById(R.id.searchToolbarView);

        _arrowTextView = (IconFontTextView) findViewById(R.id.arrow_textview);

        _searchesView = (SavedSearchList) findViewById(R.id.searchesView);
        _searchesView.setOnHideListener(_onHideListener);
        _searchesView.setOnShowListener(_onShowListener);
        _searchesView.setOnSavedSearchParamsChangeListener(_onSearchChangedListener);

        _recyclerView = (SearchResultScreen) findViewById(R.id.recyclerView);

        _ccw = AnimationUtils.loadAnimation(this, R.anim.rotate_180_ccw);
        _cw = AnimationUtils.loadAnimation(this, R.anim.rotate_180_cw);

        //_arrowTextView.startAnimation(_cw);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CURRENT_SEARCH)) {
            _savedList = savedInstanceState.getParcelable(STATE_CURRENT_SEARCH);
        }

        SavedSearchTracker.onShow(App.get());

        if (_savedList != null) {
            _recyclerView.startSearch(_savedList);
            NavActivity.this.setTitle(misc.capitalize(_savedList.getTitle()));
            SavedSearchTracker.onListChanged(App.get(), _savedList.getLabel());
        } else {
            NavActivity.this.setTitle(misc.capitalize("LOADING..."));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (App.get().needsConfirmation()) {
            launchConfirmActivity();
        }

        _workOrderClient = new WorkordersWebApi(_workOrderClient_listener);
        _workOrderClient.connect(App.get());
    }

    @Override
    protected void onPause() {
        if (_workOrderClient != null) _workOrderClient.disconnect(App.get());
        super.onPause();
    }

    private void launchConfirmActivity() {
        MyGcmListenerService.clearConfirmPush(this);
        App.get().setNeedsConfirmation(false);
        ConfirmActivity.startNew(this);
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_savedList != null)
            outState.putParcelable(STATE_CURRENT_SEARCH, _savedList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(TAG, "onRestoreInstanceState");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_CURRENT_SEARCH)) {
                _savedList = savedInstanceState.getParcelable(STATE_CURRENT_SEARCH);
                _recyclerView.startSearch(_savedList);
                NavActivity.this.setTitle(misc.capitalize(_savedList.getTitle()));
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.search_menuitem).getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_searchToolbarView.isShowing()) {
                    _searchToolbarView.hide();
                } else {
                    _searchToolbarView.show();
                    _searchesView.hide();
                }
            }
        });

        return true;
    }

    private void showDrawer() {
        if (!_searchesView.isShowing()) {
            _searchesView.show();
            _searchToolbarView.hide();
        }
    }

    private void hideDrawer() {
        if (_searchesView.isShowing()) {
            _searchesView.hide();
            Log.v(TAG, "hideDrawer");
        }
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_searchesView.isShowing()) {
                hideDrawer();
            } else {
                showDrawer();
            }
        }
    };

    private final SavedSearchList.OnHideListener _onHideListener = new SavedSearchList.OnHideListener() {
        @Override
        public void onHide() {
            _arrowTextView.startAnimation(_ccw);
        }
    };

    private final SavedSearchList.OnShowListener _onShowListener = new SavedSearchList.OnShowListener() {
        @Override
        public void onShow() {
            _arrowTextView.startAnimation(_cw);
        }
    };

    private final OnSavedListChangeListener _onSearchChangedListener = new OnSavedListChangeListener() {
        @Override
        public void onChange(SavedList savedList) {
            _savedList = savedList;
            _recyclerView.startSearch(_savedList);
            NavActivity.this.setTitle(misc.capitalize(_savedList.getTitle()));
            SavedSearchTracker.onListChanged(App.get(), _savedList.getLabel());
        }
    };

    private final WorkordersWebApi.Listener _workOrderClient_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderClient.subWorkordersWebApi();
            WorkordersWebApi.getWorkOrderLists(App.get(), true, false);
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("getWorkOrderLists")) {
                SavedList[] savedList = (SavedList[]) successObject;
                if (_savedList == null) {
                    _savedList = savedList[0];
                    _recyclerView.startSearch(_savedList);
                    NavActivity.this.setTitle(misc.capitalize(_savedList.getTitle()));
                }
            }
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, NavActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}