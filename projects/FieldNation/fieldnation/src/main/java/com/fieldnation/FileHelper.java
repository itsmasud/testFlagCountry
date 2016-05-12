package com.fieldnation;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.Toast;

import com.fieldnation.utils.ISO8601;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by michael.carver on 11/20/2014.
 */
public class FileHelper {
    private static final String TAG = "FileHelper";

    public static void getFileFromActivityResult(Context context, Intent data, Listener listener) {

        if (data == null) {
            listener.fail("No data available");
            return;
        }

        try {
            File tempfile = null;

            if (data.getExtras() != null && data.getExtras().containsKey(MediaStore.EXTRA_OUTPUT)) {
                Log.v(TAG, data.getParcelableExtra(MediaStore.EXTRA_OUTPUT).toString());
                tempfile = new File(data.getParcelableExtra(MediaStore.EXTRA_OUTPUT).toString());
                listener.fileReady(tempfile.getName(), tempfile);
                return;
            } else {
                // generate temp file
                File temppath = new File(App.get().getDownloadsFolder() + "/temp");
                temppath.mkdirs();
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

                listener.fileReady(filename, tempfile);
                return;
            } else if (data.getData() != null) {
                Uri uri = data.getData();

                if (uri.getScheme().equals("file")) {
                    filename = uri.getLastPathSegment();
                } else {
                    Cursor c = context.getContentResolver().query(uri, null, null, null, null);
                    int nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    c.moveToFirst();
                    filename = c.getString(nameIndex);
                    c.close();
                }

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
        new AsyncTaskEx<Object, Object, Object>() {
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
        }.executeEx(context, url, filename, mimetype);
    }

    public static void viewOrDownloadFile(Context context, String url, String filename) {
        new AsyncTaskEx<Object, Object, Object>() {
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
        }.executeEx(context, url, filename);
    }

    public interface Listener {
        void fileReady(String filename, File file);

        void fromUri(String filename, Uri uri);

        void fail(String reason);
    }
}
