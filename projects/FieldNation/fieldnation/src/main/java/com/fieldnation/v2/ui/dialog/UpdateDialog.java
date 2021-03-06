package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup;

import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.fnactivityresult.ActivityClient;

/**
 * Created by Michael on 9/26/2016.
 */

public class UpdateDialog extends OneButtonDialog {

    public UpdateDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public boolean onPrimaryClick() {
        Uri marketUri = Uri.parse("market://details?id=com.fieldnation.android");
        ActivityClient.startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
        return false;
    }

    @Override
    public boolean onCancel() {
        AppMessagingClient.appShutdown();
        return false;
    }

    public static void show(Context context) {
        show(context, "UpdateDialog", UpdateDialog.class, R.string.dialog_update_title, R.string.dialog_update_message,
                R.string.btn_update_now, true);
    }
}
