package com.fieldnation.ui.nav;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.ui.search.SearchEditText;
import com.fieldnation.ui.workorder.WorkorderActivity;

/**
 * Created by mc on 12/27/16.
 */

@CoordinatorLayout.DefaultBehavior(ToolbarMenuBehavior.class)
public class SearchToolbarView extends RelativeLayout implements ToolbarMenuInterface {
    private static final String TAG = "SearchToolbarView";

    // Ui
    private SearchEditText _searchEditText;

    public SearchToolbarView(Context context) {
        super(context);
        init();
    }

    public SearchToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_toolbar_layout, this);

        if (isInEditMode())
            return;

        _searchEditText = (SearchEditText) findViewById(R.id.searchEditText);
        _searchEditText.setListener(_searchEditText_listener);
    }

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

    public void show() {
        if (getBehavior() == null)
            return;

        getBehavior().startShowingAnimation(this);
    }

    public void hide() {
        if (getBehavior() == null)
            return;

        getBehavior().startHidingAnimation(this);
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

    private final SearchEditText.Listener _searchEditText_listener = new SearchEditText.Listener() {
        @Override
        public void onLookupWorkOrder(final long workOrderId) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityResultClient.startActivity(
                            App.get(),
                            WorkorderActivity.makeIntentShow(App.get(), workOrderId),
                            R.anim.activity_slide_in_right,
                            R.anim.activity_slide_out_left);
                }
            }, 100);
            hide();
        }
    };
}
