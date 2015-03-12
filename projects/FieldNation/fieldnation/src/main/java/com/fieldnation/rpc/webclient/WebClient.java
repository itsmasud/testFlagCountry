package com.fieldnation.rpc.webclient;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.WebService;
import com.fieldnation.service.objectstore.StoredObject;

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
public class WebClient implements WebServiceConstants {
    protected Context _context;
    protected ResultReceiver _callback;

    /**
     * Creates a handle to communicate with the web server
     *
     * @param context
     * @param callback Recommend using {@link com.fieldnation.rpc.common.WebResultReceiver}, however any
     *                 {@link ResultReceiver} will do.
     */
    public WebClient(Context context, ResultReceiver callback) {
        _context = context.getApplicationContext();
        _callback = callback;
    }

    public Intent http(int resultCode, JsonObject request, boolean allowCache) {
        Intent intent = new Intent(_context, WebService.class);
        intent.putExtra(KEY_PARAM_RESULT_CODE, resultCode);
        intent.putExtra(KEY_PARAM_REQUEST, request.toByteArray());
        intent.putExtra(KEY_PARAM_ALLOW_CACHE, allowCache);

        if (_callback != null) {
            intent.putExtra(KEY_PARAM_CALLBACK, _callback);
        }

        return intent;
    }

    public Intent httpWrite(int resultCode, String host, String method, String path, String options, byte[] data,
                            String contentType, boolean allowCache) {
        try {
            StoredObject obj = StoredObject.put(_context, "HttpBody", StoredObject.randomKey(), null, data);

            HttpJsonBuilder builder = new HttpJsonBuilder();
            builder.method(method)
                    .host(host)
                    .path(path)
                    .urlParams(options)
                    .body(obj)
                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, contentType);


            return http(resultCode, builder.build(), allowCache);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Intent httpWrite(int resultCode, String host, String method, String path, String options, String data,
                            String contentType, boolean allowCache) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder();
            builder.method(method)
                    .host(host)
                    .path(path)
                    .urlParams(options)
                    .body(data)
                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, contentType);


            return http(resultCode, builder.build(), allowCache);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Intent httpRead(int resultCode, String host, String method, String path, String options, boolean allowCache) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder();
            builder.method(method)
                    .host(host)
                    .path(path)
                    .urlParams(options);
            return http(resultCode, builder.build(), allowCache);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Intent httpGet(int resultCode, String host, String path, boolean allowCache) {
        return httpGet(resultCode, host, path, null, allowCache);
    }

    public Intent httpGet(int resultCode, String host, String path, String options, boolean allowCache) {
        return httpRead(resultCode, "GET", host, path, options, allowCache);
    }

    public Intent httpDelete(int resultCode, String host, String path, String options, boolean allowCache) {
        return httpRead(resultCode, "DELETE", host, path, options, allowCache);
    }

    public Intent httpPost(int resultCode, String host, String path, String options, String data, String contentType,
                           boolean allowCache) {
        return httpPost(resultCode, host, path, options, data.getBytes(), contentType, allowCache);
    }

    public Intent httpPost(int resultCode, String host, String path, String options, byte[] data, String contentType,
                           boolean allowCache) {
        return httpWrite(resultCode, "POST", host, path, options, data, contentType, allowCache);
    }

    public Intent httpPut(int resultCode, String host, String path, String options, byte[] data, String contentType,
                          boolean allowCache) {
        return httpWrite(resultCode, "PUT", host, path, options, data, contentType, allowCache);
    }

}