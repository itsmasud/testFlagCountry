package com.fieldnation.service.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Michael Carver on 2/27/2015.
 */
public class TransactionClient implements TransactionConstants {
    public static final String TAG = "TransactionClient";

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public TransactionClient() {
    }

    /*-*****************************-*/
    /*-         Commands            -*/
    /*-*****************************-*/

    public boolean send(Context context, String key, String request, String handlerName,
                        Transaction.Priority priority, List<Bundle> transforms) {
        try {
            Intent intent = new Intent(context, TransactionService.class);
            intent.putExtra(PARAM_KEY, key);
            intent.putExtra(PARAM_REQUEST, request);
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
