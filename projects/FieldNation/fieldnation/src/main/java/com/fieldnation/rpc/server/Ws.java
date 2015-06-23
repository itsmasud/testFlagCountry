package com.fieldnation.rpc.server;


import com.fieldnation.BuildConfig;
import com.fieldnation.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Provides a simple Http wrapper
 *
 * @author michael.carver
 */
public class Ws {
    private static final String TAG = "rpc.server.Ws";

    /**
     * Set to true to enable HTTPS, set to false to disable HTTPS. Default =
     * True
     */
    public static boolean USE_HTTPS = BuildConfig.USE_HTTPS;
    /**
     * Set to true to enable debug mode, set to false to disable it.
     */
    public static final boolean DEBUG = true;

    private OAuth _accessToken = null;

    public Ws(OAuth oAuth) {
        _accessToken = oAuth;
    }

    protected Ws() {
    }

    public Result httpRead(String method, String path, String options, String contentType) throws MalformedURLException, IOException, ParseException {
        if (!path.startsWith("/"))
            path = "/" + path;

        if (_accessToken != null)
            options = _accessToken.applyToUrlOptions(options);

        HttpURLConnection conn = null;
        if (USE_HTTPS) {
            // only disabled if debugging
            if (DEBUG)
                trustAllHosts();

            conn = (HttpURLConnection) new URL("https://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "https://" + _accessToken.getHostname() + path + options);

            if (DEBUG)
                ((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);

        } else {
            conn = (HttpURLConnection) new URL("http://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "http://" + _accessToken.getHostname() + path + options);
        }

        conn.setRequestMethod(method);
        if (contentType != null)
            conn.setRequestProperty("ContentType", contentType);

        conn.setDoInput(true);

        try {
            return new Result(conn);
        } finally {
            conn.disconnect();
        }
    }

    public Result httpGet(String path) throws MalformedURLException, IOException, ParseException {
        return httpGet(path, null, null);
    }

    public Result httpGet(String path, String options, String contentType) throws MalformedURLException, IOException, ParseException {
        return httpRead("GET", path, options, contentType);
    }

    public Result httpDelete(String path, String options, String contentType) throws MalformedURLException, IOException, ParseException {
        return httpRead("DELETE", path, options, contentType);
    }

    public Result httpReadWrite(String method, String path, String options, byte[] data, String contentType) throws MalformedURLException, IOException, ParseException {

        if (!path.startsWith("/"))
            path = "/" + path;

        if (_accessToken != null)
            options = _accessToken.applyToUrlOptions(options);

        HttpURLConnection conn = null;
        if (USE_HTTPS) {
            // only disabled if debugging
            if (DEBUG)
                trustAllHosts();

            conn = (HttpURLConnection) new URL("https://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "https://" + _accessToken.getHostname() + path + options);

            if (DEBUG)
                ((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);

        } else {
            conn = (HttpURLConnection) new URL("http://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "http://" + _accessToken.getHostname() + path + options);
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
            return new Result(conn);
        } finally {
            conn.disconnect();
        }
    }

    public Result httpWrite(String method, String path, String options, byte[] data, String contentType) throws MalformedURLException, IOException, ParseException {

        if (!path.startsWith("/"))
            path = "/" + path;

        if (_accessToken != null)
            options = _accessToken.applyToUrlOptions(options);

        HttpURLConnection conn = null;
        if (USE_HTTPS) {
            // only disabled if debugging
            if (DEBUG)
                trustAllHosts();

            conn = (HttpURLConnection) new URL("https://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "https://" + _accessToken.getHostname() + path + options);

            if (DEBUG)
                ((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);

        } else {
            conn = (HttpURLConnection) new URL("http://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "http://" + _accessToken.getHostname() + path + options);
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
            return new Result(conn);
        } finally {
            conn.disconnect();
        }
    }

    public Result httpPost(String path, String options, String data, String contentType) throws MalformedURLException, IOException, ParseException {
        return httpPost(path, options, data.getBytes(), contentType);
    }

    public Result httpPost(String path, String options, byte[] data, String contentType) throws MalformedURLException, IOException, ParseException {
        return httpReadWrite("POST", path, options, data, contentType);
    }

    public Result httpPut(String path, String options, byte[] data, String contentType) throws MalformedURLException, IOException, ParseException {
        return httpReadWrite("PUT", path, options, data, contentType);
    }

    public Result httpPostFile(String path, String options, String fieldName, String filename, InputStream inputStream,
                               int length, Map<String, String> map) throws ParseException, MalformedURLException, IOException {
        if (!path.startsWith("/"))
            path = "/" + path;

        if (_accessToken != null)
            options = _accessToken.applyToUrlOptions(options);

        HttpURLConnection conn = null;
        if (USE_HTTPS) {
            // only disabled if debugging
            if (DEBUG)
                trustAllHosts();

            conn = (HttpURLConnection) new URL("https://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "https://" + _accessToken.getHostname() + path + options);

            if (DEBUG)
                ((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);

        } else {
            conn = (HttpURLConnection) new URL("http://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "http://" + _accessToken.getHostname() + path + options);
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
            return new Result(util.finish());
        } finally {
            conn.disconnect();
        }
    }

    public Result httpPostFile(String path, String options, String fieldName, String filename, byte[] filedata,
                               Map<String, String> map, String contentType) throws ParseException, MalformedURLException, IOException {
        if (!path.startsWith("/"))
            path = "/" + path;

        if (_accessToken != null)
            options = _accessToken.applyToUrlOptions(options);

        HttpURLConnection conn = null;
        if (USE_HTTPS) {
            // only disabled if debugging
            if (DEBUG)
                trustAllHosts();

            conn = (HttpURLConnection) new URL("https://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "https://" + _accessToken.getHostname() + path + options);

            if (DEBUG)
                ((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);

        } else {
            conn = (HttpURLConnection) new URL("http://" + _accessToken.getHostname() + path + options).openConnection();
            Log.v(TAG, "http://" + _accessToken.getHostname() + path + options);
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
            return new Result(util.finish());
        } finally {
            conn.disconnect();
        }
    }

    // always verify the host - don't check for certificate
    protected final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            if (DEBUG)
                return true;
            else
                // disable all if not debugging
                // (this class shouldn't be used anyway)
                return false;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    protected static void trustAllHosts() {
        // disable this if not debugging
        if (!DEBUG)
            return;

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
