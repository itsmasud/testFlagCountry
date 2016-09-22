package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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

public abstract class SimpleDialog implements Dialog {
    private static final String TAG = "SimpleDialog";

    // Ui
    private View _root;
    private RelativeLayout _container;

    // Animations
    private Animation _bgFadeIn;
    private Animation _bgFadeOut;
    private Animation _fgFadeIn;
    private Animation _fgFadeOut;

    // Data
    private boolean _isCancelable = true;

    public SimpleDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        _root = inflater.inflate(R.layout.dialog_base, null);
        _container = (RelativeLayout) _root.findViewById(R.id.container);

        _container.setOnClickListener(_this_onClick);

        _container.addView(onCreateView(LayoutInflater.from(context), _container));

        _bgFadeIn = AnimationUtils.loadAnimation(context, R.anim.bg_fade_in);
        _bgFadeOut = AnimationUtils.loadAnimation(context, R.anim.bg_fade_out);
        _fgFadeIn = AnimationUtils.loadAnimation(context, R.anim.fg_fade_in);
        _fgFadeOut = AnimationUtils.loadAnimation(context, R.anim.fg_fade_out);

        _bgFadeOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _root.setVisibility(View.GONE);
            }
        });
    }

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    @Override
    public View getView() {
        return _root;
    }

    @Override
    public boolean isCancelable() {
        return _isCancelable;
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _root.setVisibility(View.VISIBLE);
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

    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isCancelable())
                cancel();
        }
    };
}
