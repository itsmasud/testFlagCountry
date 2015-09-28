package com.fieldnation.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.ForLoopRunnable;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.workorder.detail.DiscountView;
import com.fieldnation.ui.workorder.detail.WorkorderRenderer;
import com.fieldnation.utils.misc;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created by Shoyeb Ahmed on 08/07/2015.
 */
public class ProfileIndividualListLayout extends RelativeLayout {
    private static final String TAG = "ProfileIndividualListLayout";

    // UI
    private LinearLayout _profileContainerLayout;
    private ProfilePicView _picView;
    private TextView _profileNameTextView;
    private TextView _providerIdTextView;

    // Data
    private Profile _profile;
    private PhotoClient _photoClient;
    private WeakReference<Drawable> _profilePic = null;

    public ProfileIndividualListLayout(Context context) {
        super(context);
        init();
    }

    public ProfileIndividualListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileIndividualListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_profile_individual, this);

        if (isInEditMode())
            return;

        // profile
        _profileContainerLayout = (LinearLayout) findViewById(R.id.profile_container);
        _profileContainerLayout.setOnClickListener(_profileContainerLayout_onClick);

        _picView = (ProfilePicView) findViewById(R.id.pic_view);
        _picView.setProfilePic(R.drawable.missing_circle);
        _profileNameTextView = (TextView) findViewById(R.id.profile_name_textview);
        _providerIdTextView = (TextView) findViewById(R.id.providerid_textview);

        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(getContext());

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        // Log.v(TAG, "onDetachedFromWindow");
        if (_photoClient != null && _photoClient.isConnected())
            _photoClient.disconnect(getContext());

        super.onDetachedFromWindow();
    }

    public void setProfile(Profile profile) {
        // Log.v(TAG, "setProfile");
        _profile = profile;

        subPhoto();
        populateUi();
    }

    private void populateUi() {
        // Log.v(TAG, "populateUi");

        if (_profile == null)
            return;

        if (_picView == null)
            return;

        // Log.v(TAG, "populateUi go");

        _providerIdTextView.setText(_profile.getUserId() + "");
        _profileNameTextView.setText(_profile.getFirstname() + " " + _profile.getLastname());
        subPhoto();
        addProfilePhoto();
    }

    private void subPhoto() {
        // Log.v(TAG, "subPhoto");
        if (_profile == null)
            return;

        if (_profile.getPhoto() == null)
            return;

        if (misc.isEmptyOrNull(_profile.getPhoto().getThumb()))
            return;

        if (_photoClient == null || !_photoClient.isConnected())
            return;

        // Log.v(TAG, "subPhoto go");
        _photoClient.subGet(_profile.getPhoto().getThumb(), true, false);

        addProfilePhoto();
    }

    private void addProfilePhoto() {
        // Log.v(TAG, "addProfilePhoto");
        if (_profile == null || _photoClient == null || !_photoClient.isConnected()) {
            _picView.setProfilePic(R.drawable.missing_circle);
            return;
        }

        if (_profilePic == null || _profilePic.get() == null) {
            _picView.setProfilePic(R.drawable.missing_circle);
            if (!misc.isEmptyOrNull(_profile.getPhoto().getThumb())) {
                PhotoClient.get(getContext(), _profile.getPhoto().getThumb(), true, false);
                // Log.v(TAG, "addProfilePhoto get");
            }
        } else {
            _picView.setProfilePic(_profilePic.get());
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final OnClickListener _profileContainerLayout_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SwitchUserActivity.startNew(getContext(), _profile.getUserId());
        }
    };

    private final PhotoClient.Listener _photoClient_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
            // Log.v(TAG, "_photoClient_listener.onConnected");
            subPhoto();
        }

        @Override
        public void onGet(String url, BitmapDrawable bitmapDrawable, boolean isCircle, boolean failed) {
            // Log.v(TAG, "_photoClient_listener.onGet, " + url);
            if (bitmapDrawable == null || url == null || failed)
                return;

            if (_profile.getPhoto() == null
                    || misc.isEmptyOrNull(_profile.getPhoto().getThumb())
                    || !url.equals(_profile.getPhoto().getThumb()))
                return;

            // Log.v(TAG, "_photoClient_listener.onGet go");
            _profilePic = new WeakReference<Drawable>(bitmapDrawable);
            addProfilePhoto();
        }
    };
}
