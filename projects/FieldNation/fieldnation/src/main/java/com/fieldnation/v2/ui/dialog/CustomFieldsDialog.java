package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFields;
import com.fieldnation.v2.ui.CustomFieldsAdapter;

/**
 * Created by mc on 9/28/17.
 */

public class CustomFieldsDialog extends FullScreenDialog {
    private static final String TAG = "CustomFieldsDialog";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId;
    private CustomFields _customFields;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public CustomFieldsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_toolbar_recycle, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle(getContext().getString(R.string.fields_to_enter));

        _list = v.findViewById(R.id.list);
        _list.setItemAnimator(new DefaultItemAnimator());
        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _list.setAdapter(_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        _workOrdersApi.sub();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);

        _workOrderId = params.getInt("workOrderId");
        AppMessagingClient.setLoading(true);
        WorkordersWebApi.getCustomFields(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
        populateUi();
    }

    private void populateUi() {
        if (_list == null) return;

        if (_customFields == null) return;

        _adapter.setCustomFields(_customFields);
    }

    @Override
    public void onPause() {
        _workOrdersApi.unsub();
        super.onPause();
    }

    private final CustomFieldsAdapter _adapter = new CustomFieldsAdapter() {
        @Override
        public void onCustomFieldClicked(CustomField customField) {
            if (customField.getActionsSet().contains(CustomField.ActionsEnum.EDIT)) {
                CustomFieldDialog.show(App.get(), null, _workOrderId, customField);
            }
        }
    };

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return methodName.contains("getCustomFields")
                    || methodName.contains("updateCustomField");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && methodName.equals("getCustomFields")) {
                _customFields = (CustomFields) successObject;
                populateUi();
                AppMessagingClient.setLoading(false);
            } else if (methodName.equals("updateCustomField")) {
                WorkordersWebApi.getCustomFields(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
                AppMessagingClient.setLoading(true);
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, CustomFieldsDialog.class, params);
    }
}
