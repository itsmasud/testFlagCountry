package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by shoaib.ahemd on Sept 10, 2015.
 */
public class ShareRequestRowView extends RelativeLayout {
    private static final String TAG = "ShareRequestRowView";

    // Ui
    private TextView _textView;

    // Data
    private String _text;

    public ShareRequestRowView(Context context) {
        super(context);
        init();
    }

    public ShareRequestRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShareRequestRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_uploading_documents_row, this);

        if (isInEditMode())
            return;

        _textView = (TextView) findViewById(R.id.textview);
        populateUi();
    }

    public void setText(String text) {
        _text = text;
        populateUi();
    }

    private void populateUi() {
        if (_text == null)
            return;

        if (_textView == null)
            return;

        _textView.setText(_text);
    }
}
