package com.fieldnation.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fntools.DefaultAnimationListener;

/**
 * Created by Michael on 9/28/2015.
 */
public class SwitchUserOverlayView extends RelativeLayout {
    private static final String TAG = "SwitchUserOverlayView";

    // Ui
    private IconFontTextView _textView;

    // Data
    private final int[] _icons = new int[]{R.string.icon_my_wos, R.string.icon_hiring, R.string.icon_circle_dollar, R.string.icon_summary};
    private int _iconIndex = 0;

    // Serivces
    private GlobalTopicClient _globalTopicClient;

    // Animations
    private Animation _shrinkAnimation;
    private Animation _growAnimation;


    public SwitchUserOverlayView(Context context) {
        super(context);
        init();
    }

    public SwitchUserOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwitchUserOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_user_switching, this);

        if (isInEditMode())
            return;

        _textView = (IconFontTextView) findViewById(R.id.loading_textView);
        _textView.setText(_icons[_iconIndex]);

        _shrinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shrink_horrizontal);
        _shrinkAnimation.setAnimationListener(_shrinkListener);
        _growAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.grow_horrizontal);
        _growAnimation.setAnimationListener(_growListener);

        _globalTopicClient = new GlobalTopicClient(_globalTopicClient_listener);
        _globalTopicClient.connect(App.get());
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_globalTopicClient != null) _globalTopicClient.disconnect(App.get());
        super.onDetachedFromWindow();
    }

    public void startSwitch() {
        _textView.startAnimation(_shrinkAnimation);
        setVisibility(VISIBLE);
    }

    private final GlobalTopicClient.Listener _globalTopicClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.subUserSwitched();
        }

        @Override
        public void onUserSwitched(Profile profile) {
            // NavActivity.startNew(getContext());
            _textView.clearAnimation();
            setVisibility(GONE);
        }
    };

    private final Animation.AnimationListener _shrinkListener = new DefaultAnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator a = ValueAnimator.ofFloat(1.0F, 0.0F);

                    a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            _textView.setAlpha((Float) animation.getAnimatedValue());
                        }
                    });

                    a.setDuration(500);
                    a.start();
                }
            }, 8);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            _iconIndex = (_iconIndex + 1) % _icons.length;
            _textView.setText(_icons[_iconIndex]);

            _textView.startAnimation(_growAnimation);
        }
    };

    private final Animation.AnimationListener _growListener = new DefaultAnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator a = ValueAnimator.ofFloat(0.0F, 1.0F);

                    a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            _textView.setAlpha((Float) animation.getAnimatedValue());
                        }
                    });
                    a.setDuration(500);
                    a.start();

                }
            }, 8);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            _textView.startAnimation(_shrinkAnimation);
        }
    };
}
