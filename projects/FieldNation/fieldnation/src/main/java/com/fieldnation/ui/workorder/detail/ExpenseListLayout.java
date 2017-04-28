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
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.Expenses;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 6/5/2015.
 */
public class ExpenseListLayout extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "ExpenseListLayout";

    // UI
    private TextView _noDataTextView;
    private LinearLayout _listView;
    private Button _addButton;

    // Data
    private Listener _listener;
    private WorkOrder _workOrder;
    private ForLoopRunnable _forLoop;

    public ExpenseListLayout(Context context) {
        super(context);
        init();
    }

    public ExpenseListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpenseListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_expense_layout, this);

        if (isInEditMode())
            return;

        _noDataTextView = (TextView) findViewById(R.id.nodata_textview);
        _listView = (LinearLayout) findViewById(R.id.listview);

        _addButton = (Button) findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);
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

        if (_workOrder.getStatus().getId() == 2 || _workOrder.getStatus().getId() == 9) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }


        if (_workOrder.getPay().getExpenses() != null
                && _workOrder.getPay().getExpenses().getActionsSet().contains(Expenses.ActionsEnum.ADD)) {
            _addButton.setVisibility(VISIBLE);
        } else {
            _addButton.setVisibility(GONE);
        }


        final Expense[] list = _workOrder.getPay().getExpenses().getResults();
        if (list == null || list.length == 0) {
            _noDataTextView.setVisibility(VISIBLE);
            _listView.setVisibility(GONE);
            return;
        }

        _noDataTextView.setVisibility(GONE);
        _listView.setVisibility(VISIBLE);

        if (_forLoop != null) {
            _forLoop.cancel();
            _forLoop = null;
        }

        _forLoop = new ForLoopRunnable(list.length, new Handler()) {
            private final Expense[] _list = list;
            private List<View> _views = new LinkedList<>();

            @Override
            public void next(int i) throws Exception {
                ExpenseView v = new ExpenseView(getContext());
                _views.add(v);
                Expense expense = _list[i];
                v.setData(_workOrder, expense);
                v.setOnClickListener(_expense_onClick);
                v.setOnLongClickListener(_expense_onLongClick);
            }

            @Override
            public void finish(int count) throws Exception {
                _listView.removeAllViews();
                for (View v : _views) {
                    _listView.addView(v);
                }
            }
        };
        postDelayed(_forLoop, 100);
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _expense_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null
                    && _workOrder.getPay() != null
                    && _workOrder.getPay().getExpenses() != null
                    && _workOrder.getPay().getExpenses().getActionsSet().contains(Expenses.ActionsEnum.ADD)) {
                _listener.expenseOnClick(((ExpenseView) v).getExpense());
            }
        }
    };

    private final View.OnLongClickListener _expense_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null
                    && _workOrder.getPay() != null
                    && _workOrder.getPay().getExpenses() != null
                    && _workOrder.getPay().getExpenses().getActionsSet().contains(Expenses.ActionsEnum.ADD)) {
                _listener.expenseLongClick(((ExpenseView) v).getExpense());
                return true;
            }
            return false;
        }
    };

    private final View.OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null
                    && _workOrder.getPay() != null
                    && _workOrder.getPay().getExpenses() != null
                    && _workOrder.getPay().getExpenses().getActionsSet().contains(Expenses.ActionsEnum.ADD)) {
                _listener.addExpense();
            }
        }
    };


    public interface Listener {
        void addExpense();

        void expenseOnClick(Expense expense);

        void expenseLongClick(Expense expense);
    }
}
