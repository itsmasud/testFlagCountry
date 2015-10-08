package com.fieldnation.ui;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.fieldnation.Log;
import com.fieldnation.R;

/**
 * Created by Michael Carver on 5/20/2015.
 */
public class RightDrawerView extends FrameLayout {
    private static final String TAG = "RightDrawerView";

    // Ui
    private FrameLayout _bodyContainer;
    private FrameLayout _toolbarContainer;
    private View _fader;

    private View _topSheetView;

    private GestureDetectorCompat _gestureDetector;

    // Animations
    private Animation _slideOutAnimation;
    private Animation _slideInAnimation;
    private Animation _fadeInAnimation;
    private Animation _fadeOutAnimation;

    public RightDrawerView(Context context) {
        super(context);
        init();
    }

    public RightDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RightDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_right_drawer, this);

        if (isInEditMode())
            return;

        _topSheetView = findViewById(R.id.topSheet_view);
        _fader = findViewById(R.id.backgroundFader);
        _fader.setOnClickListener(_fader_onClick);

        _gestureDetector = new GestureDetectorCompat(getContext(), _gesture_listener);

        _slideOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        _slideOutAnimation.setAnimationListener(_slideOut_listener);
        _slideInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        _slideInAnimation.setAnimationListener(_slideIn_listener);
        _fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        _fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
    }

    private FrameLayout getBodyContainer() {
        if (_bodyContainer == null)
            _bodyContainer = (FrameLayout) findViewById(R.id.bodyContainer_frameLayout);

        return _bodyContainer;
    }

    private FrameLayout getToolbarContainer() {
        if (_toolbarContainer == null)
            _toolbarContainer = (FrameLayout) findViewById(R.id.toolbarContainer_framelayout);

        return _toolbarContainer;
    }

    public void animateShow() {
        setVisibility(VISIBLE);
        _topSheetView.startAnimation(_slideInAnimation);
        _fader.startAnimation(_fadeInAnimation);
    }

    public void animateHide() {
        _topSheetView.startAnimation(_slideOutAnimation);
        _fader.startAnimation(_fadeOutAnimation);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return _gestureDetector.onTouchEvent(event);
    }

    /*-*********************************-*/
    /*-         Animation Crap          -*/
    /*-*********************************-*/
    private final View.OnClickListener _fader_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            animateHide();
        }
    };

    private final Animation.AnimationListener _slideOut_listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            setVisibility(GONE);
            _topSheetView.clearAnimation();
            _fader.clearAnimation();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private final Animation.AnimationListener _slideIn_listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            setVisibility(VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            _topSheetView.clearAnimation();
            _fader.clearAnimation();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    private final GestureDetector.OnGestureListener _gesture_listener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.v(TAG, "onFling velocityX: " + velocityX + ", velocityY: " + velocityY);
            return false;
        }
    };

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.content) {
            super.addView(child, index, params);
        } else if (getBodyContainer().getChildCount() == 0) {
            getBodyContainer().addView(child, index, params);
        } else {
            getToolbarContainer().addView(child, index, params);
            getToolbarContainer().setVisibility(VISIBLE);
        }
    }

}
