package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.FileHelper;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Hashtable;

public class DocumentView extends RelativeLayout {
    private static final String TAG = "DocumentView";

    private static final Hashtable<String, Integer> _ICFN_FILES = new Hashtable<>();

    // UI
    private IconFontTextView _fileTypeIconFont;
    private TextView _filenameTextView;

    private TextView _dateTextView;
    private TextView _byTextView;
    private TextView _usernameTextView;

    // Data
    private Workorder _workorder;
    private Document _document;

    static {
        _ICFN_FILES.put("png", R.string.icfont_file_png);
        _ICFN_FILES.put("doc", R.string.icfont_file_doc);
        _ICFN_FILES.put("docx", R.string.icfont_file_docx);
        _ICFN_FILES.put("jpg", R.string.icfont_file_jpg);
        _ICFN_FILES.put("jpeg", R.string.icfont_file_jpg);
        _ICFN_FILES.put("pdf", R.string.icfont_file_pdf);
        _ICFN_FILES.put("xls", R.string.icfont_file_xls);
    }

    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    public DocumentView(Context context) {
        super(context);
        init();
    }

    public DocumentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_document, this);

        if (isInEditMode())
            return;

        _fileTypeIconFont = (IconFontTextView) findViewById(R.id.filetype_imageview);
        _filenameTextView = (TextView) findViewById(R.id.filename_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _byTextView = (TextView) findViewById(R.id.by_textview);
        _usernameTextView = (TextView) findViewById(R.id.username_textview);

        setOnClickListener(_this_onClick);
    }

    /*-*************************-*/
    /*-			Methods			-*/
    /*-*************************-*/
    public void setData(Workorder workorder, Document document) {
        _document = document;
        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        if (_document == null)
            return;

        _filenameTextView.setText(_document.getFileName());
        try {
            String ext = _document.getFileName();
            ext = ext.substring(ext.lastIndexOf(".") + 1).trim().toLowerCase();
            if (_ICFN_FILES.containsKey(ext)) {
                _fileTypeIconFont.setText(getContext().getString(_ICFN_FILES.get(ext)));
            } else {
                _fileTypeIconFont.setText(getContext().getString(R.string.icfont_file_none));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            _fileTypeIconFont.setText(getContext().getString(R.string.icfont_file_none));
        }

        try {
            _dateTextView.setText(misc.formatDateLong(ISO8601.toCalendar(_document.getLastUpdated())));
        } catch (Exception e) {
            e.printStackTrace();
            _dateTextView.setVisibility(View.GONE);
        }
        try {
            if (!misc.isEmptyOrNull(_document.getUpdatedBy().getFullName())) {
                _usernameTextView.setText(_document.getUpdatedBy().getFullName());
                _usernameTextView.setVisibility(View.VISIBLE);
                _byTextView.setVisibility(View.VISIBLE);
            } else {
                _usernameTextView.setVisibility(View.GONE);
                _byTextView.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            _usernameTextView.setVisibility(View.GONE);
            _byTextView.setVisibility(View.GONE);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_workorder.canChangeDeliverables())
                FileHelper.viewOrDownloadFile(getContext(), _document.getFilePath(),
                        _document.getFileName(), _document.getFileType());
        }
    };
}
