package com.fieldnation.service.profileimage;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.auth.AuthClient;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;

/**
 * Created by mc on 6/28/17.
 */

public class ProfilePhotoSystem implements ProfilePhotoConstants {
    private static final String TAG = "ProfilePhotoSystem";

    // Data
    private Uri _currentProfileImage;

    private static ProfilePhotoSystem _instance = null;

    public static ProfilePhotoSystem getInstance() {
        synchronized (TAG) {
            if (_instance == null) _instance = new ProfilePhotoSystem();

            return _instance;
        }
    }

    private ProfilePhotoSystem() {
        _photoClient.sub();
        _profileClient.subGet();
        _authClient.subRemoveCommand();
    }

    public void shutdown() {
        _photoClient.unsub();
        _profileClient.unsubGet();
        _authClient.unsubRemoveCommand();
    }

    public void get(Context context, String url) {
        if (_currentProfileImage == null) {
            PhotoClient.get(context, url, true, false);
        } else {
            ProfilePhotoClient.dispatchGet(context, _currentProfileImage);
        }
    }

    public void upload(Context context, UUIDGroup uuid, Uri uri) {
        _currentProfileImage = uri;
        ProfileClient.uploadProfilePhoto(context, uuid, App.getProfileId(), FileUtils.getFileNameFromUri(App.get(), uri), uri);
        ProfilePhotoClient.dispatchGet(context, _currentProfileImage);
    }

    /*-**************************************-*/
    /*-             Data receivers           -*/
    /*-**************************************-*/
    private final PhotoClient _photoClient = new PhotoClient() {
        @Override
        public void imageDownloaded(String sourceUri, Uri localUri, boolean isCircle, boolean success) {
            if (isCircle
                    && App.getProfile() != null
                    && App.getProfile().getPhoto() != null
                    && !misc.isEmptyOrNull(  App.getProfile().getPhoto().getLarge())
                    && sourceUri.equals(App.getProfile().getPhoto().getLarge())) {
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

    private final ProfileClient _profileClient = new ProfileClient() {
        @Override
        public void onGet(Profile profile, boolean failed) {
            if (failed)
                return;

            // first run
            if (App.getProfile() == null) {
                if (App.getProfile() != null
                        && App.getProfile().getPhoto() != null
                        && !misc.isEmptyOrNull(App.getProfile().getPhoto().getLarge()))
                    PhotoClient.get(App.get(), App.getProfile().getPhoto().getLarge(), true, false);
            } else {
                // we already have a profile, this one is shit
                if (profile == null
                        || profile.getPhoto() == null
                        || misc.isEmptyOrNull(profile.getPhoto().getLarge()))
                    return;

                // bad existing profile object
                if (App.getProfile().getPhoto() == null || misc.isEmptyOrNull(App.getProfile().getPhoto().getLarge())) {
                    PhotoClient.get(App.get(), App.getProfile().getPhoto().getLarge(), true, false);
                    return;
                }

                // check new url against existing. If different then update
                String newUrl = profile.getPhoto().getLarge();
                if (!App.getProfile().getPhoto().getLarge().equals(newUrl)) {
                    PhotoClient.get(App.get(), App.getProfile().getPhoto().getLarge(), true, false);
                }
            }
        }
    };

    private final AuthClient _authClient = new AuthClient() {
        @Override
        public void onCommandRemove() {
            _currentProfileImage = null;
        }
    };
}
