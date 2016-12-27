package com.fieldnation.ui.nav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 12/21/16.
 */

public class SavedSearchRow extends RelativeLayout {
    private static final String TAG = "SavedSearchRow";

    // Ui
    private TextView _titleTextView;
    private IconFontTextView _editTextView;

    // Data
    private SavedSearchParams _searchParams;
    private OnSearchSelectedListener _searchChangeListener;

    public SavedSearchRow(Context context) {
        super(context);
        init();
    }

    public SavedSearchRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SavedSearchRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_saved_search_row, this);

        if (isInEditMode())
            return;

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _titleTextView.setOnClickListener(_title_onClick);

        _editTextView = (IconFontTextView) findViewById(R.id.edit_textview);
        _editTextView.setOnClickListener(_edit_onClick);

        populateUi();
    }

    public void setSearchParams(SavedSearchParams searchParams) {
        _searchParams = searchParams;
        populateUi();
    }

    public void setOnSearchSelectedListener(OnSearchSelectedListener onSearchSelectedListener) {
        _searchChangeListener = onSearchSelectedListener;
    }

    private void populateUi() {
        if (_editTextView == null)
            return;

        if (_searchParams == null)
            return;


        _titleTextView.setText(_searchParams.title);

/*
        if (_searchParams.canEdit) {
            _editTextView.setVisibility(VISIBLE);
        } else {
            _editTextView.setVisibility(GONE);
        }
*/
    }

    private final View.OnClickListener _title_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_searchChangeListener != null)
                _searchChangeListener.onSearch(_searchParams);
        }
    };

    private final View.OnClickListener _edit_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public interface OnSearchSelectedListener {
        void onSearch(SavedSearchParams savedSearchParams);
    }
}

