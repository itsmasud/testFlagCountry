package com.fieldnation.ui.payment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.service.data.payment.PaymentClient;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.payment.MonthHeaderView.Header;
import com.fieldnation.utils.ISO8601;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class PaymentListActivity extends AuthActionBarActivity {
    private static final String TAG = "PaymentListActivity";

    // UI
    private OverScrollListView _listView;
    private RefreshView _refreshView;

    // Data
    private PaymentClient _paymentClient;
    private Hashtable<Integer, List<Payment>> _pages = new Hashtable<>();
    private List<Object> _paymentList = new LinkedList<>();
    private MyAdapter _adapter = new MyAdapter(_paymentList);

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
        _listView.setAdapter(_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _refreshView.startRefreshing();
        _paymentClient = new PaymentClient(_payment_listener);
        _paymentClient.connect(App.get());

        PaymentClient.list(this, 0);
        _refreshView.startRefreshing();
    }

    @Override
    protected void onPause() {
        if (_paymentClient != null && _paymentClient.isConnected())
            _paymentClient.disconnect(App.get());
        super.onPause();
    }

    private void rebuildList() {
        _paymentList.clear();

        int lastHash = 0;
        Calendar lastCal = null;
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

                Calendar cal = payment.getDatePaidCalendar();
                int hash = cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH);

                if (hash != lastHash) {
                    if (lastCal != null) {
                        lastCal.add(Calendar.MONTH, -1);
                        int lastCalHash = lastCal.get(Calendar.YEAR) * 100 + lastCal.get(Calendar.MONTH);
                        while (lastCalHash > hash) {
                            _paymentList.add(new Header(ISO8601.fromCalendar(lastCal), 0.0, lastCalHash));
                            _paymentList.add(new Placeholder());
                            lastCal.add(Calendar.MONTH, -1);
                            lastCalHash = lastCal.get(Calendar.YEAR) * 100 + lastCal.get(Calendar.MONTH);
                        }
                    }

                    lastHash = hash;
                    lastCal = cal;
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
                if (_refreshView != null)
                    _refreshView.refreshComplete();
                return;
            }

            _pages.put(page, list);
            rebuildList();
            PaymentClient.list(App.get(), page + 1);
        }
    };

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            PaymentClient.list(App.get(), 0);
        }
    };

    private static class Placeholder {
        public Placeholder() {
        }
    }

    private static class MyAdapter extends BaseAdapter {
        private List<Object> _items = new LinkedList<>();

        public MyAdapter(List<Object> items) {
            _items = items;
        }

        @Override
        public int getCount() {
            return _items.size();
        }

        @Override
        public Object getItem(int position) {
            return _items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object object = getItem(position);

            if (object instanceof Placeholder) {
                NoPaymentsTileView v = null;

                if (convertView == null)
                    v = new NoPaymentsTileView(parent.getContext());
                else if (convertView instanceof NoPaymentsTileView)
                    v = (NoPaymentsTileView) convertView;
                else
                    v = new NoPaymentsTileView(parent.getContext());

                return v;
            } else if (object instanceof Header) {
                Header header = (Header) object;

                MonthHeaderView v = null;

                if (convertView == null)
                    v = new MonthHeaderView(parent.getContext());
                else if (convertView instanceof MonthHeaderView)
                    v = (MonthHeaderView) convertView;
                else
                    v = new MonthHeaderView(parent.getContext());

                v.setData(header.startDate, header.amount);
                return v;
            } else if (object instanceof Payment) {
                PaymentCardView v = null;

                if (convertView == null)
                    v = new PaymentCardView(parent.getContext());
                else if (convertView instanceof PaymentCardView)
                    v = (PaymentCardView) convertView;
                else
                    v = new PaymentCardView(parent.getContext());

                v.setData((Payment) object);
                return v;
            }
            return null;
        }
    }
}
