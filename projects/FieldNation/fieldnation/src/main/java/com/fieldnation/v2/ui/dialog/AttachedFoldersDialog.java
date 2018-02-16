package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.AttachmentHelper;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.ui.AttachedFoldersAdapter;

import java.util.List;

/**
 * Created by mc on 8/24/17.
 */

public class AttachedFoldersDialog extends FullScreenDialog {
    private static final String TAG = "AttachedFoldersDialog";

    // Dialog Tags
    private static final String DIALOG_GET_FILE = TAG + ".getFileDialog";
    private static final String DIALOG_PHOTO_UPLOAD = TAG + ".photoUploadDialog";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _list;

    // Data
    private AttachmentFolders _folders = null;
    private int _workOrderId;
    private int _currentFolderId;
    private String _uiUUID;

    /*-*********----------**********-*/
    /*-         Life Cycle          -*/
    /*-*********----------**********-*/
    public AttachedFoldersDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_toolbar_recycle, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle("Attachment Folders");

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
        _list.setAdapter(_adapter);
        GetFileDialog.addOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        PhotoUploadDialog.addOnOkListener(DIALOG_PHOTO_UPLOAD, _photoUpload_onOk);
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        _workOrdersApi.sub();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "show");
        super.show(params, animate);

        _workOrderId = params.getInt("workOrderId");
        _uiUUID = params.getString("uiUUID");
        WorkordersWebApi.getAttachments(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Folders Dialog"))
                .build());

        populateUi();
    }

    private void populateUi() {
        if (_list == null)
            return;

        if (_folders == null)
            return;

        _adapter.setAttachments(_folders);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey("currentFolderId"))
            _currentFolderId = savedState.getInt("currentFolderId");
        super.onRestoreDialogState(savedState);
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_currentFolderId != 0)
            outState.putInt("currentFolderId", _currentFolderId);
        super.onSaveDialogState(outState);
    }

    @Override
    public void onPause() {
        _workOrdersApi.unsub();

        super.onPause();
    }

    @Override
    public void onStop() {
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Folders Dialog"))
                .build());

        GetFileDialog.removeOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        PhotoUploadDialog.removeOnOkListener(DIALOG_PHOTO_UPLOAD, _photoUpload_onOk);
        super.onStop();
    }

    private final AttachedFoldersAdapter _adapter = new AttachedFoldersAdapter() {
        @Override
        public void onItemClick(AttachmentFolder attachmentFolder) {
            _currentFolderId = attachmentFolder.getId();
            startAppPickerDialog();
        }
    };

    private void startAppPickerDialog() {
        GetFileDialog.show(getContext(), DIALOG_GET_FILE, _uiUUID);
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    private final GetFileDialog.OnFileListener _getFile_onFile = new GetFileDialog.OnFileListener() {
        @Override
        public void onFile(List<GetFileDialog.UriIntent> fileResult) {
            Log.v(TAG, "onFile");
            if (fileResult.size() == 0)
                return;

            if (fileResult.size() == 1) {
                GetFileDialog.UriIntent fui = fileResult.get(0);
                if (fui.uri != null) {
                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, fui.uuid, _workOrderId,
                            _currentFolderId, false, FileUtils.getFileNameFromUri(App.get(), fui.uri), fui.uri);
                } else {
                    // TODO show a toast?
                    Tracker.event(App.get(), new CustomEvent.Builder()
                            .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                            .addContext(new SpTracingContext(fui.uuid))
                            .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                            .addContext(new SpStatusContext(SpStatusContext.Status.FAIL, "Folders Dialog, no uri"))
                            .build());
                }
                return;
            }

            for (GetFileDialog.UriIntent fui : fileResult) {
                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .category("AttachmentUpload")
                                .label((misc.isEmptyOrNull(getUid()) ? TAG : getUid()) + " - multiple")
                                .action("start")
                                .addContext(new SpTracingContext(fui.uuid))
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Folders Dialog Upload"))
                                .build());

                try {
                    Attachment attachment = new Attachment();
                    attachment.folderId(_currentFolderId);
                    AttachmentHelper.addAttachment(App.get(), fui.uuid, _workOrderId, attachment, fui.intent);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
            _currentFolderId = 0;
            dismiss(true);
            AttachedFilesDialog.show(App.get(), null, _uiUUID, _workOrderId);
        }
    };

    private final PhotoUploadDialog.OnOkListener _photoUpload_onOk = new PhotoUploadDialog.OnOkListener() {
        @Override
        public void onOk() {
            dismiss(false);
            AttachedFilesDialog.show(App.get(), null, _uiUUID, _workOrderId);
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
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && methodName.equals("getAttachments")) {
                _folders = (AttachmentFolders) successObject;
                populateUi();
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, String uiUUID, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("uiUUID", uiUUID);

        Controller.show(context, uid, AttachedFoldersDialog.class, params);
    }
}
