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

public class CustomFieldsWebApi {
    private static final String TAG = "CustomFieldsWebApi";

    /**
     * Updates a work order custom field's visibility for a single client
     *
     * @param customFieldId Custom field id
     * @param clientId Client id
     * @param visibility Visibility (visible or hidden)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateCustomFieldVisibility(Context context, int customFieldId, int clientId, String visibility, boolean isBackground) {
    }

    /**
     * Updates a work order custom field's visibility for all projects and clients
     *
     * @param customFieldId Custom field id
     * @param visibility Visibility (visible or hidden)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateCustomFieldVisibility(Context context, int customFieldId, String visibility, boolean isBackground) {
    }

    /**
     * Removes a work order custom field
     *
     * @param customFieldId Custom field id
     * @param isBackground indicates that this call is low priority
     */
    public static void removeCustomField(Context context, int customFieldId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//custom-fields/{custom_field_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/custom-fields/" + customFieldId)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Updates a work order custom field
     *
     * @param customFieldId Custom field id
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void updateCustomField(Context context, int customFieldId, CustomField json, boolean isBackground) {
    }

    /**
     * Adds a work order custom field
     *
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void addCustomField(Context context, CustomField json, boolean isBackground) {
    }

    /**
     * Gets a list of work order custom fields
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getCustomFields(Context context, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//custom-fields")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/custom-fields")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Updates a work order custom field's visibility for a single project
     *
     * @param customFieldId Custom field id
     * @param projectId Project id
     * @param visibility Visibility (visible or hidden)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateCustomFieldVisibility(Context context, int customFieldId, int projectId, String visibility, boolean isBackground) {
    }

}
