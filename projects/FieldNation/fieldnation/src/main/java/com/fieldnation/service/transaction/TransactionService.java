package com.fieldnation.service.transaction;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Michael Carver on 2/27/2015.
 */
public class TransactionService extends Service implements TransactionConstants {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
