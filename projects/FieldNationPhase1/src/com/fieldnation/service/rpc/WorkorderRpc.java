package com.fieldnation.service.rpc;

import java.util.HashMap;

import com.fieldnation.service.DataService;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.rest.v1.Workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class WorkorderRpc extends RpcInterface {

	public WorkorderRpc(HashMap<String, RpcInterface> map) {
		super(map, "workorder");
	}

	@Override
	public void execute(Context context, Intent intent) {
		try {
			String method = intent.getStringExtra("METHOD");
			AccessToken at = new AccessToken(
					intent.getStringExtra("PARAM_ACCESS_TOKEN"));

			if ("getRequested".equals(method)) {
				doGetRequested(context, intent, at);
			} else if ("getAvailable".equals(method)) {
				doGetAvailable(context, intent, at);
			} else if ("getPendingApproval".equals(method)) {
				doGetPendingApproval(context, intent, at);
			} else if ("getAssigned".equals(method)) {
				doGetAssigned(context, intent, at);
			} else if ("getDetails".equals(method)) {
				doGetDetails(context, intent, at);
			} else if ("doDecline".equals(method)) {
				doDecline(context, intent, at);
			} else if ("addRequest".equals(method)) {
				doAddRequest(context, intent, at);
			} else if ("removeRequest".equals(method)) {
				doRemoveRequest(context, intent, at);
			} else if ("confirmAssignment".equals(method)) {
				doConfirmAssignment(context, intent, at);
			}

		} catch (Exception ex) {
			// TODO report error
			ex.printStackTrace();
		}
	}
	/*- TODO			cancelAssignment			-*/
	/*- TODO			ready			-*/

	/*-			confirmAssignment			-*/
	private void doConfirmAssignment(Context context, Intent intent,
			AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			long workorderId = bundle.getLong("PARAM_WORKORDER_ID");
			long startTime = bundle.getLong("PARAM_START_TIME");
			long endTime = bundle.getLong("PARAM_END_TIME");

			Workorder wo = new Workorder(at);

			Result result = wo.confirmAssignment(workorderId, startTime,
					endTime);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.confirmAssignment");
				response.putLong("PARAM_WORKORDER_ID", workorderId);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void confirmAssignment(Context context,
			ResultReceiver callback, int resultCode, AccessToken at,
			long workorderId, long startTime, long endTime) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "confirmAssignment");
		intent.putExtra("PARAM_WORKORDER_ID", workorderId);
		intent.putExtra("PARAM_START_TIME", startTime);
		intent.putExtra("PARAM_END_TIME", endTime);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

	/*-			removeRequest			-*/
	private void doRemoveRequest(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			long workorderId = bundle.getLong("PARAM_WORKORDER_ID");

			Workorder wo = new Workorder(at);

			Result result = wo.removeRequest(workorderId);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.removeRequest");
				response.putLong("PARAM_WORKORDER_ID", workorderId);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void removeRequest(Context context, ResultReceiver callback,
			int resultCode, AccessToken at, long workorderId) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "removeRequest");
		intent.putExtra("PARAM_WORKORDER_ID", workorderId);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

	/*-			addRequest			-*/
	private void doAddRequest(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			long workorderId = bundle.getLong("PARAM_WORKORDER_ID");
			int expireInSeconds = bundle.getInt("PARAM_EXPIRATION");

			Workorder wo = new Workorder(at);

			Result result = wo.addRequest(workorderId, expireInSeconds);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.addRequest");
				response.putLong("PARAM_WORKORDER_ID", workorderId);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void addRequest(Context context, ResultReceiver callback,
			int resultCode, AccessToken at, long workorderId,
			int expireInSeconds) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "addRequest");
		intent.putExtra("PARAM_WORKORDER_ID", workorderId);
		intent.putExtra("PARAM_EXPIRATION", expireInSeconds);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

	/*-			doDecline			-*/
	private void doDecline(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			long workorderId = bundle.getLong("PARAM_WORKORDER_ID");

			Workorder wo = new Workorder(at);

			Result result = wo.decline(workorderId);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.decline");
				response.putLong("PARAM_WORKORDER_ID", workorderId);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void decline(Context context, ResultReceiver callback,
			int resultCode, AccessToken at, long workorderId) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "decline");
		intent.putExtra("PARAM_WORKORDER_ID", workorderId);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

	/*-			getDetails			-*/
	private void doGetDetails(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			long workorderId = bundle.getLong("PARAM_WORKORDER_ID");

			Workorder wo = new Workorder(at);

			Result result = wo.getDetails(workorderId);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.getDetails");
				response.putLong("PARAM_WORKORDER_ID", workorderId);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void getDetails(Context context, ResultReceiver callback,
			int resultCode, AccessToken at, long workorderId) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "getDetails");
		intent.putExtra("PARAM_WORKORDER_ID", workorderId);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

	/*-			getAssigned			-*/
	private void doGetAssigned(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			int page = bundle.getInt("PARAM_PAGE");

			Workorder wo = new Workorder(at);

			Result result = wo.getAssigned(page);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.getAssigned");
				response.putInt("PARAM_PAGE", page);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void getAssigned(Context context, ResultReceiver callback,
			int resultCode, AccessToken at, int page) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "getAssigned");
		intent.putExtra("PARAM_PAGE", page);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

	/*-			getPendingApproval			-*/
	private void doGetPendingApproval(Context context, Intent intent,
			AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			int page = bundle.getInt("PARAM_PAGE");

			Workorder wo = new Workorder(at);

			Result result = wo.getPendingApproval(page);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.getPendingApproval");
				response.putInt("PARAM_PAGE", page);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void getPendingApproval(Context context,
			ResultReceiver callback, int resultCode, AccessToken at, int page) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "getPendingApproval");
		intent.putExtra("PARAM_PAGE", page);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

	/*-			getAvailable			-*/
	private void doGetAvailable(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			int page = bundle.getInt("PARAM_PAGE");

			Workorder wo = new Workorder(at);

			Result result = wo.getAvailable(page);

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");
				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_Workorder.getAvailable");
				response.putInt("PARAM_PAGE", page);
				response.putByteArray("PARAM_DATA",
						result.getResultsAsByteArray());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void getAvailable(Context context, ResultReceiver callback,
			int resultCode, AccessToken at, int page) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "getAvailable");
		intent.putExtra("PARAM_PAGE", page);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

	/*-			getRequested			-*/
	private void doGetRequested(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			int page = bundle.getInt("PARAM_PAGE");

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

	public static void getRequested(Context context, ResultReceiver callback,
			int resultCode, AccessToken at, int page) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "workorder");
		intent.putExtra("METHOD", "getRequested");
		intent.putExtra("PARAM_PAGE", page);
		intent.putExtra("PARAM_ACCESS_TOKEN", at.toString());
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		context.startService(intent);
	}

}
