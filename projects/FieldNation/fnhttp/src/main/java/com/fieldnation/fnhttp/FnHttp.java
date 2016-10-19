package com.fieldnation.fnhttp;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;

import java.io.File;
import java.text.ParseException;
import java.util.Iterator;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class FnHttp {
    private static final String TAG = "FnHttp";

    public static Request buildRequest(Context context, JsonObject json) throws Exception {
        String path = "";
        String protocol = "";
        String params = "";
        String hostname = "";
        String method = "";
        String url = null;

        if (json.has(HttpJsonBuilder.PARAM_WEB_PROTOCOL)) {
            protocol = json.getString(HttpJsonBuilder.PARAM_WEB_PROTOCOL);
        }
        if (json.has(HttpJsonBuilder.PARAM_WEB_PATH)) {
            path = json.getString(HttpJsonBuilder.PARAM_WEB_PATH);
        }
        if (json.has(HttpJsonBuilder.PARAM_WEB_URL_PARAMS)) {
            params = json.getString(HttpJsonBuilder.PARAM_WEB_URL_PARAMS);
        }
        if (json.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
            hostname = json.getString(HttpJsonBuilder.PARAM_WEB_HOST);
        }
        if (json.has(HttpJsonBuilder.PARAM_WEB_METHOD)) {
            method = json.getString(HttpJsonBuilder.PARAM_WEB_METHOD);
        }

        JsonObject headers = null;
        if (json.has(HttpJsonBuilder.PARAM_WEB_HEADERS)) {
            headers = json.getJsonObject(HttpJsonBuilder.PARAM_WEB_HEADERS);
        }

        if (misc.isEmptyOrNull(method))
            method = "GET";

        if (protocol == null)
            protocol = "";

        if (protocol.length() > 0 && !protocol.endsWith("://"))
            protocol += "://";

        if (path == null)
            path = "";

        if (hostname == null)
            hostname = "";

        if (params == null)
            params = "";

        checkUrlOptions(params);

        url = protocol + hostname + path + params;

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("Pragma", "no-cache")
                .addHeader("Cache-Control", "no-cache")
                .cacheControl(CacheControl.FORCE_NETWORK);

        if (headers != null) {
            Iterator<String> e = headers.keys();
            while (e.hasNext()) {
                String key = e.next();
                requestBuilder.addHeader(key, headers.getString(key));
            }
        }

        if (json.has(HttpJsonBuilder.PARAM_WEB_MULTIPART)) {
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            Log.v(TAG, "PARAM_WEB_MULTIPART");
            if (json.has(HttpJsonBuilder.PARAM_WEB_MULTIPART_FIELDS)) {
                Log.v(TAG, "PARAM_WEB_MULTIPART_FIELDS");
                JsonObject fields = json.getJsonObject(HttpJsonBuilder.PARAM_WEB_MULTIPART_FIELDS);
                Iterator<String> e = fields.keys();
                while (e.hasNext()) {
                    String key = e.next();
                    bodyBuilder.addFormDataPart(key, fields.getString(key));
                }
            }
            if (json.has(HttpJsonBuilder.PARAM_WEB_MULTIPART_FILES)) {
                Log.v(TAG, "PARAM_WEB_MULTIPART_FILES");
                JsonObject files = json.getJsonObject(HttpJsonBuilder.PARAM_WEB_MULTIPART_FILES);
                Iterator<String> e = files.keys();
                while (e.hasNext()) {
                    String key = e.next();
                    JsonObject fo = files.getJsonObject(key);

                    if (fo.has("soid")) {
                        String filename = fo.getString("filename");
                        long soId = fo.getLong("soid");
                        String contentType = fo.getString("contentType");
                        StoredObject so = StoredObject.get(context, soId);

                        File sourceFile = so.getFile();
                        Log.v(TAG, sourceFile.toString() + ":" + sourceFile.length());
                        if (so.isFile()) {
                            bodyBuilder.addFormDataPart(key, filename,
                                    RequestBody.create(MediaType.parse(
                                            misc.guessContentTypeFromName(filename)), sourceFile));
                        } else {
                            bodyBuilder.addFormDataPart(key, filename,
                                    RequestBody.create(MediaType.parse(contentType), so.getData()));
                        }
                    } else {
                        String uri = fo.getString("uri");
                        String filename = fo.getString("filename");
                        String contentType = fo.getString("contentType");

                        bodyBuilder.addFormDataPart(key, filename,
                                InputStreamRequestBody.create(MediaType.parse(contentType),
                                        context.getContentResolver().openInputStream(Uri.parse(uri))));
                    }
                }
            }
            requestBuilder.method(method, bodyBuilder.build());

        } else if (json.has(HttpJsonBuilder.PARAM_WEB_BODY_SOID)) {
            long soid = json.getLong(HttpJsonBuilder.PARAM_WEB_BODY_SOID);
            StoredObject so = StoredObject.get(context, soid);
            RequestBody body = RequestBody.create(MediaType.parse(json.getString(HttpJsonBuilder.HEADER_CONTENT_TYPE)), so.getFile());
            requestBuilder.method(method, body);

        } else if (json.has(HttpJsonBuilder.PARAM_WEB_BODY)) {
            RequestBody body = RequestBody.create(MediaType.parse(json.getString(HttpJsonBuilder.HEADER_CONTENT_TYPE)), json.getString(HttpJsonBuilder.PARAM_WEB_BODY).getBytes());
            requestBuilder.method(method, body);
        }

        return requestBuilder.build();
    }

    private static void checkUrlOptions(String options) throws ParseException {
        if (options == null || options.equals("")) {
            return;
        } else if (options.startsWith("?")) { // if options already specified
            return;
        }
        throw new ParseException("Options must be nothing, or start with '?'. Got: " + options, 0);
    }
}

