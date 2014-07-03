package com.fieldnation.j2j;

import java.io.File;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;

public class J2J {
	private static String hostname = "dev.fieldnation.com";
	private static String authToken = "275406312a7f0b4ae4f9229114e29b4b36c42e1f";

	public static void main(String[] args) {
		// getWorkorders();
		getMessages();
	}

	private static void getMessages() {
		String[] urls = new String[] {
				"/api/rest/v1/profile/messages/unread?access_token=" + authToken + "&page=0",
				"/api/rest/v1/profile/messages/unread?access_token=" + authToken + "&page=1",
				"/api/rest/v1/profile/messages/unread?access_token=" + authToken + "&page=2",
				"/api/rest/v1/profile/messages/unread?access_token=" + authToken + "&page=3",
				"/api/rest/v1/profile/messages/unread?access_token=" + authToken + "&page=4" };
		try {
			JsonArray objects = new JsonArray();

			for (int i = 0; i < urls.length; i++) {
				System.out.println(urls[i]);
				Result result = Ws.httpGet(hostname, urls[i]);

				objects.merge(result.getResultsAsJsonArray());
			}

			System.out.println("Building Class Structure");
			new File("c:/j2j/messages").mkdirs();
			Serializer s = new Serializer("com.fieldnation.data.messages",
					"c:/j2j/messages");

			s.performJ2J(objects.toString(), "Message");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void getWorkorders() {
		String[] urls = new String[] {
				"/api/rest/v1/workorder/requested?access_token=" + authToken + "&page=0",
				"/api/rest/v1/workorder/requested?access_token=" + authToken + "&page=1",
				"/api/rest/v1/workorder/requested?access_token=" + authToken + "&page=2",
				"/api/rest/v1/workorder/requested?access_token=" + authToken + "&page=3",
				"/api/rest/v1/workorder/requested?access_token=" + authToken + "&page=4",
				"/api/rest/v1/workorder/requested?access_token=" + authToken + "&page=5",
				"/api/rest/v1/workorder/available?access_token=" + authToken + "&page=0",
				"/api/rest/v1/workorder/available?access_token=" + authToken + "&page=1",
				"/api/rest/v1/workorder/available?access_token=" + authToken + "&page=2",
				"/api/rest/v1/workorder/available?access_token=" + authToken + "&page=3",
				"/api/rest/v1/workorder/available?access_token=" + authToken + "&page=4",
				"/api/rest/v1/workorder/available?access_token=" + authToken + "&page=5",
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
				"/api/rest/v1/workorder/assigned?access_token=" + authToken + "&page=0",
				"/api/rest/v1/workorder/assigned?access_token=" + authToken + "&page=1",
				"/api/rest/v1/workorder/assigned?access_token=" + authToken + "&page=2",
				"/api/rest/v1/workorder/assigned?access_token=" + authToken + "&page=3",
				"/api/rest/v1/workorder/assigned?access_token=" + authToken + "&page=4",
				"/api/rest/v1/workorder/assigned?access_token=" + authToken + "&page=5",
				"/api/rest/v1/workorder/completed?access_token=" + authToken + "&page=0",
				"/api/rest/v1/workorder/completed?access_token=" + authToken + "&page=1",
				"/api/rest/v1/workorder/completed?access_token=" + authToken + "&page=2",
				"/api/rest/v1/workorder/completed?access_token=" + authToken + "&page=3",
				"/api/rest/v1/workorder/completed?access_token=" + authToken + "&page=4",
				"/api/rest/v1/workorder/completed?access_token=" + authToken + "&page=5",
				"/api/rest/v1/workorder/canceled?access_token=" + authToken + "&page=0",
				"/api/rest/v1/workorder/canceled?access_token=" + authToken + "&page=1",
				"/api/rest/v1/workorder/canceled?access_token=" + authToken + "&page=2",
				"/api/rest/v1/workorder/canceled?access_token=" + authToken + "&page=3",
				"/api/rest/v1/workorder/canceled?access_token=" + authToken + "&page=4",
				"/api/rest/v1/workorder/canceled?access_token=" + authToken + "&page=5",

		};
		try {
			JsonArray objects = new JsonArray();

			for (int i = 0; i < urls.length; i++) {
				System.out.println(urls[i]);
				Result result = Ws.httpGet(hostname, urls[i]);

				objects.merge(result.getResultsAsJsonArray());
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
			new File("c:/j2j/workorder").mkdirs();
			Serializer s = new Serializer("com.fieldnation.data.workorder",
					"c:/j2j/workorder");

			s.performJ2J(objects.toString(), "Workorder");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
