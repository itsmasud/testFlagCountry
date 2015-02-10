package com.fieldnation.ui.workorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fieldnation.R;
import com.fieldnation.ui.TabActionBarFragmentActivity;

import java.util.List;

public class MyWorkActivity extends TabActionBarFragmentActivity {
    private static final String TAG = "ui.workorder.MyWorkActivity";

    // Data
    private WorkorderListFragment[] _fragments;
    private String[] _titles;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        setTitle(R.string.mywork_title);
        super.onFinishCreate(savedInstanceState);
    }

    @Override
    public void loadFragments() {
        _fragments = new WorkorderListFragment[3];
        _fragments[0] = getFragment(WorkorderDataSelector.ASSIGNED);
        _fragments[1] = getFragment(WorkorderDataSelector.COMPLETED);
        _fragments[2] = getFragment(WorkorderDataSelector.CANCELED);

        _titles = new String[]{getString(R.string.tab_assigned), getString(R.string.tab_completed), getString(R.string.tab_canceled)};
    }

    private WorkorderListFragment getFragment(WorkorderDataSelector selector) {
        Fragment fragment = null;
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
            fragment = new WorkorderListFragment().setDisplayType(selector);
        }
        return (WorkorderListFragment) fragment;
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

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
    }
 public static void startNew(Context context) {
        Intent intent = new Intent(context, MyWorkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
