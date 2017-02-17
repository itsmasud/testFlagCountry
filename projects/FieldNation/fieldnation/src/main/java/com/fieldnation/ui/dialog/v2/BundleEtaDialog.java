package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.ui.BundleEtaCardView;

import java.util.Random;

/**
 * Created by Shoaib on 10/11/2016.
 */

public class BundleEtaDialog extends FullScreenDialog {
    private static final String TAG = "BundleEtaDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private RelativeLayout _incompleteLayout;
    private RelativeLayout _completeLayout;
    private LinearLayout _incompleteList;
    private LinearLayout _completeList;


    /*-*************************************-*/
    /*-             Life cycle              -*/
    /*-*************************************-*/
    public BundleEtaDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {

        View v = inflater.inflate(R.layout.dialog_v2_bundle_eta, container, false);

        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.primary_menu);

        _incompleteLayout = (RelativeLayout) v.findViewById(R.id.incompleteEta_layout);
        _incompleteList = (LinearLayout) v.findViewById(R.id.incompleteEta_list);
        _completeLayout = (RelativeLayout) v.findViewById(R.id.completeEta_layout);
        _completeList = (LinearLayout) v.findViewById(R.id.completeEta_list);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        super.show(params, animate);
        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        super.onRestoreDialogState(savedState);

        // UI
        populateUi();
    }

    public void setCompleteLayoutVisible(boolean visibility) {
        _completeLayout.setVisibility(visibility == true ? View.VISIBLE : View.INVISIBLE);
    }

    public void setIncompleteLayoutVisible(boolean visibility) {
        _incompleteLayout.setVisibility(visibility == true ? View.VISIBLE : View.INVISIBLE);
    }


    /*-********************************************-*/
    /*-             Internal Mutators              -*/
    /*-********************************************-*/
    private void populateUi() {
        _toolbar.setTitle("Set ETAs");
        _finishMenu.setTitle(App.get().getString(R.string.btn_submit));

        ForLoopRunnable r = new ForLoopRunnable(1, new Handler()) {
//            private final ShipmentTracking[] _shipments = shipments;

            @Override
            public void next(int i) throws Exception {
                BundleEtaCardView v = null;
                if (i < _incompleteList.getChildCount()) {
                    v = (BundleEtaCardView) _incompleteList.getChildAt(i);
                } else {
                    v = new BundleEtaCardView(getView().getContext());
                    _incompleteList.addView(v);
                }
//                v.setData(_workorder, _shipments[i]);
//                v.setListener(_summaryListener);
            }
        };
        _incompleteList.postDelayed(r, new Random().nextInt(1000));

        ForLoopRunnable r2 = new ForLoopRunnable(2, new Handler()) {
//            private final ShipmentTracking[] _shipments = shipments;

            @Override
            public void next(int i) throws Exception {
                BundleEtaCardView v = null;
                if (i < _completeList.getChildCount()) {
                    v = (BundleEtaCardView) _completeList.getChildAt(i);
                } else {
                    v = new BundleEtaCardView(getView().getContext());
                    _completeList.addView(v);
                }
//                v.setData(_workorder, _shipments[i]);
//                v.setListener(_summaryListener);
            }
        };

        _completeList.postDelayed(r2, new Random().nextInt(1000));


    }


    /*-*************************************-*/
    /*-             Ui Events               -*/
    /*-*************************************-*/

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            dismiss(true);
            return true;
        }
    };


    public static void show(Context context, String uid) {
        Bundle params = new Bundle();
        Controller.show(context, uid, BundleEtaDialog.class, params);
    }


}