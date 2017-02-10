package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.workorder.WorkOrderActivity;

public class NotificationView extends RelativeLayout {
    private static final String TAG = "NotificationView";

    // UI
    private ImageView _alertThumbImageView;
    private TextView _messageTextView;
    private TextView _dateTextView;
    private View _clickOverlay;

    // Data
    private Notification _notification;
    private boolean _isTruncated = true;
    private URLSpan _span = null;

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
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _clickOverlay = findViewById(R.id.click_overlay);

        _clickOverlay.setOnClickListener(_this_onClick);
    }

    /*-*************************************-*/
    /*-				Modifiers				-*/
    /*-*************************************-*/
    public void setNotification(Notification notification) {
        _notification = notification;
        rePopulate();
    }

    private void rePopulate() {
        _messageTextView.setVisibility(View.VISIBLE);
        try {
            Spannable msg = misc.linkifyHtml(_notification.getMessage(), Linkify.ALL);
            _messageTextView.setText(msg);
            _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            if (_notification.getWorkorder() == null) {
                URLSpan[] spans = msg.getSpans(0, msg.length(), URLSpan.class);
                if (spans.length > 0) {
                    _span = spans[0];
                }
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            if (misc.isEmptyOrNull(_notification.getMessage())) {
                _messageTextView.setVisibility(View.GONE);
            } else {
                _messageTextView.setText(_notification.getMessage());
            }
        }
        _messageTextView.setClickable(false);

        try {
            long milliseconds = System.currentTimeMillis() - ISO8601.toCalendar(_notification.getDate()).getTimeInMillis();

            _dateTextView.setText(DateUtils.toRoundDuration(milliseconds));
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
    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_notification.getWorkorder() != null) {
                WorkOrderActivity.startNew(getContext(), _notification.getWorkorder().getWorkorderId().intValue(), WorkOrderActivity.TAB_NOTIFICATIONS);
            } else if (_notification.getWorkorderId() != null) {
                WorkOrderActivity.startNew(getContext(), _notification.getWorkorderId().intValue(), WorkOrderActivity.TAB_NOTIFICATIONS);
            } else if (_span != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_span.getURL()));
                getContext().startActivity(intent);
            } else {
            }
        }
    };
}
