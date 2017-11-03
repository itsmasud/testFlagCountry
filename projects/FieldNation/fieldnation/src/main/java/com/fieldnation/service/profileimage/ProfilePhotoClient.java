package com.fieldnation.service.profileimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.ImageUtils;
import com.fieldnation.fntools.MemUtils;

/**
 * Created by mc on 6/28/17.
 */

public abstract class ProfilePhotoClient extends Pigeon implements ProfilePhotoConstants {
    private static final String TAG = "ProfilePhotoClient";

    public void sub() {
        PigeonRoost.sub(this, ADDRESS_GET);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_GET);
    }

    /**
     * Gets the profile image
     *
     * @param context
     */
    public static void get(Context context) {
        try {
            ProfilePhotoSystem.getInstance().get(context, App.get().getProfile().getPhoto().getLarge());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Uploads a new profile image
     *
     * @param context
     * @param uri
     */
    public static void upload(Context context, UUIDGroup uuid, Uri uri) {
        ProfilePhotoSystem.getInstance().upload(context, uuid, uri);
    }

    static void dispatchGet(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_LOCAL_URI, uri);
        PigeonRoost.sendMessage(ADDRESS_GET, bundle, Sticky.FOREVER);
    }

    @Override
    public void onMessage(String topicId, Object payload) {
        Bundle bundle = (Bundle) payload;

        Uri uri = bundle.getParcelable(PARAM_LOCAL_URI);

        if (getProfileImage(uri)) {
            new AsyncTaskEx<Uri, Object, BitmapDrawable>() {

                @Override
                protected BitmapDrawable doInBackground(Uri... uris) {
                    try {
                        Uri uri = uris[0];

                        Bitmap image = MemUtils.getMemoryEfficientBitmap(App.get(), uri, 95);
                        Bitmap circle = ImageUtils.extractCircle(image);
                        image.recycle();

                        return new BitmapDrawable(App.get().getResources(), circle);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(BitmapDrawable drawable) {
                    onProfileImage(drawable);
                }
            }.executeEx(uri);
        }
    }

    public abstract boolean getProfileImage(Uri uri);

    public abstract void onProfileImage(BitmapDrawable drawable);
}

