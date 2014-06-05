package com.fieldnation.auth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import android.content.Context;

import com.fieldnation.R;
import com.fieldnation.webapi.AccessToken;

public class AuthHelper {
	public static AccessToken getToken(Context context, String username,
			String password) throws MalformedURLException, IOException,
			ParseException {

		String hostname = context.getString(R.string.fn_hostname);
		String grantType = context.getString(R.string.fn_grant_type);
		String clientId = context.getString(R.string.fn_client_id);
		String clientSecret = context.getString(R.string.fn_client_secret);

		return new AccessToken(hostname, grantType, clientId, clientSecret,
				username, password);
	}
	
}
