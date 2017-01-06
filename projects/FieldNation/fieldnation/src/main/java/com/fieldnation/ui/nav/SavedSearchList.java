package com.fieldnation.ui.nav;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.service.data.savedsearch.SavedSearchClient;

import java.util.List;

/**
 * Created by Michael on 8/25/2016.
 */
@DefaultBehavior(ToolbarMenuBehavior.class)
public class SavedSearchList extends RelativeLayout implements ToolbarMenuInterface {
    private static final String TAG = "SavedSearchList";

    // Ui
    private LinearLayout _paramList;

    // Data
    private OnHideListener _onHideListener;
    private OnShowListener _onShowListener;
    private OnSavedSearchParamsChangeListener _onSavedSearchParamsChangeListener;
    private List<SavedSearchParams> _list;

    private SavedSearchClient _savedSearchClient;

    public SavedSearchList(Context context) {
        super(context);
        init();
    }

    public SavedSearchList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SavedSearchList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_saved_search_list, this);

        if (isInEditMode())
            return;

        _paramList = (LinearLayout) findViewById(R.id.param_list);

        _savedSearchClient = new SavedSearchClient(_savedSearchClient_listener);
        _savedSearchClient.connect(App.get());

        populateUi();
    }

    private void populateUi() {
        if (_paramList == null)
            return;

        if (_list == null)
            return;

        _paramList.removeAllViews();
        for (int i = 0; i < _list.size(); i++) {
            SavedSearchRow row = new SavedSearchRow(getContext());
            _paramList.addView(row);
            row.setSearchParams(_list.get(i));
            row.setOnSearchSelectedListener(_savedSearchRow_onChange);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (getBehavior() != null)
            getBehavior().setListener(_toolbarMenuBehavior_listener);
    }

    private final ToolbarMenuBehavior.Listener _toolbarMenuBehavior_listener = new ToolbarMenuBehavior.Listener() {
        @Override
        public void onHide() {
            _onHideListener.onHide();
        }

        @Override
        public void onShow() {
            _onShowListener.onShow();
        }
    };

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("SHOWING", isShowing());
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            if (((Bundle) state).containsKey("SHOWING")) {
                if (((Bundle) state).getBoolean("SHOWING"))
                    show();
            }
            if (((Bundle) state).containsKey("SUPER"))
                super.onRestoreInstanceState(((Bundle) state).getParcelable("SUPER"));
        }
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        _onShowListener = onShowListener;
    }

    public void setOnHideListener(OnHideListener onHideListener) {
        _onHideListener = onHideListener;
    }

    public void setOnSavedSearchParamsChangeListener(OnSavedSearchParamsChangeListener listener) {
        _onSavedSearchParamsChangeListener = listener;
    }

    public void hide() {
        if (getBehavior() == null)
            return;

        getBehavior().startHidingAnimation(this);
    }

    public void show() {
        if (getBehavior() == null)
            return;

        getBehavior().startShowingAnimation(this);
    }

    public boolean isShowing() {
        if (getBehavior() == null)
            return false;

        return getBehavior()._mode == ToolbarMenuBehavior.MODE_SHOWN;
    }

    private ToolbarMenuBehavior getBehavior() {
        if (getLayoutParams() == null || !(getLayoutParams() instanceof CoordinatorLayout.LayoutParams))
            return null;

        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) getLayoutParams()).getBehavior();
        if (behavior == null || !(behavior instanceof ToolbarMenuBehavior))
            return null;

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
        return (ToolbarMenuBehavior) params.getBehavior();
    }

    private final SavedSearchRow.OnSearchSelectedListener _savedSearchRow_onChange = new SavedSearchRow.OnSearchSelectedListener() {
        @Override
        public void onSearch(SavedSearchParams savedSearchParams) {
            hide();
            if (_onSavedSearchParamsChangeListener != null)
                _onSavedSearchParamsChangeListener.onChange(savedSearchParams);
        }
    };

    private final SavedSearchClient.Listener _savedSearchClient_listener = new SavedSearchClient.Listener() {
        @Override
        public void onConnected() {
            _savedSearchClient.subList();
            SavedSearchClient.list();
        }

        @Override
        public void list(List<SavedSearchParams> savedSearchParams) {
            _list = savedSearchParams;
            populateUi();
        }
    };

    public interface OnHideListener {
        void onHide();
    }

    public interface OnShowListener {
        void onShow();
    }

    public interface OnSavedSearchParamsChangeListener {
        void onChange(SavedSearchParams params);
    }
}