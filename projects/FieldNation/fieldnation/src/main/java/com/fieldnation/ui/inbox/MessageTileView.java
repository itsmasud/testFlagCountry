package com.fieldnation.ui.inbox;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;

public class MessageTileView extends RelativeLayout {
    private static final String TAG = "MessageTileView";

    private ProfilePicView _picView;
    private TextView _titleTextView;
    private TextView _timeBoldTextView;
    private TextView _timeTextView;
    private TextView _workorderTextView;
    private TextView _messageBodyTextView;

    private Message _message;
    private Listener _listener;
    private boolean _imageRetried = false;

    /*-*****************************-*/
    /*-			LifeCycle			-*/
    /*-*****************************-*/
    public MessageTileView(Context context) {
        super(context);
        init();
    }

    public MessageTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageTileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_message_tile, this);

        if (isInEditMode())
            return;

        _picView = (ProfilePicView) findViewById(R.id.pic_view);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _timeBoldTextView = (TextView) findViewById(R.id.timebold_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _workorderTextView = (TextView) findViewById(R.id.workorder_textview);
        _messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);

        setOnClickListener(_this_onClick);

        populateUi();
    }

    public void setData(Message message, Listener listener) {
        _message = message;
        _listener = listener;
        _picView.setProfilePic(R.drawable.missing_circle);
        _imageRetried = false;

        populateUi();
    }

    private void setPhoto(Drawable photo) {
        if (photo == null) {
            _picView.setProfilePic(R.drawable.missing_circle);
            return;
        }
        _picView.setProfilePic(photo);
    }

    private void populateUi() {
        if (_titleTextView == null)
            return;

        if (_message == null)
            return;

        try {
            _titleTextView.setText(_message.getFromUser().getFullName());
        } catch (Exception e) {
            Log.v(TAG, e);
        }

        try {
            if (_message.isRead()) {
                _timeTextView.setText(DateUtils.humanReadableAge(ISO8601.toCalendar(_message.getDate())));
                _timeTextView.setVisibility(VISIBLE);
                _timeBoldTextView.setVisibility(INVISIBLE);
            } else {
                _timeBoldTextView.setText(DateUtils.humanReadableAge(ISO8601.toCalendar(_message.getDate())));
                _timeTextView.setVisibility(INVISIBLE);
                _timeBoldTextView.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            Log.v(TAG, e);
        }

        try {
            if (_message.getWorkorderId() != null) {
                _workorderTextView.setText("WO " + _message.getWorkorderId());
                _workorderTextView.setVisibility(VISIBLE);
            } else {
                _workorderTextView.setVisibility(GONE);
            }
        } catch (Exception e) {
            Log.v(TAG, e);
            _workorderTextView.setVisibility(GONE);
        }

        try {
            _messageBodyTextView.setText(misc.htmlify(_message.getMessage()));
        } catch (Exception e) {
            Log.v(TAG, e);
        }

        if (App.get().isLowMemDevice()) {
            if (_listener != null && _message.getFromUser() != null && !misc.isEmptyOrNull(_message.getFromUser().getPhotoThumbUrl())) {
                Drawable result = _listener.getPhoto(this, _message.getFromUser().getPhotoThumbUrl(), true);
                if (result == null) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            populateUi();
                        }
                    }, 2000);
                } else {
                    setPhoto(result);
                }
            } else {
                _picView.setProfilePic(R.drawable.missing_circle);
            }
        } else {
            if (_listener != null && _message.getFromUser() != null && !misc.isEmptyOrNull(_message.getFromUser().getPhotoUrl())) {
                Drawable result = _listener.getPhoto(this, _message.getFromUser().getPhotoUrl(), true);
                if (result == null && !_imageRetried) {
                    _imageRetried = true;
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            populateUi();
                        }
                    }, 2000);
                } else {
                    setPhoto(result);
                }
            } else {
                _picView.setProfilePic(R.drawable.missing_circle);
            }
        }
    }

    public Message getMessage() {
        return _message;
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_MESSAGE);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _message.getWorkorderId());
            getContext().startActivity(intent);

        }
    };

    public interface Listener {
        Drawable getPhoto(MessageTileView view, String url, boolean circle);
    }
}

