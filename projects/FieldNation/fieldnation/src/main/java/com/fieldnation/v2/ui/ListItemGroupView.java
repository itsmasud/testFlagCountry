package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 8/4/17.
 */

public class ListItemGroupView extends RelativeLayout {
    private static final String TAG = "ListItemGroupView";

    // Ui
    private TextView _titleTextView;
    private IconFontTextView _iconView;

    // Data
    private String _title;
    private int _titleGragity = -1;
    private int _titleTextColor = -1;
    private String _iconText = null;
    private int _iconTextColor = -1;
    private View _bottomDivider;

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
        _iconView = findViewById(R.id.x_textview);
        _bottomDivider = findViewById(R.id.bottom_divider);

        populateUi();
    }

    public void setTitle(String title) {
        _title = title;

        populateUi();
    }

    public void setTitle(String title, int titleGravity, int titleTextColor) {
        _title = title;
        _titleGragity = titleGravity;
        _titleTextColor = titleTextColor;

        populateUi();
    }

    public void setIcon(String iconText, int iconTextColor) {
        _iconText = iconText;
        _iconTextColor = iconTextColor;

        populateUi();
    }

    public void setDividerVisiblity(int isVisible){
        _bottomDivider.setVisibility(isVisible);
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_title == null)
            _titleTextView.setText("");
        else
            _titleTextView.setText(_title);

        if (_titleGragity != -1) {
            _titleTextView.setGravity(_titleGragity);
        }

        if (_titleTextColor != -1) {
            _titleTextView.setTextColor(_titleTextColor);
        }

        if (misc.isEmptyOrNull(_iconText))
            _iconView.setVisibility(GONE);
        else {
            _iconView.setVisibility(VISIBLE);
            _iconView.setText(_iconText);
        }


        if (_iconTextColor != -1)
            _iconView.setTextColor(_iconTextColor);

    }
}
