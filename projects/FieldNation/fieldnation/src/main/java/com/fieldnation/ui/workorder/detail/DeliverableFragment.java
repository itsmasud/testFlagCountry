package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ClipData;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.service.data.filecache.FileCacheClient;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.v2.ui.dialog.PhotoUploadDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.UploadSlotDialog;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.ui.dialog.AppPickerDialog;
import com.fieldnation.v2.ui.dialog.AttachmentFolderDialog;
import com.fieldnation.v2.ui.AppPickerIntent;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.WorkOrder;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class DeliverableFragment extends WorkorderFragment {
    private static final String TAG = "DeliverableFragment";

    // Dialog
    private static final String DIALOG_APP_PICKER_DIALOG = TAG + ".appPickerDialog";
    private static final String DIALOG_PHOTO_UPLOAD = TAG + ".photoUploadDialog";

    // State
    private static final String STATE_UPLOAD_SLOTID = "STATE_UPLOAD_SLOTID";
    private static final String STATE_TEMP_FILE = "STATE_TEMP_FILE";

    // UI
    private OverScrollView _scrollView;
    private LinearLayout _reviewList;
    private LinearLayout _filesLayout;
    private TextView _noDocsTextView;
    private RefreshView _refreshView;
    private Button _actionButton;

    // Dialog
    private TwoButtonDialog _yesNoDialog;
    private PhotoUploadDialog _photoUploadDialog;

    // Data
    private int _uploadingSlotId = -1;
    private File _tempFile;
    private Uri _tempUri;
    private WorkOrder _workOrder;
    private DocumentClient _docClient;
    private PhotoClient _photoClient;
    private ActivityResultClient _activityResultClient;
    private FileCacheClient _fileCacheClient;

    private static final Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();
    private ForLoopRunnable _filesRunnable = null;
    private ForLoopRunnable _reviewRunnable = null;

    /*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/
    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);

        _docClient = new DocumentClient(_documentClient_listener);
        _docClient.connect(App.get());

        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(App.get());

        _activityResultClient = new ActivityResultClient(_activityResultClient_listener);
        _activityResultClient.connect(App.get());

        _fileCacheClient = new FileCacheClient(_fileCacheClient_listener);
        _fileCacheClient.connect(App.get());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TEMP_FILE)) {
                _tempFile = new File(savedInstanceState.getString(STATE_TEMP_FILE));
                Log.e(TAG, "_tempFile: " + _tempFile.getName());
            }

            if (savedInstanceState.containsKey(STATE_UPLOAD_SLOTID))
                _uploadingSlotId = savedInstanceState.getInt(STATE_UPLOAD_SLOTID);
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
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);

        _photoUploadDialog = PhotoUploadDialog.getInstance(getFragmentManager(), TAG);
        _photoUploadDialog.setListener(_photoUploadDialog_listener);

        AppPickerDialog.addOnOkListener(DIALOG_APP_PICKER_DIALOG, _appPicker_onOk);
        AttachmentFolderDialog.addOnFolderSelectedListener(DIALOG_UPLOAD_SLOTS, _attachmentFolderDialog_onSelected);
        PhotoUploadDialog.addOnOkListener(DIALOG_PHOTO_UPLOAD, _photoUploadDialog_okListener);
        PhotoUploadDialog.addOnImageClickListener(DIALOG_PHOTO_UPLOAD, _photoUploadDialog_imageClickListener);

    }

    @Override
    public void onPause() {
        AppPickerDialog.removeOnOkListener(DIALOG_APP_PICKER_DIALOG, _appPicker_onOk);
        AttachmentFolderDialog.removeOnFolderSelectedListener(DIALOG_UPLOAD_SLOTS, _attachmentFolderDialog_onSelected);

        PhotoUploadDialog.removeOnOkListener(DIALOG_PHOTO_UPLOAD, _photoUploadDialog_okListener);
        PhotoUploadDialog.removeOnImageClickListener(DIALOG_PHOTO_UPLOAD, _photoUploadDialog_imageClickListener);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_uploadingSlotId > 0)
            outState.putInt(STATE_UPLOAD_SLOTID, _uploadingSlotId);

        if (_tempFile != null)
            outState.putString(STATE_TEMP_FILE, _tempFile.getAbsolutePath());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach");
        if (_docClient != null && _docClient.isConnected())
            _docClient.disconnect(App.get());

        if (_photoClient != null && _photoClient.isConnected())
            _photoClient.disconnect(App.get());

        if (_activityResultClient != null && _activityResultClient.isConnected())
            _activityResultClient.disconnect(App.get());

        PhotoUploadDialog.removeOnOkListener(DIALOG_PHOTO_UPLOAD, _photoUploadDialog_okListener);
        PhotoUploadDialog.removeOnImageClickListener(DIALOG_PHOTO_UPLOAD, _photoUploadDialog_imageClickListener);

        if (_fileCacheClient != null && _fileCacheClient.isConnected())
            _fileCacheClient.disconnect(App.get());
        super.onDetach();
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
        AppPickerIntent intent1 = new AppPickerIntent(intent, "Get Content");

        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            AppPickerIntent intent2 = new AppPickerIntent(intent, "Take Picture");
            AppPickerDialog.show(App.get(), DIALOG_APP_PICKER_DIALOG, new AppPickerIntent[]{intent1, intent2});
        } else {
            AppPickerDialog.show(App.get(), DIALOG_APP_PICKER_DIALOG, new AppPickerIntent[]{intent1});
        }

    }

    private boolean checkMedia() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    @Override
    public void update() {
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
            if (docs != null && docs.length > 0) {
                if (_reviewList.getChildCount() != docs.length) {
                    if (_reviewRunnable != null)
                        _reviewRunnable.cancel();

                    _reviewRunnable = new ForLoopRunnable(docs.length, new Handler()) {
                        private final Attachment[] _docs = docs;
                        private final List<DocumentView> _views = new LinkedList<>();

                        @Override
                        public void next(int i) throws Exception {
                            DocumentView v = new DocumentView(getActivity());
                            Attachment doc = _docs[i];
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
        Log.v(TAG, "pop docs time " + stopwatch.finish());

        stopwatch.start();

        if (slots != null && slots.length > 0) {
            Log.v(TAG, "US count: " + slots.length);

            if (_filesRunnable != null)
                _filesRunnable.cancel();

            _filesRunnable = new ForLoopRunnable(slots.length, new Handler()) {
                private final AttachmentFolder[] _slots = slots;
                private List<View> views = new LinkedList<>();

                @Override
                public void next(int i) throws Exception {
                    if (_slots[i].getType() == AttachmentFolder.TypeEnum.SLOT) {
                        UploadSlotView v = new UploadSlotView(getActivity());
                        AttachmentFolder slot = _slots[i];
                        v.setData(_workOrder, App.get().getProfile().getUserId(), slot, _uploaded_document_listener);
                        views.add(v);
                    }
                }

                @Override
                public void finish(int count) throws Exception {
                    _filesLayout.removeAllViews();
                    for (View v : views) {
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
            if (f.getType() == AttachmentFolder.TypeEnum.SLOT)
                _actionButton.setVisibility(View.VISIBLE);
        }

        Log.v(TAG, "upload docs time " + stopwatch.finish());

        setLoading(false);
    }

    private final ActivityResultClient.Listener _activityResultClient_listener = new ActivityResultClient.ResultListener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_activityResultClient_listener.onConnected");
            _activityResultClient.subOnActivityResult(ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
            _activityResultClient.subOnActivityResult(ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
        }

        @Override
        public ActivityResultClient getClient() {
            return _activityResultClient;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.v(TAG, "_activityResultClient_listener.onActivityResult");
            try {
                Log.v(TAG, "_activityResultClient_listener.onActivityResult() resultCode= " + resultCode);

                if ((requestCode != ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES
                        && requestCode != ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES)
                        || resultCode != Activity.RESULT_OK) {
                    return;
                }

                _activityResultClient.clearOnActivityResult(ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
                _activityResultClient.clearOnActivityResult(ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);

                setLoading(true);


                if (data == null) {
                    Log.v(TAG, "Image uploading taken by camera");
                    _tempUri = null;
                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrder.getWorkOrderId(), _tempFile.getName(), _tempFile.toString());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ClipData clipData = data.getClipData();

                        if (clipData != null) {
                            int count = clipData.getItemCount();
                            Intent intent = new Intent();
                            Uri uri = null;

                            if (count == 1) {
                                _tempUri = clipData.getItemAt(0).getUri();
                                _tempFile = null;
                                PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrder.getWorkOrderId(), FileUtils.getFileNameFromUri(App.get(), data.getData()), data.getData());
                            } else {
                                for (int i = 0; i < count; ++i) {
                                    uri = clipData.getItemAt(i).getUri();
                                    if (uri != null) {
                                        Log.v(TAG, "Multiple local/ non-local files upload");
                                        WorkorderClient.uploadDeliverable(App.get(), _workOrder.getWorkOrderId(),
                                                _uploadingSlotId, intent.setData(uri));
                                    }
                                }
                            }
                        } else {
                            Log.v(TAG, "Single local/ non-local file upload");
                            _tempUri = data.getData();
                            _tempFile = null;
                            PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrder.getWorkOrderId(), FileUtils.getFileNameFromUri(App.get(), data.getData()), data.getData());
                        }
                    } else {
                        Log.v(TAG, "Android version is pre-4.3");
                        _tempUri = data.getData();
                        _tempFile = null;
                        PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrder.getWorkOrderId(), FileUtils.getFileNameFromUri(App.get(), data.getData()), data.getData());
                    }
                }

            } catch (Exception ex) {
                Log.logException(ex);
                Log.e(TAG, ex.getMessage());
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
                _uploadingSlotId = folder.getId();
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
                AttachmentFolderDialog.show(App.get(), DIALOG_UPLOAD_SLOTS,
                        slots.toArray(new AttachmentFolder[slots.size()]));
            } else if (slots.size() == 1) {
                AttachmentFolder folder = slots.get(0);
                if (checkMedia()) {
                    // start of the upload process
                    _uploadingSlotId = folder.getId();
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
            WorkordersWebApi.getWorkOrder(App.get(), _workOrder.getWorkOrderId(), false);
        }
    };

    private final UploadedDocumentView.Listener _uploaded_document_listener = new UploadedDocumentView.Listener() {
        @Override
        public void onDelete(UploadedDocumentView v, Attachment document) {

            final int documentId = document.getId();
            _yesNoDialog.setData(getString(R.string.delete_file),
                    getString(R.string.dialog_delete_message), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkorderClient.deleteDeliverable(App.get(), _workOrder.getWorkOrderId(), documentId);
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
    private final AppPickerDialog.OnOkListener _appPicker_onOk = new AppPickerDialog.OnOkListener() {
        @Override
        public void onOk(Intent pack) {

            if (pack.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                Log.v(TAG, "onClick: " + pack.toString());
                ActivityResultClient.startActivityForResult(App.get(), pack, ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
            } else {
                File temppath = new File(App.get().getTempFolder() + "/IMAGE-"
                        + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                _tempFile = temppath;
                Log.v(TAG, "onClick: " + temppath.getAbsolutePath());

                // removed because this doesn't work on my motorola
                pack.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
                ActivityResultClient.startActivityForResult(App.get(), pack, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
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
                intent.setDataAndType(Uri.fromFile(file), FileUtils.guessContentTypeFromName(file.getName()));

                if (intent.resolveActivity(App.get().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    String name = file.getName();
                    name = name.substring(name.indexOf("_") + 1);

                    Intent folderIntent = new Intent(Intent.ACTION_VIEW);
                    folderIntent.setDataAndType(Uri.fromFile(new File(App.get().getDownloadsFolder())), "resource/folder");
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

    private final PhotoUploadDialog.OnOkListener _photoUploadDialog_okListener = new PhotoUploadDialog.OnOkListener() {
        @Override
        public void onOk(int workOrderId, String filename, String photoDescription) {
            Log.e(TAG, "uploading an image using camera");
/*
TODO            if (_tempFile != null) {
                WorkorderClient.uploadDeliverable(App.get(), workOrderId, _currentTask.getSlotId(),
                        filename, _tempFile.getAbsolutePath(), photoDescription);
            } else if (_tempUri != null) {
                WorkorderClient.uploadDeliverable(App.get(), workOrderId, _currentTask.getSlotId(),
                        filename, _tempUri, photoDescription);
            }
*/
        }
    };

    private final PhotoUploadDialog.OnImageClickListener _photoUploadDialog_imageClickListener = new PhotoUploadDialog.OnImageClickListener() {
        @Override
        public void onImageClick() {
            Intent intent;
            if (_tempUri == null) {
                Log.e(TAG, "_tempUri is null");
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(_tempFile), "image/*");

            } else {
                Log.e(TAG, "_tempFile is null");
                intent = new Intent(Intent.ACTION_VIEW, _tempUri);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                if (App.get().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    App.get().startActivity(intent);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final FileCacheClient.Listener _fileCacheClient_listener = new FileCacheClient.Listener() {
        @Override
        public void onConnected() {
            _fileCacheClient.subDeliverableCache();
        }

        @Override
        public void onDeliverableCacheEnd(Uri uri, String filename) {
            Log.v(TAG, "onDeliverableCacheEnd");

            _tempUri = uri;
//            _tempFile = null;

            if (_tempFile != null) {
                _photoUploadDialog.setPhoto(MemUtils.getMemoryEfficientBitmap(_tempFile.toString(), 400));
            } else {
                _photoUploadDialog.setPhoto(MemUtils.getMemoryEfficientBitmap(filename, 400));
            }
        }
    };
}