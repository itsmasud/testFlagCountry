package com.fieldnation.service.data.restv2;

import android.content.Context;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestTransactionBuilder {
    private static final String REST_PATH = "/api/rest/v2/";

    public static void list(Context context, String resultTag, String objectType, String params, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(RestTransactionHandler.class)
                    .handlerParams(RestTransactionHandler.pList(resultTag))
                    .key((isSync ? "Sync/" : "") + "List" + objectType)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path(REST_PATH + objectType)
                            .urlParams(params))

                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void action(Context context, String resultTag, String objectType, String id,
                              String action, String params, String contentType, String body, boolean isSync) {
        try {
            JsonObject actionDescription = new JsonObject();
            actionDescription.put("_action[0].resultTag", resultTag);
            actionDescription.put("_action[0].action", action);
            actionDescription.put("_action[0].params", params);

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v1/" + objectType + "/" + id + "/" + action);

            if (params != null) {
                http.urlParams(params);
            }

            if (body != null) {
                http.body(body);

                if (contentType != null) {
                    http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, contentType);
                }
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(RestTransactionHandler.class)
                    .handlerParams(RestTransactionHandler.pObject(resultTag))
                    .key((isSync ? "Sync/" : "") + objectType + "/" + id + "/" + action)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(http)
                    .transform(Transform.makeTransformQuery(
                            objectType,
                            id,
                            "merges",
                            actionDescription.toByteArray()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void create(Context context, String resultTag, String objectType, String body) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(RestTransactionHandler.class)
                    .handlerParams(RestTransactionHandler.pObject(resultTag))
                    .key("Create" + objectType)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path(REST_PATH + objectType)
                            .body(body))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
