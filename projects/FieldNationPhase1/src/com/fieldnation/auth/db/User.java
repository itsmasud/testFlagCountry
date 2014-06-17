package com.fieldnation.auth.db;

import java.security.MessageDigest;

import android.database.Cursor;
import android.util.Log;

import com.fieldnation.utils.misc;

/**
 * An object that represents a user.
 * 
 * @author michael.carver
 * 
 */
public class User {
	protected long _id;
	protected String _username;
	protected String _securityHash = "INVALID";
	protected String _authToken = "INVALID";
	protected String _password = "INVALID";
	protected long _authExpiresOn = 0;
	protected String _authBlob = "";

	/**
	 * Generates a user from the database
	 * 
	 * @param src
	 */
	protected User(Cursor src) {
		_id = src.getLong(0);
		_username = src.getString(1);
		_password = src.getString(2);
		_securityHash = src.getString(3);
		_authToken = src.getString(4);
		_authExpiresOn = src.getLong(5);
		_authBlob = src.getString(6);
	}

	/**
	 * Generates a user from a username/password
	 * 
	 * @param username
	 * @param password
	 */
	protected User(String username, String password) {
		_username = username;
		_password = password;
		_securityHash = generateSecurityHash(password);
	}

	/**
	 * Sets the password for this account
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		_password = password;
		_securityHash = generateSecurityHash(password);
	}

	/**
	 * Returns the expiration time in UTC milliseconds
	 * 
	 * @return
	 */
	public long getExpirationDate() {
		return _authExpiresOn;
	}

	/**
	 * 
	 * @param password
	 * @return
	 */
	public boolean validatePassword(String password) {
		try {
			String hash = generateSecurityHash(password);

			return _securityHash.equals(hash);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param expiration
	 *            token ttl in seconds
	 * @return
	 */
	public String createAuthToken(long expiration) {
		try {
			_authExpiresOn = expiration * 1000 + System.currentTimeMillis();
			_authToken = generateAuthToken(_authExpiresOn);

			return _authToken;
		} catch (Exception ex) {
			_authExpiresOn = -1;
			_authToken = null;
			return null;
		}
	}

	public void setAuthBlob(String authBlob) {
		_authBlob = authBlob;
	}

	public String getAuthBlob() {
		Log.v("User", _authBlob);
		return _authBlob;
	}

	public String getPassword() {
		return _password;
	}

	public boolean isAuthTokenExpired() {
		return _authExpiresOn < System.currentTimeMillis();
	}

	public boolean isAuthTokenValid(String authToken) {
		if (isAuthTokenExpired()) {
			return false;
		}

		return _authToken.equals(authToken);
	}

	private String generateSecurityHash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// TODO, salt this hash
			return misc.getHex(md.digest(password.getBytes()));
		} catch (Exception ex) {
			// TODO should never happen
			System.exit(1);
			return null;
		}
	}

	private String generateAuthToken(long expiration) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String data = _securityHash + ":" + expiration;
			// TODO, salt this hash
			return misc.getHex(md.digest(data.getBytes()));
		} catch (Exception ex) {
			// TODO should never happen
			System.exit(1);
			return null;
		}
	}

}
