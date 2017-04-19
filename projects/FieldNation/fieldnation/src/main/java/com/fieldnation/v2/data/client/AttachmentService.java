package com.fieldnation.v2.data.client;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.FileHelper;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.MultiThreadedService;
import com.fieldnation.v2.data.model.Attachment;

import java.io.File;

/**
 * Created by mc on 4/19/17.
 */

public class AttachmentService extends MultiThreadedService {
    private static final String TAG = "AttachmentService";

    public static final String PARAM_WORKORDER_ID = "PARAM_WORKORDER_ID";
    public static final String PARAM_ATTACHMENT = "PARAM_ATTACHMENT";
    public static final String PARAM_DATA = "PARAM_DATA";
    public static final String PARAM_DESCRIPTION = "PARAM_DESCRIPTION";
    public static final String PARAM_FILE_NAME = "PARAM_FILE_NAME";
    public static final String PARAM_URI = "PARAM_URI";
    public static final String PARAM_PATH = "PARAM_PATH";

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public void processIntent(Intent intent) {
        int workOrderId = intent.getIntExtra(PARAM_WORKORDER_ID, 0);
        Attachment attachment = intent.getParcelableExtra(PARAM_ATTACHMENT);
        String filename = intent.getStringExtra(PARAM_FILE_NAME);
        String filePath = intent.getStringExtra(PARAM_PATH);
        Uri uri = intent.getParcelableExtra(PARAM_URI);

        Log.v(TAG, "processIntent " + workOrderId + ", " + attachment.getFolderId() + ", "
                + filename + ", " + filePath + ", " + uri.toString());

        try {
            attachment.file(new com.fieldnation.v2.data.model.File().name(filename));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (uri != null) {
            try {
                StoredObject cache = StoredObject.get(this, App.getProfileId(), "CacheFile", uri.toString());
                if (cache == null) {
                    cache = StoredObject.put(this, App.getProfileId(), "TempFile", uri.toString(),
                            this.getContentResolver().openInputStream(uri), filename);
                }
                WorkordersWebApi.addAttachment(this, workOrderId, attachment.getFolderId(), attachment, filename, cache);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else
            WorkordersWebApi.addAttachment(this, workOrderId, attachment.getFolderId(), attachment, new File(filePath));
    }


    public static void addAttachment(final Context context, final int workOrderId, final Attachment attachment, Intent data) {

        FileHelper.getFileFromActivityResult(context, data, new FileHelper.Listener() {
            @Override
            public void fileReady(String filename, File file) {
                addAttachment(context, workOrderId, attachment, filename, file.getPath());
            }

            @Override
            public void fromUri(String filename, Uri uri) {
                addAttachment(context, workOrderId, attachment, filename, uri);
            }

            @Override
            public void fail(String reason) {
                Log.v("addAttachment", reason);
                ToastClient.toast(context, "Could not upload file", Toast.LENGTH_LONG);
            }
        });
    }

    public static void addAttachment(Context context, int workOrderId, Attachment attachment, String filename, Uri uri) {
        Intent intent = new Intent(context, AttachmentService.class);
        intent.putExtra(PARAM_WORKORDER_ID, workOrderId);
        intent.putExtra(PARAM_ATTACHMENT, attachment);
        intent.putExtra(PARAM_FILE_NAME, filename);
        intent.putExtra(PARAM_URI, uri);
        context.startService(intent);
    }

    public static void addAttachment(Context context, int workOrderId, Attachment attachment, String filename, String path) {
        Intent intent = new Intent(context, AttachmentService.class);
        intent.putExtra(PARAM_WORKORDER_ID, workOrderId);
        intent.putExtra(PARAM_ATTACHMENT, attachment);
        intent.putExtra(PARAM_FILE_NAME, filename);
        intent.putExtra(PARAM_PATH, path);
        context.startService(intent);
    }

}
