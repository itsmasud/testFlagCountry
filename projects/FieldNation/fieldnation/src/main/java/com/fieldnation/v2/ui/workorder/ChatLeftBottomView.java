package com.fieldnation.v2.ui.workorder;

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
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.v2.data.model.Message;

import java.lang.ref.WeakReference;

/**
 * Created by mc on 8/30/17.
 */

public class ChatLeftBottomView extends RelativeLayout implements ChatRenderer {
    private static final String TAG = "ChatLeftBottomView";

    // Ui
    private TextView _messageTextView;
    private ProfilePicView _picView;

    // Data
    private Message _message = null;
    private WeakReference<Drawable> _profilePic = null;

    public ChatLeftBottomView(Context context) {
        super(context);
        init();
    }

    public ChatLeftBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatLeftBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_chat_left_bottom, this, true);

        if (isInEditMode()) return;

        _messageTextView = findViewById(R.id.message_textview);
        _messageTextView.setOnLongClickListener(_message_onLongClick);
        _picView = findViewById(R.id.pic_view);

        _photoClient.sub();
    }

    @Override
    protected void onDetachedFromWindow() {
        _photoClient.unsub();
        super.onDetachedFromWindow();
    }

    public void setMessage(Message message) {
        _message = message;
        populateUi();
    }

    private void populateUi() {
        if (_message == null)
            return;

        try {
            _messageTextView.setText(misc.linkifyHtml(_message.getMessage(), Linkify.ALL));
            _messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception ex) {
            _messageTextView.setText(_message.getMessage());
        }

        _picView.setAlertOn(!_message.getRead());

        if (_profilePic == null || _profilePic.get() == null) {
            _picView.setProfilePic(R.drawable.missing_circle);
            String url = _message.getFrom().getThumbnail();
            if (!misc.isEmptyOrNull(url)) {
                PhotoClient.get(getContext(), url, true, false);
            }
        } else if (_profilePic != null && _profilePic.get() != null) {
            _picView.setProfilePic(_profilePic.get());
        }
    }

    private final PhotoClient _photoClient = new PhotoClient() {
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
            ClipboardManager clipboard = (ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", _message.getMessage());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), R.string.toast_copied_to_clipboard, Toast.LENGTH_LONG);
            return true;
        }
    };
}
