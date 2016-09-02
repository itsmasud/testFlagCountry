package com.fieldnation.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.search.SearchResultScreen;

/**
 * Created by Michael on 8/19/2016.
 */
public class NavActivity extends AuthSimpleActivity {
    private static final String TAG = "NavActivity";

    // Ui
    private SearchResultScreen _recyclerView;
    private Toolbar _toolbar;
    private SavedSearchList _searchesView;
    private IconFontTextView _arrowTextView;
    private CoordinatorLayout _layout;
    private AppBarLayout _appBarLayout;

    // Animations
    private Animation _ccw;
    private Animation _cw;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_v3_nav;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _layout = (CoordinatorLayout) findViewById(R.id.main_content);

        _appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setOnClickListener(_toolbar_onClick);

        _arrowTextView = (IconFontTextView) findViewById(R.id.arrow_textview);

        _searchesView = (SavedSearchList) findViewById(R.id.searchesView);
        _searchesView.setOnHideListener(_onHideListener);
        _searchesView.setOnShowListener(_onShowListener);
        _searchesView.setOnSavedSearchParamsChangeListener(_onSearchChangedListener);

        _recyclerView = (SearchResultScreen) findViewById(R.id.recyclerView);

        _ccw = AnimationUtils.loadAnimation(this, R.anim.rotate_180_ccw);
        _ccw.setAnimationListener(_ccw_animationListener);
        _cw = AnimationUtils.loadAnimation(this, R.anim.rotate_180_cw);
        _cw.setAnimationListener(_ccw_animationListener);

        _arrowTextView.startAnimation(_cw);

        SavedSearchParams savedSearchParams = new SavedSearchParams()
                .type(WorkOrderListType.ASSIGNED.getType())
                .status(WorkOrderListType.ASSIGNED.getStatuses())
                .title("Assigned");

        _recyclerView.startSearch(savedSearchParams);
        setTitle("Available");
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
    public void onProfile(Profile profile) {
    }

    private void showDrawer() {
        if (_searchesView.getVisibility() != View.VISIBLE) {
            _searchesView.show();
        }
    }

    private void hideDrawer() {
        if (_searchesView.getVisibility() != View.GONE) {
            _searchesView.hide();
            Log.v(TAG, "hideDrawer");
        }
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_searchesView.getVisibility() == View.GONE) {
                showDrawer();
            } else {
                hideDrawer();
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

    private final SavedSearchList.OnSavedSearchParamsChangeListener _onSearchChangedListener = new SavedSearchList.OnSavedSearchParamsChangeListener() {
        @Override
        public void onChange(SavedSearchParams params) {
            _recyclerView.startSearch(params);
            NavActivity.this.setTitle(misc.capitalize(params.title));
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        // misc.printStackTrace("startNew");
        Intent intent = new Intent(context, NavActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}