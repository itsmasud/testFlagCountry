package com.fieldnation.rpc.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.fieldnation.Log;
import com.fieldnation.utils.misc;

import java.security.MessageDigest;

/**
 * Provides authentication caching. It caches authentication tokens, and
 * provides authentication tokens to activities that request them.
 *
 * @author michael.carver
 */
public class AuthCache {
    private static final String TAG = "auth.server.AuthCache";

    private Context _context;
    private long _id;
    private String _username;
    private String _passwordHash = "";
    private String _sessionHash = "";
    private long _sessionExpiry = 0;
    private String _oAuthBlob = "";
    private String _requestBlob = "";

    private AuthCache(Context context, Cursor src) {
        _context = context.getApplicationContext();
        _id = src.getLong(AuthCacheSqlHelper.Column.ID.getIndex());
        _username = src.getString(AuthCacheSqlHelper.Column.USERNAME.getIndex());
        _passwordHash = src.getString(AuthCacheSqlHelper.Column.PASSWORD_HASH.getIndex());
        _sessionHash = src.getString(AuthCacheSqlHelper.Column.SESSION_HASH.getIndex());
        _oAuthBlob = src.getString(AuthCacheSqlHelper.Column.OAUTH_BLOB.getIndex());
        _requestBlob = src.getString(AuthCacheSqlHelper.Column.REQUEST_BLOB.getIndex());
        _sessionExpiry = src.getLong(AuthCacheSqlHelper.Column.SESSION_EXPIRY.getIndex());
    }

    private AuthCache(Context context, String username, String password) {
        _context = context.getApplicationContext();
        _username = username;
        _passwordHash = generatePasswordHash(password);
    }

    /*-			Password		-*/
    public void setPassword(String password) {
        _passwordHash = generatePasswordHash(password);
        save();
    }

    public boolean validatePassword(String password) {
        try {
            return _passwordHash.equals(generatePasswordHash(password));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*-				Sessions				-*/
    public boolean validateSessionHash(String sessionHash) {
        if (_sessionHash.equals("")) {
            Log.v(TAG, "no session");
            return false;
        }

        if (_sessionExpiry < System.currentTimeMillis()) {
            Log.v(TAG, "session expired");
            invalidateSession();
            return false;
        }

        // Log.d(TAG, _sessionHash);

        return _sessionHash.equals(sessionHash);
    }

    public void invalidateSession() {
        _sessionHash = "";
        _sessionExpiry = 0;
        save();
    }

    public String startSession(String password, long expiryInSeconds) {
        if (!validatePassword(password))
            return null;

        _sessionHash = generateSessionHash(password);
        // TODO, need a better clock than this, this can be thwarted by manually
        // setting the clock on a phone
        _sessionExpiry = System.currentTimeMillis() + expiryInSeconds * 1000;
        save();

        return _sessionHash;
    }

    /*-			AuthBlob		-*/
    public void setOAuthBlob(String oAuthBlob) {
        _oAuthBlob = oAuthBlob;
        save();
    }

    public String getOAuthBlob() {
        return _oAuthBlob;
    }

    /*-			RequestBlob		-*/
    public void setRequestBlob(String requestBlob) {
        _requestBlob = requestBlob;
        save();
    }

    public String getRequestBlob() {
        return _requestBlob;
    }

    /*-			Hashing Utility		-*/
    private String generateSessionHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return misc.getHex(md.digest((password + ":" + _username + ":" + System.currentTimeMillis()).getBytes()));
        } catch (Exception ex) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                return misc.getHex(md.digest((password + ":" + _username + ":" + System.currentTimeMillis()).getBytes()));
            } catch (Exception ex2) {
                return null;
            }
        }
    }

    private String generatePasswordHash(String password) {
        try {
            // TODO, salt this hash !?
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return misc.getHex(md.digest(password.getBytes()));
        } catch (Exception ex) {
            try {
                // TODO, salt this hash !?
                MessageDigest md = MessageDigest.getInstance("MD5");
                return misc.getHex(md.digest(password.getBytes()));
            } catch (Exception ex2) {
                return null;
            }
        }
    }

    /*-			Database wrapper		-*/
    private void save() {
        save(_context, this);
    }

	/*-*****************************************-*/
    /*-				Accessor Stuff				-*/
    /*-*****************************************-*/

    public static AuthCache get(Context context, String username) {
        AuthCacheSqlHelper helper = new AuthCacheSqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor cursor = db.query(AuthCacheSqlHelper.TABLE_NAME, AuthCacheSqlHelper.getColumnNames(),
                    AuthCacheSqlHelper.Column.USERNAME + "=?", new String[]{username}, null, null, null);

            try {
                if (cursor.moveToFirst()) {
                    return new AuthCache(context, cursor);
                }

                return null;
            } finally {
                cursor.close();
            }
        } finally {
            helper.close();
        }
    }

    private static AuthCache get(Context context, SQLiteDatabase db, long id) {
        Cursor cursor = db.query(AuthCacheSqlHelper.TABLE_NAME, AuthCacheSqlHelper.getColumnNames(),
                AuthCacheSqlHelper.Column.ID + "=" + id, null, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return new AuthCache(context, cursor);
            }

            return null;
        } finally {
            cursor.close();
        }
    }

    public static AuthCache create(Context context, String username, String password) {
        AuthCache authCache = null;

        authCache = get(context, username);

        if (authCache != null) {
            return null;
        }

        authCache = new AuthCache(context, username, password);

        ContentValues values = new ContentValues();
        values.put(AuthCacheSqlHelper.Column.USERNAME.getName(), authCache._username);
        values.put(AuthCacheSqlHelper.Column.PASSWORD_HASH.getName(), authCache._passwordHash);
        values.put(AuthCacheSqlHelper.Column.OAUTH_BLOB.getName(), authCache._oAuthBlob);
        values.put(AuthCacheSqlHelper.Column.SESSION_HASH.getName(), authCache._sessionHash);
        values.put(AuthCacheSqlHelper.Column.REQUEST_BLOB.getName(), authCache._requestBlob);
        values.put(AuthCacheSqlHelper.Column.SESSION_EXPIRY.getName(), authCache._sessionExpiry);

        AuthCacheSqlHelper helper = new AuthCacheSqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            return get(context, db, db.insert(AuthCacheSqlHelper.TABLE_NAME, null, values));
        } finally {
            helper.close();
        }
    }

    private static void save(Context context, AuthCache authCache) {
        ContentValues values = new ContentValues();
        values.put(AuthCacheSqlHelper.Column.USERNAME.getName(), authCache._username);
        values.put(AuthCacheSqlHelper.Column.PASSWORD_HASH.getName(), authCache._passwordHash);
        values.put(AuthCacheSqlHelper.Column.OAUTH_BLOB.getName(), authCache._oAuthBlob);
        values.put(AuthCacheSqlHelper.Column.SESSION_HASH.getName(), authCache._sessionHash);
        values.put(AuthCacheSqlHelper.Column.REQUEST_BLOB.getName(), authCache._requestBlob);
        values.put(AuthCacheSqlHelper.Column.SESSION_EXPIRY.getName(), authCache._sessionExpiry);

        AuthCacheSqlHelper helper = new AuthCacheSqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.update(AuthCacheSqlHelper.TABLE_NAME, values, AuthCacheSqlHelper.Column.ID + "=" + authCache._id, null);
        } finally {
            helper.close();
        }
    }
}
