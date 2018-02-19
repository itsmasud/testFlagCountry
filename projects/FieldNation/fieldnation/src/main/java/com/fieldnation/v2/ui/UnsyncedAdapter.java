package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.ui.workorder.WorkOrderActivity;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michaelcarver on 2/19/18.
 */

public class UnsyncedAdapter extends RecyclerView.Adapter<UnsyncedViewHolder> {
    private static final String TAG = "UnsyncedAdapter";


    // activity name - need user friendly name
    // Date/time
    // work order id

    private Hashtable<Integer, List<Tuple>> webTransactions = new Hashtable<>();
    private List<Tuple> transactions = new LinkedList<>();


    private static class Tuple {
        public int type;
        public WebTransaction webTransaction;
        public int workOrderId;
        public long timestamp;
        public String apiFunction;

        public Tuple(int type, WebTransaction webTransaction, int workOrderId, long timestamp, String apiFunction) {
            this.type = type;
            this.webTransaction = webTransaction;
            this.workOrderId = workOrderId;
            this.timestamp = timestamp;
            this.apiFunction = apiFunction;
        }

        public Tuple(int type, int workOrderId) {
            this.type = type;
            this.workOrderId = workOrderId;
        }
    }

    private List<Tuple> getList(int workOrderId) {
        if (!webTransactions.containsKey(workOrderId)) {
            webTransactions.put(workOrderId, new LinkedList<Tuple>());
        }

        return webTransactions.get(workOrderId);
    }

    public void refresh() {
        webTransactions.clear();
        List<WebTransaction> list = WebTransaction.getSyncing();
        for (WebTransaction wt : list) {
            try {
                TransactionParams tl = TransactionParams.fromJson(new JsonObject(wt.getListenerParams()));
                int workOrderId = tl.getMethodParamInt("workOrderId");
                getList(workOrderId).add(new Tuple(UnsyncedViewHolder.TYPE_TRANSACTION, wt,
                        workOrderId, wt.getCreatedTime(), tl.apiFunction));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        transactions.clear();
        Enumeration<Integer> keys = webTransactions.keys();
        while (keys.hasMoreElements()) {
            int key = keys.nextElement();
            transactions.add(new Tuple(UnsyncedViewHolder.TYPE_HEADER, key));
            transactions.addAll(webTransactions.get(key));
            transactions.add(new Tuple(UnsyncedViewHolder.TYPE_VIEW_WO, key));
        }

        notifyDataSetChanged();
    }


    @Override
    public UnsyncedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UnsyncedViewHolder holder = null;

        switch (viewType) {
            case UnsyncedViewHolder.TYPE_HEADER: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                holder = new UnsyncedViewHolder(view);
                holder.type = viewType;
                break;
            }
            case UnsyncedViewHolder.TYPE_TRANSACTION: {
                ListItemTwoVertView view = new ListItemTwoVertView(parent.getContext());
                view.setAlertVisible(true);
                view.setActionVisible(false);
                view.setProgressVisible(false);
                holder = new UnsyncedViewHolder(view);
                holder.type = viewType;
                break;
            }
            case UnsyncedViewHolder.TYPE_VIEW_WO: {
                ListItemTwoVertView view = new ListItemTwoVertView(parent.getContext());
                view.setOnClickListener(_view_onClick);
                holder = new UnsyncedViewHolder(view);
                holder.type = viewType;
                break;
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(UnsyncedViewHolder holder, int position) {
        switch (holder.type) {
            case UnsyncedViewHolder.TYPE_HEADER: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle("WO " + transactions.get(position).workOrderId);
                break;
            }
            case UnsyncedViewHolder.TYPE_TRANSACTION: {
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                Tuple t = transactions.get(position);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(t.timestamp);
                view.set(t.apiFunction, DateUtils.formatDateLong(calendar));
                break;
            }
            case UnsyncedViewHolder.TYPE_VIEW_WO: {
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                view.set("View Work Order Details", null);
                view.setTag((Integer) transactions.get(position).workOrderId);
                break;
            }
        }
    }

    private final View.OnClickListener _view_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Integer workOrderId = (Integer) view.getTag();
            WorkOrderActivity.startNew(App.get(), workOrderId);
        }
    };

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return transactions.get(position).type;
    }
}
