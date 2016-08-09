package com.fieldnation.service.data.v2.workorder;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrderTransactionBuilder implements WorkOrderConstants {
    private static final String TAG = "WorkOrderTransactionBuilder";

    public static void search(Context context, SearchParams searchParams, int page) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkOrderTransactionHandler.class)
                    .handlerParams(WorkOrderTransactionHandler.pSearch(searchParams))
                    .key(searchParams.toKey())
                    .useAuth(true)
                    .isSyncCall(false)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .timingKey("GET/v2/workorders")
                            .urlParams(searchParams.toUrlParams() + "&page=" + page)
                            .path("/v2/workorders"))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}
