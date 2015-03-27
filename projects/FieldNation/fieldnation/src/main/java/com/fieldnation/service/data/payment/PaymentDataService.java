package com.fieldnation.service.data.payment;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentDataService extends Service implements PaymentConstants {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
