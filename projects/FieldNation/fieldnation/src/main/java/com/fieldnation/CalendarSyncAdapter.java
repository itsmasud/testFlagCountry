package com.fieldnation;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 11/21/17.
 */

public class CalendarSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "CalendarSyncAdapter";

    public CalendarSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public CalendarSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.v(TAG, "onPerformSync");
    }

    public static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build();
    }
}
