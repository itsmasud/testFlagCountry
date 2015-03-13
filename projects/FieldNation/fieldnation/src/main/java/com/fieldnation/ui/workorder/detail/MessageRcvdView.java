package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.service.data.photo.PhotoDataClient;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.text.ParseException;
import java.util.Random;

public class MessageRcvdView extends RelativeLayout {
    private static final String TAG = "ui.workorder.detail.MessageRcvdView";

    private int WEB_GET_PHOTO = 1;
    // UI
    private TextView _messageTextView;
    private ImageView _profileImageView;
    private TextView _timeTextView;
    private ImageView _checkImageView;
    private TextView _usernameTextView;

    // Data
    private GlobalState _gs;
    private Message _message = null;
    private PhotoDataClient _photos;
    private Random _rand = new Random();
    private Drawable _profilePic = null;

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

        _gs = (GlobalState) getContext().getApplicationContext();

        _messageTextView = (TextView) findViewById(R.id.message_textview);
        _profileImageView = (ImageView) findViewById(R.id.profile_imageview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _checkImageView = (ImageView) findViewById(R.id.check_imageview);
        _usernameTextView = (TextView) findViewById(R.id.username_textview);

        _photos = new PhotoDataClient(getContext(), _photo_listener);
    }

    public void setMessage(Message message) {
        _message = message;

        populateUi();
    }

    private void getPhoto() {
        if (_photos == null)
            return;

        if (_message == null)
            return;

        WEB_GET_PHOTO = _rand.nextInt();
        if (_message.getFromUser().getPhotoThumbUrl() != null)
            _photos.getPhoto(getContext(), _message.getFromUser().getPhotoUrl(), false);
    }

    private void populateUi() {
        if (_photos == null)
            return;

        if (_message == null)
            return;

        _messageTextView.setText(misc.linkifyHtml(_message.getMessage(), Linkify.ALL));
        _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        try {
            _timeTextView.setText(misc.formatMessageTime(ISO8601.toCalendar(_message.getMsgCreateDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (_message.isRead()) {
            _checkImageView.setBackgroundResource(R.drawable.ic_check_grey);
        } else {
            _checkImageView.setBackgroundResource(R.drawable.ic_message_thumb);
        }

        _usernameTextView.setText(_message.getFromUser().getFirstname());

        if (_profilePic == null) {
            _profileImageView.setBackgroundResource(R.drawable.missing);
            getPhoto();
        } else {
            _profileImageView.setBackgroundDrawable(_profilePic);
        }
    }

    private PhotoDataClient.Listener _photo_listener = new PhotoDataClient.Listener() {
        @Override
        public void onPhoto(String url, File file, boolean isCircle) {
            Bitmap photo = BitmapFactory.decodeFile(file.getAbsolutePath());
            _profilePic = new BitmapDrawable(getContext().getResources(), photo);
            _profileImageView.setBackgroundDrawable(_profilePic);
        }
    };

}
