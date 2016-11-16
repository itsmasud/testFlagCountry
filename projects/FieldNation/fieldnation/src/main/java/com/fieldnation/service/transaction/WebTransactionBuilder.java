package com.fieldnation.service.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/6/2015.
 */
class WebTransactionBuilder implements WebTransactionConstants {

    private final Context context;
    private final Intent intent;
    private List<Bundle> transforms;

    private JsonObject request;

    public WebTransactionBuilder(Context context) {
        this.context = context;
        intent = new Intent(context, WebTransactionService.class);
        intent.putExtra(PARAM_PRIORITY, Priority.NORMAL);
        intent.putExtra(PARAM_IS_SYNC, false);
        intent.putExtra(PARAM_WIFI_REQUIRED, false);
        intent.putExtra(PARAM_TRACK, false);
        intent.putExtra(PARAM_NOTIFICATION_ID, -1);
        intent.putExtra(PARAM_NOTIFICATION_START, (byte[]) null);
        intent.putExtra(PARAM_NOTIFICATION_SUCCESS, (byte[]) null);
        intent.putExtra(PARAM_NOTIFICATION_FAILED, (byte[]) null);
        intent.putExtra(PARAM_NOTIFICATION_RETRY, (byte[]) null);
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

    public WebTransactionBuilder setWifiRequired(boolean required) {
        intent.putExtra(PARAM_WIFI_REQUIRED, required);
        return this;
    }

    public WebTransactionBuilder setTrack(boolean track) {
        intent.putExtra(PARAM_TRACK, track);
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

    public WebTransactionBuilder timingKey(String timingKey) throws ParseException {
        intent.putExtra(PARAM_TIMING_KEY, timingKey);
        return this;
    }

    public WebTransactionBuilder notify(NotificationDefinition start, NotificationDefinition success,
                                        NotificationDefinition failed, NotificationDefinition retry) throws ParseException {
        intent.putExtra(PARAM_NOTIFICATION_ID, App.secureRandom.nextInt(Integer.MAX_VALUE));
        intent.putExtra(PARAM_NOTIFICATION_START, start.toJson().toByteArray());
        intent.putExtra(PARAM_NOTIFICATION_SUCCESS, success.toJson().toByteArray());
        intent.putExtra(PARAM_NOTIFICATION_FAILED, failed.toJson().toByteArray());
        intent.putExtra(PARAM_NOTIFICATION_RETRY, retry.toJson().toByteArray());

        return this;
    }

    // transforms
    private void makeTransforms() {
        if (transforms == null) {
            transforms = new LinkedList<>();
        }
    }

    public WebTransactionBuilder transform(Bundle transform) {
        makeTransforms();
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
