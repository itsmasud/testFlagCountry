package com.fieldnation.service.data.documents;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.FileHelper;
import com.fieldnation.Log;
import com.fieldnation.UniqueTag;
import com.fieldnation.service.toast.ToastClient;
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

    public static void downloadDocument(Context context, long documentId, String url, String filename, boolean isSync) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception ex) {
                Log.v(STAG, ex);

                ToastClient.toast(context, "URL copied to clipboard. Could not download or view " + url, Toast.LENGTH_LONG);

                ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setPrimaryClip(ClipData.newRawUri("file download", Uri.parse(url)));
            }
            return;
        }
        Intent intent = new Intent(context, DocumentService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DOCUMENT);
        intent.putExtra(PARAM_DOCUMENT_ID, documentId);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        intent.putExtra(PARAM_FILE_NAME, filename);
        context.startService(intent);
    }

    public boolean subDocument() {
        return subDocument(0);
    }

    public boolean subDocument(long documentId) {
        String topicId = TOPIC_ID_DOWNLOAD_DOCUMENT;

        if (documentId > 0) {
            topicId += "/" + documentId;
        }

        return register(topicId, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(TOPIC_ID_DOWNLOAD_DOCUMENT)) {
                Log.v(STAG, "preOnDownload: " + topicId);
                preOnDownload((Bundle) payload);
            }
        }

        private void preOnDownload(Bundle bundle) {
            File file = null;
            int state = bundle.getInt(PARAM_STATE);

            if (bundle.containsKey(PARAM_FILE))
                file = (File) bundle.getSerializable(PARAM_FILE);

            onDownload(bundle.getLong(PARAM_DOCUMENT_ID),
                    file, state);
        }

        public void onDownload(long documentId, File file, int state) {
        }


    }

}
