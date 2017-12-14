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
import com.fieldnation.v2.data.model.Problems;
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
    private List<Problem> _problemList = new LinkedList<>();
    private int _workOrderId;
    private Problems _problems;

    public UnresolvedProblemsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_unresolved_problems, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setTitle("Unresolved Problems");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _recyclerView = v.findViewById(R.id.recyclerView);

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

        _workOrdersApi.sub();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _workOrderId = params.getInt("workOrderId");
        _problems = params.getParcelable("problems");

        super.show(params, animate);

        generateProblemsList();
    }

    private void generateProblemsList() {
        _problemList.clear();

        if (_problems.getResults().length == 0)
            return;

        Problem[] problems = _problems.getResults();
        for (Problem problem : problems) {
            if (problem.getActionsSet().contains(Problem.ActionsEnum.RESOLVE)) {
                _problemList.add(problem);
            }
        }

        _adapter.notifyDataSetChanged();

        if (_problemList.size() == 0)
            dismiss(true);
    }

    @Override
    public void onPause() {
        _workOrdersApi.unsub();
        super.onPause();
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
            ((ProblemRowView) holder.itemView).setProblem(_problemList.get(position));
        }

        @Override
        public int getItemCount() {
            return _problemList.size();
        }
    };

    private final View.OnClickListener _problemRow_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ProblemRowView) {
                ProblemRowView prv = (ProblemRowView) v;
                ResolveProblemDialog.show(App.get(), null, _workOrderId, prv.getProblem());
            }
        }
    };

    private WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return true;
        }

        @Override
        public boolean onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && successObject instanceof WorkOrder) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (_workOrderId == workOrder.getId()) {
                    _problems = workOrder.getProblems();
                    generateProblemsList();
                }
            }
            return super.onComplete(transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId, Problems problems) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("problems", problems);

        Controller.show(context, uid, UnresolvedProblemsDialog.class, params);
    }
}
