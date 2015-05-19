package com.fieldnation.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.GlobalState;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.workorder.WorkorderActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.List;

public class MessageListActivity extends ItemListActivity<Message> {
    private static final String TAG = "MessageListActivity";

    // Data
    private ProfileClient _profiles;
    private PhotoClient _photos;
    private static Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/


    protected void onResume() {
        super.onResume();
        _profiles = new ProfileClient(_profile_listener);
        _profiles.connect(this);
        _photos = new PhotoClient(_photoClient_listener);
        _photos.connect(this);
    }

    @Override
    protected void onPause() {
        _profiles.disconnect(this);
        _photos.disconnect(this);
        super.onPause();
    }

    @Override
    public void requestData(int page) {
        ProfileClient.listMessages(this, page);
    }

    @Override
    public View getView(Message object, View convertView, ViewGroup parent) {
        MessageTileView v = null;
        if (convertView == null) {
            v = new MessageTileView(parent.getContext());
        } else if (convertView instanceof MessageTileView) {
            v = (MessageTileView) convertView;
        } else {
            v = new MessageTileView(parent.getContext());
        }

        v.setData(object, _messageCard_listener);
        v.setOnClickListener(_message_onClick);

        return v;
    }

    private final View.OnClickListener _message_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MessageTileView mv = (MessageTileView) v;
            Intent intent = new Intent(MessageListActivity.this, WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_MESSAGE);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, mv.getMessage().getWorkorderId());
            startActivity(intent);
        }
    };

    private final MessageTileView.Listener _messageCard_listener = new MessageTileView.Listener() {
        @Override
        public Drawable getPhoto(MessageTileView view, String url, boolean circle) {
            if (_picCache.containsKey(url) && _picCache.get(url).get() != null) {
                return _picCache.get(url).get();
            } else {
                _photos.subGet(url, circle, false);
                PhotoClient.get(MessageListActivity.this, url, circle, false);
            }
            return null;
        }
    };


    private final PhotoClient.Listener _photoClient_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(String url, File file, boolean isCircle) {
            if (file == null || url == null)
                return;

            Drawable pic = new BitmapDrawable(GlobalState.getContext().getResources(), file.getAbsolutePath());
            _picCache.put(url, new WeakReference<>(pic));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messages, menu);

        _notificationsView = (NotificationActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.notifications_menuitem));

        return true;
    }

    private final ProfileClient.Listener _profile_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profiles.subListMessages();
        }

        @Override
        public void onMessageList(List<Message> list, int page) {
            Log.v(TAG, "onAllMessagesPage");
            addPage(page, list);
        }
    };
}
