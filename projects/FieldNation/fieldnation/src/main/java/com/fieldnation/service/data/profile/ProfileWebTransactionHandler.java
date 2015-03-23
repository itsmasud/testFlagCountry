package com.fieldnation.service.data.profile;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.Log;
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

                StoredObject.put(context, "Profile", "Me", profiledata);

                // todo parse json and put Profile/id ?

                Bundle bundle = new Bundle();
                bundle.putByteArray(PARAM_DATA, resultData.getResultsAsByteArray());
                TopicService.dispatchEvent(context, TOPIC_ID_HAVE_PROFILE, bundle, false);
                listener.onComplete(transaction);
                return;
            } else if (action.equals(PARAM_ACTION_GET_ALL_NOTIFICATIONS)) {
                Log.v(TAG, "PARAM_ACTION_GET_ALL_NOTIFICATIONS");
                int page = params.getInt("page");
                // store object
                byte[] pagedata = resultData.getResultsAsByteArray();

                StoredObject.put(context, "NotificationPage", page + "", pagedata);

                Bundle bundle = new Bundle();
                bundle.putByteArray(PARAM_DATA, pagedata);
                bundle.putInt(PARAM_PAGE, page);
                TopicService.dispatchEvent(context, PARAM_ACTION_GET_ALL_NOTIFICATIONS, bundle, false);
                listener.onComplete(transaction);
                return;
            }
            listener.onError(transaction);
        } catch (Exception ex) {
            ex.printStackTrace();
            listener.requeue(transaction);
        }

    }
}
