package com.fieldnation.service.profileimage;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.MultiThreadedService;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;

/**
 * Created by mc on 6/28/17.
 */

public class ProfilePhotoService extends MultiThreadedService implements ProfilePhotoConstants {
    private static final String TAG = "ProfilePhotoService";

    // Data
    private Profile _profile = null;
    private Uri _currentProfileImage;

    // Clients
    private PhotoClient _photoClient;
    private ProfileClient _profileClient;

    @Override
    public void onCreate() {
        super.onCreate();
        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(App.get());
        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(App.get());
    }

    @Override
    public void onDestroy() {
        if (_photoClient != null) _photoClient.disconnect(App.get());
        if (_profileClient != null) _profileClient.disconnect(App.get());

        super.onDestroy();
    }

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public void processIntent(Intent intent) {
        if (intent == null || !intent.hasExtra(PARAM_ACTION))
            return;

        String action = intent.getStringExtra(PARAM_ACTION);
        switch (action) {
            case PARAM_ACTION_GET:
                doGetImage();
                break;
            case PARAM_ACTION_UPLOAD:
                doUploadImage((Uri) intent.getParcelableExtra(PARAM_UPLOAD_URI));
                break;
        }
    }

    private void doGetImage() {
        if (_currentProfileImage == null) {
            ProfileClient.get(this);
        } else {
            ProfilePhotoClient.dispatchGet(this, _currentProfileImage);
        }
    }

    private void doUploadImage(Uri uri) {
        _currentProfileImage = uri;
        ProfileClient.uploadProfilePhoto(this, App.get().getProfile().getUserId(), FileUtils.getFileNameFromUri(this, uri), uri);

        ProfilePhotoClient.dispatchGet(this, _currentProfileImage);
    }

    /*-**************************************-*/
    /*-             Data receivers           -*/
    /*-**************************************-*/
    private final PhotoClient.Listener _photoClient_listener = new PhotoClient.Listener() {
        @Override
        public PhotoClient getClient() {
            return _photoClient;
        }

        @Override
        public void imageDownloaded(String sourceUri, Uri localUri, boolean isCircle, boolean success) {
            if (isCircle
                    && _profile != null
                    && _profile.getPhoto() != null
                    && !misc.isEmptyOrNull(_profile.getPhoto().getLarge())
                    && sourceUri.equals(_profile.getPhoto().getLarge())) {
                _currentProfileImage = localUri;
                ProfilePhotoClient.dispatchGet(App.get(), _currentProfileImage);
            }
        }

        @Override
        public boolean doGetImage(String sourceUri, boolean isCircle) {
            return false;
        }

        @Override
        public void onImageReady(String sourceUri, Uri localUri, BitmapDrawable drawable, boolean isCircle, boolean success) {
            // We don't want the image
        }
    };

    private final ProfileClient.Listener _profileClient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subGet();
        }

        @Override
        public void onGet(Profile profile, boolean failed) {
            if (failed)
                return;

            // first run
            if (_profile == null) {
                _profile = profile;

                if (_profile != null
                        && _profile.getPhoto() != null
                        && !misc.isEmptyOrNull(_profile.getPhoto().getLarge()))
                    PhotoClient.get(App.get(), _profile.getPhoto().getLarge(), true, false);
            } else {
                // we already have a profile, this one is shit
                if (profile == null
                        || profile.getPhoto() == null
                        || misc.isEmptyOrNull(profile.getPhoto().getLarge()))
                    return;

                // bad existing profile object
                if (_profile.getPhoto() == null || misc.isEmptyOrNull(_profile.getPhoto().getLarge())) {
                    _profile = profile;
                    PhotoClient.get(App.get(), _profile.getPhoto().getLarge(), true, false);
                    return;
                }

                // check new url against existing. If different then update
                String newUrl = profile.getPhoto().getLarge();
                if (!_profile.getPhoto().getLarge().equals(newUrl)) {
                    _profile = profile;
                    PhotoClient.get(App.get(), _profile.getPhoto().getLarge(), true, false);
                }
            }
        }
    };
}
