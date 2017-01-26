package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class BonusesWebApi {
    private static final String TAG = "BonusesWebApi";

    /**
     * Removes a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonusId ID of Bonus
     * @param isBackground indicates that this call is low priority
     */
    public static void removeBonus(Context context, Integer bonusId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/bonuses/{bonus_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v2/bonuses/" + bonusId)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Updates a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonusId Bonus ID
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void updateBonus(Context context, Integer bonusId, PayModifier json, boolean isBackground) {
    }

    /**
     * Adds a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void addBonus(Context context, PayModifier json, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/bonuses")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/bonuses")
                            .body(json.toJson().toString())
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

}
