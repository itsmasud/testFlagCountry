package com.fieldnation.ui.workorder.detail;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.dialog.ClosingNotesDialog;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Arrays;

public class DeliverableFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.DeliverableFragment";

    // pageRequest parameters
    public static final String PR_ACTION = "PR_ACTION";
    public static final String PR_UPLOAD_PICTURE = "PR_UPLOAD_PICTURE";
    public static final String PR_UPLOAD_FILE = "PR_UPLOAD_FILE";
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
    private LinearLayout _reviewList;
    private LinearLayout _filesLayout;
    private View _bar1View;
    private RelativeLayout _loadingLayout;
    private TextView _noDocsTextView;
    private ActionBarTopView _topBar;

    private ConfirmDialog _confirmDialog;
    private ClosingNotesDialog _closingDialog;
    private DeviceCountDialog _deviceCountDialog;
    private AppPickerDialog _appPickerDialog;
    private TermsDialog _termsDialog;

    // Data
    private GlobalState _gs;
    private Workorder _workorder;
    private WorkorderService _service;
    private ProfileService _profileService;
    private Profile _profile = null;
    private Bundle _delayedAction = null;
    private Integer[] woStatus = {5, 6, 7}; //work order status approved, paid, canceled

    // private List<Deliverable> _deliverables = null;
    // private List<Task> _tasks = null;
    // private int _loadingCounter = 0;
    private SecureRandom _rand = new SecureRandom();

    // temporary storage
    private UploadSlot _uploadingSlot;
    private UploadSlotView _uploadingSlotView;
    private int _uploadCount = 0;
    private int _deleteCount = 0;

    /*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workorder_deliverables,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _gs = (GlobalState) getActivity().getApplicationContext();
        _gs.requestAuthentication(_authClient);

        _reviewList = (LinearLayout) view.findViewById(R.id.review_list);
        _filesLayout = (LinearLayout) view.findViewById(R.id.files_layout);
        _bar1View = view.findViewById(R.id.bar1_view);
        _loadingLayout = (RelativeLayout) view
                .findViewById(R.id.loading_layout);
        _noDocsTextView = (TextView) view.findViewById(R.id.nodocs_textview);

        _topBar = (ActionBarTopView) view.findViewById(R.id.actiontop_view);
        _topBar.setListener(_actionbartop_listener);

        _closingDialog = ClosingNotesDialog.getInstance(getFragmentManager(), TAG);
        _closingDialog.setListener(_closingNotes_onOk);

        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog.setListener(_deviceCountListener);

        _appPickerDialog = AppPickerDialog.getInstance(getFragmentManager(), TAG);
        _appPickerDialog.setListener(_appdialog_listener);

        _confirmDialog = ConfirmDialog.getInstance(getFragmentManager(), TAG);
        _confirmDialog.setListener(_confirmDialog_listener);

        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);

        checkMedia();

        populateUi();
        executeDelayedAction();
        setLoading(true);
    }

    private boolean checkMedia() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {
        getData();
        checkMedia();
        executeDelayedAction();
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

        getData();
        executeDelayedAction();
    }

    private PendingIntent getNotificationIntent() {
        Intent intent = new Intent(_gs, WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB,
                WorkorderActivity.TAB_DELIVERABLES);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID,
                _workorder.getWorkorderId());

        return PendingIntent.getActivity(_gs, _rand.nextInt(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void getData() {
        if (_profileService == null)
            return;

        _profile = null;
        _gs.startService(_profileService.getMyUserInformation(WEB_GET_PROFILE,
                true));
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_topBar != null)
            _topBar.setWorkorder(_workorder);

        if (_profile == null)
            return;

        if (getActivity() == null)
            return;

        _reviewList.removeAllViews();
        Document[] docs = _workorder.getDocuments();
        if (docs != null && docs.length > 0) {
            for (int i = 0; i < docs.length; i++) {
                Document doc = docs[i];
                DocumentView v = new DocumentView(getActivity());
                _reviewList.addView(v);
                v.setDocument(doc);

                //if work order completed or canceled then hide/disable any controls actions
                if (_workorder != null && Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                    v.hideDeleteButton();
                }
            }
            _noDocsTextView.setVisibility(View.GONE);
        } else {
            _noDocsTextView.setVisibility(View.VISIBLE);
        }

        _filesLayout.removeAllViews();
        UploadSlot[] slots = _workorder.getUploadSlots();
        if (slots != null) {
            for (int i = 0; i < slots.length; i++) {
                UploadSlot slot = slots[i];
                UploadSlotView v = new UploadSlotView(getActivity());
                v.setUploadSlot(_profile.getUserId(), slot,
                        _uploaded_document_listener, _workorder);

                //if work order completed or canceled then hide/disable any controls actions
                if (_workorder != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                    v.setListener(_uploadSlot_listener);
                }

                _filesLayout.addView(v);
            }
        }
        setLoading(false);
    }

    private void executeDelayedAction() {
        if (_delayedAction == null)
            return;

        if (_workorder == null)
            return;

        if (_filesLayout == null)
            return;

        int taskId = _delayedAction.getInt(PR_TASK_ID);

        // find slot
        UploadSlot[] slots = _workorder.getUploadSlots();
        UploadSlot slot = null;
        for (int i = 0; i < slots.length; i++) {
            Task task = slots[i].getTask();
            if (task != null) {
                if (task.getTaskId() == taskId) {
                    slot = slots[i];
                    break;
                }
            }
        }

        for (int i = 0; i < _filesLayout.getChildCount(); i++) {
            View v = _filesLayout.getChildAt(i);

            if (v instanceof UploadSlotView) {
                UploadSlotView uv = (UploadSlotView) v;
                if (uv.getUploadSlotId() == taskId) {
                    _uploadCount++;
                    _uploadingSlot = slot;
                    _uploadingSlotView = uv;
                    _appPickerDialog.show();
                    break;
                }
            }
        }
        _delayedAction = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        _appPickerDialog.addIntent(getActivity().getPackageManager(), intent, "Get Content");

        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _appPickerDialog.addIntent(getActivity().getPackageManager(), intent, "Take Picture");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult() resultCode= " + resultCode);

        if (requestCode == RESULT_CODE_GET_ATTACHMENT || requestCode == RESULT_CODE_GET_CAMERA_PIC) {
            if (data == null)
                return;

            try {
                // generate temp file
                File externalPath = Environment.getExternalStorageDirectory();
                String packageName = _gs.getPackageName();
                File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");
                temppath.mkdirs();
                File tempfile = File.createTempFile("DATA", null, temppath);


                String filename = null;
                boolean gotData = false;

                if ("inline-data".equals(data.getAction())) {
                    if (data.getExtras().getParcelable("data") instanceof Bitmap) {
                        Bitmap bitmap = data.getExtras().getParcelable("data");
                        FileOutputStream fout = new FileOutputStream(tempfile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
                        fout.close();
                        filename = "Image-" + ISO8601.now() + ".png";
                        gotData = true;
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();

                    InputStream in = _gs.getContentResolver().openInputStream(uri);
                    OutputStream out = new FileOutputStream(tempfile);
                    misc.copyStream(in, out, 1024, -1, 500);
                    out.close();
                    in.close();
                    gotData = true;

                    if (uri.getScheme().equals("file")) {
                        filename = uri.getLastPathSegment();
                    } else {
                        Cursor c = _gs.getContentResolver().query(uri, null, null,
                                null, null);
                        int nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        c.moveToFirst();
                        filename = c.getString(nameIndex);
                        c.close();
                    }

                }

                if (!gotData) {
                    Toast.makeText(getActivity(), "Could not get image data.", Toast.LENGTH_LONG).show();
                    return;
                }

                _gs.startService(_service.uploadDeliverable(
                        WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
                        _uploadingSlot.getSlotId(), filename,
                        tempfile, getNotificationIntent()));

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private ClosingNotesDialog.Listener _closingNotes_onOk = new ClosingNotesDialog.Listener() {
        @Override
        public void onOk(String message) {
            getActivity().startService(_service.closingNotes(WEB_CHANGE, _workorder.getWorkorderId(), message));
        }

        @Override
        public void onCancel() {
        }
    };

    private DeviceCountDialog.Listener _deviceCountListener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            getActivity().startService(
                    _service.checkout(WEB_CHANGE, _workorder.getWorkorderId(), count));
            setLoading(true);
        }
    };

    private ActionBarTopView.Listener _actionbartop_listener = new ActionBarTopView.Listener() {
        @Override
        public void onComplete() {
            getActivity().startService(
                    _service.complete(WEB_CHANGE, _workorder.getWorkorderId()));
            setLoading(true);
        }

        @Override
        public void onCheckOut() {
            Pay pay = _workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(_workorder, pay.getMaxDevice());
            } else {
                getActivity().startService(
                        _service.checkout(WEB_CHANGE, _workorder.getWorkorderId()));
                setLoading(true);
            }
        }

        @Override
        public void onCheckIn() {
            getActivity().startService(
                    _service.checkin(WEB_CHANGE, _workorder.getWorkorderId()));
            setLoading(true);
        }

        @Override
        public void onAcknowledge() {
            getActivity().startService(
                    _service.acknowledgeHold(WEB_CHANGE,
                            _workorder.getWorkorderId()));
            setLoading(true);
        }

        @Override
        public void onConfirm() {
            _confirmDialog.show(_workorder, _workorder.getSchedule());
        }

        @Override
        public void onEnterClosingNotes() {
            _closingDialog.show(_workorder.getClosingNotes());
        }
    };

    private ConfirmDialog.Listener _confirmDialog_listener = new ConfirmDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            try {
                long end = durationMilliseconds
                        + ISO8601.toUtc(startDate);
                Intent intent = _service.confirmAssignment(
                        WEB_CHANGE,
                        _workorder.getWorkorderId(), startDate,
                        ISO8601.fromUTC(end));
                getActivity().startService(intent);
                setLoading(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onCancel(Workorder workorder) {
        }

        @Override
        public void termsOnClick(Workorder workorder) {
            _termsDialog.show();
        }

    };

    private AppPickerDialog.Listener _appdialog_listener = new AppPickerDialog.Listener() {

        @Override
        public void onClick(AppPickerPackage pack) {
            Intent src = pack.intent;

            ResolveInfo info = pack.resolveInfo;

            src.setComponent(new ComponentName(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name));

            if (src.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                startActivityForResult(src, RESULT_CODE_GET_ATTACHMENT);
            } else {
                startActivityForResult(src, RESULT_CODE_GET_CAMERA_PIC);
            }
        }
    };

    private UploadedDocumentView.Listener _uploaded_document_listener = new UploadedDocumentView.Listener() {
        @Override
        public void onDelete(UploadedDocumentView v, UploadedDocument document) {
            _deleteCount++;
            // startLoading();
            _gs.startService(_service.deleteDeliverable(WEB_DELETE_DELIVERABLE,
                    _workorder.getWorkorderId(),
                    document.getWorkorderUploadId()));
        }
    };

    private UploadSlotView.Listener _uploadSlot_listener = new UploadSlotView.Listener() {
        @Override
        public void onUploadClick(UploadSlotView view, UploadSlot slot) {
            if (checkMedia()) {
                _uploadCount++;
                _uploadingSlot = slot;
                _uploadingSlotView = view;
                _appPickerDialog.show();
            } else {
                Toast.makeText(
                        getActivity(),
                        "Need External Storage, pleaswe insert storage device before continuing",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    // Web
    private AuthenticationClient _authClient = new AuthenticationClient() {
        @Override
        public void onAuthentication(String username, String authToken) {
            _service = new WorkorderService(_gs, username, authToken,
                    _resultReceiver);
            _profileService = new ProfileService(_gs, username, authToken,
                    _resultReceiver);
            getData();
        }

        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(
            new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            // stopLoading();
            // TODO Method Stub: onSuccess()
            Log.v(TAG, "Method Stub: onSuccess()");
            if (resultCode == WEB_GET_PROFILE) {
                _profile = null;
                try {
                    _profile = Profile
                            .fromJson(new JsonObject(
                                    new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA))));
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

                setLoading(false);
            } else if (resultCode == WEB_CHANGE) {
                _workorder.dispatchOnChange();
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_service != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());
            } else if (_profileService != null) {
                _gs.invalidateAuthToken(_profileService.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authClient);
            _service = null;
            _profileService = null;
            Toast.makeText(getActivity(), "Could not complete request", Toast.LENGTH_LONG).show();
            setLoading(false);
        }
    };

    @Override
    public void doAction(Bundle bundle) {
        _delayedAction = bundle;
        executeDelayedAction();
    }
}
