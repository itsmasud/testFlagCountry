package com.fieldnation.service.data;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoDataClient implements PhotoConstants {

    PhotoDataClient

    public Intent getPhoto(Context context, String url, boolean getCircle) {
        Intent intent = new Intent(context, PhotoDataService.class);
        intent.putExtra(PARAM_CIRCLE, getCircle);
        intent.putExtra(PARAM_URL, url);

        return intent;
    }


}
