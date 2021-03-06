package com.fieldnation;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.ISO8601;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by michael.carver on 11/20/2014.
 */
public class FileHelper {
    private static final String TAG = "FileHelper";

    public static void getFileFromActivityResult(Context context, Intent data, Listener listener) {
        // TODO this might need to be in an asynctask
        if (data == null) {
            listener.fail("No data available");
            return;
        }

        try {
            File tempfile = null;

            if (data.getExtras() != null && data.getExtras().containsKey(MediaStore.EXTRA_OUTPUT)) {
                Log.v(TAG, data.getParcelableExtra(MediaStore.EXTRA_OUTPUT).toString());
                tempfile = new File(data.getParcelableExtra(MediaStore.EXTRA_OUTPUT).toString());
                listener.fromUri(tempfile.getName(), App.getUriFromFile(tempfile));
                return;
            } else {
                // generate temp file
                File temppath = new File(App.get().getTempFolder());
                tempfile = File.createTempFile("DATA", null, temppath);
            }

            String filename = null;

            if ("inline-data".equals(data.getAction())) {
                if (data.getExtras().getParcelable("data") instanceof Bitmap) {
                    Bitmap bitmap = data.getExtras().getParcelable("data");
                    FileOutputStream fout = new FileOutputStream(tempfile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
                    fout.close();
                    filename = "Image-" + ISO8601.now() + ".png";
                    bitmap.recycle();
                }

                listener.fromUri(filename, App.getUriFromFile(tempfile));
                return;
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                filename = FileUtils.getFileNameFromUri(App.get(), uri);

                listener.fromUri(filename, uri);
                return;
            }

            listener.fail("Could not get image data");


//            context.startService(_service.uploadDeliverable(
//                    WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
//                    _uploadingSlot.getSlotId(), filename,
//                    tempfile, getNotificationIntent()));

        } catch (Exception ex) {
            Log.v(TAG, ex);
            listener.fail("Exception: " + ex.getMessage());
        }
    }

    public static void viewOrDownloadFile(Context context, String url, String filename, String mimetype) {
        new DownloadAsync1().executeEx(context, url, filename, mimetype);
    }

    private static class DownloadAsync1 extends AsyncTaskEx<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            Context context = (Context) params[0];
            String url = (String) params[1];
            String filename = (String) params[2];
            String mimetype = (String) params[3];
            Intent intent = null;

            if (mimetype != null) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), mimetype);
            } else {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            boolean doDownload = true;
            try {
                if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    context.startActivity(intent);
                    doDownload = false;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            if (doDownload) {
                // http://stackoverflow.com/questions/525204/android-download-intent
                DownloadManager.Request r = new DownloadManager.Request(Uri.parse(url));
                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                r.allowScanningByMediaScanner();
                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                dm.enqueue(r);
                try {
                    Toast.makeText(context, context.getString(R.string.toast_downloading) + " " + filename, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }

            return null;
        }
    }

    public static void viewOrDownloadFile(Context context, String url, String filename) {
        new DownloadAsync2().executeEx(context, url, filename);
    }

    private static class DownloadAsync2 extends AsyncTaskEx<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            Context context = (Context) params[0];
            String url = (String) params[1];
            String filename = (String) params[2];
            Intent intent = null;

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            boolean doDownload = true;
            try {
                if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    context.startActivity(intent);
                    doDownload = false;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            if (doDownload) {
                // http://stackoverflow.com/questions/525204/android-download-intent
                DownloadManager.Request r = new DownloadManager.Request(Uri.parse(url));
                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                r.allowScanningByMediaScanner();
                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                dm.enqueue(r);
                try {
                    Toast.makeText(context, context.getString(R.string.toast_downloading) + " " + filename, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }

            return null;
        }
    }

    public interface Listener {
        void fromUri(String filename, Uri uri);

        void fail(String reason);
    }
}
