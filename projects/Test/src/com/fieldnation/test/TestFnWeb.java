package com.fieldnation.test;

import com.fieldnation.json.JsonArray;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.rest.v1.Workorder;

public class TestFnWeb {

	public static void main(String[] args) {
		Result result = null;
		try {
			AccessToken token = new AccessToken("dev.fieldnation.com",
					"password", "demoapp", "demopass", "jacobfaketech",
					"jacobfaketech");

			Workorder wo = new Workorder(token);

			System.out.println(Integer.class.getName());
			System.out.println(Integer.class.getCanonicalName());

			result = wo.getRequested(1);

			System.out.println(result.getResultsAsJsonArray().display());

			// System.out.println(dest.size());

			// result = wo.getDetails(146195);

			// System.out.println(result.getResultsAsString());

			System.out.println("BP HERE");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
