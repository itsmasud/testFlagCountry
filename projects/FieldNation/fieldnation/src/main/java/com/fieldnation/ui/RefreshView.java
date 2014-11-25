package com.fieldnation.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by michael.carver on 11/25/2014.
 */
public class RefreshView extends RelativeLayout implements OnOverScrollListener {
//    private static final String TAG = "ui.RefreshView";

    private static final int STATE_IDLE = 0;
    private static final int STATE_PULLING = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_MOVE_TO_REFRESH = 3;
    private static final int STATE_REFRESHING = 4;
    private static final int STATE_HIDING = 5;

    private View _contents;
    private Animation _rotateAnim;
    private int _state;
    private int _maxTravelDistance = -1;
    private Listener _listener;

    public RefreshView(Context context) {
        super(context);
        init();
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_refresh, this);

        if (isInEditMode())
            return;

        _contents = findViewById(R.id.loading_imageview);

        _rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_spingear_cw);

        _state = STATE_IDLE;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void startSpinning() {
        _contents.startAnimation(_rotateAnim);
    }

    private void stopSpinning() {
        _contents.clearAnimation();
    }

    private void setLoadingOffset(int px) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) _contents.getLayoutParams();
        lp.topMargin = px;
        _contents.setLayoutParams(lp);
    }

    private int getLoadingOffset() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) _contents.getLayoutParams();
        return lp.topMargin;
    }

    private int getMaxTravelDistance() {
        if (_maxTravelDistance == -1)
            _maxTravelDistance = _contents.getHeight() * 4;
        return _maxTravelDistance;
    }

    private void startAnimation(int start, int end, Animator.AnimatorListener listener) {
        ValueAnimator a = ValueAnimator.ofInt(start, end);
        if (listener != null)
            a.addListener(listener);

        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setLoadingOffset((Integer) animation.getAnimatedValue());
            }
        });

        if (start == end) {
            a.setDuration(0);
        } else {
            // 1 px/ms
            a.setDuration(Math.abs(start - end));
        }
        a.start();
    }


    private Animator.AnimatorListener _moveToRefresh_listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            _state = STATE_MOVE_TO_REFRESH;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            _state = STATE_REFRESHING;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private Animator.AnimatorListener _hiding_listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            _state = STATE_HIDING;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            _state = STATE_IDLE;
            stopSpinning();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private int cleanYPixels(int y) {
        // negative values is overscroll pull down
        y = -y;
        // clamp
        if (y > getMaxTravelDistance()) {
            y = getMaxTravelDistance();
        }
        if (y < 0) {
            y = 0;
        }
        return y;
    }

    @Override
    public void onOverScrolled(View view, int pixelsX, int pixelsY) {
        if (_state == STATE_IDLE) {
            _state = STATE_PULLING;
        }
        pixelsY = cleanYPixels(pixelsY);

        if (_state != STATE_HIDING
                && _state != STATE_MOVE_TO_REFRESH
                && _state != STATE_REFRESHING) {
            setLoadingOffset(pixelsY);

            if (_state != STATE_RELEASE_TO_REFRESH && pixelsY > getMaxTravelDistance() - _contents.getHeight()) {
                _state = STATE_RELEASE_TO_REFRESH;
                startSpinning();
            } else if (_state == STATE_RELEASE_TO_REFRESH && pixelsY < getMaxTravelDistance() - _contents.getHeight()) {
                _state = STATE_PULLING;
                stopSpinning();
            }
        }
    }

    // this is a mouse up
    @Override
    public void onOverScrollComplete(View view, int pixelsX, int pixelsY) {
        if (_state == STATE_RELEASE_TO_REFRESH) {
            _state = STATE_MOVE_TO_REFRESH;

            pixelsY = cleanYPixels(pixelsY);

            setLoadingOffset(0);

            startAnimation(
                    pixelsY,
                    (_contents.getHeight() * 3) / 2,
                    _moveToRefresh_listener);

            if (_listener != null) {
                _listener.onStartRefresh();
            }

        } else if (_state == STATE_PULLING) {
            _state = STATE_HIDING;

            pixelsY = cleanYPixels(pixelsY);

            setLoadingOffset(0);

            startAnimation(pixelsY, 0, _hiding_listener);
        }
    }

    public void startRefreshing() {
        if (_state == STATE_IDLE) {
            _state = STATE_MOVE_TO_REFRESH;
            setLoadingOffset(0);

            startAnimation(
                    0,
                    (_contents.getHeight() * 3) / 2,
                    _moveToRefresh_listener);
            startSpinning();
        }

    }

    public void refreshComplete() {
        if (_state == STATE_REFRESHING || _state == STATE_MOVE_TO_REFRESH) {
            _state = STATE_HIDING;

            int off = getLoadingOffset();

            setLoadingOffset(0);

            startAnimation(off, 0, _hiding_listener);
        }
    }

    public void refreshFailed() {
        // TODO maybe display a special state
        refreshComplete();
    }

    public interface Listener {
        public void onStartRefresh();
    }

}
