package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TabHost;

import com.fieldnation.R;

import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends DialogFragment {

    // Ui
    private TabHost _tabHost;

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

        TabHost.TabSpec tab1 = _tabHost.newTabSpec("Tab1");
        tab1.setIndicator("Pay");
        tab1.setContent(R.id.payment_view);
        _tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = _tabHost.newTabSpec("Tab2");
        tab2.setIndicator("Schedule");
        tab2.setContent(R.id.schedule_view);
        _tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = _tabHost.newTabSpec("Tab3");
        tab3.setIndicator("Expense");
        tab3.setContent(R.id.expenses_view);
        _tabHost.addTab(tab3);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    public void show(String tag) {
        super.show(_fm, tag);
    }
}
