package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;

/**
 * Created by Michael Carver on 2/23/2015.
 */
public class NavProfileDetailListView extends RelativeLayout {
    private static final String TAG = "NavProfileDetailListView";

    private LinearLayout _profileList;
    private LinearLayout _addLayout;
    private LinearLayout _manageLayout;

    private Profile _profile;
    private Listener _listener;

    public NavProfileDetailListView(Context context) {
        super(context);
        init();
    }

    public NavProfileDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavProfileDetailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_nav_profile_detail, this);

        if (isInEditMode())
            return;

        _profileList = (LinearLayout) findViewById(R.id.profile_list);

        _addLayout = (LinearLayout) findViewById(R.id.add_layout);
        _addLayout.setOnClickListener(_add_onClick);

        _manageLayout = (LinearLayout) findViewById(R.id.manage_layout);
        _manageLayout.setOnClickListener(_manage_onClick);

        populateUi();
    }

    public void setProfile(Profile profile) {
        _profile = profile;

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_profileList == null)
            return;

        if (_profile == null)
            return;

        Profile[] users = _profile.getManagedProviders();

        _profileList.removeAllViews();
        for (int i = 0; i < users.length; i++) {
            ProfileIndividualListLayout v = new ProfileIndividualListLayout(getContext());
            v.setProfile(users[i]);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userId = ((ProfileIndividualListLayout) v).getUserId();
                    if (_listener != null && _profile.getUserId() != userId)
                        _listener.onUserSwitch(userId);
                }
            });
            _profileList.addView(v);
        }

    }

    private final OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final OnClickListener _manage_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public interface Listener {
        void onUserSwitch(long userId);
    }
}
