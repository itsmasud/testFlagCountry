package com.fieldnation.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.ui.workorder.ActionBarTopView;

/**
 * Created by Shoaib on 5/5/18.
 */

public class FnToolBarView extends RelativeLayout {
    private static final String TAG = "FnToolBar";

    // Ui
    private Toolbar _toolbar;
    private IconFontTextView _arrowTextView;
    private ActionBarTopView _topBar;
    private TextView _orangeBarTextView;

    public FnToolBarView(Context context) {
        super(context);
        init();
    }

    public FnToolBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FnToolBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_fn_toolbar, this);

        if (isInEditMode())
            return;
        _toolbar = findViewById(R.id.toolbar);
        _arrowTextView = findViewById(R.id.arrow_textview);
        _arrowTextView.setVisibility(GONE);
        _topBar = findViewById(R.id.actiontop_view);
        _orangeBarTextView = findViewById(R.id.offline_bar_textview);
        refresh();
    }

    public Toolbar getToolbar() {
        return _toolbar;
    }

    public Toolbar setScrollFlag(AppBarLayout.LayoutParams params, int scrollFlag) {
        if (_toolbar == null) return null;

        params.setScrollFlags(scrollFlag);
        _toolbar.setLayoutParams(params);

        return _toolbar;
    }

    public ActionBarTopView getTopBar() {
        return _topBar;
    }


    public IconFontTextView getArrowView() {
        return _arrowTextView;
    }

    public void refresh() {
        populateUi();
    }

    private void populateUi() {
        if (_orangeBarTextView == null) return;

        _orangeBarTextView.setVisibility(App.get().getOfflineState() == App.OfflineState.OFFLINE ? VISIBLE : GONE);
    }

}
