package com.fieldnation.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.utils.misc;

public class MessageTileView extends RelativeLayout {
    private static final String TAG = "MessageTileView";
    private TextView _titleTextView;
    private TextView _messageBodyTextView;
    private ProfilePicView _picView;

    private Message _message;
    private String[] _substatus;
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

        _substatus = getResources().getStringArray(R.array.workorder_substatus);

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);
        _picView = (ProfilePicView) findViewById(R.id.pic_view);

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

        _picView.setAlertOn(!_message.isRead());

//        _viewId = _message.getMessageId() % Integer.MAX_VALUE;
        try {
            _titleTextView.setText(_message.getWorkorderTitle() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            _messageBodyTextView.setText(
                    misc.htmlify(
                            "<b>" + _message.getFromUser().getFullName() + "</b> - " + _message.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
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

    public interface Listener {
        Drawable getPhoto(MessageTileView view, String url, boolean circle);
    }
}

