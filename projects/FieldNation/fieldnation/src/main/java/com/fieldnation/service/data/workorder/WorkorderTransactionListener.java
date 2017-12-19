package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;
import com.fieldnation.service.transaction.WebTransactionSystem;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderTransactionListener extends WebTransactionListener implements WorkorderConstants {
    private static final String TAG = "WorkorderTransactionListener";

    public static byte[] pAction(long workorderId, String action) {
        try {
            JsonObject obj = new JsonObject("action", "pAction");
            obj.put("workorderId", workorderId);
            obj.put("param", action);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pActionRequest(long workorderId, long expireInSeconds, String startTime, String endTime, String note) {
        try {
            JsonObject obj = new JsonObject("action", "pActionRequest");
            obj.put("workorderId", workorderId);
            obj.put("expireInSeconds", expireInSeconds);
            obj.put("startTime", startTime);
            obj.put("endTime", endTime);
            obj.put("note", note);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pAccept(long workorderId, String startTimeIso8601, String endTimeIso8601, String note, boolean isEditEta) {
        try {
            JsonObject obj = new JsonObject("action", "pAccept");
            obj.put("workorderId", workorderId);
            obj.put("startTimeIso8601", startTimeIso8601);
            obj.put("endTimeIso8601", endTimeIso8601);
            obj.put("note", note);
            obj.put("isEditEta", isEditEta);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pRating(int satisfactionRating, int scopeRating,
                                 int respectRating, int respectComment, boolean recommendBuyer, String otherComments, long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pRating");
            obj.put("satisfactionRating", satisfactionRating);
            obj.put("scopeRating", scopeRating);
            obj.put("respectRating", respectRating);
            obj.put("respectComment", respectComment);
            obj.put("recommendBuyer", recommendBuyer);
            obj.put("otherComments", otherComments);
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    /*-***********************************-*/
    /*-             onStart               -*/
    /*-***********************************-*/
    @Override
    public void onStart(Context context, WebTransaction transaction) {
    }

    /*-**************************************-*/
    /*-             onProgress               -*/
    /*-**************************************-*/
    @Override
    public void onProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
    }

    /*-**************************************-*/
    /*-             onComplete               -*/
    /*-**************************************-*/
    @Override
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pAction":
                    return onAction(context, result, transaction, params, httpResult, throwable);
                case "pActionRequest":
                    return onActionRequest(context, result, transaction, params, httpResult, throwable);
                case "pAccept":
                    return onAccept(context, result, transaction, params, httpResult, throwable);
                case "pRating":
                    return onRating(context, result, transaction, params, httpResult, throwable);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return result;
    }

    private Result onAction(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, action, false);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            WorkorderDispatch.action(context, workorderId, params.getString("param"), true);

            if (haveErrorMessage(httpResult)) {
                ToastClient.toast(context, httpResult.getString(), Toast.LENGTH_LONG);
            } else {
                ToastClient.toast(context, "Could not update work order", Toast.LENGTH_LONG);
            }
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onActionRequest(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onActionRequest");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "request", false);
            ToastClient.toast(context, "Success! You have requested the bundle.", Toast.LENGTH_LONG);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            long expireInSeconds = params.getLong("expireInSeconds");
            String startTime = params.getString("startTime");
            String endTime = params.getString("endTime");
            String note = params.getString("note");

            WorkorderDispatch.action(context, workorderId, "request", true);

            final WebTransaction webTransaction = WorkorderTransactionBuilder.actionRequestIntent(workorderId, expireInSeconds, startTime, endTime, note);
            ToastClient.snackbar(context, pickErrorMessage(httpResult, "Unable to request bundle"),
                    "TRY AGAIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WebTransactionSystem.queueTransaction(App.get(), webTransaction);
                        }
                    }, Snackbar.LENGTH_LONG);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onAccept(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onAccept");
        long workorderId = params.getLong("workorderId");
        boolean isEditEta = params.getBoolean("isEditEta");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "assignment", false);

            if (!isEditEta)
                ToastClient.snackbar(context, "Success! You have accepted this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);
            else
                ToastClient.snackbar(context, "Success! Edited ETA is saved.", "DISMISS", null, Snackbar.LENGTH_LONG);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            String startTimeIso8601 = params.getString("startTimeIso8601");
            String endTimeIso8601 = params.getString("endTimeIso8601");
            String note = params.getString("note");

            WorkorderDispatch.action(context, workorderId, "assignment", true);

            final WebTransaction webTransaction = WorkorderTransactionBuilder.actionAcceptIntent(workorderId, startTimeIso8601, endTimeIso8601, note, isEditEta);
            ToastClient.snackbar(context, pickErrorMessage(httpResult, "Unable to accept work order"), "TRY AGAIN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WebTransactionSystem.queueTransaction(App.get(), webTransaction);
                }
            }, Snackbar.LENGTH_LONG);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onRating(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) {
        if (result == Result.CONTINUE) {
            ToastClient.snackbar(context, "Success! Your rating has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            try {
                final WebTransaction webTransaction = WorkorderTransactionBuilder.actionPostRatingIntent(
                        params.getLong("workorderId"),
                        params.getInt("satisfactionRating"),
                        params.getInt("scopeRating"),
                        params.getInt("respectRating"),
                        params.getInt("respectComment"),
                        params.getBoolean("recommendBuyer"),
                        params.getString("otherComments"));

                ToastClient.snackbar(context, pickErrorMessage(httpResult, "Could not send your rating"), "TRY AGAIN", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WebTransactionSystem.queueTransaction(App.get(), webTransaction);
                    }
                }, Snackbar.LENGTH_LONG);

            } catch (Exception ex) {
                Log.v(TAG, ex);
                ToastClient.snackbar(context, pickErrorMessage(httpResult, "Could not send your rating"), Toast.LENGTH_LONG);
            }
            return Result.DELETE;
        } else {
            return Result.RETRY;
        }
    }
}
