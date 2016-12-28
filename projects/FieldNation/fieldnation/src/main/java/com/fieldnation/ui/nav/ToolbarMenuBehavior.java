package com.fieldnation.ui.nav;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by mc on 12/28/16.
 */

public class ToolbarMenuBehavior extends CoordinatorLayout.Behavior {
    private static final String TAG = "ToolbarMenuBehavior";
    private static final int ANIMATION_DURATION = 250;

    public static final int MODE_ANIMATING = 0;
    public static final int MODE_SHOWN = 1;
    public static final int MODE_HIDDEN = 2;

    public int _mode = MODE_HIDDEN;

    private int _appBarBottom = 0;

    private ValueAnimator _showingAnimation;
    private ValueAnimator _hidingAnimation;

    public ToolbarMenuBehavior() {
        super();
    }

    public ToolbarMenuBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setYPos(View child, int y) {
        int h = child.getHeight();
        child.setTop(y);
        child.setBottom(y + h);
    }

    void startShowingAnimation(final View child) {
        if (_mode == MODE_SHOWN || _mode == MODE_ANIMATING)
            return;
        _mode = MODE_ANIMATING;

        _showingAnimation = ValueAnimator.ofInt(-child.getHeight(), _appBarBottom);
        _showingAnimation.setDuration(ANIMATION_DURATION);
        _showingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setYPos(child, (Integer) animation.getAnimatedValue());
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
                setYPos(child, _appBarBottom);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        setYPos(child, -child.getHeight());
        _showingAnimation.start();
    }

    void startHidingAnimation(final View child) {
        if (_mode == MODE_HIDDEN || _mode == MODE_ANIMATING)
            return;
        _mode = MODE_ANIMATING;
        _hidingAnimation = ValueAnimator.ofInt(child.getTop(), -child.getHeight());
        _hidingAnimation.setDuration(ANIMATION_DURATION);
        _hidingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setYPos(child, (Integer) animation.getAnimatedValue());
            }
        });
        _hidingAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                _mode = MODE_HIDDEN;
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
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        List<View> dependencies = parent.getDependencies(child);
        for (int i = 0; i < dependencies.size(); i++) {
            View dependency = dependencies.get(i);
            if (dependency instanceof AppBarLayout) {
                //Log.v(TAG, "onLayoutChild " + (dependency.getVisibility() == VISIBLE ? "VISIBLE" : "GONE") + " " + dependency.getBottom());
                _appBarBottom = dependency.getBottom();
            }
        }


        if (_mode == MODE_HIDDEN)
            setYPos(child, -child.getHeight());
        if (_mode == MODE_SHOWN)
            setYPos(child, _appBarBottom);

        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            _appBarBottom = dependency.getBottom();
        }
        startHidingAnimation(child);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
