package com.fieldnation.analytics.trackers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fieldnation.App;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.analytics.trackers.AttachmentTrackerSQLHelper.Column;

/**
 * Created by mc on 10/31/17.
 */

public class AttachmentTracker {
    private static final String TAG = "AttachmentTracker";

    private static boolean parentExists(String uuid) {
        Log.v(TAG, "parentExists");
        synchronized (TAG) {
            AttachmentTrackerSQLHelper helper = AttachmentTrackerSQLHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(AttachmentTrackerSQLHelper.TABLE_NAME,
                            AttachmentTrackerSQLHelper.getColumnNames(),
                            Column.PARENT_UUID + " = ?",
                            new String[]{uuid},
                            null, null, null, "1");
                    return cursor.getCount() > 0;
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
    }

    private static void addChild(UUIDGroup uuidGroup) {
        Log.v(TAG, "addChild");
        ContentValues v = new ContentValues();
        v.put(Column.PARENT_UUID.getName(), uuidGroup.parentUUID);
        v.put(Column.UUID.getName(), uuidGroup.uuid);

        synchronized (TAG) {
            AttachmentTrackerSQLHelper helper = AttachmentTrackerSQLHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.insert(AttachmentTrackerSQLHelper.TABLE_NAME, null, v);
            } finally {
                if (db != null) db.close();
            }
        }
    }

    private static void removeChild(UUIDGroup uuidGroup) {
        Log.v(TAG, "removeChild");
        synchronized (TAG) {
            AttachmentTrackerSQLHelper helper = AttachmentTrackerSQLHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getWritableDatabase();

            try {
                db.delete(
                        AttachmentTrackerSQLHelper.TABLE_NAME,
                        Column.UUID + " = ?",
                        new String[]{uuidGroup.uuid});
            } finally {
                if (db != null) db.close();
            }
        }
    }

    public static void start(Context context, UUIDGroup uuidGroup) {
        Log.v(TAG, "start");
        if (misc.isEmptyOrNull(uuidGroup.parentUUID))
            return; // not a child process

        if (!parentExists(uuidGroup.parentUUID)) {
            Tracker.event(context, new CustomEvent.Builder()
                    .addContext(new SpTracingContext("AttachmentTracker", uuidGroup))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.START, "Start"))
                    .build());
        }
        addChild(uuidGroup);
    }

    public static void complete(Context context, UUIDGroup uuidGroup) {
        Log.v(TAG, "complete");
        if (misc.isEmptyOrNull(uuidGroup.parentUUID))
            return; // not a child process

        if (!parentExists(uuidGroup.parentUUID))
            return; // parent doesn't exist already

        removeChild(uuidGroup);

        if (!parentExists(uuidGroup.parentUUID)) {
            Tracker.event(context, new CustomEvent.Builder()
                    .addContext(new SpTracingContext("AttachmentTracker", uuidGroup))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "All Complete"))
                    .build());
        }
    }
}
