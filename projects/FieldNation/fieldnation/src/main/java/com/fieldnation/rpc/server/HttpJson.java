package com.fieldnation.rpc.server;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.BuildConfig;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.StreamUtils;
import com.fieldnation.fntools.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

    public interface ProgressListener {
        void progress(long pos, long size, long time);
    }

    public static HttpResult run(Context context, JsonObject request) throws Exception {
        return run(context, request, null);
    }

    public static HttpResult run(Context context, JsonObject request, final ProgressListener progress) throws Exception {
        String path = "";
        String protocol = "";
        String params = "";
        String hostname = "";
        String method = "";
        String url = null;

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

        url = protocol + hostname + path + params;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        Log.v(TAG, url);

            conn.setRequestMethod(method);
            conn.setUseCaches(false);
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("X-App-Version", BuildConfig.VERSION_NAME);
            conn.setRequestProperty("X-App-Platform", "Android");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(60000);

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
                        StoredObject so = StoredObject.get(context, soId);

                        File sourceFile = so.getFile();
                        Log.v(TAG, sourceFile.toString() + ":" + sourceFile.length());
                        if (so.isFile()) {
                            InputStream fin = new FileInputStream(sourceFile);
                            try {
                                util.addFilePart(key, filename, fin, (int) sourceFile.length(), progress);
                            } finally {
                                fin.close();
                            }
                        } else {
                            util.addFilePart(key, filename, so.getData(), contentType);
                        }
                    } else {
                        String uri = fo.getString("uri");
                        String filename = fo.getString("filename");
                        String contentType = fo.getString("contentType");

                        util.addFilePart(key, filename, Uri.parse(uri), contentType, progress);
                    }
                }
                util.finish();
            }

        } else if (request.has(HttpJsonBuilder.PARAM_WEB_BODY_SOID)) {
            long soid = request.getLong(HttpJsonBuilder.PARAM_WEB_BODY_SOID);
            final StoredObject so = StoredObject.get(context, soid);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            if (so.isFile()) {
                if (progress != null) {
                    final long startTime = System.currentTimeMillis();
                    StreamUtils.copyStream(new FileInputStream(so.getFile()), out, (int) so.getFile().length(), 100, new StreamUtils.ProgressListener() {
                        @Override
                        public void progress(int position) {
                            progress.progress(position, so.getFile().length(), System.currentTimeMillis() - startTime);
                        }
                    });
                } else {
                    StreamUtils.copyStream(new FileInputStream(so.getFile()), out, (int) so.getFile().length(), 100, null);
                }
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

