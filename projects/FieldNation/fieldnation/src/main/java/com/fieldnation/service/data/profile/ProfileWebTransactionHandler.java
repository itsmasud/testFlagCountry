package com.fieldnation.service.data.profile;

import android.content.Context;

import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.text.ParseException;

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
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        Log.v(TAG, "handleResult");
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");

            if (action.equals(PARAM_ACTION_GET_MY_PROFILE)) {
                return handleGetProfile(context, transaction, resultData);
            } else if (action.equals(PARAM_ACTION_GET_ALL_NOTIFICATIONS)) {
                return handleGetAllNotifications(context, transaction, resultData, params);
            } else if (action.equals(PARAM_ACTION_GET_ALL_MESSAGES)) {
                return handleGetAllMessages(context, transaction, resultData, params);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.REQUEUE;
        }
        return Result.FINISH;
    }

    private Result handleGetProfile(Context context, WebTransaction transaction, HttpResult resultData) throws ParseException {
        Log.v(TAG, "PARAM_ACTION_GET_MY_PROFILE");
        // store object
        byte[] profiledata = resultData.getByteArray();

        StoredObject.put(context, PSO_PROFILE, PSO_MY_PROFILE_KEY, profiledata);

        // todo parse json and put Profile/id ?
        ProfileDispatch.myUserInformation(context, new JsonObject(profiledata), transaction.isSync());
        return Result.FINISH;
    }

    private Result handleGetAllNotifications(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "PARAM_ACTION_GET_ALL_NOTIFICATIONS");
        int page = params.getInt("page");
        // store object
        byte[] pagedata = resultData.getByteArray();

        StoredObject.put(context, PSO_NOTIFICATION_PAGE, page, pagedata);

        ProfileDispatch.allNotifications(context, new JsonArray(pagedata), page, transaction.isSync());
        return Result.FINISH;
    }

    private Result handleGetAllMessages(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "PARAM_ACTION_GET_ALL_MESSAGES");
        int page = params.getInt("page");
        // store object
        byte[] pagedata = resultData.getByteArray();

        StoredObject.put(context, PSO_MESSAGE_PAGE, page, pagedata);

        ProfileDispatch.allMessages(context, new JsonArray(pagedata), page, transaction.isSync());
        return Result.FINISH;
    }
}
