package com.fieldnation.ui.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadingDocument;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.workorder.WorkorderActivity;

import java.util.ArrayList;

/**
 * Created by Michael on 9/27/2016.
 */

public class ShareReceiverActivity extends AuthSimpleActivity {
    public static final String TAG = "ShareReceiverActivity";

    // UI
    private WorkOrderPickerScreen _workOrderPicker;
    private UploadSlotPickerScreen _slotPicker;
    private FilePickerScreen _filePicker;

    private View _loadingLayout;
    private ProgressBar _loadingProgress;
    private TextView _loadingTextView;

    // Services
    private WorkorderClient _workorderClient;

    // Data
    private WorkOrder _selectedWorkOrder;
    private UploadSlot _selectedUploadSlot;
    private UploadingDocument[] _sharedFileList;
    private int _remainingCacheItems = 0;

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

    }

    @Override
    public int getToolbarId() {
        return 0;
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    protected void onStart() {
        super.onStart();

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        super.onStop();
    }

    private void loadSingleFile(Intent intent) {
        Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        _sharedFileList = new UploadingDocument[1];
        _remainingCacheItems = 1;
        _loadingProgress.setIndeterminate(false);
        _loadingProgress.setMax(1);
        _loadingProgress.setProgress(0);
        _loadingTextView.setText("Preparing files(0 of 1)");
        if (fileUri != null) {
            final String fileName = FileUtils.getFileNameFromUri(App.get(), fileUri);
            _sharedFileList[0] = new UploadingDocument(fileName, fileUri);
            WorkorderClient.cacheDeliverableUpload(App.get(), fileUri);
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
            _loadingTextView.setText("Preparing files(0 of " + _remainingCacheItems + ")");
            _sharedFileList = new UploadingDocument[fileUris.size()];

            for (int i = 0; i < fileUris.size(); i++) {
                Log.v(TAG, "uris:" + fileUris.get(i));
            }

            for (int i = 0; i < fileUris.size(); i++) {
                final String fileName = FileUtils.getFileNameFromUri(App.get(), fileUris.get(i));
                _sharedFileList[i] = new UploadingDocument(fileName, fileUris.get(i));
                WorkorderClient.cacheDeliverableUpload(App.get(), fileUris.get(i));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (_workOrderPicker.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else if (_slotPicker.getVisibility() == View.VISIBLE) {
            _workOrderPicker.setVisibility(View.VISIBLE);
            _slotPicker.setVisibility(View.GONE);
        } else if (_filePicker.getVisibility() == View.VISIBLE) {
            _slotPicker.setVisibility(View.VISIBLE);
            _filePicker.setVisibility(View.GONE);
        }
    }

    private final WorkOrderPickerScreen.Listener _workOrderPicker_listener = new WorkOrderPickerScreen.Listener() {
        @Override
        public void onBackPressed() {
            finish();
        }

        @Override
        public void onWorkOrderSelected(WorkOrder workOrder) {
            _selectedWorkOrder = workOrder;
            _slotPicker.setWorkOrderId(workOrder);
            // TODO animate!
            _workOrderPicker.setVisibility(View.GONE);
            _slotPicker.setVisibility(View.VISIBLE);
        }
    };

    private final UploadSlotPickerScreen.Listener _slotPicker_listener = new UploadSlotPickerScreen.Listener() {
        @Override
        public void onBackPressed() {
            // TODO animate!!
            _slotPicker.setVisibility(View.GONE);
            _workOrderPicker.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSlotSelected(UploadSlot uploadSlot) {
            _selectedUploadSlot = uploadSlot;
            // TODO animate!
            // TODO if file list == 1, then start upload and redirect to work order details
            if (_sharedFileList.length == 1) {
                WorkorderClient.uploadDeliverable(App.get(), _selectedWorkOrder.getId(),
                        _selectedUploadSlot.getSlotId(), _sharedFileList[0].getFileName(),
                        _sharedFileList[0].getUri());
                startWorkOrderDetails();
            } else {
                _filePicker.setData(_selectedWorkOrder, _selectedUploadSlot, _sharedFileList);
                _slotPicker.setVisibility(View.GONE);
                _filePicker.setVisibility(View.VISIBLE);
            }
        }
    };

    private final FilePickerScreen.Listener _filePicker_listener = new FilePickerScreen.Listener() {
        @Override
        public void onBackPressed() {
            // TODO animate!
            _filePicker.setVisibility(View.GONE);
            _slotPicker.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSendFiles(UploadingDocument[] files) {
            ToastClient.toast(App.get(), "Sending " + files.length + " files", Toast.LENGTH_SHORT);
            for (UploadingDocument f : files) {
                WorkorderClient.uploadDeliverable(App.get(), _selectedWorkOrder.getId(),
                        _selectedUploadSlot.getSlotId(), f.getFileName(), f.getUri());
            }
            startWorkOrderDetails();
        }
    };

    private void startWorkOrderDetails() {
        Intent intent = WorkorderActivity.makeIntentShow(App.get(), _selectedWorkOrder.getId());
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DELIVERABLES);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClient.subDeliverableCache();
        }

        @Override
        public void onDeliveraleCacheEnd(Uri uri, String filename) {
            _remainingCacheItems--;

            _loadingProgress.setProgress(_sharedFileList.length - _remainingCacheItems);
            _loadingTextView.setText("Preparing files(" + (_sharedFileList.length - _remainingCacheItems) + " of " + _sharedFileList.length + ")");
            if (_remainingCacheItems == 0) {
                _loadingLayout.setVisibility(View.GONE);
            }
        }
    };
}
