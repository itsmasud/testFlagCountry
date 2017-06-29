package com.fieldnation.service.data.photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;

import java.lang.ref.WeakReference;
import java.util.Hashtable;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoClient extends TopicClient implements PhotoConstants {
    private static final String STAG = "PhotoClient";
    private final String TAG = UniqueTag.makeTag("PhotoClient");

    private static final Hashtable<String, WeakReference<BitmapDrawable>> _pictureCache = new Hashtable<>();

    public PhotoClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    /**
     * Gets the image at sourceUrl. Sends the request to PhotoService for lookup in the database or downloading
     *
     * @param context    The application context
     * @param sourceUrl  The URL of the image to look up (must be http or https)
     * @param makeCircle true if you want the circle crop, false for full image
     * @param isSync     true if this is a background process
     */
    public static void get(Context context, String sourceUrl, boolean makeCircle, boolean isSync) {
        if (misc.isEmptyOrNull(sourceUrl)) return;

        Intent intent = new Intent(context, PhotoService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        intent.putExtra(PARAM_SOURCE_URL, sourceUrl);
        intent.putExtra(PARAM_IS_CIRCLE, makeCircle);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public static void clearPhotoClientCache() {
        _pictureCache.clear();
    }

    public static abstract class Listener extends TopicClient.Listener {

        @Override
        public void onConnected() {
            getClient().register(TOPIC_ID_GET_PHOTO);
        }

        public abstract PhotoClient getClient();

        /**
         * Called when a get() operation finishes
         *
         * @param sourceUri The original URL used to get the file
         * @param localUri  The URI pointing to the local content
         * @param isCircle  true if circle cropped, false if full image
         * @param success   true if successful, false otherwise
         */
        public abstract void imageDownloaded(String sourceUri, Uri localUri, boolean isCircle, boolean success);

        /**
         * Called if the image download was successful.
         *
         * @param sourceUri the Uri that originated the request
         * @param isCircle  true if circle cropped, false if full image
         * @return true if you want a BitmapDrawable created for you. The drawable will be retruend in onImageReady
         */
        public abstract boolean doGetImage(String sourceUri, boolean isCircle);

        /**
         * Called after the bitmap has been created
         *
         * @param sourceUri
         * @param localUri
         * @param drawable
         * @param isCircle
         * @param success
         */
        public abstract void onImageReady(String sourceUri, Uri localUri, BitmapDrawable drawable, boolean isCircle, boolean success);

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Bundle bundle = (Bundle) payload;
            String action = bundle.getString(PARAM_ACTION);

            if (action.startsWith(PARAM_ACTION_GET)) {
                String sourceUrl = bundle.getString(PARAM_SOURCE_URL);
                Uri localUri = bundle.getParcelable(PARAM_CACHE_URI);
                boolean isCircle = bundle.getBoolean(PARAM_IS_CIRCLE);
                boolean success = bundle.getBoolean(PARAM_SUCCESS);

                imageDownloaded(sourceUrl, localUri, isCircle, success);

                if (success && doGetImage(sourceUrl, isCircle)) {
                    new AsyncTaskEx<Object, Object, BitmapDrawable>() {
                        String sourceUrl;
                        Uri localUri;
                        boolean isCircle;

                        @Override
                        protected BitmapDrawable doInBackground(Object... params) {
                            try {
                                sourceUrl = (String) params[0];
                                localUri = (Uri) params[1];
                                isCircle = (Boolean) params[2];

                                String key = isCircle + ":" + sourceUrl;

                                BitmapDrawable result = null;

                                if (_pictureCache.containsKey(key)) {
                                    WeakReference<BitmapDrawable> wr = _pictureCache.get(key);

                                    if (wr == null || wr.get() == null) {
                                        _pictureCache.remove(key);
                                    } else {
                                        result = wr.get();
                                    }
                                }

                                if (result == null) {
                                    result = new BitmapDrawable(App.get().getResources(), App.get().getContentResolver().openInputStream(localUri));
                                    _pictureCache.put(key, new WeakReference<>(result));
                                }

                                return result;
                            } catch (Exception ex) {
                                Log.v(STAG, ex);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
                            onImageReady(sourceUrl, localUri, bitmapDrawable, isCircle, bitmapDrawable != null);
                        }
                    }.executeEx(sourceUrl, localUri, isCircle);
                }
            }
        }
    }
}
