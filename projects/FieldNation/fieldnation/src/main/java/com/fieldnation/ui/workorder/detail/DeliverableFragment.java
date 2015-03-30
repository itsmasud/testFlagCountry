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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.ActivityResult;
import com.fieldnation.ForLoopRunnable;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderDataClient;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.Stopwatch;

import java.io.File;
import java.security.SecureRandom;

public class DeliverableFragment extends WorkorderFragment {
    private final String TAG = UniqueTag.makeTag("ui.workorder.detail.DeliverableFragment");

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

    // State
    private static final String STATE_UPLOAD_SLOTID = "STATE_UPLOAD_SLOTID";
    private static final String STATE_TEMP_FILE = "STATE_TEMP_FILE";

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
    private WorkorderDataClient _workorderClient;
    private Profile _profile = null;
    //private Bundle _delayedAction = null;
    private final SecureRandom _rand = new SecureRandom();
    private int _uploadingSlotId = -1;
    private int _uploadCount = 0;
    private int _deleteCount = 0;
    private File _tempFile;

    // Temporary storage
    private ActivityResult _activityResult = null;

    /*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/

    public DeliverableFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TEMP_FILE))
                _tempFile = new File(savedInstanceState.getString(STATE_TEMP_FILE));

            if (savedInstanceState.containsKey(STATE_UPLOAD_SLOTID))
                _uploadingSlotId = savedInstanceState.getInt(STATE_UPLOAD_SLOTID);
        }

        return inflater.inflate(R.layout.fragment_workorder_deliverables, container, false);
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
    public void onSaveInstanceState(Bundle outState) {
        if (_uploadingSlotId > 0)
            outState.putInt(STATE_UPLOAD_SLOTID, _uploadingSlotId);

        if (_tempFile != null)
            outState.putString(STATE_TEMP_FILE, _tempFile.getAbsolutePath());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        _context = getActivity().getApplicationContext();
//todo remove
        AuthTopicService.subscribeAuthState(_context, 0, TAG, _authReceiver);
        Topics.subscrubeProfileUpdated(_context, TAG + ":ProfileService", _profile_topicService);
    }

    @Override
    public void onPause() {
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
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        populateUi();
    }

    private PendingIntent getNotificationIntent() {
        Intent intent = new Intent(_context, WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DELIVERABLES);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
        return PendingIntent.getActivity(_context, _rand.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    private void tryActivityResult() {
        if (_activityResult != null) {
            Log.v(TAG, "recovering");
            if (performActivityResult(_activityResult.requestCode, _activityResult.resultCode, _activityResult.data))
                _activityResult = null;
        }

    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_profile == null)
            return;

        if (getActivity() == null)
            return;

        tryActivityResult();

        Stopwatch stopwatch = new Stopwatch(true);
        final Document[] docs = _workorder.getDocuments();
        if (docs != null && docs.length > 0) {
            ForLoopRunnable r = new ForLoopRunnable(docs.length, new Handler()) {
                private final Document[] _docs = docs;

                @Override
                public void next(int i) throws Exception {
                    DocumentView v = null;
                    if (i < _reviewList.getChildCount()) {
                        v = (DocumentView) _reviewList.getChildAt(i);
                    } else {
                        v = new DocumentView(_context);
                        _reviewList.addView(v);
                    }
                    Document doc = _docs[i];
                    v.setData(_workorder, doc);
                }

                @Override
                public void finish(int count) throws Exception {
                    if (_reviewList.getChildCount() > count) {
                        _reviewList.removeViews(count - 1, _reviewList.getChildCount() - count);
                    }
                }
            };
            _reviewList.post(r);
            _noDocsTextView.setVisibility(View.GONE);
        } else {
            _reviewList.removeAllViews();
            _noDocsTextView.setVisibility(View.VISIBLE);
        }
        Log.v(TAG, "pop docs time " + stopwatch.finish());

        stopwatch.start();

        final UploadSlot[] slots = _workorder.getUploadSlots();
        if (slots != null && slots.length > 0) {
            Log.v(TAG, "US count: " + slots.length);
            ForLoopRunnable r = new ForLoopRunnable(slots.length, new Handler()) {
                private final UploadSlot[] _slots = slots;

                @Override
                public void next(int i) throws Exception {
                    UploadSlotView v = null;
                    if (i < _filesLayout.getChildCount()) {
                        v = (UploadSlotView) _filesLayout.getChildAt(i);
                    } else {
                        v = new UploadSlotView(_context);
                        _filesLayout.addView(v);
                    }
                    UploadSlot slot = _slots[i];
                    v.setData(_workorder, _profile.getUserId(), slot, _uploaded_document_listener);
                    v.setListener(_uploadSlot_listener);
                }

                @Override
                public void finish(int count) throws Exception {
                    Log.v(TAG, "US fin: " + count + "/" + _filesLayout.getChildCount());
                    if (_filesLayout.getChildCount() > count) {
                        _filesLayout.removeViews(count - 1, _filesLayout.getChildCount() - count);
                    }
                }
            };
            _filesLayout.post(r);
        } else {
            _filesLayout.removeAllViews();
        }
        Log.v(TAG, "upload docs time " + stopwatch.finish());

        setLoading(false);
    }


    private boolean performActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult() resultCode= " + resultCode);

        if (_workorder == null)
            return false;

        if (_uploadingSlotId < 0)
            return false;

        if (_service == null)
            return false;

        if (getActivity() == null)
            return false;

        if ((requestCode == RESULT_CODE_GET_ATTACHMENT || requestCode == RESULT_CODE_GET_CAMERA_PIC)
                && resultCode == Activity.RESULT_OK) {

            setLoading(true);

            if (data == null) {
                if (_tempFile == null)
                    return false;

                Log.v(TAG, "local path");
// todo remove
                getActivity().startService(_service.uploadDeliverable(WEB_SEND_DELIVERABLE,
                        _workorder.getWorkorderId(), _uploadingSlotId,
                        _tempFile.getAbsolutePath(), getNotificationIntent()));

                return true;
            } else {
                Log.v(TAG, "from intent");
// todo remove
                getActivity().startService(_service.uploadDeliverable(
                        WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
                        _uploadingSlotId, data, getNotificationIntent()));
                return true;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        _activityResult = new ActivityResult(requestCode, resultCode, data);
        tryActivityResult();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            if (_workorder != null) {
                _workorder.dispatchOnChange();
            }
        }
    };

    private final UploadedDocumentView.Listener _uploaded_document_listener = new UploadedDocumentView.Listener() {
        @Override
        public void onDelete(UploadedDocumentView v, UploadedDocument document) {
            _deleteCount++;
// todo remove
            _context.startService(_service.deleteDeliverable(WEB_DELETE_DELIVERABLE,
                    _workorder.getWorkorderId(),
                    document.getWorkorderUploadId()));
        }
    };


    // step 1, user taps on the add button
    private final UploadSlotView.Listener _uploadSlot_listener = new UploadSlotView.Listener() {
        @Override
        public void onUploadClick(UploadSlotView view, UploadSlot slot) {
            if (checkMedia()) {
                // start of the upload process
                _uploadingSlotId = slot.getSlotId();
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
    private final AppPickerDialog.Listener _appdialog_listener = new AppPickerDialog.Listener() {

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
// todo remove

    private final TopicReceiver _profile_topicService = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (getActivity() == null)
                return;
            if (Topics.TOPIC_PROFILE_UPDATE.equals(topicId)) {
                parcel.setClassLoader(getActivity().getClassLoader());
                _profile = parcel.getParcelable(Topics.TOPIC_PROFILE_PARAM_PROFILE);
            }
            populateUi();
        }
    };
    private final AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_service == null || isNew) {
                _service = new WorkorderWebClient(_context, username, authToken, _resultReceiver);
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _service = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _service = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(_context);
        }
    };


    private final WebResultReceiver _resultReceiver = new WebResultReceiver(
            new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            Log.v(TAG, "Method Stub: onSuccess()");
            if (resultCode == WEB_GET_PROFILE) {
                populateUi();
            } else if (resultCode == WEB_DELETE_DELIVERABLE
                    || resultCode == WEB_SEND_DELIVERABLE) {
                Log.v(TAG, "WEB_DELETE_DELIVERABLE || WEB_SEND_DELIVERABLE");
                if (resultCode == WEB_DELETE_DELIVERABLE)
                    _deleteCount--;

                if (resultCode == WEB_SEND_DELIVERABLE)
                    _uploadCount--;

                if (_deleteCount < 0)
                    _deleteCount = 0;
                if (_uploadCount < 0)
                    _uploadCount = 0;

                // TODO, update individual UI elements when complete.
                if (_deleteCount == 0 && _uploadCount == 0) {
                    _workorder.dispatchOnChange();
                    setLoading(true);
                }

            } else if (resultCode == WEB_CHANGE) {
                Log.v(TAG, "WEB_CHANGE");
                setLoading(true);
                _workorder.dispatchOnChange();
            } else {
                Log.v(TAG, "unknown resultcode");
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
            try {
                Toast.makeText(_context, R.string.toast_could_not_complete_request, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            setLoading(false);
        }
    };

}
