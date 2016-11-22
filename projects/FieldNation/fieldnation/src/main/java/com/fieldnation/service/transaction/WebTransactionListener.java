package com.fieldnation.service.transaction;

import android.content.Context;
import android.widget.Toast;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.auth.AuthTopicClient;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLProtocolException;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public abstract class WebTransactionListener {
    private static final String TAG = "WebTransactionListener";

    public enum Result {RETRY, CONTINUE, DELETE}

    public void onStart(Context context, WebTransaction transaction) {
    }

    protected final Result preComplete(Context context, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        Result result = preCheck(context, transaction, httpResult, throwable);
        if (result != Result.CONTINUE) {
            try {
                if (!httpResult.isFile() && httpResult.getByteArray().length < 1024) {
                    String errorString = httpResult.getString();
                    if (errorString != null) {
                        Log.v(TAG, errorString);
                        Log.v(TAG, new TransactionException(errorString));
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
        return onComplete(context, result, transaction, httpResult, throwable);
    }

    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        return result;
    }

    private Result preCheck(Context context, WebTransaction transaction, HttpResult result, Throwable throwable) {
        JsonObject request = null;
        try {
            request = new JsonObject(transaction.getRequestString());
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.DELETE;
        }

        if (result != null) {
            if (!result.isFile() && (result.getString() != null && result.getString().contains("You must provide a valid OAuth token to make a request"))) {
                Log.v(TAG, "Reauth");
                AuthTopicClient.invalidateCommand(context);
                AuthTopicClient.requestCommand(context);
                return Result.RETRY;

            } else if (result.getResponseCode() == 400) {
                // Bad request
                // need to report this
                // need to re-auth?
                if (result.getResponseMessage().contains("Bad Request")) {
                    return Result.DELETE;

                } else {
                    Log.v(TAG, "1");
                    AuthTopicClient.invalidateCommand(context);
                    AuthTopicClient.requestCommand(context);
                    return Result.RETRY;
                }

            } else if (result.getResponseCode() == 401) {
                // 401 usually means bad auth token
                if (HttpJsonBuilder.isFieldNation(request)) {
                    Log.v(TAG, "Reauth 2");
                    AuthTopicClient.invalidateCommand(context);
                    AuthTopicClient.requestCommand(context);
                    return Result.RETRY;

                } else {
                    return Result.DELETE;
                }

            } else if (result.getResponseCode() == 404) {
                // not found?... error
                return Result.DELETE;

            } else if (result.getResponseCode() == 413) {
                ToastClient.toast(context, "File too large to upload", Toast.LENGTH_LONG);
                return Result.DELETE;

                // usually means code is being updated on the server
            } else if (result.getResponseCode() == 502) {
                Log.v(TAG, "2");
                return Result.RETRY;

            } else if (result.getResponseCode() / 100 != 2) {
                Log.v(TAG, "3");
                return Result.DELETE;
            }
        }

        if (throwable != null) {
            if (throwable instanceof MalformedURLException || throwable instanceof FileNotFoundException) {
                return Result.DELETE;

            } else if (throwable instanceof SecurityException || throwable instanceof UnknownHostException) {
                return Result.DELETE;

            } else if (throwable instanceof SSLProtocolException || throwable instanceof ConnectException || throwable instanceof SocketTimeoutException || throwable instanceof EOFException) {
                return Result.RETRY;

            } else if (throwable instanceof SSLException) {
                if (throwable.getMessage().contains("Broken pipe")) {
                    Log.v(TAG, "6");
                    return Result.RETRY;

                } else {
                    Log.v(TAG, "7");
                    return Result.RETRY;
                }
            } else if (throwable instanceof IOException) {
                return Result.RETRY;

            } else if (throwable instanceof Exception) {
                Log.v(TAG, "9");
                Log.v(TAG, throwable);
                if (throwable.getMessage() != null && throwable.getMessage().contains("ETIMEDOUT")) {
                    return Result.RETRY;

                } else {
                    // no freaking clue
                    Log.logException(throwable);
                    Log.v(TAG, throwable);
                    return Result.DELETE;
                }
            }
        }

        return Result.CONTINUE;
    }

    public void onProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
    }
}
