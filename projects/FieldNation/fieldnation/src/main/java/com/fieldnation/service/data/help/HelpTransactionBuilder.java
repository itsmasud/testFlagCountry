package com.fieldnation.service.data.help;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

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
                    .timingKey("POST/api/rest/v1/help/feedback")
                    .path("/api/rest/v1/help/feedback");

            http.body(body);
            http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED);

            return WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(HelpTransactionHandler.class)
                    .handlerParams(HelpTransactionHandler.pContactUs(message, internalTeam, uri, extraData, extraType))
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