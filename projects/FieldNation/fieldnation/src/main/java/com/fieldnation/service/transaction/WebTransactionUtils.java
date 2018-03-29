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

    private static WebTransactionUtils _webTransUtils;

    // Data
    private Listener _listener;

    public enum KeyType {
        CLOSING_NOTES
    }

    public static WebTransactionUtils getOb() {
        if (_webTransUtils == null) _webTransUtils =  new WebTransactionUtils();
        return _webTransUtils;
    }

    private static String getWebTransKeyByType(KeyType keyType, int workOrderId){
        switch (keyType){
            case CLOSING_NOTES: return WEB_TRANS_KEY_PREFIX_CLOSING_NOTES + workOrderId + "%";
        }
        return null;
    }

    private static String getParamKeyByType(KeyType keyType){
        switch (keyType){
            case CLOSING_NOTES: return PARAM_CLOSING_NOTES_KEY;
        }
        return null;
    }

    public static void setData(Listener listener, KeyType keyType, int workOrderId) {
        getOb()._listener = listener;
        new FindWebTransactionTask().executeEx(getWebTransKeyByType(keyType, workOrderId), getParamKeyByType(keyType));
    }


    private static class FindWebTransactionTask extends AsyncTaskEx<Object, Object, Object> {

        public FindWebTransactionTask() {
        }

        @Override
        protected Object doInBackground(Object... objects) {
            List<WebTransaction> webTransactions = WebTransaction.findByKey((String) objects[0]);
            String paramKey = (String) objects[1];

            // TODO maybe you need to use this stopwatch
            Stopwatch stopwatch = new Stopwatch(true);
            for (WebTransaction webTransaction : webTransactions) {
                try {
                    TransactionParams params = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));

                    if (params != null && params.methodParams != null && params.methodParams.contains(paramKey)) {
                        return webTransaction;
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getOb()._listener.onFoundWebTransaction((WebTransaction) o);
            super.onPostExecute(o);
        }
    }


    public interface Listener {
        void onFoundWebTransaction(WebTransaction webTransaction);
    }
}
