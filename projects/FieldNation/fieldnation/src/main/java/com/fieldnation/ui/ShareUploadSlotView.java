package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.workorder.detail.UploadedDocumentView;

import java.util.HashSet;
import java.util.Set;

public class ShareUploadSlotView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("ShareUploadSlotView");

    // Ui
    private TextView _titleTextView;
    private IconFontTextView _iconView;
    private CardView _Layout;


    // Data
    private Workorder _workorder;
    private UploadSlot _slot;
    private Listener _listener;
    private Set<String> _uploadingFiles = new HashSet<>();
    private UploadedDocumentView.Listener _docListener;
    private long _profileId;
    boolean _checked;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public ShareUploadSlotView(Context context) {
        super(context);
        init();
    }

    public ShareUploadSlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShareUploadSlotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_share_upload_slot_row, this);

        if (isInEditMode())
            return;

        _Layout = (CardView) findViewById(R.id.layoutCardView);
        _Layout.setOnClickListener(_shareUploadSlot_onClick);

        _titleTextView = (TextView) findViewById(R.id.name_textview);
        _titleTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
        _iconView = (IconFontTextView) findViewById(R.id.icon_view);
        _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }


    public void changeToDefaultColor() {
        _titleTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
        _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
    }

    public  boolean isChecked(){
        return _checked;
    }

    public void changeCheckStatus() {

        if (!_checked) {
            _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
            _checked = true;
        } else {
            _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
            _checked = false;
        }
    }


    public void setData(Workorder workorder, UploadSlot slot) {
        Log.v(TAG, "setData");

        _workorder = workorder;
        _slot = slot;
//        _docListener = listener;
//        _profileId = profileId;

//        subscribe();
        populateUi();
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_workorder == null)
            return;

        _titleTextView.setText(_slot.getSlotName());


    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _shareUploadSlot_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClick(ShareUploadSlotView.this, _slot);
        }
    };

    private void subscribe() {
        if (_workorder == null)
            return;

        if (_slot == null)
            return;
        Log.v(TAG, "subscribe, " + _workorder.getWorkorderId() + ", " + _slot.getSlotId());
    }


    public interface Listener {
        void onClick(ShareUploadSlotView view, UploadSlot slot);
    }
}
