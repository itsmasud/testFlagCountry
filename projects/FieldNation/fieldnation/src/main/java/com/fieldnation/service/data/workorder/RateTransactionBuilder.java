package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Shoaib on 8/8/2015.
 */
public class RateTransactionBuilder {


    public static Intent actionPostRatingIntent(Context context, int satisfactionRating, int scopeRating,
                                                int respectRating, int respectComment, boolean recommendBuyer, String otherComments, long workorderId) {
        try {
            String body = "";

            // parameterized body
            body += "topic=android";
            body += "&satisfaction_rating=" + satisfactionRating;
            body += "&scope_rating=" + scopeRating;
            body += "&respect_rating=" + respectRating;
            body += "&respect_comment=" + respectComment;
            body += "&recommend_buyer=" + recommendBuyer;
            body += "&other_comments=" + otherComments;


            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/rate");

            if (body != null) {
                http.body(body);

                http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED);
            }

            return WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(RateTransactionHandler.class)
                    .handlerParams(RateTransactionHandler.pRating(satisfactionRating, scopeRating, respectRating,
                            respectComment, recommendBuyer, otherComments, workorderId))
                    .useAuth(true)
                    .request(http)
                    .makeIntent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
}
