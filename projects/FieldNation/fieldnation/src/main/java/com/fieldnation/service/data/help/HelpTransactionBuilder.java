package com.fieldnation.service.data.help;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Shoaib on 7/4/2015.
 */
public class HelpTransactionBuilder {
    private static final String TAG = "HelpTransactionBuilder";

    public static Intent actionPostFeedbackIntent(Context context, String message, String uri,
                                                  String extraData, String extraType) {
        try {
            String body = "";

            // parameterized body
            body += "topic=android";
            body += "&message=" + message;
            body += "&uri=" + uri;
            body += "&extra_data=" + extraData;
            body += "&extra_type=" + extraType;


            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .timingKey("POST/api/rest/v1/help/feedback")
                    .path("/api/rest/v1/help/feedback");

            if (body != null) {
                http.body(body);

                http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED);
            }

            return WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(HelpTransactionHandler.class)
                    .handlerParams(HelpTransactionHandler.pFeedback(message, uri, extraData, extraType))
                    .useAuth(true)
                    .request(http)
                    .makeIntent();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }


    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
}