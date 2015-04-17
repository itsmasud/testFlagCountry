package com.fieldnation.service.data.profile;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileWebTransactionHandler extends WebTransactionHandler implements ProfileConstants {
    private static final String TAG = "ProfileWebTransactionHandler";

    public static byte[] generateGetProfileParams() {
        Log.v(TAG, "generateGetProfileParams");
        try {
            JsonObject obj = new JsonObject();
            obj.put("action", PARAM_ACTION_GET_MY_PROFILE);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] generateGetAllNotificationsParams(int page) {
        Log.v(TAG, "generateGetAllNotificationsParams");
        try {
            JsonObject obj = new JsonObject();
            obj.put("action", PARAM_ACTION_GET_ALL_NOTIFICATIONS);
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] generateGetAllMessagesParams(int page) {
        Log.v(TAG, "generateGetAllMessagesParams");
        try {
            JsonObject obj = new JsonObject();
            obj.put("action", PARAM_ACTION_GET_ALL_MESSAGES);
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        Log.v(TAG, "handleResult");
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");

            if (action.equals(PARAM_ACTION_GET_MY_PROFILE)) {
                Log.v(TAG, "PARAM_ACTION_GET_MY_PROFILE");
                // store object
                byte[] profiledata = resultData.getResultsAsByteArray();

                StoredObject.put(context, PSO_PROFILE, PSO_MY_PROFILE_KEY, profiledata);

                // todo parse json and put Profile/id ?

                Bundle bundle = new Bundle();
                bundle.putParcelable(PARAM_DATA_PARCELABLE, new JsonObject(resultData.getResultsAsByteArray()));
                TopicService.dispatchEvent(context, TOPIC_ID_HAVE_PROFILE, bundle, false);
                return;
            } else if (action.equals(PARAM_ACTION_GET_ALL_NOTIFICATIONS)) {
                Log.v(TAG, "PARAM_ACTION_GET_ALL_NOTIFICATIONS");
                int page = params.getInt("page");
                // store object
                byte[] pagedata = resultData.getResultsAsByteArray();

                StoredObject.put(context, PSO_NOTIFICATION_PAGE, page + "", pagedata);

                Bundle bundle = new Bundle();
                bundle.putParcelable(PARAM_DATA_PARCELABLE, new JsonArray(pagedata));
                bundle.putInt(PARAM_PAGE, page);
                bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_ALL_NOTIFICATIONS);
                TopicService.dispatchEvent(context, TOPIC_ID_ALL_NOTIFICATION_LIST, bundle, false);
                return;
            } else if (action.equals(PARAM_ACTION_GET_ALL_MESSAGES)) {
                Log.v(TAG, "PARAM_ACTION_GET_ALL_MESSAGES");
                int page = params.getInt("page");
                // store object
                byte[] pagedata = resultData.getResultsAsByteArray();

                StoredObject.put(context, PSO_MESSAGE_PAGE, page + "", pagedata);

                Bundle bundle = new Bundle();
                bundle.putParcelable(PARAM_DATA_PARCELABLE, new JsonArray(pagedata));
                bundle.putInt(PARAM_PAGE, page);
                bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_ALL_MESSAGES);
                TopicService.dispatchEvent(context, TOPIC_ID_ALL_MESSAGES_LIST, bundle, false);
                return;
            }
            listener.onComplete(transaction);
        } catch (Exception ex) {
            ex.printStackTrace();
            listener.requeue(transaction);
        }

    }
}
