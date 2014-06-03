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
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.Base64;
import com.fieldnation.utils.misc;

public class WSB extends WsbUi {
	private static final long serialVersionUID = 1L;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
			String method = ((String) methodComboBox.getSelectedItem()).toUpperCase();
			String url = urlTextField.getText();
			String username = usernameTextField.getText();
			String password = new String(passwordTextField.getPassword());
			String mime = (String) mimeContentType.getSelectedItem();
			String payload = postDataTextArea.getText();

			if (username.equals("")) {
				username = null;
			}

			if ("GET".equals(method) || "DELETE".equals(method)) {
				getData(method, url, username, password);
			}
			else if ("POST".equals(method) || "PUT".equals(method)) {
				pushData(method, url, payload, mime, username, password);
			}
		} catch (Exception ex) {
			log(ex);
		}

	}


	public void log(String logging) {
		try {
			JsonObject jobj = new JsonObject(logging);
			logTextArea.append(jobj.display() + "\n");
		} catch (Exception e) {
			logTextArea.append(logging + "\n");
		}

		logTextArea.setCaretPosition(logTextArea.getText().length());
	}


	public void log(Exception e) {
		log(misc.getStackTrace(e));
	}


	public WebResult getData(String method, String path, String username, String password) {
		URL url = null;
		HttpURLConnection conn = null;
		try {
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", "Internet Access");

			// configure the authorization
			if (username != null && password != null) {
				conn.setRequestProperty("Authorization", "Basic "
						+ Base64.encode((username + ":" + password).getBytes()));
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
			log(new String(rawData));

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


	public WebResult pushData(String method, String path, String payload, String contentType, String username, String password) {
		URL url = null;
		HttpURLConnection conn = null;
		try {
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", "Internet Access");
			conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Content-Length", payload.length() + "");
			conn.setDoOutput(true);

			// configure the authorization
			if (username != null && password != null) {
				conn.setRequestProperty("Authorization", "Basic "
						+ Base64.encode((username + ":" + password).getBytes()));
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
			log(new String(rawData));

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
}
