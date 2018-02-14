package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.menu.ResolveMenuButton;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Problem;
import com.fieldnation.v2.data.model.ProblemResolution;

/**
 * Created by mc on 5/25/17.
 */

public class ResolveProblemDialog extends FullScreenDialog {
    private static final String TAG = "ResolveProblemDialog";

    // Ui
    private Toolbar _toolbar;
    private TextView _titleTextView;
    private TextView _commentsTextView;
    private Button _resolveButton;
    private RefreshView _refreshView;

    // Data
    private Problem _problem;
    private int _workOrderId;

    public ResolveProblemDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_resolve_problem, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setTitle("Problem");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _titleTextView = v.findViewById(R.id.title_textview);

        _commentsTextView = v.findViewById(R.id.comments_textview);

        _refreshView = v.findViewById(R.id.refresh_view);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _toolbar.inflateMenu(R.menu.resolve_problem);

        _resolveButton = ((ResolveMenuButton) _toolbar.getMenu().findItem(R.id.resolve_menuitem).getActionView()).getButton();
        _resolveButton.setOnClickListener(_resolve_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();

        _workordersApi.sub();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _problem = params.getParcelable("problem");
        _workOrderId = params.getInt("workOrderId");

        _titleTextView.setText(_problem.getType().getName());
        _commentsTextView.setText(_problem.getComments());

        if (_problem.getActionsSet().contains(Problem.ActionsEnum.RESOLVE)) {
            _resolveButton.setEnabled(true);
        } else {
            _resolveButton.setEnabled(false);
        }

        super.show(params, animate);
    }

    @Override
    public void onPause() {
        _workordersApi.unsub();

        super.onPause();
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    private final View.OnClickListener _resolve_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "_resolve_onClick");
            try {
                SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                uiContext.page += " - Resolve Problem Dialog";

                Problem problem = new Problem()
                        .id(_problem.getId())
                        .resolution(new ProblemResolution()
                                .status(ProblemResolution.StatusEnum.RESOLVED));

                WorkordersWebApi.updateProblem(App.get(), _workOrderId, _problem.getId(), problem, uiContext);
                _refreshView.startRefreshing();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final WorkordersWebApi _workordersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return methodName.equals("updateProblem");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (methodName.equals("updateProblem") && success) {
                PigeonRoost.clearAddressCacheAll("ADDRESS_WEB_API_V2/WorkordersWebApi");
                WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
                dismiss(true);
            } else {
                Log.v(TAG, "onComplete");
            }
            _refreshView.refreshComplete();
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId, Problem problem) {
        Bundle params = new Bundle();
        params.putParcelable("problem", problem);
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, ResolveProblemDialog.class, params);
    }
}
