package com.fieldnation.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.photo.PhotoClient;

import java.lang.ref.WeakReference;

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
        _profileContainerLayout = findViewById(R.id.profile_container);

        _picView = findViewById(R.id.pic_view);
        _picView.setProfilePic(R.drawable.missing_circle);
        _profileNameTextView = findViewById(R.id.profile_name_textview);
        _providerIdTextView = findViewById(R.id.providerid_textview);

        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(App.get());

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        // Log.v(TAG, "onDetachedFromWindow");
        if (_photoClient != null) _photoClient.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    public void setProfile(Profile profile) {
        // Log.v(TAG, "setProfile");
        _profile = profile;

        populateUi();
    }

    public long getUserId() {
        return _profile.getUserId();
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
            }
        } else {
            _picView.setProfilePic(_profilePic.get());
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/

    private final PhotoClient.Listener _photoClient_listener = new PhotoClient.Listener() {

        @Override
        public void onConnected() {
            super.onConnected();
            populateUi();
        }

        @Override
        public PhotoClient getClient() {
            return _photoClient;
        }

        @Override
        public void imageDownloaded(String sourceUri, Uri localUri, boolean isCircle, boolean success) {
        }

        @Override
        public boolean doGetImage(String sourceUri, boolean isCircle) {
            return isCircle
                    && _profile != null
                    && _profile.getPhoto() != null
                    && !misc.isEmptyOrNull(_profile.getPhoto().getThumb())
                    && (sourceUri.equals(_profile.getPhoto().getThumb()));
        }

        @Override
        public void onImageReady(String sourceUri, Uri localUri, BitmapDrawable drawable, boolean isCircle, boolean success) {
            if (drawable == null || misc.isEmptyOrNull(sourceUri) || !success)
                return;

            _profilePic = new WeakReference<Drawable>(drawable);
            addProfilePhoto();
        }
    };
}
