package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.DateUtils;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.util.Hashtable;

public class DocumentView extends RelativeLayout implements PhotoReceiver {
    private static final String TAG = "DocumentView";

    private static final Hashtable<String, Integer> _ICFN_FILES = new Hashtable<>();

    // UI
    private IconFontTextView _fileTypeIconFont;
    private ImageView _picView;
    private TextView _filenameTextView;
    private TextView _dateTextView;
    private TextView _byTextView;
    private TextView _usernameTextView;
    private ProgressBar _progressBar;
    private TextView _statusTextView;

    // Data
    private Workorder _workorder;
    private Document _document;
    private DocumentClient _docClient;

    private boolean _isLoading = false;
    private Listener _listener;

    private boolean _hasImage = false;

    static {
        _ICFN_FILES.put("png", R.string.icon_file_png);
        _ICFN_FILES.put("doc", R.string.icon_file_doc);
        _ICFN_FILES.put("docx", R.string.icon_file_docx);
        _ICFN_FILES.put("jpg", R.string.icon_file_jpg);
        _ICFN_FILES.put("jpeg", R.string.icon_file_jpg);
        _ICFN_FILES.put("pdf", R.string.icon_file_pdf);
        _ICFN_FILES.put("xls", R.string.icon_file_xls);
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
        _picView = (ImageView) findViewById(R.id.pic_view);
        _filenameTextView = (TextView) findViewById(R.id.filename_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _byTextView = (TextView) findViewById(R.id.by_textview);
        _usernameTextView = (TextView) findViewById(R.id.username_textview);

        _progressBar = (ProgressBar) findViewById(R.id.progressBar);
        _statusTextView = (TextView) findViewById(R.id.status_textview);

        _docClient = new DocumentClient(_docClient_listener);
        _docClient.connect(App.get());

        setOnClickListener(_this_onClick);
        setLoading(false, 0);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_docClient != null && _docClient.isConnected()) {
            _docClient.disconnect(App.get());
            _docClient = null;
        }
        super.onDetachedFromWindow();
    }

    /*-*************************-*/
    /*-			Methods			-*/
    /*-*************************-*/
    @Override
    public void setPhoto(String url, Drawable photo) {
        Log.v(TAG, "setPhoto");
        if (_document != null && _document.getThumbNail() != null && _document.getThumbNail().startsWith(url)) {
            _picView.setImageDrawable(photo);
            _hasImage = true;
            updateThumb();
        }
    }

    public void setData(Workorder workorder, Document document) {
        setLoading(false, R.string.uploading);
        _document = document;
        _workorder = workorder;
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setLoading(boolean isloading, int messageResId) {
        _isLoading = isloading;
        if (isloading) {
            _progressBar.setVisibility(View.VISIBLE);
            _statusTextView.setVisibility(View.VISIBLE);
            _dateTextView.setVisibility(View.GONE);
            _byTextView.setVisibility(GONE);
            _usernameTextView.setVisibility(GONE);
            _statusTextView.setText(messageResId);
            _fileTypeIconFont.setVisibility(VISIBLE);
            _fileTypeIconFont.setText(getContext().getString(R.string.icon_file_generic));
            _hasImage = false;
            updateThumb();
        } else {
            _progressBar.setVisibility(View.GONE);
            _statusTextView.setVisibility(View.GONE);
            _dateTextView.setVisibility(View.VISIBLE);
            _byTextView.setVisibility(VISIBLE);
            _usernameTextView.setVisibility(VISIBLE);
            _dateTextView.setVisibility(View.VISIBLE);
            updateThumb();
            populateUi();

        }
    }

    public void setDownloading(String filename) {
        _isLoading = true;
        _progressBar.setVisibility(View.VISIBLE);
        _statusTextView.setVisibility(View.VISIBLE);
        _dateTextView.setVisibility(View.GONE);
        _byTextView.setVisibility(GONE);
        _usernameTextView.setVisibility(GONE);
        _dateTextView.setVisibility(View.GONE);
        _statusTextView.setText(R.string.downloading);
        _filenameTextView.setText(filename);
    }

    private void populateUi() {
        if (_document == null)
            return;

        _filenameTextView.setText(_document.getFileName());
        try {
            _dateTextView.setText(DateUtils.formatDateLong(ISO8601.toCalendar(_document.getLastUpdated())));
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        _usernameTextView.setText(_document.getUpdatedBy().getFullName());

        try {
            String ext = _document.getFileName();
            ext = ext.substring(ext.lastIndexOf(".") + 1).trim().toLowerCase();
            if (_ICFN_FILES.containsKey(ext)) {
                _fileTypeIconFont.setText(getContext().getString(_ICFN_FILES.get(ext)));
                switch (ext) {
                    case "png":
                    case "jpg":
                    case "jpeg":
                        if (_listener != null && !misc.isEmptyOrNull(_document.getThumbNail())) {
                            Drawable result = _listener.getPhoto(this, _document.getThumbNail(), true);
                            if (result != null) {
                                setPhoto(_document.getThumbNail(), result);
                            } else {
                                _hasImage = false;
                            }
                        } else {
                            _hasImage = false;
                        }
                        break;
                    default:
                        _hasImage = false;
                        break;
                }
            } else {
                _hasImage = false;
                _fileTypeIconFont.setText(getContext().getString(R.string.icon_file_generic));
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        updateThumb();
        setClickable(_workorder.canViewDeliverables());
    }

    private void updateThumb() {
        if (_hasImage) {
            _picView.setVisibility(VISIBLE);
            _fileTypeIconFont.setVisibility(GONE);
        } else {
            _picView.setVisibility(GONE);
            _fileTypeIconFont.setVisibility(VISIBLE);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final DocumentClient.Listener _docClient_listener = new DocumentClient.Listener() {
        @Override
        public void onConnected() {
            _docClient.subDocument();
        }

        @Override
        public void onDownload(long documentId, File file, int state) {
            if (_document == null)
                return;

            if (_document.getDocumentId() == null || _document.getDocumentId() != documentId)
                return;

            if (state == DocumentConstants.PARAM_STATE_START) {
                setDownloading(_document.getFileName());
            } else {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setLoading(false, R.string.downloading);
                    }
                }, 1000);
            }
        }
    };


    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_document == null || _document.getDocumentId() == null) {
                return;
            }
            DocumentClient.downloadDocument(getContext(), _document.getDocumentId(),
                    _document.getFilePath(), _document.getFileName(), false);
        }
    };

    public interface Listener {
        Drawable getPhoto(DocumentView view, String url, boolean circle);
    }
}
