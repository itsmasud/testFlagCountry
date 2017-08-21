package com.fieldnation.service.data.photo;

import android.net.Uri;
import android.os.Bundle;

import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class PhotoDispatch implements PhotoConstants {

    public static void get(String sourceUrl, Uri localUri, boolean makeCircle, boolean success) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET);
        bundle.putBoolean(PARAM_IS_CIRCLE, makeCircle);
        bundle.putString(PARAM_SOURCE_URL, sourceUrl);
        bundle.putParcelable(PARAM_CACHE_URI, localUri);
        bundle.putBoolean(PARAM_SUCCESS, success);

        PigeonRoost.sendMessage(ADDRESS_GET_PHOTO, bundle, Sticky.TEMP);
    }
}
