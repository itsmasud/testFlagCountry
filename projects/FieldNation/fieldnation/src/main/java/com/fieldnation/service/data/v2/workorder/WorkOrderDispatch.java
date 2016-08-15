package com.fieldnation.service.data.v2.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicService;

/**
 * Created by Michael on 7/22/2016.
 */
public class WorkOrderDispatch implements WorkOrderConstants {
    private static final String TAG = "WorkOrderDispatch";

    public static void search(Context context, SearchParams searchParams, byte[] listEnvelope, boolean failed) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, ACTION_SEARCH);
        bundle.putParcelable(PARAM_SEARCH_PARAMS, searchParams);
        bundle.putByteArray(PARAM_LIST_ENVELOPE, listEnvelope);
        bundle.putBoolean(PARAM_FAILED, failed);

        TopicService.dispatchEvent(context, TOPIC_ID_SEARCH + "/" + searchParams.toKey(), bundle, Sticky.TEMP);
    }

    public static void action(Context context, long workOrderId, String action, boolean failed) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, ACTION_ACTION_COMPLETE);
        bundle.putLong(PARAM_WORKORDER_ID, workOrderId);
        bundle.putBoolean(PARAM_FAILED, failed);
        bundle.putString(PARAM_WO_ACTION, action);

        String topicId = TOPIC_ID_ACTION_COMPLETE;
        topicId += "/" + workOrderId;

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

}
