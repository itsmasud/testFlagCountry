package com.fieldnation.service.data.help;

import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by Shoaib on 7/4/2015.
 */
public class HelpTransactionBuilder {
    private static final String TAG = "HelpTransactionBuilder";

    public static WebTransaction actionPostContactUsIntent(String message, String internalTeam, String uri,
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

            return transaction;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }


    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
}