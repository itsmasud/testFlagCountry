package com.fieldnation.v2.ui.nav;

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
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.nav.ToolbarMenuBehavior;
import com.fieldnation.ui.nav.ToolbarMenuInterface;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.SavedList;

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
    private OnSavedListChangeListener _onSavedListChangeListener;
    private SavedList[] _list;

    private WorkordersWebApi _workOrderClient;

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

        _workOrderClient = new WorkordersWebApi(_workOrderClient_listener);
        _workOrderClient.connect(App.get());

        populateUi();
    }

    private void populateUi() {
        if (_paramList == null)
            return;

        if (_list == null)
            return;

        _paramList.removeAllViews();
        for (int i = 0; i < _list.length; i++) {
            SavedSearchRow row = new SavedSearchRow(getContext());
            _paramList.addView(row);
            row.setSearchParams(_list[i]);
            row.setOnSearchSelectedListener(_savedSearchRow_onChange);
        }
        misc.hideKeyboard(_paramList);
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

    public void setOnSavedSearchParamsChangeListener(OnSavedListChangeListener listener) {
        _onSavedListChangeListener = listener;
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
        public void onSearch(SavedList savedList) {
            hide();
            if (_onSavedListChangeListener != null)
                _onSavedListChangeListener.onChange(savedList);
        }
    };

    private WorkordersWebApi.Listener _workOrderClient_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderClient.subGetWorkOrderLists();
            WorkordersWebApi.getWorkOrderLists(App.get(), false);
        }

        @Override
        public void onGetWorkOrderLists(SavedList[] savedList, boolean success, Error error) {
            _list = savedList;
            populateUi();
        }
    };

    public interface OnHideListener {
        void onHide();
    }

    public interface OnShowListener {
        void onShow();
    }

    public interface OnSavedListChangeListener {
        void onChange(SavedList savedList);
    }
}