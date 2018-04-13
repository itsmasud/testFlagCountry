package com.fieldnation.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.service.data.payment.PaymentClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.EmptyCardView;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.payment.MonthHeaderView.Header;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class PaymentListActivity extends AuthSimpleActivity {
    private static final String TAG = "PaymentListActivity";

    // UI
    private OverScrollListView _listView;
    private RefreshView _refreshView;
    private EmptyCardView _emptyView;

    // Data
    private PaymentClient _paymentClient;
    private final Hashtable<Integer, List<Payment>> _pages = new Hashtable<>();
    private final List<Object> _paymentList = new LinkedList<>();
    private PaymentListAdapter _adapter;
    private int _nextPage = 0;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    public int getLayoutResource() {
        return R.layout.activity_itemlist;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _listView = (OverScrollListView) findViewById(R.id.items_listview);
        _listView.setOnOverScrollListener(_refreshView);
        _adapter = new PaymentListAdapter(_paymentList, _paymentListAdapter);
        _listView.setAdapter(_adapter);

        _emptyView = (EmptyCardView) findViewById(R.id.empty_view);
        _emptyView.setData(EmptyCardView.PARAM_VIEW_TYPE_PAYMENT);

    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _paymentClient = new PaymentClient(_payment_listener);
        _paymentClient.connect(App.get());

        _refreshView.startRefreshing();
        PaymentClient.list(this, 0);
    }

    @Override
    protected void onPause() {
        if (_paymentClient != null) _paymentClient.disconnect(App.get());
        super.onPause();
    }

    @Override
    public void onProfile(Profile profile) {
    }

    private void rebuildList() {
        _paymentList.clear();
        int lastHash = 0;
        Calendar futureCal = null;
        Header header = null;

        for (int i = 0; i < _pages.size(); i++) {
            if (!_pages.containsKey(i))
                break;

            List<Payment> payments = _pages.get(i);

            for (int j = 0; j < payments.size(); j++) {
                Payment payment = payments.get(j);

                if (payment.getDatePaid() == null) {
                    //_paymentList.add(new Header(null, payment.getAmount(), -1));
                    _paymentList.add(payment);
                    continue;
                }

                Calendar pastCal = payment.getDatePaidCalendar();
                int hash = pastCal.get(Calendar.YEAR) * 100 + pastCal.get(Calendar.MONTH);

                if (hash != lastHash) {
                    // next will be further in the past
                    // prev will be future
                    if (futureCal != null) {
                        futureCal.add(Calendar.MONTH, -1);
                        int lastCalHash = futureCal.get(Calendar.YEAR) * 100 + futureCal.get(Calendar.MONTH);

                        // ok, there is at least one year skip in here.
                        while (futureCal.get(Calendar.YEAR) > pastCal.get(Calendar.YEAR)) {
                            int year = futureCal.get(Calendar.YEAR);

                            Header h = new Header(ISO8601.fromCalendar(futureCal), 0.0, lastCalHash);
                            futureCal.add(Calendar.MONTH, -1);
                            while (futureCal.get(Calendar.YEAR) == year) {
                                h.endDate = ISO8601.fromCalendar(futureCal);
                                futureCal.add(Calendar.MONTH, -1);
                            }
                            _paymentList.add(h);
                            _paymentList.add(new Placeholder());
                            lastCalHash = futureCal.get(Calendar.YEAR) * 100 + futureCal.get(Calendar.MONTH);
                        }

                        if (futureCal.get(Calendar.MONTH) - pastCal.get(Calendar.MONTH) == 1) {
                            Header h = new Header(ISO8601.fromCalendar(futureCal), 0.0, lastCalHash);
                            _paymentList.add(h);
                            _paymentList.add(new Placeholder());
                            futureCal.add(Calendar.MONTH, -1);
                            lastCalHash = futureCal.get(Calendar.YEAR) * 100 + futureCal.get(Calendar.MONTH);
                        } else if (futureCal.get(Calendar.MONTH) - pastCal.get(Calendar.MONTH) > 1) {
                            Header h = new Header(ISO8601.fromCalendar(futureCal), 0.0, lastCalHash);
                            futureCal.add(Calendar.MONTH, -1);
                            while (futureCal.get(Calendar.MONTH) > pastCal.get(Calendar.MONTH)) {
                                h.endDate = ISO8601.fromCalendar(futureCal);
                                futureCal.add(Calendar.MONTH, -1);
                            }
                            _paymentList.add(h);
                            _paymentList.add(new Placeholder());
                            lastCalHash = futureCal.get(Calendar.YEAR) * 100 + futureCal.get(Calendar.MONTH);
                        }

                        //while (lastCalHash > hash) {
                        //    _paymentList.add(new Header(ISO8601.fromCalendar(futureCal), 0.0, lastCalHash));
                        //    _paymentList.add(new Placeholder());
                        //    futureCal.add(Calendar.MONTH, -1);
                        //    lastCalHash = futureCal.get(Calendar.YEAR) * 100 + futureCal.get(Calendar.MONTH);
                        //}
                    }

                    lastHash = hash;
                    futureCal = pastCal;
                    header = new Header(payment.getDatePaid(), 0.0, hash);
                    _paymentList.add(header);
                }
                if (header != null)
                    header.amount += payment.getAmount();

                _paymentList.add(payment);
            }
        }
        _adapter.notifyDataSetChanged();
    }

    private final PaymentClient.Listener _payment_listener = new PaymentClient.Listener() {
        @Override
        public void onConnected() {
            _paymentClient.subList();
        }

        @Override
        public void onList(int page, List<Payment> list, boolean failed, boolean isCached) {
            if (list == null || list.size() == 0) {
                _nextPage = -1;
                if (_paymentList.size() == 0)
                    _emptyView.setVisibility(View.VISIBLE);
                return;
            } else {
                _emptyView.setVisibility(View.GONE);
            }

            _pages.put(page, list);
            rebuildList();
            _nextPage = page + 1;

            if (_refreshView != null)
                _refreshView.refreshComplete();
        }
    };

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            _nextPage = 1;
            PaymentClient.list(App.get(), 0);
        }
    };

    private final PaymentListAdapter.Listener _paymentListAdapter = new PaymentListAdapter.Listener() {
        @Override
        public void onNextPage() {
            if (_nextPage > -1)
                PaymentClient.list(App.get(), _nextPage);
        }
    };

    public static void startNew(Context context) {
        Intent intent = new Intent(context, PaymentListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityClient.startActivity(intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);

    }
}
