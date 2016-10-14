package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.service.activityresult.ActivityResultClient;

/**
 * Created by Michael on 9/26/2016.
 */

public class UpdateDialog extends OneButtonDialog {

    public UpdateDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public void onPrimaryClick() {
        Uri marketUri = Uri.parse("market://details?id=com.fieldnation.android");
        ActivityResultClient.startActivity(App.get(), new Intent(Intent.ACTION_VIEW).setData(marketUri));
    }

    @Override
    public void onCancel() {
        GlobalTopicClient.appShutdown(App.get());
    }

    public static abstract class Controller extends OneButtonDialog.Controller {
        public Controller(Context context) {
            super(context, UpdateDialog.class, null);
        }

        public static void show(Context context) {
            show(context, null, R.string.dialog_update_title, R.string.dialog_update_message,
                    R.string.btn_update_now, true);
        }
    }
}
