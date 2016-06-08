package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
import com.fieldnation.utils.ISO8601;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UploadSlotView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("UploadSlotView");

    // Ui
    private TextView _titleTextView;
    private LinearLayout _docsList;
    private LinearLayout _uploadList;
    private TextView _noDocsTextView;

    // Data
    private Workorder _workorder;
    private UploadSlot _slot;
    private Listener _listener;
    private Set<String> _uploadingFiles = new HashSet<>();
    private UploadedDocumentView.Listener _docListener;
    private long _profileId;
    private WorkorderClient _workorderClient;

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
        _uploadList = (LinearLayout) findViewById(R.id.upload_list);
        _noDocsTextView = (TextView) findViewById(R.id.nodocs_textview);

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

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setData(Workorder workorder, long profileId, UploadSlot slot, UploadedDocumentView.Listener listener) {
        _workorder = workorder;
        _slot = slot;
        _docListener = listener;
        _profileId = profileId;

        subscribe();
        populateUi();
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_workorder == null)
            return;

        _titleTextView.setText(_slot.getSlotName().toUpperCase());

        final UploadedDocument[] docs = _slot.getUploadedDocuments();
        if (docs != null && docs.length > 0) {
            Log.v(TAG, "docs: " + docs.length + " " + _docsList.getChildCount());

            Arrays.sort(docs, new Comparator<UploadedDocument>() {
                @Override
                public int compare(UploadedDocument lhs, UploadedDocument rhs) {
                    try {
                        long l = ISO8601.toUtc(lhs.getUploadedTime());
                        long r = ISO8601.toUtc(rhs.getUploadedTime());

                        if (l > r)
                            return -1;
                        else if (l < r)
                            return 1;
                        else
                            return 0;
                    } catch (ParseException e) {
                        Log.v(TAG, e);
                    }
                    return 0;
                }
            });

            ForLoopRunnable r = new ForLoopRunnable(docs.length, new Handler()) {
                private UploadedDocument[] _docs = docs;
                private List<UploadedDocumentView> _views = new LinkedList<>();

                @Override
                public void next(int i) throws Exception {
                    UploadedDocumentView v = new UploadedDocumentView(getContext());
                    UploadedDocument doc = _docs[i];
                    v.setData(_workorder, _profileId, doc);
                    v.setListener(_docListener);
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
            postDelayed(r, 100);
        } else {
            _docsList.removeAllViews();
        }

        // uploading placeholders
        if (_uploadingFiles.size() > 0) {
            Log.v(TAG, "UF: " + _uploadingFiles.size() + " " + _uploadList.getChildCount());
            _uploadList.setVisibility(View.VISIBLE);

            ForLoopRunnable r = new ForLoopRunnable(_uploadingFiles.size(), new Handler()) {
                private List<UploadedDocumentView> _view = new LinkedList<>();

                @Override
                public void next(int i) throws Exception {
                    Log.v(TAG, "UF: new view " + i);
                    UploadedDocumentView v = new UploadedDocumentView(getContext());
                    v.setUploading((String) (_uploadingFiles.toArray()[i]));
                    v.setListener(null);
                    _view.add(v);
                }

                @Override
                public void finish(int count) throws Exception {
                    _uploadList.removeAllViews();
                    for (UploadedDocumentView v : _view) {
                        _uploadList.addView(v);
                    }
                }
            };
            postDelayed(r, 100);
        } else {
            _uploadList.removeAllViews();
            _uploadList.setVisibility(View.GONE);
        }


//        if (_slot.getMaxFiles() > 0 && docs.length >= _slot.getMaxFiles() || !_workorder.canChangeDeliverables()) {
////            _uploadTextView.setVisibility(GONE);
//        } else {
////            _uploadTextView.setVisibility(VISIBLE);
//        }

        if (docs.length == 0 && _uploadingFiles.size() == 0) {
            _noDocsTextView.setVisibility(VISIBLE);
        } else {
            _noDocsTextView.setVisibility(GONE);
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
                }

                populateUi();
            }
        }
    };

    public interface Listener {
        void onUploadClick(UploadSlotView view, UploadSlot slot);
    }
}
