package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Qualifications;
import com.fieldnation.v2.ui.QualificationsAdapter;

/**
 * Created by Shoaib on 11/14/17.
 */

public class QualificationsDialog extends FullScreenDialog {
    private static final String TAG = "QualificationsDialog";


    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private RefreshView _refreshView;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId;
    private Qualifications _qualifications;
    private QualificationsAdapter _adapter = new QualificationsAdapter();

    public QualificationsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_qualification_list, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle(v.getResources().getString(R.string.qualifications));

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setVisibility(View.GONE);

        _refreshView = v.findViewById(R.id.refresh_view);

        _list = v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _list.setAdapter(_adapter);

        _workOrdersApi.sub();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);

        _workOrderId = params.getInt("workOrderId");
        WorkordersWebApi.getQualifications(App.get(), _workOrderId, true, false);
        populateUi();
    }

    private void populateUi() {
        _finishMenu.setVisibility(View.GONE);

        if (_list == null)
            return;

        if (_qualifications == null
                || _qualifications.getSelectionRule() == null
                || _qualifications.getSelectionRule().getResults() == null
                || _qualifications.getSelectionRule().getResults().length == 0)
            return;

        _adapter.setQualifications(_qualifications.getSelectionRule().getResults());
    }

    @Override
    public void onStop() {
        _workOrdersApi.unsub();
        super.onStop();
    }

    private final View.OnClickListener _toolbar_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            dismiss(true);
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.toLowerCase().contains("qualifications");
        }

        @Override
        public boolean onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (successObject != null && successObject instanceof Qualifications) {
                Qualifications qualifications = (Qualifications) successObject;
                QualificationsDialog.this._qualifications = qualifications;
                AppMessagingClient.setLoading(false);
                populateUi();
            } else {
                WorkordersWebApi.getQualifications(App.get(), _workOrderId, true, false);
            }
            return super.onComplete(transactionParams, methodName, successObject, success, failObject);
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, QualificationsDialog.class, params);
    }
}
