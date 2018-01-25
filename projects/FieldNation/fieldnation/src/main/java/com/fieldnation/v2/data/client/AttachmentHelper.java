package com.fieldnation.v2.data.client;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.FileHelper;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpFileContext;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.v2.data.model.Attachment;

/**
 * Created by mc on 4/19/17.
 */

public class AttachmentHelper {
    private static final String TAG = "AttachmentHelper";

    public static void addAttachment(final Context context, final UUIDGroup uuid, final int workOrderId, final Attachment attachment, Intent data) {
        Log.v(TAG, "addAttachment");
        FileHelper.getFileFromActivityResult(context, data, new FileHelper.Listener() {
            @Override
            public void fromUri(String filename, Uri uri) {
                addAttachment(context, uuid, workOrderId, attachment, filename, uri);
            }

            @Override
            public void fail(String reason) {
                Log.v("addAttachment", reason);
                ToastClient.toast(context, "Could not upload file", Toast.LENGTH_LONG);
            }
        });
    }

    public static void addAttachment(Context context, UUIDGroup uuid, int workOrderId, Attachment attachment, String filename, Uri uri) {
        Log.v(TAG, "addAttachment");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(uuid))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Attachment Helper"))
                .addContext(new SpFileContext.Builder().name(filename).build())
                .build());

        new AddAttachmentTask().executeEx(context, workOrderId, attachment, filename, uri, uuid);
    }

    private static class AddAttachmentTask extends AsyncTaskEx<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... objects) {
            Context context = (Context) objects[0];
            int workOrderId = (Integer) objects[1];
            Attachment attachment = (Attachment) objects[2];
            String filename = (String) objects[3];
            Uri uri = (Uri) objects[4];
            UUIDGroup uuid = (UUIDGroup) objects[5];

            Log.v(TAG, "processIntent " + workOrderId + ", " + attachment.getFolderId() + ", "
                    + filename + ", " + (uri == null ? "null" : uri.toString()));

            try {
                attachment.file(new com.fieldnation.v2.data.model.File().name(filename));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            if (uri != null) {
                try {
                    StoredObject cache = StoredObject.get(context, App.getProfileId(), "CacheFile", uri.toString());
                    if (cache == null) {
                        cache = StoredObject.put(context, App.getProfileId(), "TempFile", uri.toString(),
                                context.getContentResolver().openInputStream(uri), filename);
                    }
                    WorkordersWebApi.addAttachment(context, uuid, workOrderId, attachment.getFolderId(), attachment, filename, cache, App.get().getSpUiContext());

                    Tracker.event(App.get(), new CustomEvent.Builder()
                            .addContext(new SpTracingContext(uuid))
                            .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                            .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Attachment Helper"))
                            .addContext(new SpFileContext.Builder().name(filename).build())
                            .build());

                } catch (Exception ex) {
                    Tracker.event(App.get(), new CustomEvent.Builder()
                            .addContext(new SpTracingContext(uuid))
                            .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                            .addContext(new SpStatusContext(SpStatusContext.Status.FAIL, "Attachment Helper"))
                            .addContext(new SpFileContext.Builder().name(filename).build())
                            .build());
                    Log.v(TAG, ex);
                }
            }
            return null;
        }
    }
}
