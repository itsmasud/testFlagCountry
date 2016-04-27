package com.fieldnation.service.data.contact;

import android.content.Context;

/**
 * Created by Shoaib on 7/7/2015.
 */
public class ContactUsClient {

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    public static void sendContactUsFeedback(Context context, String message, String internalTeam, String uri, String extra_data, String extra_type) {
        context.startService(
                ContactUsTransactionBuilder.actionPostContactUsIntent(context, message, internalTeam, uri, extra_data, extra_type));
    }


}
