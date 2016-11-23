package com.fieldnation.service.data.documents;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentTransactionListener extends WebTransactionListener implements DocumentConstants {
    private static final String TAG = "DocumentTransactionListener";

    public static byte[] pDownload(long documentId, String filename) {
        try {
            JsonObject obj = new JsonObject("action", "pDownload");
            obj.put("documentId", documentId);
            obj.put("filename", filename);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    // entry points
    @Override
    public void onStart(Context context, WebTransaction transaction) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    onStartDownload(context, transaction, params);
                    break;
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public void onStartDownload(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        long documentId = params.getLong("documentId");
        DocumentDispatch.download(context, documentId, null, PARAM_STATE_START, transaction.isSync());
    }

    @Override
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    return onDownload(context, result, transaction, params, httpResult, throwable);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.DELETE;
        }
        return result;
    }

    private Result onDownload(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException, IOException {
        String filename = params.getString("filename");
        long documentId = params.getLong("documentId");

        if (result == Result.CONTINUE) {
            StoredObject obj = null;
            if (httpResult.isFile()) {
                obj = StoredObject.put(context, App.getProfileId(), PSO_DOCUMENT, documentId, httpResult.getFile(), filename);
            } else {
                obj = StoredObject.put(context, App.getProfileId(), PSO_DOCUMENT, documentId, httpResult.getByteArray(), filename);
            }

            String name = obj.getFile().getName();
            name = name.substring(name.indexOf("_") + 1);
            File dlFolder = new File(App.get().getDownloadsFolder() + "/" + name);
            if (!dlFolder.exists())
                FileUtils.copyFile(obj.getFile(), dlFolder);

            DocumentDispatch.download(context, documentId, dlFolder, PARAM_STATE_FINISH,
                    transaction.isSync());

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            DocumentDispatch.download(context, documentId, null, PARAM_STATE_FINISH, transaction.isSync());
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }
}

