package com.fieldnation.service.data.workorder;

import android.content.Context;

/**
 * Created by Shoaib on 8/8/2015.
 */
public class RateClient {

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    public static void sendRating(Context context, int satisfactionRating, int scopeRating,
                                  int respectRating, int respectComment, boolean recommendBuyer, String otherComments, long workorderId) {
        context.startService(
                RateTransactionBuilder.actionPostRatingIntent(context, satisfactionRating, scopeRating,
                        respectRating, respectComment, recommendBuyer, otherComments, workorderId));
    }


}
