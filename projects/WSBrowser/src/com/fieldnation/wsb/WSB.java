package com.fieldnation.wsb;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.Base64;
import com.fieldnation.utils.misc;

public class WSB extends WsbUi {
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		trustAllHosts();
		new WSB().setVisible(true);
	}

	public WSB() {
		super();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void goButton_onClick(ActionEvent e) {
		try {
			String method = getHttpMethod();
			String url = getUrl();
			String username = getUsername();
			String password = getPassword();
			String mime = getMIME();
			String payload = getPostData();

			if (username.equals("")) {
				username = null;
			}

			if ("GET".equals(method) || "DELETE".equals(method)) {
				httpRead(method, url, username, password);
			} else if ("POST".equals(method) || "PUT".equals(method)) {
				httpWrite(method, url, payload, mime, username, password);
			}
		} catch (Exception ex) {
			log(ex);
		}

	}

	public void log(Exception e) {
		log(misc.getStackTrace(e));
	}

	public WebResult httpRead(String method, String path, String username,
			String password) {
		URL url = null;
		HttpURLConnection conn = null;
		Exception ex2 = null;
		try {
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();

			if (conn instanceof HttpsURLConnection) {
				((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);
			}

			conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", "Internet Access");

			// configure the authorization
			if (username != null && password != null) {
				conn.setRequestProperty(
						"Authorization",
						"Basic "
								+ Base64.encode((username + ":" + password)
										.getBytes()));
			}

			log("*******************************************");
			log("                Request");
			log("*******************************************");
			log(path);
			dumpConnectionRequest(conn);

			// get the steam from the server
			byte[] rawData;
			int size;
			log("*******************************************");
			log("                Response");
			log("*******************************************");

			try {
				InputStream str = conn.getInputStream();

				size = conn.getContentLength();

				rawData = misc.readAllFromStream(str, 1024, size, 3000);
			} catch (Exception ex) {
				ex2 = ex;
				rawData = misc.readAllFromStream(conn.getErrorStream(), 1024,
						-1, 3000);
				log(ex);
			}

			dumpConnectionResponse(conn);
			String strData = new String(rawData);
			log(strData);

			if (isJ2JChecked()) {
				performJ2J(strData);
			}

			return new WebResult(conn.getResponseCode(), rawData);

		} catch (MalformedURLException e) {
			log(e);
		} catch (IOException e) {
			log(e);
		} finally {
			if (conn != null)
				conn.disconnect();

			if (ex2 != null)
				log(ex2);
		}
		return null;
	}

	public WebResult httpWrite(String method, String path, String payload,
			String contentType, String username, String password) {
		URL url = null;
		HttpURLConnection conn = null;
		Exception ex2 = null;
		try {
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();

			if (conn instanceof HttpsURLConnection) {
				((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);
			}

			conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", "Internet Access");
			conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Content-Length", payload.length() + "");
			conn.setDoOutput(true);

			// configure the authorization
			if (username != null && password != null) {
				conn.setRequestProperty(
						"Authorization",
						"Basic "
								+ Base64.encode((username + ":" + password)
										.getBytes()));
			}

			log("*******************************************");
			log("                Request");
			log("*******************************************");
			log(path);
			dumpConnectionRequest(conn);
			log(payload);

			// write the data
			OutputStream out = conn.getOutputStream();
			out.write(payload.getBytes());
			out.close();

			// get the steam from the server
			byte[] rawData;
			int size;
			log("*******************************************");
			log("                Response");
			log("*******************************************");

			try {
				InputStream str = conn.getInputStream();

				size = conn.getContentLength();

				rawData = misc.readAllFromStream(str, 1024, size, 3000);
			} catch (Exception ex) {
				ex2 = ex;
				rawData = misc.readAllFromStream(conn.getErrorStream(), 1024,
						-1, 3000);
				log(ex);
			}

			dumpConnectionResponse(conn);
			String strData = new String(rawData);
			log(strData);

			if (isJ2JChecked()) {
				performJ2J(strData);
			}

			return new WebResult(conn.getResponseCode(), rawData);

		} catch (MalformedURLException e) {
			log(e);
		} catch (IOException e) {
			log(e);
		} finally {
			if (conn != null)
				conn.disconnect();

			if (ex2 != null)
				log(ex2);
		}

		return null;
	}

	public void performJ2J(String data) {
		try {
			JsonObject json = new JsonObject(data);
			performJ2J(json, "Root");
		} catch (Exception ex) {
			try {
				JsonArray ja = new JsonArray(data);
				performJ2J(ja, "Root");
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}

	private void performJ2J(JsonObject jsonSource, String className)
			throws IOException, ParseException {
		FileOutputStream fout = new FileOutputStream(getJ2JOut() + "/"
				+ className + ".java");

		JavaObject obj = new JavaObject(className, getJ2JPackage());

		Enumeration<String> keys = jsonSource.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object j = jsonSource.get(key);
			JavaField field = null;

			if (j instanceof JsonObject) {
				field = new JavaField(key, formatForClassName(key), false);
				performJ2J((JsonObject) j, formatForClassName(key));
			} else if (j instanceof JsonArray) {
				field = new JavaField(key, formatForClassName(key), true);
				performJ2J((JsonArray) j, formatForClassName(key));
			} else {
				try {
					int par = jsonSource.getInt(key);
					field = new JavaField(key, "int", false);
				} catch (Exception ex) {
					try {
						double par = jsonSource.getDouble(key);
						field = new JavaField(key, "double", false);
					} catch (Exception ex1) {
						try {
							String str = jsonSource.getString(key);
							if ("true".equals(str) || "false".equals(str)) {
								field = new JavaField(key, "boolean", false);
							} else {
								field = new JavaField(key, "String", false);
							}
						} catch (Exception ex2) {
							field = new JavaField(key, "String", false);
						}
					}
				}
			}
			obj._fields.add(field);
		}

		fout.write(obj.toString().getBytes());
		fout.close();

	}

	private String formatForClassName(String value) {
		String[] splitted = value.split("_");

		String result = "";

		for (int i = 0; i < splitted.length; i++) {
			result += splitted[i].substring(0, 1).toUpperCase()
					+ splitted[i].substring(1);
		}

		return result;
	}

	private void performJ2J(JsonArray jsonSource, String className)
			throws ParseException, IOException {
		JsonObject merged = mergeArray(jsonSource);
		performJ2J(merged, className);
	}

	private JsonObject mergeArray(JsonArray jsonSource) throws ParseException {
		JsonObject output = new JsonObject();

		for (int i = 0; i < jsonSource.size(); i++) {
			JsonObject item = jsonSource.getJsonObject(i);
			output.deepmerge(item);
		}

		return output;
	}

	public void dumpConnectionRequest(HttpURLConnection conn) {
		Map<String, List<String>> map = conn.getRequestProperties();

		Iterator<String> itr = map.keySet().iterator();

		while (itr.hasNext()) {
			String key = itr.next();
			List<String> list = map.get(key);

			String header = key + ": ";
			for (int i = 0; i < list.size(); i++) {
				header += list.get(i) + " ";
			}

			log(header.trim());
		}
	}

	public void dumpConnectionResponse(HttpURLConnection conn)
			throws IOException {
		Map<String, List<String>> map = conn.getHeaderFields();

		Iterator<String> itr = map.keySet().iterator();

		while (itr.hasNext()) {
			String key = itr.next();
			List<String> list = map.get(key);

			String header = key + ": ";
			for (int i = 0; i < list.size(); i++) {
				header += list.get(i) + " ";
			}

			log(header.trim());
		}
	}

	// always verify the host - don't check for certificate
	protected final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - dont check for any certificate
	 */
	protected static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
