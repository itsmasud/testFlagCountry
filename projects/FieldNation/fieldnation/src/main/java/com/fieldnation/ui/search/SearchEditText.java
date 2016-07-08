package com.fieldnation.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by Michael on 7/8/2016.
 */
public class SearchEditText extends RelativeLayout {
    private static final String TAG = "SearchEditText";

    // UI
    private IconFontTextView _searchIconFont;
    private EditText _searchTermEditText;
    private IconFontTextView _micIconFont;

    public SearchEditText(Context context) {
        super(context);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_edit_text, this);

        if (isInEditMode())
            return;

        _searchIconFont = (IconFontTextView) findViewById(R.id.left_textview);
        _searchTermEditText = (EditText) findViewById(R.id.search_edittext);
        _micIconFont = (IconFontTextView) findViewById(R.id.right_textview);
    }
}
