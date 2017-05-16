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

import com.fieldnation.R;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.model.PayModifier;
import com.fieldnation.v2.data.model.PayModifiers;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.Random;

/**
 * Created by Michael Carver on 6/5/2015.
 */
public class DiscountListLayout extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "DiscountListLayout";

    // UI
    private TextView _noDataTextView;
    private LinearLayout _listView;
    private Button _addButton;

    // Data
    private Listener _listener;
    private WorkOrder _workOrder;

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

        setVisibility(GONE);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_addButton == null)
            return;

        if (_workOrder == null)
            return;

        if (_workOrder.getPay() == null)
            return;


        if (_workOrder.getStatus() == null
                || _workOrder.getStatus().getId() == 2
                || _workOrder.getStatus().getId() == 9
                || _workOrder.getPay() == null
                || _workOrder.getPay().getDiscounts() == null) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        if (_workOrder.getPay() != null
                && _workOrder.getPay().getDiscounts().getActionsSet() != null
                && _workOrder.getPay().getDiscounts().getActionsSet().contains(PayModifiers.ActionsEnum.ADD)) {
            _addButton.setVisibility(VISIBLE);
        } else {
            _addButton.setVisibility(GONE);
        }

        final PayModifier[] list = _workOrder.getPay().getDiscounts().getResults();
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
            private final PayModifier[] _list = list;

            @Override
            public void next(int i) throws Exception {
                DiscountView v = null;
                if (i < _listView.getChildCount()) {
                    v = (DiscountView) _listView.getChildAt(i);
                } else {
                    v = new DiscountView(getContext());
                    _listView.addView(v);
                }
                PayModifier discount = _list[i];
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

//TODO            if (_listener != null && _workorder.canChangeDiscounts()) {
//                _listener.discountOnClick(((DiscountView) v).getDiscount());
//            }

        }
    };

    private final OnLongClickListener _discount_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            PayModifier payModifier = ((DiscountView) v).getDiscount();
            if (_listener != null
                    && payModifier != null
                    && payModifier.getActionsSet() != null
                    && payModifier.getActionsSet().contains(PayModifier.ActionsEnum.DELETE)) {
                _listener.discountLongClick(payModifier);
                return true;
            }
            return false;
        }
    };

    private final OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null
                    && _workOrder.getPay() != null
                    && _workOrder.getPay().getDiscounts() != null
                    && _workOrder.getPay().getDiscounts().getActionsSet() != null
                    && _workOrder.getPay().getDiscounts().getActionsSet().contains(PayModifiers.ActionsEnum.ADD)) {
                _listener.addDiscount();
            }
        }
    };


    public interface Listener {
        void addDiscount();

        void discountOnClick(PayModifier discount);

        void discountLongClick(PayModifier discount);
    }
}
