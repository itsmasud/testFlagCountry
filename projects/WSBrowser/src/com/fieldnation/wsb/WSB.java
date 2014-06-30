package com.fieldnation.wsb;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
						"Basic " + Base64.encode((username + ":" + password).getBytes()));
			}

			log("*******************************************");
			log("                Request");
			log("*******************************************");
			log(path);
			dumpConnectionRequest(conn);

			// get the steam from the server
			InputStream str = conn.getInputStream();
			int size = conn.getContentLength();
			byte[] rawData = misc.readAllFromStream(str, 1024, size, 3000);

			log("*******************************************");
			log("                Response");
			log("*******************************************");
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
		}

		return null;
	}

	public WebResult httpWrite(String method, String path, String payload,
			String contentType, String username, String password) {
		URL url = null;
		HttpURLConnection conn = null;
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
						"Basic " + Base64.encode((username + ":" + password).getBytes()));
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
			InputStream str = conn.getInputStream();

			int size = conn.getContentLength();

			byte[] rawData = misc.readAllFromStream(str, 1024, size, 3000);

			log("*******************************************");
			log("                Response");
			log("*******************************************");
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
		}

		return null;
	}

	public void performJ2J(String data) {
		try {
			JsonObject json = new JsonObject(data);

		} catch (Exception ex) {
			try {
				JsonArray ja = new JsonArray(data);
				performJ2J(ja);
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}

	private void performJ2J(JsonArray jsonSource) {

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

	public void dumpConnectionResponse(HttpURLConnection conn) throws IOException {
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
