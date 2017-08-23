package com.fieldnation.service.data.help;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;
import com.fieldnation.service.transaction.WebTransactionSystem;

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
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pContactUs":
                    return onContactUs(context, result, transaction, params, httpResult, throwable);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return result;
    }

    private Result onContactUs(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) {

        if (result == Result.CONTINUE) {
            ToastClient.snackbar(context, context.getString(R.string.snackbar_feedback_success_message), "DISMISS", null, Snackbar.LENGTH_LONG);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            try {
                final WebTransaction webTransaction = HelpTransactionBuilder.actionPostContactUsIntent(
                        params.getString("message"),
                        params.getString("internalTeam"),
                        params.getString("uri"),
                        params.getString("extraData"),
                        params.getString("extraType"));

                ToastClient.snackbar(context, context.getString(R.string.snackbar_feedback_connection_failed),
                        "TRY AGAIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                WebTransactionSystem.queueTransaction(App.get(), webTransaction);
                            }
                        }, Snackbar.LENGTH_LONG);

            } catch (Exception ex) {
                Log.v(TAG, ex);
                ToastClient.snackbar(context, context.getString(R.string.snackbar_feedback_sent_failed), Toast.LENGTH_LONG);
            }
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }
}
