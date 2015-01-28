package com.fieldnation.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;

/**
 * Created by Michael Carver on 1/28/2015.
 */
public class PagerTabListView extends RelativeLayout {
    private static final String TAG = UniqueTag.makeTag("ui.PagerTabListView");

    // Ui
    private LinearLayout _tabContainer;
    private View _tabSelector;
    private View[] _tabs;

    // Data
    private int _selectedTab = 0;
    private ViewPager _pager;
    private TabFactory _tabFactory = null;

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
        _tabSelector = findViewById(R.id.tab_selector);
    }

    public void setTabFactory(TabFactory tabFactory) {
        _tabFactory = tabFactory;
    }

    public void setViewPager(ViewPager pager) {
        _pager = pager;
        _pager.setOnPageChangeListener(_pageChangeListener);
        populateUi();
    }

    private void populateUi() {
        int count = _pager.getAdapter().getCount();

        _tabs = new View[count];
        // build tabs
        _tabContainer.removeAllViews();
        for (int i = 0; i < count; i++) {
            View v = null;
            if (_tabFactory != null)
                v = _tabFactory.getTab(i, _pager.getAdapter().getPageTitle(i));
            else
                v = getTab(i, _pager.getAdapter().getPageTitle(i));

            v.setTag(i);
            v.setOnClickListener(_tab_onClick);
            _tabContainer.addView(v);
            _tabs[i] = v;
        }
    }

    private View getTab(int position, CharSequence title) {
        TextView tv = new TextView(getContext(), null, R.style.TextViewEmptyLabel);
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
        _tabs[_selectedTab].setSelected(false);

        _selectedTab = position;

        View tab = _tabs[position];
        _tabSelector.setLeft(tab.getLeft());
        _tabSelector.setRight(tab.getRight());
        tab.setSelected(true);
    }

    private final ViewPager.OnPageChangeListener _pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private final OnClickListener _tab_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer i = (Integer) v.getTag();
            setSelected(i);
            _pager.setCurrentItem(i, true);
        }
    };

    public interface TabFactory {
        public View getTab(int position, CharSequence title);
    }

}
