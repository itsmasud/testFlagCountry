package com.fieldnation.ui;

import com.fieldnation.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class LoadingView extends RelativeLayout {

	// Ui
	private ImageView _gear1;

	// animations

	RotateAnimation _rotateAnim;

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
		LayoutInflater.from(getContext()).inflate(R.layout.view_loading_screen,
				this);

		if (isInEditMode())
			return;

		_gear1 = (ImageView) findViewById(R.id.loading_gear1);

		_rotateAnim = (RotateAnimation) AnimationUtils.loadAnimation(
				getContext(), R.anim.anim_spingear);

		_gear1.startAnimation(_rotateAnim);
	}
}
