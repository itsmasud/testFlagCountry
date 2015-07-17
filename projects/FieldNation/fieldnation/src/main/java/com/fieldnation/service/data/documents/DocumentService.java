package com.fieldnation.service.data.documents;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.Log;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentService extends MSService implements DocumentConstants {
    private static final String TAG = "DocumentService";

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public void processIntent(Intent intent) {
        Log.v(TAG, "processIntent");
        if (this != null && intent.hasExtra(PARAM_ACTION)) {
            String action = intent.getStringExtra(PARAM_ACTION);
            switch (action) {
                case PARAM_ACTION_DOWNLOAD_DOCUMENT:
                    download(this, intent);
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    private static void download(Context context, Intent intent) {
        Log.v(TAG, "download");

        long documentId = intent.getLongExtra(PARAM_DOCUMENT_ID, 0);
        String url = intent.getStringExtra(PARAM_URL);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        String filename = intent.getStringExtra(PARAM_FILE_NAME);

        StoredObject obj = StoredObject.get(context, PSO_DOCUMENT, documentId);
        if (obj != null) {
            try {
                DocumentDispatch.download(context, documentId, obj.getFile(), false, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            DocumentTransactionBuilder.download(context, documentId, url, filename, isSync);
        }
    }
}
