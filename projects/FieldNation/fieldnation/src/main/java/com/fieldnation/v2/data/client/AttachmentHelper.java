package com.fieldnation.v2.data.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.fieldnation.fntools.ImageUtils;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.v2.data.model.Attachment;

import java.io.InputStream;

/**
 * Created by mc on 4/19/17.
 */

public class AttachmentHelper {
    private static final String TAG = "AttachmentHelper";

    public static void addAttachment(final Context context, final UUIDGroup uuid, final int workOrderId, final Attachment attachment, Intent data, final boolean allowCompression) {
        Log.v(TAG, "addAttachment");
        FileHelper.getFileFromActivityResult(context, data, new FileHelper.Listener() {
            @Override
            public void fromUri(String filename, Uri uri) {
                addAttachment(context, uuid, workOrderId, attachment, filename, uri, allowCompression);
            }

            @Override
            public void fail(String reason) {
                Log.v("addAttachment", reason);
                ToastClient.toast(context, "Could not upload file", Toast.LENGTH_LONG);
            }
        });
    }

    public static void addAttachment(Context context, UUIDGroup uuid, int workOrderId, Attachment attachment, String filename, Uri uri, boolean allowCompression) {
        Log.v(TAG, "addAttachment");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(uuid))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Attachment Helper"))
                .addContext(new SpFileContext.Builder().name(filename).build())
                .build());

        new AddAttachmentTask().executeEx(context, workOrderId, attachment, filename, uri, uuid, allowCompression);
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
            boolean allowCompression = (Boolean) objects[6];

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
                    // TODO, if image... check size and compress if option is on

                    if (allowCompression) {
                        Tracker.event(App.get(), new CustomEvent.Builder()
                                .addContext(new SpTracingContext(uuid))
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Attachment Helper - Compression enabled"))
                                .addContext(new SpFileContext.Builder().name(filename).build())
                                .build());
                        try {
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            InputStream is = context.getContentResolver().openInputStream(cache.getUri());
                            int size = is.available();
                            BitmapFactory.decodeStream(is, null, options);
                            is.close();

                            int h = options.outHeight;
                            int w = options.outWidth;

                            if (h * w > 5000000L || size > 2000000) {
                                if (filename.contains("."))
                                    filename = filename.substring(0, filename.lastIndexOf(".")) + ".jpg";

                                Bitmap bitmap = null;
                                if (h * w > 5000000L)
                                    bitmap = ImageUtils.getScalledBitmap(context, cache.getUri(), 5000000L);
                                else
                                    bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(cache.getUri()));

                                bitmap = MemUtils.rotateImageIfRequired(context, bitmap, cache.getUri());
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, context.getContentResolver().openOutputStream(cache.getUri()));
                                bitmap.recycle();
                                Log.v(TAG, "File Compressed");
                                Tracker.event(App.get(), new CustomEvent.Builder()
                                        .addContext(new SpTracingContext(uuid))
                                        .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                        .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Attachment Helper - Image Compressed"))
                                        .addContext(new SpFileContext.Builder().name(filename).build())
                                        .build());
                            } else {
                                Tracker.event(App.get(), new CustomEvent.Builder()
                                        .addContext(new SpTracingContext(uuid))
                                        .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                        .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Attachment Helper - Not compressing file. Already small or not an image."))
                                        .addContext(new SpFileContext.Builder().name(filename).build())
                                        .build());
                            }
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                            Tracker.event(App.get(), new CustomEvent.Builder()
                                    .addContext(new SpTracingContext(uuid))
                                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                    .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Attachment Helper - Not an image, or compression failed."))
                                    .addContext(new SpFileContext.Builder().name(filename).build())
                                    .build());
                        }
                    } else {
                        Tracker.event(App.get(), new CustomEvent.Builder()
                                .addContext(new SpTracingContext(uuid))
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Attachment Helper - Not compressing file"))
                                .addContext(new SpFileContext.Builder().name(filename).build())
                                .build());

                    }
                    attachment.getFile().name(filename);
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
