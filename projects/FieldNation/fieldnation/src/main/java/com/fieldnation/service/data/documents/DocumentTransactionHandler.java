package com.fieldnation.service.data.documents;

import android.content.Context;

import com.fieldnation.GlobalState;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.io.File;
import java.io.FileOutputStream;
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
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDownload":
                    return handleDownload(context, transaction, params, resultData);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.ERROR;
        }
        return Result.FINISH;
    }

    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            DocumentDispatch.download(context, params.getLong("documentId"), null, true, transaction.isSync());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.FINISH;
    }

    private Result handleDownload(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException, IOException {
        String filename = params.getString("filename");
        File file = null;
        if (resultData.isFile()) {
            file = resultData.getFile();
        } else {
            file = new File(GlobalState.getContext().getStoragePath() + "/temp");
            file.mkdirs();
            file = File.createTempFile("web", "dat", file);
            FileOutputStream fout = new FileOutputStream(file, false);
            fout.write(resultData.getByteArray());
            fout.close();
        }

        long documentId = params.getLong("documentId");

        StoredObject obj = StoredObject.put(context, PSO_DOCUMENT, documentId, file, filename);

        DocumentDispatch.download(context, documentId, obj.getFile(), false, transaction.isSync());

        return Result.FINISH;
    }
}

