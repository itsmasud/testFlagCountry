package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.Problem;

/**
 * Created by mc on 5/25/17.
 */

public class ProblemRowView extends RelativeLayout {
    private static final String TAG = "ProblemRowView";

    // Ui
    private TextView _titleTextView; // problem type
    private TextView _detailTextView; // comments

    // Data
    private Problem _problem;

    public ProblemRowView(Context context) {
        super(context);
        init();
    }

    public ProblemRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProblemRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_problem_row_view, this);

        if (isInEditMode())
            return;

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _detailTextView = (TextView) findViewById(R.id.detail_textview);

        populateUi();
    }

    public void setProblem(Problem problem) {
        _problem = problem;

        populateUi();
    }

    public Problem getProblem() {
        return _problem;
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_problem == null)
            return;

        _titleTextView.setText(_problem.getType().getName());
        _detailTextView.setText(_problem.getComments());
    }
}
