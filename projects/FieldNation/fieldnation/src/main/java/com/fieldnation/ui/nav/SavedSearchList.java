package com.fieldnation.ui.nav;

import android.animation.Animator;
import android.animation.ValueAnimator;
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

    public void hide() {
        if (getBehavior() == null)
            return;

        getBehavior().startHidingAnimation(this);
    }

    public void show() {
        if (getBehavior() == null)
            return;

        getBehavior().startShowingAnimation((CoordinatorLayout) getParent(), this);
    }

    private Behavior getBehavior() {
        if (getLayoutParams() == null || !(getLayoutParams() instanceof CoordinatorLayout.LayoutParams))
            return null;

        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) getLayoutParams()).getBehavior();
        if (behavior == null || !(behavior instanceof Behavior))
            return null;

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
        return (Behavior) params.getBehavior();
    }

    private void setYPos(int y) {
        int h = getHeight();
        setTop(y);
        setBottom(y + h);
    }

    private int getYPos() {
        return getTop();
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
        private static final int MODE_HIDING = 3;
        private static final int MODE_HIDDEN = 4;
        private static final int MODE_SHOWING = 5;

        private int _mode = MODE_ATTACHED_TO_APPBAR;

        public Behavior() {
            super();
            Log.v(TAG, "Behavior");
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            Log.v(TAG, "Behavior");
        }

        private void setMode(int mode) {
            if (mode != _mode) {
                switch (mode) {
                    case MODE_ATTACHED_TO_APPBAR:
                        Log.v(TAG, "Behavior Mode: MODE_ATTACHED_TO_APPBAR");
                        break;
                    case MODE_SCROLLING:
                        Log.v(TAG, "Behavior Mode: MODE_SCROLLING");
                        break;
                    case MODE_HIDDEN:
                        Log.v(TAG, "Behavior Mode: MODE_HIDDEN");
                        break;
                    case MODE_HIDING:
                        Log.v(TAG, "Behavior Mode: MODE_HIDING");
                        break;
                    case MODE_SHOWING:
                        Log.v(TAG, "Behavior Mode: MODE_SHOWING");
                        break;
                }
            }
            _mode = mode;
        }

        void startShowingAnimation(CoordinatorLayout parent, final SavedSearchList child) {
            if (_mode != MODE_HIDDEN)
                return;
            setMode(MODE_SHOWING);
            child.setVisibility(VISIBLE);

            int fin = 0;
            List<View> dependencies = parent.getDependencies(child);
            for (int i = 0; i < dependencies.size(); i++) {
                View dependency = dependencies.get(i);
                if (dependency instanceof AppBarLayout) {
                    fin = dependency.getBottom();
                    break;
                }
            }

            ValueAnimator a = ValueAnimator.ofInt(child.getTop(), fin);
            a.setDuration(100);
            a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    child.setYPos((Integer) animation.getAnimatedValue());
                }
            });
            a.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setMode(MODE_ATTACHED_TO_APPBAR);
                    child.requestLayout();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            a.start();
        }

        void startHidingAnimation(final SavedSearchList child) {
            setMode(MODE_HIDING);

            ValueAnimator a = ValueAnimator.ofInt(child.getTop(), -child.getHeight());
            a.setDuration(100);
            a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    child.setYPos((Integer) animation.getAnimatedValue());
                }
            });
            a.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setMode(MODE_HIDDEN);
                    child.setVisibility(GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            a.start();
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
                        child.setYPos(dependency.getBottom());
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
        public boolean onDependentViewChanged(CoordinatorLayout parent, SavedSearchList child, View dependency) {
            //Log.v(TAG, "onDependentViewChanged " + dependency.toString());
            if (dependency instanceof AppBarLayout) {
                if (dependency.getBottom() == 0) {
                    setMode(MODE_SCROLLING);
                } else {
                    setMode(MODE_ATTACHED_TO_APPBAR);
                    child.setYPos(dependency.getBottom());
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, SavedSearchList child, View directTargetChild, View target, int nestedScrollAxes) {
            Log.v(TAG, "onStartNestedScroll " + nestedScrollAxes);
            if (_mode == MODE_SCROLLING && child.getBottom() <= 0) {
                setMode(MODE_HIDDEN);
                child.setVisibility(GONE);
                return false;
            } else if (_mode == MODE_HIDDEN) {
                return false;
            }
            return true;
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, SavedSearchList child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            Log.v(TAG, "onNestedScroll " + dxConsumed + " " + dyConsumed + " " + dxUnconsumed + " " + dyUnconsumed);
            if (_mode == MODE_SCROLLING) {
                int h = child.getHeight();
                int b = child.getBottom() - (dyUnconsumed + dyConsumed);
                Log.v(TAG, "onNestedScroll " + child.getTop() + " " + child.getBottom() + " " + (b - h) + " " + b);
                if (b < 0) {
                    setMode(MODE_HIDDEN);
                    child.setVisibility(GONE);
                } else {
                    child.setYPos(b - h);
                }
            }
        }

        @Override
        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, final SavedSearchList child, View target, float velocityX, float velocityY) {
            Log.v(TAG, "onNestedPreFling " + velocityX + " " + velocityY);
            if (_mode == MODE_SCROLLING && velocityY > 0) {
                startHidingAnimation(child);
            }
            return false;
        }
    }
}