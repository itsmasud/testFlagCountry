package com.fieldnation.test;

import com.fieldnation.json.JsonArray;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.rest.v1.Workorder;

public class TestFnWeb {

	public static void main(String[] args) {
		try {
			AccessToken token = new AccessToken("dev.fieldnation.com",
					"password", "demoapp", "demopass", "jacobfaketech",
					"jacobfaketech");

			Workorder wo = new Workorder(token);

			JsonArray ja = wo.getRequested();

			String test = ja.getJsonObject(0).getString(
					"providersPhoto[0].photo_url");

			System.out.println(ja.display());

			System.out.println("bp here");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
