package com.fieldnation.auth.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import android.content.Context;
import android.database.Cursor;
import android.os.Message;

import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

public class User {
	protected long _id;
	protected String _username;
	protected String _securityHash = "INVALID";
	protected String _authToken = "INVALID";
	protected long _authExpiresOn = 0;

	/**
	 * Generates a user from the database
	 * 
	 * @param src
	 */
	protected User(Cursor src) {
		_id = src.getLong(0);
		_username = src.getString(1);
		_securityHash = src.getString(2);
		_authToken = src.getString(3);
		_authExpiresOn = src.getLong(4);
	}

	/**
	 * Generates a user from a username/password
	 * 
	 * @param username
	 * @param password
	 * @throws NoSuchAlgorithmException
	 */
	protected User(String username, String password) throws NoSuchAlgorithmException {
		_username = username;
		_securityHash = generateSecurityHash(password);
	}

	/**
	 * Sets the password for this account
	 * 
	 * @param password
	 * @throws NoSuchAlgorithmException
	 */
	public void setPassword(String password) throws NoSuchAlgorithmException {
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
	 * @throws NoSuchAlgorithmException
	 */
	public boolean validatePassword(String password) throws NoSuchAlgorithmException {
		String hash = generateSecurityHash(password);

		return _securityHash.equals(hash);
	}

	/**
	 * 
	 * @param expiration
	 *            token ttl in seconds
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String createAuthToken(long expiration) throws NoSuchAlgorithmException {
		_authExpiresOn = expiration * 1000 + System.currentTimeMillis();
		_authToken = generateAuthToken(_authExpiresOn);

		return _authToken;
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

	private String generateSecurityHash(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		// TODO, salt this hash
		return misc.getHex(md.digest(password.getBytes()));
	}

	private String generateAuthToken(long expiration) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		String data = _securityHash + ":" + expiration;
		// TODO, salt this hash
		return misc.getHex(md.digest(data.getBytes()));
	}

}
