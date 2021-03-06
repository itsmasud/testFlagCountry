package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.view.menu.ActionMenuItemView;
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
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.v2.data.client.BundlesWebApi;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.BundleEtaCardView;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by Shoaib on 10/11/2016.
 */

public class BundleEtaDialog extends FullScreenDialog {
    private static final String TAG = "BundleEtaDialog";

    // State
    private static final String STATE_COMPLETE_WO_LIST = "STATE_COMPLETE_WO_LIST";
    private static final String STATE_ETA_LIST = "STATE_ETA_LIST";

    // Dialogs
    private static final String UID_DIALOG_ETA = TAG + ".etaDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private RelativeLayout _incompleteLayout;
    private RelativeLayout _completeLayout;
    private LinearLayout _incompleteList;
    private LinearLayout _completeList;

    // Data
    private HashSet<Integer> _completeWorkOrders = new HashSet<>();
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

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setTextColor(getView().getResources().getColor(R.color.fn_light_text_80));

        _incompleteLayout = v.findViewById(R.id.incompleteEta_layout);
        _incompleteList = v.findViewById(R.id.incompleteEta_list);
        _completeLayout = v.findViewById(R.id.completeEta_layout);
        _completeLayout.setVisibility(View.GONE);
        _completeList = v.findViewById(R.id.completeEta_list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EtaDialog.addOnBundleEtaListener(UID_DIALOG_ETA, _etaDialog_onBundleEta);

        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _bundlesApi.sub();
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
        _bundlesApi.unsub();

        super.onStop();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        super.show(params, animate);
        _bundleId = params.getInt("bundleId", 0);
        if (_bundleId != 0)
            BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, WebTransaction.Type.NORMAL);
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);

        if (_completeWorkOrders != null)
            outState.putSerializable(STATE_COMPLETE_WO_LIST, _completeWorkOrders);

        if (_etaList != null)
            outState.putSerializable(STATE_ETA_LIST, _etaList);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        super.onRestoreDialogState(savedState);
        if (savedState.containsKey(STATE_COMPLETE_WO_LIST))
            _completeWorkOrders = (HashSet<Integer>) savedState.getSerializable(STATE_COMPLETE_WO_LIST);

        if (savedState.containsKey(STATE_ETA_LIST))
            _etaList = (Hashtable<Integer, ETA>) savedState.getSerializable(STATE_ETA_LIST);

        populateUi();
    }

    public void setCompleteLayoutVisible(boolean visibility) {
        _completeLayout.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    public void setIncompleteLayoutVisible(boolean visibility) {
        _incompleteLayout.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    /*-********************************************-*/
    /*-             Internal Mutators              -*/
    /*-********************************************-*/
    private void populateUi() {
        if (_bundleId == 0)
            return;

        if (_workOrders == null) {
            return;
        }

        _toolbar.setTitle("Set ETAs");
        _finishMenu.setText(App.get().getString(R.string.btn_submit));


        if (_completeWorkOrders.isEmpty())
            _completeLayout.setVisibility(View.GONE);
        else {
            _completeLayout.setVisibility(View.VISIBLE);
            _completeList.setVisibility(View.VISIBLE);
        }

        if (_completeWorkOrders.size() == _workOrders.getResults().length) {
            _finishMenu.setEnabled(true);
            _finishMenu.setTextColor(getView().getResources().getColor(R.color.white));
            _incompleteLayout.setVisibility(View.GONE);
        } else {
            _finishMenu.setEnabled(false);
            _finishMenu.setTextColor(getView().getResources().getColor(R.color.fn_light_text_80));
            _incompleteLayout.setVisibility(View.VISIBLE);
        }


        _incompleteList.removeAllViews();
        _completeList.removeAllViews();

        ForLoopRunnable r = new ForLoopRunnable(_workOrders.getResults().length, new Handler()) {
            private final WorkOrder[] workOrders = _workOrders.getResults();

            @Override
            public void next(int i) throws Exception {
                BundleEtaCardView v = null;
                if (!_completeWorkOrders.isEmpty() && _completeWorkOrders.contains(workOrders[i].getId())) {
                    if (i < _completeList.getChildCount()) {
                        v = (BundleEtaCardView) _completeList.getChildAt(i);
                    } else {
                        v = new BundleEtaCardView(getView().getContext());
                        _completeList.addView(v);
                    }
                } else {
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
            _onCancelDispatcher.dispatch(getUid());
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new ApatheticOnMenuItemClickListener() {
        @Override
        public boolean onSingleMenuItemClick(MenuItem item) {
            ETA[] etaAll = _etaList.values().toArray(new ETA[_etaList.size()]);
            WorkordersWebApi.MassAcceptWorkOrder(App.get(), "{" + "\"eta\":" + new ETA().toJsonArray(etaAll).toString() + "}");
            _onAcceptedDispatcher.dispatch(getUid(), _bundleId);

            dismiss(true);
            return true;
        }
    };


    private final EtaDialog.OnBundleEtaListener _etaDialog_onBundleEta = new EtaDialog.OnBundleEtaListener() {
        @Override
        public void onBundleEta(int workOrderId, ETA eta) {

            if (!_completeWorkOrders.contains(workOrderId)) {
                _completeWorkOrders.add(workOrderId);
            }

            if (!_etaList.contains(workOrderId)) {
                _etaList.put(workOrderId, eta);
            } else {
                _etaList.remove(workOrderId);
                _etaList.put(workOrderId, eta);
            }
            populateUi();
        }
    };

    private final BundleEtaCardView.OnClickListener _summaryListener = new BundleEtaCardView.OnClickListener() {
        @Override
        public void onClick(BundleEtaCardView view, WorkOrder workOrder) {
            EtaDialog.show(App.get(), UID_DIALOG_ETA, workOrder.getId(), workOrder.getSchedule(),
                    workOrder.getEta(), EtaDialog.PARAM_DIALOG_TYPE_MASS_ACCEPT);
        }
    };

    private final BundlesWebApi _bundlesApi = new BundlesWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("getBundleWorkOrders");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("getBundleWorkOrders")) {
                WorkOrders workOrders = (WorkOrders) successObject;

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

    /*-*************************************-*/
    /*-         Accepted Listener           -*/
    /*-*************************************-*/
    public interface OnAcceptedListener {
        void onAccepted(int bundleId);
    }

    private static KeyedDispatcher<OnAcceptedListener> _onAcceptedDispatcher = new KeyedDispatcher<OnAcceptedListener>() {
        @Override
        public void onDispatch(OnAcceptedListener listener, Object... parameters) {
            listener.onAccepted((Integer) parameters[0]);
        }
    };

    public static void addOnAcceptedListener(String uid, OnAcceptedListener onAcceptedListener) {
        _onAcceptedDispatcher.add(uid, onAcceptedListener);
    }

    public static void removeOnAcceptedListener(String uid, OnAcceptedListener onAcceptedListener) {
        _onAcceptedDispatcher.remove(uid, onAcceptedListener);
    }

    public static void removeAllOnAcceptedListener(String uid) {
        _onAcceptedDispatcher.removeAll(uid);
    }

    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }


}
