package com.fieldnation.v2.ui.dialog;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.detail.DocumentView;
import com.fieldnation.ui.workorder.detail.PhotoReceiver;
import com.fieldnation.ui.workorder.detail.UploadSlotView;
import com.fieldnation.ui.workorder.detail.UploadedDocumentView;
import com.fieldnation.v2.data.client.AttachmentService;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.GetFileIntent;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shoaib.ahmed on 08/02/17.
 */

public class AttachmentDialog extends FullScreenDialog {
    private static final String TAG = "AttachmentDialog";

    // Dialog
    private static final String DIALOG_GET_FILE = TAG + ".getFileDialog";
    private static final String DIALOG_UPLOAD_SLOTS = TAG + ".attachmentFolderDialog";
    private static final String DIALOG_PHOTO_UPLOAD = TAG + ".photoUploadDialog";
    private static final String DIALOG_YES_NO = TAG + ".yesNoDialog";

    // State
    private static final String STATE_UPLOAD_FOLDER = "STATE_UPLOAD_FOLDER";
    private static final String STATE_WORK_ORDER_ID = "STATE_WORK_ORDER_ID";


    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _recyclerView;
    private OverScrollView _scrollView;
    private LinearLayout _reviewList;
    private LinearLayout _filesLayout;
    private TextView _noDocsTextView;
    private RefreshView _refreshView;
    private Button _actionButton;

    // Data
    private WorkOrder _workOrder;
    private int _workOrderId = 0;
    private DocumentClient _docClient;
    private PhotoClient _photoClient;
    private AttachmentFolder _folder;

    private static final Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();
    private ForLoopRunnable _filesRunnable = null;
    private ForLoopRunnable _reviewRunnable = null;


    // Services
    private WorkordersWebApi _workOrderApi;

    public AttachmentDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_v2_attachments, container, false);

        _toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        _toolbar.setTitle(view.getResources().getString(R.string.attachment_files));
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _refreshView = view.findViewById(R.id.refresh_view);
        _scrollView = view.findViewById(R.id.scroll_view);
        _reviewList = view.findViewById(R.id.review_list);
        _filesLayout = view.findViewById(R.id.files_layout);
        _noDocsTextView = view.findViewById(R.id.nodocs_textview);
        _actionButton = view.findViewById(R.id.action_button);

        checkMedia();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _scrollView.setOnOverScrollListener(_refreshView);
        _actionButton.setOnClickListener(_actionButton_onClick);
        _refreshView.setListener(_refreshView_listener);

        _workOrderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workOrderApi.connect(App.get());

        GetFileDialog.addOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        AttachmentFolderDialog.addOnFolderSelectedListener(DIALOG_UPLOAD_SLOTS, _attachmentFolderDialog_onSelected);

        TwoButtonDialog.addOnPrimaryListener(DIALOG_YES_NO, _yesNoDialog_onPrimary);
        TwoButtonDialog.addOnSecondaryListener(DIALOG_YES_NO, _yesNoDialog_onSecondary);


        _docClient = new DocumentClient(_documentClient_listener);
        _docClient.connect(App.get());

        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(App.get());

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

        if (App.get().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
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

//    @Override
//    public void update() {
//        App.get().getSpUiContext().page(WorkOrderTracker.Tab.ATTACHMENTS.name());
//        checkMedia();
//    }
//
//    @Override
//    public void setWorkOrder(WorkOrder workOrder) {
//        _workOrder = workOrder;
//        _workOrderId = workOrder.getId();
//        populateUi();
//    }

    public void setLoading(boolean isLoading) {
        if (_refreshView != null) {
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
                            DocumentView v = new DocumentView(getView().getContext());
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
                            v = new UploadSlotView(getView().getContext());
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


    @Override
    public void show(Bundle params, boolean animate) {
        _workOrder = params.getParcelable("workOrder");
        _workOrderId = _workOrder.getId();
        populateUi();
        super.show(params, animate);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (_workOrderApi != null) _workOrderApi.disconnect(App.get());
        if (_docClient != null) _docClient.disconnect(App.get());
        if (_photoClient != null) _photoClient.disconnect(App.get());

        GetFileDialog.removeOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        AttachmentFolderDialog.removeOnFolderSelectedListener(DIALOG_UPLOAD_SLOTS, _attachmentFolderDialog_onSelected);

        TwoButtonDialog.removeOnPrimaryListener(DIALOG_YES_NO, _yesNoDialog_onPrimary);
        TwoButtonDialog.removeOnSecondaryListener(DIALOG_YES_NO, _yesNoDialog_onSecondary);
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };


    private WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderApi.subWorkordersWebApi();
        }

        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return true;
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (successObject != null && successObject instanceof WorkOrder) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (_workOrder.getId().equals(workOrder.getId())) {
                    _workOrder = workOrder;
                    populateUi();
                }
            }
        }
    };


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
                        getView().getResources().getString(R.string.toast_external_storage_needed),
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
                            getView().getResources().getString(R.string.toast_external_storage_needed),
                            Toast.LENGTH_LONG);
                }
            }
        }
    };

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, false);
        }
    };


    private Attachment _document;
    private final UploadedDocumentView.Listener _uploaded_document_listener = new UploadedDocumentView.Listener() {
        @Override
        public void onDelete(UploadedDocumentView v, final Attachment document) {

            _document = document;
//            _yesNoDialog.setData(getView().getResources().getString(R.string.delete_file),
//                    getView().getResources().getString(R.string.dialog_delete_message), getView().getResources().getString(R.string.btn_yes), getView().getResources().getString(R.string.btn_no),
//                    new TwoButtonDialog.Listener() {
//                        @Override
//                        public void onPositive() {
//                            WorkordersWebApi.deleteAttachment(App.get(), _workOrderId, document.getFolderId(), documentId, App.get().getSpUiContext());
//                            setLoading(true);
//                        }
//
//                        @Override
//                        public void onNegative() {
//                        }
//
//                        @Override
//                        public void onCancel() {
//                        }
//                    });
//            _yesNoDialog.show();


            Log.v(TAG, "Asking coi");
            TwoButtonDialog.show(App.get(), DIALOG_YES_NO, getView().getResources().getString(R.string.delete_file),
                    getView().getResources().getString(R.string.dialog_delete_message),
                    getView().getResources().getString(R.string.btn_yes), getView().getResources().getString(R.string.btn_no), false, null);


        }

        @Override
        public Drawable getPhoto(UploadedDocumentView view, String url, boolean circle) {
            synchronized (_picCache) {
                String turl = url.substring(0, url.lastIndexOf('?'));
                if (_picCache.containsKey(turl) && _picCache.get(turl).get() != null) {
                    return _picCache.get(turl).get();
                } else {
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
                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _folder,
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
                    AttachmentService.addAttachment(App.get(), _workOrderId, attachment, fui.intent);
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
                    App.get().startActivity(intent);
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
        public PhotoClient getClient() {
            return _photoClient;
        }

        @Override
        public void imageDownloaded(String sourceUri, Uri localUri, boolean isCircle, boolean success) {
        }

        @Override
        public boolean doGetImage(String sourceUri, boolean isCircle) {
            return isCircle;
        }

        @Override
        public void onImageReady(String sourceUri, Uri localUri, BitmapDrawable drawable, boolean isCircle, boolean success) {
            if (drawable == null || sourceUri == null || !success)
                return;

            if (sourceUri.contains("?"))
                sourceUri = sourceUri.substring(0, sourceUri.lastIndexOf('?'));

            synchronized (_picCache) {
                _picCache.put(sourceUri, new WeakReference<>((Drawable) drawable));
            }

            Log.v(TAG, "PhotoClient.Listener.onGet");
            for (int i = 0; i < _reviewList.getChildCount(); i++) {
                View v = _reviewList.getChildAt(i);
                if (v instanceof PhotoReceiver) {
                    ((PhotoReceiver) v).setPhoto(sourceUri, drawable);
                }
            }

            for (int i = 0; i < _filesLayout.getChildCount(); i++) {
                View v = _filesLayout.getChildAt(i);
                if (v instanceof PhotoReceiver) {
                    ((PhotoReceiver) v).setPhoto(sourceUri, drawable);
                }
            }
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _yesNoDialog_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary() {
            Log.e(TAG, "onPrimary");

            if (_document == null) {
                Log.e(TAG, "_document is null");
            } else {
                Log.e(TAG, "_document is not null");
            }

            WorkordersWebApi.deleteAttachment(App.get(), _workOrderId, _document.getFolderId(), _document.getId(), App.get().getSpUiContext());
            setLoading(true);
        }
    };

    private final TwoButtonDialog.OnSecondaryListener _yesNoDialog_onSecondary = new TwoButtonDialog.OnSecondaryListener() {
        @Override
        public void onSecondary() {
            Log.e(TAG, "onSecondary");
//            _profileBounceProtect = false;
//            App.get().setNeverRemindCoi();
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {
//                    gotProfile();
//                }
//            });


        }
    };


    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable("workOrder", workOrder);

        Controller.show(context, uid, AttachmentDialog.class, params);
    }
}
