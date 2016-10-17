package com.fieldnation.fnhttp;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.ContextProvider;
import com.fieldnation.fntools.Stopwatch;
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

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class FnHttp {
    private static final String TAG = "FnHttp";

    static String TEMP_FOLDER;
    static EventListener EVENT;

    static {
        // try to set a smart default
        try {
            File externalPath = Environment.getExternalStorageDirectory();
            String packageName = ContextProvider.get().getPackageName();
            File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName);
            temppath.mkdirs();
            File tempFolder = new File(temppath.getAbsolutePath() + "/temp");
            if (!tempFolder.exists()) tempFolder.mkdirs();
            TEMP_FOLDER = tempFolder.getAbsolutePath();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void setTempFolder(String tempFolder) {
        TEMP_FOLDER = tempFolder;
    }

    public static void setEventListener(EventListener listener) {
        EVENT = listener;
    }


    public static Request run(Context context, JsonObject json) throws Exception {
        String path = "";
        String timingKey = null;
        Stopwatch watch = new Stopwatch(true);
        try {
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
            if (json.has(HttpJsonBuilder.PARAM_TIMING_KEY)) {
                timingKey = json.getString(HttpJsonBuilder.PARAM_TIMING_KEY);
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

            OkHttpClient client = new OkHttpClient();

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

                            bodyBuilder.addFormDataPart(key,filename,RequestBody.create(MediaType.parse(contentType),))
                            util.addFilePart(key, filename, Uri.parse(uri), contentType);
                        }
                    }
                    util.finish();
                }

            } else if (json.has(HttpJsonBuilder.PARAM_WEB_BODY_SOID)) {
                long soid = json.getLong(HttpJsonBuilder.PARAM_WEB_BODY_SOID);
                StoredObject so = StoredObject.get(context, soid);
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                if (so.isFile()) {
                    StreamUtils.copyStream(new FileInputStream(so.getFile()), out, (int) so.getFile().length(), 100);
                } else {
                    out.write(so.getData());
                }
                out.flush();
                out.close();
            } else if (json.has(HttpJsonBuilder.PARAM_WEB_BODY)) {
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                out.write(json.getString(HttpJsonBuilder.PARAM_WEB_BODY).getBytes());
                out.flush();
                out.close();
            }

            try {
                return new HttpResult(conn);
            } finally {
                conn.disconnect();
            }
        } finally {
            if (timingKey != null) {
                if (EVENT != null)
                    EVENT.timing(timingKey, watch.finish());
                Log.v(TAG, timingKey + " run time: " + watch.getTime());
            } else {
                Log.v(TAG, "run time: " + watch.getTime());
            }
        }
    }

    public static HttpResult run(Context context, JsonObject request) throws Exception {
        String path = "";
        String timingKey = null;
        Stopwatch watch = new Stopwatch(true);
        try {
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
            if (request.has(HttpJsonBuilder.PARAM_TIMING_KEY)) {
                timingKey = request.getString(HttpJsonBuilder.PARAM_TIMING_KEY);
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
                                    util.addFilePart(key, filename, fin, (int) sourceFile.length());
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

                            util.addFilePart(key, filename, Uri.parse(uri), contentType);
                        }
                    }
                    util.finish();
                }

            } else if (request.has(HttpJsonBuilder.PARAM_WEB_BODY_SOID)) {
                long soid = request.getLong(HttpJsonBuilder.PARAM_WEB_BODY_SOID);
                StoredObject so = StoredObject.get(context, soid);
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                if (so.isFile()) {
                    StreamUtils.copyStream(new FileInputStream(so.getFile()), out, (int) so.getFile().length(), 100);
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
        } finally {
            if (timingKey != null) {
                if (EVENT != null)
                    EVENT.timing(timingKey, watch.finish());
                Log.v(TAG, timingKey + " run time: " + watch.getTime());
            } else {
                Log.v(TAG, "run time: " + watch.getTime());
            }
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

