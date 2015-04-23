package com.fieldnation.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Calendar;

public class MessageCardView extends RelativeLayout {
    private static final String TAG = "MessageView";
    private View _statusView;
    private TextView _titleTextView;
    private LinearLayout _substatusLayout;
    private TextView _substatusTextView;
    private TextView _messageBodyTextView;
    private TextView _timeTextView;
    private ImageView _profileImageView;
    private int _viewId;

    private Message _message;
    private String[] _substatus;
    private Listener _listener;
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

        _memoryClass = GlobalState.getContext().getMemoryClass();

        _substatus = getResources().getStringArray(R.array.workorder_substatus);

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);
        _substatusLayout = (LinearLayout) findViewById(R.id.substatus_layout);
        _substatusTextView = (TextView) findViewById(R.id.substatus_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _profileImageView = (ImageView) findViewById(R.id.profile_imageview);
        _statusView = findViewById(R.id.status_view);

        populateUi();
    }

    public void setData(Message message, Listener listener) {
        _message = message;
        _listener = listener;
        _profileImageView.setBackgroundResource(R.drawable.missing_circle);

        populateUi();
    }

    private void setPhoto(Drawable photo) {
        if (photo == null) {
            _profileImageView.setBackgroundResource(R.drawable.missing_circle);
            return;
        }
        _profileImageView.setBackgroundDrawable(photo);
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

        if (_memoryClass <= 64) {
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
                _profileImageView.setBackgroundResource(R.drawable.missing_circle);
            }
        } else {
            if (_listener != null && _message.getFromUser() != null && !misc.isEmptyOrNull(_message.getFromUser().getPhotoUrl())) {
                Drawable result = _listener.getPhoto(this, _message.getFromUser().getPhotoUrl(), true);
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
                _profileImageView.setBackgroundResource(R.drawable.missing_circle);
            }
        }

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

    public Message getMessage() {
        return _message;
    }

    public interface Listener {
        public Drawable getPhoto(MessageCardView view, String url, boolean circle);
    }
}

