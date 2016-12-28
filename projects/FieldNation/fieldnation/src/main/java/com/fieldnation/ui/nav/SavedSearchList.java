package com.fieldnation.ui.nav;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.data.savedsearch.SavedSearchClient;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;

import java.util.List;

/**
 * Created by Michael on 8/25/2016.
 */
@DefaultBehavior(SavedSearchList.Behavior.class)
public class SavedSearchList extends RelativeLayout {
    private static final String TAG = "SavedSearchList";

    // Ui
    private LinearLayout _paramList;

    // Data
    private OnHideListener _onHideListener;
    private OnShowListener _onShowListener;
    private OnSavedSearchParamsChangeListener _onSavedSearchParamsChangeListener;
    private List<SavedSearchParams> _list;

    private SavedSearchClient _savedSearchClient;

    public SavedSearchList(Context context) {
        super(context);
        init();
    }

    public SavedSearchList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SavedSearchList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_saved_search_list, this);

        if (isInEditMode())
            return;

        _paramList = (LinearLayout) findViewById(R.id.param_list);

        setVisibility(GONE);

        _savedSearchClient = new SavedSearchClient(_savedSearchClient_listener);
        _savedSearchClient.connect(App.get());

        populateUi();
    }

    private void populateUi() {
        if (_paramList == null)
            return;

        if (_list == null)
            return;

        _paramList.removeAllViews();
        for (int i = 0; i < _list.size(); i++) {
            SavedSearchRow row = new SavedSearchRow(getContext());
            _paramList.addView(row);
            row.setSearchParams(_list.get(i));
            row.setOnSearchSelectedListener(_savedSearchRow_onChange);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt("VISIBILITY", getVisibility());
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            if (((Bundle) state).containsKey("VISIBILITY"))
                setVisibility(((Bundle) state).getInt("VISIBILITY") == GONE ? GONE : VISIBLE);
            if (((Bundle) state).containsKey("SUPER"))
                super.onRestoreInstanceState(((Bundle) state).getParcelable("SUPER"));
        }
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        _onShowListener = onShowListener;
    }

    public void setOnHideListener(OnHideListener onHideListener) {
        _onHideListener = onHideListener;
    }

    public void setOnSavedSearchParamsChangeListener(OnSavedSearchParamsChangeListener listener) {
        _onSavedSearchParamsChangeListener = listener;
    }

    public void hide() {
        if (getBehavior() == null)
            return;

        getBehavior().startHidingAnimation(this);
    }

    public void show() {
        if (getBehavior() == null)
            return;

        getBehavior().startShowingAnimation(this);
    }

    private Behavior getBehavior() {
        if (getLayoutParams() == null || !(getLayoutParams() instanceof CoordinatorLayout.LayoutParams))
            return null;

        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) getLayoutParams()).getBehavior();
        if (behavior == null || !(behavior instanceof Behavior))
            return null;

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
        return (Behavior) params.getBehavior();
    }

    private void setYPos(int y) {
        int h = getHeight();
        setTop(y);
        setBottom(y + h);
    }

    private final SavedSearchRow.OnSearchSelectedListener _savedSearchRow_onChange = new SavedSearchRow.OnSearchSelectedListener() {
        @Override
        public void onSearch(SavedSearchParams savedSearchParams) {
            hide();
            if (_onSavedSearchParamsChangeListener != null)
                _onSavedSearchParamsChangeListener.onChange(savedSearchParams);
        }
    };

    private final SavedSearchClient.Listener _savedSearchClient_listener = new SavedSearchClient.Listener() {
        @Override
        public void onConnected() {
            _savedSearchClient.subList();
            SavedSearchClient.list();
        }

        @Override
        public void list(List<SavedSearchParams> savedSearchParams) {
            _list = savedSearchParams;
            populateUi();
        }
    };

    public static class Behavior extends CoordinatorLayout.Behavior<SavedSearchList> {
        private static final String TAG = "SavedSearchList.Behavior";
        private static final int ANIMATION_DURATION = 250;

        private static final int MODE_ANIMATING = 0;
        private static final int MODE_SHOWN = 1;
        private static final int MODE_HIDDEN = 2;

        private int _mode = MODE_HIDDEN;

        private int _appBarBottom = 0;

        private ValueAnimator _showingAnimation;
        private ValueAnimator _hidingAnimation;

        public Behavior() {
            super();
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        void startShowingAnimation(final SavedSearchList child) {
            if (_mode == MODE_SHOWN || _mode == MODE_ANIMATING)
                return;
            _mode = MODE_ANIMATING;

            _showingAnimation = ValueAnimator.ofInt(_appBarBottom - child.getHeight(), _appBarBottom);
            _showingAnimation.setDuration(ANIMATION_DURATION);
            _showingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    child.setYPos((Integer) animation.getAnimatedValue());
                }
            });
            _showingAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
//                    setMode(MODE_ATTACHED_TO_APPBAR);
                    _mode = MODE_SHOWN;
                    child.setYPos(_appBarBottom);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            child.setYPos(_appBarBottom - child.getHeight());
            child.setVisibility(VISIBLE);
            _showingAnimation.start();
        }

        void startHidingAnimation(final SavedSearchList child) {
            if (_mode == MODE_HIDDEN || _mode == MODE_ANIMATING)
                return;
            _mode = MODE_ANIMATING;
            _hidingAnimation = ValueAnimator.ofInt(child.getTop(), -child.getHeight());
            _hidingAnimation.setDuration(ANIMATION_DURATION);
            _hidingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    child.setYPos((Integer) animation.getAnimatedValue());
                }
            });
            _hidingAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //setMode(MODE_ATTACHED_TO_APPBAR);
                    _mode = MODE_HIDDEN;
                    child.setVisibility(GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            _hidingAnimation.start();
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, SavedSearchList child, int layoutDirection) {
            parent.onLayoutChild(child, layoutDirection);
            List<View> dependencies = parent.getDependencies(child);
            for (int i = 0; i < dependencies.size(); i++) {
                View dependency = dependencies.get(i);
                if (dependency instanceof AppBarLayout) {
                    //Log.v(TAG, "onLayoutChild " + (dependency.getVisibility() == VISIBLE ? "VISIBLE" : "GONE") + " " + dependency.getBottom());
                    _appBarBottom = dependency.getBottom();
                }
            }
            return true;
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, SavedSearchList child, View dependency) {
            return dependency instanceof AppBarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, SavedSearchList child, View dependency) {
            child.hide();
            return super.onDependentViewChanged(parent, child, dependency);
        }
    }

    public interface OnHideListener {
        void onHide();
    }

    public interface OnShowListener {
        void onShow();
    }

    public interface OnSavedSearchParamsChangeListener {
        void onChange(SavedSearchParams params);
    }
}