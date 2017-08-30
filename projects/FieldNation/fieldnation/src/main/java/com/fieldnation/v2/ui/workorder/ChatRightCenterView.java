package com.fieldnation.v2.ui.workorder;

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
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Message;

/**
 * Created by mc on 8/30/17.
 */

public class ChatRightCenterView extends RelativeLayout implements ChatRenderer {
    private static final String TAG = "ChatRightCenterView";

    // Ui
    private TextView _messageTextView;

    // Data
    private Message _message = null;

    public ChatRightCenterView(Context context) {
        super(context);
        init();
    }

    public ChatRightCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatRightCenterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_chat_right_center, this, true);

        if (isInEditMode()) return;

        _messageTextView = findViewById(R.id.message_textview);
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
            _messageTextView.setText(_message.getMessage());
        }
    }

    private final OnLongClickListener _message_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", _message.getMessage());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), R.string.toast_copied_to_clipboard, Toast.LENGTH_LONG);
            return true;
        }
    };
}
