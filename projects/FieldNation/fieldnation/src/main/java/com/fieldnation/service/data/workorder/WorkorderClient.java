package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.transaction.WebTransactionSystem;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class WorkorderClient extends TopicClient implements WorkorderConstants {
    private static final String STAG = "WorkorderClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public WorkorderClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subActions() {
        return subActions(0);
    }

    public boolean subActions(long workorderId) {
        String topicId = TOPIC_ID_ACTION_COMPLETE;

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId);
    }

    public static void sendRating(Context context, long workorderId, int satisfactionRating, int scopeRating,
                                  int respectRating, String otherComments) {
        WebTransactionSystem.queueTransaction(context,
                WorkorderTransactionBuilder.actionPostRatingIntent(workorderId, satisfactionRating, scopeRating,
                        respectRating, -1, false, otherComments));
    }

    // request
    public static void actionRequest(Context context, long workorderId, long expireInSeconds) {
        WorkorderTransactionBuilder.actionRequest(context, workorderId, expireInSeconds);
    }

    public static void actionDecline(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionDecline(context, workorderId);
    }

    public static void actionWithdrawRequest(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionWithdrawRequest(context, workorderId);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "topicId " + topicId);
            if (topicId.startsWith(TOPIC_ID_ACTION_COMPLETE)) {
                preAction((Bundle) payload);
            }
        }

        private void preAction(Bundle payload) {
            Log.v(STAG, "preAction "
                    + payload.getLong(PARAM_WORKORDER_ID) + " "
                    + payload.getString(PARAM_ACTION));

            onAction(payload.getLong(PARAM_WORKORDER_ID),
                    payload.getString(PARAM_ACTION),
                    payload.getBoolean(PARAM_ERROR));
        }

        public void onAction(long workorderId, String action, boolean failed) {
        }
    }
}
