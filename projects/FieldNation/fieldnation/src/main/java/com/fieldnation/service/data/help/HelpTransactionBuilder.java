package com.fieldnation.service.data.help;

import android.content.Context;

import com.fieldnation.GlobalState;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.data.help.HelpConstants;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class HelpTransactionBuilder {

//    public static void get(Context context, long profileId, boolean isSync) {
//        try {
//            HttpJsonBuilder http = new HttpJsonBuilder()
//            HttpJsonBuilder http = new HttpJsonBuilder()
//                    .protocol("https")
//                    .method("GET");
//
//            if (profileId > 0) {
//                http.path("/api/rest/v1/profile/" + profileId);
//            } else {
//                http.path("/api/rest/v1/profile");
//            }
//
//            WebTransactionBuilder.builder(context)
//                    .priority(Priority.HIGH)
//                    .handler(ProfileTransactionHandler.class)
//                    .handlerParams(ProfileTransactionHandler.pGet(profileId))
//                    .key((isSync ? "Sync/" : "") + "ProfileGet")
//                    .isSyncCall(isSync)
//                    .useAuth(true)
//                    .request(http)
//                    .send();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }





    /*
    Newly edited.
   Date: July 07, 2015
     */

    public static void actionPostFeedback(Context context,
                                          String topic,  String message, String uri,  String extra_data ,  String extra_type ) {
        try {
            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/help/feedback");

            JsonObject body = new JsonObject();
            body.put("topic", "android");
            body.put("message", message);
            body.put("uri", uri);
            body.put("extra_data", extra_data);
            body.put("extra_type", extra_type);

            http.body("[" + body.toString() + "]");

            http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, "application/json");

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(http)
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}
