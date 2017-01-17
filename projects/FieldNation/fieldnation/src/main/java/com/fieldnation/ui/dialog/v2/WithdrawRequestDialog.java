package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.KeyedDispatcher;

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
            Long workOrderId = ((Bundle) extraData).getLong("workOrderId");
            WorkorderClient.actionWithdrawRequest(App.get(), workOrderId);
            _onWithdrawDispatcher.dispatch(getUid(), workOrderId);
        }
        return true;
    }


    public static void show(Context context, String uid, long workOrderId) {
        Bundle extraData = new Bundle();
        extraData.putLong("workOrderId", workOrderId);

        show(context, uid, WithdrawRequestDialog.class, R.string.dialog_withdraw_title, R.string.dialog_withdraw_body,
                R.string.btn_yes, R.string.btn_no, true, extraData);
    }

    /*-*******************************************/
    /*-         Experimental Listener           -*/
    /*-*******************************************/
    public interface OnWithdrawListener {
        void onWithdraw(long workOrderId);
    }

    private static KeyedDispatcher<OnWithdrawListener> _onWithdrawDispatcher = new KeyedDispatcher<OnWithdrawListener>() {
        @Override
        public void onDispatch(OnWithdrawListener listener, Object... parameters) {
            listener.onWithdraw((Long) parameters[0]);
        }
    };

    public static void addOnWithdrawListener(String uid, OnWithdrawListener onWithdrawListener) {
        _onWithdrawDispatcher.add(uid, onWithdrawListener);
    }

    public static void removeOnWithdrawListener(String uid, OnWithdrawListener onWithdrawListener) {
        _onWithdrawDispatcher.remove(uid, onWithdrawListener);
    }

    public static void removeAllOnWithdrawListener(String uid) {
        _onWithdrawDispatcher.removeAll(uid);
    }

}
