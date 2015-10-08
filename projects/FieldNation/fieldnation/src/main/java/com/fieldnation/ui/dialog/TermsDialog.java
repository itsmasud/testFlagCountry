package com.fieldnation.ui.dialog;

import android.support.v4.app.FragmentManager;

import com.fieldnation.App;
import com.fieldnation.R;

/**
 * Created by michael.carver on 11/18/2014.
 */
public class TermsDialog extends OneButtonDialog {
    private static final String TAG = "TermsDialog";

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static TermsDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, TermsDialog.class);
    }

    public void show(String title, String body) {
        setData(title, body, App.get().getString(R.string.btn_done), _super_listener);

        super.show();
    }

    private final OneButtonDialog.Listener _super_listener = new Listener() {
        @Override
        public void onButtonClick() {
            dismiss();
        }

        @Override
        public void onCancel() {

        }
    };
}

