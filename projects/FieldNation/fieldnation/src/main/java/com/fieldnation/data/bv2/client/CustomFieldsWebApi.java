package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.CustomField;
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

public class CustomFieldsWebApi extends TopicClient {
    private static final String STAG = "CustomFieldsWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public CustomFieldsWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    /**
     * Updates a work order custom field's visibility for a single client
     *
     * @param customFieldId Custom field id
     * @param clientId      Client id
     * @param visibility    Visibility (visible or hidden)
     */
    public static void updateCustomFieldVisibility(Context context, Integer customFieldId, Integer clientId, String visibility) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/custom-fields/" + customFieldId + "/visibility/client/" + clientId + "/" + visibility);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/custom-fields/{custom_field_id}/visibility/client/{client_id}/{visibility}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/custom-fields/" + customFieldId + "/visibility/client/" + clientId + "/" + visibility))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomFieldVisibility(Integer customFieldId, Integer clientId, String visibility) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/custom-fields/" + customFieldId + "/visibility/client/" + clientId + "/" + visibility));
    }

    /**
     * Updates a work order custom field's visibility for all projects and clients
     *
     * @param customFieldId Custom field id
     * @param visibility    Visibility (visible or hidden)
     */
    public static void updateCustomFieldVisibility(Context context, Integer customFieldId, String visibility) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/custom-fields/" + customFieldId + "/visibility/" + visibility);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/custom-fields/{custom_field_id}/visibility/{visibility}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/custom-fields/" + customFieldId + "/visibility/" + visibility))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomFieldVisibility(Integer customFieldId, String visibility) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/custom-fields/" + customFieldId + "/visibility/" + visibility));
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/custom-fields/" + customFieldId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveCustomField(Integer customFieldId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/custom-fields/" + customFieldId));
    }

    /**
     * Updates a work order custom field
     *
     * @param customFieldId Custom field id
     * @param json          JSON Model
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/custom-fields/" + customFieldId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomField(Integer customFieldId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/custom-fields/" + customFieldId));
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
                    .key(misc.md5("POST/" + "/api/rest/v2/custom-fields"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddCustomField() {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/custom-fields"));
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
                    .key(misc.md5("GET/" + "/api/rest/v2/custom-fields"))
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

    public boolean subGetCustomFields() {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/custom-fields"));
    }

    /**
     * Updates a work order custom field's visibility for a single project
     *
     * @param customFieldId Custom field id
     * @param projectId     Project id
     * @param visibility    Visibility (visible or hidden)
     */
    public static void updateCustomFieldVisibilityByProject(Context context, Integer customFieldId, Integer projectId, String visibility) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/custom-fields/" + customFieldId + "/visibility/project/" + projectId + "/" + visibility);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/custom-fields/{custom_field_id}/visibility/project/{project_id}/{visibility}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/custom-fields/" + customFieldId + "/visibility/project/" + projectId + "/" + visibility))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomFieldVisibilityByProject(Integer customFieldId, Integer projectId, String visibility) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/custom-fields/" + customFieldId + "/visibility/project/" + projectId + "/" + visibility));
    }
}
