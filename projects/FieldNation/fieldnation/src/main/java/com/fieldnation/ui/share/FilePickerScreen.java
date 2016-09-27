package com.fieldnation.ui.share;

import android.content.Context;
import android.os.Handler;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadingDocument;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.ui.RefreshView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 9/27/2016.
 */

public class FilePickerScreen extends FrameLayout {
    private static final String TAG = "FilePickerScreen";

    // UI
    private Toolbar _toolbar;
    private TextView _uploadSlotTextView;
    private LinearLayout _fileLayout;
    private RefreshView _refreshView;
    private ActionMenuItemView _sendMenu;

    // Data
    private WorkOrder _workOrder;
    private UploadSlot _slot;
    private UploadingDocument[] _files;
    private Listener _listener;

    public FilePickerScreen(Context context) {
        super(context);
        init();
    }

    public FilePickerScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FilePickerScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_share_file_picker, this);

        if (isInEditMode())
            return;

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle("Select files");
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _toolbar.inflateMenu(R.menu.share);
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _sendMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.send_menuitem);


        _uploadSlotTextView = (TextView) findViewById(R.id.uploadSlot_textview);

        _fileLayout = (LinearLayout) findViewById(R.id.fileList);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setData(WorkOrder workOrder, UploadSlot slot, UploadingDocument[] files) {
        _workOrder = workOrder;
        _slot = slot;
        _files = files;
        populateUi();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                _refreshView.startRefreshing();
            }
        }, 100);
    }

    private void populateMenu() {
        int selectedFileNumber = 0;
        for (int i = 0; i < _fileLayout.getChildCount(); i++) {
            final ShareRequestedFileRowView row = (ShareRequestedFileRowView) _fileLayout.getChildAt(i);
            if (row.isChecked()) {
                selectedFileNumber++;
            }
        }

        if (selectedFileNumber > 0) {
            _sendMenu.setEnabled(true);
            _sendMenu.setTitle("Send (" + selectedFileNumber + ")");
        } else {
            _sendMenu.setTitle("");
            _sendMenu.setEnabled(false);
        }
    }

    private void populateUi() {
        if (_toolbar == null || _workOrder == null)
            return;

        _uploadSlotTextView.setText(_slot.getSlotName());

        Runnable r = new ForLoopRunnable(_files.length, new Handler()) {
            private final List<ShareRequestedFileRowView> _views = new LinkedList<>();

            @Override
            public void next(int i) throws Exception {
                ShareRequestedFileRowView v = new ShareRequestedFileRowView(getContext());
                v.setData(_files[i]);
                v.setListener(_shareRequestedFileRowView_listener);
                _views.add(v);
            }

            @Override
            public void finish(int count) throws Exception {
                _fileLayout.removeAllViews();
                for (ShareRequestedFileRowView v : _views) {
                    _fileLayout.addView(v);
                }

                _refreshView.refreshComplete();

                populateMenu();
            }
        };
        post(r);
        populateMenu();
    }

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // TODO, do a thing
            if (item.getItemId() == R.id.send_menuitem) {
                Log.d(TAG, "onMenuItemClick");

                List<UploadingDocument> list = new LinkedList<>();
                for (int i = 0; i < _fileLayout.getChildCount(); i++) {
                    final ShareRequestedFileRowView row = (ShareRequestedFileRowView) _fileLayout.getChildAt(i);
                    if (row.isChecked() && row.getUploadingDocument() != null) {
                        list.add(row.getUploadingDocument());
                    }
                }
                if (_listener != null)
                    _listener.onSendFiles(list.toArray(new UploadingDocument[list.size()]));
                return true;
            }
            return false;
        }
    };

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onBackPressed();
        }
    };

    private final ShareRequestedFileRowView.Listener _shareRequestedFileRowView_listener = new ShareRequestedFileRowView.Listener() {
        @Override
        public void onClick(ShareRequestedFileRowView view, UploadingDocument uploadingDocument) {
            Log.v(TAG, "_shareRequestedFileRowView_listener.onClick");
            view.changeCheckStatus();

            populateMenu();
        }
    };


    public interface Listener {
        void onBackPressed();

        void onSendFiles(UploadingDocument[] files);
    }
}
