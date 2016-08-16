package com.fieldnation.service.data.v2.workorder;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.Log;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.v2.ListEnvelope;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicClient;

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

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    /*-********************************-*/
    /*-             Search             -*/
    /*-********************************-*/
    public static void search(Context context, SearchParams searchParams, int page) {
        WorkOrderTransactionBuilder.search(context, searchParams, page);
    }

    public boolean subSearch(SearchParams searchParams) {
        return register(TOPIC_ID_SEARCH + "/" + searchParams.toKey(), TAG);
    }

    public boolean subSearch() {
        return register(TOPIC_ID_SEARCH, TAG);
    }

    public static void actionDecline(Context context, long workorderId, int declineReasonId, String explanation) {
        WorkOrderTransactionBuilder.actionDecline(context, workorderId, declineReasonId, explanation);
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
                SearchParams searchParams;
                ListEnvelope envelope;
                boolean failed;

                @Override
                protected List<WorkOrder> doInBackground(Bundle... params) {
                    try {
                        Bundle payload = params[0];

                        searchParams = payload.getParcelable(PARAM_SEARCH_PARAMS);
                        List<WorkOrder> list = new LinkedList<WorkOrder>();
                        Log.v(STAG, new String(payload.getByteArray(PARAM_LIST_ENVELOPE)));
                        failed = payload.getBoolean(PARAM_FAILED);
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

        public void onSearch(SearchParams searchParams, ListEnvelope envelope, List<WorkOrder> workOrders, boolean failed) {
        }
    }
}
