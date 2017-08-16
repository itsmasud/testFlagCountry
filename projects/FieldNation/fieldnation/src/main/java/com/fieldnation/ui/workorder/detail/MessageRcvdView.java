package com.fieldnation.ui.workorder.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.v2.data.model.Message;

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
        _messageTextView.setOnLongClickListener(_message_onLongClick);
        _picView = (ProfilePicView) findViewById(R.id.pic_view);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _usernameTextView = (TextView) findViewById(R.id.username_textview);

        _photos = new PhotoClient(_photo_listener);
        _photos.connect(App.get());
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_photos != null) _photos.disconnect(App.get());
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
            _timeTextView.setText(DateUtils.formatMessageTime(_message.getCreated().getCalendar()));
        } catch (ParseException e) {
            Log.v(TAG, e);
        }

        _picView.setAlertOn(!_message.getRead());

        _usernameTextView.setText(_message.getFrom().getName());

        if (_photos.isConnected() && (_profilePic == null || _profilePic.get() == null)) {
            _picView.setProfilePic(R.drawable.missing_circle);
            String url = _message.getFrom().getThumbnail();
            if (!misc.isEmptyOrNull(url)) {
                PhotoClient.get(getContext(), url, true, false);
            }
        } else if (_profilePic != null && _profilePic.get() != null) {
            _picView.setProfilePic(_profilePic.get());
        }
    }

    private final PhotoClient.Listener _photo_listener = new PhotoClient.Listener() {

        @Override
        public PhotoClient getClient() {
            return _photos;
        }

        @Override
        public void imageDownloaded(String sourceUri, Uri localUri, boolean isCircle, boolean success) {
        }

        @Override
        public boolean doGetImage(String sourceUri, boolean isCircle) {
            return _message != null
                    && _message.getFrom() != null
                    && !misc.isEmptyOrNull(_message.getFrom().getThumbnail())
                    && sourceUri.equals(_message.getFrom().getThumbnail())
                    && isCircle;
        }

        @Override
        public void onImageReady(String sourceUri, Uri localUri, BitmapDrawable drawable, boolean isCircle, boolean success) {
            if (drawable == null) {
                _picView.setProfilePic(R.drawable.missing_circle);
                return;
            }

            Drawable pic = drawable;
            _profilePic = new WeakReference<>(pic);
            _picView.setProfilePic(pic);
        }
    };

    private final OnLongClickListener _message_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipboardManager clipboard = (android.content.ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = android.content.ClipData.newPlainText("Copied Text", _message.getMessage());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), getResources().getString(R.string.toast_copied_to_clipboard), Toast.LENGTH_LONG);
            return true;
        }
    };
}
