package com.fieldnation.service.data.restv2;

import android.content.Context;

import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

import java.util.Map;
import java.util.Set;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestTransactionBuilder {
    private static final String REST_PATH = "/api/rest/v2/";

    public static void list(Context context, String resultTag, String objectType,String params, boolean isSync) {
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
}
