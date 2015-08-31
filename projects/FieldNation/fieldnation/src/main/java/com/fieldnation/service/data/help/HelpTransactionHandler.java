package com.fieldnation.service.data.help;

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
 * Created by Michael Carver on 7/20/2015.
 */
public class HelpTransactionHandler extends WebTransactionHandler {

    public static byte[] pFeedback(String message, String uri, String extraData, String extraType) {
        try {
            JsonObject obj = new JsonObject("action", "pFeedback");
            obj.put("message", message);
            obj.put("uri", uri);
            obj.put("extraData", extraData);
            obj.put("extraType", extraType);
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
                case "pFeedback":
                    handleFeedback(context, transaction, resultData, params);
                    break;
            }
            return super.handleResult(context, transaction, resultData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.ERROR;
    }

    private Result handleFeedback(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
        ToastClient.snackbar(context, "Success! Your feedback has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);

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
                case "pFeedback":
                    handleFeedbackFail(context, transaction, resultData, params);
                    break;
            }
            return super.handleResult(context, transaction, resultData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.FINISH;
    }

    private Result handleFeedbackFail(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
        try {
            Intent intent = HelpTransactionBuilder.actionPostFeedbackIntent(context,
                    params.getString("message"), params.getString("uri"), params.getString("extraData"),
                    params.getString("extraType"));

            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);


            ToastClient.snackbar(context, "Could not send your feedback. Please check your connection.",
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        } catch (Exception ex) {
            ex.printStackTrace();
            ToastClient.snackbar(context, "Failed. Your feedback could not be sent.", Toast.LENGTH_LONG);
        }
        return Result.FINISH;
    }

}
