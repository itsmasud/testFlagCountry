package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

public class NotificationView extends RelativeLayout {
    private static final String TAG = "NotificationView";

    // UI
    private ImageView _alertThumbImageView;
    private TextView _messageTextView;
    private TextView _dateTextView;

    // Data
    private Notification _notification;
    private boolean _isTruncated = true;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public NotificationView(Context context) {
        super(context);
        init();
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NotificationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_note, this);

        if (isInEditMode())
            return;

        _alertThumbImageView = (ImageView) findViewById(R.id.alertthumb_imageview);
        _messageTextView = (TextView) findViewById(R.id.message_textview);
        _messageTextView.setOnClickListener(_message_onClick);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
    }

    /*-*************************************-*/
    /*-				Modifiers				-*/
    /*-*************************************-*/
    public void setNotification(Notification notification) {
        _notification = notification;
        rePopulate();
    }

    private void rePopulate() {
        String msg = _notification.getMessage();
        _messageTextView.setText(misc.linkifyHtml(msg, Linkify.ALL));
        _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());

        try {
            long milliseconds = System.currentTimeMillis() - ISO8601.toCalendar(_notification.getDate()).getTimeInMillis();

            _dateTextView.setText(misc.toRoundDuration(milliseconds));
        } catch (Exception e) {
            _dateTextView.setText(_notification.getDate());
        }

        if (_notification.getViewed() == 1) {
            _alertThumbImageView.setVisibility(GONE);
        } else {
            _alertThumbImageView.setVisibility(VISIBLE);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private View.OnClickListener _message_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_isTruncated) {
                _messageTextView.setText(_notification.getMessage());
                _isTruncated = false;
            } else {
                _isTruncated = true;
                String msg = _notification.getMessage();
                if (msg.length() > 170) {
                    msg = msg.substring(0, 170) + "...";
                }
                _messageTextView.setText(msg);
            }
        }
    };
}
