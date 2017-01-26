package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.KeyValue;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class SystemWebApi {
    private static final String TAG = "SystemWebApi";

    /**
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path  The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json  JSON parameters of the change
     */
    public static void updateModel(Context context, String path, String event, KeyValue json) {
    }

    /**
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path  The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json  JSON parameters of the change
     * @param async Return the model in the response (slower) (Optional)
     */
    public static void updateModel(Context context, String path, String event, KeyValue json, Boolean async) {
    }

}
