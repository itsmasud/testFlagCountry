package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
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
    private EditText _commentsEditText;
    private Button _resolveButton;

    // Data
    private Problem _problem;
    private int _workOrderId;

    // Services
    private WorkordersWebApi _workOrderApi;

    public ResolveProblemDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_resolve_problem, container, false);

        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        _toolbar.setTitle("Problem");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _commentsEditText = (EditText) v.findViewById(R.id.comments_edittext);

        return v;
    }

    @Override
    public void onStart() {
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _toolbar.inflateMenu(R.menu.resolve_problem);

        _resolveButton = ((ResolveMenuButton) _toolbar.getMenu().findItem(R.id.resolve_menuitem).getActionView()).getButton();
        _resolveButton.setOnClickListener(_resolve_onClick);

        _workOrderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workOrderApi.connect(App.get());

        super.onStart();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _problem = params.getParcelable("problem");
        _workOrderId = params.getInt("workOrderId");

        _titleTextView.setText(_problem.getType().getName());

        if (!_problem.getActionsSet().contains(Problem.ActionsEnum.RESOLVE)) {
            _resolveButton.setEnabled(false);
        }

        super.show(params, animate);
    }

    @Override
    public void onStop() {
        super.onStop();
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

                String comments = _commentsEditText.getText().toString();

                Problem problem = new Problem()
                        .id(_problem.getId())
                        .resolution(new ProblemResolution()
                                .status(ProblemResolution.StatusEnum.RESOLVED))
                        .comments(comments);

                WorkordersWebApi.updateProblem(App.get(), _workOrderId, _problem.getId(), problem, uiContext);

                // TODO start loading
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderApi.subWorkordersWebApi();
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("updateProblem") && success) {
                _workOrderApi.clearTopicAll("TOPIC_ID_WEB_API_V2/WorkordersWebApi");
                WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, false);
                dismiss(true);
            } else {

            }
            // TODO stop loading
        }
    };

    public static void show(Context context, String uid, int workOrderId, Problem problem) {
        Bundle params = new Bundle();
        params.putParcelable("problem", problem);
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, ResolveProblemDialog.class, params);
    }
}
