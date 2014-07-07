package com.fieldnation.j2j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;

public class J2J {
	private static String hostname = "dev.fieldnation.com";
	private static String authToken = "81aab86ff298860734e76713f30363a53edcde14";

	public static void main(String[] args) {
		getWorkorders();
		getMessages();
		getPayments();
	}

	private static void dumpClasses(JsonArray objects, String path,
			String packageName, String rootName) throws ParseException, IOException {
		new File(path).mkdirs();

		JavaObject.cleanup();

		JavaObject object = JavaObject.getInstance(rootName, packageName);
		for (int i = 0; i < objects.size(); i++) {
			object.addData(objects.getJsonObject(i));
		}

		Enumeration<JavaObject> javaObjects = JavaObject.getJavaObjects();
		while (javaObjects.hasMoreElements()) {
			JavaObject obj = javaObjects.nextElement();

			FileOutputStream fout = new FileOutputStream(
					path + "/" + obj.getClassName() + ".java");

			fout.write(obj.toString().getBytes());
			fout.close();
		}

	}

	private static void getPayments() {
		String[] urls = new String[] {
				"/api/rest/v1/accounting/payment-queue/pending?access_token=" + authToken + "&page=",
				"/api/rest/v1/accounting/payment-queue/paid?access_token=" + authToken + "&page=",
				"/api/rest/v1/accounting/payment-queue/all?access_token=" + authToken + "&page=" };
		try {
			JsonArray objects = new JsonArray();

			for (int i = 0; i < urls.length; i++) {
				for (int j = 1; true; j++) {
					System.out.println(urls[i] + j);
					Result result = Ws.httpGet(hostname, urls[i] + j);

					JsonArray ja = result.getResultsAsJsonArray();
					if (ja.size() == 0)
						break;

					objects.merge(result.getResultsAsJsonArray());
				}
			}

			System.out.println("Building Class Structure");
			dumpClasses(objects, "c:/j2j/payments",
					"com.fieldnation.data.payments", "Payment");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void getMessages() {
		String[] urls = new String[] { "/api/rest/v1/profile/messages/unread?access_token=" + authToken + "&page=" };
		try {
			JsonArray objects = new JsonArray();

			for (int i = 0; i < urls.length; i++) {
				for (int j = 0; true; j++) {
					System.out.println(urls[i] + j);
					Result result = Ws.httpGet(hostname, urls[i] + j);

					JsonArray ja = result.getResultsAsJsonArray();
					if (ja.size() == 0)
						break;

					objects.merge(result.getResultsAsJsonArray());
				}
			}

			System.out.println("Building Class Structure");
			dumpClasses(objects, "c:/j2j/messages",
					"com.fieldnation.data.messages", "Message");

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
					System.out.println(urls[i] + j);
					Result result = Ws.httpGet(hostname, urls[i] + j);

					JsonArray res = result.getResultsAsJsonArray();
					if (res.size() == 0)
						break;

					objects.merge(res);
				}
			}

			System.out.println("Have " + objects.size() + " workorders.");
			JsonArray details = new JsonArray();
			for (int i = 0; i < objects.size(); i++) {
				System.out.println(i);
				JsonObject workorder = objects.getJsonObject(i);

				Result result = Ws.httpGet(
						hostname,
						"/api/rest/v1/workorder/" + workorder.getString("workorder_id") + "/details?access_token=" + authToken);

				details.add(result.getResultsAsJsonObject());
			}
			objects.merge(details);

			System.out.println("Building Class Structure");
			dumpClasses(objects, "c:/j2j/workorder",
					"com.fieldnation.data.workorder", "Workorder");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
