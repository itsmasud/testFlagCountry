package com.fieldnation.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.ForLoopRunnable;
import com.fieldnation.GpsLocationService;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadingDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderCardView;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shoaib.ahmed on Sept/08/2015.
 */
public class ShareRequestActivity extends AuthFragmentActivity {
    private static final String TAG = "ShareRequestActivity";

    private static final String STATE_LAYOUT = "STATE_LAYOUT";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_CURRENT_UPLOAD_SLOT = "STATE_CURRENT_UPLOAD_SLOT";
    private static final String STATE_UPLAODING_DOCS = "STATE_UPLAODING_DOCS";

    // UI
    private OverScrollListView _workorderListView;
    private OverScrollView _uploadSlotScrollView;
    private TextView _titleWorkorderTextView;
    private LinearLayout _uploadSlotLayout;

    private OverScrollView _sharedFilesScrollView;
    private LinearLayout _sharedFilesLayout;
    private TextView _titleTaskTextView;
    private TextView _maxFilesNumberTextView;

    private OverScrollListView _fileList;
    private RefreshView _refreshView;
    private UnavailableCardView _emptyView;

    private ActionBarDrawerView _actionBarView;
    private Toolbar _toolbar;

    // Data
    private WorkorderClient _workorderClient;
    private GpsLocationService _gpsLocationService;
    private ActionMenuItemView _sendMenuItem;
    private WorkorderCardView _currentWorkOrderCardView = null;

    // Data that Needs to be saved
    private LayoutType layoutType;
    private Workorder _workorder;
    private UploadSlot _currentUploadSlot;
    private UploadingDocument[] _uploadingDocumentList;

    // State data
    private WorkorderDataSelector _displayView = WorkorderDataSelector.ASSIGNED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_request);

        layoutType = LayoutType.WORKORDER_LAYOUT;

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_LAYOUT)) {
                layoutType = LayoutType.values()[savedInstanceState.getInt(STATE_LAYOUT)];
            }
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
            if (savedInstanceState.containsKey(STATE_CURRENT_UPLOAD_SLOT)) {
                _currentUploadSlot = savedInstanceState.getParcelable(STATE_CURRENT_UPLOAD_SLOT);
            }
            if (savedInstanceState.containsKey(STATE_UPLAODING_DOCS)) {
                _uploadingDocumentList = (UploadingDocument[]) savedInstanceState.getParcelableArray(STATE_UPLAODING_DOCS);
            }
        }

        _actionBarView = (ActionBarDrawerView) findViewById(R.id.actionbardrawerview);

        _toolbar = _actionBarView.getToolbar();
        _toolbar.setTitleTextColor(Color.WHITE);
        _toolbar.setSubtitleTextColor(Color.WHITE);
        _toolbar.setTitle(R.string.activity_share_request_title_workorder);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setNavigationOnClickListener(_toolbarNavication_listener);
        _toolbar.setOnMenuItemClickListener(_sendMenuItem_listener);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _adapter.setOnLoadingCompleteListener(_adapterListener);

        _workorderListView = (OverScrollListView) findViewById(R.id.workorders_listview);
        _workorderListView.setDivider(null);
        _workorderListView.setOnOverScrollListener(_refreshView);
        _workorderListView.setAdapter(_adapter);

        _uploadSlotScrollView = (OverScrollView) findViewById(R.id.uploadSolt_scrollView);
        _uploadSlotScrollView.setOnOverScrollListener(_refreshView);
        _uploadSlotLayout = (LinearLayout) findViewById(R.id.uploadSlot_layout);
        _titleWorkorderTextView = (TextView) findViewById(R.id.titleWorkorder_textview);

        _sharedFilesScrollView = (OverScrollView) findViewById(R.id.sharedFiles_scrollView);
        _sharedFilesScrollView.setOnOverScrollListener(_refreshView);
        _sharedFilesLayout = (LinearLayout) findViewById(R.id.filesList_layout);
        _titleTaskTextView = (TextView) findViewById(R.id.titleTask_textview);
        _maxFilesNumberTextView = (TextView) findViewById(R.id.maxFilesNumber_textview);

        _emptyView = (UnavailableCardView) findViewById(R.id.empty_view);

        _workorderClient = new WorkorderClient(_workorderData_listener);
        _workorderClient.connect(App.get());

        populateUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_LAYOUT, layoutType.ordinal());

        if (_workorder != null) {
            outState.putParcelable(STATE_WORKORDER, _workorder);
        }

        if (_currentUploadSlot != null) {
            outState.putParcelable(STATE_CURRENT_UPLOAD_SLOT, _currentUploadSlot);
        }

        if (_uploadingDocumentList != null && _uploadingDocumentList.length > 0) {
            outState.putParcelableArray(STATE_UPLAODING_DOCS, _uploadingDocumentList);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");
        switch (LayoutType.fromString(layoutType.getCurrentLayoutValue())) {
            case WORKORDER_LAYOUT:
                finish();
                break;

            case UPLOAD_SLOT_LAYOUT:
                layoutType = LayoutType.WORKORDER_LAYOUT;
                populateUi();
                break;

            case SHARED_FILES_LAYOUT:
                layoutType = LayoutType.UPLOAD_SLOT_LAYOUT;
                populateUi();
                break;
        }
    }

    private void populateUi() {
        if (_workorderListView == null)
            return;

        switch (layoutType) {
            case WORKORDER_LAYOUT:
                _toolbar.setTitle(R.string.activity_share_request_title_workorder);
                _toolbar.getMenu().clear();
                _workorderListView.setVisibility(View.VISIBLE);
                _uploadSlotScrollView.setVisibility(View.GONE);
                _sharedFilesScrollView.setVisibility(View.GONE);
                break;
            case UPLOAD_SLOT_LAYOUT:
                _toolbar.setTitle(R.string.activity_share_request_title_task);
                _toolbar.getMenu().clear();
                _workorderListView.setVisibility(View.GONE);
                _uploadSlotScrollView.setVisibility(View.VISIBLE);
                _sharedFilesScrollView.setVisibility(View.GONE);
                populateUploadSlotLayout();
                break;
            case SHARED_FILES_LAYOUT:
                _workorderListView.setVisibility(View.GONE);
                _uploadSlotScrollView.setVisibility(View.GONE);
                _sharedFilesScrollView.setVisibility(View.VISIBLE);
                populateSharedFilesLayout();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.v(TAG, intent.toString());

        setLoading(true);

        if (action.equals(Intent.ACTION_SEND_MULTIPLE)) {
            handleRequestMultipleFiles(intent);
        } else if (action.equals(Intent.ACTION_SEND)) {
            handleRequestSingleFile(intent);
        }
        _gpsLocationService = new GpsLocationService(this);
    }

    private void handleRequestSingleFile(Intent intent) {
        Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        _uploadingDocumentList = new UploadingDocument[1];
        if (fileUri != null) {
            final String fileName = getFileNameFromUri(fileUri);
            _uploadingDocumentList[0] = new UploadingDocument(fileName, fileUri);
        }
    }

    private void handleRequestMultipleFiles(Intent intent) {
        Log.v(TAG, intent.getExtras() + "");

        ArrayList<Uri> fileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        _uploadingDocumentList = new UploadingDocument[fileUris.size()];

        if (fileUris != null) {
            for (int i = 0; i < fileUris.size(); i++) {
                Log.v(TAG, "uris:" + fileUris.get(i));
            }

            for (int i = 0; i < fileUris.size(); i++) {
                final String fileName = getFileNameFromUri(fileUris.get(i));
                _uploadingDocumentList[i] = new UploadingDocument(fileName, fileUris.get(i));
            }
        }
    }

    private String getFileNameFromUri(final Uri uri) {
        String fileName = "";

        if (uri.getScheme().toString().compareTo("content") == 0) {
            final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {

                Log.v(TAG, "Columns");
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.v(TAG, i + "\\" + cursor.getColumnName(i) + "\\" + cursor.getString(i));
                }

                int column_index = -1;
                try {
                    column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

                } catch (Exception ex) {
                    try {
                        column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    } catch (Exception ex2) {
                        ex2.printStackTrace();
                    }
                }

                final Uri filePathUri = Uri.parse(cursor.getString(column_index));
                fileName = filePathUri.getLastPathSegment().toString();
                Log.e(TAG, "getFilePathFromIntent: fileName: " + fileName);
            }
        } else if (uri.getScheme().toString().compareTo("file") == 0) {
            fileName = new File(uri.getPath()).getName();
        }

        return fileName;
    }

    @Override
    protected void onPause() {
        if (_gpsLocationService != null)
            _gpsLocationService.stopLocationUpdates();

        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setLoading(boolean loading) {
        Log.v(TAG, "setLoading(" + loading + ")");
        if (_refreshView != null) {
            if (loading) {
                _refreshView.startRefreshing();
            } else {
                _refreshView.refreshComplete();
            }
        }
    }

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            if (_workorder != null) {
                _workorder.dispatchOnChange();
            }
        }
    };

    private void recreateToolBar() {
        int selectedFileNumber = 0;
        for (int i = 0; i < _sharedFilesLayout.getChildCount(); i++) {
            final ShareRequestedFileRowView row = (ShareRequestedFileRowView) _sharedFilesLayout.getChildAt(i);
            if (row.isChecked()) {
                ++selectedFileNumber;
            }
        }

        if (selectedFileNumber > 0) {
            _toolbar.getMenu().clear();
            _toolbar.inflateMenu(R.menu.share_menu);
            _sendMenuItem = (ActionMenuItemView) findViewById(R.id.send_menuitem);
            _sendMenuItem.setTextColor(Color.WHITE);
            _sendMenuItem.setTitle("Send (" + selectedFileNumber + ")");
        } else {
            _toolbar.getMenu().clear();
        }
    }

    private Toolbar.OnMenuItemClickListener _sendMenuItem_listener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.send_menuitem) {
                Log.d(TAG, "onMenuItemClick");

                for (int i = 0; i < _sharedFilesLayout.getChildCount(); i++) {
                    final ShareRequestedFileRowView row = (ShareRequestedFileRowView) _sharedFilesLayout.getChildAt(i);
                    if (row.isChecked()) {
                        WorkorderClient.uploadDeliverable(ShareRequestActivity.this, _workorder.getWorkorderId(),
                                _currentUploadSlot.getSlotId(), row.getUploadingDocument().getFileName(), row.getUploadingDocument().getUri());
                    }
                }

                Intent intent = new Intent(ShareRequestActivity.this, WorkorderActivity.class);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
                intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DELIVERABLES);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            return false;
        }
    };

    private View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    /*-************************************************-*/
    /*-             Work Order Select Data             -*/
    /*-************************************************-*/
    private void requestList(int page, boolean allowCache) {
        Log.v(TAG, "requestList " + page);
        if (page == 0)
            setLoading(true);
        WorkorderClient.list(App.get(), _displayView, page, false, allowCache);
    }

    private final WorkorderClient.Listener _workorderData_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderData_listener.onConnected");
            _workorderClient.subList(_displayView);
            _workorderClient.subGet(false);
            _workorderClient.subActions();
            _workorderClient.subDeliverableUpload();
            _adapter.refreshPages();
        }

        @Override
        public void onList(List<Workorder> list, WorkorderDataSelector selector, int page, boolean failed, boolean isCached) {
            Log.v(TAG, "_workorderData_listener.onList");
            if (!selector.equals(_displayView))
                return;
            if (list != null) {
                addPage(page, list);
            }
        }

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            Log.v(TAG, "_workorderData_listener.onAction " + workorderId + "/" + action);
            _adapter.refreshPages();
        }
    };

    private final PagingAdapter<Workorder> _adapter = new PagingAdapter<Workorder>() {
        @Override
        public View getView(Workorder object, View convertView, ViewGroup parent) {
            WorkorderCardView v = null;
            if (convertView == null) {
                v = new WorkorderCardView(parent.getContext());
            } else if (convertView instanceof WorkorderCardView) {
                v = (WorkorderCardView) convertView;
            } else {
                v = new WorkorderCardView(parent.getContext());
            }

            if (_gpsLocationService != null && _gpsLocationService.getLocation() != null) {
                v.setWorkorder(object, _gpsLocationService.getLocation());
            } else {
                v.setWorkorder(object, null);
            }
            v.setWorkorderSummaryListener(_wocv_listener);

            v.setDisplayMode(WorkorderCardView.MODE_NORMAL);
            v.makeButtonsGone();

            return v;
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            requestList(page, allowCache);
        }
    };

    private final PagingAdapter.OnLoadingCompleteListener _adapterListener = new PagingAdapter.OnLoadingCompleteListener() {
        @Override
        public void onLoadingComplete() {
            setLoading(false);
        }
    };

    private void addPage(int page, List<Workorder> list) {
        List<Workorder> workorderListWithoutOnHoldWorkorder = new ArrayList<>();

        WorkorderStatus status;
        WorkorderSubstatus substatus;

        for (Workorder workorder : list) {
            status = workorder.getWorkorderStatus();
            substatus = workorder.getWorkorderSubstatus();

            if (status == WorkorderStatus.ASSIGNED
                    || status == WorkorderStatus.INPROGRESS) {
                if (!(substatus == WorkorderSubstatus.ONHOLD_ACKNOWLEDGED
                        || substatus == WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED
                        || substatus == WorkorderSubstatus.UNCONFIRMED)) {
                    workorderListWithoutOnHoldWorkorder.add(workorder);
                }
            }
        }

        if (page == 0 && workorderListWithoutOnHoldWorkorder.size() == 0 && _displayView.shouldShowGoToMarketplace()) {
            _emptyView.setVisibility(View.VISIBLE);
        } else if (page == 0 && workorderListWithoutOnHoldWorkorder.size() > 0 || !_displayView.shouldShowGoToMarketplace()) {
            _emptyView.setVisibility(View.GONE);
        }

        if (workorderListWithoutOnHoldWorkorder.size() == 0) {
            _adapter.setNoMorePages();
        }

        _adapter.setPage(page, workorderListWithoutOnHoldWorkorder);
    }

    /*-*************************************-*/
    /*-         Work Order Select           -*/
    /*-*************************************-*/
    private final WorkorderCardView.Listener _wocv_listener = new WorkorderCardView.DefaultListener() {
        @Override
        public void onClick(final WorkorderCardView view, Workorder workorder) {
            Log.e(TAG, "onClick_WorkorderCardView");
            _workorder = workorder;
            setLoading(true);
            _currentWorkOrderCardView = view;
            _currentWorkOrderCardView.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
            _currentWorkOrderCardView.makeButtonsGone();

            if (_workorderClient != null && _workorderClient.isConnected())
                _workorderClient.disconnect(App.get());
            _workorderClient = new WorkorderClient(_workorderClient_listener);
            _workorderClient.connect(App.get());
        }
    };

    public void getData(boolean allowCache) {
        Log.v(TAG, "getData");
        setLoading(true);
        WorkorderClient.get(this, _workorder.getWorkorderId(), allowCache);
    }


    /*-*****************************************-*/
    /*-         Upload Slot Select Data         -*/
    /*-*****************************************-*/
    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        private long bounceTimer = 0;
        private long lastWorkorderId = 0;

        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderClient_listener.onConnected " + _workorder.getWorkorderId());
            _workorderClient.subGet(_workorder.getWorkorderId());
            _workorderClient.subActions(_workorder.getWorkorderId());
            _workorderClient.subDeliverableUpload();
            getData(false);
        }

        @Override
        public void onUploadDeliverable(long workorderId, long slotId, String filename, boolean isComplete, boolean failed) {
            Log.v(TAG, "_workorderClient_listener.onUploadDeliverable ");
            if (isComplete)
                getData(false);
        }

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            Log.v(TAG, "_workorderClient_listener.onAction " + workorderId + "/" + action);
            getData(false);
        }

        @Override
        public void onGet(Workorder workorder, boolean failed, boolean isCached) {
            Log.v(TAG, "_workorderClient_listener.onGet");
            if (workorder == null || failed) {
                if (isCached) {
                    WorkorderClient.get(App.get(), _workorder.getWorkorderId(), false);
                } else {
                    try {
                        Toast.makeText(ShareRequestActivity.this, R.string.workorder_no_permission, Toast.LENGTH_LONG).show();
                        finish();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return;
            }

            if (workorder.getWorkorderId() == lastWorkorderId && bounceTimer < System.currentTimeMillis()) {
                return;
            } else {
                lastWorkorderId = workorder.getWorkorderId();
                bounceTimer = System.currentTimeMillis() + 1000;
            }

            _workorder = workorder;
            _workorder.addListener(_workorder_listener);

            // do this so that we don't inadvertantly switch pages
            populateUploadSlotLayout();
        }
    };

    private Workorder.Listener _workorder_listener = new Workorder.Listener() {
        @Override
        public void onChange(Workorder workorder) {
            Log.v(TAG, "_workorder_listener");
            getData(false);
        }
    };

    /*-**************************************-*/
    /*-         Upload Slot Select           -*/
    /*-**************************************-*/
    private void populateUploadSlotLayout() {
        Log.e(TAG, "populateUploadSlotLayout");
        final UploadSlot[] slots = _workorder.getUploadSlots();

        if (slots == null || slots.length == 0) {
            Toast.makeText(this, R.string.cant_upload_to_work_order_no_slots, Toast.LENGTH_LONG).show();
            setLoading(false);
            return;
        }

        layoutType = LayoutType.UPLOAD_SLOT_LAYOUT;
        _toolbar.setTitle(R.string.activity_share_request_title_task);
        _workorderListView.setVisibility(View.GONE);
        _sharedFilesScrollView.setVisibility(View.GONE);

        _titleWorkorderTextView.setText(_workorder.getTitle());

        Log.v(TAG, "US count: " + slots.length);

        ForLoopRunnable r = new ForLoopRunnable(slots.length, new Handler()) {
            private final UploadSlot[] _slots = slots;

            @Override
            public void next(int i) throws Exception {
                Log.v(TAG, "US loop: " + i);

                if (i == 0) {
                    _uploadSlotLayout.removeAllViews();
                }

                ShareUploadSlotView v = null;
                if (i < _uploadSlotLayout.getChildCount()) {
                    v = (ShareUploadSlotView) _uploadSlotLayout.getChildAt(i);
                } else {
                    v = new ShareUploadSlotView(ShareRequestActivity.this);
                    _uploadSlotLayout.addView(v);
                }
                final UploadSlot slot = _slots[i];
                v.setData(slot);
                v.setListener(_shareUploadSlotView_listener);
            }

            @Override
            public void finish(int count) throws Exception {
                setLoading(false);
            }
        };
        _uploadSlotScrollView.setVisibility(View.VISIBLE);
        _uploadSlotLayout.postDelayed(r, new Random().nextInt(100));

        if (_currentWorkOrderCardView != null) {
            _currentWorkOrderCardView.setDisplayMode(WorkorderCardView.MODE_NORMAL);
            _currentWorkOrderCardView.makeButtonsGone();
        }
    }

    private ShareUploadSlotView.Listener _shareUploadSlotView_listener = new ShareUploadSlotView.Listener() {
        public void onClick(ShareUploadSlotView view, UploadSlot slot) {
            Log.e(TAG, "_shareUploadSlotView_listener.onClick" + slot.getSlotName());

            if (view.isChecked()) {
                view.changeCheckStatus();
            }

            _currentUploadSlot = slot;

            layoutType = LayoutType.SHARED_FILES_LAYOUT;
            recreateToolBar();
            populateUi();
        }
    };

    /*-*******************************-*/
    /*-         File Select           -*/
    /*-*******************************-*/
    private void populateSharedFilesLayout() {
        _toolbar.setTitle(R.string.activity_share_request_title_files);

        _uploadSlotScrollView.setVisibility(View.GONE);
        _sharedFilesScrollView.setVisibility(View.VISIBLE);
        _titleTaskTextView.setText(_currentUploadSlot.getSlotName().toUpperCase());

        if (_currentUploadSlot.getMaxFiles() < 1) {
            _maxFilesNumberTextView.setVisibility(View.GONE);
        } else {
            _maxFilesNumberTextView.setText("Select up to " + _currentUploadSlot.getMaxFiles() + " file(s)");
            _maxFilesNumberTextView.setVisibility(View.VISIBLE);
        }

        if (_uploadingDocumentList != null && _uploadingDocumentList.length > 0) {
            Log.e(TAG, "UD count: " + _uploadingDocumentList.length);

            if (_sharedFilesLayout.getChildCount() > _uploadingDocumentList.length) {
                _sharedFilesLayout.removeViews(_uploadingDocumentList.length - 1, _sharedFilesLayout.getChildCount() - _uploadingDocumentList.length);
            }

            ForLoopRunnable r = new ForLoopRunnable(_uploadingDocumentList.length, new Handler()) {
                private final UploadingDocument[] uploadingDocumentList = _uploadingDocumentList;

                @Override
                public void next(int i) throws Exception {
                    Log.v(TAG, "next" + uploadingDocumentList.length);

                    ShareRequestedFileRowView v = null;
                    if (i < _sharedFilesLayout.getChildCount()) {
                        v = (ShareRequestedFileRowView) _sharedFilesLayout.getChildAt(i);
                    } else {
                        v = new ShareRequestedFileRowView(ShareRequestActivity.this);
                        _sharedFilesLayout.addView(v);
                    }
                    final UploadingDocument uploadingDocument = uploadingDocumentList[i];
                    v.setData(uploadingDocument);
                    v.setListener(_shareRequestedFileRowView_listener);
                    recreateToolBar();
                }

                @Override
                public void finish(int count) throws Exception {
                    setLoading(false);
                }
            };
            _sharedFilesLayout.postDelayed(r, new Random().nextInt(1000));
        } else {
            _sharedFilesLayout.removeAllViews();
            setLoading(false);
        }
    }

    private ShareRequestedFileRowView.Listener _shareRequestedFileRowView_listener = new ShareRequestedFileRowView.Listener() {
        @Override
        public void onClick(ShareRequestedFileRowView view, UploadingDocument uploadingDocument) {
            Log.v(TAG, "_shareRequestedFileRowView_listener.onClick");
            view.changeCheckStatus();

            recreateToolBar();
        }
    };

    public enum LayoutType {
        UNKNOWN(null),
        WORKORDER_LAYOUT("workorder"),
        UPLOAD_SLOT_LAYOUT("uploadslot"),
        SHARED_FILES_LAYOUT("sharedfiles");

        private final String _currentLayoutValue;

        LayoutType(String currentLayoutValue) {
            _currentLayoutValue = currentLayoutValue;
        }

        String getCurrentLayoutValue() {
            return _currentLayoutValue;
        }

        public static LayoutType fromString(String type) {
            LayoutType[] vs = values();

            if (type == null) {
                return UNKNOWN;
            }

            for (LayoutType v : vs) {
                if (type.equals(v._currentLayoutValue))
                    return v;
            }

            Log.w(TAG, "invalid LayoutType of " + type + " found!!!");

            return UNKNOWN;
        }
    }
}
