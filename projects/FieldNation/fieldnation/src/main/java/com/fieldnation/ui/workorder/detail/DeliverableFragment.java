package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AsyncTaskEx;
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
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.IconFontButton;
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
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
    private IconFontButton _navigateButton;
    private AppPickerDialog _appPickerDialog;
    private UploadSlotDialog _uploadSlotDialog;

    // Dialog
    private TwoButtonDialog _yesNoDialog;

    // Data
    private Workorder _workorder;
    private WorkorderClient _workorderClient;
    private GlobalTopicClient _globalClient;
    private Profile _profile = null;
    //private Bundle _delayedAction = null;
    private final SecureRandom _rand = new SecureRandom();
    private int _uploadingSlotId = -1;
    private File _tempFile;
    private DocumentClient _docClient;


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

        _navigateButton = (IconFontButton) view.findViewById(R.id.navigate_button);
        _navigateButton.setOnClickListener(_navigationButton_onClick);

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

        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _appPickerDialog.addIntent(getActivity().getPackageManager(), intent, "Take Picture");
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _globalClient = new GlobalTopicClient(_globalClient_listener);
        _globalClient.connect(activity);

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(activity);

        _docClient = new DocumentClient(_documentClient_listener);
        _docClient.connect(activity);

        while (_untilAdded.size() > 0) {
            _untilAdded.remove(0).run();
        }
    }

    @Override
    public void onDetach() {
        _globalClient.disconnect(getActivity());
        _workorderClient.disconnect(getActivity());
        _docClient.disconnect(getActivity());
        super.onDetach();
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
            _navigateButton.setVisibility(View.VISIBLE);
        } else {
            _navigateButton.setVisibility(View.GONE);
        }

        Stopwatch stopwatch = new Stopwatch(true);
        final Document[] docs = _workorder.getDocuments();
        if (docs != null && docs.length > 0) {
            if (_reviewList.getChildCount() > docs.length) {
                _reviewList.removeViews(docs.length - 1, _reviewList.getChildCount() - docs.length);
            }

            ForLoopRunnable r = new ForLoopRunnable(docs.length, new Handler()) {
                private final Document[] _docs = docs;

                @Override
                public void next(int i) throws Exception {
                    DocumentView v = null;
                    if (i < _reviewList.getChildCount()) {
                        v = (DocumentView) _reviewList.getChildAt(i);
                    } else {
                        v = new DocumentView(getActivity());
                        _reviewList.addView(v);
                    }
                    Document doc = _docs[i];
                    v.setData(_workorder, doc);
                }
            };
            _reviewList.postDelayed(r, new Random().nextInt(1000));
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
            if (_filesLayout.getChildCount() > slots.length) {
                _filesLayout.removeViews(slots.length - 1, _filesLayout.getChildCount() - slots.length);
            }

            ForLoopRunnable r = new ForLoopRunnable(slots.length, new Handler()) {
                private final UploadSlot[] _slots = slots;

                @Override
                public void next(int i) throws Exception {
                    UploadSlotView v = null;
                    if (i < _filesLayout.getChildCount()) {
                        v = (UploadSlotView) _filesLayout.getChildAt(i);
                    } else {
                        v = new UploadSlotView(getActivity());
                        _filesLayout.addView(v);
                    }
                    UploadSlot slot = _slots[i];
                    v.setData(_workorder, _profile.getUserId(), slot, _uploaded_document_listener);
                    v.setListener(_uploadSlot_listener);
                }
            };
            _filesLayout.postDelayed(r, new Random().nextInt(1000));
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
    private final View.OnClickListener _navigationButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
                            Toast.makeText(
                                    getActivity(),
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
                    Toast.makeText(
                            getActivity(),
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
            _yesNoDialog.setData("Delete File",
                    "Are you sure you want to delete this file?", "YES", "NO",
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkorderClient.deleteDeliverable(getActivity(), _workorder.getWorkorderId(),
                                    documentId);
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
    };

    // step 1, user taps on the add button
    private final UploadSlotView.Listener _uploadSlot_listener = new UploadSlotView.Listener() {
        @Override
        public void onUploadClick(UploadSlotView view, UploadSlot slot) {
            if (checkMedia()) {
                // start of the upload process
                _uploadingSlotId = slot.getSlotId();
                _appPickerDialog.show();
            } else if (getActivity() != null) {
                Toast.makeText(
                        getActivity(),
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

            if (src.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                Log.v(TAG, "onClick: " + src.toString());
                startActivityForResult(src, RESULT_CODE_GET_ATTACHMENT);
            } else {
                File temppath = new File(App.get().getStoragePath() + "/temp/IMAGE-"
                        + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                _tempFile = temppath;
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
            _globalClient.registerGotProfile();
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            populateUi();
        }
    };

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClient.subDeliverableUpload();
        }

        @Override
        public void onUploadDeliverable(long workorderId, long slotId, String filename, boolean isComplete, boolean failed) {
            WorkorderClient.get(App.get(), workorderId, false);
        }
    };

    private final DocumentClient.Listener _documentClient_listener = new DocumentClient.Listener() {
        @Override
        public void onConnected() {
            _docClient.subDocument();
        }

        @Override
        public void onDownload(long documentId, File file, int state) {
            if (file == null || state == DocumentConstants.PARAM_STATE_START) {
                if (state == DocumentConstants.PARAM_STATE_FINISH)
                    ToastClient.toast(App.get(), "Couldn't download file", Toast.LENGTH_SHORT);
                return;
            }

            // todo, maybe this should be put somewhere else
            new AsyncTaskEx<File, Object, Object>() {
                @Override
                protected Object doInBackground(File... params) {
                    try {
                        File f = params[0];
                        String name = f.getName();
                        name = name.substring(name.indexOf("_") + 1);
                        misc.copyFile(f, new File(App.get().getDownloadsFolder() + "/" + name));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }
            }.executeEx(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), URLConnection.guessContentTypeFromName(file.getName()));

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
        }
    };
}
