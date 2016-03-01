package com.fieldnation.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;

/**
 * Created by Michael Carver on 1/28/2015.
 */
public class PagerTabListView extends RelativeLayout {
    private static final String TAG = UniqueTag.makeTag("PagerTabListView");

    // Ui
    private LinearLayout _tabContainer;
    //    private View _tabSelector;
    private ToolbarTabView[] _tabs;

    // Data
    private int _selectedTab = 0;
    private ViewPager _pager;

    public PagerTabListView(Context context) {
        super(context);
        init();
    }

    public PagerTabListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagerTabListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_pager_tab_list, this, true);

        _tabContainer = (LinearLayout) findViewById(R.id.tab_container);
//        _tabSelector = findViewById(R.id.tab_selector);
    }

    public void setViewPager(ViewPager pager) {
        _pager = pager;
        populateUi();
    }

    private void populateUi() {
        int count = _pager.getAdapter().getCount();

        _tabs = new ToolbarTabView[count];
        // build tabs
        _tabContainer.removeAllViews();
        for (int i = 0; i < count; i++) {
            ToolbarTabView v = getTab(i, _pager.getAdapter().getPageTitle(i));

            v.setTag(i);
            v.setOnClickListener(_tab_onClick);
            _tabContainer.addView(v);
            _tabs[i] = v;
        }
    }

    private ToolbarTabView getTab(int position, CharSequence title) {
        ToolbarTabView tv = new ToolbarTabView(getContext(), null, R.style.TextViewEmptyLabel);
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        param.weight = 1f;
        param.gravity = Gravity.CENTER;
        tv.setLayoutParams(param);

        return tv;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setSelected(_selectedTab);
    }

    public void setSelected(int position) {

        if (position > _selectedTab) {
            _tabs[_selectedTab].animateUnhighlightRight();
            _tabs[position].animateHighlightRight();
        } else if (position < _selectedTab) {
            _tabs[_selectedTab].animateUnhighlightLeft();
            _tabs[position].animateHighlightLeft();
        } else {
            _tabs[_selectedTab].setHighlight();
        }

        _tabs[_selectedTab].setSelected(false);

        _selectedTab = position;

        View tab = _tabs[position];
//        _tabSelector.setLeft(tab.getLeft());
//        _tabSelector.setRight(tab.getRight());
        tab.setSelected(true);
    }


    private final OnClickListener _tab_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer i = (Integer) v.getTag();
            setSelected(i);
            _pager.setCurrentItem(i, true);
        }
    };

}
