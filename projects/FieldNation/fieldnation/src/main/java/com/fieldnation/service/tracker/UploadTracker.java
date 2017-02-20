package com.fieldnation.service.tracker;

import android.app.PendingIntent;
import android.content.Context;

/**
 * Created by mc on 2/20/17.
 */

public interface UploadTracker {
    void update(Context context, String action, PendingIntent failIntent);

    boolean isViable();
}
