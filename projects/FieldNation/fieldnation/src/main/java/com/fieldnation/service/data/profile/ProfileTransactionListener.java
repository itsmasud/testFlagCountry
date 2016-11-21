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
    public Result onSuccess(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        Log.v(TAG, "onSuccess");
        result = super.onSuccess(context, result, transaction, httpResult, throwable);
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");

            switch (action) {
                case "pGet":
                    return onSuccessGet(context, result, transaction, params, httpResult, throwable);
                case "pListNotifications":
                    return onSuccessListNotifications(context, result, transaction, params, httpResult, throwable);
                case "pListMessages":
                    return onSuccessListMessages(context, result, transaction, params, httpResult, throwable);
                case "pSwitchUser":
                    return onSuccessSwitchUser(context, result, transaction, params, httpResult, throwable);
                case "pAction":
                    return onSuccessAction(context, result, transaction, params, httpResult, throwable);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.RETRY;
        }
        return result;
    }

    private Result onSuccessGet(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessGet");
        // store object
        byte[] data = httpResult.getByteArray();
        long profileId = params.getLong("profileId");

        // todo parse json and put Profile/id ?
        ProfileDispatch.get(context, profileId, new JsonObject(data), false, transaction.isSync());

        StoredObject.put(context, (int) profileId, PSO_PROFILE, profileId, data);

        return Result.CONTINUE;
    }

    private Result onSuccessListNotifications(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessListNotifications");
        int page = params.getInt("page");
        // store object
        byte[] pagedata = httpResult.getByteArray();

        ProfileDispatch.listNotifications(context, new JsonArray(pagedata), page, false, transaction.isSync(), false);

        StoredObject.put(context, App.getProfileId(), PSO_NOTIFICATION_PAGE, page, pagedata);

        return Result.CONTINUE;
    }

    private Result onSuccessListMessages(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessListMessages");
        int page = params.getInt("page");
        // store object
        byte[] pagedata = httpResult.getByteArray();

        ProfileDispatch.listMessages(context, new JsonArray(pagedata), page, false, transaction.isSync(), false);

        StoredObject.put(context, App.getProfileId(), PSO_MESSAGE_PAGE, page, pagedata);

        return Result.CONTINUE;
    }

    private Result onSuccessAction(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessAction");

        long profileId = params.getLong("profileId");
        String action = params.getString("param");

        ProfileDispatch.action(context, profileId, action, false);

        return Result.CONTINUE;
    }

    private Result onSuccessSwitchUser(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessSwitchUser");

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
    }

    @Override
    public Result onFail(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        result = super.onFail(context, result, transaction, httpResult, throwable);
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");

            switch (action) {
                case "pGet":
                    ProfileDispatch.get(context, params.getLong("profileId"), null, true, transaction.isSync());
                    break;
                case "pListNotifications":
                    ProfileDispatch.listNotifications(context, null, params.getInt("page"), true, transaction.isSync(), true);
                    break;
                case "pListMessages":
                    ProfileDispatch.listMessages(context, null, params.getInt("page"), true, transaction.isSync(), true);
                    break;
                case "pAction":
                    ProfileDispatch.action(context, params.getLong("profileId"), params.getString("param"), true);
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.RETRY;
        }
        return result;
    }
}
