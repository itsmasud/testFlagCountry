package com.fieldnation.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;

public class FnToolBarView extends RelativeLayout {
    private static final String TAG = "FnToolBar";

    // Ui
    private Toolbar toolbar;
    private IconFontTextView _arrowTextView;
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
        toolbar = findViewById(R.id.toolbar);
        _arrowTextView =  findViewById(R.id.arrow_textview);
        _orangeBarTextView = findViewById(R.id.offline_bar_textview);
        populateUi();
    }

    public Toolbar getToolbar() {
        return toolbar;
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
