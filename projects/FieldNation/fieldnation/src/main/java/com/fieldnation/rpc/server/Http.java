package com.fieldnation.rpc.server;


import com.fieldnation.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides a simple Http wrapper
 *
 * @author michael.carver
 */
public class Http {
    private static final String TAG = "rpc.server.Http";

    /**
     * Set to true to enable HTTPS, set to false to disable HTTPS. Default =
     * True
     */
    public static boolean USE_HTTPS = true;

    public static HttpResult read(String method, String hostname, String path, String options, String contentType) throws IOException, ParseException {
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
        if (contentType != null)
            conn.setRequestProperty("ContentType", contentType);

        conn.setDoInput(true);

        try {
            return new HttpResult(conn);
        } finally {
            conn.disconnect();
        }
    }

    public static HttpResult get(String hostname, String path) throws IOException, ParseException {
        return get(hostname, path, null, null);
    }

    public static HttpResult get(String hostname, String path, String options, String contentType) throws IOException, ParseException {
        return read("GET", hostname, path, options, contentType);
    }

    public static HttpResult delete(String hostname, String path, String options, String contentType) throws IOException, ParseException {
        return read("DELETE", hostname, path, options, contentType);
    }

    public static HttpResult readWrite(String method, String hostname, String path, String options, byte[] data, String contentType) throws IOException, ParseException {
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
        conn.setRequestProperty("ContentType", contentType);

        conn.setDoInput(true);
        conn.setReadTimeout(10000);

        if (data != null) {
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
            try {
                Log.v(TAG, "Output:" + new String(data));
            } catch (Exception ex) {
                Log.v(TAG, "Can't show data");
            }
        }

        try {
            return new HttpResult(conn);
        } finally {
            conn.disconnect();
        }
    }

    public static HttpResult write(String method, String hostname, String path, String options, byte[] data, String contentType) throws IOException, ParseException {
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
        conn.setRequestProperty("ContentType", contentType);

        conn.setDoInput(false);
        conn.setReadTimeout(10000);

        if (data != null) {
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
            try {
                System.out.println("Output:" + new String(data));
            } catch (Exception ex) {
                System.out.println("Can't show data");
            }
        }

        try {
            conn.connect();
            return new HttpResult(conn);
        } finally {
            conn.disconnect();
        }
    }

    public static HttpResult post(String hostname, String path, String options, String data, String contentType) throws IOException, ParseException {
        return post(hostname, path, options, data.getBytes(), contentType);
    }

    public static HttpResult post(String hostname, String path, String options, byte[] data, String contentType) throws IOException, ParseException {
        return readWrite("POST", hostname, path, options, data, contentType);
    }

    public static HttpResult put(String hostname, String path, String options, byte[] data, String contentType) throws IOException, ParseException {
        return readWrite("PUT", hostname, path, options, data, contentType);
    }

    public static HttpResult postFile(String hostname, String path, String options, String fieldName, String filename, InputStream inputStream,
                                      int length, Map<String, String> map) throws ParseException, IOException {
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
        conn.setReadTimeout(10000);

        MultipartUtility util = new MultipartUtility(conn, "UTF-8");
        if (map != null) {
            Iterator<String> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                util.addFormField(key, map.get(key));
            }
        }

        util.addFilePart(fieldName, filename, inputStream, length);

        try {
            return new HttpResult(util.finish());
        } finally {
            conn.disconnect();
        }
    }

    public static HttpResult postFile(String hostname, String path, String options, String fieldName, String filename, byte[] filedata,
                                      Map<String, String> map, String contentType) throws ParseException, IOException {
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
        conn.setReadTimeout(10000);

        MultipartUtility util = new MultipartUtility(conn, "UTF-8");
        if (map != null) {
            Iterator<String> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                util.addFormField(key, map.get(key));
            }
        }

        util.addFilePart(fieldName, filename, filedata, contentType);

        try {
            return new HttpResult(util.finish());
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
