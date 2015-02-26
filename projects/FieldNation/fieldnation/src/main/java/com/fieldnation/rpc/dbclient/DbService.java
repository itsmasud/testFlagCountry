package com.fieldnation.rpc.dbclient;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.rpc.server.DatabaseService;

/**
 * Created by Michael Carver on 2/25/2015.
 */
public class DbService {
    protected Context _context;
    protected ResultReceiver _callback;
    protected String _authToken;
    protected String _username;

    public DbService(Context context, String username, String authToken, ResultReceiver callback) {
        _context = context.getApplicationContext();
        _username = username;
        _authToken = authToken;
        _callback = callback;
    }

    public Intent startWorkorderIntent(String objectName) {
        return new Intent(_context, DatabaseService.class);
    }
}
