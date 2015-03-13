package com.fieldnation.service.data;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.handlers.WebTransactionHandler;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoTransactionHandler extends WebTransactionHandler implements PhotoConstants {
    public static byte[] generateParams(String url, boolean getCircle) {
        try {
            JsonObject json = new JsonObject();
            json.put(PARAM_URL, url);
            json.put(PARAM_CIRCLE, getCircle);
            return json.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, Bundle resultData) {

    }
}
