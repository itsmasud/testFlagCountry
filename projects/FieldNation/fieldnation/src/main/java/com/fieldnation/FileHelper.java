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
import android.util.Log;
import android.widget.Toast;

import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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

            Log.v(TAG, data.getParcelableExtra(MediaStore.EXTRA_OUTPUT).toString());

            if (data.getExtras().containsKey(MediaStore.EXTRA_OUTPUT)) {
                tempfile = new File(data.getParcelableExtra(MediaStore.EXTRA_OUTPUT).toString());
                listener.fileReady(tempfile.getName(), tempfile);
                return;
            } else {
                // generate temp file
                File externalPath = Environment.getExternalStorageDirectory();
                String packageName = context.getPackageName();
                File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");
                temppath.mkdirs();
                tempfile = File.createTempFile("DATA", null, temppath);
            }

            String filename = null;
            boolean gotData = false;

            if ("inline-data".equals(data.getAction())) {
                if (data.getExtras().getParcelable("data") instanceof Bitmap) {
                    Bitmap bitmap = data.getExtras().getParcelable("data");
                    FileOutputStream fout = new FileOutputStream(tempfile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
                    fout.close();
                    filename = "Image-" + ISO8601.now() + ".png";
                    gotData = true;
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();

                InputStream in = context.getContentResolver().openInputStream(uri);
                OutputStream out = new FileOutputStream(tempfile);
                misc.copyStream(in, out, 1024, -1, 500);
                out.close();
                in.close();
                gotData = true;

                if (uri.getScheme().equals("file")) {
                    filename = uri.getLastPathSegment();
                } else {
                    Cursor c = context.getContentResolver().query(uri, null, null, null, null);
                    int nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    c.moveToFirst();
                    filename = c.getString(nameIndex);
                    c.close();
                }

            }

            if (!gotData) {
                listener.fail("Could not get image data");
                return;
            }

            listener.fileReady(filename, tempfile);

//            context.startService(_service.uploadDeliverable(
//                    WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
//                    _uploadingSlot.getSlotId(), filename,
//                    tempfile, getNotificationIntent()));

        } catch (Exception ex) {
            ex.printStackTrace();
            listener.fail("Exception: " + ex.getMessage());
        }
    }

    public static void viewOrDownloadFile(Context context, String url, String filename, String mimetype) {
        Intent intent = null;

        if (mimetype != null) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), mimetype);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }

        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            context.startActivity(intent);
        } else {
            // http://stackoverflow.com/questions/525204/android-download-intent
            DownloadManager.Request r = new DownloadManager.Request(Uri.parse(url));
            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
            r.allowScanningByMediaScanner();
            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            dm.enqueue(r);
            Toast.makeText(context, "Downloading " + filename, Toast.LENGTH_LONG).show();
        }
    }


    public interface Listener {
        public void fileReady(String filename, File file);

        public void fail(String reason);
    }
}
