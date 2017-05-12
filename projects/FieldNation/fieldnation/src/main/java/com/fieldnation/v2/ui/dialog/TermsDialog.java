package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;

/**
 * Created by michael.carver on 11/18/2014.
 */
public class TermsDialog extends OneButtonDialog {
    private static final String TAG = "TermsDialog";

    public TermsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    public static void show(Context context, String uid, String title, String body) {
        show(context, uid, TermsDialog.class, title, body, App.get().getString(R.string.btn_done), true);
    }
}

