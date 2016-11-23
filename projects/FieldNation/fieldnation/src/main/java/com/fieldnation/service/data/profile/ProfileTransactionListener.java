package com.fieldnation.service.data.profile;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileTransactionListener extends WebTransactionListener implements ProfileConstants {
    private static final String TAG = "ProfileTransactionListener";

    public static byte[] pGet(long profileId) {
        try {
            JsonObject obj = new JsonObject("action", "pGet");
            obj.put("profileId", profileId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pListNotifications(int page) {
        try {
            JsonObject obj = new JsonObject("action", "pListNotifications");
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pListMessages(int page) {
        try {
            JsonObject obj = new JsonObject("action", "pListMessages");
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pSwitchUser(long userId) {
        try {
            JsonObject obj = new JsonObject("action", "pSwitchUser");
            obj.put("userId", userId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }

    @Override
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        Log.v(TAG, "onComplete");
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pGet":
                    return onGet(context, result, transaction, params, httpResult, throwable);
                case "pListNotifications":
                    return onListNotifications(context, result, transaction, params, httpResult, throwable);
                case "pListMessages":
                    return onListMessages(context, result, transaction, params, httpResult, throwable);
                case "pSwitchUser":
                    return onSwitchUser(context, result, transaction, params, httpResult, throwable);
                case "pAction":
                    return onAction(context, result, transaction, params, httpResult, throwable);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.RETRY;
        }
        return result;
    }

    private Result onGet(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onGet");
        // store object
        long profileId = params.getLong("profileId");

        if (result == Result.CONTINUE) {
            byte[] data = httpResult.getByteArray();

            // todo parse json and put Profile/id ?
            ProfileDispatch.get(context, profileId, new JsonObject(data), false, transaction.isSync());
            StoredObject.put(context, (int) profileId, PSO_PROFILE, profileId, data);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ProfileDispatch.get(context, profileId, null, true, transaction.isSync());
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onListNotifications(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onListNotifications");
        int page = params.getInt("page");

        if (result == Result.CONTINUE) {
            // store object
            byte[] pagedata = httpResult.getByteArray();

            ProfileDispatch.listNotifications(context, new JsonArray(pagedata), page, false, transaction.isSync(), false);
            StoredObject.put(context, App.getProfileId(), PSO_NOTIFICATION_PAGE, page, pagedata);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ProfileDispatch.listNotifications(context, null, page, true, transaction.isSync(), true);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onListMessages(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onListMessages");
        int page = params.getInt("page");

        if (result == Result.CONTINUE) {
            // store object
            byte[] pagedata = httpResult.getByteArray();

            ProfileDispatch.listMessages(context, new JsonArray(pagedata), page, false, transaction.isSync(), false);
            StoredObject.put(context, App.getProfileId(), PSO_MESSAGE_PAGE, page, pagedata);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ProfileDispatch.listMessages(context, null, page, true, transaction.isSync(), true);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onSwitchUser(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSwitchUser");

        if (result == Result.CONTINUE) {
            long userId = params.getLong("userId");
            ProfileClient.get(context, false);
            ProfileClient.listMessages(context, 0, false, false);
            ProfileClient.listNotifications(context, 0, false, false);
            WorkorderClient.list(context, WorkorderDataSelector.AVAILABLE, 0, false, false);
            WorkorderClient.list(context, WorkorderDataSelector.REQUESTED, 0, false, false);
            WorkorderClient.list(context, WorkorderDataSelector.ASSIGNED, 0, false, false);
            WorkorderClient.list(context, WorkorderDataSelector.COMPLETED, 0, false, false);
            WorkorderClient.list(context, WorkorderDataSelector.CANCELED, 0, false, false);

            ProfileDispatch.switchUser(context, userId, false);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            return Result.DELETE;

        } else {

            return Result.RETRY;
        }
    }

    private Result onAction(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onAction");
        long profileId = params.getLong("profileId");
        String action = params.getString("param");

        if (result == Result.CONTINUE) {
            ProfileDispatch.action(context, profileId, action, false);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ProfileDispatch.action(context, profileId, action, true);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }
}
