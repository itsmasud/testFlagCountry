package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.Stopwatch;
import com.fieldnation.utils.misc;

import java.io.File;
import java.security.SecureRandom;

public class DeliverableFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.DeliverableFragment";

    // pageRequest parameters
    public static final String PR_TASK_ID = "PR_TASK_ID";

    // activity result codes
    private static final int RESULT_CODE_BASE = 100;
    private static final int RESULT_CODE_GET_ATTACHMENT = RESULT_CODE_BASE + 1;
    private static final int RESULT_CODE_GET_CAMERA_PIC = RESULT_CODE_BASE + 2;

    // private static final int WEB_GET_DOCUMENTS = 1;
    private static final int WEB_GET_PROFILE = 2;
    private static final int WEB_DELETE_DELIVERABLE = 3;
    private static final int WEB_SEND_DELIVERABLE = 4;
    private static final int WEB_CHANGE = 5;

    // UI
    private OverScrollView _scrollView;
    private LinearLayout _reviewList;
    private LinearLayout _filesLayout;
    private TextView _noDocsTextView;
    private RefreshView _refreshView;

    private AppPickerDialog _appPickerDialog;

    // Data
    private Context _context;
    private Workorder _workorder;
    private WorkorderService _service;
    private ProfileService _profileService;
    private Profile _profile = null;
    //private Bundle _delayedAction = null;
    private SecureRandom _rand = new SecureRandom();

    // Temporary storage
    private UploadSlot _uploadingSlot;
    private UploadSlotView _uploadingSlotView;
    private int _uploadCount = 0;
    private int _deleteCount = 0;
    private boolean _isCached = true;
    private File _tempFile;

    /*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workorder_deliverables,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _scrollView = (OverScrollView) view.findViewById(R.id.scroll_view);
        _scrollView.setOnOverScrollListener(_refreshView);

        _reviewList = (LinearLayout) view.findViewById(R.id.review_list);

        _filesLayout = (LinearLayout) view.findViewById(R.id.files_layout);

        _noDocsTextView = (TextView) view.findViewById(R.id.nodocs_textview);

        _appPickerDialog = AppPickerDialog.getInstance(getFragmentManager(), TAG);
        _appPickerDialog.setListener(_appdialog_listener);

        checkMedia();

        populateUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        _context = getActivity().getApplicationContext();
        AuthTopicService.subscribeAuthState(_context, 0, TAG, _authReceiver);
    }

    @Override
    public void onPause() {
        TopicService.delete(_context, TAG);
        super.onPause();
    }

    private boolean checkMedia() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {
//        getData();
        checkMedia();
//        executeDelayedAction();
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        _isCached = isCached;
        getData();
    }

    private PendingIntent getNotificationIntent() {
        Intent intent = new Intent(_context, WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DELIVERABLES);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
        return PendingIntent.getActivity(_context, _rand.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void getData() {
        if (_profileService == null)
            return;

        _refreshView.startRefreshing();
        _profile = null;
        _context.startService(_profileService.getMyUserInformation(WEB_GET_PROFILE, true));
    }

    @Override
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
        if (_workorder == null)
            return;

        if (_profile == null)
            return;

        if (getActivity() == null)
            return;

        Stopwatch stopwatch = new Stopwatch(true);
        _reviewList.removeAllViews();
        final Document[] docs = _workorder.getDocuments();
        if (docs != null && docs.length > 0) {
            ForLoopRunnable r = new ForLoopRunnable(docs.length) {
                private Document[] _docs = docs;

                @Override
                public void next(int i) {
                    Document doc = _docs[i];
                    DocumentView v = new DocumentView(_context);
                    _reviewList.addView(v);
                    v.setData(_workorder, doc);

                    _reviewList.postDelayed(this, 50);
                }
            };
            _reviewList.postDelayed(r, 100);
            _noDocsTextView.setVisibility(View.GONE);
        } else {
            _noDocsTextView.setVisibility(View.VISIBLE);
        }
        Log.v(TAG, "pop docs time " + stopwatch.finish());

        stopwatch.start();
        _filesLayout.removeAllViews();
        final UploadSlot[] slots = _workorder.getUploadSlots();
        if (slots != null) {
            ForLoopRunnable r = new ForLoopRunnable(slots.length) {
                private UploadSlot[] _slots = slots;

                @Override
                public void next(int i) {
                    UploadSlot slot = _slots[i];
                    UploadSlotView v = new UploadSlotView(_context);
                    v.setData(_workorder, _profile.getUserId(), slot, _uploaded_document_listener);
                    v.setListener(_uploadSlot_listener);

                    _filesLayout.addView(v);
                    _filesLayout.postDelayed(this, 50);
                }
            };
            _filesLayout.postDelayed(r, 100);
        }
        Log.v(TAG, "upload docs time " + stopwatch.finish());
        _refreshView.refreshComplete();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _context = getActivity().getApplicationContext();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        _appPickerDialog.addIntent(_context.getPackageManager(), intent, "Get Content");

        if (_context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _appPickerDialog.addIntent(_context.getPackageManager(), intent, "Take Picture");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult() resultCode= " + resultCode);


        if ((requestCode == RESULT_CODE_GET_ATTACHMENT || requestCode == RESULT_CODE_GET_CAMERA_PIC)
                && resultCode == Activity.RESULT_OK) {

            _refreshView.startRefreshing();

            if (data == null) {
                Log.v(TAG, "local path");
                _context.startService(_service.uploadDeliverable(WEB_SEND_DELIVERABLE,
                        _workorder.getWorkorderId(), _uploadingSlot.getSlotId(),
                        _tempFile.getAbsolutePath(), getNotificationIntent()));
            } else {
                Log.v(TAG, "from intent");
                _context.startService(_service.uploadDeliverable(
                        WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
                        _uploadingSlot.getSlotId(), data, getNotificationIntent()));
            }
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            if (_workorder != null) {
                _workorder.dispatchOnChange();
            } else {
                getData();
            }
            setLoading(true);
        }
    };

    private UploadedDocumentView.Listener _uploaded_document_listener = new UploadedDocumentView.Listener() {
        @Override
        public void onDelete(UploadedDocumentView v, UploadedDocument document) {
            _deleteCount++;
            _context.startService(_service.deleteDeliverable(WEB_DELETE_DELIVERABLE,
                    _workorder.getWorkorderId(),
                    document.getWorkorderUploadId()));
        }
    };


    // step 1, user taps on the add button
    private UploadSlotView.Listener _uploadSlot_listener = new UploadSlotView.Listener() {
        @Override
        public void onUploadClick(UploadSlotView view, UploadSlot slot) {
            if (checkMedia()) {
                // start of the upload process
                _uploadingSlot = slot;
                _uploadingSlotView = view;
                _appPickerDialog.show();
            } else {
                Toast.makeText(
                        _context,
                        "Need External Storage, please insert storage device before continuing",
                        Toast.LENGTH_LONG).show();
            }
        }
    };


    // step 2, user selects an app to load the file with
    private AppPickerDialog.Listener _appdialog_listener = new AppPickerDialog.Listener() {

        @Override
        public void onClick(AppPickerPackage pack) {
            Intent src = pack.intent;

            ResolveInfo info = pack.resolveInfo;

            src.setComponent(new ComponentName(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name));

            _uploadCount++;

            if (src.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                startActivityForResult(src, RESULT_CODE_GET_ATTACHMENT);
            } else {
                String packageName = _context.getPackageName();
                File externalPath = Environment.getExternalStorageDirectory();
                new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp").mkdirs();
                File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp/IMAGE-" + System.currentTimeMillis() + ".png");
                _tempFile = temppath;
                src.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
                startActivityForResult(src, RESULT_CODE_GET_CAMERA_PIC);
            }
            // next: see onActivityResult()
        }
    };


    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_service == null || _profileService == null || isNew) {
                _service = new WorkorderService(_context, username, authToken, _resultReceiver);
                _profileService = new ProfileService(_context, username, authToken, _resultReceiver);
                getData();
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _service = null;
            _profileService = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _service = null;
            _profileService = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(_context);
        }
    };


    private WebResultReceiver _resultReceiver = new WebResultReceiver(
            new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            Log.v(TAG, "Method Stub: onSuccess()");
            if (resultCode == WEB_GET_PROFILE) {
                _profile = null;
                try {
                    _profile = Profile.fromJson(
                            new JsonObject(new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA))));
                } catch (Exception e) {
                    // TODO mulligan?
                    e.printStackTrace();
                    _profile = null;
                }
                populateUi();
            } else if (resultCode == WEB_DELETE_DELIVERABLE
                    || resultCode == WEB_SEND_DELIVERABLE) {
                if (resultCode == WEB_DELETE_DELIVERABLE)
                    _deleteCount--;

                if (resultCode == WEB_SEND_DELIVERABLE)
                    _uploadCount--;

                if (_deleteCount < 0)
                    _deleteCount = 0;
                if (_uploadCount < 0)
                    _uploadCount = 0;

                // TODO, update individual UI elements when complete.
                if (_deleteCount == 0 && _uploadCount == 0)
                    _workorder.dispatchOnChange();

            } else if (resultCode == WEB_CHANGE) {
                _workorder.dispatchOnChange();
            }
        }

        @Override
        public Context getContext() {
            return DeliverableFragment.this._context;
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            AuthTopicService.requestAuthInvalid(_context);
            if (resultCode == WEB_DELETE_DELIVERABLE)
                _deleteCount--;
            if (resultCode == WEB_SEND_DELIVERABLE)
                _uploadCount--;
            _service = null;
            _profileService = null;
            Toast.makeText(_context, "Could not complete request", Toast.LENGTH_LONG).show();
            _refreshView.refreshComplete();
        }
    };

}
