package com.fieldnation.rpc.dbclient;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.ui.workorder.WorkorderDataSelector;

/**
 * Created by Michael Carver on 2/25/2015.
 */
public class WorkorderDbService extends DbService {

    public WorkorderDbService(Context context, String username, String authToken, ResultReceiver callback) {
        super(context, username, authToken, callback);
    }

    public Intent getList(int resultCode, int page, WorkorderDataSelector selector) {
        return null;
    }

}
