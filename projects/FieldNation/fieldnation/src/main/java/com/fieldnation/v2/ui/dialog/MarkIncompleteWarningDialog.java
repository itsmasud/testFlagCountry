package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.v2.data.client.WorkordersWebApi;

/**
 * Created by mc on 11/8/16.
 */

public class MarkIncompleteWarningDialog extends TwoButtonDialog {
    public MarkIncompleteWarningDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public boolean onPrimaryClick() {
        Parcelable extraData = getExtraData();

        if (extraData != null && extraData instanceof Bundle) {
            Integer workOrderId = ((Bundle) extraData).getInt("workOrderId");

            SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
            uiContext.page += " - Mark Incomplete Warning";

            WorkordersWebApi.incompleteWorkOrder(App.get(), workOrderId, uiContext);
            _onMarkIncompleteDispatcher.dispatch(getUid(), workOrderId);
        }
        return true;
    }

    public static void show(Context context, String uid, int workOrderId) {
        Bundle extraData = new Bundle();
        extraData.putInt("workOrderId", workOrderId);

        show(context, uid, MarkIncompleteWarningDialog.class, R.string.mark_as_incomplete, R.string.mark_as_incomplete_full_warning,
                R.string.btn_continue, R.string.btn_cancel, true, extraData);
    }

    /*-***********************************-*/
    /*-         Mark Incomplete           -*/
    /*-***********************************-*/
    public interface OnMarkIncompleteListener {
        void onMarkIncomplete(long workOrderId);
    }

    private static KeyedDispatcher<OnMarkIncompleteListener> _onMarkIncompleteDispatcher = new KeyedDispatcher<OnMarkIncompleteListener>() {
        @Override
        public void onDispatch(OnMarkIncompleteListener listener, Object... parameters) {
            listener.onMarkIncomplete((Long) parameters[0]);
        }
    };

    public static void addOnMarkIncompleteListener(String uid, OnMarkIncompleteListener onMarkIncompleteListener) {
        _onMarkIncompleteDispatcher.add(uid, onMarkIncompleteListener);
    }

    public static void removeOnMarkIncompleteListener(String uid, OnMarkIncompleteListener onMarkIncompleteListener) {
        _onMarkIncompleteDispatcher.remove(uid, onMarkIncompleteListener);
    }

    public static void removeAllOnMarkIncompleteListener(String uid) {
        _onMarkIncompleteDispatcher.removeAll(uid);
    }
}
