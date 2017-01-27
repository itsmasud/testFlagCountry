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

public class BonusesWebApi extends TopicClient {
    private static final String STAG = "BonusesWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public BonusesWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }
    /**
     * Removes a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonusId ID of Bonus
     */
    public static void removeBonus(Context context, Integer bonusId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/bonuses/" + bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/bonuses/{bonus_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveBonus(Integer bonusId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/bonuses/" + bonusId));
    }
    /**
     * Updates a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonusId Bonus ID
     * @param json JSON Model
     */
    public static void updateBonus(Context context, Integer bonusId, PayModifier json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/bonuses/" + bonusId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/bonuses/{bonus_id}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateBonus(Integer bonusId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/bonuses/" + bonusId));
    }
    /**
     * Adds a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param json JSON Model
     */
    public static void addBonus(Context context, PayModifier json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/bonuses");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/bonuses")
                    .key(misc.md5("POST/" + "/api/rest/v2/bonuses"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddBonus() {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/bonuses"));
    }
}
