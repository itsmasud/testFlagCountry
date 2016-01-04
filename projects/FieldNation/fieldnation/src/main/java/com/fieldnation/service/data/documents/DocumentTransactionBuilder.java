package com.fieldnation.service.data.documents;

import android.content.Context;
import android.content.res.Resources;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentTransactionBuilder {
    private static final String TAG = "DocumentTransactionBuilder";

    public static void download(Context context, long documentId, String link, String filename, boolean isSync) {
        try {
            Resources res = context.getResources();
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DocumentTransactionHandler.class)
                    .handlerParams(DocumentTransactionHandler.pDownload(documentId, filename))
                    .key((isSync ? "Sync/" : "") + "Document/" + documentId)
                    .useAuth(false)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .path(link)
                            .notify(res.getString(R.string.app_name),
                                    res.getString(R.string.notification_content_text_start_downloading, filename),
                                    res.getString(R.string.notification_content_text_start_downloading, filename),
                                    R.drawable.ic_notif_logo,
                                    res.getString(R.string.notification_title_success),
                                    res.getString(R.string.notification_content_text_success_downloading, filename),
                                    res.getString(R.string.notification_content_text_success_downloading, filename),
                                    R.drawable.ic_notif_logo,
                                    res.getString(R.string.notification_title_failed),
                                    res.getString(R.string.notification_content_text_failed_downloading, filename),
                                    res.getString(R.string.notification_content_text_failed_downloading, filename),
                                    R.drawable.ic_notif_logo))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}
