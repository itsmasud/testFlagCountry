package com.fieldnation.ui;

import com.fieldnation.R;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingView extends RelativeLayout {

	// Ui
	private ImageView _gear1;
	private ImageView _gear2;
	private ImageView _gear3;
	private ImageView _center;
	private TextView _messageTextview;
	private TextView _obstructTextView;

	// animations
	RotateAnimation _rotateAnim1;
	RotateAnimation _rotateAnim2;
	RotateAnimation _rotateAnim3;

	// data
	private Vibrator _vib;
	private boolean _done;
	private boolean _mouseDown;

	private static final long[] VIB_PATTERN = new long[] { 0, 100, 10, 50, 10, 25, 10, 12 };

	public LoadingView(Context context) {
		super(context);
		init();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_loading_screen, this);

		if (isInEditMode())
			return;

		_vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

		_gear1 = (ImageView) findViewById(R.id.loading_gear1);
		_gear2 = (ImageView) findViewById(R.id.loading_gear2);
		_gear3 = (ImageView) findViewById(R.id.loading_gear3);
		_center = (ImageView) findViewById(R.id.center_imageview);
		_center.setOnTouchListener(_cetner_onTouch);
		_messageTextview = (TextView) findViewById(R.id.message_textview);
		_obstructTextView = (TextView) findViewById(R.id.obstruct_textview);

		_rotateAnim1 = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.anim_spingear_cw);
		_rotateAnim2 = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.anim_spingear_cw);
		_rotateAnim3 = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.anim_spingear_ccw);

		_gear1.startAnimation(_rotateAnim1);
		_gear2.startAnimation(_rotateAnim2);
		_gear3.startAnimation(_rotateAnim3);

		_done = false;
		_mouseDown = false;
	}

	private void stopAnimation() {
		_rotateAnim1.cancel();
		_rotateAnim2.cancel();
		_rotateAnim3.cancel();
	}

	private void startAnimation() {
		_rotateAnim1.reset();
		_rotateAnim2.reset();
		_rotateAnim3.reset();
		_rotateAnim1.start();
		_rotateAnim2.start();
		_rotateAnim3.start();
	}

	public void startLoading() {
		_done = false;
		setVisibility(View.VISIBLE);
		startAnimation();
	}

	public void requestStopLoading() {
		_done = true;
	}

	public void stopLoading() {
		_done = true;
		stopAnimation();
		setVisibility(View.GONE);
	}

	private final View.OnTouchListener _cetner_onTouch = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				_vib.vibrate(VIB_PATTERN, -1);
				_obstructTextView.setVisibility(View.VISIBLE);
				stopAnimation();
				_mouseDown = true;
				return true;
			case MotionEvent.ACTION_UP:
				if (_done) {
					stopLoading();
				} else {
					startAnimation();
					_obstructTextView.setVisibility(View.GONE);
					_mouseDown = false;
				}
				return true;
			}
			return false;
		}
	};

}
