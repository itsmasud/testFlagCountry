package com.fieldnation.service.data.help;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.fieldnation.R;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

/**
 * Created by Michael Carver on 7/20/2015.
 */
public class HelpTransactionListener extends WebTransactionListener {
    private static final String TAG = "HelpTransactionListener";

    public static byte[] pContactUs(String message, String internalTeam, String uri, String extraData, String extraType) {
        try {
            JsonObject obj = new JsonObject("action", "pContactUs");
            obj.put("message", message);
            obj.put("internalTeam", internalTeam);
            obj.put("uri", uri);
            obj.put("extraData", extraData);
            obj.put("extraType", extraType);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    /*-*****************************-*/
    /*-             Good            -*/
    /*-*****************************-*/
    @Override
    public Result onComplete(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pContactUs":
                    handleContactUs(context, transaction, resultData, params);
                    break;
            }
            return super.onComplete(context, transaction, resultData);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.DELETE;
    }

    private Result handleContactUs(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {

        ToastClient.snackbar(context, context.getString(R.string.snackbar_feedback_success_message), "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.CONTINUE;
    }


    /*-****************************-*/
    /*-             Bad            -*/
    /*-****************************-*/
    @Override
    public Result onFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pContactUs":
                    handleContactUsFail(context, transaction, resultData, params);
                    break;
            }
            return super.onComplete(context, transaction, resultData);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result handleContactUsFail(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
        try {
            Intent intent = HelpTransactionBuilder.actionPostContactUsIntent(context,
                    params.getString("message"), params.getString("internalTeam"), params.getString("uri"), params.getString("extraData"),
                    params.getString("extraType"));

            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);


            ToastClient.snackbar(context, context.getString(R.string.snackbar_feedback_connection_failed),
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        } catch (Exception ex) {
            Log.v(TAG, ex);
            ToastClient.snackbar(context, context.getString(R.string.snackbar_feedback_sent_failed), Toast.LENGTH_LONG);
        }
        return Result.CONTINUE;
    }

}
