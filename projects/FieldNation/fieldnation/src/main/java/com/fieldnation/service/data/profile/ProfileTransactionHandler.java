package com.fieldnation.service.data.profile;

import android.content.Context;

import com.fieldnation.App;
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

    public static byte[] pGet(long profileId) {
        try {
            JsonObject obj = new JsonObject("action", "pGet");
            obj.put("profileId", profileId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pListNotifications(int page) {
        try {
            JsonObject obj = new JsonObject("action", "pListNotifications");
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pListMessages(int page) {
        try {
            JsonObject obj = new JsonObject("action", "pListMessages");
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pSwitchUser(long userId) {
        try {
            JsonObject obj = new JsonObject("action", "pSwitchUser");
            obj.put("userId", userId);
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
                case "pGet":
                    return handleGet(context, transaction, resultData, params);
                case "pListNotifications":
                    return handleListNotifications(context, transaction, resultData, params);
                case "pListMessages":
                    return handleListMessages(context, transaction, resultData, params);
                case "pSwitchUser":
                    return handleSwitchUser(context, transaction, resultData, params);
                case "pAction":
                    return handleAction(context, transaction, resultData, params);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.REQUEUE;
        }
        return Result.FINISH;
    }

    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");

            switch (action) {
                case "pGet":
                    ProfileDispatch.get(context, params.getLong("profileId"), null, true, transaction.isSync());
                    break;
                case "pListNotifications":
                    ProfileDispatch.listNotifications(context, null, params.getInt("page"), true, transaction.isSync());
                    break;
                case "pListMessages":
                    ProfileDispatch.listMessages(context, null, params.getInt("page"), true, transaction.isSync());
                    break;
                case "pAction":
                    ProfileDispatch.action(context, params.getLong("profileId"), params.getString("param"), true);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.FINISH;
        }
        return Result.FINISH;
    }

    private Result handleGet(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "handleGet");
        // store object
        byte[] data = resultData.getByteArray();
        long profileId = params.getLong("profileId");

        // todo parse json and put Profile/id ?
        ProfileDispatch.get(context, profileId, new JsonObject(data), false, transaction.isSync());

        StoredObject.put((int) profileId, PSO_PROFILE, profileId, data);

        return Result.FINISH;
    }

    private Result handleListNotifications(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "handleListNotifications");
        int page = params.getInt("page");
        // store object
        byte[] pagedata = resultData.getByteArray();

        ProfileDispatch.listNotifications(context, new JsonArray(pagedata), page, false, transaction.isSync());

        StoredObject.put(App.getProfileId(), PSO_NOTIFICATION_PAGE, page, pagedata);

        return Result.FINISH;
    }

    private Result handleListMessages(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "handleListMessages");
        int page = params.getInt("page");
        // store object
        byte[] pagedata = resultData.getByteArray();

        ProfileDispatch.listMessages(context, new JsonArray(pagedata), page, false, transaction.isSync());

        StoredObject.put(App.getProfileId(), PSO_MESSAGE_PAGE, page, pagedata);

        return Result.FINISH;
    }

    private Result handleAction(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "handleAction");

        long profileId = params.getLong("profileId");
        String action = params.getString("param");

        ProfileDispatch.action(context, profileId, action, false);

        return Result.FINISH;
    }

    private Result handleSwitchUser(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        Log.v(TAG, "handleSwitchUser");

        long userId = params.getLong("userId");

        ProfileDispatch.switchUser(context, userId, false);

        return Result.FINISH;
    }
}
