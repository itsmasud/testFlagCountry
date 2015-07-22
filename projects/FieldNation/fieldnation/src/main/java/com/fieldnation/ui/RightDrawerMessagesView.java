package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.workorder.WorkorderActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Michael Carver on 6/10/2015.
 */
public class RightDrawerMessagesView extends FrameLayout {
    private static final String TAG = "RightDrawerMessagesView";

    // Ui
    private ListView _messageListView;
    private RightDrawerView _rightDrawer;

    private RelativeLayout _toolbarNormalLayout;
    private Button _closeButton;

    // Data
    private ProfileClient _profileClient;
    private PhotoClient _photoClient;
    private static Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();

    public RightDrawerMessagesView(Context context) {
        super(context);
        init();
    }

    public RightDrawerMessagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RightDrawerMessagesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_right_drawer_messages, this);

        if (isInEditMode())
            return;

        _rightDrawer = (RightDrawerView) findViewById(R.id.drawerView);
        _rightDrawer.setVisibility(GONE);

        _closeButton = (Button) findViewById(R.id.close_button);
        _closeButton.setOnClickListener(_close_onClick);
        _messageListView = (ListView) findViewById(R.id.listview);
        _messageListView.setAdapter(_adapter);
        _messageListView.setOnItemClickListener(_message_onClick);

        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(getContext());
        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        _profileClient.disconnect(getContext());
        _photoClient.disconnect(getContext());
        _picCache.clear();
        super.onDetachedFromWindow();
    }

    public void animateShow() {
        _rightDrawer.animateShow();
    }

    public void animateHide() {
        _rightDrawer.animateHide();
    }

    public boolean isOpen() {
        return _rightDrawer.getVisibility() == VISIBLE;
    }

    private final View.OnClickListener _close_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            animateHide();
        }
    };

    private final PagingAdapter<Message> _adapter = new PagingAdapter<Message>() {
        @Override
        public View getView(int page, int position, Message object, View convertView, ViewGroup parent) {
            MessageTileView v = null;
            if (convertView == null) {
                v = new MessageTileView(parent.getContext());
            } else if (convertView instanceof MessageTileView) {
                v = (MessageTileView) convertView;
            } else {
                v = new MessageTileView(parent.getContext());
            }

            v.setData(object, _messageCard_listener);

            return v;
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            ProfileClient.listMessages(getContext(), page);
        }
    };

    private final AdapterView.OnItemClickListener _message_onClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MessageTileView mv = (MessageTileView) view;
            Intent intent = new Intent(getContext(), WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_MESSAGE);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, mv.getMessage().getWorkorderId());
            getContext().startActivity(intent);
        }
    };

    private final MessageTileView.Listener _messageCard_listener = new MessageTileView.Listener() {
        @Override
        public Drawable getPhoto(MessageTileView view, String url, boolean circle) {
            if (_picCache.containsKey(url) && _picCache.get(url).get() != null) {
                return _picCache.get(url).get();
            } else {
                _photoClient.subGet(url, circle, false);
                PhotoClient.get(getContext(), url, circle, false);
            }
            return null;
        }
    };

    /*-*****************************************-*/
    /*-                Data Events              -*/
    /*-*****************************************-*/

    private final PhotoClient.Listener _photoClient_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(String url, File file, boolean isCircle, boolean failed) {
            if (file == null || url == null)
                return;

            Drawable pic = new BitmapDrawable(GlobalState.getContext().getResources(), file.getAbsolutePath());
            _picCache.put(url, new WeakReference<>(pic));
        }
    };

    private final ProfileClient.Listener _profileClient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subListMessages();
        }

        @Override
        public void onMessageList(List<Message> list, int page, boolean failed) {
            // TODO need to handle failed condition
            _adapter.setPage(page, list);
        }
    };
}
