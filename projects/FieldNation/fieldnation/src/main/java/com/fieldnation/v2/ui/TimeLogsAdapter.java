package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;

/**
 * Created by Shoaib on 10/20/17.
 */

public class TimeLogsAdapter extends RecyclerView.Adapter<TimeLogViewHolder> {
    private static final String TAG = "TimeLogsAdapter";

    private WorkOrder _workOrder;
    private Listener _listener;

    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        rebuild();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void rebuild() {
        notifyDataSetChanged();
    }

    @Override
    public TimeLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TimeLogRowView v = new TimeLogRowView(parent.getContext());
        return new TimeLogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TimeLogViewHolder holder, int position) {
        TimeLogRowView v = (TimeLogRowView) holder.itemView;
        TimeLog timeLog = _workOrder.getTimeLogs().getResults()[position];
        v.setTag(timeLog);
        v.setData(_workOrder, timeLog);

        if (App.get().getOfflineState() == App.OfflineState.OFFLINE || App.get().getOfflineState() == App.OfflineState.UPLOADING)
            v.setOnClickListener(_disable_onClick);
        else v.setOnClickListener(_timelog_onClick);


        v.setOnLongClickListener(_timelog_onLongClick);
// TODO       v.setEnabled(timeLog.getActionsSet().contains(TimeLog.ActionsEnum.EDIT));
    }

    @Override
    public int getItemCount() {
        if (_workOrder == null || _workOrder.getTimeLogs() == null || _workOrder.getTimeLogs().getResults() == null)
            return 0;
        return _workOrder.getTimeLogs().getResults().length;
    }

    private final View.OnLongClickListener _timelog_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            TimeLog timelog = (TimeLog) view.getTag();
            if (_listener != null
                    && timelog.getActionsSet().contains(TimeLog.ActionsEnum.DELETE)) {
                _listener.onLongClick(view, timelog);
                return true;
            }
            return false;
        }
    };

    private final View.OnClickListener _timelog_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View view) {
            TimeLog timelog = (TimeLog) view.getTag();
            if (_listener != null
                    && timelog.getActionsSet().contains(TimeLog.ActionsEnum.EDIT)) {
                _listener.onClick(view, timelog);
            }
        }
    };

    private final View.OnClickListener _disable_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TwoButtonDialog.show(App.get(), null, v.getContext().getResources().getString(R.string.not_available),
                    v.getContext().getResources().getString(R.string.not_available_body_text),
                    v.getContext().getResources().getString(R.string.btn_close), null, true, null);
        }
    };

    public interface Listener {
        void onLongClick(View v, TimeLog timelog);

        void onClick(View v, TimeLog timelog);
    }
}
