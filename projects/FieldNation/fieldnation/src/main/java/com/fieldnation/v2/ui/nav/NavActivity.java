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
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnjson.JsonObject;
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
import com.fieldnation.v2.ui.workorder.WorkOrderActivity;

import java.util.List;

/**
 * Created by Michael on 8/19/2016.
 */
public class NavActivity extends AuthSimpleActivity {
    private static final String TAG = "NavActivity";

    // Intent stuff
    public static final String INTENT_FIELD_WORKORDER_ID = WorkOrderActivity.INTENT_FIELD_WORKORDER_ID;
    public static final String INTENT_FIELD_ACTION = WorkOrderActivity.INTENT_FIELD_ACTION;
    public static final String INTENT_UI_UUID = WorkOrderActivity.INTENT_UI_UUID;
    public static final String ACTION_ATTACHMENTS = "ACTION_ATTACHMENTS";
    public static final String ACTION_MESSAGES = "ACTION_MESSAGES";
    public static final String ACTION_CONFIRM = "ACTION_CONFIRM";

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
    private boolean _isLoadingWorkOrder = false;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_v3_nav;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        Intent intent = getIntent();
        if (intent != null) {
            int _workOrderId = 0;
            // taking a link from e-mail/browser
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                try {
                    final List<String> segments = intent.getData().getPathSegments();
                    if (segments.size() > 1) {
                        if (segments.get(0).equals("wo")) {
                            _workOrderId = Integer.parseInt(segments.get(1));
                        } else if (segments.get(0).equals("workorder")) {
                            _workOrderId = Integer.parseInt(segments.get(2));
                        } else if (segments.get(0).equals("marketplace")) {
                            _workOrderId = Integer.parseInt(intent.getData().getQueryParameter("workorder_id"));
                        } else if (segments.get(0).equals("w") && segments.get(1).equals("r")) {
                            _workOrderId = Integer.parseInt(segments.get(2));
                        }
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }

            if (_workOrderId != 0) {
                _isLoadingWorkOrder = true;
                this.startActivity(
                        WorkOrderActivity.makeIntentShow(this, _workOrderId));
            } else {
                if (intent.hasExtra(INTENT_FIELD_WORKORDER_ID)) {
                    _workOrderId = intent.getIntExtra(INTENT_FIELD_WORKORDER_ID, 0);

                    if (intent.hasExtra(INTENT_FIELD_ACTION)) {
                        _isLoadingWorkOrder = true;
                        this.startActivity(
                                WorkOrderActivity.makeIntentShow(this, _workOrderId, intent.getStringExtra(INTENT_FIELD_ACTION), null));
                    } else {
                        _isLoadingWorkOrder = true;
                        this.startActivity(
                                WorkOrderActivity.makeIntentShow(this, _workOrderId, null, null));
                    }
                }
            }
        }

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

        _savedList = App.get().getLastVisitedWoL();

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
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        _recyclerView.onStart();
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        SavedList savedList = App.get().getLastVisitedWoL();
        if (_savedList == null) {
            NavActivity.this.setTitle(misc.capitalize("LOADING..."));
        } else if (savedList == null) {
            NavActivity.this.setTitle(misc.capitalize("LOADING..."));
            _savedList = null;
        } else if (savedList.getId().equals(_savedList.getId())) {
            NavActivity.this.setTitle(misc.capitalize(_savedList.getTitle()));
            SavedSearchTracker.onListChanged(App.get(), _savedList.getLabel());
        } else {
            _savedList = savedList;
            _recyclerView.startSearch(_savedList);
        }

        if (!_isLoadingWorkOrder && App.get().needsConfirmation()
                && App.get().confirmRemindMeExpired()) {
            launchConfirmActivity();
        } else if (_isLoadingWorkOrder) {
            _isLoadingWorkOrder = false;
        }
        _recyclerView.onResume();

        _workOrdersApi.sub();
        WorkordersWebApi.getWorkOrderLists(App.get(), true, false);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        _workOrdersApi.unsub();
        _recyclerView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        _recyclerView.onStop();
        super.onStop();
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
            App.get().setLastVisitedWoL(savedList);
            _recyclerView.startSearch(_savedList);
            NavActivity.this.setTitle(misc.capitalize(_savedList.getTitle()));
            SavedSearchTracker.onListChanged(App.get(), _savedList.getLabel());
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrderLists");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && methodName.equals("getWorkOrderLists")) {
                SavedList[] savedList = (SavedList[]) successObject;
                if (_savedList == null) {
                    _savedList = savedList[0];
                    App.get().setLastVisitedWoL(_savedList);
                    _recyclerView.startSearch(_savedList);
                    NavActivity.this.setTitle(misc.capitalize(_savedList.getTitle()));
                } else {
                    for (SavedList list : savedList) {
                        if (list.getId().equals(_savedList.getId())) {
                            _savedList = list;
                            NavActivity.this.setTitle(misc.capitalize(_savedList.getTitle()));
                            break;
                        }
                    }
                }
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static Intent startNewIntent(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, NavActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, NavActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Intent intentShowWorkOrder(Context context, int workOrderId) {
        Intent intent = new Intent(context, NavActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(/*Intent.FLAG_ACTIVITY_CLEAR_TOP |*/ Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        return intent;
    }


    public static Intent intentShowWorkOrder(Context context, int workOrderId, String action) {
        Intent intent = new Intent(context, NavActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);

        if (action != null)
            intent.putExtra(INTENT_FIELD_ACTION, action);
        return intent;
    }
}