package com.fieldnation.rpc.dbclient;

import android.content.Context;
import android.os.ResultReceiver;

/**
 * Created by Michael Carver on 2/25/2015.
 */
public class DbService {
    private Context _context;
    private ResultReceiver _callback;
    private String _authToken;
    private String _username;

    public DbService(Context context, String username, String authToken, ResultReceiver callback) {
        _context = context.getApplicationContext();
        _username = username;
        _authToken = authToken;
        _callback = callback;
    }


}
