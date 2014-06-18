package com.fieldnation;

import java.util.LinkedList;
import java.util.List;

import com.fieldnation.webapi.OAuth;
import com.fieldnation.webapi.Ws;

import android.app.Application;

/**
 * Defines some global values that will be shared between all objects.
 * 
 * @author michael.carver
 * 
 */
public class GlobalState extends Application {
	private static final String TAG = "GlobalState";

	public String accountType;
	public String authority;
	public String username = null;
	public String accessToken = null;

	private List<ApplicationState> _listeners = new LinkedList<ApplicationState>();

	public GlobalState() {
		super();
		Ws.USE_HTTPS = true;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		accountType = getString(R.string.accounttype);
		authority = getString(R.string.authority);
	}

	public void addApplicationStateListener(ApplicationState listener) {
		_listeners.add(listener);
	}

	public void removeApplicationStateListener(ApplicationState listener) {
		_listeners.remove(listener);
	}

	public void dispatchAuthenticationLost() {
		for (int i = 0; i < _listeners.size(); i++) {
			_listeners.get(i).onAuthenticationLost();
		}
	}

	public void dispatchAuthenticationObtained() {
		for (int i = 0; i < _listeners.size(); i++) {
			_listeners.get(i).onAuthenticationObtained();
		}
	}
}
