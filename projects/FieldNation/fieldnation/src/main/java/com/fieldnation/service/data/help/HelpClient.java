package com.fieldnation.service.data.help;

import android.content.Context;

import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by Shoaib on 7/7/2015.
 */
public class HelpClient {

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    public static void sendContactUsFeedback(Context context, String message, String internalTeam, String uri, String extra_data, String extra_type) {
        WebTransactionService.queueTransaction(context,
                HelpTransactionBuilder.actionPostContactUsIntent(message, internalTeam, uri, extra_data, extra_type));
    }


}
