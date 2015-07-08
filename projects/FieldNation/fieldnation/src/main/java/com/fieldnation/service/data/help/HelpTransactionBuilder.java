package com.fieldnation.service.data.help;

import android.content.Context;

import com.fieldnation.GlobalState;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.data.workorder.WorkorderTransactionHandler;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

/**
 * Created by Shoaib on 7/4/2015.
 */
public class HelpTransactionBuilder {


    public static void actionPostFeedback(Context context, String message, String uri,
                                      String extra_data, String extra_type) {
        String body = "";


        // parameterized body
                body += "topic=android";
                body += "&message=" + message;
                body += "&uri=" + uri;
                body += "&extra_data=" + extra_data;
                body += "&extra_type=" + extra_type;


        action(context,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                body);
    }


    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    public static void action(Context context,
                               String contentType, String body) {
        try {

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/help/feedback");



            if (body != null) {
                http.body(body);

                if (contentType != null) {
                    http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, contentType);
                }
            }


            WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(WorkorderTransactionHandler.class)
                    .useAuth(true)
                    .request(http)
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
