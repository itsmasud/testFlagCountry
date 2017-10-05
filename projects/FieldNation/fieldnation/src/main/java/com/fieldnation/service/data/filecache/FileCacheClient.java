package com.fieldnation.service.data.filecache;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;

/**
 * Created by mc on 12/19/16.
 */

public class FileCacheClient extends Pigeon implements FileCacheConstants {
    private static final String TAG = "FileCacheClient";

    public void sub() {
        PigeonRoost.sub(this, ADDRESS_CACHE_FILE_START);
        PigeonRoost.sub(this, ADDRESS_CACHE_FILE_END);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_CACHE_FILE_START);
        PigeonRoost.unsub(this, ADDRESS_CACHE_FILE_END);
    }

    public static void cacheFileUpload(Context context, final String tag, Uri uri) {
        Log.v(TAG, "cacheFileUpload");
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                Context context = (Context) params[0];
                Uri uri = (Uri) params[1];

                cacheFileStart(context, tag, uri);
                StoredObject upFile = null;
                try {
                    upFile = StoredObject.put(context, App.getProfileId(), "CacheFile", uri.toString(),
                            context.getContentResolver().openInputStream(uri), "uploadTemp.dat");
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                } finally {
                    if (upFile != null)
                        cacheFileEnd(context, tag, upFile.getUri(), upFile.size(), true);
                    else
                        cacheFileEnd(context, tag, uri, -1, false);
                }
                return null;
            }
        }.executeEx(context, uri);
    }

    private static void cacheFileStart(Context context, String tag, Uri uri) {
        Log.v(TAG, "cacheFileStart");
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);
        bundle.putString(PARAM_TAG, tag);

        PigeonRoost.sendMessage(ADDRESS_CACHE_FILE_START, bundle, Sticky.TEMP);
    }

    private static void cacheFileEnd(Context context, String tag, Uri uri, long size, boolean success) {
        Log.v(TAG, "cacheFileEnd");
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);
        bundle.putString(PARAM_TAG, tag);
        bundle.putBoolean(PARAM_SUCCESS, success);
        bundle.putLong(PARAM_SIZE, size);

        PigeonRoost.sendMessage(ADDRESS_CACHE_FILE_END, bundle, Sticky.TEMP);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/

    @Override
    public void onMessage(String address, Object message) {
        Log.v(TAG, "address " + address);

        if (address.startsWith(ADDRESS_CACHE_FILE_START)) {
            Bundle bundle = (Bundle) message;
            onFileCacheStart(
                    bundle.getString(PARAM_TAG),
                    (Uri) bundle.getParcelable(PARAM_URI));
        } else if (address.startsWith(ADDRESS_CACHE_FILE_END)) {
            try {
                Bundle bundle = (Bundle) message;
                onFileCacheEnd(
                        bundle.getString(PARAM_TAG),
                        (Uri) bundle.getParcelable(PARAM_URI),
                        bundle.getLong(PARAM_SIZE),
                        bundle.getBoolean(PARAM_SUCCESS)
                );

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onFileCacheStart(String tag, Uri uri) {
    }

    public void onFileCacheEnd(String tag, Uri uri, long size, boolean success) {
    }
}
