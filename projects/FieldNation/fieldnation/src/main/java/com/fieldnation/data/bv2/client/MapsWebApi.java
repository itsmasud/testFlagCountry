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

public class MapsWebApi extends TopicClient {
    private static final String STAG = "MapsWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public MapsWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }
    /**
     * This endpoint returns a list of exact coordinates as well as additional info such as the address and whether the coordinates are an exact match.
     *
     * @param type Type and id of the item being looked up separated by a colon, e.g. workorder:21
     * @param isBackground indicates that this call is low priority
     */
    public static void getMaps(Context context, String type, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/maps/search")
                    .urlParams("?type=" + type);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/maps/search")
                    .key(misc.md5("GET/" + "/api/rest/v2/maps/search" + "?type=" + type))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetMaps(String type) {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/maps/search" + "?type=" + type));
    }
}
