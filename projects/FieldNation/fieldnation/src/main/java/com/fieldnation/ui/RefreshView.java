package com.fieldnation.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by michael.carver on 11/25/2014.
 */
public class RefreshView extends RelativeLayout implements OnOverScrollListener {
    private final String TAG = UniqueTag.makeTag("RefreshView");

    private static final int STATE_IDLE = 0;
    private static final int STATE_PULLING = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_MOVE_TO_REFRESH = 3;
    private static final int STATE_REFRESHING = 4;
    private static final int STATE_HIDING = 5;

    private ImageView _spinnerImageView;
    private View _contentLayout;
    private ImageView _gradientImageView;
    private Animation _rotateAnim;
    private Animation _rotateRevAnim;
    private int _state;
    private Listener _listener;

    // Data
    private GlobalTopicClient _globalClient;

    private boolean _completeWhenAble = false;

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

        _contentLayout = findViewById(R.id.content_layout);
        _spinnerImageView = (ImageView) findViewById(R.id.spinner_imageview);
        _gradientImageView = (ImageView) findViewById(R.id.gradient_imageview);

        _rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_spingear_cw);
        _rotateRevAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_spingear_ccw);

        _globalClient = new GlobalTopicClient(_globalTopic_listener);
        _globalClient.connect(App.get());

        _state = STATE_IDLE;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_globalClient != null && _globalClient.isConnected())
            _globalClient.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void startSpinning() {
        _spinnerImageView.clearAnimation();
        _rotateAnim.reset();
        _spinnerImageView.startAnimation(_rotateAnim);
        _gradientImageView.clearAnimation();
        _rotateRevAnim.reset();
        _gradientImageView.startAnimation(_rotateRevAnim);
    }

    private void stopSpinning() {
        _spinnerImageView.clearAnimation();
        _gradientImageView.clearAnimation();
    }

    private void setLoadingOffset(int px) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) _contentLayout.getLayoutParams();
        lp.topMargin = px;
        _contentLayout.setLayoutParams(lp);
        requestLayout();
    }

    private int getLoadingOffset() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) _contentLayout.getLayoutParams();
        return lp.topMargin;
    }

    private int getMaxTravelDistance() {
//        Log.v(TAG, "getMaxTravelDistance() = " + _contents.getHeight() * 4);
        return _contentLayout.getHeight() * 4;
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
            a.setDuration(100);
        } else {
            // 1 px/ms
            a.setDuration(Math.abs(start - end));
        }
        a.start();
    }


    private final Animator.AnimatorListener _moveToRefresh_listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            _state = STATE_MOVE_TO_REFRESH;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            _state = STATE_REFRESHING;
            if (_completeWhenAble) {
                refreshComplete();
                _completeWhenAble = false;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private final Animator.AnimatorListener _hiding_listener = new Animator.AnimatorListener() {
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

            if (_state != STATE_RELEASE_TO_REFRESH && pixelsY > getMaxTravelDistance() - _contentLayout.getHeight()) {
                _state = STATE_RELEASE_TO_REFRESH;
                startSpinning();
            } else if (_state == STATE_RELEASE_TO_REFRESH && pixelsY < getMaxTravelDistance() - _contentLayout.getHeight()) {
                _state = STATE_PULLING;
                stopSpinning();
            }
        }
    }

    // this is a mouse up
    @Override
    public void onOverScrollComplete(View view, int pixelsX, int pixelsY) {
        if (_state == STATE_RELEASE_TO_REFRESH) {
            Log.v(TAG, "onOverScrollComplete, STATE_RELEASE_TO_REFRESH");
            _state = STATE_MOVE_TO_REFRESH;

            pixelsY = cleanYPixels(pixelsY);

            setLoadingOffset(0);

            startAnimation(
                    pixelsY,
                    (_contentLayout.getHeight() * 3) / 2,
                    _moveToRefresh_listener);

            if (_listener != null) {
                _listener.onStartRefresh();
            }

        } else if (_state == STATE_PULLING) {
            Log.v(TAG, "onOverScrollComplete, STATE_PULLING");
            _state = STATE_HIDING;

            pixelsY = cleanYPixels(pixelsY);

            setLoadingOffset(0);

            startAnimation(pixelsY, 0, _hiding_listener);
        }
    }

    public void startRefreshing() {
        Log.v(TAG, "startRefreshing " + _state + ":" + _contentLayout.getHeight());

        if (_contentLayout.getHeight() == 0)
            return;

        if (_state == STATE_IDLE) {
            _state = STATE_MOVE_TO_REFRESH;

            setLoadingOffset(0);
            startAnimation(
                    0,
                    (_contentLayout.getHeight() * 3) / 2,
                    _moveToRefresh_listener);
            startSpinning();
        }
    }

    public void refreshComplete() {
        Log.v(TAG, "refreshComplete " + _state);
        if (_state == STATE_REFRESHING) {
            _state = STATE_HIDING;
            _completeWhenAble = false;

            int off = getLoadingOffset();

            setLoadingOffset(0);

            startAnimation(off, 0, _hiding_listener);
        } else if (_state == STATE_MOVE_TO_REFRESH || _state == STATE_PULLING || _state == STATE_RELEASE_TO_REFRESH) {
            _completeWhenAble = true;
        }
    }

    public void refreshFailed() {
        // TODO maybe display a special state
        refreshComplete();
    }

    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {

        @Override
        public void onConnected() {
            _globalClient.subLoading();
        }

        @Override
        public void setLoading(boolean isLoading) {
            Log.v(TAG, "setLoading()");
            if (isLoading) {
                startRefreshing();
            } else {
                refreshComplete();
            }
        }
    };

    public interface Listener {
        void onStartRefresh();
    }

}
