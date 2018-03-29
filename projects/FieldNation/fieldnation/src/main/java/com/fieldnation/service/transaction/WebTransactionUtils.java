package com.fieldnation.service.transaction;

import android.view.View;

import com.fieldnation.v2.data.model.Expense;

/**
 * Created by Shoiab Ahmed on 3/29/2018.
 */
public class WebTransactionUtils {
    private static final String TAG = "WebTransactionUtils";

    // Param Key
    public static final String PARAM_CLOSING_NOTES_KEY = "closingNotes";

    // WebTransaction Key
    public static final String WEB_TRANS_KEY_PREFIX_CLOSING_NOTES = "%/updateClosingNotesByWorkOrder/api/rest/v2/workorders/";


    public static String getWebTransKeyForClosingNotes(int workOrderId) {
        return WEB_TRANS_KEY_PREFIX_CLOSING_NOTES + workOrderId + "%";
    }

    public static void test(Listener listener, WebTransaction webTransaction){
        listener.onFoundWebTransaction(webTransaction);
    }


    public interface Listener {
        void onFoundWebTransaction(WebTransaction webTransaction);
    }
}
