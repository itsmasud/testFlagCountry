package com.fieldnation.service.data.filecache;

import android.net.Uri;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.InputStreamMonitor;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpFileContext;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.FileUtils;

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

    private static class UploadTask extends AsyncTaskEx<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            Uri uri = (Uri) params[0];
            final UUIDGroup uuid = (UUIDGroup) params[1];

            cacheFileStart(uuid, uri);
            StoredObject upFile = null;

            try {
                if ((upFile = StoredObject.get(App.get(), App.getProfileId(), "CacheFile", uuid.uuid)) != null) {
                    cacheFileEnd(uuid, upFile.getUri(), upFile.size(), true);
                    return null;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            try {
                upFile = StoredObject.put(App.get(), App.getProfileId(), "CacheFile", uuid.uuid,
                        new InputStreamMonitor(
                                App.get().getContentResolver().openInputStream(uri), new InputStreamMonitor.Monitor() {
                            long last = 0;

                            @Override
                            public void progress(int bytesRead) {
                                if (System.currentTimeMillis() > last) {
                                    last = System.currentTimeMillis() + 1000;
                                    cacheFileProgress(uuid, bytesRead);
                                }
                            }
                        }), "uploadTemp.dat");
            } catch (Exception ex) {
                Log.v(TAG, ex);
            } finally {
                if (upFile != null)
                    cacheFileEnd(uuid, upFile.getUri(), upFile.size(), true);
                else
                    cacheFileEnd(uuid, uri, -1, false);
            }
            return null;
        }
    }

    public static void cacheFileUpload(UUIDGroup uuid, Uri uri) {
        Log.v(TAG, "cacheFileUpload");
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(uuid))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "File Cache"))
                .addContext(new SpFileContext.Builder()
                        .name(FileUtils.getFileNameFromUri(App.get(), uri))
                        .size(0)
                        .createdAt(System.currentTimeMillis() / 1000)
                        .build())
                .build());
        new UploadTask().executeEx(uri, uuid);
    }

    private static void cacheFileStart(UUIDGroup uuid, Uri uri) {
        Log.v(TAG, "cacheFileStart");
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);
        bundle.putParcelable(PARAM_UUID, uuid);

        PigeonRoost.sendMessage(ADDRESS_CACHE_FILE_START, bundle, Sticky.TEMP);
    }

    private static void cacheFileProgress(UUIDGroup uuid, long position) {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_POS, position);
        bundle.putParcelable(PARAM_UUID, uuid);

        PigeonRoost.sendMessage(ADDRESS_CACHE_FILE_PROGRESS, bundle, Sticky.NONE);
    }

    private static void cacheFileEnd(UUIDGroup uuid, Uri uri, long size, boolean success) {
        Log.v(TAG, "cacheFileEnd");
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(uuid))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Cache complete"))
                .addContext(new SpFileContext.Builder()
                        .name(FileUtils.getFileNameFromUri(App.get(), uri))
                        .size((int) size)
                        .createdAt(System.currentTimeMillis() / 1000)
                        .build())
                .build());

        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_URI, uri);
        bundle.putBoolean(PARAM_SUCCESS, success);
        bundle.putLong(PARAM_SIZE, size);
        bundle.putParcelable(PARAM_UUID, uuid);

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
                    (UUIDGroup) bundle.getParcelable(PARAM_UUID),
                    (Uri) bundle.getParcelable(PARAM_URI));
        } else if (address.startsWith(ADDRESS_CACHE_FILE_END)) {
            try {
                onFileCacheEnd(
                        (UUIDGroup) bundle.getParcelable(PARAM_UUID),
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
                        (UUIDGroup) bundle.getParcelable(PARAM_UUID),
                        bundle.getLong(PARAM_POS));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onFileCacheStart(UUIDGroup uuid, Uri uri) {
    }

    public void onFileCacheProgress(UUIDGroup uuid, long size) {
    }

    public void onFileCacheEnd(UUIDGroup uuid, Uri uri, long size, boolean success) {
    }
}
