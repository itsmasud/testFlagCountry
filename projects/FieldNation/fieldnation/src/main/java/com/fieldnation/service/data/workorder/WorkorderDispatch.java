package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicService;

/**
 * Created by Michael Carver on 4/20/2015.
 */
public class WorkorderDispatch implements WorkorderConstants {
    private static final String TAG = "WorkorderDispatch";

    public static void action(Context context, long workorderId, String action, boolean failed) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_COMPLETE);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);
        bundle.putBoolean(PARAM_ERROR, failed);

        String topicId = TOPIC_ID_ACTION_COMPLETE;
        topicId += "/" + workorderId;

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }
}
