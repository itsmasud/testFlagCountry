package com.fieldnation.service.data.filecache;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.InputStreamMonitor;
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
        PigeonRoost.sub(this, ADDRESS_CACHE_FILE_PROGRESS);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_CACHE_FILE_START);
        PigeonRoost.unsub(this, ADDRESS_CACHE_FILE_END);
        PigeonRoost.unsub(this, ADDRESS_CACHE_FILE_PROGRESS);
    }

    public static void cacheFileUpload(Context context, final String tag, Uri uri) {
        Log.v(TAG, "cacheFileUpload");
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                Context context = (Context) params[0];
                Uri uri = (Uri) params[1];

                cacheFileStart(tag, uri);
                StoredObject upFile = null;

                try {
                    if ((upFile = StoredObject.get(context, App.getProfileId(), "CacheFile", uri.toString())) != null) {
                        cacheFileEnd(tag, upFile.getUri(), upFile.size(), true);
                        return null;
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

                try {
                    upFile = StoredObject.put(context, App.getProfileId(), "CacheFile", uri.toString(),
                            new InputStreamMonitor(
                                    context.getContentResolver().openInputStream(uri), new InputStreamMonitor.Monitor() {
                                long last = 0;

                                @Override
                                public void progress(int bytesRead) {
                                    if (System.currentTimeMillis() > last) {
                                        last = System.currentTimeMillis() + 1000;
                                        cacheFileProgress(tag, bytesRead);
                                    }
                                }
                            }), "uploadTemp.dat");
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                } finally {
                    if (upFile != null)
                        cacheFileEnd(tag, upFile.getUri(), upFile.size(), true);
                    else
                        cacheFileEnd(tag, uri, -1, false);
                }
                return null;
            }
        }.executeEx(context, uri);
    }

    private static void cacheFileStart(String tag, Uri uri) {
        Log.v(TAG, "cacheFileStart");
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);
        bundle.putString(PARAM_TAG, tag);

        PigeonRoost.sendMessage(ADDRESS_CACHE_FILE_START, bundle, Sticky.TEMP);
    }

    private static void cacheFileProgress(String tag, long position) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_TAG, tag);
        bundle.putLong(PARAM_POS, position);

        PigeonRoost.sendMessage(ADDRESS_CACHE_FILE_PROGRESS, bundle, Sticky.NONE);
    }

    private static void cacheFileEnd(String tag, Uri uri, long size, boolean success) {
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

        Bundle bundle = (Bundle) message;
        if (address.startsWith(ADDRESS_CACHE_FILE_START)) {
            onFileCacheStart(
                    bundle.getString(PARAM_TAG),
                    (Uri) bundle.getParcelable(PARAM_URI));
        } else if (address.startsWith(ADDRESS_CACHE_FILE_END)) {
            try {
                onFileCacheEnd(
                        bundle.getString(PARAM_TAG),
                        (Uri) bundle.getParcelable(PARAM_URI),
                        bundle.getLong(PARAM_SIZE),
                        bundle.getBoolean(PARAM_SUCCESS)
                );

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (address.startsWith(ADDRESS_CACHE_FILE_PROGRESS)) {
            try {
                onFileCacheProgress(bundle.getString(PARAM_TAG),
                        bundle.getLong(PARAM_POS));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onFileCacheStart(String tag, Uri uri) {
    }

    public void onFileCacheProgress(String tag, long size) {
    }

    public void onFileCacheEnd(String tag, Uri uri, long size, boolean success) {
    }
}
