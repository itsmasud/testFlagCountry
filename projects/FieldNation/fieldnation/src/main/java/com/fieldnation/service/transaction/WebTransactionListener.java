package com.fieldnation.service.transaction;

import android.content.Context;
import android.widget.Toast;

import com.fieldnation.AppMessagingClient;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.auth.AuthClient;

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

    public enum Result {RETRY, CONTINUE, DELETE, ZOMBIE}

    protected void preOnQueued(Context context, WebTransaction transaction) {
        onQueued(context, transaction);
    }

    public void onQueued(Context context, WebTransaction transaction) {
    }


    protected void preOnStart(Context context, WebTransaction transaction) {
        onStart(context, transaction);
    }

    public void onStart(Context context, WebTransaction transaction) {
    }


    protected void preOnPaused(Context context, WebTransaction transaction) {
        onPaused(context, transaction);
    }

    public void onPaused(Context context, WebTransaction transaction) {
    }


    protected final Result preComplete(Context context, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        Result result = preCheck(context, transaction, httpResult, throwable);
        Log.v(TAG, "Result: " + result.name());
        if (result != Result.CONTINUE) {
            try {
                if (httpResult != null && !httpResult.isFile() && httpResult.getByteArray().length < 1024) {
                    String errorString = httpResult.getString();
                    if (errorString != null) {
                        Log.v(TAG, "Error:  " + errorString);
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

    private Result preCheck(Context context, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        JsonObject request = null;
        try {
            request = new JsonObject(transaction.getRequestString());
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.DELETE;
        }

        if (httpResult != null) {
            if (!httpResult.isFile() && (httpResult.getString() != null && httpResult.getString().contains("You must provide a valid OAuth token to make a request"))) {
                Log.v(TAG, "Reauth");
                AuthClient.invalidateCommand();
                AuthClient.requestCommand();
                return Result.RETRY;

            } else if (httpResult.getResponseCode() == 400) {
                // Bad request
                // need to report this
                // need to re-auth?
                if (httpResult.getResponseMessage().contains("Bad Request")) {
                    if (httpResult.getString() != null
                            && httpResult.getString().toLowerCase().contains("invalid user")) {
                        AuthClient.removeCommand();
                        ToastClient.toast(context, "Invalid User, Cannot login", Toast.LENGTH_LONG);
                        return Result.DELETE;
                    } else {
                        return transaction.getType() == WebTransaction.Type.SYNC ? Result.ZOMBIE : Result.DELETE;
                    }
                } else {
                    Log.v(TAG, "1");
                    AuthClient.invalidateCommand();
                    AuthClient.requestCommand();
                    return Result.RETRY;
                }

            } else if (httpResult.getResponseCode() == 401) {
                // 401 usually means bad auth token
                if (HttpJsonBuilder.isFieldNation(request)) {
                    Log.v(TAG, "Reauth 2");
                    AuthClient.invalidateCommand();
                    AuthClient.requestCommand();
                    return Result.RETRY;

                } else {
                    return transaction.getType() == WebTransaction.Type.SYNC ? Result.ZOMBIE : Result.DELETE;
                }

            } else if (httpResult.getResponseCode() == 404) {
                // not found?... error
                return transaction.getType() == WebTransaction.Type.SYNC ? Result.ZOMBIE : Result.DELETE;

            } else if (httpResult.getResponseCode() == 413) {
                ToastClient.toast(context, "File too large to upload", Toast.LENGTH_LONG);
                return Result.DELETE;

                // usually means code is being updated on the server
            } else if (httpResult.getResponseCode() == 502 || httpResult.getResponseCode() == 503) {
                Log.v(TAG, "2");
                return Result.RETRY;

            } else if (httpResult.getResponseCode() / 100 != 2) {
                Log.v(TAG, "3");
                return transaction.getType() == WebTransaction.Type.SYNC ? Result.ZOMBIE : Result.DELETE;
            }
        }

        if (throwable != null) {
            Log.v(TAG, "throwable: " + throwable.getClass().getName());
            Log.v(TAG, "message: " + throwable.getMessage());

            if (throwable instanceof MalformedURLException || throwable instanceof FileNotFoundException) {
                return Result.DELETE;

            } else if (throwable instanceof UnknownHostException) {
                AppMessagingClient.networkDisconnected();
                return Result.RETRY;

            } else if (throwable instanceof SecurityException) {
                return transaction.getType() == WebTransaction.Type.SYNC ? Result.ZOMBIE : Result.DELETE;

            } else if (throwable instanceof SSLProtocolException
                    || throwable instanceof ConnectException
                    || throwable instanceof SocketTimeoutException
                    || throwable instanceof EOFException) {
                AppMessagingClient.networkDisconnected();
                return Result.RETRY;

            } else if (throwable instanceof SSLException) {
                if (throwable.getMessage().contains("Broken pipe")) {
                    Log.v(TAG, "6");
                    AppMessagingClient.networkDisconnected();
                    return Result.RETRY;

                } else {
                    Log.v(TAG, "7");
                    AppMessagingClient.networkDisconnected();
                    return Result.RETRY;
                }
            } else if (throwable instanceof IOException) {
                AppMessagingClient.networkDisconnected();
                return Result.RETRY;

            } else if (throwable instanceof Exception) {
                Log.v(TAG, "9");
                Log.v(TAG, throwable);
                if (throwable.getMessage() != null && throwable.getMessage().contains("ETIMEDOUT")) {
                    AppMessagingClient.networkDisconnected();
                    return Result.RETRY;

                } else {
                    // no freaking clue
                    Log.logException(throwable);
                    Log.v(TAG, throwable);
                    return transaction.getType() == WebTransaction.Type.SYNC ? Result.ZOMBIE : Result.DELETE;
                }
            }
        }

        return Result.CONTINUE;
    }


    protected void preOnProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
        onProgress(context, transaction, pos, size, time);
    }

    public void onProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
    }

    public static boolean haveErrorMessage(HttpResult httpResult) {
        return httpResult != null && !httpResult.isFile() && httpResult.getByteArray().length < 200;
    }

    public static String pickErrorMessage(HttpResult httpResult, String defaultMessage) {
        if (haveErrorMessage(httpResult))
            return httpResult.getString();

        return defaultMessage;
    }
}
