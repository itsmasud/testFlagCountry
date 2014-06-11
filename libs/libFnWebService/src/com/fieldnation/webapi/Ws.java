package com.fieldnation.webapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpConnection;

import android.net.Uri;
import android.os.Debug;

public class Ws {
	public static boolean USE_HTTPS = true;

	private AccessToken _accessToken = null;

	public Ws(AccessToken accessToken) {
		_accessToken = accessToken;
	}

	protected Ws() {
	}

	public Result httpRead(String method, String path, String options,
			String contentType) throws MalformedURLException, IOException, ParseException {
		if (!path.startsWith("/"))
			path = "/" + path;

		if (_accessToken != null)
			options = _accessToken.addToken(options);

		HttpURLConnection conn = null;
		if (USE_HTTPS) {
			// only disabled if debugging
			if (Debug.isDebuggerConnected())
				trustAllHosts();

			conn = (HttpURLConnection) new URL("https://" + _accessToken.getHostname() + path + options).openConnection();

			if (Debug.isDebuggerConnected())
				((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);

		} else {
			conn = (HttpURLConnection) new URL("http://" + _accessToken.getHostname() + path + options).openConnection();
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

	public Result httpWrite(String method, String path, String options,
			byte[] data, String contentType) throws MalformedURLException, IOException, ParseException {

		if (!path.startsWith("/"))
			path = "/" + path;

		if (_accessToken != null)
			options = _accessToken.addToken(options);

		HttpURLConnection conn = null;
		if (USE_HTTPS) {
			// only enabled if debugging
			if (Debug.isDebuggerConnected())
				trustAllHosts();

			conn = (HttpURLConnection) new URL("https://" + _accessToken.getHostname() + path + options).openConnection();

			if (Debug.isDebuggerConnected())
				((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);
		} else {
			conn = (HttpURLConnection) new URL("http://" + _accessToken.getHostname() + path + options).openConnection();
		}

		conn.setRequestMethod(method);
		conn.setRequestProperty("ContentType", contentType);

		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setReadTimeout(10000);

		OutputStream out = conn.getOutputStream();
		out.write(data);
		out.flush();
		out.close();

		try {
			return new Result(conn);
		} finally {
			conn.disconnect();
		}
	}

	public Result httpPost(String path, String options, String data,
			String contentType) throws MalformedURLException, IOException, ParseException {
		return httpPost(path, options, data.getBytes(), contentType);
	}

	public Result httpPost(String path, String options, byte[] data,
			String contentType) throws MalformedURLException, IOException, ParseException {
		return httpWrite("POST", path, options, data, contentType);
	}

	public Result httpPut(String path, String options, byte[] data,
			String contentType) throws MalformedURLException, IOException, ParseException {
		return httpWrite("PUT", path, options, data, contentType);
	}

	// always verify the host - don't check for certificate
	protected final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			if (Debug.isDebuggerConnected())
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
		if (!Debug.isDebuggerConnected())
			return;

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}
		} };

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
