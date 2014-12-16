package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.rpc.client.PhotoService;
import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Calendar;

public class MessageCardView extends RelativeLayout {
    private static final String TAG = "ui.MessageView";
    private View _statusView;
    private TextView _titleTextView;
    private TextView _substatusTextView;
    private TextView _messageBodyTextView;
    private TextView _timeTextView;
    private ImageView _profileImageView;
    private int _viewId;

    private PhotoService _photoService;
    private Message _message;
    private String[] _substatus;

    /*-*****************************-*/
    /*-			LifeCycle			-*/
    /*-*****************************-*/
    public MessageCardView(Context context) {
        super(context);
        init();
    }

    public MessageCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_message_card, this);

        if (isInEditMode())
            return;

        _substatus = getResources().getStringArray(R.array.workorder_substatus);

        _photoService = new PhotoService(getContext(), _resultReceiver);

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);
        _substatusTextView = (TextView) findViewById(R.id.substatus_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _profileImageView = (ImageView) findViewById(R.id.profile_imageview);
        _statusView = findViewById(R.id.status_view);
    }

    public void setMessage(Message message) {
        _message = message;
        _substatusTextView.setText(_substatus[_message.getStatus().getWorkorderSubstatus().ordinal()]);

        _viewId = message.getMessageId() % Integer.MAX_VALUE;
        try {
            _titleTextView.setText(message.getWorkorderTitle() + "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            // compress the data a bit
            _messageBodyTextView.setText(message.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Calendar cal = ISO8601.toCalendar(message.getDate());

            String date = misc.formatDateLong(cal);

            _timeTextView.setText(date);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            // _profileImageView.setBackgroundDrawable(null);
            _profileImageView.setImageDrawable(null);
            String url = message.getPhotoUrl();
            getContext().startService(_photoService.getPhoto(_viewId, url, true));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        switch (_message.getStatus().getStatusIntent()) {
            case NORMAL:
                _statusView.setBackgroundResource(R.drawable.card_status_white);
                break;
            case SUCCESS:
                _statusView.setBackgroundResource(R.drawable.card_status_green);
                break;
            case UNKNOWN:
                _statusView.setBackgroundResource(R.drawable.card_status_white);
                break;
            case WAITING:
                _statusView.setBackgroundResource(R.drawable.card_status_gray);
                break;
            case WARNING:
                _statusView.setBackgroundResource(R.drawable.card_status_orange);
                break;
        }
    }

    public Message getMessage() {
        return _message;
    }

    private ResultReceiver _resultReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == _viewId) {
                Bitmap photo = resultData.getParcelable(PhotoServiceConstants.KEY_RESPONSE_DATA);
                Drawable draw = new BitmapDrawable(getContext().getResources(), photo);
                _profileImageView.setImageDrawable(draw);
            }
            super.onReceiveResult(resultCode, resultData);
        }
    };

}
