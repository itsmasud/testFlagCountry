package com.fieldnation.service.data.help;

import android.content.Context;

import com.fieldnation.service.data.profile.ProfileTransactionBuilder;

/**
 * Created by Shoaib on 7/7/2015.
 */
public class HelpClient {

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    public static void sendFeedback(Context context, String topic, String message, String uri, String extra_data, String extra_type) {
        HelpTransactionBuilder.actionPostFeedback(context, topic, message, uri, extra_data, extra_type);
    }


}
