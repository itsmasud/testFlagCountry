package com.fieldnation.v2.ui.dialog;

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

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.client.BundlesWebApi;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.BundleEtaCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Shoaib on 10/11/2016.
 */

public class BundleEtaDialog extends FullScreenDialog {
    private static final String TAG = "BundleEtaDialog";

    // Dialogs
    private static final String UID_DIALOG_ETA = TAG + ".etaDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private RelativeLayout _incompleteLayout;
    private RelativeLayout _completeLayout;
    private LinearLayout _incompleteList;
    private LinearLayout _completeList;

    // Services
    private BundlesWebApi _bundlesApi;

    // Data
    private Hashtable<Integer, WorkOrder> _completeWorkOrders = new Hashtable();
    Hashtable<Integer, ETA> _etaList = new Hashtable();
    private int _bundleId = 0;
    private WorkOrders _workOrders;

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

//        _refreshView = (RefreshView) v.findViewById(R.id.refresh_view);

        _finishMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.primary_menu);

        _incompleteLayout = (RelativeLayout) v.findViewById(R.id.incompleteEta_layout);
        _incompleteList = (LinearLayout) v.findViewById(R.id.incompleteEta_list);
        _completeLayout = (RelativeLayout) v.findViewById(R.id.completeEta_layout);
        _completeLayout.setVisibility(View.GONE);
        _completeList = (LinearLayout) v.findViewById(R.id.completeEta_list);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        EtaDialog.addOnBundleEtaListener(UID_DIALOG_ETA, _etaDialog_onBundleEta);

        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _bundlesApi = new BundlesWebApi(_bundlesWebApi_listener);
        _bundlesApi.connect(App.get());

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
    public void onStop() {
        EtaDialog.removeOnBundleEtaListener(UID_DIALOG_ETA, _etaDialog_onBundleEta);
        if (_bundlesApi != null)
            _bundlesApi.disconnect(App.get());

        super.onStop();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        super.show(params, animate);
        _bundleId = params.getInt("bundleId", 0);
        if (_bundleId != 0)
            BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, false);
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        super.onRestoreDialogState(savedState);
        populateUi();
    }

    public void setCompleteLayoutVisible(boolean visibility) {
        _completeLayout.setVisibility(visibility == true ? View.VISIBLE : View.INVISIBLE);
    }

    public void setIncompleteLayoutVisible(boolean visibility) {
        _incompleteLayout.setVisibility(visibility == true ? View.VISIBLE : View.INVISIBLE);
    }

//    private void setLoading(boolean loading) {
//        if (loading) {
//            _toolbar.setEnabled(false);
//            _refreshView.startRefreshing();
//        } else {
//            _toolbar.setEnabled(true);
//            _refreshView.refreshComplete();
//        }
//    }


    /*-********************************************-*/
    /*-             Internal Mutators              -*/
    /*-********************************************-*/
    private void populateUi() {
        if (_bundleId == 0)
            return;

        if (_workOrders == null) {
            return;
        }

        Log.e(TAG, "populateUi");
        _toolbar.setTitle("Set ETAs");
        _finishMenu.setTitle(App.get().getString(R.string.btn_submit));


        if (_completeWorkOrders.isEmpty())
            _completeLayout.setVisibility(View.GONE);
        else {
            _completeLayout.setVisibility(View.VISIBLE);
            _completeList.setVisibility(View.VISIBLE);
        }

        if (_completeWorkOrders.size() == _workOrders.getResults().length) {
            _finishMenu.setEnabled(true);
            _incompleteLayout.setVisibility(View.GONE);
        } else {
            _finishMenu.setEnabled(false);
            _incompleteLayout.setVisibility(View.VISIBLE);
        }


        Log.e(TAG, "bundle id: " + _bundleId);

        _incompleteList.removeAllViews();
        _completeList.removeAllViews();

        ForLoopRunnable r = new ForLoopRunnable(_workOrders.getResults().length, new Handler()) {
            private final WorkOrder[] workOrders = _workOrders.getResults();

            @Override
            public void next(int i) throws Exception {
                BundleEtaCardView v = null;
                if (!_completeWorkOrders.isEmpty() && _completeWorkOrders.containsKey(workOrders[i].getId())) {
                    Log.e(TAG, "preparing complete list");
                    if (i < _completeList.getChildCount()) {
                        v = (BundleEtaCardView) _completeList.getChildAt(i);
                    } else {
                        v = new BundleEtaCardView(getView().getContext());
                        _completeList.addView(v);
                    }
                } else {
                    Log.e(TAG, "preparing incomplete list");
                    if (i < _incompleteList.getChildCount()) {
                        v = (BundleEtaCardView) _incompleteList.getChildAt(i);
                    } else {
                        v = new BundleEtaCardView(getView().getContext());
                        _incompleteList.addView(v);
                    }
                }
                v.setData(workOrders[i]);
                v.setListener(_summaryListener);
            }
        };
        _incompleteList.postDelayed(r, new Random().nextInt(1000));
        _completeList.postDelayed(r, new Random().nextInt(1000));
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

            ETA[] etaAll = (ETA[]) _etaList.values().toArray(new ETA[_etaList.size()]);

            Log.e(TAG, "etaAll with eta node: " + "{" +"\"eta\":" + new ETA ().toJsonArray(etaAll).toString() + "}");

            WorkordersWebApi.MassAcceptWorkOrder(App.get(), "{" +"\"eta\":" + new ETA ().toJsonArray(etaAll).toString() + "}");

            dismiss(true);
            return true;
        }
    };


    private final EtaDialog.OnBundleEtaListener _etaDialog_onBundleEta = new EtaDialog.OnBundleEtaListener() {
        @Override
        public void onBundleEta(ETA eta, WorkOrder workOrder) {

            Log.e(TAG, "onBundleEta");
            if (!_completeWorkOrders.containsKey(workOrder.getId())) {
                _completeWorkOrders.put(workOrder.getId(), workOrder);
            }

            if (!_etaList.contains(workOrder.getId())) {
                _etaList.put(workOrder.getId(), eta);
            } else {
                _etaList.remove(workOrder.getId());
                _etaList.put(workOrder.getId(), eta);
                Log.e(TAG, "size of _etaList" + _etaList.size());
            }

            for (int key : _etaList.keySet()) {
                System.out.println("key : " + key);
                System.out.println("value : " + _etaList.get(key).getJson());
            }


            populateUi();
        }
    };

    private final BundleEtaCardView.OnClickListener _summaryListener = new BundleEtaCardView.OnClickListener() {
        @Override
        public void onClick(BundleEtaCardView view, WorkOrder workOrder) {
            EtaDialog.show(App.get(), UID_DIALOG_ETA, workOrder);
        }
    };

    private final BundlesWebApi.Listener _bundlesWebApi_listener = new BundlesWebApi.Listener() {
        @Override
        public void onConnected() {
            _bundlesApi.subBundlesWebApi();
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("getBundleWorkOrders")) {
                WorkOrders workOrders = (WorkOrders) successObject;
//                setLoading(false);

                if (!success || workOrders == null || workOrders.getResults() == null) {
                    return;
                }
                _workOrders = workOrders;
                populateUi();
            }
        }
    };

    public static void show(Context context, String uid, int bundleId) {
        Bundle params = new Bundle();
        params.putInt("bundleId", bundleId);
        Controller.show(context, uid, BundleEtaDialog.class, params);
    }

}