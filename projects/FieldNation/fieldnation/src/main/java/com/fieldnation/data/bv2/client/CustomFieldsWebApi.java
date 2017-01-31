package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.bv2.listener.TransactionListener;
import com.fieldnation.data.bv2.listener.TransactionParams;
import com.fieldnation.data.bv2.model.CustomField;
import com.fieldnation.data.bv2.model.CustomFields;
import com.fieldnation.data.bv2.model.Error;
import com.fieldnation.data.bv2.model.IdResponse;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/30/17.
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

    public boolean subCustomFieldsWebApi() {
        return register("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi");
    }

    /**
     * Swagger operationId: updateCustomFieldVisibilityByClient
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
                    .key(misc.md5("PUT//api/rest/v2/custom-fields/" + customFieldId + "/visibility/client/" + clientId + "/" + visibility))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId + "/visibility/client/" + clientId + "/" + visibility,
                                    CustomFieldsWebApi.class, "updateCustomFieldVisibility"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomFieldVisibility(Integer customFieldId, Integer clientId, String visibility) {
        return register("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId + "/visibility/client/" + clientId + "/" + visibility);
    }

    /**
     * Swagger operationId: updateCustomFieldVisibility
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
                    .key(misc.md5("PUT//api/rest/v2/custom-fields/" + customFieldId + "/visibility/" + visibility))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId + "/visibility/" + visibility,
                                    CustomFieldsWebApi.class, "updateCustomFieldVisibility"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomFieldVisibility(Integer customFieldId, String visibility) {
        return register("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId + "/visibility/" + visibility);
    }

    /**
     * Swagger operationId: removeCustomField
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
                    .key(misc.md5("DELETE//api/rest/v2/custom-fields/" + customFieldId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId,
                                    CustomFieldsWebApi.class, "removeCustomField"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveCustomField(Integer customFieldId) {
        return register("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId);
    }

    /**
     * Swagger operationId: updateCustomField
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
                    .key(misc.md5("PUT//api/rest/v2/custom-fields/" + customFieldId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId,
                                    CustomFieldsWebApi.class, "updateCustomField"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomField(Integer customFieldId) {
        return register("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId);
    }

    /**
     * Swagger operationId: addCustomField
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
                    .key(misc.md5("POST//api/rest/v2/custom-fields"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/custom-fields",
                                    CustomFieldsWebApi.class, "addCustomField"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddCustomField() {
        return register("TOPIC_ID_WEB_API_V2/custom-fields");
    }

    /**
     * Swagger operationId: getCustomFields
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
                    .key(misc.md5("GET//api/rest/v2/custom-fields"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/custom-fields",
                                    CustomFieldsWebApi.class, "getCustomFields"))
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
        return register("TOPIC_ID_WEB_API_V2/custom-fields");
    }

    /**
     * Swagger operationId: updateCustomFieldVisibilityByProject
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
                    .key(misc.md5("PUT//api/rest/v2/custom-fields/" + customFieldId + "/visibility/project/" + projectId + "/" + visibility))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId + "/visibility/project/" + projectId + "/" + visibility,
                                    CustomFieldsWebApi.class, "updateCustomFieldVisibilityByProject"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomFieldVisibilityByProject(Integer customFieldId, Integer projectId, String visibility) {
        return register("TOPIC_ID_WEB_API_V2/CustomFieldsWebApi/" + customFieldId + "/visibility/project/" + projectId + "/" + visibility);
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            new AsyncParser(this, (Bundle) payload);
        }

        public void onCustomFieldsWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onUpdateCustomFieldVisibility(byte[] data, boolean success, Error error) {
        }

        public void onUpdateCustomFieldVisibilityByProject(byte[] data, boolean success, Error error) {
        }

        public void onRemoveCustomField(byte[] data, boolean success, Error error) {
        }

        public void onUpdateCustomField(byte[] data, boolean success, Error error) {
        }

        public void onAddCustomField(IdResponse idResponse, boolean success, Error error) {
        }

        public void onGetCustomFields(CustomFields customFields, boolean success, Error error) {
        }

    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "CustomFieldsWebApi.AsyncParser";

        private Listener listener;
        private TransactionParams transactionParams;
        private boolean success;
        private byte[] data;

        private Object successObject;
        private Object failObject;

        public AsyncParser(Listener listener, Bundle bundle) {
            this.listener = listener;
            transactionParams = bundle.getParcelable("params");
            success = bundle.getBoolean("success");
            data = bundle.getByteArray("data");

            executeEx();
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (transactionParams.apiFunction) {
                    case "updateCustomFieldVisibility":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateCustomFieldVisibilityByProject":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeCustomField":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateCustomField":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addCustomField":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getCustomFields":
                        if (success)
                            successObject = CustomFields.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    default:
                        Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                        break;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                listener.onCustomFieldsWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "updateCustomFieldVisibility":
                        listener.onUpdateCustomFieldVisibility((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "updateCustomFieldVisibilityByProject":
                        listener.onUpdateCustomFieldVisibilityByProject((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "removeCustomField":
                        listener.onRemoveCustomField((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "updateCustomField":
                        listener.onUpdateCustomField((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "addCustomField":
                        listener.onAddCustomField((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "getCustomFields":
                        listener.onGetCustomFields((CustomFields) successObject, success, (Error) failObject);
                        break;
                    default:
                        Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                        break;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }
}
