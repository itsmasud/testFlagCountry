package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Problem;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 5/24/17.
 */

public class UnresolvedProblemsDialog extends FullScreenDialog {
    private static final String TAG = "UnresolvedProblemsDialog";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _recyclerView;

    // Data
    private WorkOrder _workOrder;
    private List<Problem> _problems = new LinkedList<>();

    // Services
    private WorkordersWebApi _workOrderApi;

    public UnresolvedProblemsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_unresolved_problems, container, false);

        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        _toolbar.setTitle("Unresolved Problems");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _recyclerView = (OverScrollRecyclerView) v.findViewById(R.id.recyclerView);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _recyclerView.setAdapter(_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        _adapter.notifyDataSetChanged();

        _workOrderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workOrderApi.connect(App.get());
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _workOrder = params.getParcelable("workOrder");

        super.show(params, animate);

        generateProblemsList();
    }

    private void generateProblemsList() {
        _problems.clear();

        if (_workOrder == null || _workOrder.getProblems().getResults().length == 0)
            return;

        Problem[] problems = _workOrder.getProblems().getResults();
        for (Problem problem : problems) {
            if (problem.getActionsSet().contains(Problem.ActionsEnum.RESOLVE)) {
                _problems.add(problem);
            }
        }

        _adapter.notifyDataSetChanged();

        if (_problems.size() == 0)
            dismiss(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (_workOrderApi != null) _workOrderApi.disconnect(App.get());
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private final RecyclerView.Adapter<ViewHolder> _adapter = new RecyclerView.Adapter<ViewHolder>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ProblemRowView v = new ProblemRowView(parent.getContext());
            v.setOnClickListener(_problemRow_onClick);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((ProblemRowView) holder.itemView).setProblem(_problems.get(position));
        }

        @Override
        public int getItemCount() {
            return _problems.size();
        }
    };

    private final View.OnClickListener _problemRow_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ProblemRowView) {
                ProblemRowView prv = (ProblemRowView) v;
                ResolveProblemDialog.show(App.get(), null, _workOrder.getId(), prv.getProblem());
            }
        }
    };

    private WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderApi.subWorkordersWebApi();
        }

        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return true;
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (successObject != null && successObject instanceof WorkOrder) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (_workOrder.getId().equals(workOrder.getId())) {
                    _workOrder = workOrder;
                    generateProblemsList();
                }
            }
        }
    };

    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable("workOrder", workOrder);

        Controller.show(context, uid, UnresolvedProblemsDialog.class, params);
    }
}
