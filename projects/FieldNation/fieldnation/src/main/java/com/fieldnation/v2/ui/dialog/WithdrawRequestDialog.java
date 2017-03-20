package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.v2.data.client.WorkordersWebApi;

/**
 * Created by mc on 11/7/16.
 */

public class WithdrawRequestDialog extends TwoButtonDialog {
    private static final String TAG = "WithdrawRequestDialog";

    public WithdrawRequestDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public boolean onPrimaryClick() {
        Parcelable extraData = getExtraData();

        if (extraData != null && extraData instanceof Bundle) {
            Bundle bundle = (Bundle) extraData;
            int workOrderId = bundle.getInt("workOrderId");
            int requestId = bundle.getInt("requestId");

            try {
                WorkordersWebApi.removeRequest(App.get(), workOrderId, requestId);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            _onWithdrawDispatcher.dispatch(getUid(), workOrderId);
        }
        return true;
    }


    public static void show(Context context, String uid, int workOrderId, int requestId) {
        Bundle extraData = new Bundle();
        extraData.putInt("workOrderId", workOrderId);
        extraData.putInt("requestId", requestId);

        show(context, uid, WithdrawRequestDialog.class, R.string.dialog_withdraw_title, R.string.dialog_withdraw_body,
                R.string.btn_yes, R.string.btn_no, true, extraData);
    }

    /*-****************************-*/
    /*-         Withdraw           -*/
    /*-****************************-*/
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
