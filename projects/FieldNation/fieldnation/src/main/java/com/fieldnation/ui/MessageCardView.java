package com.fieldnation.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
import com.fieldnation.service.data.photo.PhotoDataClient;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Hashtable;

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

    private PhotoDataClient _photos;
    private Message _message;
    private String[] _substatus;
    private static Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();
    private WeakReference<Drawable> _profilePic = null;

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

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);
        _substatusLayout = (LinearLayout) findViewById(R.id.substatus_layout);
        _substatusTextView = (TextView) findViewById(R.id.substatus_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _profileImageView = (ImageView) findViewById(R.id.profile_imageview);
        _statusView = findViewById(R.id.status_view);
        
        _photos = new PhotoDataClient(_photo_listener);
        _photos.connect(getContext());

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        _photos.disconnect(getContext());
        super.onDetachedFromWindow();
    }

    public void setMessage(Message message) {
        _message = message;
        _profilePic = null;

        if (_photos.isConnected())
            _photos.unregisterAll();

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
        if (_photos.isConnected() && (_profilePic == null || _profilePic.get() == null)) {
            _profileImageView.setBackgroundResource(R.drawable.missing_circle);
            String url = _message.getFromUser().getPhotoUrl();
            if (!misc.isEmptyOrNull(url)) {
                if (_picCache.containsKey(url)) {
                    _profilePic = _picCache.get(url);
                    if (_profilePic.get() != null) {
                        _profileImageView.setBackgroundDrawable(_profilePic.get());
                    } else {
                        _picCache.remove(url);
                        _photos.getPhoto(getContext(), url, true);
                    }
                } else {
                    _photos.getPhoto(getContext(), url, true);
                }
            }
        } else if (_profilePic != null && _profilePic.get() != null) {
            _profileImageView.setBackgroundDrawable(_profilePic.get());
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

    private final PhotoDataClient.Listener _photo_listener = new PhotoDataClient.Listener() {
        @Override
        public void onConnected() {
            if (_message != null && _profilePic == null || _profilePic.get() == null) {
                _profileImageView.setBackgroundResource(R.drawable.missing_circle);
                String url = _message.getFromUser().getPhotoUrl();
                if (!misc.isEmptyOrNull(url)) {
                    if (_picCache.containsKey(url)) {
                        _profilePic = _picCache.get(url);
                        if (_profilePic.get() != null) {
                            _profileImageView.setBackgroundDrawable(_profilePic.get());
                        } else {
                            _picCache.remove(url);
                            _photos.getPhoto(getContext(), url, true);
                        }
                    } else {
                        _photos.getPhoto(getContext(), url, true);
                    }
                }
            } else if (_profilePic != null && _profilePic.get() != null) {
                _profileImageView.setBackgroundDrawable(_profilePic.get());
            }

        }

        @Override
        public void onPhoto(String url, File file, boolean isCircle) {
            if (file == null || url == null)
                return;
            
            Drawable pic = new BitmapDrawable(GlobalState.getContext().getResources(), file.getAbsolutePath());
            _profilePic = new WeakReference<>(pic);
            _picCache.put(url, _profilePic);
            _profileImageView.setBackgroundDrawable(pic);
        }
    };

}

