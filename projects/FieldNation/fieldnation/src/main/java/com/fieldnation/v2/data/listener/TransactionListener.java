package com.fieldnation.v2.data.listener;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.StreamUtils;
import com.fieldnation.service.tracker.UploadTrackerClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;
import com.fieldnation.v2.ui.workorder.WorkOrderActivity;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by mc on 1/27/17.
 */

public class TransactionListener extends WebTransactionListener {
    private static final String TAG = "TransactionListener";

    public static byte[] params(String topicId, Class<?> apiClass, String apiFunction) {
        return params(topicId, apiClass, apiFunction, null);
    }

    public static byte[] params(String topicId, Class<?> apiClass, String apiFunction, JsonObject methodParams) {
        try {
            TransactionParams params = new TransactionParams();

            params.topicId = topicId;
            params.apiClassName = apiClass.getName();
            params.apiFunction = apiFunction;
            params.methodParams = methodParams.toString();

            return params.toJson().toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public void onQueued(Context context, WebTransaction transaction) {
        try {
            TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));
            Bundle bundle = new Bundle();
            bundle.putParcelable("params", params);
            bundle.putString("type", "queued");
            PigeonRoost.sendMessage(params.topicId, bundle, Sticky.NONE);

            if (transaction.isTracked()) {
                UploadTrackerClient.uploadQueued(context, transaction.getTrackType());
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    @Override
    public void onStart(Context context, WebTransaction transaction) {
        try {
            TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));
            Bundle bundle = new Bundle();
            bundle.putParcelable("params", params);
            bundle.putString("type", "start");
            PigeonRoost.sendMessage(params.topicId, bundle, Sticky.NONE);

            if (transaction.isTracked()) {
                UploadTrackerClient.uploadStarted(context, transaction.getTrackType());
            }

            Tracker.event(App.get(),
                    new SimpleEvent.Builder()
                            .tag(AnswersWrapper.TAG)
                            .category(params.apiFunction)
                            .action("START")
                            .build());

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    @Override
    public void onPaused(Context context, WebTransaction transaction) {
        try {
            TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));
            Bundle bundle = new Bundle();
            bundle.putParcelable("params", params);
            bundle.putString("type", "paused");
            PigeonRoost.sendMessage(params.topicId, bundle, Sticky.NONE);

            if (transaction.isTracked()) {
                UploadTrackerClient.uploadRequeued(context, transaction.getTrackType());
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    @Override
    public void onProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
        try {
            TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));
            Bundle bundle = new Bundle();
            bundle.putParcelable("params", params);
            bundle.putString("type", "progress");
            bundle.putLong("pos", pos);
            bundle.putLong("size", size);
            bundle.putLong("time", time);
            PigeonRoost.sendMessage(params.topicId, bundle, Sticky.NONE);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    @Override
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        //Log.v(TAG, "onComplete");
        if (result == Result.CONTINUE) {
            //Log.v(TAG, "onComplete - CONTINUE");
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));
                Log.v(TAG, "onComplete " + params.apiFunction + (transaction.getKey() != null ? transaction.getKey() : ""));
                Bundle bundle = new Bundle();
                bundle.putParcelable("params", params);

                if (httpResult.isFile()) {
                    //Log.v(TAG, "isFile true");
                    File file = httpResult.getFile();
                    byte[] raw = StreamUtils.readAllFromStream(new FileInputStream(file), (int) file.length(), 1000);
                    Log.v(TAG, "file size: " + raw.length);
                    bundle.putByteArray("data", raw);
                } else {
                    //Log.v(TAG, "isFile false");
                    bundle.putByteArray("data", httpResult.getByteArray());
                }
                bundle.putBoolean("success", true);
                bundle.putString("type", "complete");

                //Log.v(TAG, "topicId: " + params.topicId);
                PigeonRoost.sendMessage(params.topicId, bundle, Sticky.NONE);

                if (transaction.isTracked()) {
                    UploadTrackerClient.uploadSuccess(context, transaction.getTrackType());
                }

                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .tag(AnswersWrapper.TAG)
                                .category(params.apiFunction)
                                .action("COMPLETE")
                                .build());

                String method = new JsonObject(transaction.getRequestString()).getString("method");
                if (method.equals("GET")) {
                    StoredObject.put(context, App.getProfileId(), "V2_PARAMS", transaction.getKey(), params.toJson().toByteArray(), true);
                    if (httpResult.isFile()) {
                        StoredObject.put(context, App.getProfileId(), "V2_DATA", transaction.getKey(), new FileInputStream(httpResult.getFile()), transaction.getKey(), true);
                    } else {
                        StoredObject.put(context, App.getProfileId(), "V2_DATA", transaction.getKey(), httpResult.getByteArray(), true);
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
                // TODO error!
            }

            return Result.CONTINUE;
        } else if (result == Result.DELETE) {
            //Log.v(TAG, "onComplete - DELETE");
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));

                Bundle bundle = new Bundle();
                bundle.putParcelable("params", params);

                if (httpResult != null) {
                    if (httpResult.isFile()) {
                        File file = httpResult.getFile();
                        bundle.putByteArray("data", StreamUtils.readAllFromStream(new FileInputStream(file), (int) file.length(), 1000));
                    } else {
                        bundle.putByteArray("data", httpResult.getByteArray());
                    }
                } else {
                    bundle.putByteArray("data", (byte[]) null);
                }
                bundle.putBoolean("success", false);
                bundle.putString("type", "complete");

                PigeonRoost.sendMessage(params.topicId, bundle, Sticky.TEMP);

                try {
                    JsonObject methodParams = new JsonObject(params.methodParams);
                    if (transaction.isTracked()) {
                        if (methodParams.has("workOrderId")) {
                            Intent workorderIntent = WorkOrderActivity.makeIntentShow(App.get(), methodParams.getInt("workOrderId"));
                            PendingIntent pendingIntent = PendingIntent.getActivity(App.get(), App.secureRandom.nextInt(), workorderIntent, 0);
                            UploadTrackerClient.uploadFailed(context, transaction.getTrackType(), pendingIntent);
                        } else {
                            UploadTrackerClient.uploadFailed(context, transaction.getTrackType(), null);
                        }
                    }

                    Log.v(TAG, "Saving zombie transaction");
                    if (methodParams.has("allowZombie") && methodParams.getBoolean("allowZombie")) {
                        Tracker.event(App.get(),
                                new SimpleEvent.Builder()
                                        .tag(AnswersWrapper.TAG)
                                        .category(params.apiFunction)
                                        .action("ZOMBIE")
                                        .build());

                        return Result.ZOMBIE;
                    }

                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category(params.apiFunction)
                                    .action("FAIL")
                                    .build());

                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

            } catch (Exception ex) {
                Log.v(TAG, ex);
                // TODO error!
            }

            return Result.DELETE;
        } else if (result == Result.RETRY) {
            //Log.v(TAG, "onComplete - RETRY");
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));

                JsonObject methodParams = new JsonObject(params.methodParams);

                Log.v(TAG, "Saving zombie transaction");
                if (methodParams.has("allowZombie") && methodParams.getBoolean("allowZombie")) {
                    if (transaction.isTracked()) {
                        if (methodParams.has("workOrderId")) {
                            Intent workorderIntent = WorkOrderActivity.makeIntentShow(App.get(), methodParams.getInt("workOrderId"));
                            PendingIntent pendingIntent = PendingIntent.getActivity(App.get(), App.secureRandom.nextInt(), workorderIntent, 0);
                            UploadTrackerClient.uploadFailed(context, transaction.getTrackType(), pendingIntent);
                        } else {
                            UploadTrackerClient.uploadFailed(context, transaction.getTrackType(), null);
                        }
                    }
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category(params.apiFunction)
                                    .action("ZOMBIE")
                                    .build());

                    return Result.ZOMBIE;
                } else if (transaction.isTracked()) {
                    UploadTrackerClient.uploadRequeued(context, transaction.getTrackType());
                }

                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .tag(AnswersWrapper.TAG)
                                .category(params.apiFunction)
                                .action("RETRY")
                                .build());

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return Result.RETRY;
        }
        return super.onComplete(context, result, transaction, httpResult, throwable);
    }
}
