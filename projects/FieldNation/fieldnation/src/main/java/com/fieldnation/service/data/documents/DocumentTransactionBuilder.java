package com.fieldnation.service.data.documents;

import android.content.Context;

import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentTransactionBuilder {

    public static void download(Context context, long documentId, String link, String filename, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DocumentTransactionHandler.class)
                    .handlerParams(DocumentTransactionHandler.pDownload(documentId, filename))
                    .key((isSync ? "Sync/" : "") + "Document/" + documentId)
                    .useAuth(false)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .path(link))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
