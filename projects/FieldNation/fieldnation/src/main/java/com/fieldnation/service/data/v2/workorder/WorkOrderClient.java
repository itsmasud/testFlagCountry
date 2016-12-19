package com.fieldnation.service.data.v2.workorder;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.v2.ListEnvelope;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrderClient extends TopicClient implements WorkOrderConstants {
    private static final String STAG = "WorkOrderClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    /*-************************************-*/
    /*-             Life Cycle             -*/
    /*-************************************-*/
    public WorkOrderClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    /*-********************************-*/
    /*-             Search             -*/
    /*-********************************-*/
    public static void search(Context context, SavedSearchParams searchParams, int page) {
        WorkOrderTransactionBuilder.search(context, searchParams, page);
    }

    public boolean subSearch(SavedSearchParams searchParams) {
        return register(TOPIC_ID_SEARCH + "/" + searchParams.toKey());
    }

    public boolean subSearch() {
        return register(TOPIC_ID_SEARCH);
    }

    public static void actionDecline(Context context, long workOrderId, int declineReasonId, String declineExplanation) {
        WorkOrderTransactionBuilder.actionDecline(context, workOrderId, declineReasonId, declineExplanation);
    }

    public static void actionEta(Context context, long workOrderId, String startTime, String endTime, String note) {
        WorkOrderTransactionBuilder.actionEta(context, workOrderId, startTime, endTime, note);
    }

    public static void actionOnMyWay(Context context, long workOrderId, Double lat, Double lon) {
        WorkOrderTransactionBuilder.actionOnMyWay(context, workOrderId, lat, lon);
    }

    public boolean subActions() {
        return register(TOPIC_ID_ACTION_COMPLETE);
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "topicId " + topicId);
            if (topicId.startsWith(TOPIC_ID_ACTION_COMPLETE)) {
                preOnAction((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_SEARCH)) {
                preOnSearch((Bundle) payload);
            }
        }

        private void preOnAction(Bundle payload) {
            onAction(payload.getLong(PARAM_WORKORDER_ID),
                    payload.getString(PARAM_WO_ACTION),
                    payload.getBoolean(PARAM_FAILED));
        }

        public void onAction(long workOrderId, String action, boolean failed) {
        }

        private void preOnSearch(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, List<WorkOrder>>() {
                SavedSearchParams searchParams;
                ListEnvelope envelope;
                boolean failed;

                @Override
                protected List<WorkOrder> doInBackground(Bundle... params) {
                    try {
                        Bundle payload = params[0];

                        searchParams = payload.getParcelable(PARAM_SEARCH_PARAMS);
                        List<WorkOrder> list = new LinkedList<WorkOrder>();
                        failed = payload.getBoolean(PARAM_FAILED);

                        if (failed)
                            return null;

                        Log.v(STAG, new String(payload.getByteArray(PARAM_LIST_ENVELOPE)));
                        envelope = ListEnvelope.fromJson(new JsonObject(payload.getByteArray(PARAM_LIST_ENVELOPE)));
                        JsonArray array = envelope.getResults();

                        for (int i = 0; i < array.size(); i++) {
                            list.add(WorkOrder.fromJson(array.getJsonObject(i)));
                        }

                        return list;
                    } catch (Exception ex) {
                        Log.v(STAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<WorkOrder> o) {
                    onSearch(searchParams, envelope, o, failed);
                }
            }.executeEx(payload);
        }

        public void onSearch(SavedSearchParams searchParams, ListEnvelope envelope, List<WorkOrder> workOrders, boolean failed) {
        }
    }
}
