package com.fieldnation.fnhttp;

import android.net.Uri;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.misc;

import java.security.SecureRandom;
import java.text.ParseException;

/**
 * Created by Michael Carver on 3/10/2015.
 */
public class HttpJsonBuilder {
    private static final String TAG = "HttpJsonBuilder";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_TYPE_FORM_ENCODED = "application/x-www-form-urlencoded";

    public static final String PARAM_WEB_PROTOCOL = "protocol";
    public static final String PARAM_WEB_METHOD = "method";
    public static final String PARAM_WEB_HOST = "host";
    public static final String PARAM_WEB_PATH = "path";
    public static final String PARAM_WEB_URL_PARAMS = "urlParams";
    public static final String PARAM_WEB_HEADERS = "headers";
    public static final String PARAM_WEB_MULTIPART = "multipart";
    public static final String PARAM_WEB_MULTIPART_FILES = "multipart.files";
    public static final String PARAM_WEB_MULTIPART_FIELDS = "multipart.fields";
    public static final String PARAM_WEB_BODY_SOID = "bodySoID";
    public static final String PARAM_WEB_BODY = "body";

    private static final SecureRandom _random = new SecureRandom();
/*
    public static final String PARAM_NOTIFICATION_ID = "notification.id";
    public static final String PARAM_NOTIFICATION_START = "notification.start";
    public static final String PARAM_NOTIFICATION_SUCCESS = "notification.success";
    public static final String PARAM_NOTIFICATION_FAILED = "notification.failed";
    public static final String PARAM_NOTIFICATION_RETRY = "notification.retry";
*/

    public static final String PARAM_TIMING_KEY = "timingKey";

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

    public HttpJsonBuilder protocol(String protocol) throws ParseException {
        getRequest();
        if (!misc.isEmptyOrNull(protocol))
            request.put(PARAM_WEB_PROTOCOL, protocol);
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

    public HttpJsonBuilder timingKey(String timingKey) throws ParseException {
        getRequest();
        if (!misc.isEmptyOrNull(timingKey))
            request.put(PARAM_TIMING_KEY, timingKey);
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

/*
    public HttpJsonBuilder notify(NotificationDefinition start, NotificationDefinition success,
                                  NotificationDefinition failed, NotificationDefinition retry) throws ParseException {
        getRequest();
        request.put(PARAM_NOTIFICATION_ID, _random.nextInt(Integer.MAX_VALUE));

        request.put(PARAM_NOTIFICATION_START, start.toJson());
        request.put(PARAM_NOTIFICATION_SUCCESS, success.toJson());
        request.put(PARAM_NOTIFICATION_FAILED, failed.toJson());
        request.put(PARAM_NOTIFICATION_RETRY, retry.toJson());

        return this;
    }
*/

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

    public HttpJsonBuilder multipartFile(String fieldName, String filename, StoredObject obj) throws ParseException {
        getMultiPartFile();
        JsonObject f = new JsonObject();
        f.put("filename", filename);
        f.put("soid", obj.getId());
        f.put("contentType", misc.guessContentTypeFromName(filename));
        multiPartFiles.put(fieldName, f);
        return this;
    }

    public HttpJsonBuilder multipartFile(String fieldName, String filename, Uri uri) throws ParseException {
        getMultiPartFile();
        JsonObject f = new JsonObject();
        f.put("uri", uri.toString());
        f.put("filename", filename);
        f.put("contentType", misc.guessContentTypeFromName(filename));
        multiPartFiles.put(fieldName, f);
        return this;
    }

    public JsonObject build() {
        return request;
    }

    public static boolean isFieldNation(JsonObject request) {
        try {
            String protocol = "";
            String path = "";
            String hostname = "";

            if (request.has(HttpJsonBuilder.PARAM_WEB_PROTOCOL)) {
                protocol = request.getString(HttpJsonBuilder.PARAM_WEB_PROTOCOL);
            }
            if (request.has(HttpJsonBuilder.PARAM_WEB_PATH)) {
                path = request.getString(HttpJsonBuilder.PARAM_WEB_PATH);
            }
            if (request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
                hostname = request.getString(HttpJsonBuilder.PARAM_WEB_HOST);
            }

            if (protocol == null)
                protocol = "";

            if (protocol.length() > 0 && !protocol.endsWith("://"))
                protocol += "://";

            if (path == null)
                path = "";

            if (hostname == null)
                hostname = "";

            String url = protocol + hostname + path;

            return url.startsWith("https://app.fieldnation.com");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return false;
    }

}
