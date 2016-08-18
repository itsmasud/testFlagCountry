package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.ForLoopRunnable;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UploadSlotView extends RelativeLayout implements PhotoReceiver {
    private final String TAG = UniqueTag.makeTag("UploadSlotView");

    // Ui
    private TextView _titleTextView;
    private LinearLayout _docsList;
    private TextView _noDocsTextView;
    private ProgressBar _loadingProgressBar;

    // Data
    private Workorder _workorder;
    private UploadSlot _slot;
    private final Set<String> _uploadingFiles = new HashSet<>();
    private UploadedDocumentView.Listener _docListener;
    private long _profileId;
    private WorkorderClient _workorderClient;
    private ForLoopRunnable _docsRunnable = null;

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

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _docsList = (LinearLayout) findViewById(R.id.docs_list);
        _noDocsTextView = (TextView) findViewById(R.id.nodocs_textview);
        _loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());
        super.onDetachedFromWindow();
    }

    public void setData(Workorder workorder, long profileId, UploadSlot slot, UploadedDocumentView.Listener listener) {
        _workorder = workorder;
        _slot = slot;
        _docListener = listener;
        _profileId = profileId;

        subscribe();
        populateUi();
    }

    @Override
    public void setPhoto(String url, Drawable photo) {
        Log.v(TAG, "setPhoto");
        for (int i = 0; i < _docsList.getChildCount(); i++) {
            View v = _docsList.getChildAt(i);
            if (v instanceof PhotoReceiver) {
                ((PhotoReceiver) v).setPhoto(url, photo);
            }
        }
    }

    private void populateDocs() {
        final List<Object> files = new LinkedList<>();

        Collections.addAll(files, _uploadingFiles.toArray());

        UploadedDocument[] d = _slot.getUploadedDocuments();
        if (d != null && d.length > 0) {
            Arrays.sort(d, new UploadedDocument.DateTimeComparator());
            Collections.addAll(files, d);
        }

        if (files.size() > 0) {
            Log.v(TAG, "docs: " + files.size() + " " + _docsList.getChildCount());

            if (_docsRunnable != null)
                _docsRunnable.cancel();

            if (_docsList.getChildCount() == 0) {
                _loadingProgressBar.setVisibility(VISIBLE);
                _docsRunnable = new ForLoopRunnable(files.size(), new Handler()) {
                    private final List<Object> _docs = files;
                    private final List<UploadedDocumentView> _views = new LinkedList<>();

                    @Override
                    public void next(int i) throws Exception {
                        UploadedDocumentView v = new UploadedDocumentView(getContext());
                        if (_docs.get(i) instanceof UploadedDocument) {
                            UploadedDocument doc = (UploadedDocument) _docs.get(i);
                            v.setListener(_docListener);
                            v.setData(_workorder, _profileId, doc);
                        } else {
                            v.setUploading((String) (_uploadingFiles.toArray()[i]));
                            v.setListener(null);
                        }
                        _views.add(v);
                    }

                    @Override
                    public void finish(int count) throws Exception {
                        _loadingProgressBar.setVisibility(GONE);
                        _docsList.removeAllViews();
                        for (UploadedDocumentView v : _views) {
                            _docsList.addView(v);
                        }
                    }
                };
                post(_docsRunnable);
            } else {
                _docsRunnable = new ForLoopRunnable(files.size(), new Handler()) {
                    private final List<Object> _docs = files;
                    private final List<UploadedDocumentView> _views = new LinkedList<>();

                    @Override
                    public void next(int i) throws Exception {
                        UploadedDocumentView v = null;
                        if (i < _docsList.getChildCount()) {
                            v = (UploadedDocumentView) _docsList.getChildAt(i);
                        } else {
                            v = new UploadedDocumentView(getContext());
                            //_docsList.addView(v);
                        }
                        if (_docs.get(i) instanceof UploadedDocument) {
                            UploadedDocument doc = (UploadedDocument) _docs.get(i);
                            v.setListener(_docListener);
                            v.setData(_workorder, _profileId, doc);
                        } else {
                            v.setUploading((String) (_uploadingFiles.toArray()[i]));
                            v.setListener(null);
                        }
                        _views.add(v);
                    }

                    @Override
                    public void finish(int count) throws Exception {
                        _docsList.removeAllViews();
                        for (UploadedDocumentView v : _views) {
                            _docsList.addView(v);
                        }
                    }
                };
                post(_docsRunnable);
            }
        } else {
            _docsList.removeAllViews();
        }

        if (files.size() == 0 && _uploadingFiles.size() == 0) {
            _noDocsTextView.setVisibility(VISIBLE);
            _loadingProgressBar.setVisibility(GONE);
        } else {
            _noDocsTextView.setVisibility(GONE);
        }

//        if (files.size() == 0 && !_workorder.canChangeDeliverables()) {
//            setVisibility(View.GONE);
//        } else {
//            setVisibility(View.VISIBLE);
//        }
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_workorder == null)
            return;

        _titleTextView.setText(_slot.getSlotName().toUpperCase());

        populateDocs();
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private void subscribe() {
        if (_workorder == null)
            return;

        if (_slot == null)
            return;

        if (!_workorderClient.isConnected())
            return;

        Log.v(TAG, "subscribe, " + _workorder.getWorkorderId() + ", " + _slot.getSlotId());
        _workorderClient.subDeliverableUpload(_workorder.getWorkorderId(), _slot.getSlotId());
    }

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected()");
            subscribe();
        }

        @Override
        public void onUploadDeliverable(long workorderId, long slotId, String filename, boolean isComplete, boolean failed) {
            Log.v(TAG, "onUploadDeliverable(" + workorderId + "," + slotId + "," + filename + "," + isComplete + "," + failed);
            if (slotId == _slot.getSlotId()) {
                if (failed || isComplete) {
                    _uploadingFiles.remove(filename);
                } else {
                    _uploadingFiles.add(filename);
                    populateUi();
                }
            }
        }
    };
}
