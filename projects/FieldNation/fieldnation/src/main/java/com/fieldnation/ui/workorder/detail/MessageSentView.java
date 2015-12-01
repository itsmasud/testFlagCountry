package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.utils.misc;

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
        if (_message.isRead()) {
            _statusTextView.setText("Delivered");
        } else {
            _statusTextView.setText("Not read");
        }
    }
}
