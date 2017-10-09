package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 8/4/17.
 */

public class ListItemGroupWithIconView extends RelativeLayout {
    private static final String TAG = "ListItemGroupView";

    // Ui
    private TextView _titleTextView;
    private IconFontTextView _iconView;

    // Data
    private String _title;
    private String _iconText;
    private int _iconTextColor = -1;

    public ListItemGroupWithIconView(Context context) {
        super(context);
        init();
    }

    public ListItemGroupWithIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemGroupWithIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v4_list_group, this, true);

        if (isInEditMode())
            return;

        _titleTextView = findViewById(R.id.title_textview);
        _iconView = findViewById(R.id.x_textview);

        populateUi();
    }

    public void setData(String title, String iconText, int iconTextColor) {
        _title = title;
        _iconText = iconText;
        _iconTextColor = iconTextColor;

        populateUi();
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_title == null)
            _titleTextView.setText("");
        else
            _titleTextView.setText(_title);

        if (_iconText == null)
            _iconView.setText("");
        else
            _iconView.setText(_iconText);


        if (_iconTextColor!=-1)
        _iconView.setTextColor(_iconTextColor);
    }
}
