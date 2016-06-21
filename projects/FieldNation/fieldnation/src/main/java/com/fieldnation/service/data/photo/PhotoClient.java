package com.fieldnation.service.data.photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.AsyncTaskEx;
import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.utils.misc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoClient extends TopicClient implements PhotoConstants {
    private final String TAG = UniqueTag.makeTag("PhotoClient");

    private static final Hashtable<String, WeakReference<BitmapDrawable>> _pictureCache = new Hashtable<>();


    public PhotoClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void get(Context context, String url, boolean getCircle, boolean isSync) {
        if (misc.isEmptyOrNull(url))
            return;

        Intent intent = new Intent(context, PhotoService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        intent.putExtra(PARAM_CIRCLE, getCircle);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public static void clearPhotoClientCache() {
        _pictureCache.clear();
    }

    public boolean subGet(boolean getCircle, boolean isSync) {
        return subGet("", getCircle, isSync);
    }

    public boolean subGet(String url, boolean getCircle, boolean isSync) {
        String topicId = TOPIC_ID_GET_PHOTO;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (getCircle) {
            topicId += "/Circle";
        }

        topicId += url;

        return register(topicId, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            final Bundle bundle = (Bundle) payload;
            String action = bundle.getString(PARAM_ACTION);

            if (action.startsWith(PARAM_ACTION_GET))
                if (bundle.containsKey(PARAM_ERROR) && bundle.getBoolean(PARAM_ERROR)) {
                    onGet(bundle.getString(PARAM_URL),
                            null,
                            bundle.getBoolean(PARAM_CIRCLE), true);
                } else {
                    new AsyncTaskEx<Bundle, Object, BitmapDrawable>() {
                        @Override
                        protected BitmapDrawable doInBackground(Bundle... params) {
                            String url = bundle.getString(PARAM_URL);
                            boolean isCircle = bundle.getBoolean(PARAM_CIRCLE);
                            File file = (File) bundle.getSerializable(RESULT_IMAGE_FILE);

                            String key = isCircle + ":" + url;

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
                                if (file != null) {
                                    result = new BitmapDrawable(App.get().getResources(), file.getAbsolutePath());
                                    _pictureCache.put(key, new WeakReference<>(result));
                                }
                            }

                            return result;
                        }

                        @Override
                        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
                            onGet(bundle.getString(PARAM_URL),
                                    bitmapDrawable,
                                    bundle.getBoolean(PARAM_CIRCLE), false);
                        }
                    }.executeEx();
                }
        }


        public void onGet(String url, BitmapDrawable bitmapDrawable, boolean isCircle, boolean failed) {
        }
    }
}
