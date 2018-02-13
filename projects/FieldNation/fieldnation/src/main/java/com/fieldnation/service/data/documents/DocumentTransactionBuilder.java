package com.fieldnation.service.data.documents;

import android.content.Context;
import android.content.res.Resources;

import com.fieldnation.R;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.NotificationDefinition;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionSystem;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentTransactionBuilder {
    private static final String TAG = "DocumentTransactionBuilder";

    public static void download(Context context, int documentId, String link, String filename, boolean isSync) {
        try {
            Resources res = context.getResources();
            HttpJsonBuilder builder = new HttpJsonBuilder().path(link);

            WebTransaction.Builder webBuilder = new WebTransaction.Builder()
                    .priority(isSync ? Priority.NORMAL : Priority.HIGH)
                    .listener(DocumentTransactionListener.class)
                    .listenerParams(DocumentTransactionListener.pDownload(documentId, filename))
                    .key((isSync ? "Sync/" : "") + "Document/" + documentId)
                    .useAuth(false)
                    .isSyncCall(isSync)
                    .request(builder);

            if (!isSync) {
                webBuilder.notifyOnStart(new NotificationDefinition(
                        R.drawable.ic_anim_download_start,
                        res.getString(R.string.app_name),
                        res.getString(R.string.notification_start_body_downloading, filename),
                        res.getString(R.string.notification_start_body_downloading, filename)
                ));
                webBuilder.notifyOnSuccess(new NotificationDefinition(
                        R.drawable.ic_anim_download_success,
                        res.getString(R.string.notification_success_title),
                        res.getString(R.string.notification_success_body_downloading, filename),
                        res.getString(R.string.notification_success_body_downloading, filename)
                ));
                webBuilder.notifyOnFail(new NotificationDefinition(
                        R.drawable.ic_anim_download_failed,
                        res.getString(R.string.notification_failed_title),
                        res.getString(R.string.notification_failed_body_downloading, filename),
                        res.getString(R.string.notification_failed_body_downloading, filename)
                ));
                webBuilder.notifyOnRetry(new NotificationDefinition(
                        R.drawable.ic_anim_download_retry,
                        res.getString(R.string.notification_retry_title),
                        res.getString(R.string.notification_retry_body_downloading, filename),
                        res.getString(R.string.notification_retry_body_downloading, filename)
                ));
            }
            WebTransactionSystem.queueTransaction(context, webBuilder.build());

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}
