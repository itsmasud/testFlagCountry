package com.fieldnation.rpc.server;

import android.content.Context;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.utils.misc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Enumeration;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class HttpJson {
    private static final String TAG = "rpc.server.HttpJson";

    /**
     * Set to true to enable HTTPS, set to false to disable HTTPS. Default =
     * True
     */
    public static boolean USE_HTTPS = true;

    public static HttpResult run(Context context, JsonObject request) throws ParseException, IOException {
        String path = request.getString(HttpJsonBuilder.PARAM_WEB_PATH);
        String params = request.getString(HttpJsonBuilder.PARAM_WEB_URL_PARAMS);
        String hostname = request.getString(HttpJsonBuilder.PARAM_WEB_HOST);
        String method = request.getString(HttpJsonBuilder.PARAM_WEB_METHOD);
        JsonObject headers = null;
        if (request.has(HttpJsonBuilder.PARAM_WEB_HEADERS)) {
            headers = request.getJsonObject(HttpJsonBuilder.PARAM_WEB_HEADERS);
        }

        if (!path.startsWith("/"))
            path = "/" + path;

        if (params == null)
            params = "";

        checkUrlOptions(params);

        HttpURLConnection conn = null;
        if (USE_HTTPS) {
            conn = (HttpURLConnection) new URL("https://" + hostname + path + params).openConnection();
            Log.v(TAG, "https://" + hostname + path + params);

        } else {
            conn = (HttpURLConnection) new URL("http://" + hostname + path + params).openConnection();
            Log.v(TAG, "http://" + hostname + path + params);
        }

        conn.setRequestMethod(method);

        if (headers != null) {
            Enumeration<String> e = headers.keys();
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                conn.setRequestProperty(key, headers.getString(key));
            }
        }

        conn.setDoInput(true);

        if (request.has("multipart")) {
            MultipartUtility util = new MultipartUtility(conn, "UTF-8");
            if (request.has(HttpJsonBuilder.PARAM_WEB_MULTIPART_FIELDS)) {
                JsonObject fields = request.getJsonObject(HttpJsonBuilder.PARAM_WEB_MULTIPART_FIELDS);
                Enumeration<String> e = fields.keys();
                while (e.hasMoreElements()) {
                    String key = e.nextElement();
                    util.addFormField(key, fields.getString(key));
                }
            }

            if (request.has(HttpJsonBuilder.PARAM_WEB_MULTIPART_FILES)) {
                JsonObject files = request.getJsonObject(HttpJsonBuilder.PARAM_WEB_MULTIPART_FILES);
                Enumeration<String> e = files.keys();
                while (e.hasMoreElements()) {
                    String key = e.nextElement();
                    JsonObject fo = files.getJsonObject(key);
                    String filename = fo.getString("filename");
                    long soId = fo.getLong("soid");
                    String contentType = fo.getString("contentType");
                    StoredObject so = StoredObject.get(context, soId);

                    if (so.isFile()) {
                        util.addFilePart(key, filename, new FileInputStream(so.getFile()), (int) so.getFile().length());
                    } else {
                        util.addFilePart(key, filename, so.getData(), contentType);
                    }
                }
            }

        } else if (request.has(HttpJsonBuilder.PARAM_WEB_BODY_SOID)) {
            long soid = request.getLong(HttpJsonBuilder.PARAM_WEB_BODY_SOID);
            StoredObject so = StoredObject.get(context, soid);
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

