package com.fieldnation.rpc.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.fieldnation.Log;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.utils.misc;

import java.text.ParseException;
import java.util.HashMap;

public class WebRpc extends RpcInterface implements WebServiceConstants {
    private static final String TAG = "rpc.server.WebRpc";

    public WebRpc(HashMap<String, RpcInterface> map) {
        super(map, ACTION_NAME);
    }

    @Override
    public void execute(Context context, Intent intent) {
        String method = intent.getStringExtra(KEY_METHOD);
        String authToken = intent.getStringExtra(KEY_PARAM_AUTH_TOKEN);
        String username = intent.getStringExtra(KEY_PARAM_USERNAME);
        String errorMessage = null;
        String errorType = ERROR_NONE;

        // Log.v(TAG, "username " + username);

        if (misc.isEmptyOrNull(username)) {
            doHttpError(context, intent, ERROR_NO_USERNAME, "No username supplied!");
            return;
        }


        AuthToken auth = null;

        // look up user in the cache
        AuthCache authCache = AuthCache.get(context, username);

        // not found
        if (authCache == null) {
            errorType = ERROR_USER_NOT_FOUND;
            errorMessage = "Could not find the user.";
        } else {
            // validate session
            if (authToken != null) {
                // Log.v(TAG, authToken);
            } else {
                // Log.v(TAG, "null authToken");
            }
            if (authCache.validateSessionHash(authToken)) {
                try {
                    auth = AuthToken.fromCache(authCache.getOAuthBlob());
                } catch (ParseException e) {
                    // this is handled below when we check for auth != null
                    e.printStackTrace();
                }
            } else {
                errorType = ERROR_SESSION_INVALID;
                errorMessage = "The authtoken is invalid or has expired.";
            }
        }

        if (auth != null) {
            if (METHOD_HTTP_READ.equals(method)) {
                new Thread(new HttpReadRunnable(context, intent, auth)).start();
                // doHttpRead(context, intent, auth);
            } else if (METHOD_HTTP_WRITE.equals(method)) {
                new Thread(new HttpWriteRunnable(context, intent, auth)).start();
                // doHttpWrite(context, intent, auth);
            } else if (METHOD_HTTP_POST_FILE.equals(method)) {
                new Thread(new HttpPostFileRunnable(context, intent, auth)).start();
            }
        } else {
            doHttpError(context, intent, errorType, errorMessage);
        }
    }

    private void doHttpError(Context context, Intent intent, String errorType, String message) {
        Bundle bundle = intent.getExtras();

        if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
            ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

            bundle.putString(KEY_RESPONSE_ERROR_TYPE, errorType);
            bundle.putString(KEY_RESPONSE_ERROR, message);
            rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
        }
    }

    private void doHttpRead(Context context, Intent intent, AuthToken at) {
        Bundle bundle = intent.getExtras();
        String method = bundle.getString(KEY_PARAM_METHOD);
        String path = bundle.getString(KEY_PARAM_PATH);
        String options = bundle.getString(KEY_PARAM_OPTIONS);
        String contentType = bundle.getString(KEY_PARAM_CONTENT_TYPE);
        boolean allowCache = bundle.getBoolean(KEY_ALLOW_CACHE);

        Log.v(TAG, "doHttpRead");
        if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
            ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

            WebDataCacheNode cachedData = null;

            cachedData = WebDataCache.query(context, at, bundle);

            if (allowCache && cachedData != null) {
                Log.v(TAG, "Cached Response");
                bundle.putByteArray(KEY_RESPONSE_DATA, cachedData.getResponseData());
                bundle.putInt(KEY_RESPONSE_CODE, cachedData.getResponseCode());
                bundle.putBoolean(KEY_RESPONSE_CACHED, true);
                bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
            } else {
                Log.v(TAG, "Atempting web request");
                try {
                    HttpResult result = Http.read(method, at.getHostname(), path, at.applyToUrlOptions(options), contentType);

                    try {
                        // happy path
                        bundle.putByteArray(KEY_RESPONSE_DATA, result.getResultsAsByteArray());
                        bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                        bundle.putBoolean(KEY_RESPONSE_CACHED, false);
                        bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
                        WebDataCache.store(context, at, bundle, bundle.getByteArray(KEY_RESPONSE_DATA),
                                bundle.getInt(KEY_RESPONSE_CODE));
                        Log.v(TAG, "web request success");
                    } catch (Exception ex) {
                        try {
                            // unhappy, but http error
                            bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                            bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_HTTP_ERROR);
                            bundle.putString(KEY_RESPONSE_ERROR, result.getResponseMessage());
                        } catch (Exception ex1) {
                            // sad path
                            bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
                            bundle.putString(KEY_RESPONSE_ERROR, ex1.getMessage());

                        }
                    }

                } catch (Exception ex) {
                    Log.v(TAG, "web request fail");
                    bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NETWORK_ERROR);
                    bundle.putString(KEY_RESPONSE_ERROR, ex.getMessage());
                }
            }
            rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
        }

    }

    private void doHttpWrite(Context context, Intent intent, AuthToken at) {
        Bundle bundle = intent.getExtras();
        String method = bundle.getString(KEY_PARAM_METHOD);
        String path = bundle.getString(KEY_PARAM_PATH);
        String options = bundle.getString(KEY_PARAM_OPTIONS);
        String contentType = bundle.getString(KEY_PARAM_CONTENT_TYPE);
        byte[] data = bundle.getByteArray(KEY_PARAM_DATA);
        boolean allowCache = bundle.getBoolean(KEY_ALLOW_CACHE);

        if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
            ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

            WebDataCacheNode cachedData = null;

            if (allowCache)
                cachedData = WebDataCache.query(context, at, bundle);

            if (cachedData != null) {
                bundle.putByteArray(KEY_RESPONSE_DATA, cachedData.getResponseData());
                bundle.putInt(KEY_RESPONSE_CODE, cachedData.getResponseCode());
                bundle.putBoolean(KEY_RESPONSE_CACHED, true);
            } else {
                HttpResult result = null;
                try {
                    result = Http.readWrite(method, at.getHostname(), path, at.applyToUrlOptions(options), data, contentType);

                    try {
                        // happy path
                        bundle.putByteArray(KEY_RESPONSE_DATA, result.getResultsAsByteArray());
                        bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                        bundle.putBoolean(KEY_RESPONSE_CACHED, false);
                        bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
                        WebDataCache.store(context, at, bundle, bundle.getByteArray(KEY_RESPONSE_DATA),
                                bundle.getInt(KEY_RESPONSE_CODE));
                        Log.v(TAG, "web request success");
                    } catch (Exception ex) {
                        try {
                            // unhappy, but http error
                            bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
                            bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_HTTP_ERROR);
                            bundle.putString(KEY_RESPONSE_ERROR, result.getResponseMessage());
                        } catch (Exception ex1) {
                            // sad path
                            bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
                            bundle.putString(KEY_RESPONSE_ERROR, ex1.getMessage());
                        }
                    }

                } catch (Exception ex) {
                    Log.v(TAG, "web request fail");
                    bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
                    bundle.putString(KEY_RESPONSE_ERROR, ex.getMessage());
                    if (result != null) {
                        bundle.putLong(KEY_RESPONSE_CODE, result.getResponseCode());
                    }
                }
            }

            rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
        }

    }
}
