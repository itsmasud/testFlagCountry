package com.fieldnation.service.data.filecache;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by mc on 12/19/16.
 */

public class FileCacheClient extends TopicClient implements FileCacheConstants {
    private static final String STAG = "FileCacheClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public FileCacheClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subFileCache() {
        return register(TOPIC_ID_CACHE_FILE_START)
                && register(TOPIC_ID_CACHE_FILE_END);
    }

    public static void cacheFileUpload(Context context, Uri uri) {
        Log.v(STAG, "cacheFileUpload");
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                Context context = (Context) params[0];
                Uri uri = (Uri) params[1];

                cacheFileStart(context, uri);
                StoredObject upFile = null;
                try {
                    upFile = StoredObject.put(context, App.getProfileId(), "CacheFile", uri.toString(),
                            context.getContentResolver().openInputStream(uri), "uploadTemp.dat");
                } catch (Exception ex) {
                    Log.v(STAG, ex);
                } finally {
                    if (upFile != null)
                        cacheFileEnd(context, uri, upFile.getFile().toString());
                    else
                        cacheFileEnd(context, uri, null);
                }
                return null;
            }
        }.executeEx(context, uri);
    }

    private static void cacheFileStart(Context context, Uri uri) {
        Log.v(STAG, "cacheFileStart");
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);

        TopicService.dispatchEvent(context, TOPIC_ID_CACHE_FILE_START, bundle, Sticky.TEMP);
    }

    private static void cacheFileEnd(Context context, Uri uri, String file) {
        Log.v(STAG, "cacheFileStart");
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);
        bundle.putString(PARAM_FILE, file);

        TopicService.dispatchEvent(context, TOPIC_ID_CACHE_FILE_END, bundle, Sticky.TEMP);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "topicId " + topicId);

            if (topicId.startsWith(TOPIC_ID_CACHE_FILE_START)) {
                onFileCacheStart((Uri) ((Bundle) payload).getParcelable(PARAM_URI));
            } else if (topicId.startsWith(TOPIC_ID_CACHE_FILE_END)) {
                try {
                    onFileCacheEnd(
                            (Uri) ((Bundle) payload).getParcelable(PARAM_URI),
                            ((Bundle) payload).getString(PARAM_FILE));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void onFileCacheStart(Uri uri) {
        }

        public void onFileCacheEnd(Uri uri, String filename) {
        }
    }
}