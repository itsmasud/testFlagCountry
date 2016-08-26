package com.fieldnation.ui.nav;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;

import java.util.List;

/**
 * Created by Michael on 8/25/2016.
 */
@DefaultBehavior(SavedSearchList.Behavior.class)
public class SavedSearchList extends LinearLayout {
    private static final String TAG = "SavedSearchList";

    private static final SavedSearchParams[] defaults = new SavedSearchParams[]{
            new SavedSearchParams()
                    .type(WorkOrderListType.ASSIGNED.getType())
                    .status(WorkOrderListType.ASSIGNED.getStatuses())
                    .title("Assigned"),
            new SavedSearchParams()
                    .type(WorkOrderListType.AVAILABLE.getType())
                    .status(WorkOrderListType.AVAILABLE.getStatuses())
                    .title("Available"),
            new SavedSearchParams()
                    .type(WorkOrderListType.CANCELED.getType())
                    .status(WorkOrderListType.CANCELED.getStatuses())
                    .title("Canceled"),
            new SavedSearchParams()
                    .type(WorkOrderListType.COMPLETED.getType())
                    .status(WorkOrderListType.COMPLETED.getStatuses())
                    .title("Completed"),
            new SavedSearchParams()
                    .type(WorkOrderListType.REQUESTED.getType())
                    .status(WorkOrderListType.REQUESTED.getStatuses())
                    .title("Requested"),
            new SavedSearchParams()
                    .type(WorkOrderListType.ROUTED.getType())
                    .status(WorkOrderListType.ROUTED.getStatuses())
                    .title("Routed")
    };

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

        if (isInEditMode())
            return;

        setOrientation(VERTICAL);

        for (int i = 0; i < defaults.length; i++) {
            TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_saved_search_row, null);
            tv.setText(defaults[i].title);
            tv.setTag(defaults[i]);
            tv.setOnClickListener(_textView_onClick);
            addView(tv);
        }
    }

    private final View.OnClickListener _textView_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Todo send some sort of signal here
            Log.v(TAG, ((SavedSearchParams) v.getTag()).title);
        }
    };

    public static class Behavior extends CoordinatorLayout.Behavior<SavedSearchList> {
        private static final String TAG = "SavedSearchList.Behavior";

        private static final int MODE_ATTACHED_TO_APPBAR = 1;
        private static final int MODE_SCROLLING = 2;
        private static final int MODE_HIDDEN = 3;
        private int _mode = MODE_ATTACHED_TO_APPBAR;

        public Behavior() {
            super();
            Log.v(TAG, "Behavior");
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            Log.v(TAG, "Behavior");
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, SavedSearchList child, int layoutDirection) {
            // Log.v(TAG, "onLayoutChild");

            parent.onLayoutChild(child, layoutDirection);

            if (_mode == MODE_ATTACHED_TO_APPBAR) {

                List<View> dependencies = parent.getDependencies(child);
                for (int i = 0; i < dependencies.size(); i++) {
                    View dependency = dependencies.get(i);
                    if (dependency instanceof AppBarLayout) {
                        //Log.v(TAG, "onLayoutChild " + (dependency.getVisibility() == VISIBLE ? "VISIBLE" : "GONE") + " " + dependency.getBottom());
                        int h = child.getHeight();
                        child.setTop(dependency.getBottom());
                        child.setBottom(dependency.getBottom() + h);
                    }
                }

                return true;
            }
            return false;
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, SavedSearchList child, View dependency) {
            return dependency instanceof AppBarLayout;
        }

        @Override
        public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, SavedSearchList child, View directTargetChild, View target, int nestedScrollAxes) {
            super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, SavedSearchList child, View dependency) {
            //Log.v(TAG, "onDependentViewChanged " + dependency.toString());
            if (dependency instanceof AppBarLayout) {
                if (dependency.getBottom() == 0) {
                    _mode = MODE_SCROLLING;
                } else {
                    _mode = MODE_ATTACHED_TO_APPBAR;

                    int h = child.getHeight();
                    child.setTop(dependency.getBottom());
                    child.setBottom(dependency.getBottom() + h);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, SavedSearchList child, View directTargetChild, View target, int nestedScrollAxes) {
            Log.v(TAG, "onStartNestedScroll " + nestedScrollAxes);
            if (_mode == MODE_SCROLLING) {
                child.setVisibility(GONE);
                _mode = MODE_HIDDEN;
            }
            return false;
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, SavedSearchList child, View target, int dx, int dy, int[] consumed) {
            Log.v(TAG, "onNestedPreScroll " + dx + " " + dy);
        }

        @Override
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, SavedSearchList child, View target) {
            Log.v(TAG, "onStopNestedScroll");
        }

        @Override
        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, SavedSearchList child, View target, float velocityX, float velocityY, boolean consumed) {
            Log.v(TAG, "onNestedFling " + velocityX + " " + velocityY + " " + consumed);
            return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        }

        @Override
        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, SavedSearchList child, View target, float velocityX, float velocityY) {
            Log.v(TAG, "onNestedPreFling " + velocityX + " " + velocityY);
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        }

        @Override
        public void onDependentViewRemoved(CoordinatorLayout parent, SavedSearchList child, View dependency) {
            Log.v(TAG, "onDependentViewRemoved " + dependency.toString());
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, SavedSearchList child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            Log.v(TAG, "onNestedScroll " + dxConsumed + " " + dyConsumed + " " + dxUnconsumed + " " + dyUnconsumed);

//            if (_mode == MODE_SCROLLING) {
//                Log.v(TAG, "onNestedScroll " + child.getTop() + " " + child.getBottom());
//                int h = child.getHeight();
//                int b = child.getBottom() - (dyUnconsumed + dyConsumed);
//                if (b < 0) b = 0;
//
//                child.setBottom(b);
//                child.setTop(b - h);
//            }
        }
    }

}
