package com.fieldnation.service.data.filecache;

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

    public static void cacheFileUpload(String uuid, final String tag, Uri uri) {
        Log.v(TAG, "cacheFileUpload");
        // TODO analytics
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                Uri uri = (Uri) params[0];
                final String uuid = (String) params[1];

                cacheFileStart(uuid, tag, uri);
                StoredObject upFile = null;

                try {
                    if ((upFile = StoredObject.get(App.get(), App.getProfileId(), "CacheFile", uri.toString())) != null) {
                        cacheFileEnd(uuid, tag, upFile.getUri(), upFile.size(), true);
                        return null;
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

                try {
                    upFile = StoredObject.put(App.get(), App.getProfileId(), "CacheFile", uri.toString(),
                            new InputStreamMonitor(
                                    App.get().getContentResolver().openInputStream(uri), new InputStreamMonitor.Monitor() {
                                long last = 0;

                                @Override
                                public void progress(int bytesRead) {
                                    if (System.currentTimeMillis() > last) {
                                        last = System.currentTimeMillis() + 1000;
                                        cacheFileProgress(uuid, tag, bytesRead);
                                    }
                                }
                            }), "uploadTemp.dat");
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                } finally {
                    // TODO analytics
                    if (upFile != null)
                        cacheFileEnd(uuid, tag, upFile.getUri(), upFile.size(), true);
                    else
                        cacheFileEnd(uuid, tag, uri, -1, false);
                }
                return null;
            }
        }.executeEx(uri, uuid);
    }

    private static void cacheFileStart(String uuid, String tag, Uri uri) {
        Log.v(TAG, "cacheFileStart");
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);
        bundle.putString(PARAM_TAG, tag);
        bundle.putString(PARAM_UUID, uuid);

        PigeonRoost.sendMessage(ADDRESS_CACHE_FILE_START, bundle, Sticky.TEMP);
    }

    private static void cacheFileProgress(String uuid, String tag, long position) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_TAG, tag);
        bundle.putLong(PARAM_POS, position);
        bundle.putString(PARAM_UUID, uuid);

        PigeonRoost.sendMessage(ADDRESS_CACHE_FILE_PROGRESS, bundle, Sticky.NONE);
    }

    private static void cacheFileEnd(String uuid, String tag, Uri uri, long size, boolean success) {
        Log.v(TAG, "cacheFileEnd");
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);
        bundle.putString(PARAM_TAG, tag);
        bundle.putBoolean(PARAM_SUCCESS, success);
        bundle.putLong(PARAM_SIZE, size);
        bundle.putString(PARAM_UUID, uuid);

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
                    bundle.getString(PARAM_UUID),
                    bundle.getString(PARAM_TAG),
                    (Uri) bundle.getParcelable(PARAM_URI));
        } else if (address.startsWith(ADDRESS_CACHE_FILE_END)) {
            try {
                onFileCacheEnd(
                        bundle.getString(PARAM_UUID),
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
                onFileCacheProgress(
                        bundle.getString(PARAM_UUID),
                        bundle.getString(PARAM_TAG),
                        bundle.getLong(PARAM_POS));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onFileCacheStart(String uuid, String tag, Uri uri) {
    }

    public void onFileCacheProgress(String uuid, String tag, long size) {
    }

    public void onFileCacheEnd(String uuid, String tag, Uri uri, long size, boolean success) {
    }
}
