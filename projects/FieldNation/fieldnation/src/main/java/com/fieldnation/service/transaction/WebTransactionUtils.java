package com.fieldnation.service.transaction;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.v2.data.listener.TransactionParams;

import java.util.List;

/**
 * Created by Shoaib Ahmed on 3/29/2018.
 */
public class WebTransactionUtils {
    private static final String TAG = "WebTransactionUtils";

    // Param Key
    public static final String PARAM_CLOSING_NOTES_KEY = "closingNotes";

    // WebTransaction Key
    public static final String WEB_TRANS_KEY_PREFIX_CLOSING_NOTES = "%/updateClosingNotesByWorkOrder/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_ADD_DISCOUNT = "%/addDiscountByWorkOrder/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_DELETE_DISCOUNT = "%/deleteDiscountByWorkOrder/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_ADD_EXPENSE = "%/addExpenseByWorkOrder/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_DELETE_EXPENSE = "%/deleteExpenseByWorkOrderAndExpense/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_ADD_MESSAGE = "%/addMessageByWorkOrder/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_ADD_SHIPMENT = "%/addShipmentByWorkOrder/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_DELETE_SHIPMENT = "%/deleteShipmentByWorkOrderAndShipment/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_ADD_SIGNATURE = "%/addSignatureByWorkOrder/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_DELETE_SIGNATURE = "%/deleteSignatureByWorkOrderAndSignature/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_WORKORDER_API = "%/api/rest/v2/workorders/";
    public static final String WEB_TRANS_KEY_PREFIX_CUSTOM_FIELD = "%/updateCustomFieldByWorkOrderAndCustomField/api/rest/v2/workorders/";


    // TODO more key type will be added
    public enum KeyType {
        CLOSING_NOTES,
        ADD_DISCOUNT,
        DELETE_DISCOUNT,
        ADD_EXPENSE,
        DELETE_EXPENSE,
        ADD_MESSAGE,
        ADD_SHIPMENT,
        DELETE_SHIPMENT,
        ADD_SIGNATURE,
        DELETE_SIGNATURE,
        WORK_ORDER,
        CUSTOM_FIELD
    }

    // TODO more cases will be added while implementing more offline features
    private static String getWebTransKeyByType(KeyType keyType, int workOrderId) {
        switch (keyType) {
            case CLOSING_NOTES:
                return WEB_TRANS_KEY_PREFIX_CLOSING_NOTES + workOrderId + "%";
            case ADD_DISCOUNT:
                return WEB_TRANS_KEY_PREFIX_ADD_DISCOUNT + workOrderId + "%";
            case DELETE_DISCOUNT:
                return WEB_TRANS_KEY_PREFIX_DELETE_DISCOUNT + workOrderId + "%";
            case ADD_EXPENSE:
                return WEB_TRANS_KEY_PREFIX_ADD_EXPENSE + workOrderId + "%";
            case DELETE_EXPENSE:
                return WEB_TRANS_KEY_PREFIX_DELETE_EXPENSE + workOrderId + "%";
            case ADD_MESSAGE:
                return WEB_TRANS_KEY_PREFIX_ADD_MESSAGE + workOrderId + "%";
            case ADD_SHIPMENT:
                return WEB_TRANS_KEY_PREFIX_ADD_SHIPMENT + workOrderId + "%";
            case DELETE_SHIPMENT:
                return WEB_TRANS_KEY_PREFIX_DELETE_SHIPMENT + workOrderId + "%";
            case ADD_SIGNATURE:
                return WEB_TRANS_KEY_PREFIX_ADD_SIGNATURE + workOrderId + "%";
            case DELETE_SIGNATURE:
                return WEB_TRANS_KEY_PREFIX_DELETE_SIGNATURE + workOrderId + "%";
            case WORK_ORDER:
                return WEB_TRANS_KEY_PREFIX_WORKORDER_API + workOrderId + "%";
            case CUSTOM_FIELD:
                return WEB_TRANS_KEY_PREFIX_CUSTOM_FIELD + workOrderId + "%";
            default:
                return null;
        }
    }

    // TODO more cases will be added while implementing more offline features
    private static String getParamKeyByType(KeyType keyType) {
        switch (keyType) {
            case CLOSING_NOTES:
                return PARAM_CLOSING_NOTES_KEY;
            default:
                return null;
        }
    }

    public static void setData(Listener listener, KeyType keyType, int workOrderId) {
        new FindWebTransactionTask(listener).executeEx(keyType, workOrderId);
    }

    private static class FindWebTransactionTask extends AsyncTaskEx<Object, Object, Object> {
        private Listener listener;

        public FindWebTransactionTask(Listener listener) {
            this.listener = listener;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            Stopwatch stopwatch = new Stopwatch(true);
            KeyType keyType = (KeyType) objects[0];
            int workOrderId = (Integer) objects[1];

            List<WebTransaction> webTransactions = WebTransaction.findByKey(getWebTransKeyByType(keyType, workOrderId));
            Log.v(TAG, "Searching time in DB " + stopwatch.finishAndRestart());

            String paramKey = getParamKeyByType(keyType);

            for (WebTransaction webTransaction : webTransactions) {
                try {
                    TransactionParams params = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));

                    if (params != null && params.methodParams != null
                            && (paramKey == null || params.methodParams.contains(paramKey))) {
                        publishProgress(keyType, workOrderId, webTransaction, params, new JsonObject(params.methodParams));
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }

            Log.v(TAG, "Traversing time " + stopwatch.finish());

            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            this.listener.onFoundWebTransaction((KeyType) values[0], (Integer) values[1], (WebTransaction) values[2], (TransactionParams) values[3], (JsonObject) values[4]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            this.listener.onComplete();
        }
    }

    /*-*********************************-*/
    /*-			Offline getters			-*/
    /*-*********************************-*/
    public static String getOfflineClosingNotes(WebTransaction webTransaction) {
        if (webTransaction == null) return null;

        try {
            TransactionParams params = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));

            if (params != null && params.methodParams != null && params.methodParams.contains(PARAM_CLOSING_NOTES_KEY)) {
                return params.getMethodParamString(PARAM_CLOSING_NOTES_KEY);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public abstract static class Listener {
        public abstract void onFoundWebTransaction(KeyType keyType, int workOrderId, WebTransaction webTransaction, TransactionParams transactionParams, JsonObject methodParams);

        public void onComplete() {
        }
    }
}
