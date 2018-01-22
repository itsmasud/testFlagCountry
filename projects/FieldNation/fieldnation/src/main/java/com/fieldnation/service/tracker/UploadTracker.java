package com.fieldnation.service.tracker;

import android.content.Context;

import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by mc on 2/20/17.
 */

public interface UploadTracker {
    void update(Context context, String action, WebTransaction webTransaction);

    boolean isViable();
}
