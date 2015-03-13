package com.fieldnation.service.data.profile;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileWebTransactionHandler extends WebTransactionHandler implements ProfileConstants {

    public static byte[] generateParams(String action) {
        return action.getBytes();
    }


    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        String action = new String(transaction.getHandlerParams());

        if (action.equals(PARAM_ACTION_GET_MY_PROFILE)) {
            // store object
            byte[] profiledata = resultData.getResultsAsByteArray();

            StoredObject.put(context, "Profile", "Me", null, profiledata);
            // todo parse json and put Profile/id

            Bundle bundle = new Bundle();
            bundle.putByteArray(PARAM_PROFILE, resultData.getResultsAsByteArray());
            TopicService.dispatchEvent(context, TOPIC_ID_HAVE_PROFILE, bundle, true);
        }

    }
}
