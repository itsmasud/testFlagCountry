package com.fieldnation.service.data.documents;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.misc;

import java.io.File;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public abstract class DocumentClient extends Pigeon implements DocumentConstants {
    private static final String TAG = "DocumentClient";

    public static void downloadDocument(Context context, long documentId, String url, String filename, boolean isSync) {
        if (misc.isEmptyOrNull(url)) {
            return;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            try {
                if (!isSync) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);

                ToastClient.toast(context, "URL copied to clipboard. Could not download or view " + url, Toast.LENGTH_LONG);

                ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setPrimaryClip(ClipData.newRawUri("file download", Uri.parse(url)));
            }
        } else {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    long documentId = (Long) params[0];
                    String url = (String) params[1];
                    boolean isSync = (Boolean) params[2];
                    String filename = (String) params[3];

                    StoredObject obj = StoredObject.get(App.get(), App.getProfileId(), PSO_DOCUMENT, documentId);
                    if (obj != null) {
                        try {

                            Uri uri = obj.getUri();
                            String name = FileUtils.getFileNameFromUri(App.get(), uri);
                            File dlFolder = new File(App.get().getDownloadsFolder() + "/" + name);
                            if (!dlFolder.exists())
                                FileUtils.writeStream(App.get().getContentResolver().openInputStream(uri), dlFolder);

                            DocumentDispatch.download(App.get(), documentId, dlFolder, PARAM_STATE_FINISH, isSync);
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                        }
                    } else {
                        DocumentTransactionBuilder.download(App.get(), documentId, url, filename, isSync);
                    }
                    return null;
                }
            }.executeEx(documentId, url, isSync, filename);
        }
    }

    public void sub() {
        PigeonRoost.sub(this, ADDRESS_DOWNLOAD_DOCUMENT);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_DOWNLOAD_DOCUMENT);
    }

    @Override
    public void onMessage(String address, Object message) {
        Bundle bundle = (Bundle) message;

        if (address.startsWith(ADDRESS_DOWNLOAD_DOCUMENT)) {
            if (processDownload(bundle.getLong(PARAM_DOCUMENT_ID))) {
                Log.v(TAG, "preOnDownload: " + address);
                preOnDownload(bundle);
            }
        }
    }

    public abstract boolean processDownload(long documentId);

    private void preOnDownload(Bundle bundle) {
        File file = null;
        int state = bundle.getInt(PARAM_STATE);

        if (bundle.containsKey(PARAM_FILE))
            file = (File) bundle.getSerializable(PARAM_FILE);

        onDownload(bundle.getLong(PARAM_DOCUMENT_ID),
                file, state, bundle.getBoolean(PARAM_IS_SYNC));
    }

    public void onDownload(long documentId, File file, int state, boolean isSync) {
    }
}
