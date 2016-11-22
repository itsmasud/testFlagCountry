package com.fieldnation.service.data.documents;

import android.content.Context;
import android.content.res.Resources;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.NotificationDefinition;
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
            HttpJsonBuilder builder = new HttpJsonBuilder().path(link);

            WebTransactionBuilder webBuilder = WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DocumentTransactionHandler.class)
                    .handlerParams(DocumentTransactionHandler.pDownload(documentId, filename))
                    .key((isSync ? "Sync/" : "") + "Document/" + documentId)
                    .useAuth(false)
                    .isSyncCall(isSync)
                    .request(builder);

            if (!isSync) {
                webBuilder.notify(new NotificationDefinition(
                                R.drawable.ic_anim_download_start,
                                res.getString(R.string.app_name),
                                res.getString(R.string.notification_start_body_downloading, filename),
                                res.getString(R.string.notification_start_body_downloading, filename)
                        ),
                        new NotificationDefinition(
                                R.drawable.ic_anim_download_success,
                                res.getString(R.string.notification_success_title),
                                res.getString(R.string.notification_success_body_downloading, filename),
                                res.getString(R.string.notification_success_body_downloading, filename)
                        ),
                        new NotificationDefinition(
                                R.drawable.ic_anim_download_failed,
                                res.getString(R.string.notification_failed_title),
                                res.getString(R.string.notification_failed_body_downloading, filename),
                                res.getString(R.string.notification_failed_body_downloading, filename)
                        ),
                        new NotificationDefinition(
                                R.drawable.ic_anim_download_retry,
                                res.getString(R.string.notification_retry_title),
                                res.getString(R.string.notification_retry_body_downloading, filename),
                                res.getString(R.string.notification_retry_body_downloading, filename)
                        ));
            }
            webBuilder.send();

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}
