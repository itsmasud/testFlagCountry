package com.fieldnation.service.rpc;

import java.util.HashMap;

import com.fieldnation.service.BackgroundService;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.rest.v1.Workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class WorkorderGetRequestedRpc extends RpcInterface {

	public WorkorderGetRequestedRpc(HashMap<String, RpcInterface> map) {
		super(map, "Workorder.getRequested");
	}

	@Override
	public void execute(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			int page = bundle.getInt("PARAM_PAGE");
			AccessToken at = new AccessToken(
					bundle.getString("PARAM_ACCESS_TOKEN"));

			Workorder wo = new Workorder(at);

			Result result = wo.getRequested(page);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.getRequested");
				response.putInt("PARAM_PAGE", page);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void sendRpc(Context context, ResultReceiver callback,
			int resultCode, AccessToken at, int page) {
		Intent intent = new Intent(context, BackgroundService.class);
		intent.setAction("RPC");
		intent.putExtra("METHOD", "Workorder.getRequested");
		intent.putExtra("PARAM_PAGE", page);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}
}
