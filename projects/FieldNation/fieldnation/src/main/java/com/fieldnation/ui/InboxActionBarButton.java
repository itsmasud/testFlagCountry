package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.ui.inbox.InboxActivity;
import com.fieldnation.ui.nav.AdditionalOptionsActivity;

import org.w3c.dom.Text;

public class InboxActionBarButton extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("InboxActionBarButton");

    // UI
    private TextView _inboxCountTextView;

    // data
    private Profile _profile = null;
    private GlobalTopicClient _globalTopicClient;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public InboxActionBarButton(Context context) {
        super(context);
        init();
    }

    public InboxActionBarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InboxActionBarButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_inbox_action_bar, this);

        if (isInEditMode())
            return;

        _inboxCountTextView = (TextView) findViewById(R.id.inboxCount_textview);

        setOnClickListener(_inbox_onClick);

        _globalTopicClient = new GlobalTopicClient(_globalTopicClient_listener);
        _globalTopicClient.connect(App.get());
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_globalTopicClient != null && _globalTopicClient.isConnected())
            _globalTopicClient.disconnect(App.get());
        super.onDetachedFromWindow();
    }


    private final View.OnClickListener _inbox_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            InboxActivity.startNew(v.getContext());
        }
    };

    private final GlobalTopicClient.Listener _globalTopicClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.subGotProfile();
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            refresh();
        }
    };

    private void refresh() {
        if (_profile == null)
            return;

        int count = _profile.getNewNotificationCount() + _profile.getUnreadMessageCount();

        if (count == 0) {
            _inboxCountTextView.setVisibility(GONE);
        } else {
            _inboxCountTextView.setVisibility(VISIBLE);
            if (count >= 99) {
                _inboxCountTextView.setText("!!");
            } else {
                _inboxCountTextView.setText(count + "");
            }
        }
        //TODO if is cached consider requesting a new version
    }
}