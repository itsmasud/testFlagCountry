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
import android.widget.TabHost;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CounterOfferInfo;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;

import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class CounterOfferDialog extends DialogFragment {
    private static final String TAG = "ui.dialog.CounterOfferDialog";

    // Ui
    private TabHost _tabHost;
    private Button _backButton;
    private Button _okButton;

    private PaymentCoView _paymentView;

    private PayDialog _payDialog;

    // Data
    private FragmentManager _fm;
    private Workorder _workorder;
    private Pay _counterPay;

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

        _paymentView = (PaymentCoView) v.findViewById(R.id.payment_view);
        _paymentView.setListener(_payment_listener);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _backButton = (Button) v.findViewById(R.id.back_button);
        _backButton.setOnClickListener(_back_onClick);

        _payDialog = PayDialog.getInstance(getFragmentManager(), TAG);


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

        populateUi();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_paymentView == null)
            return;


        if (_counterPay != null)
            _paymentView.setPay(_counterPay, true);
        else
            _paymentView.setPay(_workorder.getPay(), false);
    }

    public void show(String tag, Workorder workorder) {
        _workorder = workorder;

        CounterOfferInfo info = _workorder.getCounterOfferInfo();

        if (info != null && info.getPay() != null) {
            _counterPay = info.getPay();
        }

        super.show(_fm, tag);
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private PaymentCoView.Listener _payment_listener = new PaymentCoView.Listener() {
        @Override
        public void onClearClick() {
            _counterPay = null;
            populateUi();
        }

        @Override
        public void onChangeClick(Pay pay) {
            _payDialog.show(TAG, pay, _payDialog_listener);
        }
    };

    private PayDialog.Listener _payDialog_listener = new PayDialog.Listener() {
        @Override
        public void onPerDevices(double rate, double max) {
            _counterPay = new Pay(rate, (int) max);
            populateUi();
        }

        @Override
        public void onHourly(double rate, double max) {
            _counterPay = new Pay(rate, max);
            populateUi();
        }

        @Override
        public void onFixed(double amount) {
            _counterPay = new Pay(amount);
            populateUi();
        }

        @Override
        public void onBlended(double rate, double max, double rate2, double max2) {
            _counterPay = new Pay(rate, max, rate2, max2);
            populateUi();
        }

        @Override
        public void onNothing() {
        }
    };


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
