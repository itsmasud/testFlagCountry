package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.GlobalState;
import com.fieldnation.json.JsonArray;
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
 * Created by Michael on 4/9/2015.
 */
public class DeliverableTransactionHandler extends WebTransactionHandler implements WorkorderConstants {

    public static byte[] pChange(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pChange");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pGet(long workorderId, long deliverableId) {
        try {
            JsonObject obj = new JsonObject("action", "pGet");
            obj.put("workorderId", workorderId);
            obj.put("deliverableId", deliverableId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pList(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pList");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pDownload(long workorderId, long deliverableId, String url) {
        try {
            JsonObject obj = new JsonObject("action", "pDownload");
            obj.put("workorderId", workorderId);
            obj.put("deliverableId", deliverableId);
            obj.put("url", url);
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
                case "pChange":
                    return handleChange(context, transaction, resultData, params);
                case "pGet":
                    return handleGet(context, transaction, resultData, params);
                case "pDownload":
                    return handleDownload(context, transaction, resultData, params);
                case "pList":
                    return handleList(context, transaction, resultData, params);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.REQUEUE;
        }
        return Result.FINISH;
    }

    public Result handleChange(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");

        WorkorderTransactionBuilder.get(context, workorderId, false);

        return Result.FINISH;
    }

    public Result handleGet(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long deliverableId = params.getLong("deliverableId");
        byte[] data = resultData.getByteArray();

        StoredObject.put(context, PSO_DELIVERABLE, deliverableId, data);

        WorkorderDispatch.getDeliverable(context, new JsonObject(data), workorderId, deliverableId, transaction.isSync());

        return Result.FINISH;
    }

    public Result handleList(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");
        byte[] data = resultData.getByteArray();

        StoredObject.put(context, PSO_DELIVERABLE_LIST, workorderId, data);

        WorkorderDispatch.listDeliverables(context, new JsonArray(data), workorderId, transaction.isSync());

        return Result.FINISH;
    }

    public Result handleDownload(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException, IOException {
        long workorderId = params.getLong("workorderId");
        long deliverableId = params.getLong("deliverableId");

        if (resultData.isFile()) {
            StoredObject obj = StoredObject.put(context, PSO_DELIVERABLE_FILE, deliverableId, resultData.getFile());
            resultData.getFile().delete();
            WorkorderDispatch.downloadDeliverable(context, workorderId, deliverableId, obj.getFile(), transaction.isSync());
        } else {
            File tempFolder = new File(GlobalState.getContext().getStoragePath() + "/temp");
            tempFolder.mkdirs();
            File tempFile = File.createTempFile("tmp", "dat", tempFolder);
            FileOutputStream fout = new FileOutputStream(tempFile, false);
            fout.write(resultData.getByteArray());
            fout.close();

            StoredObject obj = StoredObject.put(context, PSO_DELIVERABLE_FILE, deliverableId, tempFile);
            tempFile.delete();
            WorkorderDispatch.downloadDeliverable(context, workorderId, deliverableId, obj.getFile(), transaction.isSync());
        }

        return Result.FINISH;
    }
}
