package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.topics.FileUploadTopicReceiver;
import com.fieldnation.topics.Topics;

import java.util.LinkedList;
import java.util.List;

public class UploadSlotView extends RelativeLayout {
    private String TAG = UniqueTag.makeTag("ui.workorder.detail.UploadSlotView");

    // Ui
    private TextView _titleTextView;
    private LinearLayout _docsList;
    private TextView _uploadTextView;

    // Data
    private Workorder _workorder;
    private UploadSlot _slot;
    private Listener _listener;
    private String _uploadUrl;
    private List<String> _uploadingFiles;
    private UploadedDocumentView.Listener _docListener;
    private long _profileId;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public UploadSlotView(Context context) {
        super(context);
        init();
    }

    public UploadSlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UploadSlotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_uploadslot, this);

        if (isInEditMode())
            return;

        _uploadingFiles = new LinkedList<>();

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _docsList = (LinearLayout) findViewById(R.id.docs_list);
        _uploadTextView = (TextView) findViewById(R.id.upload_textview);
        _uploadTextView.setOnClickListener(_upload_onClick);

        populateUi();
    }

    public int getUploadSlotId() {
        if (_slot != null)
            return _slot.getSlotId();
        return -1;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setData(Workorder workorder, long profileId, UploadSlot slot, UploadedDocumentView.Listener listener) {
        _workorder = workorder;
        _slot = slot;
        _docListener = listener;
        _profileId = profileId;

        populateUi();
    }

    private void populateUi() {
        if (_uploadTextView == null)
            return;

        if (_workorder == null)
            return;

        _uploadUrl = _workorder.getWorkorderId() + "/deliverables/" + _slot.getSlotId();
        Topics.subscribeFileUpload(getContext(), TAG, _uploadReceiver);

        UploadedDocument[] docs = _slot.getUploadedDocuments();
        _titleTextView.setText(_slot.getSlotName());

        _docsList.removeAllViews();
        for (int i = 0; i < docs.length; i++) {
            UploadedDocument doc = docs[i];
            UploadedDocumentView v = new UploadedDocumentView(getContext());
            v.setData(_workorder, _profileId, doc);
            v.setListener(_docListener);

            _docsList.addView(v);
        }

        for (int i = 0; i < _uploadingFiles.size(); i++) {
            UploadedDocumentView v = new UploadedDocumentView(getContext());
            v.setUploading(_uploadingFiles.get(i));
            _docsList.addView(v);
        }

        if (_slot.getMaxFiles() > 0 && docs.length >= _slot.getMaxFiles() || !_workorder.canChangeDeliverables()) {
            _uploadTextView.setVisibility(GONE);
        } else {
            _uploadTextView.setVisibility(VISIBLE);
        }

        if (docs.length == 0 && !_workorder.canChangeDeliverables()) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private FileUploadTopicReceiver _uploadReceiver = new FileUploadTopicReceiver(new Handler()) {
        @Override
        public void onStart(String url, String filename) {
            if (url.contains(_uploadUrl)) {
                _uploadingFiles.add(filename);
                populateUi();
            }
        }

        @Override
        public void onFinish(String url, String filename) {
            if (url.contains(_uploadUrl)) {
                _uploadingFiles.remove(filename);
                populateUi();
            }
        }

        @Override
        public void onError(String url, String filename, String message) {
        }
    };

    private View.OnClickListener _upload_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onUploadClick(UploadSlotView.this, _slot);
        }
    };

    public interface Listener {
        public void onUploadClick(UploadSlotView view, UploadSlot slot);
    }
}
