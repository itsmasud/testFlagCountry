package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadingDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.detail.UploadedDocumentView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shoaib.ahemd on Oct 07, 2015.
 */
public class ShareRequestedFileRowView extends RelativeLayout {
    private static final String TAG = "ShareRequestedFileRowView";

    // Ui
    private TextView _titleTextView;
    private IconFontTextView _iconView;
    private CardView _Layout;


    // Data
    private Workorder _workorder;
    private UploadingDocument _uploadingDocument;
    private Listener _listener;
    private Set<String> _uploadingFiles = new HashSet<>();
    private UploadedDocumentView.Listener _docListener;
    private long _profileId;
    private boolean _checked;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public ShareRequestedFileRowView(Context context) {
        super(context);
        init();
    }

    public ShareRequestedFileRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShareRequestedFileRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_share_upload_slot_row, this);

        if (isInEditMode())
            return;

        _Layout = (CardView) findViewById(R.id.layoutCardView);
        _Layout. setOnClickListener(_shareUploadSlot_onClick);

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

    public void changeCheckStatus() {
        if(!_checked) {
            _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
            _checked = true;
        }
        else{
            _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
            _checked = false;
        }
    }


    public void setData(Workorder workorder, UploadingDocument uploadingDocument) {
        Log.v(TAG, "setData");

        _workorder = workorder;
        _uploadingDocument = uploadingDocument;
//        _docListener = listener;
//        _profileId = profileId;

//        subscribe();
        populateUi();
    }

    public boolean isChecked(){
        return _checked;
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_workorder == null)
            return;

        _titleTextView.setText(_uploadingDocument.getFileName());


    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _shareUploadSlot_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClick(ShareRequestedFileRowView.this, _uploadingDocument);
        }
    };

    private void subscribe() {
        if (_workorder == null)
            return;

        if (_uploadingDocument == null)
            return;
        Log.v(TAG, "subscribe, " + _workorder.getWorkorderId() + ", " + _uploadingDocument.getFileName());
    }


    public interface Listener {
        void onClick(ShareRequestedFileRowView view, UploadingDocument uploadingDocument);
    }
}
