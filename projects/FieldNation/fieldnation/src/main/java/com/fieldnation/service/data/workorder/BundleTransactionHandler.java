package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael on 3/31/2015.
 */
public class BundleTransactionHandler extends WebTransactionHandler implements WorkorderConstants {

    public static byte[] pBundle(long bundleId) {
        try {
            JsonObject obj = new JsonObject();
            obj.put("action", "pBundle");
            obj.put("bundleId", bundleId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            long bundleId = params.getLong("bundleId");
            byte[] data = resultData.getByteArray();

            StoredObject.put(context, PSO_BUNDLE, bundleId, data);

            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
            bundle.putParcelable(PARAM_DATA_PARCELABLE, new JsonObject(data));
            bundle.putLong(PARAM_ID, bundleId);
            TopicService.dispatchEvent(context, PARAM_ACTION_GET_BUNDLE, bundle, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.FINISH;
    }
}
