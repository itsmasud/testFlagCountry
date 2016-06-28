package com.fieldnation.ui.market;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fieldnation.R;
import com.fieldnation.ui.TabActionBarFragmentActivity;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.ui.workorder.WorkorderListFragment;

import java.util.List;

/**
 * Displays all the work orders in the market that are available to this user
 *
 * @author michael.carver
 */
public class MarketActivity extends TabActionBarFragmentActivity {
    private static final String TAG = "MarketActivity";

    // UI
    private WorkorderListFragment[] _fragments;
    private String[] _titles;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        setTitle(R.string.activity_market_title);
        super.onFinishCreate(savedInstanceState);
    }

    @Override
    public void loadFragments() {
        _fragments = new WorkorderListFragment[3];

        _fragments[0] = getFragment(WorkorderDataSelector.ROUTED);
        _fragments[1] = getFragment(WorkorderDataSelector.AVAILABLE);
        _fragments[2] = getFragment(WorkorderDataSelector.REQUESTED);

        _titles = new String[]{getString(R.string.tab_routed), getString(R.string.tab_available), getString(R.string.tab_requested)};
    }

    private WorkorderListFragment getFragment(WorkorderDataSelector selector) {
        WorkorderListFragment fragment = null;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) instanceof WorkorderListFragment) {
                    WorkorderListFragment frag = (WorkorderListFragment) fragments.get(i);
                    if (frag.getDisplayType() == selector) {
                        fragment = frag;
                        break;
                    }
                }
            }
        }

        if (fragment == null) {
            if (selector.equals(WorkorderDataSelector.AVAILABLE)) {
                fragment = new AvailableWorkorderListFragment().setDisplayType(selector);
            } else if (selector.equals(WorkorderDataSelector.ROUTED)) {
                fragment = new RoutedWorkorderListFragment().setDisplayType(selector);
            } else {
                fragment = new WorkorderListFragment().setDisplayType(selector);
            }
        }
        return fragment;
    }

    @Override
    public int getFragmentCount() {
        return _fragments.length;
    }

    @Override
    public String getFragmentTitle(int index) {
        return _titles[index];
    }

    @Override
    public TabFragment getFragment(int index) {
        return _fragments[index];
    }
}

