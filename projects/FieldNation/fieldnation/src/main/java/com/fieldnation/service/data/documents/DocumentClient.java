package com.fieldnation.service.data.documents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.TopicClient;

import java.io.File;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentClient extends TopicClient implements DocumentConstants {
    private static final String STAG = "DocumentClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public DocumentClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void downloadDocument(Context context, long documentId, String url, boolean isSync) {
        Intent intent = new Intent(context, DocumentService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DOCUMENT);
        intent.putExtra(PARAM_DOCUMENT_ID, documentId);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subDocument(long documentId) {
        String topicId = TOPIC_ID_DOWNLOAD_DOCUMENT;

        topicId += "/" + documentId;

        return register(topicId, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(TOPIC_ID_DOWNLOAD_DOCUMENT)) {
                preOnDownload((Bundle) payload);
            }
        }

        private void preOnDownload(Bundle bundle) {
            onDownload(bundle.getLong(PARAM_DOCUMENT_ID),
                    (File) bundle.getSerializable(PARAM_FILE));
        }

        public void onDownload(long documentId, File file) {
        }


    }

}
