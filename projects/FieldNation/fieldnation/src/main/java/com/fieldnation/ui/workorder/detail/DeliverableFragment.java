package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.fieldnation.Debug;
import com.fieldnation.ForLoopRunnable;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.UploadSlotDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.Stopwatch;
import com.fieldnation.utils.misc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class DeliverableFragment extends WorkorderFragment {
    private final String TAG = UniqueTag.makeTag("DeliverableFragment");

    // activity result codes
    private static final int RESULT_CODE_BASE = 100;
    private static final int RESULT_CODE_GET_ATTACHMENT = RESULT_CODE_BASE + 1;
    private static final int RESULT_CODE_GET_CAMERA_PIC = RESULT_CODE_BASE + 2;


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
    private AppPickerDialog _appPickerDialog;
    private UploadSlotDialog _uploadSlotDialog;

    // Dialog
    private TwoButtonDialog _yesNoDialog;

    // Data
    private Workorder _workorder;
    private GlobalTopicClient _globalClient;
    private Profile _profile = null;
    //private Bundle _delayedAction = null;
    private final SecureRandom _rand = new SecureRandom();
    private int _uploadingSlotId = -1;
    private File _tempFile;
    private DocumentClient _docClient;
    private PhotoClient _photoClient;
    private static Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();
    private ForLoopRunnable _filesRunnable = null;
    private ForLoopRunnable _reviewRunnable = null;

    // Temporary storage
    private List<Runnable> _untilAdded = new LinkedList<>();

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

        _uploadSlotDialog = UploadSlotDialog.getInstance(getFragmentManager(), TAG);

        _actionButton = (Button) view.findViewById(R.id.action_button);
        _actionButton.setOnClickListener(_actionButton_onClick);

        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);

        checkMedia();

        populateUi();
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        _appPickerDialog.addIntent(getActivity().getPackageManager(), intent, "Get Content");

        Log.e(TAG, "onResume");
        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(App.get());

        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _appPickerDialog.addIntent(getActivity().getPackageManager(), intent, "Take Picture");
        }
    }

    @Override
    public void onPause() {
        if (_photoClient != null && _photoClient.isConnected())
            _photoClient.disconnect(App.get());
        super.onPause();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _globalClient = new GlobalTopicClient(_globalClient_listener);
        _globalClient.connect(App.get());

        _docClient = new DocumentClient(_documentClient_listener);
        _docClient.connect(App.get());

        while (_untilAdded.size() > 0) {
            _untilAdded.remove(0).run();
        }
    }

    @Override
    public void onDetach() {
        if (_globalClient != null && _globalClient.isConnected())
            _globalClient.disconnect(App.get());

        if (_docClient != null && _docClient.isConnected())
            _docClient.disconnect(App.get());
        super.onDetach();
    }

    private boolean checkMedia() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
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
        if (getActivity() == null)
            return null;
        Intent intent = new Intent(getActivity(), WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DELIVERABLES);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
        return PendingIntent.getActivity(getActivity(), _rand.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

        if (_workorder == null)
            return;

        if (_profile == null)
            return;

        if (getActivity() == null)
            return;

        if (_workorder.canChangeDeliverables()) {
            _actionButton.setVisibility(View.VISIBLE);
        } else {
            _actionButton.setVisibility(View.GONE);
        }

        Stopwatch stopwatch = new Stopwatch(true);
        final Document[] docs = _workorder.getDocuments();
        if (docs != null && docs.length > 0) {
            if (_reviewRunnable != null)
                _reviewRunnable.cancel();

            _reviewRunnable = new ForLoopRunnable(docs.length, new Handler()) {
                private final Document[] _docs = docs;
                private List<DocumentView> _views = new LinkedList<>();

                @Override
                public void next(int i) throws Exception {
                    DocumentView v = new DocumentView(getActivity());
                    Document doc = _docs[i];
                    v.setListener(_document_listener);
                    v.setData(_workorder, doc);
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
        } else {
            _reviewList.removeAllViews();
            _noDocsTextView.setVisibility(View.VISIBLE);
        }
        Log.v(TAG, "pop docs time " + stopwatch.finish());

        stopwatch.start();

        final UploadSlot[] slots = _workorder.getUploadSlots();
        if (slots != null && slots.length > 0) {
            Log.v(TAG, "US count: " + slots.length);

            if (_filesRunnable != null)
                _filesRunnable.cancel();

            _filesRunnable = new ForLoopRunnable(slots.length, new Handler()) {
                private final UploadSlot[] _slots = slots;
                private List<UploadSlotView> _views = new LinkedList<>();

                @Override
                public void next(int i) throws Exception {
                    UploadSlotView v = new UploadSlotView(getActivity());
                    UploadSlot slot = _slots[i];
                    v.setData(_workorder, _profile.getUserId(), slot, _uploaded_document_listener);
                    _views.add(v);
                }

                @Override
                public void finish(int count) throws Exception {
                    _filesLayout.removeAllViews();
                    for (UploadSlotView v : _views) {
                        _filesLayout.addView(v);
                    }
                }
            };
            _filesLayout.postDelayed(_filesRunnable, 100);
        } else {
            _filesLayout.removeAllViews();
        }
        Log.v(TAG, "upload docs time " + stopwatch.finish());

        setLoading(false);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (!isAdded()) {
            _untilAdded.add(new Runnable() {
                @Override
                public void run() {
                    onActivityResult(requestCode, resultCode, data);
                }
            });
            return;
        }

        try {
            Log.v(TAG, "onActivityResult() resultCode= " + resultCode);

            if ((requestCode == RESULT_CODE_GET_ATTACHMENT
                    || requestCode == RESULT_CODE_GET_CAMERA_PIC)
                    && resultCode == Activity.RESULT_OK) {

                setLoading(true);

                if (data == null) {
                    Log.v(TAG, "local path");
                    WorkorderClient.uploadDeliverable(getActivity(), _workorder.getWorkorderId(),
                            _uploadingSlotId, _tempFile.getName(), _tempFile.getAbsolutePath());

                } else {
                    Log.v(TAG, "from intent");
                    WorkorderClient.uploadDeliverable(getActivity(), _workorder.getWorkorderId(),
                            _uploadingSlotId, data);
                }
            }
        } catch (Exception ex) {
            Debug.logException(ex);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onActivityResult(requestCode, resultCode, data);
                }
            }, 100);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _actionButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "slots: " + _workorder.getUploadSlots().length);

            if (_workorder.getUploadSlots().length > 1) {
                _uploadSlotDialog.setUploadSlots(_workorder.getUploadSlots());
                _uploadSlotDialog.setListener(new UploadSlotDialog.Listener() {
                    @Override
                    public void onItemClick(int position) {
                        UploadSlot slot = _workorder.getUploadSlots()[position];
                        if (checkMedia()) {
                            // start of the upload process
                            _uploadingSlotId = slot.getSlotId();
                            _appPickerDialog.show();
                        } else if (getActivity() != null) {
                            Toast.makeText(getActivity(),
                                    "Need External Storage, please insert storage device before continuing",
                                    Toast.LENGTH_LONG).show();
                        }
                        _uploadSlotDialog.dismiss();
                    }
                });
                _uploadSlotDialog.show();
            } else {
                UploadSlot slot = _workorder.getUploadSlots()[0];
                if (checkMedia()) {
                    // start of the upload process
                    _uploadingSlotId = slot.getSlotId();
                    _appPickerDialog.show();
                } else if (getActivity() != null) {
                    Toast.makeText(getActivity(),
                            "Need External Storage, please insert storage device before continuing",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    };

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
            final int documentId = document.getId();
            _yesNoDialog.setData("Delete File", "Are you sure you want to delete this file?", "YES", "NO",
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkorderClient.deleteDeliverable(getActivity(), _workorder.getWorkorderId(), documentId);
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
            if (_picCache.containsKey(url) && _picCache.get(url).get() != null) {
                return _picCache.get(url).get();
            } else {
                _photoClient.subGet(url, circle, false);
                PhotoClient.get(App.get(), url, circle, false);
            }
            return null;
        }
    };

    private final DocumentView.Listener _document_listener = new DocumentView.Listener() {
        @Override
        public Drawable getPhoto(DocumentView view, String url, boolean circle) {
            if (_picCache.containsKey(url) && _picCache.get(url).get() != null) {
                return _picCache.get(url).get();
            } else {
                _photoClient.subGet(url, circle, false);
                PhotoClient.get(App.get(), url, circle, false);
            }
            return null;
        }
    };


    // step 1, user clicks on a upload - done elseware?
    // step 2, user selects an app to load the file with
    private final AppPickerDialog.Listener _appdialog_listener = new AppPickerDialog.Listener() {

        @Override
        public void onClick(AppPickerPackage pack) {
            Intent src = pack.intent;

            ResolveInfo info = pack.resolveInfo;

            src.setComponent(new ComponentName(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name));

            if (src.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                Log.v(TAG, "onClick: " + src.toString());
                startActivityForResult(src, RESULT_CODE_GET_ATTACHMENT);
            } else {
                File temppath = new File(App.get().getDownloadsFolder() + "/IMAGE-"
                        + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                _tempFile = temppath;
                Log.v(TAG, "onClick: " + temppath.getAbsolutePath());

                src.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
                startActivityForResult(src, RESULT_CODE_GET_CAMERA_PIC);
            }
        }
    };


    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private final GlobalTopicClient.Listener _globalClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.subGotProfile();
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            populateUi();
        }
    };

    private final DocumentClient.Listener _documentClient_listener = new DocumentClient.Listener() {
        @Override
        public void onConnected() {
            _docClient.subDocument();
        }

        @Override
        public void onDownload(long documentId, final File file, int state) {
            if (file == null || state == DocumentConstants.PARAM_STATE_START) {
                if (state == DocumentConstants.PARAM_STATE_FINISH)
                    ToastClient.toast(App.get(), "Couldn't download file", Toast.LENGTH_SHORT);
                return;
            }

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), App.guessContentTypeFromName(file.getName()));

                if (intent.resolveActivity(App.get().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    String name = file.getName();
                    name = name.substring(name.indexOf("_") + 1);

                    Intent folderIntent = new Intent(Intent.ACTION_VIEW);
                    folderIntent.setDataAndType(Uri.fromFile(new File(App.get().getDownloadsFolder())), "resource/folder");
                    if (folderIntent.resolveActivity(App.get().getPackageManager()) != null) {
                        PendingIntent pendingIntent = PendingIntent.getActivity(App.get(), 0, folderIntent, 0);
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
            _picCache.put(url, new WeakReference<>((Drawable) drawable));

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
}
