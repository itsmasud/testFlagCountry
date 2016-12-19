package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.service.data.workorder.WorkorderClient;

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
            WorkorderClient.actionIncomplete(App.get(), ((Bundle) extraData).getLong("workOrderId"));
        }

        return true;
    }

    public static class Controller extends TwoButtonDialog.Controller {

        public Controller(Context context, String uid) {
            super(context, MarkIncompleteWarningDialog.class, uid);
        }

        public static void show(Context context, long workOrderId) {
            Bundle extraData = new Bundle();
            extraData.putLong("workOrderId", workOrderId);

            show(context, null, MarkIncompleteWarningDialog.class, R.string.mark_as_incomplete, R.string.mark_as_incomplete_full_warning,
                    R.string.btn_continue, R.string.btn_cancel, true, extraData);
        }
    }
}
