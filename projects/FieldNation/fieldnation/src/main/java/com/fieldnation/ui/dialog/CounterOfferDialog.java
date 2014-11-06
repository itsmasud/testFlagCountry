package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.fieldnation.R;

import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends DialogFragment {

    // Ui
    private TabHost _tabHost;
    private Button _backButton;
    private Button _okButton;
    private HorizontalScrollView _scrollView;

    // Data
    private FragmentManager _fm;

    public static CounterOfferDialog getInstance(FragmentManager fm, String tag) {
        CounterOfferDialog d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (frag instanceof CounterOfferDialog && frag.getTag().equals(tag)) {
                    d = (CounterOfferDialog) frag;
                    break;
                }
            }
        }
        if (d == null)
            d = new CounterOfferDialog();
        d._fm = fm;
        return d;
    }


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_counter_offer, container, false);

        _tabHost = (TabHost) v.findViewById(R.id.tabhost);
        _tabHost.setup();

        TabHost.TabSpec tab1 = _tabHost.newTabSpec("start");
        tab1.setIndicator("Pay");
        tab1.setContent(R.id.scrollview1);
        _tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = _tabHost.newTabSpec("mid1");
        tab2.setIndicator("Schedule");
        tab2.setContent(R.id.scrollview2);
        _tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = _tabHost.newTabSpec("mid2");
        tab3.setIndicator("Expense");
        tab3.setContent(R.id.scrollview3);
        _tabHost.addTab(tab3);

        TabHost.TabSpec tab4 = _tabHost.newTabSpec("end");
        tab4.setIndicator("Reason");
        tab4.setContent(R.id.scrollview4);
        _tabHost.addTab(tab4);

        _tabHost.setOnTabChangedListener(_tab_changeListener);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _backButton = (Button) v.findViewById(R.id.back_button);
        _backButton.setOnClickListener(_back_onClick);

        _scrollView = (HorizontalScrollView) v.findViewById(R.id.tabscroll_view);

        for (int i = 0; i < 4; i++) {
            _tabHost.getTabWidget().getChildAt(i).setFocusableInTouchMode(true);
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Dialog d = getDialog();

        if (d == null)
            return;

        Window window = d.getWindow();

        Display display = window.getWindowManager().getDefaultDisplay();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.setLayout((display.getWidth() * 9) / 10, (display.getHeight() * 7) / 10);
        } else {
            window.setLayout((display.getWidth() * 9) / 10, (display.getHeight() * 9) / 10);
        }
        //window.setGravity(Gravity.TOP);

    }

    public void show(String tag) {
        super.show(_fm, tag);
    }

    private TabHost.OnTabChangeListener _tab_changeListener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            _backButton.setVisibility(View.VISIBLE);
            _okButton.setText("NEXT");
            if (tabId.equals("start")) {
                _backButton.setVisibility(View.GONE);
            } else if (tabId.startsWith("mid")) {
            } else if (tabId.equals("end")) {
                _okButton.setText("FINISH");
            }
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // start?
            if (_tabHost.getCurrentTabTag().equals("start")) {
                _tabHost.setCurrentTab(_tabHost.getCurrentTab() + 1);
            } else if (_tabHost.getCurrentTabTag().startsWith("mid")) {
                _tabHost.setCurrentTab(_tabHost.getCurrentTab() + 1);
            } else if (_tabHost.getCurrentTabTag().equals("end")) {
                // todo finish
            }
        }
    };

    private View.OnClickListener _back_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _tabHost.setCurrentTab(_tabHost.getCurrentTab() - 1);
        }
    };
}
