package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;

import java.util.Random;

/**
 * Created by Michael Carver on 6/5/2015.
 */
public class DiscountListLayout extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "DiscountListLayout";

    // UI
    private TextView _noDataTextView;
    private LinearLayout _listView;
    private Button _addButton;

    // Data
    private Listener _listener;
    private Workorder _workorder;

    public DiscountListLayout(Context context) {
        super(context);
        init();
    }

    public DiscountListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiscountListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_discount_layout, this);

        if (isInEditMode())
            return;

        _noDataTextView = (TextView) findViewById(R.id.nodata_textview);
        _listView = (LinearLayout) findViewById(R.id.listview);

        _addButton = (Button) findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_addButton == null)
            return;

        if (_workorder == null)
            return;

        if (_workorder.getPay() == null)
            return;

        if (_workorder.getStatus().getWorkorderStatus() == WorkorderStatus.AVAILABLE
                || _workorder.getPay().hidePay()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        if (_workorder.canChangeDiscounts()) {
            _addButton.setVisibility(VISIBLE);
        } else {
            _addButton.setVisibility(GONE);
        }

        final Discount[] list = _workorder.getDiscounts();
        if (list == null || list.length == 0) {
            _noDataTextView.setVisibility(VISIBLE);
            _listView.setVisibility(GONE);
            return;
        }

        _noDataTextView.setVisibility(GONE);
        _listView.setVisibility(VISIBLE);

        if (_listView.getChildCount() > list.length) {
            _listView.removeViews(list.length - 1, _listView.getChildCount() - list.length);
        }

        ForLoopRunnable r = new ForLoopRunnable(list.length, new Handler()) {
            private final Discount[] _list = list;

            @Override
            public void next(int i) throws Exception {
                DiscountView v = null;
                if (i < _listView.getChildCount()) {
                    v = (DiscountView) _listView.getChildAt(i);
                } else {
                    v = new DiscountView(getContext());
                    _listView.addView(v);
                }
                Discount discount = _list[i];
                v.setDiscount(discount);
                v.setOnClickListener(_discount_onClick);
                v.setOnLongClickListener(_discount_onLongClick);
            }
        };
        postDelayed(r, new Random().nextInt(1000));
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final OnClickListener _discount_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null && _workorder.canChangeDiscounts()) {
                _listener.discountOnClick(((DiscountView) v).getDiscount());
            }
        }
    };

    private final OnLongClickListener _discount_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null && _workorder.canChangeDiscounts()) {
                _listener.discountLongClick(((DiscountView) v).getDiscount());
                return true;
            }
            return false;
        }
    };

    private final OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null && _workorder.canChangeDiscounts()) {
                _listener.addDiscount();
            }
        }
    };


    public interface Listener {
        void addDiscount();

        void discountOnClick(Discount discount);

        void discountLongClick(Discount discount);
    }
}
