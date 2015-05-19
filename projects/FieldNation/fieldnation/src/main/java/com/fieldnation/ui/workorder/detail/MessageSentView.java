package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.service.data.photo.PhotoDataClient;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.ParseException;

public class MessageSentView extends RelativeLayout {
    private static final String TAG = "MessageSentView";

    // UI
    private TextView _messageTextView;
    private ImageView _profileImageView;
    private TextView _timeTextView;
    private TextView _pendingTextView;
    private TextView _checkIconFont;

    // Data
    private PhotoDataClient _photos;
    private Message _message = null;
    private WeakReference<Drawable> _profilePic = null;

    public MessageSentView(Context context) {
        super(context);
        init();
    }

    public MessageSentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageSentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_message_sent, this);

        _messageTextView = (TextView) findViewById(R.id.message_textview);
        _profileImageView = (ImageView) findViewById(R.id.profile_imageview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        // _pendingTextView = (TextView) findViewById(R.id.pending_textview);
        _checkIconFont = (TextView) findViewById(R.id.check_iconfont);

        _photos = new PhotoDataClient(_photo_listener);
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

        _messageTextView.setText(misc.linkifyHtml(_message.getMessage(), Linkify.ALL));
        _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        try {
            _timeTextView.setText(misc.formatMessageTime(ISO8601.toCalendar(_message.getMsgCreateDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (_message.isRead()) {
//            _checkImageView.setBackgroundResource(R.drawable.ic_check_grey);
            // TODO switch to check mark
        } else {
//            _checkImageView.setBackgroundResource(R.drawable.ic_message_thumb);
            _checkIconFont.setText(R.string.icfont_message_alert);
        }
        if (_photos.isConnected() && (_profilePic == null || _profilePic.get() == null)) {
            _profileImageView.setBackgroundResource(R.drawable.missing);
            String url = _message.getFromUser().getPhotoUrl();
            if (!misc.isEmptyOrNull(url))
                _photos.getPhoto(getContext(), url, false);
        } else if (_profilePic != null && _profilePic.get() != null) {
            _profileImageView.setBackgroundDrawable(_profilePic.get());
        }
    }

    private final PhotoDataClient.Listener _photo_listener = new PhotoDataClient.Listener() {
        @Override
        public void onConnected() {
            populateUi();
        }

        @Override
        public void onPhoto(String url, File file, boolean isCircle) {
            Drawable pic = new BitmapDrawable(getContext().getResources(), file.getAbsolutePath());
            _profilePic = new WeakReference<>(pic);
            _profileImageView.setBackgroundDrawable(pic);
        }

    };
}
