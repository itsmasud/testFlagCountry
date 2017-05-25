package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.model.WorkOrder;

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
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _workOrder = params.getParcelable("workOrder");

        super.show(params, animate);

        _adapter.notifyDataSetChanged();
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
            return new ViewHolder(new ProblemRowView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // TODO bind the position
        }

        @Override
        public int getItemCount() {
            if (_workOrder != null
                    && _workOrder.getProblems() != null
                    && _workOrder.getProblems().getResults() != null)
                return _workOrder.getProblems().getResults().length;

            return 0;
        }
    };

    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable("workOrder", workOrder);

        Controller.show(context, uid, UnresolvedProblemsDialog.class, params);
    }
}
