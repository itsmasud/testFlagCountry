package com.fieldnation.service.data.gmaps;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

/**
 * Created by Shoaib on 10/14/2016.
 */
public class GmapsDispatch implements GmapsConstants {
    private static final String TAG = "GmapsDispatch";

    public static void directions(Context context, int workOrderId, byte[] directions) {
        Log.v(TAG, "directions");
        Bundle bundle = new Bundle();
        bundle.putByteArray(PARAM_DIRECTIONS, directions);
        bundle.putInt(PARAM_WORKORDER_ID, workOrderId);

        PigeonRoost.sendMessage(ADDRESS_DIRECTIONS, bundle, Sticky.TEMP);
    }

    public static void staticMapClassic(Context context, int workOrderId, byte[] imageData, boolean failed) {
        Bundle bundle = new Bundle();

        if (imageData != null)
            bundle.putByteArray(PARAM_IMAGE_DATA, imageData);
        if (failed)
            bundle.putBoolean(PARAM_FAILED, failed);
        bundle.putInt(PARAM_WORKORDER_ID, workOrderId);

        PigeonRoost.sendMessage(ADDRESS_STATIC_MAP_CLASSIC, bundle, Sticky.TEMP);
    }
}
