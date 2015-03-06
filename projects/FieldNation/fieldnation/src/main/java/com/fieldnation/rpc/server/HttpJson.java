package com.fieldnation.rpc.server;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class HttpJson {
    private static final String TAG = "rpc.server.HttpJson";

    public static final String PARAM_METHOD = "method";
    public static final String PARAM_HOSTNAME = "hostname";
    public static final String PARAM_PATH = "path";
    public static final String PARAM_URL_PARAMS = "urlParams";
    public static final String PARAM_HEADERS = "headers";

    public static final String PARAM_RESPONSE_CODE = "responseCode";
    public static final String PARAM_RESPONSE_MESSAGE = "responseMessage";

    /**
     * Set to true to enable HTTPS, set to false to disable HTTPS. Default =
     * True
     */
    public static boolean USE_HTTPS = true;

    public static HttpResult run(JsonObject query) throws ParseException, IOException {
        String path = query.getString(PARAM_PATH);
        String options = query.getString(PARAM_URL_PARAMS);
        String hostname = query.getString(PARAM_HOSTNAME);
        String method = query.getString(PARAM_METHOD);
        JsonObject headers = null;
        if (query.has(PARAM_HEADERS)) {
            headers = query.getJsonObject(PARAM_HEADERS);
        }

        if (!path.startsWith("/"))
            path = "/" + path;

        if (options == null)
            options = "";

        checkUrlOptions(options);

        HttpURLConnection conn = null;
        if (USE_HTTPS) {
            conn = (HttpURLConnection) new URL("https://" + hostname + path + options).openConnection();
            Log.v(TAG, "https://" + hostname + path + options);

        } else {
            conn = (HttpURLConnection) new URL("http://" + hostname + path + options).openConnection();
            Log.v(TAG, "http://" + hostname + path + options);
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

        try {
            return new HttpResult(conn);
        } finally {
            conn.disconnect();
        }

    }

    public static HttpResult run(JsonObject query, StoredObject body) {
        if (body.isFile())
            return run(query, body.getFile());
        else
            return run(query, body.getData());
    }

    public static HttpResult run(JsonObject query, File file) throws ParseException, IOException {
        String path = query.getString(PARAM_PATH);
        String options = query.getString(PARAM_URL_PARAMS);
        String hostname = query.getString(PARAM_HOSTNAME);
        String method = query.getString(PARAM_METHOD);
        JsonObject headers = null;
        if (query.has(PARAM_HEADERS)) {
            headers = query.getJsonObject(PARAM_HEADERS);
        }

        if (!path.startsWith("/"))
            path = "/" + path;

        if (options == null)
            options = "";

        checkUrlOptions(options);

        HttpURLConnection conn = null;
        if (USE_HTTPS) {
            conn = (HttpURLConnection) new URL("https://" + hostname + path + options).openConnection();
            Log.v(TAG, "https://" + hostname + path + options);
        } else {
            conn = (HttpURLConnection) new URL("http://" + hostname + path + options).openConnection();
            Log.v(TAG, "http://" + hostname + path + options);
        }
        conn.setRequestMethod(method);
        conn.setReadTimeout(10000);

        MultipartUtility util = new MultipartUtility(conn, "UTF-8");
        if (map != null) {
            Iterator<String> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                util.addFormField(key, map.get(key));
            }
        }

        util.addFilePart(fieldName, file.getAbsolutePath(), new FileInputStream(file), file.length());

        try {
            return new HttpResult(util.finish());
        } finally {
            conn.disconnect();
        }
    }

    public static HttpResult run(JsonObject query, byte[] body) {
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

