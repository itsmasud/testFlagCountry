package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.PayModifier;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class PenaltiesWebApi {
    private static final String TAG = "PenaltiesWebApi";

    /**
     * Add a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     */
    public static void addPenalty(Context context) {
    }

    /**
     * Removes a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     * @param penaltyId Penalty ID
     */
    public static void removePenalty(Context context, int penaltyId) {
    }

    /**
     * Update a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     * @param penaltyId Penalty ID
     * @param json      JSON Model
     */
    public static void updatePenalty(Context context, String penaltyId, PayModifier json) {
    }

}
