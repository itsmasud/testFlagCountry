package com.fieldnation.service.data.help;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by Shoaib on 7/4/2015.
 */
public class HelpTransactionBuilder {
    private static final String TAG = "HelpTransactionBuilder";

    public static Intent actionPostContactUsIntent(Context context, String message, String internalTeam, String uri,
                                                   String extraData, String extraType) {
        try {
            String body = "";

            // parameterized body
            body += "topic=android";
            body += "&message=" + message;
            body += "&internal_team=" + internalTeam;
            body += "&uri=" + uri;
            body += "&extra_data=" + extraData;
            body += "&extra_type=" + extraType;


            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/help/feedback");

            http.body(body);
            http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v1/help/feedback")
                    .priority(Priority.LOW)
                    .listener(HelpTransactionListener.class)
                    .listenerParams(HelpTransactionListener.pContactUs(message, internalTeam, uri, extraData, extraType))
                    .useAuth(true)
                    .request(http)
                    .build();

            return WebTransactionService.makeQueueTransactionIntent(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }


    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
}