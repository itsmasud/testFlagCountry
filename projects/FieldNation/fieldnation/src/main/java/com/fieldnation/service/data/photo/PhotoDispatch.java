package com.fieldnation.service.data.photo;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicService;

import java.io.File;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class PhotoDispatch implements PhotoConstants {

    public static void get(Context context, File file, String url, boolean getCircle, boolean failed, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET);
        bundle.putBoolean(PARAM_CIRCLE, getCircle);
        bundle.putString(PARAM_URL, url);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);

        if (!failed) {
            bundle.putSerializable(RESULT_IMAGE_FILE, file);
        }

        String topicId = TOPIC_ID_GET_PHOTO;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (getCircle) {
            topicId += "/Circle";
        }

        topicId += url;

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }
}
