package com.fieldnation.service.data.documents;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.MultiThreadedService;

import java.io.File;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentService extends MultiThreadedService implements DocumentConstants {
    private static final String TAG = "DocumentService";

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public void processIntent(Intent intent) {
        Log.v(TAG, "processIntent");
        if (intent != null && intent.hasExtra(PARAM_ACTION)) {
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

        StoredObject obj = StoredObject.get(context, App.getProfileId(), PSO_DOCUMENT, documentId);
        if (obj != null) {
            try {

                Uri uri = obj.getUri();
                String name = FileUtils.getFileNameFromUri(context, uri);
                File dlFolder = new File(App.get().getDownloadsFolder() + "/" + name);
                if (!dlFolder.exists())
                    FileUtils.writeStream(context.getContentResolver().openInputStream(uri), dlFolder);

                DocumentDispatch.download(context, documentId, dlFolder, PARAM_STATE_FINISH, isSync);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else {
            DocumentTransactionBuilder.download(context, documentId, url, filename, isSync);
        }
    }
}
