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
    public boolean httpRequest(
            Context context, String handlerName, String key, String method, String path, String options,
            WebTransaction.Priority priority, List<Bundle> transforms) {
        try {
            JsonObject meta = new JsonObject();
            meta.put(PARAM_WEB_METHOD, method);
            meta.put(PARAM_WEB_PATH, path);
            meta.put(PARAM_WEB_OPTIONS, options);

            return send(context, key, meta, null, handlerName, priority, transforms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean httpRequest(
            Context context, String handlerName, String key, String method, String path,
            String options, byte[] payload, String contentType, WebTransaction.Priority priority,
            List<Bundle> transforms) {
        try {
            JsonObject meta = new JsonObject();
            meta.put(PARAM_WEB_METHOD, method);
            meta.put(PARAM_WEB_PATH, path);
            meta.put(PARAM_WEB_OPTIONS, options);
            meta.put(PARAM_WEB_CONTENT_TYPE, contentType);

            StoredObject obj = StoredObject.put(context, "WebTransactionClientStore",
                    StoredObject.randomKey(), null, payload);

            return send(context, key, meta, obj, handlerName, priority, transforms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean httpRequest(
            Context context, String handlerName, String key, String method, String path,
            String options, File payload, String contentType, WebTransaction.Priority priority,
            List<Bundle> transforms) {
        try {
            JsonObject meta = new JsonObject();
            meta.put(PARAM_WEB_METHOD, method);
            meta.put(PARAM_WEB_PATH, path);
            meta.put(PARAM_WEB_OPTIONS, options);
            meta.put(PARAM_WEB_CONTENT_TYPE, contentType);

            StoredObject obj = StoredObject.put(context, "WebTransactionClientStore",
                    StoredObject.randomKey(), null, payload);

            return send(context, key, meta, obj, handlerName, priority, transforms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean send(Context context, String key, JsonObject meta, StoredObject payload, String handlerName,
                        WebTransaction.Priority priority, List<Bundle> transforms) {
        try {
            Intent intent = new Intent(context, WebTransactionService.class);
            intent.putExtra(PARAM_KEY, key);
            intent.putExtra(PARAM_META, meta.toByteArray());
            if (payload != null)
                intent.putExtra(PARAM_STORED_OBJECT_ID, payload.getId());
            intent.putExtra(PARAM_HANDLER_NAME, handlerName);
            intent.putExtra(PARAM_PRIORITY, priority.ordinal());
            intent.putExtra(PARAM_TRANSFORM_LIST, (Parcelable[]) transforms.toArray());
            context.startService(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
