package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.WorkOrder;

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
    private WorkOrder _workOrder;
    private Attachment _attachment;
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
        if (_attachment != null
                && _attachment.getFile() != null
                && _attachment.getFile().getThumbnail() != null
// TODO               && _attachment.getThumbNail().startsWith(url)
                ) {
            _picView.setImageDrawable(photo);
            _hasImage = true;
            updateThumb();
        }
    }

    public void setData(WorkOrder workOrder, Attachment attachment) {
        setLoading(false, R.string.uploading);
        _attachment = attachment;
        this._workOrder = workOrder;
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
        if (_attachment == null)
            return;

        _filenameTextView.setText(_attachment.getFile().getName());
        try {
            _dateTextView.setText(DateUtils.formatDateLong(_attachment.getCreated().getCalendar()));
        } catch (Exception e) {
            Log.v(TAG, e);
        }

        if (_attachment.getAuthor() != null) {
            String firstName = _attachment.getAuthor().getFirstName();
            String lastName = _attachment.getAuthor().getFirstName();
            _usernameTextView.setText(firstName == null ? "" : firstName + " " + lastName == null ? "" : lastName);
            _byTextView.setVisibility(VISIBLE);
        } else {
            _byTextView.setVisibility(GONE);
            _usernameTextView.setText("");
        }

        try {
            String ext = _attachment.getFile().getName();
            ext = ext.substring(ext.lastIndexOf(".") + 1).trim().toLowerCase();
            if (_ICFN_FILES.containsKey(ext)) {
                _fileTypeIconFont.setText(getContext().getString(_ICFN_FILES.get(ext)));
                switch (ext) {
                    case "png":
                    case "jpg":
                    case "jpeg":
                        if (_listener != null
                                && _attachment.getFile() != null
                                && !misc.isEmptyOrNull(_attachment.getFile().getThumbnail())) {
                            Drawable result = _listener.getPhoto(this, _attachment.getFile().getThumbnail(), true);
                            if (result != null) {
                                setPhoto(_attachment.getFile().getThumbnail(), result);
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
            if (_attachment == null)
                return;

            if (_attachment.getId() == null || _attachment.getId() != documentId)
                return;

            if (state == DocumentConstants.PARAM_STATE_START) {
                setDownloading(_attachment.getFile().getName());
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
            if (_attachment == null) {
                return;
            }

            if (_attachment.getFile() != null
                    && _attachment.getFile().getType() != null
                    && _attachment.getFile().getType().equals(com.fieldnation.v2.data.model.File.TypeEnum.LINK)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_attachment.getFile().getLink()));
                getContext().startActivity(intent);
            } else if (_attachment.getId() != null) {
                DocumentClient.downloadDocument(getContext(), _attachment.getId(),
                        _attachment.getFile().getLink(), _attachment.getFile().getName(), false);
            }
        }
    };

    public interface Listener {
        Drawable getPhoto(DocumentView view, String url, boolean circle);
    }
}
