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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.rpc.client.PhotoService;
import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Hashtable;

public class MessageCardView extends RelativeLayout {
    private static final String TAG = "ui.MessageView";
    private View _statusView;
    private TextView _titleTextView;
    private LinearLayout _substatusLayout;
    private TextView _substatusTextView;
    private TextView _messageBodyTextView;
    private TextView _timeTextView;
    private ImageView _profileImageView;
    private ImageView _unreadImageView;
    private int _viewId;

    private PhotoService _photoService;
    private Message _message;
    private String[] _substatus;
    private static Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();
    private WeakReference<Drawable> _profilePic = null;
    private int _memoryClass;

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
        _substatusLayout = (LinearLayout) findViewById(R.id.substatus_layout);
        _substatusTextView = (TextView) findViewById(R.id.substatus_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _profileImageView = (ImageView) findViewById(R.id.profile_imageview);
        _statusView = findViewById(R.id.status_view);
        _unreadImageView = (ImageView) findViewById(R.id.unread_imageview);

        _memoryClass = GlobalState.getContext().getMemoryClass();

        populateUi();
    }

    public void setMessage(Message message) {
        _message = message;
        _profilePic = null;
        populateUi();
    }

    private void populateUi() {
        if (_statusView == null)
            return;

        if (_message == null)
            return;

        try {
            _substatusTextView.setText(_substatus[_message.getStatus().getWorkorderSubstatus().ordinal()]);
            _substatusLayout.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            _substatusLayout.setVisibility(View.GONE);
        }

        try {
            if (_message.isRead())
                _unreadImageView.setVisibility(View.GONE);
            else
                _unreadImageView.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            _unreadImageView.setVisibility(View.GONE);
        }

        _viewId = _message.getMessageId() % Integer.MAX_VALUE;
        try {
            _titleTextView.setText(_message.getWorkorderTitle() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            _messageBodyTextView.setText(_message.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Calendar cal = ISO8601.toCalendar(_message.getDate());

            String date = misc.formatDateLong(cal);

            _timeTextView.setText(date);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        addPhoto();

        try {
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
        } catch (Exception ex) {
            _statusView.setBackgroundResource(R.drawable.card_status_white);
        }
    }

    private void addPhoto() {
        if (_message == null || _photoService == null) {
            _profileImageView.setBackgroundResource(R.drawable.missing_circle);
            return;
        }

        if (_profilePic == null || _profilePic.get() == null) {
            String url = null;
            if (_memoryClass <= 64) {
                url = _message.getFromUser().getPhotoThumbUrl();
            } else {
                url = _message.getFromUser().getPhotoUrl();
            }

            if (misc.isEmptyOrNull(url)) {
                _profileImageView.setBackgroundResource(R.drawable.missing_circle);
            } else {
                if (_picCache.containsKey(url) && _picCache.get(url).get() != null) {
                    _profilePic = _picCache.get(url);
                    _profileImageView.setBackgroundDrawable(_profilePic.get());
                } else {
                    _profileImageView.setBackgroundResource(R.drawable.missing_circle);
                    getContext().startService(_photoService.getPhoto(_viewId, url, true));
                }
            }
        } else {
            _profileImageView.setBackgroundDrawable(_profilePic.get());
        }
    }

    public Message getMessage() {
        return _message;
    }

    private final ResultReceiver _resultReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == _viewId) {
                Bitmap photo = resultData.getParcelable(PhotoServiceConstants.KEY_RESPONSE_DATA);
                Drawable d = new BitmapDrawable(getContext().getResources(), photo);
                _profilePic = new WeakReference<>(d);
                _profileImageView.setBackgroundDrawable(_profilePic.get());
                if (_memoryClass <= 64) {
                    _picCache.put(_message.getFromUser().getPhotoThumbUrl(), _profilePic);
                } else {
                    _picCache.put(_message.getFromUser().getPhotoUrl(), _profilePic);
                }
                addPhoto();
            }
            super.onReceiveResult(resultCode, resultData);
        }
    };

}
