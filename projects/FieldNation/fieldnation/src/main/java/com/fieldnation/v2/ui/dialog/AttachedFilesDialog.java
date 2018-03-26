package com.fieldnation.v2.ui.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.analytics.trackers.AttachmentTracker;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.service.tracker.TrackerEnum;
import com.fieldnation.service.tracker.UploadTrackerClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.AttachmentHelper;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.ui.AttachedFilesAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by mc on 3/9/17.
 */

public class AttachedFilesDialog extends FullScreenDialog {
    private static final String TAG = "AttachedFilesDialog";

    // Dialog
    private static final String DIALOG_GET_FILE = TAG + ".getFileDialog";
    private static final String DIALOG_PHOTO_UPLOAD = TAG + ".photoUploadDialog";
    private static final String DIALOG_UPLOAD_SLOTS = TAG + ".attachmentFolderDialog";
    private static final String DIALOG_YES_NO = TAG + ".yesNoDialog";
    private static final String DIALOG_YES_NO_FAILED = TAG + ".yesNoDialogFailed";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _list;

    // Data
    private AttachmentFolders folders = null;
    private int _selectedFolderId;
    private AttachedFilesAdapter adapter = null;
    private int _selectedAttachmentId;
    private int _selectedAttachmentFolderId;
    private int _workOrderId;
    private long _selectedTransactionId;
    private String _myUUID;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public AttachedFilesDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_toolbar_recycle, container, false);

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
        TwoButtonDialog.addOnPrimaryListener(DIALOG_YES_NO_FAILED, _yesNoDialog_onPrimaryFailed);
        PhotoUploadDialog.addOnOkListener(DIALOG_PHOTO_UPLOAD, _photoDialog_onUpload);

        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransReceiver, new IntentFilter(
                WebTransaction.BROADCASE_ON_CHANGE));
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        _workOrdersApi.sub();
        _appClient.subNetworkState();
        _documentClient.sub();
    }

    private final BroadcastReceiver _webTransReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "_webTransReceiver.onReceive");
            if (adapter != null) {
                populateUi();
            }
        }
    };

    @Override
    public void show(Bundle payload, boolean animate) {
        Log.v(TAG, "show");
        super.show(payload, animate);
        _workOrderId = payload.getInt("workOrderId");
        _myUUID = payload.getString("uuid");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                .addContext(new SpTracingContext(new UUIDGroup(null, _myUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Files Dialog"))
                .build());

        WorkordersWebApi.getAttachments(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
        AppMessagingClient.setLoading(true);
    }

    private void populateUi() {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            if (_list == null)
                return;

            if (folders == null)
                return;

            if (adapter == null) {
                adapter = new AttachedFilesAdapter();
                adapter.setListener(_attachmentFolder_listener);
                _list.setAdapter(adapter);
            }

            Stopwatch sw = new Stopwatch(true);
            adapter.setAttachments(folders);
            Log.v(TAG, "setAttachments time: " + sw.finishAndRestart());
            WebTransaction.cleanZombies(folders);
            Log.v(TAG, "cleanZombies time: " + sw.finishAndRestart());
            adapter.setFailedUploads(WebTransaction.getZombies());
            Log.v(TAG, "setFailedUploads time: " + sw.finishAndRestart());
            adapter.setPausedUploads(WebTransaction.getPaused("%addAttachmentByWorkOrderAndFolder%/workorders/" + _workOrderId + "/attachments/%"));
            Log.v(TAG, "setPausedUploads time: " + sw.finishAndRestart());
        } finally {
            Log.v(TAG, "populateUi time: " + stopwatch.finish());
        }
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");

        if (savedState.containsKey("selectedFolderId"))
            _selectedFolderId = savedState.getInt("selectedFolderId");
        if (savedState.containsKey("selectedAttachmentId"))
            _selectedAttachmentId = savedState.getInt("selectedAttachmentId");
        if (savedState.containsKey("selectedAttachmentFolderId"))
            _selectedAttachmentFolderId = savedState.getInt("selectedAttachmentFolderId");
        if (savedState.containsKey("selectedTransactionId"))
            _selectedTransactionId = savedState.getLong("selectedTransactionId");

        super.onRestoreDialogState(savedState);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        LocalBroadcastManager.getInstance(App.get()).unregisterReceiver(_webTransReceiver);

        _documentClient.unsub();
        _workOrdersApi.unsub();
        _appClient.unsubNetworkState();

        super.onPause();
    }

    @Override
    public void onStop() {
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                .addContext(new SpTracingContext(new UUIDGroup(null, _myUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Files Dialog"))
                .build());

        GetFileDialog.removeOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_YES_NO, _yesNoDialog_onPrimary);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_YES_NO_FAILED, _yesNoDialog_onPrimaryFailed);
        PhotoUploadDialog.removeOnOkListener(DIALOG_PHOTO_UPLOAD, _photoDialog_onUpload);

        super.onStop();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");

        if (_selectedFolderId != 0)
            outState.putInt("selectedFolderId", _selectedFolderId);
        if (_selectedAttachmentId != 0)
            outState.putInt("selectedAttachmentId", _selectedAttachmentId);
        if (_selectedAttachmentFolderId != 0)
            outState.putInt("selectedAttachmentFolderId", _selectedAttachmentFolderId);
        if (_selectedTransactionId != 0)
            outState.putLong("selectedTransactionId", _selectedTransactionId);

        super.onSaveDialogState(outState);
    }

    // Utils
    private void startAppPickerDialog() {
        GetFileDialog.show(App.get(), DIALOG_GET_FILE, _myUUID);
    }

    private boolean checkMedia() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private Attachment findAttachmentById(int attachmentId) {
        AttachmentFolder[] attachmentFolders = folders.getResults();
        for (AttachmentFolder attachmentFolder : attachmentFolders) {
            if (attachmentFolder.getResults().length > 0
                    || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)
                    || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.DELETE)
                    || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.EDIT)) {

                Attachment[] attachments = attachmentFolder.getResults();
                for (Attachment attachment : attachments) {
                    if (attachment.getId() == attachmentId) return attachment;
                }
            }
        }
        return null;
    }

    // Ui listeners
    private final AttachedFilesAdapter.Listener _attachmentFolder_listener = new AttachedFilesAdapter.Listener() {
        @Override
        public void onShowAttachment(Attachment attachment) {
            Log.v(TAG, "AttachedFilesAdapter.onShowAttachment");
            if (attachment.getFile().getType().equals(com.fieldnation.v2.data.model.File.TypeEnum.LINK)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(attachment.getFile().getLink()));
                getContext().startActivity(intent);
            } else if (attachment.getId() != null) {
                DocumentClient.downloadDocument(getContext(), attachment.getId(),
                        attachment.getFile().getLink(), attachment.getFile().getName(), false);
                if (adapter != null)
                    adapter.downloadStart(attachment.getId());
            }
        }

        @Override
        public void onDeleteAttachment(Attachment attachment) {
            Log.v(TAG, "AttachedFilesAdapter.onDeleteAttachment");
            _selectedAttachmentId = attachment.getId();
            _selectedAttachmentFolderId = attachment.getFolderId();
            TwoButtonDialog.show(App.get(), DIALOG_YES_NO,
                    getView().getResources().getString(R.string.delete_file),
                    getView().getResources().getString(R.string.dialog_delete_message),
                    getView().getResources().getString(R.string.btn_yes),
                    getView().getResources().getString(R.string.btn_no), true, null);
        }

        @Override
        public void onAdd(AttachmentFolder attachmentFolder) {
            Log.v(TAG, "AttachedFilesAdapter.onAdd");
            if (checkMedia()) {
                // start of the upload process
                _selectedFolderId = attachmentFolder.getId();
                startAppPickerDialog();
            } else {
                ToastClient.toast(App.get(), R.string.toast_external_storage_needed, Toast.LENGTH_LONG);
            }
        }

        @Override
        public void onFailedClick(WebTransaction webTransaction) {
            PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, webTransaction.getUUID(), webTransaction.getId());
        }

        @Override
        public void onFailedLongClick(WebTransaction webTransaction) {
            _selectedTransactionId = webTransaction.getId();
            TwoButtonDialog.show(App.get(), DIALOG_YES_NO_FAILED,
                    "Cancel Upload", "Are you sure you want to cancel this upload?",
                    getView().getResources().getString(R.string.btn_yes),
                    getView().getResources().getString(R.string.btn_no), true, webTransaction);
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
                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, fui.uuid, _workOrderId,
                            _selectedFolderId, false, FileUtils.getFileNameFromUri(App.get(), fui.uri), fui.uri);
                } else {
                    Tracker.event(App.get(), new CustomEvent.Builder()
                            .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                            .addContext(new SpTracingContext(fui.uuid))
                            .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                            .addContext(new SpStatusContext(SpStatusContext.Status.FAIL, "Files Dialog, no uri"))
                            .build());
                }
                return;
            }

            for (GetFileDialog.UriIntent fui : fileResult) {
                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .category("AttachmentUpload")
                                .label(misc.isEmptyOrNull(getUid()) ? TAG : getUid())
                                .action("start")
                                .addContext(new SpTracingContext(fui.uuid))
                                .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Files Dialog Upload"))
                                .build());
                Attachment attachment = new Attachment();
                try {
                    attachment.folderId(_selectedFolderId);
                    AttachmentHelper.addAttachment(App.get(), fui.uuid, _workOrderId, attachment, fui.intent);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
            AppMessagingClient.setLoading(true);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _yesNoDialog_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setLoading(true);
            Attachment attachment = findAttachmentById(_selectedAttachmentId);
            WorkordersWebApi.deleteAttachment(App.get(), _workOrderId, _selectedAttachmentFolderId,
                    attachment, App.get().getSpUiContext());
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _yesNoDialog_onPrimaryFailed = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AttachmentTracker.complete(getContext(), ((WebTransaction) extraData).getUUID());

            UploadTrackerClient.uploadDelete((WebTransaction) extraData, TrackerEnum.DELIVERABLES);
            WebTransaction.delete(_selectedTransactionId);
            populateUi();
            // Todo, this is to force the WoD to update after the transaction is deleted
            WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
        }
    };

    private final PhotoUploadDialog.OnOkListener _photoDialog_onUpload = new PhotoUploadDialog.OnOkListener() {
        @Override
        public void onOk() {
            populateUi();
        }
    };

    private final DocumentClient _documentClient = new DocumentClient() {
        @Override
        public boolean processDownload(int documentId) {
            return true;
        }

        @Override
        public void onDownload(int documentId, File file, int state, boolean isSync) {
            Log.v(TAG, "DocumentClient.onDownload");
            if (file == null || state == DocumentConstants.PARAM_STATE_START) {
                if (state == DocumentConstants.PARAM_STATE_FINISH)
                    ToastClient.toast(App.get(), R.string.could_not_download_file, Toast.LENGTH_SHORT);
                return;
            }

            if (adapter != null)
                adapter.downloadComplete((int) documentId);
        }
    };

    private final AppMessagingClient _appClient = new AppMessagingClient() {
        @Override
        public void onNetworkDisconnected() {
            if (adapter != null)
                adapter.uploadClear();
            populateUi();
        }

        @Override
        public void onNetworkConnected() {
            WorkordersWebApi.getAttachments(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
            AppMessagingClient.setLoading(true);
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return methodName.toLowerCase().contains("attachment");
        }

        @Override
        public void onQueued(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onQueued");

            if (!methodName.equals("addAttachment"))
                return;

            populateUi();
        }

        @Override
        public void onStart(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onStart");
            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");
                if (adapter != null)
                    adapter.uploadProgress(uuidGroup, transactionParams, 0);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            populateUi();
        }

        @Override
        public void onPaused(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onPaused");
            if (!methodName.equals("addAttachment"))
                return;

            populateUi();
        }

        @Override
        public void onProgress(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, long pos, long size, long time) {
            Log.v(TAG, "WorkordersWebApi.onProgress");
            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                Double percent = pos * 1.0 / size;
                Log.v(TAG, "onProgress(" + folderId + "," + name + "," + (pos * 100 / size) + "," + (int) (time / percent));

                if (pos == size) {
                    AppMessagingClient.setLoading(true);
                    if (adapter != null)
                        adapter.uploadStop(uuidGroup, transactionParams);
                    populateUi();
                } else {
                    if (adapter != null)
                        adapter.uploadProgress(uuidGroup, transactionParams, (int) (pos * 100 / size));
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            populateUi();
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            Log.v(TAG, "WorkordersWebApi.onComplete");

            if (methodName.equals("addAttachment")) {
                try {
                    JsonObject obj = new JsonObject(transactionParams.methodParams);
                    String name = obj.getString("attachment.file.name");
                    int folderId = obj.getInt("attachment.folder_id");
                    if (adapter != null)
                        adapter.uploadStop(uuidGroup, transactionParams);
                    AppMessagingClient.setLoading(true);
                    WorkordersWebApi.getAttachments(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            } else if (methodName.equals("deleteAttachment")) {
                AppMessagingClient.setLoading(true);
                WorkordersWebApi.getAttachments(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
            } else if (successObject != null && methodName.equals("getAttachments")) {
                folders = (AttachmentFolders) successObject;
                populateUi();
                AppMessagingClient.setLoading(false);
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, String uuid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("uuid", uuid);

        Controller.show(context, uid, AttachedFilesDialog.class, params);
    }
}
