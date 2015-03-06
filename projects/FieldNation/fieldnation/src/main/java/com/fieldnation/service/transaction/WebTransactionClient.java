package com.fieldnation.service.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;

import java.io.File;
import java.util.List;

/**
 * Created by Michael Carver on 2/27/2015.
 */
public class WebTransactionClient implements WebTransactionConstants {
    public static final String TAG = "service.transaction.TransactionClient";

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public WebTransactionClient() {
    }

    /*-*****************************-*/
    /*-         Commands            -*/
    /*-*****************************-*/



    /**
     * @param context    required, application context
     * @param handler    optional, the class that will handle the transaction response
     * @param key        optional, a key to identify this query. If set, and a transaction with
     *                   this key already exists, then that transaction will be overwritten
     * @param method     required, the http method name to use
     * @param headers    optional, http headers to add to the request
     * @param path       required, the path to query
     * @param urlParams  optional, additional url parameters
     * @param extras     optional, any extra information that the WebTransactionHandler needs
     * @param priority   required, the priority level to execute the class at
     * @param transforms optional, a list of data transforms that this query is expected to perform
     *                   when complete
     * @return true if query sent, false otherwise
     */
    public static boolean httpRequest(
            Context context,
            String method,
            JsonObject headers,
            String path,
            String urlParams,
            WebTransaction.Priority priority,
            String key,
            JsonObject extras,
            List<Bundle> transforms,
            Class<? extends WebTransactionHandler> handler
    ) {
        try {
            JsonObject meta = new JsonObject();
            meta.put(PARAM_WEB_METHOD, method);
            meta.put(PARAM_WEB_PATH, path);

            if (urlParams != null)
                meta.put(PARAM_WEB_URL_PARAMS, urlParams);

            if (extras != null)
                meta.put(PARAM_WEB_EXTRAS, extras);

            if (headers != null)
                meta.put(PARAM_WEB_HEADERS, headers);

            return send(context, meta, priority, null, key, transforms, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @param context     required, application context
     * @param method      required, the http method name to use
     * @param headers     optional, http headers to add to the request
     * @param path        required, the path to query
     * @param urlParams   optional, additional url paramters
     * @param payload     required, data to send as the body of the message
     * @param contentType required, the http content type to use
     * @param priority    required, the priority level to execute the class at
     * @param key         optional, a key to identify this query. If set, and a transaction with
     *                    this key already exists, then that transaction will be overwritten
     * @param extras      optional, any extra information that the WebTransactionHandler needs
     * @param transforms  optional, a list of data transforms that this query is expected to perform
     *                    when complete
     * @param handler     optional, the class that will handle the transaction response
     * @return true if query sent, false otherwise
     */
    public static boolean httpRequest(
            Context context,
            String method,
            JsonObject headers,
            String path,
            String urlParams,
            byte[] payload,
            String contentType,
            WebTransaction.Priority priority,
            String key,
            JsonObject extras,
            List<Bundle> transforms,
            Class<? extends WebTransactionHandler> handler
    ) {
        try {
            JsonObject meta = new JsonObject();
            meta.put(PARAM_WEB_METHOD, method);
            meta.put(PARAM_WEB_PATH, path);
            meta.put(PARAM_WEB_CONTENT_TYPE, contentType);

            if (urlParams != null)
                meta.put(PARAM_WEB_URL_PARAMS, urlParams);

            if (extras != null)
                meta.put(PARAM_WEB_EXTRAS, extras);

            if (headers != null)
                meta.put(PARAM_WEB_HEADERS, headers);

            StoredObject obj = StoredObject.put(context, "WebTransactionClientStore",
                    StoredObject.randomKey(), null, payload);

            return send(context, meta, priority, obj, key, transforms, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @param context     required, application context
     * @param method      required, the http method name to use
     * @param headers     optional, http headers to add to the request
     * @param path        required, the path to query
     * @param urlParams   optional, additional url paramters
     * @param payload     required, the file to send to the server
     * @param contentType required, the http content type to use
     * @param priority    required, the priority level to execute the class at
     * @param key         optional, a key to identify this query. If set, and a transaction with
     *                    this key already exists, then that transaction will be overwritten
     * @param extras      optional, any extra information that the WebTransactionHandler needs
     * @param transforms  optional, a list of data transforms that this query is expected to perform
     *                    when complete
     * @param handler     optional, the class that will handle the transaction response
     * @return true if query sent, false otherwise
     */
    public static boolean httpRequest(
            Context context,
            String method,
            JsonObject headers,
            String path,
            String urlParams,
            File payload,
            String contentType,
            WebTransaction.Priority priority,
            String key,
            JsonObject extras,
            List<Bundle> transforms,
            Class<? extends WebTransactionHandler> handler
    ) {
        try {
            JsonObject meta = new JsonObject();
            meta.put(PARAM_WEB_METHOD, method);
            meta.put(PARAM_WEB_PATH, path);
            meta.put(PARAM_WEB_CONTENT_TYPE, contentType);

            if (urlParams != null)
                meta.put(PARAM_WEB_URL_PARAMS, urlParams);

            if (extras != null)
                meta.put(PARAM_WEB_EXTRAS, extras);

            if (headers != null)
                meta.put(PARAM_WEB_HEADERS, headers);

            StoredObject obj = StoredObject.put(context, "WebTransactionClientStore",
                    StoredObject.randomKey(), null, payload);

            return send(context, meta, priority, obj, key, transforms, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Sends a web transaction query to the transaction service.
     *
     * @param context    required, application context
     * @param meta       required, the http query information
     * @param priority   required, the priority level to execute the class at
     * @param payload    optional, the data to send in the query
     * @param key        optional, a key to identify this query. If set, and a transaction with
     *                   this key already exists, then that transaction will be overwritten
     * @param transforms optional, a list of data transforms that this query is expected to perform
     *                   when complete
     * @param handler    optional, the class that will handle the transaction response
     * @return true if query sent, false otherwise
     */
    public static boolean send(
            Context context,
            JsonObject meta,
            WebTransaction.Priority priority,
            StoredObject payload,
            String key,
            List<Bundle> transforms,
            Class<? extends WebTransactionHandler> handler
    ) {
        try {
            Intent intent = new Intent(context, WebTransactionService.class);
            intent.putExtra(PARAM_META, meta.toByteArray());
            intent.putExtra(PARAM_PRIORITY, priority.ordinal());

            if (handler != null)
                intent.putExtra(PARAM_HANDLER_NAME, handler.getName());

            if (key != null)
                intent.putExtra(PARAM_KEY, key);

            if (payload != null)
                intent.putExtra(PARAM_STORED_OBJECT_ID, payload.getId());

            if (transforms != null)
                intent.putExtra(PARAM_TRANSFORM_LIST, (Parcelable[]) transforms.toArray());

            context.startService(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
