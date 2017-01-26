package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;

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
     */
    public static void updateCustomFieldVisibility(Context context, Integer customFieldId, Integer clientId, String visibility) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/custom-fields/" + customFieldId + "/visibility/client/" + clientId + "/" + visibility);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/custom-fields/{custom_field_id}/visibility/client/{client_id}/{visibility}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Updates a work order custom field's visibility for all projects and clients
     *
     * @param customFieldId Custom field id
     * @param visibility Visibility (visible or hidden)
     */
    public static void updateCustomFieldVisibility(Context context, Integer customFieldId, String visibility) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/custom-fields/" + customFieldId + "/visibility/" + visibility);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/custom-fields/{custom_field_id}/visibility/{visibility}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Removes a work order custom field
     *
     * @param customFieldId Custom field id
     */
    public static void removeCustomField(Context context, Integer customFieldId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/custom-fields/" + customFieldId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/custom-fields/{custom_field_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

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
     */
    public static void updateCustomField(Context context, Integer customFieldId, CustomField json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/custom-fields/" + customFieldId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/custom-fields/{custom_field_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Adds a work order custom field
     *
     * @param json JSON Model
     */
    public static void addCustomField(Context context, CustomField json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/custom-fields");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/custom-fields")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Gets a list of work order custom fields
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getCustomFields(Context context, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/custom-fields");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/custom-fields")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

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
     */
    public static void updateCustomFieldVisibility(Context context, Integer customFieldId, Integer projectId, String visibility) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/custom-fields/" + customFieldId + "/visibility/project/" + projectId + "/" + visibility);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/custom-fields/{custom_field_id}/visibility/project/{project_id}/{visibility}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

}
