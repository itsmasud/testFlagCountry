package com.fieldnation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;

import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by michael.carver on 11/20/2014.
 */
public class FileReceiver {

    public static void fileFromActivityResult(Context context, Intent data, Listener listener) {
        if (data == null) {
            listener.fail("No data available");
            return;
        }

        try {
            // generate temp file
            File externalPath = Environment.getExternalStorageDirectory();
            String packageName = context.getPackageName();
            File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");
            temppath.mkdirs();
            File tempfile = File.createTempFile("DATA", null, temppath);


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


    public interface Listener {
        public void fileReady(String filename, File file);

        public void fail(String reason);
    }
}
