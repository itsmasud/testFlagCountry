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
    public Result onStart(Context context, WebTransaction transaction) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    return handleDownloadStart(context, transaction, params);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.DELETE;
        }
        return Result.CONTINUE;
    }

    @Override
    public Result onComplete(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    return handleDownloadFinish(context, transaction, params, resultData);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.DELETE;
        }
        return Result.CONTINUE;
    }

    @Override
    public Result onFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    return handleDownloadFail(context, transaction, params, resultData);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.DELETE;
        }
        return Result.CONTINUE;
    }


    // handlers
    public Result handleDownloadStart(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        String filename = params.getString("filename");
        long documentId = params.getLong("documentId");

        DocumentDispatch.download(context, documentId, null, PARAM_STATE_START, transaction.isSync());

        return Result.CONTINUE;
    }


    private Result handleDownloadFinish(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException, IOException {
        String filename = params.getString("filename");
        long documentId = params.getLong("documentId");

        StoredObject obj = null;
        if (resultData.isFile()) {
            obj = StoredObject.put(context, App.getProfileId(), PSO_DOCUMENT, documentId, resultData.getFile(), filename);
        } else {
            obj = StoredObject.put(context, App.getProfileId(), PSO_DOCUMENT, documentId, resultData.getByteArray(), filename);
        }

        String name = obj.getFile().getName();
        name = name.substring(name.indexOf("_") + 1);
        File dlFolder = new File(App.get().getDownloadsFolder() + "/" + name);
        if (!dlFolder.exists())
            FileUtils.copyFile(obj.getFile(), dlFolder);

        DocumentDispatch.download(context, documentId, dlFolder, PARAM_STATE_FINISH,
                transaction.isSync());

        return Result.CONTINUE;
    }


    private Result handleDownloadFail(Context context, WebTransaction transaction, JsonObject params,
                                      HttpResult resultData) throws ParseException {
        DocumentDispatch.download(context, params.getLong("documentId"), null, PARAM_STATE_FINISH, transaction.isSync());

        return Result.CONTINUE;
    }
}

