package com.fieldnation.rpc.server;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.utils.misc;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Iterator;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class HttpJson {
    private static final String TAG = "HttpJson";

    public static HttpResult run(Context context, JsonObject request) throws Exception {
        String protocol = "";
        String path = "";
        String params = "";
        String hostname = "";
        String method = "";

        if (request.has(HttpJsonBuilder.PARAM_WEB_PROTOCOL)) {
            protocol = request.getString(HttpJsonBuilder.PARAM_WEB_PROTOCOL);
        }
        if (request.has(HttpJsonBuilder.PARAM_WEB_PATH)) {
            path = request.getString(HttpJsonBuilder.PARAM_WEB_PATH);
        }
        if (request.has(HttpJsonBuilder.PARAM_WEB_URL_PARAMS)) {
            params = request.getString(HttpJsonBuilder.PARAM_WEB_URL_PARAMS);
        }
        if (request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
            hostname = request.getString(HttpJsonBuilder.PARAM_WEB_HOST);
        }
        if (request.has(HttpJsonBuilder.PARAM_WEB_METHOD)) {
            method = request.getString(HttpJsonBuilder.PARAM_WEB_METHOD);
        }

        JsonObject headers = null;
        if (request.has(HttpJsonBuilder.PARAM_WEB_HEADERS)) {
            headers = request.getJsonObject(HttpJsonBuilder.PARAM_WEB_HEADERS);
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

        String url = protocol + hostname + path + params;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        Log.v(TAG, url);

        conn.setRequestMethod(method);

        if (headers != null) {
            Iterator<String> e = headers.keys();
            while (e.hasNext()) {
                String key = e.next();
                conn.setRequestProperty(key, headers.getString(key));
            }
        }

        if (request.has(HttpJsonBuilder.PARAM_DO_NOT_READ)) {
            conn.setDoInput(false);
        } else {
            conn.setDoInput(true);
        }

        if (request.has(HttpJsonBuilder.PARAM_WEB_MULTIPART)) {
            Log.v(TAG, "PARAM_WEB_MULTIPART");
            MultipartUtility util = new MultipartUtility(conn, "UTF-8");
            if (request.has(HttpJsonBuilder.PARAM_WEB_MULTIPART_FIELDS)) {
                Log.v(TAG, "PARAM_WEB_MULTIPART_FIELDS");
                JsonObject fields = request.getJsonObject(HttpJsonBuilder.PARAM_WEB_MULTIPART_FIELDS);
                Iterator<String> e = fields.keys();
                while (e.hasNext()) {
                    String key = e.next();
                    util.addFormField(key, fields.getString(key));
                }
            }
            if (request.has(HttpJsonBuilder.PARAM_WEB_MULTIPART_FILES)) {
                Log.v(TAG, "PARAM_WEB_MULTIPART_FILES");
                JsonObject files = request.getJsonObject(HttpJsonBuilder.PARAM_WEB_MULTIPART_FILES);
                Iterator<String> e = files.keys();
                while (e.hasNext()) {
                    String key = e.next();
                    JsonObject fo = files.getJsonObject(key);

                    if (fo.has("soid")) {
                        String filename = fo.getString("filename");
                        long soId = fo.getLong("soid");
                        String contentType = fo.getString("contentType");
                        StoredObject so = StoredObject.get(soId);

                        Log.v(TAG, so.getFile().toString() + ":" + so.getFile().length());
                        if (so.isFile()) {
                            util.addFilePart(key, filename, new FileInputStream(so.getFile()), (int) so.getFile().length());
                        } else {
                            util.addFilePart(key, filename, so.getData(), contentType);
                        }
                    } else {
                        String uri = fo.getString("uri");
                        String filename = fo.getString("filename");
                        String contentType = fo.getString("contentType");

                        util.addFilePart(key, filename, Uri.parse(uri), contentType);
                    }
                }
                util.finish();
            }

        } else if (request.has(HttpJsonBuilder.PARAM_WEB_BODY_SOID)) {
            long soid = request.getLong(HttpJsonBuilder.PARAM_WEB_BODY_SOID);
            StoredObject so = StoredObject.get(soid);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            if (so.isFile()) {
                misc.copyStream(new FileInputStream(so.getFile()), out, 1024, (int) so.getFile().length(), 100);
            } else {
                out.write(so.getData());
            }
            out.flush();
            out.close();
        } else if (request.has(HttpJsonBuilder.PARAM_WEB_BODY)) {
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(request.getString(HttpJsonBuilder.PARAM_WEB_BODY).getBytes());
            out.flush();
            out.close();
        }

        try {
            return new HttpResult(conn);
        } finally {
            conn.disconnect();
        }

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

