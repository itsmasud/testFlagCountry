package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.text.ParseException;

/**
 * Created by Michael on 4/9/2015.
 */
public class DeliverableTransactionListener extends WebTransactionListener implements WorkorderConstants {
    private static final String TAG = "DeliverableTransactionListener";

    public static byte[] pChange(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pChange");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }

//    public static byte[] pDownload(long workorderId, long deliverableId, String url) {
//        try {
//            JsonObject obj = new JsonObject("action", "pDownload");
//            obj.put("workorderId", workorderId);
//            obj.put("deliverableId", deliverableId);
//            obj.put("url", url);
//            return obj.toByteArray();
//        } catch (Exception ex) {
//            Log.v(TAG, ex);
//            return null;
//        }
//    }

    @Override
    public Result onComplete(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pChange":
                    return handleChange(context, transaction, resultData, params);
                case "pGet":
                    return handleGet(context, transaction, resultData, params);
//                case "pDownload":
//                    return handleDownload(context, transaction, resultData, params);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.RETRY;
        }
        return Result.CONTINUE;
    }

    @Override
    public Result onFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        // TODO implement fail
        return Result.CONTINUE;
    }

    public Result handleChange(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");

        WorkorderTransactionBuilder.get(context, workorderId, false);

        WorkorderClient.get(context, workorderId, false, false);

        return Result.CONTINUE;
    }

    public Result handleGet(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long deliverableId = params.getLong("deliverableId");
        byte[] data = resultData.getByteArray();

        StoredObject.put(context, App.getProfileId(), PSO_DELIVERABLE, deliverableId, data);

        WorkorderDispatch.getDeliverable(context, new JsonObject(data), workorderId, deliverableId, false, transaction.isSync());

        return Result.CONTINUE;
    }

//    public Result handleDownload(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException, IOException {
//        long workorderId = params.getLong("workorderId");
//        long deliverableId = params.getLong("deliverableId");
//
//        if (resultData.isFile()) {
//            StoredObject obj = StoredObject.put(context, PSO_DELIVERABLE_FILE, deliverableId, resultData.getFile());
//            resultData.getFile().delete();
//            WorkorderDispatch.downloadDeliverable(context, workorderId, deliverableId, obj.getFile(), transaction.isSync());
//        } else {
//            File tempFolder = new File(App.get().getTempFolder());
//            tempFolder.mkdirs();
//            File tempFile = File.createTempFile("tmp", "dat", tempFolder);
//            FileOutputStream fout = new FileOutputStream(tempFile, false);
//            fout.write(resultData.getByteArray());
//            fout.close();
//
//            StoredObject obj = StoredObject.put(context, PSO_DELIVERABLE_FILE, deliverableId, tempFile);
//            tempFile.delete();
//            WorkorderDispatch.downloadDeliverable(context, workorderId, deliverableId, obj.getFile(), transaction.isSync());
//        }
//
//        return Result.FINISH;
//    }
}
