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

import com.fieldnation.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;

import java.util.Random;

/**
 * Created by Michael Carver on 6/5/2015.
 */
public class ExpenseListLayout extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "ExpenseListLayout";

    // UI
    private TextView _noDataTextView;
    private LinearLayout _listView;
    private Button _addButton;

    // Data
    private Listener _listener;
    private Workorder _workorder;

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

        if (_workorder.getStatus().getWorkorderStatus() == WorkorderStatus.AVAILABLE) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        final Expense[] list = _workorder.getAdditionalExpenses();

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
            private Expense[] _list = list;

            @Override
            public void next(int i) throws Exception {
                ExpenseView v = null;
                if (i < _listView.getChildCount()) {
                    v = (ExpenseView) _listView.getChildAt(i);
                } else {
                    v = new ExpenseView(getContext());
                    _listView.addView(v);
                }
                Expense expense = _list[i];
                v.setData(_workorder, expense);
                v.setOnClickListener(_expense_onClick);
                v.setOnLongClickListener(_expense_onLongClick);
            }
        };
        postDelayed(r, new Random().nextInt(1000));

        if (_workorder.canChangeExpenses()) {
            _addButton.setVisibility(VISIBLE);
        } else {
            _addButton.setVisibility(GONE);
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _expense_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null && _workorder.canChangeExpenses()) {
                _listener.expenseOnClick(((ExpenseView) v).getExpense());
            }
        }
    };

    private final View.OnLongClickListener _expense_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null && _workorder.canChangeExpenses()) {
                _listener.expenseLongClick(((ExpenseView) v).getExpense());
                return true;
            }
            return false;
        }
    };

    private final View.OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null && _workorder.canChangeExpenses()) {
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
