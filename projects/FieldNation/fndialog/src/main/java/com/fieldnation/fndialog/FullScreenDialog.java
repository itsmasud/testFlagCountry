package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.misc;

/**
 * Created by Michael on 9/23/2016.
 */

public abstract class FullScreenDialog implements Dialog {
    private static final String TAG = "FullScreenDialog";

    // Ui
    private View _root;
    private View _clickBarrier;
    private RelativeLayout _container;
    private View _child;

    // Animations
    private Animation _bgFadeIn;
    private Animation _bgFadeOut;
    private Animation _fgSlideIn;
    private Animation _fgSlideOut;

    // Listener
    private DismissListener _dismissListener;

    // Data
    private String _uid;
    private Bundle _savedState;

    public FullScreenDialog(Context context, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);

        _root = inflater.inflate(R.layout.dialog_fullscreen, container, false);
        _clickBarrier = _root.findViewById(R.id.click_barrier);
        _container = _root.findViewById(R.id.container);

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
                _root.post(new Runnable() {
                    @Override
                    public void run() {
                        if (_dismissListener != null)
                            _dismissListener.onDismissed(FullScreenDialog.this);
                    }
                });
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

    public Context getContext() {
        return _root.getContext();
    }

    @Override
    public void setSavedState(Bundle savedState) {
        this._savedState = savedState;
    }

    @Override
    public Bundle getSavedState() {
        return _savedState;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public String getUid() {
        return _uid;
    }

    @Override
    public void setUid(String uid) {
        _uid = uid;
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
            _clickBarrier.clearAnimation();
            _clickBarrier.startAnimation(_bgFadeIn);
            _child.clearAnimation();
            _child.startAnimation(_fgSlideIn);
        }
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
    }

    @Override
    public void dismiss(boolean animate) {
        misc.hideKeyboard(getView());
        if (animate) {
            _clickBarrier.clearAnimation();
            _clickBarrier.startAnimation(_bgFadeOut);
            _child.clearAnimation();
            _child.startAnimation(_fgSlideOut);
        } else {
            _child.setVisibility(View.GONE);
            _root.setVisibility(View.GONE);
            if (_dismissListener != null)
                _dismissListener.onDismissed(this);
        }
    }

    @Override
    public void setDismissListener(DismissListener listener) {
        _dismissListener = listener;
    }

    @Override
    public void cancel() {
    }
}
