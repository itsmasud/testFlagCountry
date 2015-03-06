package com.fieldnation.rpc.server;

import com.fieldnation.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class HttpW {
    private static final String TAG = "rpc.server.HttpW";

    private HttpURLConnection conn;
    private MultipartUtility multi;

    public HttpW(boolean useHttps, String hostname, String path, String urlParams) throws ParseException, IOException {
        if (!path.startsWith("/"))
            path = "/" + path;

        if (urlParams == null)
            urlParams = "";

        checkUrlOptions(urlParams);

        if (useHttps) {
            conn = (HttpURLConnection) new URL("https://" + hostname + path + urlParams).openConnection();
            Log.v(TAG, "https://" + hostname + path + urlParams);
        } else {
            conn = (HttpURLConnection) new URL("http://" + hostname + path + urlParams).openConnection();
            Log.v(TAG, "http://" + hostname + path + urlParams);
        }
        conn.setReadTimeout(10000);
    }

    public HttpW header(String key, String value) {
        conn.setRequestProperty(key, value);
        return this;
    }

    public HttpW method(String method) throws ProtocolException {
        conn.setRequestMethod(method);
        return this;
    }

    public HttpW body(byte[] body) throws IOException {
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        out.write(body);
        out.flush();
        out.close();
        return this;
    }

    private MultipartUtility getMulti() throws IOException {
        if (multi == null) {
            multi = new MultipartUtility(conn, "UTF-8");
        }
        return multi;
    }


    public HttpW formField(String key, String value) throws IOException {
        getMulti();
        multi.addFormField(key, value);
        return this;
    }

    public HttpW file(String fieldName, String filename, File file) throws IOException {
        getMulti();

        multi.addFilePart(fieldName, filename, new FileInputStream(file), (int) file.length());
        return this;
    }

    public HttpW file(String fieldName, String filename, byte[] data, String contentType) throws IOException {
        getMulti();

        multi.addFilePart(fieldName, filename, data, contentType);
        return this;
    }

    public HttpResult execute() throws IOException {
        if (multi != null) {
            multi.finish();
        }
        return new HttpResult(conn);
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
