package com.fieldnation.j2j;

import java.io.IOException;
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

/**
 * Provides a simple Http wrapper
 * 
 * @author michael.carver
 * 
 */
public class Ws {

	/**
	 * Set to true to enable HTTPS, set to false to disable HTTPS. Default =
	 * True
	 */
	public static boolean USE_HTTPS = true;
	/**
	 * Set to true to enable debug mode, set to false to disable it.
	 */
	public static final boolean DEBUG = true;

	public static Result httpRead(String method, String hostname, String path,
			String options, String contentType) throws MalformedURLException, IOException, ParseException {
		if (!path.startsWith("/"))
			path = "/" + path;

		HttpURLConnection conn = null;
		if (USE_HTTPS) {
			// only disabled if debugging
			if (DEBUG)
				trustAllHosts();

			conn = (HttpURLConnection) new URL(
					"https://" + hostname + path + options).openConnection();

			if (DEBUG)
				((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);

		} else {
			conn = (HttpURLConnection) new URL(
					"http://" + hostname + path + options).openConnection();
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

	public static Result httpGet(String hostname, String path) throws MalformedURLException, IOException, ParseException {
		return httpGet(hostname, path, "", null);
	}

	public static Result httpGet(String hostname, String path, String options,
			String contentType) throws MalformedURLException, IOException, ParseException {
		return httpRead("GET", hostname, path, options, contentType);
	}

	public static Result httpDelete(String hostname, String path,
			String options, String contentType) throws MalformedURLException, IOException, ParseException {
		return httpRead("DELETE", hostname, path, options, contentType);
	}

	public static Result httpWrite(String method, String hostname, String path,
			String options, byte[] data, String contentType) throws MalformedURLException, IOException, ParseException {

		if (!path.startsWith("/"))
			path = "/" + path;

		HttpURLConnection conn = null;
		if (USE_HTTPS) {
			// only enabled if debugging
			if (DEBUG)
				trustAllHosts();

			conn = (HttpURLConnection) new URL(
					"https://" + hostname + path + options).openConnection();

			if (DEBUG)
				((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);
		} else {
			conn = (HttpURLConnection) new URL(
					"http://" + hostname + path + options).openConnection();
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

	public static Result httpPost(String hostname, String path, String options,
			String data, String contentType) throws MalformedURLException, IOException, ParseException {
		return httpPost(hostname, path, options, data.getBytes(), contentType);
	}

	public static Result httpPost(String hostname, String path, String options,
			byte[] data, String contentType) throws MalformedURLException, IOException, ParseException {
		return httpWrite("POST", hostname, path, options, data, contentType);
	}

	public static Result httpPut(String hostname, String path, String options,
			byte[] data, String contentType) throws MalformedURLException, IOException, ParseException {
		return httpWrite("PUT", hostname, path, options, data, contentType);
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
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
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
