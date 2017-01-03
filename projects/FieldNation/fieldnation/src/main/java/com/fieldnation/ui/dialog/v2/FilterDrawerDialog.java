package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.fndialog.RightDrawerDialog;

/**
 * Created by mc on 1/3/17.
 */

public class FilterDrawerDialog extends RightDrawerDialog {
    private static final String TAG = "FilterDrawerDialog";

    public FilterDrawerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_filter_drawer, container, false);
        return v;
    }

    public static class Controller extends com.fieldnation.fndialog.Controller {
        public Controller(Context context, String uid) {
            super(context, FilterDrawerDialog.class, uid);
        }

        public static void show(Context context) {
            show(context, null, FilterDrawerDialog.class, null);
        }
    }

}
