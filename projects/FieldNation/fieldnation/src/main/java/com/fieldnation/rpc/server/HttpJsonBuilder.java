package com.fieldnation.rpc.server;

import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.utils.misc;

import java.security.SecureRandom;
import java.text.ParseException;

/**
 * Created by Michael Carver on 3/10/2015.
 */
public class HttpJsonBuilder {
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
    public static final String PARAM_WEB_BODY_SOID = "PARAM_BODY_SOID";
    public static final String PARAM_WEB_BODY = "PARAM_BODY";
    public static final String PARAM_DO_NOT_READ = "PARAM_DO_NOT_READ";

    public static final String PARAM_NOTIFICATION_ID = "notification.id";

    public static final String PARAM_NOTIFICATION_START_TICKER = "notification.start.ticker";
    public static final String PARAM_NOTIFICATION_START_TITLE = "notification.start.title";
    public static final String PARAM_NOTIFICATION_START_BODY = "notification.start.body";
    public static final String PARAM_NOTIFICATION_START_ICON = "notification.start.icon";

    public static final String PARAM_NOTIFICATION_SUCCESS_TICKER = "notification.success.ticker";
    public static final String PARAM_NOTIFICATION_SUCCESS_TITLE = "notification.success.title";
    public static final String PARAM_NOTIFICATION_SUCCESS_BODY = "notification.success.body";
    public static final String PARAM_NOTIFICATION_SUCCESS_ICON = "notification.success.icon";

    public static final String PARAM_NOTIFICATION_FAILED_TICKER = "notification.failed.ticker";
    public static final String PARAM_NOTIFICATION_FAILED_TITLE = "notification.failed.title";
    public static final String PARAM_NOTIFICATION_FAILED_BODY = "notification.failed.body";
    public static final String PARAM_NOTIFICATION_FAILED_ICON = "notification.failed.icon";

    public static final String PARAM_NOTIFICATION_RETRY_TICKER = "notification.retry.ticker";
    public static final String PARAM_NOTIFICATION_RETRY_TITLE = "notification.retry.title";
    public static final String PARAM_NOTIFICATION_RETRY_BODY = "notification.retry.body";
    public static final String PARAM_NOTIFICATION_RETRY_ICON = "notification.retry.icon";


    private JsonObject request;
    private JsonObject headers;
    private JsonObject multiPartFields;
    private JsonObject multiPartFiles;

    private static final SecureRandom _random = new SecureRandom();

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

    public HttpJsonBuilder doNotRead() throws ParseException {
        getRequest();
        request.put(PARAM_DO_NOT_READ, true);
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

    public HttpJsonBuilder notify(String titleStart, String tickerStart, String contentStart, int iconStart,
                                  String titleSuccess, String tickerSuccess, String contentSuccess, int iconSuccess,
                                  String titleFailed, String tickerFailed, String contentFailed, int iconFailed,
                                  String titleRetry, String tickerRetry, String contentRetry, int iconRetry) throws ParseException {
        getRequest();
        if (!misc.isEmptyOrNull(titleStart)) {
            request.put(PARAM_NOTIFICATION_ID, _random.nextInt(Integer.MAX_VALUE));

            request.put(PARAM_NOTIFICATION_START_TITLE, titleStart);
            request.put(PARAM_NOTIFICATION_START_BODY, contentStart);
            request.put(PARAM_NOTIFICATION_START_ICON, iconStart);
            request.put(PARAM_NOTIFICATION_START_TICKER, tickerStart);

            request.put(PARAM_NOTIFICATION_SUCCESS_TITLE, titleSuccess);
            request.put(PARAM_NOTIFICATION_SUCCESS_BODY, contentSuccess);
            request.put(PARAM_NOTIFICATION_SUCCESS_ICON, iconSuccess);
            request.put(PARAM_NOTIFICATION_SUCCESS_TICKER, tickerSuccess);

            request.put(PARAM_NOTIFICATION_FAILED_TITLE, titleFailed);
            request.put(PARAM_NOTIFICATION_FAILED_BODY, contentFailed);
            request.put(PARAM_NOTIFICATION_FAILED_ICON, iconFailed);
            request.put(PARAM_NOTIFICATION_FAILED_TICKER, tickerFailed);

            request.put(PARAM_NOTIFICATION_RETRY_TITLE, titleRetry);
            request.put(PARAM_NOTIFICATION_RETRY_BODY, contentRetry);
            request.put(PARAM_NOTIFICATION_RETRY_ICON, iconRetry);
            request.put(PARAM_NOTIFICATION_RETRY_TICKER, tickerRetry);
        }
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

    public HttpJsonBuilder multipartFile(String fieldName, String filename, StoredObject obj) throws ParseException {
        getMultiPartFile();
        JsonObject f = new JsonObject();
        f.put("filename", filename);
        f.put("soid", obj.getId());
        f.put("contentType", App.guessContentTypeFromName(filename));
        multiPartFiles.put(fieldName, f);
        return this;
    }

    public HttpJsonBuilder multipartFile(String fieldName, String filename, Uri uri) throws ParseException {
        getMultiPartFile();
        JsonObject f = new JsonObject();
        f.put("uri", uri.toString());
        f.put("filename", filename);
        f.put("contentType", App.guessContentTypeFromName(filename));
        multiPartFiles.put(fieldName, f);
        return this;
    }

    public JsonObject build() {
        return request;
    }

}
