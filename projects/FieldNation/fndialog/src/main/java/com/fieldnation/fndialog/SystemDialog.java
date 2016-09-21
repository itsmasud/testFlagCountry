package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.fieldnation.fntools.DefaultAnimationListener;

/**
 * Created by Michael on 9/20/2016.
 */

public abstract class SystemDialog extends RelativeLayout implements Dialog {
    private static final String TAG = "SystemDialog";

    // Ui
    private RelativeLayout _container;

    // Animations
    private Animation _bgFadeIn;
    private Animation _bgFadeOut;
    private Animation _fgFadeIn;
    private Animation _fgFadeOut;

    // Data
    private boolean _isCancelable = true;

    public SystemDialog(Context context) {
        super(context);
        init();
    }

    public SystemDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SystemDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_base, this);

        if (isInEditMode())
            return;

        getContainer().setOnClickListener(_this_onClick);

        _bgFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.bg_fade_in);
        _bgFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.bg_fade_out);
        _fgFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fg_fade_in);
        _fgFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fg_fade_out);

        _bgFadeOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
            }
        });
    }

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    // Handles children
    private RelativeLayout getContainer() {
        if (_container == null)
            _container = (RelativeLayout) findViewById(R.id.container);

        return _container;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.container || getContainer() == null)
            super.addView(child, index, params);
        else
            getContainer().addView(child, index, params);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean isCancelable() {
        return _isCancelable;
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        setVisibility(VISIBLE);
        if (animate) {
            View child = _container.getChildAt(0);
            _container.startAnimation(_bgFadeIn);
            child.startAnimation(_fgFadeIn);
        }
    }

    @Override
    public void onRestoreDialogState(Parcelable savedState) {

    }

    @Override
    public Parcelable onSaveDialogState() {
        return null;
    }

    @Override
    public void dismiss(boolean animate) {
        if (animate) {
            View child = _container.getChildAt(0);
            _container.startAnimation(_bgFadeOut);
            child.startAnimation(_fgFadeOut);
        }
    }

    @Override
    public void cancel() {
        dismiss(true);
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isCancelable())
                cancel();
        }
    };
}
