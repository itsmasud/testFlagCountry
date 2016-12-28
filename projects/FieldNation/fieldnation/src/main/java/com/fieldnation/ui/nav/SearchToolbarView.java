package com.fieldnation.ui.nav;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;

import java.util.List;

/**
 * Created by mc on 12/27/16.
 */

@CoordinatorLayout.DefaultBehavior(SearchToolbarView.Behavior.class)
public class SearchToolbarView extends RelativeLayout {
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

        setVisibility(GONE);
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

    private void setYPos(int y) {
        int h = getHeight();
        setTop(y);
        setBottom(y + h);
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

    public static class Behavior extends CoordinatorLayout.Behavior<SearchToolbarView> {
        private static final String TAG = "SearchToolbarView.Behavior";
        private static final int ANIMATION_DURATION = 250;

        private static final int MODE_ANIMATING = 0;
        private static final int MODE_SHOWN = 1;
        private static final int MODE_HIDDEN = 2;

        private int _mode = MODE_HIDDEN;

        private int _appBarBottom = 0;

        private ValueAnimator _showingAnimation;
        private ValueAnimator _hidingAnimation;

        public Behavior() {
            super();
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        void startShowingAnimation(final SearchToolbarView child) {
            if (_mode == MODE_SHOWN || _mode == MODE_ANIMATING)
                return;
            _mode = MODE_ANIMATING;

            _showingAnimation = ValueAnimator.ofInt(_appBarBottom - child.getHeight(), _appBarBottom);
            _showingAnimation.setDuration(ANIMATION_DURATION);
            _showingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    child.setYPos((Integer) animation.getAnimatedValue());
                }
            });
            _showingAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
//                    setMode(MODE_ATTACHED_TO_APPBAR);
                    _mode = MODE_SHOWN;
                    child.setYPos(_appBarBottom);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            child.setYPos(_appBarBottom - child.getHeight());
            child.setVisibility(VISIBLE);
            _showingAnimation.start();
        }

        void startHidingAnimation(final SearchToolbarView child) {
            if (_mode == MODE_HIDDEN || _mode == MODE_ANIMATING)
                return;
            _mode = MODE_ANIMATING;
            _hidingAnimation = ValueAnimator.ofInt(child.getTop(), -child.getHeight());
            _hidingAnimation.setDuration(ANIMATION_DURATION);
            _hidingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    child.setYPos((Integer) animation.getAnimatedValue());
                }
            });
            _hidingAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //setMode(MODE_ATTACHED_TO_APPBAR);
                    _mode = MODE_HIDDEN;
                    child.setVisibility(GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            _hidingAnimation.start();
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, SearchToolbarView child, int layoutDirection) {
            parent.onLayoutChild(child, layoutDirection);
            List<View> dependencies = parent.getDependencies(child);
            for (int i = 0; i < dependencies.size(); i++) {
                View dependency = dependencies.get(i);
                if (dependency instanceof AppBarLayout) {
                    //Log.v(TAG, "onLayoutChild " + (dependency.getVisibility() == VISIBLE ? "VISIBLE" : "GONE") + " " + dependency.getBottom());
                    _appBarBottom = dependency.getBottom();
                }
            }
            return true;
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, SearchToolbarView child, View dependency) {
            return dependency instanceof AppBarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, SearchToolbarView child, View dependency) {
            child.hide();
            return super.onDependentViewChanged(parent, child, dependency);
        }
    }

}
