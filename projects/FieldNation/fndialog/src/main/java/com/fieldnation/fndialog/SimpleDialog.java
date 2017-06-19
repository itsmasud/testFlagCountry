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
 * Created by Michael on 9/20/2016.
 */

public abstract class SimpleDialog implements Dialog {
    private static final String TAG = "SimpleDialog";

    // Ui
    private View _root;
    private View _clickBarrier;
    private RelativeLayout _container;
    private View _child;

    // Animations
    private Animation _bgFadeIn;
    private Animation _bgFadeOut;
    private Animation _fgFadeIn;
    private Animation _fgFadeOut;

    // Listeners
    private DismissListener _dismissListener;

    // Data
    private String _uid;

    public SimpleDialog(Context context, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);

        _root = inflater.inflate(R.layout.dialog_simple, container, false);
        _container = (RelativeLayout) _root.findViewById(R.id.container);
        _clickBarrier = _root.findViewById(R.id.click_barrier);

        _child = onCreateView(inflater, context, _container);
        _container.addView(_child);

        _bgFadeIn = AnimationUtils.loadAnimation(context, R.anim.bg_fade_in);
        _bgFadeOut = AnimationUtils.loadAnimation(context, R.anim.bg_fade_out);
        _fgFadeIn = AnimationUtils.loadAnimation(context, R.anim.fg_fade_in);
        _fgFadeOut = AnimationUtils.loadAnimation(context, R.anim.fg_fade_out);

        _bgFadeOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _root.setVisibility(View.GONE);
                _root.post(new Runnable() {
                    @Override
                    public void run() {
                        if (_dismissListener != null)
                            _dismissListener.onDismissed(SimpleDialog.this);
                    }
                });
            }
        });

        _fgFadeOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _child.setVisibility(View.GONE);
            }
        });
    }

    public Context getContext() {
        return _root.getContext();
    }

    public abstract View onCreateView(LayoutInflater inflater, Context context, ViewGroup container);

    @Override
    public void onStart() {
        _clickBarrier.setOnClickListener(_this_onClick);
        _clickBarrier.setClickable(true);
        _container.setClickable(false);
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
    public void show(Bundle payload, boolean animate) {
        _root.setVisibility(View.VISIBLE);
        _child.setVisibility(View.VISIBLE);
        if (animate) {
            _clickBarrier.clearAnimation();
            _clickBarrier.startAnimation(_bgFadeIn);
            _child.clearAnimation();
            _child.startAnimation(_fgFadeIn);
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
            _child.startAnimation(_fgFadeOut);
        } else {
            _child.setVisibility(View.GONE);
            _root.setVisibility(View.GONE);
            if (_dismissListener != null)
                _dismissListener.onDismissed(this);
        }
    }

    @Override
    public void cancel() {
    }

    @Override
    public void setDismissListener(DismissListener listener) {
        _dismissListener = listener;
    }

    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isCancelable()) {
                cancel();
                dismiss(true);
            }
        }
    };
}
