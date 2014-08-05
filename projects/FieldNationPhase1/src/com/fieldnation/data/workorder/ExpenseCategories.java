package com.fieldnation.data.workorder;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fieldnation.GlobalState;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class ExpenseCategories {
	private static final String TAG = "data.workorder.ExpenseCategories";
	private static Hashtable<Context, ExpenseCategories> _instances = new Hashtable<Context, ExpenseCategories>();

	private GlobalState _gs;
	private WorkorderService _ws;
	private ExpenseCategory[] _categories;
	private Listener _listener = null;

	public static ExpenseCategories getInstance(Context context) {
		Context appcontext = context.getApplicationContext();

		if (!_instances.containsKey(appcontext)) {
			_instances.put(appcontext, new ExpenseCategories(appcontext));
		}

		return _instances.get(appcontext);
	}

	private ExpenseCategories(Context context) {
		_gs = (GlobalState) context;
		_gs.requestAuthentication(_authclient);
	}

	public ExpenseCategory[] getCategories() {
		return _categories;
	}

	public void setListener(Listener listener) {
		_listener = listener;

		if (_categories != null) {
			_listener.onHaveCategories(_categories);
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private AuthenticationClient _authclient = new AuthenticationClient() {

		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authclient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_ws = new WorkorderService(_gs, username, authToken, _resultReciever);
			_gs.startService(_ws.getExpenseCategories(0, true));
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			try {
				JsonArray ja = new JsonArray(new String(resultData.getByteArray(WorkorderService.KEY_RESPONSE_DATA)));

				_categories = new ExpenseCategory[ja.size()];

				for (int i = 0; i < ja.size(); i++) {
					JsonObject obj = ja.getJsonObject(i);
					ExpenseCategory cat = ExpenseCategory.fromJson(obj);
					_categories[i] = cat;
				}

				if (_listener != null) {
					_listener.onHaveCategories(_categories);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");
		}
	};

	public interface Listener {
		public void onHaveCategories(ExpenseCategory[] categories);
	}
}
