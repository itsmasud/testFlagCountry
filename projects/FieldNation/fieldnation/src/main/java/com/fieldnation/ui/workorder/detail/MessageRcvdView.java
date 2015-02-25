package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
import com.fieldnation.rpc.client.PhotoService;
import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

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
    private PhotoService _service;
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

        _service = new PhotoService(_gs, _resultReceiver);
    }

    public void setMessage(Message message) {
        _message = message;

        populateUi();
    }

    private void getPhoto() {
        if (_service == null)
            return;

        if (_message == null)
            return;

        WEB_GET_PHOTO = _rand.nextInt();
        if (_message.getFromUser().getPhotoThumbUrl() != null)
            _gs.startService(_service.getPhoto(WEB_GET_PHOTO, _message.getFromUser().getPhotoThumbUrl(), true));
    }

    private void populateUi() {
        if (_service == null)
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

    private ResultReceiver _resultReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == WEB_GET_PHOTO) {
                Bitmap photo = resultData.getParcelable(PhotoServiceConstants.KEY_RESPONSE_DATA);
                _profilePic = new BitmapDrawable(getContext().getResources(), photo);
                _profileImageView.setBackgroundDrawable(_profilePic);
            }
            super.onReceiveResult(resultCode, resultData);
        }
    };

}
