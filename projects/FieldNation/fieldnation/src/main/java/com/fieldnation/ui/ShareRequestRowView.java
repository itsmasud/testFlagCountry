package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.UploadingDocument;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

/**
 * Created by shoaib.ahemd on Sept 10, 2015.
 */
public class ShareRequestRowView extends RelativeLayout {
    private static final String TAG = "ShareRequestRowView";

    // Ui
    private TextView _filenameTextView;
    private IconFontTextView _fileIcon;


    // Data
    private String _text;
    UploadingDocument _document;

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

        _filenameTextView = (TextView) findViewById(R.id.name_textview);
        _fileIcon = (IconFontTextView) findViewById(R.id.icon_view);
        populateUi();
    }


    /*-*************************-*/
    /*-			Methods			-*/
    /*-*************************-*/
    public void setData(UploadingDocument document) {
        _document = document;
        populateUi();
    }


    private void populateUi() {
        if (_document == null)
            return;

        _filenameTextView.setText(_document.getFileName());

    }
}
