package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;

/**
 * Created by Michael Carver on 1/16/2015.
 */
public class UpdateDialog extends OneButtonDialog {

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static UpdateDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, UpdateDialog.class);
    }

    @Override
    public void show() {
        setData(App.get().getString(R.string.dialog_update_title),
                App.get().getString(R.string.dialog_update_message),
                App.get().getString(R.string.btn_update_now),
                _super_listener);

        super.show();
    }

    private final OneButtonDialog.Listener _super_listener = new Listener() {
        @Override
        public void onButtonClick() {
            Uri marketUri = Uri.parse("market://details?id=com.fieldnation.android");
            startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
            dismiss();
        }

        @Override
        public void onCancel() {
            GlobalTopicClient.appShutdown(getActivity());
        }
    };

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}
