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
public class ProfileTransactionHandler extends WebTransactionHandler implements ProfileConstants {
    private static final String TAG = "ProfileWebTransactionHandler";

    public static byte[] pGetProfile() {
        try {
            JsonObject obj = new JsonObject("action", "pGetProfile");
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pGetAllNotifications(int page) {
        try {
            JsonObject obj = new JsonObject("action", "pGetAllNotifications");
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pGetAllMessages(int page) {
        try {
            JsonObject obj = new JsonObject("action", "pGetAllMessages");
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pAction(long profileId, String action) {
        try {
            JsonObject obj = new JsonObject("action", "pAction");
            obj.put("profileId", profileId);
            obj.put("param", action);
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

            switch (action) {
                case "pGetProfile":
                    return handleGetProfile(context, transaction, resultData);
                case "pGetAllNotifications":
                    return handleGetAllNotifications(context, transaction, resultData, params);
                case "pGetAllMessages":
                    return handleGetAllMessages(context, transaction, resultData, params);
                case "pAction":
                    return handleAction(context, transaction, resultData, params);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.REQUEUE;
        }
        return Result.FINISH;
    }

    private Result handleAction(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "handleAction");

        long profileId = params.getLong("profileId");
        String action = params.getString("param");

        ProfileDispatch.action(context, profileId, action);

        return Result.FINISH;
    }

    private Result handleGetProfile(Context context, WebTransaction transaction, HttpResult resultData) throws ParseException {
        Log.v(TAG, "PARAM_ACTION_GET_MY_PROFILE");
        // store object
        byte[] profiledata = resultData.getByteArray();

        // todo parse json and put Profile/id ?
        ProfileDispatch.myUserInformation(context, new JsonObject(profiledata), transaction.isSync());

        StoredObject.put(context, PSO_PROFILE, PSO_MY_PROFILE_KEY, profiledata);

        return Result.FINISH;
    }

    private Result handleGetAllNotifications(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "PARAM_ACTION_GET_ALL_NOTIFICATIONS");
        int page = params.getInt("page");
        // store object
        byte[] pagedata = resultData.getByteArray();

        ProfileDispatch.allNotifications(context, new JsonArray(pagedata), page, transaction.isSync());

        StoredObject.put(context, PSO_NOTIFICATION_PAGE, page, pagedata);

        return Result.FINISH;
    }

    private Result handleGetAllMessages(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "PARAM_ACTION_GET_ALL_MESSAGES");
        int page = params.getInt("page");
        // store object
        byte[] pagedata = resultData.getByteArray();

        ProfileDispatch.allMessages(context, new JsonArray(pagedata), page, transaction.isSync());

        StoredObject.put(context, PSO_MESSAGE_PAGE, page, pagedata);

        return Result.FINISH;
    }
}
