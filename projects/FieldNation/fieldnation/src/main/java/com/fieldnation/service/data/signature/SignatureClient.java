package com.fieldnation.service.data.signature;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.data.workorder.WorkorderTransactionBuilder;
import com.fieldnation.service.topics.TopicClient;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class SignatureClient extends TopicClient implements SignatureConstants {
    private static final String STAG = "SignatureClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public SignatureClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void get(Context context, long workorderId, long signatureId) {
        get(context, workorderId, signatureId, false);
    }

    public static void get(Context context, long workorderId, long signatureId, boolean isSync) {
        Intent intent = new Intent(context, SignatureService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_SIGNATURE_ID, signatureId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subGet(boolean isSync) {
        return subGet(0, 0, isSync);
    }

    public boolean subGet(long workorderId, long signatureId, boolean isSync) {
        String topicId = "";

        if (isSync) {
            topicId += isSync;
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;

            if (signatureId > 0) {
                topicId += "/" + signatureId;
            }
        }


        return register(topicId, TAG);
    }

    // add signature json
    public static void requestAddSignatureJson(Context context, long workorderId, String name, String json) {
        WorkorderTransactionBuilder.postSignatureJson(context, workorderId, name, json);
    }

    // complete signature
    public static void requestCompleteSignatureTaskJson(Context context, long workorderId, long taskId, String name, String json) {
        WorkorderTransactionBuilder.postSignatureJsonTask(context, workorderId, taskId, name, json);
    }


    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {

        }
    }
}
