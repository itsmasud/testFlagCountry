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
 * Created by Michael on 9/23/2016.
 */

public abstract class FullScreenDialog implements Dialog {
    private static final String TAG = "FullScreenDialog";

    // Ui
    private View _root;
    private RelativeLayout _container;
    private View _child;

    // Animations
    private Animation _bgFadeIn;
    private Animation _bgFadeOut;
    private Animation _fgSlideIn;
    private Animation _fgSlideOut;

    // Listener
    private DismissListener _listener;

    public FullScreenDialog(Context context, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);

        _root = inflater.inflate(R.layout.dialog_fullscreen, container, false);
        _container = (RelativeLayout) _root.findViewById(R.id.container);

        _child = onCreateView(inflater, context, _container);
        _container.addView(_child);

        _bgFadeIn = AnimationUtils.loadAnimation(context, R.anim.bg_fade_in);
        _bgFadeOut = AnimationUtils.loadAnimation(context, R.anim.bg_fade_out);
        _fgSlideIn = AnimationUtils.loadAnimation(context, R.anim.fg_slide_in_bottom);
        _fgSlideOut = AnimationUtils.loadAnimation(context, R.anim.fg_slide_out_bottom);

        _bgFadeOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _root.setVisibility(View.GONE);
                if (_listener != null)
                    _listener.onDismissed(FullScreenDialog.this);
            }
        });

        _fgSlideOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _child.setVisibility(View.GONE);
            }
        });
    }

    public abstract View onCreateView(LayoutInflater inflater, Context context, ViewGroup container);

    @Override
    public void onAdded() {
    }

    @Override
    public View getView() {
        return _root;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _root.setVisibility(View.VISIBLE);
        _child.setVisibility(View.VISIBLE);
        if (animate) {
            _container.clearAnimation();
            _container.startAnimation(_bgFadeIn);
            _child.clearAnimation();
            _child.startAnimation(_fgSlideIn);
        }
    }

    @Override
    public void onRestoreDialogState(Parcelable savedState) {
    }

    @Override
    public Parcelable onSaveDialogState() {
        return Bundle.EMPTY;
    }

    @Override
    public void dismiss(boolean animate) {
        if (animate) {
            _container.clearAnimation();
            _container.startAnimation(_bgFadeOut);
            _child.clearAnimation();
            _child.startAnimation(_fgSlideOut);
        } else {
            _child.setVisibility(View.GONE);
            _root.setVisibility(View.GONE);
            if (_listener != null)
                _listener.onDismissed(this);
        }
    }

    @Override
    public void setDismissListener(DismissListener listener) {
        _listener = listener;
    }

    @Override
    public void cancel() {
        dismiss(true);
    }
}
