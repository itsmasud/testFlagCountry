package com.fieldnation.ui.share;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.v2.data.model.AttachmentFolder;

public class UploadSlotView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("UploadSlotView");

    // Ui
    private TextView _titleTextView;
    private IconFontTextView _iconView;
    private RelativeLayout _Layout;

    // Data
    private AttachmentFolder _slot;
    private Listener _listener;
    boolean _checked;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public UploadSlotView(Context context) {
        super(context);
        init();
    }

    public UploadSlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UploadSlotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_share_upload_slot_row, this);

        if (isInEditMode())
            return;

        _Layout = findViewById(R.id.layoutRoot);
        _Layout.setOnClickListener(_shareUploadSlot_onClick);

        _titleTextView = findViewById(R.id.name_textview);
        _titleTextView.setTextColor(getResources().getColor(R.color.fn_light_text));
        _iconView = findViewById(R.id.icon_view);

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public boolean isChecked() {
        return _checked;
    }

    public void changeCheckStatus() {
        if (!_checked) {
            _iconView.setText(getResources().getString(R.string.icon_task_done));
            _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
            _titleTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
            _checked = true;
        } else {
            _iconView.setText(getResources().getString(R.string.icon_task));
            _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
            _titleTextView.setTextColor(getResources().getColor(R.color.fn_light_text));
            _checked = false;
        }
    }


    public void setData(AttachmentFolder slot) {
        Log.v(TAG, "setData");

        _slot = slot;
        populateUi();
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_slot == null)
            return;

        _titleTextView.setText(_slot.getName().toUpperCase());
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _shareUploadSlot_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClick(UploadSlotView.this, _slot);
        }
    };

    public interface Listener {
        void onClick(UploadSlotView view, AttachmentFolder slot);
    }
}
