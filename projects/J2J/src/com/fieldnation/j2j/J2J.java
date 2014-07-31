package com.fieldnation.j2j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Enumeration;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;

public class J2J {
	private static String hostname = "dev.fieldnation.com";
	private static String authToken = "";

	public static void main(String[] args) {
		String[] usernames = { "jacobfaketech", "hexixtech" };
		String[] passwords = { "jacobfaketech", "hexixtech" };

		try {
			new File(Config.ObjectPath).mkdirs();

			for (int i = 0; i < usernames.length; i++) {
				Log.println("Sarting: " + usernames[i]);

				try {
					authToken = Tools.authServer("dev.fieldnation.com", "/authentication/api/oauth/token", "password",
							"demoapp", "demopass", usernames[i], passwords[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//				getProfile();
//				getWorkorders();
//				getExpenseCategories();
//				getMessages();
//				getPayments();
				getNotifications();
			}
			exportClasses();

			Log.println("***********************************");
			Enumeration<JavaObject> javaObjects = JavaObject.getJavaObjects();
			while (javaObjects.hasMoreElements()) {
				JavaObject obj = javaObjects.nextElement();

				Log.println(obj.getUnderscoreFields());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void addData(JsonArray json, String packageName, String className) throws ParseException {
		for (int i = 0; i < json.size(); i++) {
			addData(json.getJsonObject(i), packageName, className);
		}
	}

	private static void addData(JsonObject json, String packageName, String className) throws ParseException {
		JavaObject object = JavaObject.getInstance(packageName, className);
		object.addData(json);
	}

	private static void exportClasses() throws IOException {
		Enumeration<JavaObject> javaObjects = JavaObject.getJavaObjects();
		while (javaObjects.hasMoreElements()) {
			JavaObject obj = javaObjects.nextElement();

			String exportPath = Config.ObjectPath + obj.packageName.replaceAll("\\.", "/") + "/";

			new File(exportPath).mkdirs();

			FileOutputStream fout = new FileOutputStream(exportPath + obj.getClassName() + ".java");
			fout.write(obj.toString().getBytes());
			fout.close();
		}
	}

	// private static void dumpClasses(JsonArray objects, String path, String
	// packageName, String rootName) throws ParseException, IOException {
	// new File(path).mkdirs();
	//
	// JavaObject.cleanup();
	// JavaObject object = JavaObject.getInstance(packageName, rootName);
	// for (int i = 0; i < objects.size(); i++) {
	// object.addData(objects.getJsonObject(i));
	// }
	//
	// Enumeration<JavaObject> javaObjects = JavaObject.getJavaObjects();
	// while (javaObjects.hasMoreElements()) {
	// JavaObject obj = javaObjects.nextElement();
	//
	// FileOutputStream fout = new FileOutputStream(path + "/" +
	// obj.getClassName() + ".java");
	//
	// fout.write(obj.toString().getBytes());
	// fout.close();
	// }
	//
	// Log.println("***********************************");
	// javaObjects = JavaObject.getJavaObjects();
	// while (javaObjects.hasMoreElements()) {
	// JavaObject obj = javaObjects.nextElement();
	//
	// Log.println(obj.getUnderscoreFields());
	// }
	// }
	//
	// private static void dumpClass(JsonObject json, String path, String
	// packageName, String className) throws ParseException, IOException {
	// new File(path).mkdirs();
	// JavaObject.cleanup();
	// JavaObject object = JavaObject.getInstance(packageName, className);
	// object.addData(json);
	//
	// Enumeration<JavaObject> javaObjects = JavaObject.getJavaObjects();
	// while (javaObjects.hasMoreElements()) {
	// JavaObject obj = javaObjects.nextElement();
	//
	// FileOutputStream fout = new FileOutputStream(path + "/" +
	// obj.getClassName() + ".java");
	// fout.write(obj.toString().getBytes());
	// fout.close();
	// }
	// }

	private static void getPayments() {
		String[] urls = new String[] {
				"/api/rest/v1/accounting/payment-queue/pending?access_token=" + authToken + "&page=",
				"/api/rest/v1/accounting/payment-queue/paid?access_token=" + authToken + "&page=",
				"/api/rest/v1/accounting/payment-queue/all?access_token=" + authToken + "&page=" };
		try {
			JsonArray objects = new JsonArray();

			for (int i = 0; i < urls.length; i++) {
				for (int j = 0; true; j++) {
					Log.println(urls[i] + j);
					Result result = Ws.httpGet(hostname, urls[i] + j);

					JsonArray ja = result.getResultsAsJsonArray();
					if (ja.size() == 0)
						break;

					objects.merge(result.getResultsAsJsonArray());
				}
			}

			Log.println("Building Class Structure");
			addData(objects, "com.fieldnation.data.accounting", "Payment");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void getMessages() {
		String[] urls = new String[] {
				"/api/rest/v1/profile/messages/unread?access_token=" + authToken + "&page=",
				"/api/rest/v1/profile/messages?access_token=" + authToken + "&page=" };
		try {
			JsonArray objects = new JsonArray();

			for (int i = 0; i < urls.length; i++) {
				for (int j = 0; true; j++) {
					Log.println(urls[i] + j);
					Result result = Ws.httpGet(hostname, urls[i] + j);

					JsonArray ja = result.getResultsAsJsonArray();
					if (ja.size() == 0)
						break;

					objects.merge(result.getResultsAsJsonArray());
				}
			}

			Log.println("Building Class Structure");
			addData(objects, "com.fieldnation.data.profile", "Message");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void getProfile() {
		String[] urls = new String[] { "/api/rest/v1/profile?access_token=" + authToken };
		try {
			Result result = Ws.httpGet(hostname, urls[0]);

			JsonObject obj = result.getResultsAsJsonObject();

			Log.println("Building Class Structure");
			addData(obj, "com.fieldnation.data.profile", "Profile");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void getNotifications() {
		String[] urls = new String[] {
				"/api/rest/v1/profile/notifications/new?access_token=" + authToken + "&page=",
				"/api/rest/v1/profile/notifications?access_token=" + authToken + "&page=" };
		try {
			JsonArray objects = new JsonArray();

			for (int i = 0; i < urls.length; i++) {
				for (int j = 0; true; j++) {
					Log.println(urls[i] + j);
					Result result = Ws.httpGet(hostname, urls[i] + j);

					JsonArray ja = result.getResultsAsJsonArray();
					if (ja.size() == 0)
						break;

					objects.merge(result.getResultsAsJsonArray());
				}
			}

			Log.println("Building Class Structure");
			addData(objects, "com.fieldnation.data.profile", "Notification");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void getWorkorders() {
		String[] urls = new String[] {
				"/api/rest/v1/workorder/requested?access_token=" + authToken + "&page=",
				"/api/rest/v1/workorder/available?access_token=" + authToken + "&page=",
				// "/api/rest/v1/workorder/pending_approval?access_token=" +
				// authToken + "",
				// "/api/rest/v1/workorder/pending_approval?access_token=" +
				// authToken + "&page=1",
				// "/api/rest/v1/workorder/pending_approval?access_token=" +
				// authToken + "&page=2",
				// "/api/rest/v1/workorder/pending_approval?access_token=" +
				// authToken + "&page=3",
				// "/api/rest/v1/workorder/pending_approval?access_token=" +
				// authToken + "&page=4",
				// "/api/rest/v1/workorder/pending_approval?access_token=" +
				// authToken + "&page=5",
				"/api/rest/v1/workorder/assigned?access_token=" + authToken + "&page=",
				"/api/rest/v1/workorder/completed?access_token=" + authToken + "&page=",
				"/api/rest/v1/workorder/canceled?access_token=" + authToken + "&page=",

		};
		try {
			JsonArray objects = new JsonArray();

			for (int i = 0; i < urls.length; i++) {
				for (int j = 0; true; j++) {
					Log.println(urls[i] + j);
					Result result = Ws.httpGet(hostname, urls[i] + j);

					String strres = result.getResultsAsString();

					if (strres.contains("payRateBasis")) {
						Log.println("payRateBasis");
					}
					if (strres.contains("basis")) {
						Log.println("basis");
					}
					if (!strres.contains("pay")) {
						Log.println("no pay!");
					}

					if (!strres.contains("file_url")) {
						Log.println("file_url");
					}

					JsonArray res = result.getResultsAsJsonArray();
					if (res.size() == 0)
						break;

					objects.merge(res);
				}
			}

			Log.println("Have " + objects.size() + " workorders.");
			JsonArray details = new JsonArray();
			for (int i = 0; i < objects.size(); i++) {
				Log.println(i);
				JsonObject workorder = objects.getJsonObject(i);

				String url = "/api/rest/v1/workorder/" + workorder.getString("workorderId") + "/details?access_token=" + authToken;

				Log.println(url);

				Result result = Ws.httpGet(hostname, url);

				String res = result.getResultsAsString();

				if (res.contains("payRateBasis")) {
					Log.println("payRateBasis");
				}
				if (res.contains("basis")) {
					Log.println("basis");
				}
				if (!res.contains("pay")) {
					Log.println("no pay!");
				}
				if (!res.contains("file_url")) {
					Log.println("file_url");
				}

				details.add(result.getResultsAsJsonObject());
			}
			objects.merge(details);

			Log.println("Building Class Structure");
			addData(objects, "com.fieldnation.data.workorder", "Workorder");

			getWorkorderMessages(details);
			getWorkorderTasks(details);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void getExpenseCategories() {
		String[] urls = new String[] { "/api/rest/v1/workorder/expense/categories?access_token=" + authToken };
		try {
			Result result = Ws.httpGet(hostname, urls[0]);

			JsonArray ja = result.getResultsAsJsonArray();

			Log.println("Building Class Structure");
			addData(ja, "com.fieldnation.data.workorder", "ExpenseCategory");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void getWorkorderMessages(JsonArray workorders) {
		try {
			JsonArray messages = new JsonArray();
			for (int i = 0; i < workorders.size(); i++) {
				JsonObject workorder = workorders.getJsonObject(i);

				String url = "/api/rest/v1/workorder/" + workorder.getLong("workorderId") + "/messages?access_token=" + authToken;
				Log.println(url);
				try {
					Result result = Ws.httpGet(hostname, url);
					messages.merge(result.getResultsAsJsonArray());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (messages.size() > 0) {
				System.out.println("BP");
			}

			Log.println("Building Class Structure");
			addData(messages, "com.fieldnation.data.workorder", "Message");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void getWorkorderTasks(JsonArray workorders) {
		try {
			for (int i = 0; i < workorders.size(); i++) {
				JsonObject workorder = workorders.getJsonObject(i);

				String url = "/api/rest/v1/workorder/" + workorder.getLong("workorderId") + "/tasks?access_token=" + authToken;
				Log.println(url);
				try {
					Result result = Ws.httpGet(hostname, url);
					JsonArray ja = result.getResultsAsJsonArray();

					if (ja.size() > 0) {
						System.out.println(ja.size());
					}

					addData(ja, "com.fieldnation.data.workorder", "TaskDetail");

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
