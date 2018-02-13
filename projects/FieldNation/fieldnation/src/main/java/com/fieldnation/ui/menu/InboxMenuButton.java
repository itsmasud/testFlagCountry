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
    private TextView _inboxTextView;

    // data
    private Profile _profile = null;

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
        LayoutInflater.from(getContext()).inflate(R.layout.menu_inbox_button, this);

        if (isInEditMode())
            return;

        _inboxTextView = findViewById(R.id.inbox_textview);
        _inboxCountTextView = findViewById(R.id.inboxCount_textview);

        setOnClickListener(_inbox_onClick);

        _profileClient.subGet();
        ProfileClient.get(App.get());

        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        _profileClient.unsubGet();
        super.onDetachedFromWindow();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            _inboxCountTextView.setVisibility(VISIBLE);
            setOnClickListener(_inbox_onClick);
        } else {
            _inboxCountTextView.setVisibility(GONE);
            setOnClickListener(null);
        }
        _inboxTextView.setEnabled(enabled);
    }

    private final View.OnClickListener _inbox_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SavedSearchTracker.onClick(App.get(), SavedSearchTracker.Item.INBOX);
            InboxActivity.startNew(v.getContext());
        }
    };

    private final ProfileClient _profileClient = new ProfileClient() {
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