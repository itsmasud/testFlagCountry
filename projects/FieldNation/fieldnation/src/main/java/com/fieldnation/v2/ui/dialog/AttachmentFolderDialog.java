package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.AttachmentService;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.ui.AttachmentFoldersAdapter;
import com.fieldnation.v2.ui.GetFileIntent;

import java.io.File;
import java.util.List;

/**
 * Created by mc on 3/9/17.
 */

public class AttachmentFolderDialog extends FullScreenDialog {
    private static final String TAG = "AttachmentFolderDialog";

    // Dialog
    private static final String DIALOG_GET_FILE = TAG + ".getFileDialog";
    private static final String DIALOG_PHOTO_UPLOAD = TAG + ".photoUploadDialog";
    private static final String DIALOG_UPLOAD_SLOTS = TAG + ".attachmentFolderDialog";
    private static final String DIALOG_YES_NO = TAG + ".yesNoDialog";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _list;

    // Services
    private DocumentClient _docClient;
    private WorkordersWebApi _workOrderClient;

    // Data
    private AttachmentFolders folders = null;
    private AttachmentFoldersAdapter adapter = null;
    private int _workOrderId;
    private AttachmentFolder _selectedFolder = null;
    private Attachment _selectedAttachment = null;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public AttachmentFolderDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_attachment_folder, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle("Attached Files");

        _list = v.findViewById(R.id.list);
        _list.setItemAnimator(new DefaultItemAnimator());
        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        GetFileDialog.addOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_YES_NO, _yesNoDialog_onPrimary);
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        _workOrdersApi.sub();

        _docClient = new DocumentClient(_documentClient_listener);
        _docClient.connect(App.get());
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        Log.v(TAG, "show");
        super.show(payload, animate);
        folders = payload.getParcelable("folders");
        _workOrderId = payload.getInt("workOrderId");
        adapter = new AttachmentFoldersAdapter();
        adapter.setAttachments(folders);
        adapter.setListener(_attachmentFolder_listener);
        _list.setAdapter(adapter);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        if (savedState.containsKey("folders")) {
            folders = savedState.getParcelable("folders");
            adapter.setAttachments(folders);
        }
        if (savedState.containsKey("selectedFolder"))
            _selectedFolder = savedState.getParcelable("selectedFolder");
        if (savedState.containsKey("selectedAttachment"))
            _selectedAttachment = savedState.getParcelable("selectedAttachment");


        super.onRestoreDialogState(savedState);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        if (_docClient != null) _docClient.disconnect(App.get());
        _workOrdersApi.unsub();

        super.onPause();
    }

    @Override
    public void onStop() {
        GetFileDialog.removeOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_YES_NO, _yesNoDialog_onPrimary);

        super.onStop();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");
        outState.putParcelable("folders", folders);
        if (_selectedFolder != null)
            outState.putParcelable("selectedFolder", _selectedFolder);
        if (_selectedAttachment != null)
            outState.putParcelable("selectedAttachment", _selectedAttachment);

        super.onSaveDialogState(outState);
    }

    // Utils
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

    // Ui listeners
    private final AttachmentFoldersAdapter.Listener _attachmentFolder_listener = new AttachmentFoldersAdapter.Listener() {
        @Override
        public void onShowAttachment(Attachment attachment) {
            Log.v(TAG, "AttachmentFoldersAdapter.onShowAttachment");
            if (attachment.getFile().getType().equals(com.fieldnation.v2.data.model.File.TypeEnum.LINK)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(attachment.getFile().getLink()));
                getContext().startActivity(intent);
            } else if (attachment.getId() != null) {
                DocumentClient.downloadDocument(getContext(), attachment.getId(),
                        attachment.getFile().getLink(), attachment.getFile().getName(), false);
                adapter.downloadStart(attachment.getId());
            }
        }

        @Override
        public void onDeleteAttachment(Attachment attachment) {
            Log.v(TAG, "AttachmentFoldersAdapter.onDeleteAttachment");
            _selectedAttachment = attachment;
            TwoButtonDialog.show(App.get(), DIALOG_YES_NO,
                    getView().getResources().getString(R.string.delete_file),
                    getView().getResources().getString(R.string.dialog_delete_message),
                    getView().getResources().getString(R.string.btn_yes),
                    getView().getResources().getString(R.string.btn_no), true, null);
        }

        @Override
        public void onAdd(AttachmentFolder attachmentFolder) {
            Log.v(TAG, "AttachmentFoldersAdapter.onAdd");
            if (checkMedia()) {
                // start of the upload process
                _selectedFolder = attachmentFolder;
                startAppPickerDialog();
            } else {
                ToastClient.toast(App.get(), R.string.toast_external_storage_needed, Toast.LENGTH_LONG);
            }
        }
    };

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    // step 1, user clicks on a upload - done elseware?
    // step 2, user selects an app to load the file with
    private final GetFileDialog.OnFileListener _getFile_onFile = new GetFileDialog.OnFileListener() {
        @Override
        public void onFile(List<GetFileDialog.UriIntent> fileResult) {
            Log.v(TAG, "GetFileDialog.onFile");

            if (fileResult.size() == 0)
                return;

            if (fileResult.size() == 1) {
                GetFileDialog.UriIntent fui = fileResult.get(0);
                if (fui.uri != null) {
                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _selectedFolder,
                            FileUtils.getFileNameFromUri(App.get(), fui.uri), fui.uri);
                } else {
                    // TODO show a toast?
                }
                return;
            }

            for (GetFileDialog.UriIntent fui : fileResult) {
                Attachment attachment = new Attachment();
                try {
                    attachment.folderId(_selectedFolder.getId());
                    AttachmentService.addAttachment(App.get(), _workOrderId, attachment, fui.intent);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _yesNoDialog_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            WorkordersWebApi.deleteAttachment(App.get(), _workOrderId, _selectedAttachment.getFolderId(),
                    _selectedAttachment.getId(), App.get().getSpUiContext());
        }
    };

    private final DocumentClient.Listener _documentClient_listener = new DocumentClient.Listener() {
        @Override
        public void onConnected() {
            _docClient.subDocument();
        }

        @Override
        public void onDownload(long documentId, final File file, int state) {
            Log.v(TAG, "DocumentClient.onDownload");
            if (file == null || state == DocumentConstants.PARAM_STATE_START) {
                if (state == DocumentConstants.PARAM_STATE_FINISH)
                    ToastClient.toast(App.get(), R.string.could_not_download_file, Toast.LENGTH_SHORT);
                return;
            }

            adapter.downloadComplete((int) documentId);

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

                    final Intent folderIntent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(App.getUriFromFile(new File(App.get().getDownloadsFolder())), "resource/folder");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    if (folderIntent.resolveActivity(App.get().getPackageManager()) != null) {
                        ToastClient.snackbar(App.get(), "Can not open " + name + ", placed in downloads folder", "View", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityClient.startActivity(folderIntent);
                            }
                        }, Snackbar.LENGTH_LONG);
                    } else {
                        ToastClient.toast(App.get(), "Can not open " + name + ", placed in downloads folder", Toast.LENGTH_LONG);
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.toLowerCase().contains("attachment");
        }

        @Override
        public void onQueued(TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onQueued");

            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");
                adapter.uploadStart(folderId, name);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onStart(TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onStart");
            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");
                adapter.uploadProgress(folderId, name, 0);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onPaused(TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onPaused");
            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");
                adapter.uploadProgress(folderId, name, -1);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onProgress(TransactionParams transactionParams, String methodName, long pos, long size, long time) {
            Log.v(TAG, "WorkordersWebApi.onProgress");
            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                Double percent = pos * 1.0 / size;
                Log.v(TAG, "onProgress(" + folderId + "," + name + "," + (pos * 100 / size) + "," + (int) (time / percent));
                adapter.uploadProgress(folderId, name, (int) (pos * 100 / size));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            Log.v(TAG, "WorkordersWebApi.onComplete");
            if (methodName.equals("addAttachment")) {
                try {
                    JsonObject obj = new JsonObject(transactionParams.methodParams);
                    String name = obj.getString("attachment.file.name");
                    int folderId = obj.getInt("attachment.folder_id");
                    adapter.uploadStop(folderId, name);
                    WorkordersWebApi.getAttachments(App.get(), _workOrderId, false, false);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            } else if (methodName.equals("deleteAttachment")) {
                WorkordersWebApi.getAttachments(App.get(), _workOrderId, false, false);
            } else if (methodName.equals("getAttachments")) {
                folders = (AttachmentFolders) successObject;
                adapter.setAttachments(folders);
            }
        }
    };

    public static void show(Context context, String uid, int workOrderId, AttachmentFolders folders) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("folders", folders);

        Controller.show(context, uid, AttachmentFolderDialog.class, params);
    }
}