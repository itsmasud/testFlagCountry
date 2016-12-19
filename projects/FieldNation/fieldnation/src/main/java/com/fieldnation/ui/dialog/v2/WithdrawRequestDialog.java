package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.service.data.workorder.WorkorderClient;

/**
 * Created by mc on 11/7/16.
 */

public class WithdrawRequestDialog extends TwoButtonDialog {

    public WithdrawRequestDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public boolean onPrimaryClick() {
        Parcelable extraData = getExtraData();

        if (extraData != null && extraData instanceof Bundle) {
            WorkorderClient.actionWithdrawRequest(App.get(), ((Bundle) extraData).getLong("workOrderId"));
        }
        return true;
    }


    public static class Controller extends TwoButtonDialog.Controller {

        public Controller(Context context, String uid) {
            super(context, WithdrawRequestDialog.class, uid);
        }

        public static void show(Context context, long workOrderId) {
            Bundle extraData = new Bundle();
            extraData.putLong("workOrderId", workOrderId);

            show(context, null, WithdrawRequestDialog.class, R.string.dialog_withdraw_title, R.string.dialog_withdraw_body,
                    R.string.btn_yes, R.string.btn_no, true, extraData);
        }
    }
}
