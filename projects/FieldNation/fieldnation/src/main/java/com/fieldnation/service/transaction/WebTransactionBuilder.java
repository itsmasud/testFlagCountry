package com.fieldnation.service.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;

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

    public WebTransactionBuilder(Context context) {
        this.context = context;
        intent = new Intent(context, WebTransactionService.class);
        intent.putExtra(PARAM_PRIORITY, Priority.NORMAL);
        intent.putExtra(PARAM_IS_SYNC, false);
    }

    public static WebTransactionBuilder builder(Context context) {
        return new WebTransactionBuilder(context);
    }

    // Intent
    public WebTransactionBuilder priority(Priority priority) {
        intent.putExtra(PARAM_PRIORITY, priority);
        return this;
    }

    public WebTransactionBuilder key(String key) {
        intent.putExtra(PARAM_KEY, key);
        return this;
    }

    public WebTransactionBuilder useAuth(boolean use) {
        intent.putExtra(PARAM_USE_AUTH, use);
        return this;
    }

    public WebTransactionBuilder isSyncCall(boolean sync) {
        intent.putExtra(PARAM_IS_SYNC, sync);
        if (sync)
            intent.putExtra(PARAM_PRIORITY, Priority.LOW);
        return this;
    }

    public WebTransactionBuilder handler(Class<? extends WebTransactionHandler> clazz) {
        intent.putExtra(PARAM_HANDLER_NAME, clazz.getName());
        return this;
    }

    public WebTransactionBuilder handlerParams(byte[] params) {
        intent.putExtra(PARAM_HANDLER_PARAMS, params);
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
        context.startService(makeIntent());
    }

    public Intent makeIntent() {
        if (request != null) {
            intent.putExtra(PARAM_REQUEST, request.toByteArray());
        }

        if (transforms != null) {
            Parcelable[] p = new Parcelable[transforms.size()];
            for (int i = 0; i < transforms.size(); i++) {
                p[i] = transforms.get(i);
            }
            intent.putExtra(PARAM_TRANSFORM_LIST, p);
        }
        return intent;
    }

}
