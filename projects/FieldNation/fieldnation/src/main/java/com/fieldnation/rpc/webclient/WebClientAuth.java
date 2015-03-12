package com.fieldnation.rpc.webclient;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.auth.OAuth;

/**
 * <p>
 * Provides a basic interface for making webservice calls to fieldnation's
 * servers. Other webservices like {@link WorkorderWebClient} inherit from this
 * class.
 * <p/>
 * Note, you need to manually set the context after this is loaded as a parcel
 * to be useable
 * </p>
 *
 * @author michael.carver
 */
public class WebClientAuth extends WebClient {
    private OAuth _token;

    /**
     * Creates a handle to communicate with the web server
     *
     * @param context
     * @param callback Recommend using {@link com.fieldnation.rpc.common.WebResultReceiver}, however any
     *                 {@link android.os.ResultReceiver} will do.
     */
    public WebClientAuth(Context context, OAuth token, ResultReceiver callback) {
        super(context, callback);
        _token = token;
    }

    @Override
    public Intent http(int resultCode, JsonObject request, boolean allowCache) {
        try {
            _token.applyToRequest(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Intent intent = super.http(resultCode, request, allowCache);

        return intent;
    }

    public Intent httpRead(int resultCode, String method, String path, String options, boolean allowCache) {
        return super.httpRead(resultCode, _token.getHost(), method, path, options, allowCache);
    }

    public Intent httpWrite(int resultCode, String method, String path, String options, byte[] data, String contentType, boolean allowCache) {
        return super.httpWrite(resultCode, _token.getHost(), method, path, options, data, contentType, allowCache);
    }

    public Intent httpWrite(int resultCode, String method, String path, String options, String data, String contentType, boolean allowCache) {
        return super.httpWrite(resultCode, _token.getHost(), method, path, options, data, contentType, allowCache);
    }

    public Intent httpGet(int resultCode, String path, boolean allowCache) {
        return httpGet(resultCode, _token.getHost(), path, null, allowCache);
    }

    public Intent httpGet(int resultCode, String path, String options, boolean allowCache) {
        return httpRead(resultCode, "GET", _token.getHost(), path, options, allowCache);
    }

    public Intent httpDelete(int resultCode, String path, String options, boolean allowCache) {
        return httpRead(resultCode, "DELETE", _token.getHost(), path, options, allowCache);
    }

    public Intent httpPost(int resultCode, String path, String options, String data, String contentType,
                           boolean allowCache) {
        return httpPost(resultCode, _token.getHost(), path, options, data.getBytes(), contentType, allowCache);
    }

    public Intent httpPost(int resultCode, String path, String options, byte[] data, String contentType,
                           boolean allowCache) {
        return httpWrite(resultCode, "POST", _token.getHost(), path, options, data, contentType, allowCache);
    }

    public Intent httpPut(int resultCode, String path, String options, byte[] data, String contentType,
                          boolean allowCache) {
        return httpWrite(resultCode, "PUT", _token.getHost(), path, options, data, contentType, allowCache);
    }

}