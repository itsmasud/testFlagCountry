package com.fieldnation.rpc.server;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fntools.StreamUtils;
import com.fieldnation.fntools.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;

/**
 * Represents an HTTP response with some extra parsing.
 *
 * @author michael.carver
 */
public class HttpResult {
    private static final String TAG = "HttpResult";

    private byte[] _baResults = null;
    private String _sResults = null;
    private JsonObject _jsonResults = null;
    private JsonArray _jaResults = null;
    private File _file = null;

    private int _responseCode;
    private String _responseMessage;

    public HttpResult(HttpURLConnection conn) throws IOException {
        _responseCode = conn.getResponseCode();
        _responseMessage = conn.getResponseMessage();
        cacheResults(conn);
    }

    public int getResponseCode() {
        return _responseCode;
    }

    public String getResponseMessage() {
        return _responseMessage;
    }

    private void storeData(InputStream in) throws IOException {
        _baResults = StreamUtils.readAllFromStreamUntil(in, -1, 102400, 1000);
        if (_baResults != null && _baResults.length >= 102400) {
            // temp file
            File tempFolder = new File(App.get().getTempFolder());
            File tempFile = File.createTempFile("web", "dat", tempFolder);
            FileOutputStream fout = new FileOutputStream(tempFile, false);
            fout.write(_baResults);
            StreamUtils.copyStream(in, fout, -1, 1000);
            fout.close();
            _baResults = null;

            _file = tempFile;
        }
    }

    private void cacheResults(HttpURLConnection conn) {
        try {
            if (conn.getDoInput()) {

                InputStream in = conn.getInputStream();
                try {
                    storeData(in);
                } finally {
                    in.close();
                }

                if (_baResults != null) {
                    Log.v("HttpJson", "data size " + misc.readableFileSize(conn.getContentLength()) + ", " + misc.readableFileSize(_baResults.length));
                }
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _baResults = null;
            try {
                InputStream in = conn.getErrorStream();
                try {
                    storeData(in);
                } finally {
                    in.close();
                }
            } catch (Exception ex1) {
                Log.v(TAG, ex1);
            }
        }
    }

    public boolean isFile() {
        return _file != null;
    }

    public File getFile() {
        return _file;
    }

    public byte[] getByteArray() {
        if (_file != null && _baResults == null) {
            try {
                FileInputStream fin = null;
                try {
                    fin = new FileInputStream(_file);
                    _baResults = StreamUtils.readAllFromStream(fin, -1, 1000);
                } finally {
                    if (fin != null) {
                        fin.close();
                    }
                }
            } catch (IOException e) {
            }
        }
        return _baResults;
    }

    public String getString() {
        if (_sResults == null) {
            _sResults = new String(getByteArray());
        }
        return _sResults;
    }

    public JsonObject getJsonObject() throws ParseException {
        if (_jsonResults == null) {
            _jsonResults = new JsonObject(getByteArray());
        }
        return _jsonResults;
    }

    public JsonArray getResultsAsJsonArray() throws ParseException {
        if (_jaResults == null) {
            _jaResults = new JsonArray(getByteArray());
        }
        return _jaResults;
    }
}
