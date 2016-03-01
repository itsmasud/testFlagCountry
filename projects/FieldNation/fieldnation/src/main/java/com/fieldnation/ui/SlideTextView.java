package com.fieldnation.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Michael Carver on 1/29/2015.
 */
public class SlideTextView extends TextView {

    private static int ANIMATION_DELAY = 175;

    private float _left = 0f;
    private float _right = 0f;

    public SlideTextView(Context context) {
        super(context);
    }

    public SlideTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        canvas.clipRect(getWidth() * _left, 0, getWidth() * _right, getHeight());

        if (getLayout() != null)
            getLayout().draw(canvas);
        canvas.restore();
    }

    public void setHighlight() {
        _left = 0f;
        _right = 1f;
        invalidate();
    }

    public void animateHighlightRight() {
        _left = 0.0f;
        _right = 0.0f;
        invalidate();
        ValueAnimator a = ValueAnimator.ofFloat(0f, 1.0f);
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _right = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
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

    public void animateHighlightLeft() {
        ValueAnimator a = ValueAnimator.ofFloat(1f, 0f);
        _left = 1.0f;
        _right = 1.0f;
        invalidate();
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _left = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
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
        ValueAnimator a = ValueAnimator.ofFloat(1f, 0f);
        _left = 0f;
        _right = 1f;
        invalidate();
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _right = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                _left = 0f;
                _right = 0f;
                invalidate();
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


    public void animateUnhighlightRight() {
        ValueAnimator a = ValueAnimator.ofFloat(0f, 1f);
        _left = 0f;
        _right = 1f;
        invalidate();
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _left = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                _left = 1f;
                _right = 1f;
                invalidate();
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

}
