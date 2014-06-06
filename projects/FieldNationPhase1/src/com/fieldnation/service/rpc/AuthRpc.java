package com.fieldnation.service.rpc;

import java.util.HashMap;

import com.fieldnation.webapi.AccessToken;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AuthRpc extends RpcInterface {

	AuthRpc(HashMap<String, RpcInterface> map) {
		super(map, "oauth/token");
	}

	@Override
	public void execute(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();

			String hostname = bundle.getString("PARAM_HOSTNAME");
			String path = bundle.getString("PARAM_PATH");
			String grantType = bundle.getString("PARAM_GRANT_TYPE");
			String clientId = bundle.getString("PARAM_CLIENT_ID");
			String clientSecret = bundle.getString("PARAM_CLIENT_SECRET");
			String username = bundle.getString("PARAM_USERNAME");
			String password = bundle.getString("PARAM_PASSWORD");

			AccessToken at = null;
			try {
				at = new AccessToken(hostname, path, grantType, clientId,
						clientSecret, username, password);
			} catch (Exception ex) {
				// could not get the token... need to figure out why
			}

			// TODO Auto-generated method stub
			System.out.println("Method Stub: execute()");
		} catch (Exception ex) {

		}
	}

}
