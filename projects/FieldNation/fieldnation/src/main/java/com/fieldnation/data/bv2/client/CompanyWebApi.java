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

public class CompanyWebApi extends TopicClient {
    private static final String STAG = "CompanyWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public CompanyWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }
    /**
     * Get a list of all company_integrations for a company.
     *
     * @param companyId null
     * @param accessToken null
     * @param isBackground indicates that this call is low priority
     */
    public static void getIntegrations(Context context, String companyId, String accessToken, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/" + companyId + "/integrations")
                    .urlParams("?access_token=" + accessToken);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}/integrations")
                    .key(misc.md5("GET/" + "/api/rest/v2/company/" + companyId + "/integrations" + "?access_token=" + accessToken))
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

    public boolean subGetIntegrations(String companyId, String accessToken) {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/company/" + companyId + "/integrations" + "?access_token=" + accessToken));
    }
}
