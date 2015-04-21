package com.fieldnation.service.data.photo;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.service.topics.TopicService;

import java.io.File;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class PhotoDataDispatch implements PhotoConstants {

    public static void photo(Context context, File file, String url, boolean getCircle) {
        Bundle response = new Bundle();
        response.putBoolean(PARAM_CIRCLE, getCircle);
        response.putString(PARAM_URL, url);
        response.putSerializable(RESULT_IMAGE_FILE, file);
        TopicService.dispatchEvent(context, TOPIC_ID_PHOTO_READY + "/" + url, response, true);

    }
}
