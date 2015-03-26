package com.fieldnation.ui.payment;

import android.content.Intent;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.data.accounting.Payment;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.webclient.PaymentWebService;
import com.fieldnation.ui.ItemListActivity;

import java.util.LinkedList;
import java.util.List;

public class PaymentListActivity extends ItemListActivity<Payment> {
    private static final String TAG = "ui.payment.PaymentListActivity";

    // Data
    private PaymentDataClient _paymentClient;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/


// todo delete
    @Override
    public Intent requestData(int resultCode, int page, boolean allowCache) {
        if (_service == null)
            return null;

        return _service.getAll(resultCode, page, allowCache);
    }


    @Override
    public void requestData(int page) {

    }

    @Override
    public View getView(Payment object, View convertView, ViewGroup parent) {
        PaymentCardView v = null;
        if (convertView == null) {
            v = new PaymentCardView(parent.getContext());
        } else if (convertView instanceof PaymentCardView) {
            v = (PaymentCardView) convertView;
        } else {
            v = new PaymentCardView(parent.getContext());
        }

        v.setData(object);

        return v;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

// todo remove
    @Override
    public void onAuthentication(String username, String authToken, boolean isNew, ResultReceiver resultReceiver) {
        if (_service == null || isNew) {
            _service = new PaymentWebService(this, username, authToken, resultReceiver);
        }
    }


// todo remove
    @Override
    public List<Payment> onParseData(int page, boolean isCached, byte[] data) {
        JsonArray objects = null;
        try {
            objects = new JsonArray(new String(data));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        List<Payment> list = new LinkedList<Payment>();
        for (int i = 0; i < objects.size(); i++) {
            try {
                list.add(Payment.fromJson(objects.getJsonObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }


// todo remove
    @Override
    public void invalidateService() {
        _service = null;
    }

}
