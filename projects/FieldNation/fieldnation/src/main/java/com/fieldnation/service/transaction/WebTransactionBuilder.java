package com.fieldnation.service.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.handlers.WebTransactionHandler;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WebTransactionBuilder implements WebTransactionConstants {

    private Context context;
    private Intent intent;
    private List<Bundle> transforms;

    private JsonObject request;
    private JsonObject meta;

    public WebTransactionBuilder(Context context) {
        this.context = context;
        intent = new Intent(context, WebTransactionService.class);
        intent.putExtra(PARAM_PRIORITY, WebTransaction.Priority.NORMAL);
    }

    public static WebTransactionBuilder builder(Context context) {
        return new WebTransactionBuilder(context);
    }

    // Intent
    public WebTransactionBuilder priority(WebTransaction.Priority priority) {
        intent.putExtra(PARAM_PRIORITY, priority.ordinal());
        return this;
    }

    public WebTransactionBuilder handler(Class<? extends WebTransactionHandler> clazz) {
        intent.putExtra(PARAM_HANDLER_NAME, clazz.getName());
        return this;
    }

    public WebTransactionBuilder key(String key) {
        intent.putExtra(PARAM_KEY, key);
        return this;
    }

    // Extras
    private void getMetas() throws ParseException {
        if (meta == null) {
            meta = new JsonObject();
        }
    }

    public WebTransactionBuilder meta(String key, Object value) throws ParseException {
        getMetas();
        meta.put(key, value);
        return this;
    }


    // transforms
    private void getTransforms() {
        if (transforms == null) {
            transforms = new LinkedList<>();
        }
    }

    public WebTransactionBuilder transform(Bundle transform) {
        getTransforms();
        transforms.add(transform);
        return this;
    }

    public WebTransactionBuilder request(HttpJsonBuilder builder) {
        request = builder.build();
        return this;
    }


    public void send() {
        if (request != null) {
            intent.putExtra(PARAM_REQUEST, request.toByteArray());
        }

        if (meta != null) {
            intent.putExtra(PARAM_META, meta.toByteArray());
        }

        if (transforms != null) {
            intent.putExtra(PARAM_TRANSFORM_LIST, (Parcelable[]) transforms.toArray());
        }

        context.startService(intent);
    }

}
