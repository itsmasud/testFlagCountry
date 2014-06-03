package com.fieldnation.webapi.rest.v1;

import java.io.IOException;
import java.text.ParseException;

import com.fieldnation.json.JsonArray;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Ws;

public class Workorder extends Ws {

	public Workorder(AccessToken token) {
		super(token);
	}

	public JsonArray getRequested() throws IOException, ParseException {
		return httpGet("/api/rest/v1/workorder/requested")
				.getResultsAsJsonArray();
	}

}
