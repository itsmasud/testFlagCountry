package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/27/17.
 */

public class SystemWebApi extends TopicClient {
    private static final String STAG = "SystemWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public SystemWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }
    /**
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json JSON parameters of the change
     */
    public static void updateModel(Context context, String path, String event, KeyValue json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/system/update-model")
                    .urlParams("?path=" + path + "&event=" + event);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/system/update-model")
                    .key(misc.md5("POST/" + "/api/rest/v2/system/update-model" + "?path=" + path + "&event=" + event))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateModel(String path, String event) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/system/update-model" + "?path=" + path + "&event=" + event));
    }
    /**
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json JSON parameters of the change
     * @param async Return the model in the response (slower) (Optional)
     */
    public static void updateModel(Context context, String path, String event, KeyValue json, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/system/update-model")
                    .urlParams("?path=" + path + "&event=" + event + "&async=" + async);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/system/update-model")
                    .key(misc.md5("POST/" + "/api/rest/v2/system/update-model" + "?path=" + path + "&event=" + event + "&async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateModel(String path, String event, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/system/update-model" + "?path=" + path + "&event=" + event + "&async=" + async));
    }
}
