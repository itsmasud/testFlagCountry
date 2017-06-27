package com.fieldnation.ui.workorder.detail;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.v2.data.client.AttachmentService;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.GetFileIntent;
import com.fieldnation.v2.ui.dialog.AttachmentFolderDialog;
import com.fieldnation.v2.ui.dialog.GetFileDialog;
import com.fieldnation.v2.ui.dialog.PhotoUploadDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class DeliverableFragment extends WorkorderFragment {
    private static final String TAG = "DeliverableFragment";

    // Dialog
    private static final String DIALOG_GET_FILE = TAG + ".getFileDialog";
    private static final String DIALOG_UPLOAD_SLOTS = TAG + ".attachmentFolderDialog";
    private static final String DIALOG_PHOTO_UPLOAD = TAG + ".photoUploadDialog";

    // State
    private static final String STATE_UPLOAD_FOLDER = "STATE_UPLOAD_FOLDER";

    // UI
    private OverScrollView _scrollView;
    private LinearLayout _reviewList;
    private LinearLayout _filesLayout;
    private TextView _noDocsTextView;
    private RefreshView _refreshView;
    private Button _actionButton;

    // Dialog
    private TwoButtonDialog _yesNoDialog;

    // Data
    private WorkOrder _workOrder;
    private DocumentClient _docClient;
    private PhotoClient _photoClient;
    private AttachmentFolder _folder;

    private static final Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();
    private ForLoopRunnable _filesRunnable = null;
    private ForLoopRunnable _reviewRunnable = null;

    /*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_UPLOAD_FOLDER))
                _folder = savedInstanceState.getParcelable(STATE_UPLOAD_FOLDER);
        }

        return inflater.inflate(R.layout.fragment_workorder_deliverables, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.v(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _scrollView = (OverScrollView) view.findViewById(R.id.scroll_view);
        _scrollView.setOnOverScrollListener(_refreshView);

        _reviewList = (LinearLayout) view.findViewById(R.id.review_list);

        _filesLayout = (LinearLayout) view.findViewById(R.id.files_layout);

        _noDocsTextView = (TextView) view.findViewById(R.id.nodocs_textview);

        _actionButton = (Button) view.findViewById(R.id.action_button);
        _actionButton.setOnClickListener(_actionButton_onClick);

        checkMedia();
        populateUi();
    }

    @Override
    public void onStart() {
        super.onStart();
        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);

        GetFileDialog.addOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        AttachmentFolderDialog.addOnFolderSelectedListener(DIALOG_UPLOAD_SLOTS, _attachmentFolderDialog_onSelected);

        _docClient = new DocumentClient(_documentClient_listener);
        _docClient.connect(App.get());

        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(App.get());
    }

    @Override
    public void onStop() {
        if (_docClient != null) _docClient.disconnect(App.get());
        if (_photoClient != null) _photoClient.disconnect(App.get());

        GetFileDialog.removeOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        AttachmentFolderDialog.removeOnFolderSelectedListener(DIALOG_UPLOAD_SLOTS, _attachmentFolderDialog_onSelected);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_folder != null)
            outState.putParcelable(STATE_UPLOAD_FOLDER, _folder);

        super.onSaveInstanceState(outState);
    }

    /*-*******************************************************************************-*/
    /*-*******************************************************************************-*/
    /*-*******************************************************************************-*/
    /*-*******************************************************************************-*/
    private void startAppPickerDialog() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        GetFileIntent intent1 = new GetFileIntent(intent, "Get Content");

        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            GetFileIntent intent2 = new GetFileIntent(intent, "Take Picture");
            GetFileDialog.show(App.get(), DIALOG_GET_FILE, new GetFileIntent[]{intent1, intent2});
        } else {
            GetFileDialog.show(App.get(), DIALOG_GET_FILE, new GetFileIntent[]{intent1});
        }
    }

    private boolean checkMedia() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    @Override
    public void update() {
        App.get().getSpUiContext().page(WorkOrderTracker.Tab.ATTACHMENTS.name());
        checkMedia();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (_refreshView != null && getActivity() != null) {
            if (isLoading) {
                _refreshView.startRefreshing();
            } else {
                _refreshView.refreshComplete();
            }
        }
    }

    private void populateUi() {
        misc.hideKeyboard(getView());
        if (_actionButton == null)
            return;

        if (_workOrder == null)
            return;

        if (App.get().getProfile() == null)
            return;

        if (getActivity() == null)
            return;

        if (_workOrder.getAttachments() == null) {
            _noDocsTextView.setVisibility(View.VISIBLE);
            setLoading(false);
            return;
        }

        Stopwatch stopwatch = new Stopwatch(true);

        final AttachmentFolder[] slots = _workOrder.getAttachments().getResults();
        AttachmentFolder reviewSlot = null;
        for (AttachmentFolder ob : slots) {
            if (ob.getType().equals(AttachmentFolder.TypeEnum.DOCUMENT)) {
                reviewSlot = ob;
            }
        }

        if (reviewSlot != null) {
            final Attachment[] docs = reviewSlot.getResults();

            if (docs.length > 0) {
                if (_reviewList.getChildCount() != docs.length) {
                    if (_reviewRunnable != null)
                        _reviewRunnable.cancel();

                    _reviewRunnable = new ForLoopRunnable(docs.length, new Handler()) {
                        private final Attachment[] _docs = docs;
                        private final List<DocumentView> _views = new LinkedList<>();

                        @Override
                        public void next(int i) throws Exception {
                            Attachment doc = _docs[i];
                            DocumentView v = new DocumentView(getActivity());
                            v.setListener(_document_listener);
                            v.setData(_workOrder, doc);
                            _views.add(v);
                        }

                        @Override
                        public void finish(int count) throws Exception {
                            _reviewList.removeAllViews();
                            for (DocumentView v : _views) {
                                _reviewList.addView(v);
                            }
                        }
                    };
                    _reviewList.postDelayed(_reviewRunnable, 100);
                    _noDocsTextView.setVisibility(View.GONE);
                }
            } else {
                _reviewList.removeAllViews();
                _noDocsTextView.setVisibility(View.VISIBLE);
            }
        } else {
            _reviewList.removeAllViews();
            _noDocsTextView.setVisibility(View.VISIBLE);
        }
        //Log.v(TAG, "pop docs time " + stopwatch.finish());

        //stopwatch.start();

        if (slots.length > 0) {
            //Log.v(TAG, "US count: " + slots.length);

            if (_filesRunnable != null)
                _filesRunnable.cancel();

            final List<View> fViews = new LinkedList<>();
            for (int i = 0; i < _filesLayout.getChildCount(); i++) {
                fViews.add(_filesLayout.getChildAt(i));
            }

            _filesRunnable = new ForLoopRunnable(slots.length, new Handler(), 50) {
                private final AttachmentFolder[] _slots = slots;
                private final List<View> views = fViews;
                private final List<View> buffer = new LinkedList<>();

                @Override
                public void next(int i) throws Exception {
                    if (_slots[i].getType() == AttachmentFolder.TypeEnum.SLOT) {
                        UploadSlotView v = null;
                        if (views.size() > 0)
                            v = (UploadSlotView) views.remove(0);
                        else {
                            v = new UploadSlotView(getActivity());
                        }

                        AttachmentFolder slot = _slots[i];
                        v.setData(_workOrder, App.get().getProfile().getUserId(), slot, _uploaded_document_listener);
                        buffer.add(v);
                    }
                }

                @Override
                public void finish(int count) throws Exception {
                    //Log.v(TAG, "finish");
                    _filesLayout.removeAllViews();
                    for (View v : buffer) {
                        _filesLayout.addView(v);
                    }
                }
            };
            _filesLayout.postDelayed(_filesRunnable, 100);
        } else {
            _filesLayout.removeAllViews();
        }

        _actionButton.setVisibility(View.GONE);
        for (AttachmentFolder f : slots) {
            if (f.getType() == AttachmentFolder.TypeEnum.SLOT && f.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD))
                _actionButton.setVisibility(View.VISIBLE);
        }

        //Log.v(TAG, "upload docs time " + stopwatch.finish());

        setLoading(false);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final AttachmentFolderDialog.OnFolderSelectedListener _attachmentFolderDialog_onSelected = new AttachmentFolderDialog.OnFolderSelectedListener() {
        @Override
        public void onFolderSelected(AttachmentFolder folder) {
            if (checkMedia()) {
                // start of the upload process
                _folder = folder;
                startAppPickerDialog();
            } else {
                ToastClient.toast(App.get(),
                        getString(R.string.toast_external_storage_needed),
                        Toast.LENGTH_LONG);
            }
        }
    };
    private final View.OnClickListener _actionButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AttachmentFolder[] folders = _workOrder.getAttachments().getResults();

            List<AttachmentFolder> slots = new LinkedList<>();
            for (AttachmentFolder folder : folders) {
                if (folder.getType() == AttachmentFolder.TypeEnum.SLOT)
                    slots.add(folder);
            }

            if (slots.size() > 1) {
                AttachmentFolderDialog.show(App.get(), DIALOG_UPLOAD_SLOTS, slots.toArray(new AttachmentFolder[slots.size()]));
            } else if (slots.size() == 1) {
                AttachmentFolder folder = slots.get(0);
                if (checkMedia()) {
                    // start of the upload process
                    _folder = folder;
                    startAppPickerDialog();
                } else {
                    ToastClient.toast(App.get(),
                            getString(R.string.toast_external_storage_needed),
                            Toast.LENGTH_LONG);
                }
            }
        }
    };

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            WorkordersWebApi.getWorkOrder(App.get(), _workOrder.getId(), false, false);
        }
    };

    private final UploadedDocumentView.Listener _uploaded_document_listener = new UploadedDocumentView.Listener() {
        @Override
        public void onDelete(UploadedDocumentView v, final Attachment document) {

            final int documentId = document.getId();
            _yesNoDialog.setData(getString(R.string.delete_file),
                    getString(R.string.dialog_delete_message), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkordersWebApi.deleteAttachment(App.get(), _workOrder.getId(), document.getFolderId(), documentId, App.get().getSpUiContext());
                            setLoading(true);
                        }

                        @Override
                        public void onNegative() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            _yesNoDialog.show();

        }

        @Override
        public Drawable getPhoto(UploadedDocumentView view, String url, boolean circle) {
            synchronized (_picCache) {
                String turl = url.substring(0, url.lastIndexOf('?'));
                if (_picCache.containsKey(turl) && _picCache.get(turl).get() != null) {
                    return _picCache.get(turl).get();
                } else {
                    _photoClient.subGet(url, circle, false);
                    PhotoClient.get(App.get(), url, circle, false);
                }
                return null;
            }
        }
    };

    private final DocumentView.Listener _document_listener = new DocumentView.Listener() {
        @Override
        public Drawable getPhoto(DocumentView view, String url, boolean circle) {
            synchronized (_picCache) {
                String turl = url.substring(0, url.lastIndexOf('?'));
                if (_picCache.containsKey(turl) && _picCache.get(turl).get() != null) {
                    return _picCache.get(turl).get();
                } else {
                    _photoClient.subGet(url, circle, false);
                    PhotoClient.get(App.get(), url, circle, false);
                }
                return null;
            }
        }
    };

    // step 1, user clicks on a upload - done elseware?
    // step 2, user selects an app to load the file with
    private final GetFileDialog.OnFileListener _getFile_onFile = new GetFileDialog.OnFileListener() {
        @Override
        public void onFile(List<GetFileDialog.UriIntent> fileResult) {
            if (fileResult.size() == 0)
                return;

            if (fileResult.size() == 1) {
                GetFileDialog.UriIntent fui = fileResult.get(0);
                if (fui.uri != null) {
                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrder.getId(), _folder,
                            FileUtils.getFileNameFromUri(App.get(), fui.uri), fui.uri);
                } else {
                    // TODO show a toast?
                }
                return;
            }

            for (GetFileDialog.UriIntent fui : fileResult) {
                Attachment attachment = new Attachment();
                try {
                    attachment.folderId(_folder.getId());
                    AttachmentService.addAttachment(App.get(), _workOrder.getId(), attachment, fui.intent);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private final DocumentClient.Listener _documentClient_listener = new DocumentClient.Listener() {
        @Override
        public void onConnected() {
            _docClient.subDocument();
        }

        @Override
        public void onDownload(long documentId, final File file, int state) {
            if (file == null || state == DocumentConstants.PARAM_STATE_START) {
                if (state == DocumentConstants.PARAM_STATE_FINISH)
                    ToastClient.toast(App.get(), R.string.could_not_download_file, Toast.LENGTH_SHORT);
                return;
            }

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(App.getUriFromFile(file),
                        FileUtils.guessContentTypeFromName(file.getName()));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (intent.resolveActivity(App.get().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    String name = file.getName();
                    name = name.substring(name.indexOf("_") + 1);

                    Intent folderIntent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(App.getUriFromFile(new File(App.get().getDownloadsFolder())), "resource/folder");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    if (folderIntent.resolveActivity(App.get().getPackageManager()) != null) {
                        PendingIntent pendingIntent = PendingIntent.getActivity(App.get(), App.secureRandom.nextInt(), folderIntent, 0);
                        ToastClient.snackbar(App.get(), "Can not open " + name + ", placed in downloads folder", "View", pendingIntent, Snackbar.LENGTH_LONG);
                    } else {
                        ToastClient.toast(App.get(), "Can not open " + name + ", placed in downloads folder", Toast.LENGTH_LONG);
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final PhotoClient.Listener _photoClient_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(String url, BitmapDrawable drawable, boolean isCircle, boolean failed) {
            if (drawable == null || url == null || failed)
                return;

            if (url.contains("?"))
                url = url.substring(0, url.lastIndexOf('?'));

            synchronized (_picCache) {
                _picCache.put(url, new WeakReference<>((Drawable) drawable));
            }

            Log.v(TAG, "PhotoClient.Listener.onGet");
            for (int i = 0; i < _reviewList.getChildCount(); i++) {
                View v = _reviewList.getChildAt(i);
                if (v instanceof PhotoReceiver) {
                    ((PhotoReceiver) v).setPhoto(url, drawable);
                }
            }

            for (int i = 0; i < _filesLayout.getChildCount(); i++) {
                View v = _filesLayout.getChildAt(i);
                if (v instanceof PhotoReceiver) {
                    ((PhotoReceiver) v).setPhoto(url, drawable);
                }
            }
        }
    };
}