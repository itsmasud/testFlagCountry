package com.fieldnation.ui;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Workorder;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

public class NotificationView extends RelativeLayout {
    private static final String TAG = "ui.NotificationView";

    // UI
    private LinearLayout _titleLayout;
    private ImageView _titleThumbImageView;
    private TextView _titleTextView;
    private LinearLayout _statusLayout;
    private TextView _statusTextView;
    private TextView _messageTextView;
    private TextView _dateTextView;

    // Data
    private Notification _note;
    private String[] _substatus;
    private int[] _colors = new int[5];
    private URLSpan _span = null;

    /*-*****************************-*/
    /*-			Lifecycle			-*/
    /*-*****************************-*/
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
        LayoutInflater.from(getContext()).inflate(R.layout.view_notification, this);
        if (isInEditMode())
            return;

        _substatus = getResources().getStringArray(R.array.workorder_substatus);
        _colors[0] = getResources().getColor(R.color.fn_dark_text);
        _colors[1] = getResources().getColor(R.color.fn_dark_text);
        _colors[2] = getResources().getColor(R.color.fn_dark_text);
        _colors[3] = getResources().getColor(R.color.fn_dark_text);
        _colors[4] = getResources().getColor(R.color.fn_dark_text);

        _titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        _titleThumbImageView = (ImageView) findViewById(R.id.titlethumb_imageview);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _statusLayout = (LinearLayout) findViewById(R.id.status_layout);
        _statusTextView = (TextView) findViewById(R.id.status_textview);
        _messageTextView = (TextView) findViewById(R.id.message_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _statusLayout.setVisibility(View.GONE);
        setOnClickListener(_this_onClick);
    }

    /*-*****************************-*/
    /*-			Modifiers			-*/
    /*-*****************************-*/
    public void setNotification(Notification notification) {
        _note = notification;
        populateUi();
    }

    private void populateUi() {
        if (_note.getWorkorder() != null) {
            Workorder work = _note.getWorkorder();
            _titleTextView.setText(work.getTitle());
            _statusTextView.setText(_substatus[work.getStatus().getWorkorderSubstatus().ordinal()]);
            _statusTextView.setTextColor(_colors[work.getStatus().getStatusIntent().ordinal()]);
            _titleLayout.setVisibility(View.VISIBLE);
//            _statusLayout.setVisibility(View.VISIBLE);
        } else {
            _titleLayout.setVisibility(View.GONE);
//            _statusLayout.setVisibility(View.GONE);

        }
        if (_note.getViewed() != null && _note.getViewed() == 1) {
            _titleThumbImageView.setVisibility(View.GONE);
        } else {
            _titleThumbImageView.setVisibility(View.VISIBLE);
        }

        _messageTextView.setVisibility(View.VISIBLE);
        try {
            Spannable msg = misc.linkifyHtml(_note.getMessage(), Linkify.ALL);
            _messageTextView.setText(msg);
            _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            if (_note.getWorkorder() == null) {
                URLSpan[] spans = msg.getSpans(0, msg.length(), URLSpan.class);
                if (spans.length > 0) {
                    _span = spans[0];
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            if (misc.isEmptyOrNull(_note.getMessage())) {
                _messageTextView.setVisibility(View.GONE);
            } else {
                _messageTextView.setText(_note.getMessage());
            }
        }
        try {
            _dateTextView.setText(misc.formatDateLong(ISO8601.toCalendar(_note.getDate())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_note.getWorkorder() != null) {
                Intent intent = new Intent(getContext(), WorkorderActivity.class);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _note.getWorkorder().getWorkorderId());
                getContext().startActivity(intent);
            } else if (_span != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_span.getURL()));
                getContext().startActivity(intent);
            } else {
            }
        }
    };
}
