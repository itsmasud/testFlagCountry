package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Michael on 9/19/2016.
 */
public abstract class Controller {
    private static final String TAG = "Controller";

    public static void show(Context context, String uid, Class<? extends Dialog> klass, Bundle params) {
        Client.show(context, uid, klass, params);
    }

    public static void dismiss(Context context, String uid) {
        Client.dismiss(context, uid);
    }
}
