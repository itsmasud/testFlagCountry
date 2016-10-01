package com.fieldnation.ui.share;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by shoaib.ahemd on Oct 07, 2015.
 */
public class FileRowView extends RelativeLayout {
    private static final String TAG = "FileRowView";

    // Ui
    private TextView _titleTextView;
    private IconFontTextView _iconView;
    private RelativeLayout _Layout;


    // Data
    private SharedFile _sharedFile;
    private Listener _listener;
    private boolean _checked = true;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public FileRowView(Context context) {
        super(context);
        init();
    }

    public FileRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_share_file_row, this);

        if (isInEditMode())
            return;

        _Layout = (RelativeLayout) findViewById(R.id.layoutRoot);
        _Layout.setOnClickListener(_shareUploadSlot_onClick);

        _titleTextView = (TextView) findViewById(R.id.name_textview);
        _titleTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
        _iconView = (IconFontTextView) findViewById(R.id.icon_view);

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public SharedFile getSharedFile() {
        return _sharedFile;
    }

    public void changeCheckStatus() {
        _checked = !_checked;
        populateUi();
    }

    public void setData(SharedFile sharedFile) {
        Log.v(TAG, "setData");
        _sharedFile = sharedFile;
        populateUi();
    }

    public boolean isChecked() {
        return _checked;
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_sharedFile == null)
            return;

        _titleTextView.setText(_sharedFile.getFileName());

        if (_checked) {
            _iconView.setText(getResources().getString(R.string.icon_task_done));
            _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
            _titleTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
        } else {
            _iconView.setText(getResources().getString(R.string.icon_task));
            _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
            _titleTextView.setTextColor(getResources().getColor(R.color.fn_light_text));
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _shareUploadSlot_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClick(FileRowView.this, _sharedFile);
        }
    };


    public interface Listener {
        void onClick(FileRowView view, SharedFile sharedFile);
    }
}
