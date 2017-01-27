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

public class PenaltiesWebApi extends TopicClient {
    private static final String STAG = "PenaltiesWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public PenaltiesWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }
    /**
     * Add a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     */
    public static void addPenalty(Context context) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/penalties");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/penalties")
                    .key(misc.md5("POST/" + "/api/rest/v2/penalties"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddPenalty() {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/penalties"));
    }
    /**
     * Removes a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     * @param penaltyId Penalty ID
     */
    public static void removePenalty(Context context, Integer penaltyId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/penalties/{penalty_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemovePenalty(Integer penaltyId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/penalties/" + penaltyId));
    }
    /**
     * Update a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     * @param penaltyId Penalty ID
     * @param json JSON Model
     */
    public static void updatePenalty(Context context, String penaltyId, PayModifier json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/penalties/" + penaltyId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/penalties/{penalty_id}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePenalty(String penaltyId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/penalties/" + penaltyId));
    }
}
