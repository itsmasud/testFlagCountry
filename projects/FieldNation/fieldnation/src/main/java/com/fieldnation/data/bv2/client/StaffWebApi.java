package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/27/17.
 */

public class StaffWebApi extends TopicClient {
    private static final String STAG = "StaffWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public StaffWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }
    /**
     * Get email templates by category.
     *
     * @param category email category
     * @param isBackground indicates that this call is low priority
     */
    public static void getEmailTemplates(Context context, String category, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/staff/email-templates/category/" + category);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/staff/email-templates/category/{category}")
                    .key(misc.md5("GET/" + "/api/rest/v2/staff/email-templates/category/" + category))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetEmailTemplates(String category) {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/staff/email-templates/category/" + category));
    }
}
