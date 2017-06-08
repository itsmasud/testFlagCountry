package com.fieldnation.fnpermissions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;

/**
 * Created by mc on 6/8/17.
 */

public class PermissionsDialog extends FullScreenDialog {
    private static final String TAG = "PermissionsDialog";

    // Ui

    // Data
    private String _permission;

    public PermissionsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_permissions, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().setClickable(true);
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);

        String permission = params.getString("permission");

    }

    @Override
    public void cancel() {
        super.cancel();
        // TODO send denied results to client
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public static void show(Context context, String uid, String permission) {
        Bundle params = new Bundle();
        params.putString("permission", permission);
        Controller.show(context, uid, PermissionsDialog.class, null);
    }

}
