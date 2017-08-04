package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by mc on 8/4/17.
 */

public class ListItemGroupView extends RelativeLayout {
    private static final String TAG = "ListItemGroupView";

    // Ui
    private TextView _titleTextView;

    // Data
    private String _title;

    public ListItemGroupView(Context context) {
        super(context);
        init();
    }

    public ListItemGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v3_list_group, this, true);

        if (isInEditMode())
            return;

        _titleTextView = findViewById(R.id.title_textview);

        populateUi();
    }

    public void setTitle(String title) {
        _title = title;

        populateUi();
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_title == null)
            _titleTextView.setText("");
        else
            _titleTextView.setText(_title);
    }
}
