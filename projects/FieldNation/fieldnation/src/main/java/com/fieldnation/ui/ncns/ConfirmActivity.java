package com.fieldnation.ui.ncns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.nav.NavActivity;
import com.fieldnation.ui.search.SearchResultScreen;

import java.util.Calendar;
import java.util.Hashtable;

/**
 * Created by Michael on 10/3/2016.
 */

public class ConfirmActivity extends AuthSimpleActivity {
    private static final String TAG = "NavActivity";

    private static final String STATE_CURRENT_SEARCH = "STATE_CURRENT_SEARCH";

    // Ui
    private SearchResultScreen _recyclerView;
    private Toolbar _toolbar;
    private CoordinatorLayout _layout;
    private AppBarLayout _appBarLayout;
    private Button _confirmButton;

    // Animations
    private Animation _ccw;
    private Animation _cw;

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

        _layout = (CoordinatorLayout) findViewById(R.id.main_content);

        _appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(null);

        _confirmButton = (Button) findViewById(R.id.confirm_button);
        _confirmButton.setOnClickListener(_confirmButton_onClick);
        _confirmButton.setEnabled(false);

        _recyclerView = (SearchResultScreen) findViewById(R.id.recyclerView);
        _recyclerView.setCheckboxEnabled(true);
        _recyclerView.setOnChildCheckChangedListener(_recycler_onCheckChanged);

        _ccw = AnimationUtils.loadAnimation(this, R.anim.rotate_180_ccw);
        _ccw.setAnimationListener(_ccw_animationListener);
        _cw = AnimationUtils.loadAnimation(this, R.anim.rotate_180_cw);
        _cw.setAnimationListener(_ccw_animationListener);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CURRENT_SEARCH)) {
            _currentSearch = savedInstanceState.getParcelable(STATE_CURRENT_SEARCH);
        }

        if (_currentSearch == null) {
            _currentSearch = new SavedSearchParams()
                    .type(WorkOrderListType.CONFIRM_TOMORROW.getType())
                    .status(WorkOrderListType.CONFIRM_TOMORROW.getStatuses())
                    .title("Confirm Tomorrow's Work");
        }

        _recyclerView.startSearch(_currentSearch);
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

    private final Animation.AnimationListener _ccw_animationListener = new DefaultAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            _ccw.cancel();
        }
    };

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return null;
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    public void onBackPressed() {
        // do nothing, you're stuck here.... muhahahah
    }

    private final View.OnClickListener _confirmButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Hashtable<Long, WorkOrder> checks = _recyclerView.getCheckWorkorders();

            for (Long woId : checks.keySet()) {
                WorkOrder wo = checks.get(woId);

                try {
                    Calendar cal = ISO8601.toCalendar(wo.getSchedule().getExact());
                    cal.add(Calendar.HOUR, 1);

                    WorkorderClient.actionConfirmAssignment(App.get(), woId,
                            wo.getSchedule().getBegin(), ISO8601.fromCalendar(cal), null, false);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            NavActivity.startNew(App.get());
            finish();
        }
    };

    private final SearchResultScreen.OnCheckChangeListener _recycler_onCheckChanged = new SearchResultScreen.OnCheckChangeListener() {
        @Override
        public void onChecked(Hashtable<Long, WorkOrder> checks) {
            _confirmButton.setEnabled(checks.size() > 0);
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, ConfirmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Intent startNewIntent(Context context) {
        Intent intent = new Intent(context, ConfirmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
