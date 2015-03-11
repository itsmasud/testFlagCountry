package com.fieldnation.rpc.server;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.utils.misc;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/10/2015.
 */
public class HttpJsonBuilder {
    public static final String PARAM_WEB_METHOD = "method";
    public static final String PARAM_WEB_HOST = "host";
    public static final String PARAM_WEB_PATH = "path";
    public static final String PARAM_WEB_URL_PARAMS = "urlParams";
    public static final String PARAM_WEB_HEADERS = "headers";
    public static final String PARAM_WEB_MULTIPART_FILES = "multipart/files";
    public static final String PARAM_WEB_MULTIPART_FIELDS = "multipart/fields";
    public static final String PARAM_WEB_BODY_SOID = "PARAM_BODY_SOID";
    public static final String PARAM_WEB_BODY = "PARAM_BODY";

    private JsonObject request;
    private JsonObject headers;
    private JsonObject multiPartFields;
    private JsonObject multiPartFiles;

    public HttpJsonBuilder() {
    }

    // Request
    private void getRequest() {
        if (request == null)
            request = new JsonObject();
    }

    public HttpJsonBuilder method(String method) throws ParseException {
        getRequest();
        if (!misc.isEmptyOrNull(method))
            request.put(PARAM_WEB_METHOD, method);
        return this;
    }

    public HttpJsonBuilder path(String path) throws ParseException {
        getRequest();
        if (!misc.isEmptyOrNull(path))
            request.put(PARAM_WEB_PATH, path);
        return this;
    }

    public HttpJsonBuilder host(String host) throws ParseException {
        getRequest();
        if (!misc.isEmptyOrNull(host))
            request.put(PARAM_WEB_HOST, host);
        return this;
    }

    public HttpJsonBuilder urlParams(String urlParams) throws ParseException {
        getRequest();
        if (!misc.isEmptyOrNull(urlParams))
            request.put(PARAM_WEB_URL_PARAMS, urlParams);
        return this;
    }

    public HttpJsonBuilder body(StoredObject obj) throws ParseException {
        getRequest();
        if (obj != null)
            request.put(PARAM_WEB_BODY_SOID, obj.getId());
        return this;
    }

    public HttpJsonBuilder body(String body) throws ParseException {
        getRequest();
        if (!misc.isEmptyOrNull(body))
            request.put(PARAM_WEB_BODY, body);
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

    public HttpJsonBuilder header(String key, String value) throws ParseException {
        getHeaders();
        if (!misc.isEmptyOrNull(key) && !misc.isEmptyOrNull(value))
            headers.put(key, value);
        return this;
    }

    // Multipart
    private void getMultiPartField() throws ParseException {
        if (multiPartFields == null) {
            multiPartFields = new JsonObject();
            getRequest();
            request.put(PARAM_WEB_MULTIPART_FIELDS, multiPartFields);
        }
    }

    public HttpJsonBuilder multipartField(String key, Object value) throws ParseException {
        getMultiPartField();
        if (!misc.isEmptyOrNull(key) && value != null)
            multiPartFields.put(key, value);
        return this;
    }

    private void getMultiPartFile() throws ParseException {
        if (multiPartFiles == null) {
            multiPartFiles = new JsonObject();
            getRequest();
            request.put(PARAM_WEB_MULTIPART_FILES, multiPartFiles);
        }
    }

    public HttpJsonBuilder multipartFile(String fieldName, String filename, StoredObject obj, String contentType) throws ParseException {
        getMultiPartFile();
        JsonObject f = new JsonObject();
        f.put("filename", filename);
        f.put("soid", obj.getId());
        f.put("contentType", contentType);
        multiPartFiles.put(fieldName, f);
        return this;
    }

    public JsonObject build() {
        return request;
    }

}
