package com.fieldnation.service.data.documents;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentTransactionListener extends WebTransactionListener implements DocumentConstants {
    private static final String TAG = "DocumentTransactionListener";

    public static byte[] pDownload(int documentId, String filename) {
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
        int documentId = params.getInt("documentId");
        DocumentDispatch.download(context, documentId, null, PARAM_STATE_START, transaction.getType() == WebTransaction.Type.SYNC);
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
        int documentId = params.getInt("documentId");

        if (result == Result.CONTINUE) {
            StoredObject obj = null;
            if (httpResult.isFile()) {
                obj = StoredObject.put(context, App.getProfileId(), PSO_DOCUMENT, documentId, new FileInputStream(httpResult.getFile()), filename);
            } else {
                obj = StoredObject.put(context, App.getProfileId(), PSO_DOCUMENT, documentId, httpResult.getByteArray(), filename);
            }

            if (transaction.getType() == WebTransaction.Type.CRAWLER) {
                DocumentDispatch.download(context, documentId, new File(obj.getUri().toString()), DocumentConstants.PARAM_STATE_FINISH, true);
            } else {

                Uri uri = obj.getUri();
                String name = FileUtils.getFileNameFromUri(context, uri);
                File dlFolder = new File(App.get().getDownloadsFolder() + "/" + name);
                if (!dlFolder.exists())
                    FileUtils.writeStream(context.getContentResolver().openInputStream(uri), dlFolder);

                DocumentDispatch.download(context, documentId, dlFolder, PARAM_STATE_FINISH, transaction.getType() == WebTransaction.Type.SYNC);
            }

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            DocumentDispatch.download(context, documentId, null, PARAM_STATE_FINISH, transaction.getType() == WebTransaction.Type.SYNC);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }
}

