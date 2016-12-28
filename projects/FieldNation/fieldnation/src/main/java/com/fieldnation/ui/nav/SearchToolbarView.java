package com.fieldnation.ui.nav;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by mc on 12/27/16.
 */

@CoordinatorLayout.DefaultBehavior(ToolbarMenuBehavior.class)
public class SearchToolbarView extends RelativeLayout implements ToolbarMenuInterface {
    private static final String TAG = "SearchToolbarView";

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
}
