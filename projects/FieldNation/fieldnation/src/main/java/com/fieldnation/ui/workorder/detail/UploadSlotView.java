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
import com.fieldnation.R;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.WorkOrder;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class UploadSlotView extends RelativeLayout implements PhotoReceiver {
    private final String TAG = UniqueTag.makeTag("UploadSlotView");

    // Ui
    private TextView _titleTextView;
    private LinearLayout _docsList;
    private TextView _noDocsTextView;
    private ProgressBar _loadingProgressBar;

    // Data
    private WorkOrder _workOrder;
    private AttachmentFolder _slot;
    private final HashSet<String> _uploadingFiles = new HashSet<>();
    private final Hashtable<String, Integer> _uploadingProgress = new Hashtable<>();
    private UploadedDocumentView.Listener _docListener;
    private long _profileId;
    private WorkordersWebApi _workOrderApi;
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

        _workOrderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workOrderApi.connect(App.get());

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_workOrderApi != null && _workOrderApi.isConnected())
            _workOrderApi.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    public void setData(WorkOrder workOrder, long profileId, AttachmentFolder slot, UploadedDocumentView.Listener listener) {
        _workOrder = workOrder;
        _slot = slot;
        _docListener = listener;
        _profileId = profileId;

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

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_workOrder == null)
            return;

        _titleTextView.setText(_slot.getName().toUpperCase());

        final List<Object> files = new LinkedList<>();

        Collections.addAll(files, _uploadingFiles.toArray());

        Attachment[] d = _slot.getResults();
        if (d != null && d.length > 0) {
            Arrays.sort(d, new DateTimeComparator());
            Collections.addAll(files, d);
        }

        if (files.size() > 0) {
            Log.v(TAG, "docs: " + files.size() + " " + _docsList.getChildCount());

            if (_docsRunnable != null)
                _docsRunnable.cancel();

            if (_docsList.getChildCount() == 0) {
                _loadingProgressBar.setVisibility(VISIBLE);
            }
            _docsRunnable = new ForLoopRunnable(files.size(), new Handler(), 50) {
                private final List<Object> _docs = files;
                private final List<UploadedDocumentView> _views = new LinkedList<>();
                private final String[] uploadingFileNames = _uploadingFiles.toArray(new String[_uploadingFiles.size()]);

                @Override
                public void next(int i) throws Exception {
                    UploadedDocumentView v = null;
                    if (i < _docsList.getChildCount()) {
                        v = (UploadedDocumentView) _docsList.getChildAt(i);
                    } else {
                        v = new UploadedDocumentView(getContext());
                        //_docsList.addView(v);
                    }
                    if (_docs.get(i) instanceof Attachment) {
                        Attachment doc = (Attachment) _docs.get(i);
                        v.setListener(_docListener);
                        v.setData(_workOrder, _profileId, doc);
                    } else {
                        v.setUploading(uploadingFileNames[i]);
                        v.setListener(null);
                        if (_uploadingProgress.containsKey(uploadingFileNames[i])) {
                            v.setProgress(_uploadingProgress.get(uploadingFileNames[i]));
                        } else {
                            v.setProgress(null);
                        }
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
            _docsList.removeAllViews();
        }

        if (files.size() == 0 && _uploadingFiles.size() == 0) {
            _noDocsTextView.setVisibility(VISIBLE);
            _loadingProgressBar.setVisibility(GONE);
        } else {
            _noDocsTextView.setVisibility(GONE);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderApi.subWorkordersWebApi();
        }

        @Override
        public void onStart(TransactionParams transactionParams, String methodName) {
            if (!methodName.equals("addAttachment"))
                return;
            Log.v(TAG, "onStart");
            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                if (folderId == _slot.getId()) {
                    _uploadingFiles.add(name);
                    populateUi();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onProgress(TransactionParams transactionParams, String methodName, long pos, long size, long time) {
            if (!methodName.equals("addAttachment"))
                return;

            Log.v(TAG, "onProgress");
            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                if (folderId == _slot.getId()) {
                    Double percent = pos * 1.0 / size;
                    Log.v(TAG, "onProgress(" + folderId + "," + name + "," + (pos * 100 / size) + "," + (int) (time / percent));
                    _uploadingFiles.add(name);
                    _uploadingProgress.put(name, (int) (pos * 100 / size));
                    populateUi();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (!methodName.equals("addAttachment"))
                return;

            Log.v(TAG, "onComplete");
            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                if (folderId == _slot.getId()) {
                    _uploadingFiles.remove(name);
                    _uploadingProgress.remove(name);
                    populateUi();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private static class DateTimeComparator implements Comparator<Attachment> {
        @Override
        public int compare(Attachment lhs, Attachment rhs) {
            try {
                long l = lhs.getCreated().getUtcLong();
                long r = rhs.getCreated().getUtcLong();

                if (l > r)
                    return -1;
                else if (l < r)
                    return 1;
                else
                    return 0;
            } catch (ParseException e) {
                Log.v("UploadSlotView", e);
            }
            return 0;
        }
    }
}
