package com.fieldnation.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 1/28/2015.
 */
public class TabView extends RelativeLayout {

    private static int ANIMATION_DELAY = 1000;

    private TextView _back;
    private TextView _front;

    private Animation _expandRight;

    public TabView(Context context) {
        super(context);
        init();
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tab, this, true);

        _back = (TextView) findViewById(R.id.back);
        _front = (TextView) findViewById(R.id.front);
        _front.setVisibility(View.GONE);
    }

    public void aniamteHighlightRight() {
        ValueAnimator a = ValueAnimator.ofInt(0, getWidth());
        _front.setLeft(0);
        _front.setRight(getWidth());
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _front.setRight((int) animation.getAnimatedValue());
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                _front.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        a.setDuration(ANIMATION_DELAY);
        a.start();
    }

    public void aniamteHighlightLeft() {
        ValueAnimator a = ValueAnimator.ofInt(getWidth(), 0);
        _front.setLeft(0);
        _front.setRight(getWidth());
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _front.setLeft((int) animation.getAnimatedValue());
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                _front.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        a.setDuration(ANIMATION_DELAY);
        a.start();
    }

    public void animateUnhighlightLeft() {
        ValueAnimator a = ValueAnimator.ofInt(getWidth(), 0);
        _front.setLeft(0);
        _front.setRight(getWidth());
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _front.setRight((int) animation.getAnimatedValue());
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                _front.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        a.setDuration(ANIMATION_DELAY);

        a.start();
    }


    public void aniamteUnhighlightRight() {
        ValueAnimator a = ValueAnimator.ofInt(0, getWidth());
        _front.setLeft(0);
        _front.setRight(getWidth());
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _front.setLeft((int) animation.getAnimatedValue());
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                _front.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        a.setDuration(ANIMATION_DELAY);

        a.start();
    }

    public void setText(String text) {
        _front.setText(text);
        _back.setText(text);
    }

    public void setText(int resid) {
        _front.setText(resid);
        _back.setText(resid);
    }

}
