package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by dmgen from swagger on 1/25/17.
 */

public class SystemTransactionBuilder {
    private static final String TAG = "SystemTransactionBuilder";

    /**
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json JSON parameters of the change
     */
    public static void updateModel(Context context, String path, String event, KeyValue json) {
    }

}
