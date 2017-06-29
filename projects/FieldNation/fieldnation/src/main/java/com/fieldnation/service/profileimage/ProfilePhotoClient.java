package com.fieldnation.service.profileimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.ImageUtils;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by mc on 6/28/17.
 */

public class ProfilePhotoClient extends TopicClient implements ProfilePhotoConstants {
    private static final String STAG = "ProfilePhotoClient";
    private final String TAG = UniqueTag.makeTag("ProfilePhotoClient");

    public ProfilePhotoClient(Listener listener) {
        super(listener);
    }

    /**
     * Gets the profile image
     *
     * @param context
     */
    public static void get(Context context) {
        Intent intent = new Intent(context, ProfilePhotoService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        context.startService(intent);
    }

    /**
     * Uploads a new profile image
     *
     * @param context
     * @param uri
     */
    public static void upload(Context context, Uri uri) {
        Intent intent = new Intent(context, ProfilePhotoService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_UPLOAD);
        intent.putExtra(PARAM_UPLOAD_URI, uri);
        context.startService(intent);
    }

    static void dispatchGet(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_LOCAL_URI, uri);
        TopicService.dispatchEvent(context, TOPIC_ID_GET, bundle, Sticky.FOREVER);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onConnected() {
            getClient().register(TOPIC_ID_GET);
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Bundle bundle = (Bundle) payload;

            Uri uri = bundle.getParcelable(PARAM_LOCAL_URI);

            if (getProfileImage(uri)) {
                new AsyncTaskEx<Uri, Object, BitmapDrawable>() {

                    @Override
                    protected BitmapDrawable doInBackground(Uri... uris) {
                        Bitmap circle = null;
                        try {
                            Uri uri = uris[0];

                            Bitmap source = BitmapFactory.decodeStream(App.get().getContentResolver().openInputStream(uri));
                            Bitmap image = ImageUtils.resizeBitmap(source, 95, 95);
                            source.recycle();
                            circle = ImageUtils.extractCircle(image);
                            image.recycle();

                            return new BitmapDrawable(App.get().getResources(), ImageUtils.extractCircle(circle));
                        } catch (Exception ex) {
                            Log.v(STAG, ex);
                        } finally {
                            if (circle != null) circle.recycle();
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

        public abstract ProfilePhotoClient getClient();

        public abstract boolean getProfileImage(Uri uri);

        public abstract void onProfileImage(BitmapDrawable drawable);
    }
}
