package com.fieldnation.service;

import com.fieldnation.json.JsonArray;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.rest.v1.Workorder;

import android.app.IntentService;
import android.content.Intent;

public class BackgroundService extends IntentService {

	public BackgroundService() {
		super(null);
	}

	public BackgroundService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String command = intent.getExtras().getString("COMMAND");

		System.out.println("Command: " + command);

		testWeb();
	}

	private void testWeb() {
		try {
			AccessToken token = new AccessToken("dev.fieldnation.com",
					"password", "demoapp", "demopass", "jacobfaketech",
					"jacobfaketech");

			Workorder wo = new Workorder(token);

			JsonArray ja = wo.getRequested();

			String test = ja.getJsonObject(0).getString(
					"providersPhoto[0].photo_url");

			System.out.println(ja.display());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
