package com.fieldnation.ui.inbox;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.workorder.WorkorderActivity;

public class NotificationTileView extends RelativeLayout {
    private static final String TAG = "NotificationTileView";

    // UI
    private TextView _titleTextView;
    private TextView _timeBoldTextView;
    private TextView _timeTextView;
    private TextView _workorderTextView;
    private TextView _messageBodyTextView;
    private View _overlayView;

    // Data
    private Notification _notification;
    private URLSpan _span = null;

    /*-*****************************-*/
    /*-			LifeCycle			-*/
    /*-*****************************-*/
    public NotificationTileView(Context context) {
        super(context);
        init();
    }

    public NotificationTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NotificationTileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_notification_tile, this);

        if (isInEditMode())
            return;

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _timeBoldTextView = (TextView) findViewById(R.id.timebold_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _workorderTextView = (TextView) findViewById(R.id.workorder_textview);
        _messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);

        _overlayView = findViewById(R.id.click_overlay);
        _overlayView.setOnClickListener(_this_onClick);

        populateUi();
    }

    public void setData(Notification notification) {
        _notification = notification;

        populateUi();
    }

    public Notification getNotification() {
        return _notification;
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_notification == null)
            return;

        try {
            _titleTextView.setText(_notification.getWorkorder().getTitle());
        } catch (Exception e) {
            Log.v(TAG, e);
        }

        try {
            if (_notification.getViewed() == 1) {
                _timeTextView.setText(DateUtils.humanReadableAge(ISO8601.toCalendar(_notification.getDate())));
                _timeTextView.setVisibility(VISIBLE);
                _timeBoldTextView.setVisibility(INVISIBLE);
            } else {
                _timeBoldTextView.setText(DateUtils.humanReadableAge(ISO8601.toCalendar(_notification.getDate())));
                _timeTextView.setVisibility(INVISIBLE);
                _timeBoldTextView.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            Log.v(TAG, e);
        }

        try {
            if (_notification.getWorkorderId() != null) {
                _workorderTextView.setText("WO " + _notification.getWorkorderId());
                _workorderTextView.setVisibility(VISIBLE);
            } else if (_notification.getWorkorder() != null && _notification.getWorkorder().getWorkorderId() != null) {
                _workorderTextView.setText("WO " + _notification.getWorkorder().getWorkorderId());
                _workorderTextView.setVisibility(VISIBLE);
            } else {
                _workorderTextView.setVisibility(GONE);
            }
        } catch (Exception e) {
            Log.v(TAG, e);
            _workorderTextView.setVisibility(GONE);
        }

        try {
            Spannable msg = misc.linkifyHtml(_notification.getMessage(), Linkify.ALL);
            _messageBodyTextView.setText(msg);
            _messageBodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
            if (_notification.getWorkorder() == null) {
                URLSpan[] spans = msg.getSpans(0, msg.length(), URLSpan.class);
                if (spans.length > 0) {
                    _span = spans[0];
                }
            }
            _messageBodyTextView.setText(msg);
        } catch (Exception e) {
            Log.v(TAG, e);
            if (misc.isEmptyOrNull(_notification.getMessage())) {
                _messageBodyTextView.setVisibility(View.GONE);
            } else {
                _messageBodyTextView.setText(_notification.getMessage());
            }
        }
        _messageBodyTextView.setClickable(false);
    }

    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_notification.getWorkorder() != null && _notification.getWorkorder().getWorkorderId() != null) {
                WorkorderActivity.startNew(getContext(), _notification.getWorkorder().getWorkorderId(), WorkorderActivity.TAB_DETAILS);
            } else if (_notification.getWorkorderId() != null) {
                WorkorderActivity.startNew(getContext(), _notification.getWorkorderId(), WorkorderActivity.TAB_DETAILS);
            } else if (_span != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_span.getURL()));
                getContext().startActivity(intent);
            } else {
                Log.v(TAG, "***********_this_onClick what?");
            }
        }
    };
}


