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
    private List<Bundle> transforms;

    private JsonObject request;
    private JsonObject headers;
    private JsonObject extras;
    private JsonObject multiPartFields;
    private JsonObject multiPartFiles;

    public WebTransactionBuilder(Context context) {
        this.context = context;
        intent = new Intent(context, WebTransactionService.class);
        intent.putExtra(PARAM_PRIORITY, WebTransaction.Priority.NORMAL);
    }

    public static WebTransactionBuilder builder(Context context) {
        return new WebTransactionBuilder(context);
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

    // Request
    private void getRequest() {
        if (request == null)
            request = new JsonObject();
    }

    public WebTransactionBuilder method(String method) throws ParseException {
        getRequest();
        request.put(PARAM_WEB_METHOD, method);
        return this;
    }

    public WebTransactionBuilder path(String path) throws ParseException {
        getRequest();
        request.put(PARAM_WEB_PATH, path);
        return this;
    }

    public WebTransactionBuilder host(String host) throws ParseException {
        getRequest();
        request.put(PARAM_WEB_HOST, host);
        return this;
    }

    public WebTransactionBuilder urlParams(String urlParams) throws ParseException {
        getRequest();
        request.put(PARAM_WEB_URL_PARAMS, urlParams);
        return this;
    }

    public WebTransactionBuilder body(StoredObject obj) throws ParseException {
        getRequest();
        request.put(PARAM_WEB_BODY_SOID, obj.getId());
        return this;
    }

    // Extras
    private void getExtras() throws ParseException {
        if (extras == null) {
            extras = new JsonObject();
            getRequest();
            request.put(PARAM_WEB_EXTRAS, extras);
        }
    }

    public WebTransactionBuilder extra(String key, Object value) throws ParseException {
        getExtras();
        extras.put(key, value);
        return this;
    }


    // Headers
    private void getHeaders() throws ParseException {
        if (headers == null) {
            headers = new JsonObject();
            getRequest();
            request.put(PARAM_WEB_HEADERS, headers);
        }
    }

    public WebTransactionBuilder header(String key, String value) throws ParseException {
        getHeaders();
        headers.put(key, value);
        return this;
    }

    // Multipart
    private void getMultiPartField() throws ParseException {
        if (multiPartFields == null) {
            multiPartFields = new JsonObject();
            getRequest();
            request.put("multipart/fields", multiPartFields);
        }
    }

    public WebTransactionBuilder multipartField(String key, Object value) throws ParseException {
        getMultiPartField();
        multiPartFields.put(key, value);
        return this;
    }

    private void getMultiPartFile() throws ParseException {
        if (multiPartFiles == null) {
            multiPartFiles = new JsonObject();
            getRequest();
            request.put("multipart/files", multiPartFiles);
        }
    }

    public WebTransactionBuilder multipartFile(String fieldName, String filename, StoredObject obj, String contentType) throws ParseException {
        getMultiPartFile();
        JsonObject f = new JsonObject();
        f.put("filename", filename);
        f.put("soid", obj.getId());
        f.put("contentType", contentType);
        multiPartFiles.put(fieldName, f);
        return this;
    }

    public void send() {
        if (request != null) {
            intent.putExtra(PARAM_REQUEST, request.toByteArray());
        }

        if (transforms != null) {
            intent.putExtra(PARAM_TRANSFORM_LIST, (Parcelable[]) transforms.toArray());
        }

        context.startService(intent);
    }

}
