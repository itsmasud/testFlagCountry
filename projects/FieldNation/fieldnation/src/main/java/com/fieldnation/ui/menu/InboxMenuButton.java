package com.fieldnation.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.SavedSearchTracker;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.inbox.InboxActivity;

public class InboxMenuButton extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("InboxActionBarButton");

    // UI
    private TextView _inboxCountTextView;

    // data
    private Profile _profile = null;
    private ProfileClient _profileClient;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public InboxMenuButton(Context context) {
        super(context);
        init();
    }

    public InboxMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InboxMenuButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_inbox_action_bar, this);

        if (isInEditMode())
            return;

        _inboxCountTextView = (TextView) findViewById(R.id.inboxCount_textview);

        setOnClickListener(_inbox_onClick);

        _profileClient = new ProfileClient(_profileCLient_listener);
        _profileClient.connect(App.get());

        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_profileClient != null && _profileClient.isConnected())
            _profileClient.disconnect(App.get());
        super.onDetachedFromWindow();
    }


    private final View.OnClickListener _inbox_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SavedSearchTracker.onClick(App.get(), SavedSearchTracker.Item.INBOX);
            InboxActivity.startNew(v.getContext());
        }
    };

    private final ProfileClient.Listener _profileCLient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subGet();
            ProfileClient.get(App.get());
        }

        @Override
        public void onGet(Profile profile, boolean failed) {
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