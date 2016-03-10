package com.fieldnation.service.data.documents;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;
import com.fieldnation.utils.FileUtils;
import com.fieldnation.utils.misc;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentTransactionHandler extends WebTransactionHandler implements DocumentConstants {
    private static final String TAG = "DocumentTransactionHandler";

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
    public Result handleStart(Context context, WebTransaction transaction) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    return handleDownloadStart(context, transaction, params);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.ERROR;
        }
        return Result.FINISH;
    }

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    return handleDownloadFinish(context, transaction, params, resultData);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.ERROR;
        }
        return Result.FINISH;
    }

    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    return handleDownloadFail(context, transaction, params, resultData);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.ERROR;
        }
        return Result.FINISH;
    }


    // handlers
    public Result handleDownloadStart(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        String filename = params.getString("filename");
        long documentId = params.getLong("documentId");

        DocumentDispatch.download(context, documentId, null, PARAM_STATE_START, transaction.isSync());

        return Result.FINISH;
    }


    private Result handleDownloadFinish(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException, IOException {
        String filename = params.getString("filename");
        long documentId = params.getLong("documentId");

        StoredObject obj = null;
        if (resultData.isFile()) {
            obj = StoredObject.put(App.getProfileId(), PSO_DOCUMENT, documentId, resultData.getFile(), filename);
        } else {
            obj = StoredObject.put(App.getProfileId(), PSO_DOCUMENT, documentId, resultData.getByteArray(), filename);
        }

        String name = obj.getFile().getName();
        name = name.substring(name.indexOf("_") + 1);
        File dlFolder = new File(App.get().getDownloadsFolder() + "/" + name);
        if (!dlFolder.exists())
            FileUtils.copyFile(obj.getFile(), dlFolder);

        DocumentDispatch.download(context, documentId, dlFolder, PARAM_STATE_FINISH,
                transaction.isSync());

        return Result.FINISH;
    }


    private Result handleDownloadFail(Context context, WebTransaction transaction, JsonObject params,
                                      HttpResult resultData) throws ParseException {
        DocumentDispatch.download(context, params.getLong("documentId"), null, PARAM_STATE_FINISH, transaction.isSync());

        return Result.FINISH;
    }
}

