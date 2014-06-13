package com.fieldnation.test;

import java.util.Enumeration;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.rest.v1.Workorder;

public class TestFnWeb {

	public static void main(String[] args) {
		Result result = null;
		try {

			AccessToken token = new AccessToken("dev.fieldnation.com", "password", "demoapp", "demopass", "jacobfaketech", "jacobfaketech");

			Workorder wo = new Workorder(token);

			// result = wo.getDetails(41463);
			result = wo.getAssigned(1);

			// JsonObject w = result.getResultsAsJsonObject();
			JsonObject w = result.getResultsAsJsonArray().getJsonObject(0);

			Enumeration<String> e = w.keys();
			while (e.hasMoreElements()) {
				System.out.println(e.nextElement());
			}

			// System.out.println(result.getResultsAsJsonObject().display());

			// System.out.println(dest.size());

			// result = wo.getDetails(146195);

			// System.out.println(result.getResultsAsString());

			System.out.println("BP HERE");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
