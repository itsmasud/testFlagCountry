package com.fieldnation.ui.workorder.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Message;

public class MessageSentView extends RelativeLayout {
    private static final String TAG = "MessageSentView";

    // UI
    private TextView _messageTextView;
    private TextView _statusTextView;

    // Data
    private Message _message = null;

    public MessageSentView(Context context) {
        super(context);
        init();
    }

    public MessageSentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageSentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_message_sent, this);

        _messageTextView = (TextView) findViewById(R.id.message_textview);
        _statusTextView = (TextView) findViewById(R.id.status_textview);
        _messageTextView.setOnLongClickListener(_message_onLongClick);
    }

    public void setMessage(Message message) {
        _message = message;

        populateUi();
    }

    private void populateUi() {
        if (_message == null)
            return;

        try {
            _messageTextView.setText(misc.linkifyHtml(_message.getMessage(), Linkify.ALL));
            _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        if (_message.getRead()) {
            _statusTextView.setText("Delivered");
        } else {
            _statusTextView.setText("Not read");
        }
    }

    private final OnLongClickListener _message_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipboardManager clipboard = (android.content.ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = android.content.ClipData.newPlainText("Copied Text", _message.getMessage());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), getResources().getString(R.string.toast_copied_to_clipboard), Toast.LENGTH_LONG);
            return true;
        }
    };
}
