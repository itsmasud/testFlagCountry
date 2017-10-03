package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;

/**
 * Created by mc on 9/29/17.
 */

public class TaskSummaryRow extends RelativeLayout {
    private static final String TAG = "TaskSummaryRow";

    // Ui
    private TextView _titleTextView;
    private TextView _countTextView;

    // Data
    private String _title;
    private String _count;
    private int _countBg;

    public TaskSummaryRow(Context context) {
        super(context);
        init();
    }

    public TaskSummaryRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskSummaryRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_task_summary_row, this, true);

        if (isInEditMode())
            return;

        _titleTextView = findViewById(R.id.title_textview);
        _countTextView = findViewById(R.id.count_textview);
    }

    public void setTitle(String title) {
        _title = title;
        populateUi();
    }

    public void setCount(String count) {
        _count = count;
        populateUi();
    }

    public void setCountBg(int bg) {
        _countBg = bg;
        populateUi();
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (misc.isEmptyOrNull(_title)) {
            _titleTextView.setText("");
        } else {
            _titleTextView.setText(_title);
        }

        if (misc.isEmptyOrNull(_count)) {
            _countTextView.setText("");
        } else {
            _countTextView.setText(_count);
        }

        _countTextView.setBackgroundResource(_countBg);

    }
}
