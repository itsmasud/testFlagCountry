package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by dmgen from swagger on 1/25/17.
 */

public class BonusesTransactionBuilder {
    private static final String TAG = "BonusesTransactionBuilder";

    /**
     * Removes a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonusId ID of Bonus
     */
    public static void removeBonus(Context context, int bonusId) {
    }

    /**
     * Updates a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonusId Bonus ID
     * @param json JSON Model
     */
    public static void updateBonus(Context context, int bonusId, PayModifier json) {
    }

    /**
     * Adds a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param json JSON Model
     */
    public static void addBonus(Context context, PayModifier json) {
    }

}
