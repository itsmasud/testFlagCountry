package com.fieldnation.service.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;

import java.util.List;

/**
 * Created by Michael Carver on 2/27/2015.
 */
public class WebTransactionClient implements WebTransactionConstants {
    public static final String TAG = "TransactionClient";

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public WebTransactionClient() {
    }

    /*-*****************************-*/
    /*-         Commands            -*/
    /*-*****************************-*/

    public boolean send(Context context, String key, JsonObject meta, byte[] payload, String handlerName,
                        WebTransaction.Priority priority, List<Bundle> transforms) {
        try {
            Intent intent = new Intent(context, WebTransactionService.class);
            intent.putExtra(PARAM_KEY, key);
            intent.putExtra(PARAM_META, meta.toByteArray());
            intent.putExtra(PARAM_PAYLOAD, payload);
            intent.putExtra(PARAM_HANDLER_NAME, handlerName);
            intent.putExtra(PARAM_PRIORITY, priority.ordinal());
            intent.putExtra(PARAM_TRANSFORM_LIST, (Parcelable[]) transforms.toArray());
            context.startService(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
