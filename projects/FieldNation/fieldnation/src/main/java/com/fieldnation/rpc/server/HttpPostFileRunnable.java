package com.fieldnation.rpc.server;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fieldnation.FileHelper;
import com.fieldnation.R;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebServiceConstants;

import java.io.File;
import java.io.FileInputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpPostFileRunnable extends HttpRunnable implements WebServiceConstants, FileHelper.Listener {
    private static final String TAG = "rpc.server.HttpPostFileRunnable";

    private int NOTIFICATION_ID = 1;

    private Bundle _bundle;
    private ResultReceiver _rr;
    private NotificationManager _noteManager;
    private NotificationCompat.Builder _noteBuilder;
    private SecureRandom _rand = new SecureRandom();
    private PendingIntent _responseIntent;


    public HttpPostFileRunnable(Context context, Intent intent, OAuth at) {
        super(context, intent, at);
    }

    @Override
    public void run() {
        NOTIFICATION_ID = _rand.nextInt();

        _bundle = _intent.getExtras();
        _rr = _bundle.getParcelable(KEY_PARAM_CALLBACK);
        _responseIntent = _bundle.getParcelable(KEY_PARAM_NOTIFICATION_INTENT);

        _noteBuilder = new NotificationCompat.Builder(_context)
                .setSmallIcon(R.drawable.ic_action_upload)
                .setContentTitle("Starting File Upload...")
                .setContentText("Getting file")
                .setContentIntent(_responseIntent);

        _noteManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        _noteManager.notify(NOTIFICATION_ID, _noteBuilder.build());

        Intent data = _bundle.getParcelable(KEY_PARAM_FILE_DATA_INTENT);
        FileHelper.getFileFromActivityResult(_context, data, this);
    }

    @Override
    public void fileReady(String filename, File file) {
        String path = _bundle.getString(KEY_PARAM_PATH);
        String options = _bundle.getString(KEY_PARAM_OPTIONS);
        String fieldName = _bundle.getString(KEY_PARAM_FILE_FIELD_NAME);
        String fieldMapString = _bundle.getString(KEY_PARAM_FIELD_MAP);
        Map<String, String> fields = null;

        if (fieldMapString != null) {
            try {
                JsonObject obj = new JsonObject(fieldMapString);
                fields = new HashMap<String, String>();

                Enumeration<String> keys = obj.keys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    fields.put(key, obj.getString(key));
                }

            } catch (Exception ex) {
            }
        }

        _noteBuilder.setContentTitle("Uploading " + filename)
                .setContentText("Uploading...");
        _noteManager.notify(NOTIFICATION_ID, _noteBuilder.build());

        Ws ws = new Ws(_auth);
        Result result = null;
        try {

            result = ws.httpPostFile(path, options, fieldName, filename, new FileInputStream(file),
                    (int) file.length(), fields);

            if (result.getResponseCode() / 100 != 2) {
                Log.v(TAG, "Error response: " + result.getResponseCode());
                _bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                _bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_HTTP_ERROR);
                _bundle.putString(KEY_RESPONSE_ERROR, result.getResponseMessage());
                _noteBuilder.setContentText("Failed!");
                _noteManager.notify(NOTIFICATION_ID, _noteBuilder.build());
            } else {
                try {
                    // happy path
                    _bundle.putByteArray(KEY_RESPONSE_DATA, result.getResultsAsByteArray());
                    _bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                    _bundle.putBoolean(KEY_RESPONSE_CACHED, false);
                    _bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
                    DataCache.store(_context, _auth, _bundle, _bundle.getByteArray(KEY_RESPONSE_DATA),
                            _bundle.getInt(KEY_RESPONSE_CODE));
                    Log.v(TAG, "web request success");
                    _noteBuilder.setContentText("Success!");
                    _noteManager.notify(NOTIFICATION_ID, _noteBuilder.build());
                } catch (Exception ex) {
                    _noteBuilder.setContentText("Failed!");
                    _noteManager.notify(NOTIFICATION_ID, _noteBuilder.build());
                    try {
                        // unhappy, but http error
                        _bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                        _bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_HTTP_ERROR);
                        _bundle.putString(KEY_RESPONSE_ERROR, result.getResponseMessage());
                    } catch (Exception ex1) {
                        // sad path
                        _bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
                        _bundle.putString(KEY_RESPONSE_ERROR, ex1.getMessage());
                    }
                }
            }

        } catch (Exception ex) {
            _noteBuilder.setContentText("Failed!");
            _noteManager.notify(NOTIFICATION_ID, _noteBuilder.build());
            Log.v(TAG, "web request fail");
            _bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
            _bundle.putString(KEY_RESPONSE_ERROR, ex.getMessage());
            if (result != null) {
                _bundle.putLong(KEY_RESPONSE_CODE, result.getResponseCode());
            }
        }
        _rr.send(_bundle.getInt(KEY_RESULT_CODE), _bundle);
    }

    @Override
    public void fail(String reason) {
        _noteBuilder.setContentText("Failed!");
        _noteManager.notify(NOTIFICATION_ID, _noteBuilder.build());
        Log.v(TAG, "get file failed");
        _bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
        _bundle.putString(KEY_RESPONSE_ERROR, reason);
        _rr.send(_bundle.getInt(KEY_RESULT_CODE), _bundle);
    }

}
