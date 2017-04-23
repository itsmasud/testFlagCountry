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
import android.widget.Toast;

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

public class UploadedDocumentView extends RelativeLayout implements PhotoReceiver {
    private static final String TAG = "UploadedDocumentView";

    private static final Hashtable<String, Integer> _ICFN_FILES = new Hashtable<>();

    // Progress Constants
    public static final int PROGRESS_QUEUED = -100;
    public static final int PROGRESS_PAUSED = -200;

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
    private Attachment _doc;
    private DocumentClient _docClient;

    private boolean _isLoading = false;
    private Listener _listener;

    private long _profileId;
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
    public UploadedDocumentView(Context context) {
        super(context);
        init();
    }

    public UploadedDocumentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UploadedDocumentView(Context context, AttributeSet attrs, int defStyle) {
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
        if (_doc != null && _doc.getFile().getThumbnail() != null && _doc.getFile().getThumbnail().startsWith(url)) {
            _picView.setImageDrawable(photo);
            _hasImage = true;
            updateThumb();
        }
    }

    public void setData(WorkOrder workOrder, long profileId, Attachment deliverable) {
        setLoading(false, R.string.uploading);
        _doc = deliverable;
        _profileId = profileId;
        _workOrder = workOrder;
        populateUi();
    }

    public void setProgress(Integer progress) {
        if (_progressBar == null)
            return;

        if (progress == null) {
            _progressBar.setIndeterminate(true);
            return;
        }

        if (progress == PROGRESS_PAUSED) {
            _statusTextView.setText("Paused");
            _progressBar.setIndeterminate(true);
            return;
        }

        if (progress == PROGRESS_QUEUED) {
            _statusTextView.setText("Queued");
            _progressBar.setIndeterminate(true);
            return;
        }


        _statusTextView.setText("Uploading...");
        _progressBar.setIndeterminate(false);
        _progressBar.setMax(100);
        _progressBar.setProgress(progress);
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
            _dateTextView.setVisibility(View.GONE);
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

    public void setUploading(String filename) {
        setLoading(true, R.string.uploading);
        _doc = null;
        _workOrder = null;
        _filenameTextView.setText(filename);
    }

    private void populateUi() {
        if (_doc == null)
            return;

        _filenameTextView.setText(_doc.getFile().getName());
        try {
            _dateTextView.setText(DateUtils.formatDateLong(_doc.getCreated().getCalendar()));
        } catch (Exception e) {
            Log.v(TAG, e);
        }

        if (_doc.getAuthor() != null) {
            String firstName = _doc.getAuthor().getFirstName();
            String lastName = _doc.getAuthor().getLastName();
            _usernameTextView.setText((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName));
        }


        if (_doc.getActionsSet().contains(Attachment.ActionsEnum.DELETE))
            setOnLongClickListener(_delete_onClick);

        try {
            String ext = _doc.getFile().getName();
            ext = ext.substring(ext.lastIndexOf(".") + 1).trim().toLowerCase();
            if (_ICFN_FILES.containsKey(ext)) {
                _fileTypeIconFont.setText(getContext().getString(_ICFN_FILES.get(ext)));
                switch (ext) {
                    case "png":
                    case "jpg":
                    case "jpeg":
                        if (_listener != null &&
                                _doc.getFile() != null
                                && !misc.isEmptyOrNull(_doc.getFile().getThumbnail())) {
                            Drawable result = _listener.getPhoto(this, _doc.getFile().getThumbnail(), true);
                            if (result != null) {
                                setPhoto(_doc.getFile().getThumbnail(), result);
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
            if (_doc == null)
                return;

            if (_doc.getId() != documentId)
                return;

            if (state == DocumentConstants.PARAM_STATE_START) {
                setDownloading(_doc.getFile().getName());
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
            if (!App.get().isFreeSpaceAvailable()) {
                Toast.makeText(getContext(), getResources().getString(R.string.toast_no_disk_space), Toast.LENGTH_LONG).show();
                return;
            }

            if (_isLoading)
                return;
            setDownloading(_doc.getFile().getName());
            DocumentClient.downloadDocument(getContext(), _doc.getId(), _doc.getFile().getLink(), _doc.getFile().getName(), false);
        }
    };

    private final View.OnLongClickListener _delete_onClick = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            if (_doc == null || _doc.getAuthor() == null && _doc.getAuthor().getId() == null)
                return false;

            if (_profileId == _doc.getAuthor().getId()
                    && !_isLoading && _doc.getActionsSet().contains(Attachment.ActionsEnum.DELETE)) {

                if (_listener != null)
                    _listener.onDelete(UploadedDocumentView.this, _doc);

                return true;
            } else {
                return false;
            }
        }
    };

    public interface Listener {
        void onDelete(UploadedDocumentView v, Attachment document);

        Drawable getPhoto(UploadedDocumentView view, String url, boolean circle);

    }
}
