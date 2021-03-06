package com.fieldnation.ui.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.contexts.SpFileContext;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.service.data.filecache.FileCacheClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.v2.data.client.AttachmentHelper;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Michael on 9/27/2016.
 */

public class ReceiverActivity extends AuthSimpleActivity {
    public static final String TAG = "ReceiverActivity";

    // UI
    private WorkOrderPickerScreen _workOrderPicker;
    private UploadSlotPickerScreen _slotPicker;
    private FilePickerScreen _filePicker;

    private View _loadingLayout;
    private ProgressBar _loadingProgress;
    private TextView _loadingTextView;

    // Data
    private WorkOrder _selectedWorkOrder;
    private AttachmentFolder _selectedUploadSlot;
    private SharedFile[] _sharedFiles;
    private int _remainingCacheItems = 0;
    private String _myUUID = UUID.randomUUID().toString();

    // Animations
    private Animation _slideInLeft;
    private Animation _slideInRight;
    private Animation _slideOutLeft;
    private Animation _slideOutRight;

    private AnimationOutListener _animOutListener = new AnimationOutListener();
    private AnimationInListener _animInListener = new AnimationInListener();

    @Override
    public int getLayoutResource() {
        return R.layout.activity_share_receiver;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        _workOrderPicker = (WorkOrderPickerScreen) findViewById(R.id.workOrderPicker);
        _workOrderPicker.setListener(_workOrderPicker_listener);

        _slotPicker = (UploadSlotPickerScreen) findViewById(R.id.uploadSlotPicker);
        _slotPicker.setListener(_slotPicker_listener);

        _filePicker = (FilePickerScreen) findViewById(R.id.filePickerScreen);
        _filePicker.setListener(_filePicker_listener);

        _loadingLayout = findViewById(R.id.loading_layout);
        _loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        _loadingTextView = (TextView) findViewById(R.id.loading_title);

        _slideInLeft = AnimationUtils.loadAnimation(this, R.anim.activity_slide_in_left);
        _slideInLeft.setAnimationListener(_animInListener);
        _slideInRight = AnimationUtils.loadAnimation(this, R.anim.activity_slide_in_right);
        _slideInRight.setAnimationListener(_animInListener);
        _slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.activity_slide_out_left);
        _slideOutLeft.setAnimationListener(_animOutListener);
        _slideOutRight = AnimationUtils.loadAnimation(this, R.anim.activity_slide_out_right);
        _slideOutRight.setAnimationListener(_animOutListener);
    }

    @Override
    public int getToolbarId() {
        return 0;
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        _fileCacheClient.sub();
        _workOrderPicker.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _workOrderPicker.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.v(TAG, intent.toString());

        if (action.equals(Intent.ACTION_SEND_MULTIPLE)) {
            loadMultipleFiles(intent);
        } else if (action.equals(Intent.ACTION_SEND)) {
            loadSingleFile(intent);
        }
    }

    @Override
    protected void onPause() {
        _workOrderPicker.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        _fileCacheClient.unsub();
        _workOrderPicker.onStop();
        super.onStop();
    }

    private void loadSingleFile(Intent intent) {
        Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        _sharedFiles = new SharedFile[1];
        _remainingCacheItems = 1;
        _loadingProgress.setIndeterminate(false);
        _loadingProgress.setMax(1);
        _loadingProgress.setProgress(0);
        _loadingTextView.setText(getString(R.string.preparing_files_num, 1, 1));
        if (fileUri != null) {
            final String fileName = FileUtils.getFileNameFromUri(App.get(), fileUri);
            _sharedFiles[0] = new SharedFile(_myUUID, fileName, fileUri);

            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpTracingContext(_sharedFiles[0].getUUID()))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.START, "Single File"))
                    .addContext(new SpFileContext.Builder().name(_sharedFiles[0].getFileName()).size(0).build())
                    .build());

            FileCacheClient.cacheFileUpload(_sharedFiles[0].getUUID(), fileUri);
        } else {
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpTracingContext(new UUIDGroup(null, _myUUID)))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.FAIL, "Single File"))
                    .build());

            Toast.makeText(this, "Cannot upload file", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadMultipleFiles(Intent intent) {
        Log.v(TAG, intent.getExtras() + "");

        ArrayList<Uri> fileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (fileUris != null) {
            _remainingCacheItems = fileUris.size();
            _loadingProgress.setIndeterminate(false);
            _loadingProgress.setMax(_remainingCacheItems);
            _loadingProgress.setProgress(0);
            _loadingTextView.setText(getString(R.string.preparing_files_num, 1, _remainingCacheItems));
            _sharedFiles = new SharedFile[fileUris.size()];

            for (int i = 0; i < fileUris.size(); i++) {
                Log.v(TAG, "uris:" + fileUris.get(i));
            }

            for (int i = 0; i < fileUris.size(); i++) {
                final String fileName = FileUtils.getFileNameFromUri(App.get(), fileUris.get(i));
                _sharedFiles[i] = new SharedFile(_myUUID, fileName, fileUris.get(i));

                Tracker.event(App.get(), new CustomEvent.Builder()
                        .addContext(new SpTracingContext(_sharedFiles[i].getUUID()))
                        .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                        .addContext(new SpStatusContext(SpStatusContext.Status.START, "Multiple Files"))
                        .addContext(new SpFileContext.Builder().name(_sharedFiles[i].getFileName()).size(0).build())
                        .build());

                FileCacheClient.cacheFileUpload(_sharedFiles[i].getUUID(), fileUris.get(i));
            }
        } else {
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpTracingContext(new UUIDGroup(null, _myUUID)))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.FAIL, "Multiple Files"))
                    .build());

            Toast.makeText(this, "Cannot upload files", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (_workOrderPicker.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else if (_slotPicker.getVisibility() == View.VISIBLE) {
            animateSwap(_workOrderPicker, _slotPicker, true);
        } else if (_filePicker.getVisibility() == View.VISIBLE) {
            animateSwap(_slotPicker, _filePicker, true);
        }
    }

    @Override
    public void finish() {
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _myUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Leaving Receiver"))
                .build());
        super.finish();
    }

    private void animateSwap(View inView, View outView, boolean backwards) {
        _animInListener.view = inView;
        _animOutListener.view = outView;
        Animation inAnim = backwards ? _slideInLeft : _slideInRight;
        Animation outAnim = backwards ? _slideOutRight : _slideOutLeft;
        inView.setVisibility(View.VISIBLE);
        inView.clearAnimation();
        inView.startAnimation(inAnim);
        outView.clearAnimation();
        outView.startAnimation(outAnim);
    }

    private final WorkOrderPickerScreen.Listener _workOrderPicker_listener = new WorkOrderPickerScreen.Listener() {
        @Override
        public void onBackPressed() {
            finish();
        }

        @Override
        public void onWorkOrderSelected(WorkOrder workOrder) {
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpWorkOrderContext.Builder().workOrderId(workOrder.getId()).build())
                    .addContext(new SpTracingContext(new UUIDGroup(null, _myUUID)))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Workorder Selected"))
                    .build());

            _selectedWorkOrder = workOrder;
            _slotPicker.setWorkOrderId(workOrder.getId());
            animateSwap(_slotPicker, _workOrderPicker, false);
        }
    };

    private final UploadSlotPickerScreen.Listener _slotPicker_listener = new UploadSlotPickerScreen.Listener() {
        @Override
        public void onBackPressed() {
            animateSwap(_workOrderPicker, _slotPicker, true);
        }

        @Override
        public void onSlotSelected(AttachmentFolder uploadSlot) {
            _selectedUploadSlot = uploadSlot;
            // if file list == 1, then start upload and redirect to work order details
            if (_sharedFiles.length == 1) {
                startWorkOrderDetails();
                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .category("AttachmentUpload")
                                .label("ReceiverActivity - single")
                                .action("start")
                                .addContext(new SpTracingContext(null, _myUUID))
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Slot Selected"))
                                .addContext(new SpFileContext.Builder().name(_sharedFiles[0].getFileName()).size(0).build())
                                .build());

                try {
                    Attachment attachment = new Attachment();
                    attachment.folderId(_selectedUploadSlot.getId()).file(new com.fieldnation.v2.data.model.File().name(_sharedFiles[0].getFileName()));

                    AttachmentHelper.addAttachment(App.get(), _sharedFiles[0].getUUID(), _selectedWorkOrder.getId(), attachment, _sharedFiles[0].getFileName(), _sharedFiles[0].getUri(), true);
                } catch (Exception e) {
                    Log.v(TAG, e);
                }

            } else {
                _filePicker.setData(_selectedWorkOrder, _selectedUploadSlot, _sharedFiles);
                animateSwap(_filePicker, _slotPicker, false);
            }
        }
    };

    private final FilePickerScreen.Listener _filePicker_listener = new FilePickerScreen.Listener() {
        @Override
        public void onBackPressed() {
            animateSwap(_slotPicker, _filePicker, true);
        }

        @Override
        public void onSendFiles(SharedFile[] sharedFiles) {
            ToastClient.toast(App.get(), getString(R.string.sending_num_files, sharedFiles.length), Toast.LENGTH_SHORT);

            // TODO V2 api is not ready for this
//            if (_selectedUploadSlot.getMaxFiles() != null
//                    && _selectedUploadSlot.getMaxFiles() > 0
//                    && _selectedUploadSlot.getUploadedDocuments().length + sharedFiles.length > _selectedUploadSlot.getMaxFiles()) {
//                ToastClient.toast(App.get(), getString(R.string.please_select_fewer_than_num_files,
//                        _selectedUploadSlot.getMaxFiles() - _selectedUploadSlot.getUploadedDocuments().length), Toast.LENGTH_LONG);
//                return;
//            }
            for (SharedFile file : sharedFiles) {
                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .category("AttachmentUpload")
                                .label("ReceiverActivity - multiple")
                                .action("start")
                                .addContext(new SpTracingContext(file.getUUID()))
                                .addContext(new SpWorkOrderContext.Builder().workOrderId(_selectedWorkOrder.getId()).build())
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Send Files"))
                                .addContext(new SpFileContext.Builder().name(file.getFileName()).size(0).build())
                                .build());

                try {
                    Attachment attachment = new Attachment();
                    attachment.folderId(_selectedUploadSlot.getId()).file(new com.fieldnation.v2.data.model.File().name(file.getFileName()));

                    AttachmentHelper.addAttachment(App.get(), file.getUUID(), _selectedWorkOrder.getId(), attachment, file.getFileName(), file.getUri(), true);
                } catch (Exception e) {
                    Log.v(TAG, e);
                }

            }
            startWorkOrderDetails();
        }
    };

    private void startWorkOrderDetails() {
        startActivity(WorkOrderActivity.makeIntentShow(App.get(), _selectedWorkOrder.getId(), WorkOrderActivity.ACTION_ATTACHMENTS, _myUUID));
        finish();
    }

    private final FileCacheClient _fileCacheClient = new FileCacheClient() {
        @Override
        public void onFileCacheEnd(UUIDGroup uuid, Uri uri, long size, boolean success) {
            _remainingCacheItems--;

            _loadingProgress.setProgress(_sharedFiles.length - _remainingCacheItems);
            _loadingTextView.setText(
                    getString(R.string.preparing_files_num,
                            _sharedFiles.length - _remainingCacheItems + 1,
                            _sharedFiles.length));
            if (_remainingCacheItems == 0) {
                _loadingLayout.setVisibility(View.GONE);
            }
        }
    };

    private static class AnimationOutListener extends DefaultAnimationListener {
        public View view;

        @Override
        public void onAnimationEnd(Animation animation) {
            view.clearAnimation();
            view.setVisibility(View.GONE);
        }
    }

    private static class AnimationInListener extends DefaultAnimationListener {
        public View view;

        @Override
        public void onAnimationEnd(Animation animation) {
            view.clearAnimation();
        }
    }
}
