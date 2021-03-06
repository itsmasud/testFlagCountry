package com.fieldnation.v2.ui.chat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Message;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by mc on 8/30/17.
 */

public class ChatRightView extends RelativeLayout implements ChatRenderer {
    private static final String TAG = "ChatRightView";
    private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm a", Locale.getDefault());

    // Ui
    private TextView _messageTextView;
    private TextView _timeTextView;
    private TextView _alertTextView;
    private LinearLayout _statusLayout;

    // Data
    private Message _message = null;
    private Position _position = null;
    private boolean _offline = false;

    public ChatRightView(Context context) {
        super(context);
        init();
    }

    public ChatRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_chat_right, this, true);

        if (isInEditMode()) return;

        _messageTextView = findViewById(R.id.message_textview);
        _messageTextView.setOnLongClickListener(_message_onLongClick);

        _statusLayout = findViewById(R.id.status_layout);
        _timeTextView = findViewById(R.id.time_textview);
        _alertTextView = findViewById(R.id.alert_textview);
    }

    @Override
    public void setMessage(Message message, boolean offline) {
        _message = message;
        _offline = offline;
        populateUi();
    }

    @Override
    public void setPosition(Position position) {
        _position = position;
        populateUi();
    }

    private void populateUi() {
        if (_message == null)
            return;

        if (_position == null)
            return;

        try {
            String message = _message.getMessage();
            _messageTextView.setText(misc.linkifyHtml(message, Linkify.ALL));
            _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception ex) {
            _messageTextView.setText(_message.getMessage());
        }

        _statusLayout.setVisibility(GONE);
        _alertTextView.setVisibility(GONE);

        if (_position == Position.FULL || _position == Position.BOTTOM) {
            _statusLayout.setVisibility(VISIBLE);
            _timeTextView.setVisibility(VISIBLE);
            try {
                _timeTextView.setText(
                        TIME_FORMAT.format(_message.getCreated().getCalendar().getTime()).toUpperCase()
                                + DateUtils.getDeviceTimezone(_message.getCreated().getCalendar()));
                if (_offline)
                    _alertTextView.setVisibility(VISIBLE);
                else
                    _alertTextView.setVisibility(GONE);
            } catch (Exception ex) {
                _statusLayout.setVisibility(GONE);
                _timeTextView.setVisibility(GONE);
            }
        } else {
            _statusLayout.setVisibility(GONE);
            _timeTextView.setVisibility(GONE);
        }

        _messageTextView.setTextColor(getResources().getColor(_offline ? R.color.fn_dark_text : R.color.fn_white_text));
        switch (_position) {
            case FULL:
                _messageTextView.setBackgroundResource(_offline ? R.drawable.chat_right_full_offline : R.drawable.chat_right_full);
                break;
            case TOP:
                _messageTextView.setBackgroundResource(_offline ? R.drawable.chat_right_top_offline : R.drawable.chat_right_top);
                break;
            case CENTER:
                _messageTextView.setBackgroundResource(_offline ? R.drawable.chat_right_center_offline : R.drawable.chat_right_center);
                break;
            case BOTTOM:
                _messageTextView.setBackgroundResource(_offline ? R.drawable.chat_right_bottom_offline : R.drawable.chat_right_bottom);
                break;
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
