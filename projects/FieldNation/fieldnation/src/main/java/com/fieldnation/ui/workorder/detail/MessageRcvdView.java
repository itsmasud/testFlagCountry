package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.ParseException;

public class MessageRcvdView extends RelativeLayout {
    private static final String TAG = "MessageRcvdView";

    // UI
    private TextView _messageTextView;
    private ProfilePicView _picView;
    private TextView _timeTextView;
    private TextView _usernameTextView;

    // Data
    private PhotoClient _photos;
    private Message _message = null;
    private WeakReference<Drawable> _profilePic = null;

    public MessageRcvdView(Context context) {
        super(context);
        init();
    }

    public MessageRcvdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageRcvdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_message_rcvd, this);

        _messageTextView = (TextView) findViewById(R.id.message_textview);
        _picView = (ProfilePicView) findViewById(R.id.pic_view);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _usernameTextView = (TextView) findViewById(R.id.username_textview);

        _photos = new PhotoClient(_photo_listener);
        _photos.connect(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        _photos.disconnect(getContext());
        super.onDetachedFromWindow();
    }

    public void setMessage(Message message) {
        _message = message;

        populateUi();
    }

    private void populateUi() {
        if (_photos == null)
            return;

        if (_message == null)
            return;

        try {
            _messageTextView.setText(misc.linkifyHtml(_message.getMessage(), Linkify.ALL));
            _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception ex) {
            _messageTextView.setText(_message.getMessage());
        }

        try {
            _timeTextView.setText(misc.formatMessageTime(ISO8601.toCalendar(_message.getMsgCreateDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        _picView.setAlertOn(!_message.isRead());

        _usernameTextView.setText(_message.getFromUser().getFirstName());

        if (_photos.isConnected() && (_profilePic == null || _profilePic.get() == null)) {
            _picView.setProfilePic(R.drawable.missing_circle);
            String url = _message.getFromUser().getPhotoUrl();
            if (!misc.isEmptyOrNull(url)) {
                PhotoClient.get(getContext(), url, true, false);
                _photos.subGet(url, true, false);
            }
        } else if (_profilePic != null && _profilePic.get() != null) {
            _picView.setProfilePic(_profilePic.get());
        }
    }

    private PhotoClient.Listener _photo_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
            populateUi();
        }

        @Override
        public void onGet(String url, File file, boolean isCircle) {
            Drawable pic = new BitmapDrawable(getContext().getResources(), file.getAbsolutePath());
            _profilePic = new WeakReference<>(pic);
            _picView.setProfilePic(pic);
        }
    };
}
