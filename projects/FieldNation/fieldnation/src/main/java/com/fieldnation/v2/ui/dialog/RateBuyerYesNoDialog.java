package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by mc on 1/20/17.
 */

public class RateBuyerYesNoDialog extends TwoButtonDialog {
    private static final String TAG = "RateBuyerYesNoDialog";

    // Dialog tags
    private static final String DIALOG_RATE_BUYER = TAG + ".discountDialog";

    private static final String PARAM_WORKORDER = "workOrder";

    private WorkOrder _workOrder;

    public RateBuyerYesNoDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        _workOrder = (WorkOrder) getExtraData();
    }

    @Override
    public boolean onPrimaryClick() {
        _onContinueDispatcher.dispatch(getUid());
        RateBuyerDialog.show(App.get(), DIALOG_RATE_BUYER, _workOrder);
        return super.onPrimaryClick();
    }

    @Override
    public boolean onSecondaryClick() {
        _onLaterDispatcher.dispatch(getUid());
        return super.onSecondaryClick();
    }

    public static void show(Context context, String uid, WorkOrder workOrder, String companyName) {
        show(context, uid, RateBuyerYesNoDialog.class,
                context.getString(R.string.modal_rate_buyer_title),
                context.getString(R.string.modal_rate_buyer_body, companyName),
                context.getString(R.string.btn_continue),
                context.getString(R.string.btn_remind_me_later),
                true, workOrder);
    }

    /*-*****************************-*/
    /*-         Continue            -*/
    /*-*****************************-*/
    public interface OnContinueListener {
        void onContinue();
    }

    private static KeyedDispatcher<OnContinueListener> _onContinueDispatcher = new KeyedDispatcher<OnContinueListener>() {
        @Override
        public void onDispatch(OnContinueListener listener, Object... parameters) {
            listener.onContinue();
        }
    };

    public static void addOnContinueListener(String uid, OnContinueListener onContinueListener) {
        _onContinueDispatcher.add(uid, onContinueListener);
    }

    public static void removeOnContinueListener(String uid, OnContinueListener onContinueListener) {
        _onContinueDispatcher.remove(uid, onContinueListener);
    }

    public static void removeAllOnContinueListener(String uid) {
        _onContinueDispatcher.removeAll(uid);
    }

    /*-*************************-*/
    /*-         Later           -*/
    /*-*************************-*/
    public interface OnLaterListener {
        void onLater();
    }

    private static KeyedDispatcher<OnLaterListener> _onLaterDispatcher = new KeyedDispatcher<OnLaterListener>() {
        @Override
        public void onDispatch(OnLaterListener listener, Object... parameters) {
            listener.onLater();
        }
    };

    public static void addOnLaterListener(String uid, OnLaterListener onLaterListener) {
        _onLaterDispatcher.add(uid, onLaterListener);
    }

    public static void removeOnLaterListener(String uid, OnLaterListener onLaterListener) {
        _onLaterDispatcher.remove(uid, onLaterListener);
    }

    public static void removeAllOnLaterListener(String uid) {
        _onLaterDispatcher.removeAll(uid);
    }
}
