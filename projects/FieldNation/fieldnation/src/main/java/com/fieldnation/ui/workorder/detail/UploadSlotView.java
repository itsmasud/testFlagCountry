package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.ForLoopRunnable;
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
    private final String TAG = UniqueTag.makeTag("ui.workorder.detail.UploadSlotView");

    // Ui
    private TextView _titleTextView;
    private LinearLayout _docsList;
    private LinearLayout _uploadList;
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
        _uploadList = (LinearLayout) findViewById(R.id.upload_list);
        _uploadTextView = (TextView) findViewById(R.id.upload_textview);
        _uploadTextView.setOnClickListener(_upload_onClick);

        Topics.subscribeFileUpload(getContext(), TAG, _uploadReceiver);

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setData(Workorder workorder, long profileId, UploadSlot slot, UploadedDocumentView.Listener listener) {
        _workorder = workorder;
        _slot = slot;
        _docListener = listener;
        _profileId = profileId;

        _uploadUrl = _workorder.getWorkorderId() + "/deliverables/" + _slot.getSlotId();

        populateUi();
    }

    private void populateUi() {
        if (_uploadTextView == null)
            return;

        if (_workorder == null)
            return;

        _titleTextView.setText(_slot.getSlotName());

        final UploadedDocument[] docs = _slot.getUploadedDocuments();
        if (docs != null && docs.length > 0) {
            Log.v(TAG, "docs: " + docs.length + " " + _docsList.getChildCount());
            ForLoopRunnable r = new ForLoopRunnable(docs.length, new Handler()) {
                private UploadedDocument[] _docs = docs;

                @Override
                public void next(int i) throws Exception {
                    UploadedDocumentView v = (UploadedDocumentView) _docsList.getChildAt(i);
                    if (v == null) {
                        v = new UploadedDocumentView(getContext());
                        _docsList.addView(v);
                    }
                    UploadedDocument doc = _docs[i];
                    v.setData(_workorder, _profileId, doc);
                    v.setListener(_docListener);
                }

                @Override
                public void finish(final int count) throws Exception {
                    Log.v(TAG, "finish " + count + "/"
                            + _docsList.getChildCount() + ": "
                            + (count - 1) + "/" + (_docsList.getChildCount() - count));
                    if (_docsList.getChildCount() > count) {
                        _docsList.removeViews(count - 1, _docsList.getChildCount() - count);
                    }
                }
            };
            post(r);
        } else {
            _docsList.removeAllViews();
        }

        if (_uploadingFiles.size() > 0) {
            Log.v(TAG, "UF: " + _uploadingFiles.size() + " " + _uploadList.getChildCount());
            _uploadList.setVisibility(View.VISIBLE);
            ForLoopRunnable r = new ForLoopRunnable(_uploadingFiles.size(), new Handler()) {
                @Override
                public void next(int i) throws Exception {
                    UploadedDocumentView v = null;
                    if (i < _uploadList.getChildCount()) {
                        v = (UploadedDocumentView) _uploadList.getChildAt(i);
                    } else {
                        v = new UploadedDocumentView(getContext());
                        _uploadList.addView(v);
                    }
                    v.setUploading(_uploadingFiles.get(i));
                    v.setListener(null);
                }

                @Override
                public void finish(int count) throws Exception {
                    Log.v(TAG, "UF finish " + count + "/" + _uploadList.getChildCount() + ": " + (count - 1) + "/" + (_uploadList.getChildCount() - count));
                    if (_uploadList.getChildCount() > count) {
                        _uploadList.removeViews(count - 1, _uploadList.getChildCount() - count);
                    }
                }
            };
            post(r);
        } else {
            _uploadList.removeAllViews();
            _uploadList.setVisibility(View.GONE);
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
                Log.v(TAG, "onStart");
                _uploadingFiles.add(filename);
                populateUi();
            }
        }

        @Override
        public void onFinish(String url, String filename) {
            if (url.contains(_uploadUrl)) {
                Log.v(TAG, "onFinish");
                _uploadingFiles.remove(filename);
                //populateUi();
            }
        }

        @Override
        public void onError(String url, String filename, String message) {
            if (url.contains(_uploadUrl)) {
                Log.v(TAG, "onError");
                _uploadingFiles.remove(filename);
                //populateUi();
            }
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
