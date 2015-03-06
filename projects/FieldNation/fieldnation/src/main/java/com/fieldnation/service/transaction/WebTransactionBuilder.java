package com.fieldnation.service.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WebTransactionBuilder implements WebTransactionConstants {

    private Context context;
    private Intent intent;
    private JsonObject meta;
    private JsonObject headers;
    private JsonObject extras;
    private List<Bundle> transforms;

    public WebTransactionBuilder(Context context) {
        this.context = context;
        intent = new Intent(context, WebTransactionService.class);
        intent.putExtra(PARAM_PRIORITY, WebTransaction.Priority.NORMAL);
    }

    // Meta
    private void getMeta() {
        if (meta == null)
            meta = new JsonObject();
    }

    public WebTransactionBuilder method(String method) throws ParseException {
        getMeta();
        meta.put(PARAM_WEB_METHOD, method);
        return this;
    }

    public WebTransactionBuilder path(String path) throws ParseException {
        getMeta();
        meta.put(PARAM_WEB_PATH, path);
        return this;
    }

    public WebTransactionBuilder urlParams(String urlParams) throws ParseException {
        getMeta();
        meta.put(PARAM_WEB_URL_PARAMS, urlParams);
        return this;
    }

    // Extras
    private void getExtras() throws ParseException {
        if (extras == null) {
            extras = new JsonObject();
            getMeta();
            meta.put(PARAM_WEB_EXTRAS, extras);
        }
    }

    public WebTransactionBuilder extra(String key, Object value) throws ParseException {
        getExtras();
        extras.put(key, value);
        return this;
    }

    // Intent
    public WebTransactionBuilder priority(WebTransaction.Priority priority) {
        intent.putExtra(PARAM_PRIORITY, priority.ordinal());
        return this;
    }

    public WebTransactionBuilder handler(Class<? extends WebTransactionHandler> clazz) {
        intent.putExtra(PARAM_HANDLER_NAME, clazz.getName());
        return this;
    }

    public WebTransactionBuilder key(String key) {
        intent.putExtra(PARAM_KEY, key);
        return this;
    }

    public WebTransactionBuilder body(StoredObject obj) {
        intent.putExtra(PARAM_STORED_OBJECT_ID, obj.getId());
        return this;
    }

    // transforms
    private void getTransforms() {
        if (transforms == null) {
            transforms = new LinkedList<>();
        }
    }

    public WebTransactionBuilder transform(Bundle transform) {
        getTransforms();
        transforms.add(transform);
        return this;
    }

    // Headers
    private void getHeaders() throws ParseException {
        if (headers == null) {
            headers = new JsonObject();
            getMeta();
            meta.put(PARAM_WEB_HEADERS, headers);
        }
    }

    public WebTransactionBuilder header(String key, String value) throws ParseException {
        getHeaders();
        headers.put(key, value);
        return this;
    }

    public void send() {
        if (meta != null)
            intent.putExtra(PARAM_META, meta.toByteArray());

        if (transforms != null) {
            intent.putExtra(PARAM_TRANSFORM_LIST, (Parcelable[]) transforms.toArray());
        }

        context.startService(intent);
    }

}
