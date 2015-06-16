package com.fieldnation.service.data.documents;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicService;

import java.io.File;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentDispatch implements DocumentConstants {

    public static void download(Context context, long documentId, File file, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DOCUMENT);
        bundle.putLong(PARAM_DOCUMENT_ID, documentId);
        bundle.putSerializable(PARAM_FILE, file);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_DOWNLOAD_DOCUMENT;

        if (isSync) {
            topicId += "_SYNC";
        }

        topicId += "/" + documentId;

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.NONE);
    }
}
