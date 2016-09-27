package com.fieldnation.ui.share;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadingDocument;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.RefreshView;

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

    // Data
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

        _uploadSlotTextView = (TextView) findViewById(R.id.uploadSlot_textview);

        _fileLayout = (LinearLayout) findViewById(R.id.fileList);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setData(WorkOrder workOrder, UploadSlot slot, UploadingDocument[] files) {

    }

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.send_menuitem) {
                ToastClient.toast(App.get(), "Send", Toast.LENGTH_LONG);
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

    public interface Listener {
        void onBackPressed();
    }
}
