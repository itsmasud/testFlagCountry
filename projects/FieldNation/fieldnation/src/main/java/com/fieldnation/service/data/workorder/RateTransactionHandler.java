package com.fieldnation.service.data.workorder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Shoaib on 8/8/2015.
 */
public class RateTransactionHandler extends WebTransactionHandler {

    public static byte[] pRating( int satisfactionRating, int scopeRating,
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
            ex.printStackTrace();
        }
        return null;
    }

    /*-*****************************-*/
    /*-             Good            -*/
    /*-*****************************-*/
    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pRating":
                    handleRating(context, transaction, resultData, params);
                    break;
            }
            return super.handleResult(context, transaction, resultData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.ERROR;
    }

    private Result handleRating(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
        ToastClient.snackbar(context, "Success! Your rating has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }


    /*-****************************-*/
    /*-             Bad            -*/
    /*-****************************-*/
    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pRating":
                    handleRatingFail(context, transaction, resultData, params);
                    break;
            }
            return super.handleResult(context, transaction, resultData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.FINISH;
    }

    private Result handleRatingFail(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
        try {
            Intent intent = RateTransactionBuilder.actionPostRatingIntent(context,
                    params.getInt("satisfactionRating"), params.getInt("scopeRating"), params.getInt("respectRating"),
                    params.getInt("respectComment"), params.getBoolean("recommendBuyer"),params.getString("otherComments") ,params.getLong("workorderId"));

            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);


            ToastClient.snackbar(context, "Could not send your rating. Please check your connection.",
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        } catch (Exception ex) {
            ex.printStackTrace();
            ToastClient.snackbar(context, "Failed. Your rating could not be sent.", Toast.LENGTH_LONG);
        }
        return Result.FINISH;
    }

}
