package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.ui.workorder.WorkOrderActivity;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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

    private SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d, yyyy @ h:mm a", Locale.getDefault());
    private DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
    private Listener _listener;


    private static class Tuple {
        public int type;
        public WebTransaction webTransaction;
        public int workOrderId;
        public long timestamp;
        public String activityName;
        public Double activityValue;

        public Tuple(int type, WebTransaction webTransaction, int workOrderId, long timestamp, String activityName, Double activityValue) {
            this.type = type;
            this.webTransaction = webTransaction;
            this.workOrderId = workOrderId;
            this.timestamp = timestamp;
            this.activityName = activityName;
            this.activityValue = activityValue;
        }

        public Tuple(int type, int workOrderId) {
            this.type = type;
            this.workOrderId = workOrderId;
        }
    }

    public UnsyncedAdapter() {
        symbols.setAmPmStrings(App.get().getResources().getStringArray(R.array.schedule_small_case_am_pm_array));
        sdf.setDateFormatSymbols(symbols);
    }

    private List<Tuple> getList(int workOrderId) {
        if (!webTransactions.containsKey(workOrderId)) {
            webTransactions.put(workOrderId, new LinkedList<Tuple>());
        }

        return webTransactions.get(workOrderId);
    }

    public void setListener(UnsyncedAdapter.Listener listener) {
        _listener = listener;
    }

    public int getWorkOrderCount() {
        return webTransactions.size();
    }

    public void refresh() {
        Stopwatch stopwatch = new Stopwatch(true);
        webTransactions.clear();
        List<WebTransaction> list = WebTransaction.getSyncing();


        for (WebTransaction wt : list) {
            try {
                TransactionParams tl = TransactionParams.fromJson(new JsonObject(wt.getListenerParams()));
                int workOrderId = tl.getMethodParamInt("workOrderId");
                String activityName = tl.getMethodParamString(WebTransaction.ActivityType.ACTIVITY_NAME.name());
                Double activityValue = tl.getMethodParamDouble(WebTransaction.ActivityType.ACTIVITY_VALUE.name());

                getList(workOrderId).add(new Tuple(UnsyncedViewHolder.TYPE_TRANSACTION, wt,
                        workOrderId, wt.getCreatedTime(), activityName, activityValue));
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

        Log.v(TAG, "refresh time " + stopwatch.finish());

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
                ListItemTwoHorizTwoVertView view = new ListItemTwoHorizTwoVertView(parent.getContext());
                view.setAlertVisible(false);
                view.setTitleEllipse(true);
                view.setOnLongClickListener(_view_onLongClick);
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
                ListItemTwoHorizTwoVertView view = (ListItemTwoHorizTwoVertView) holder.itemView;
                Tuple t = transactions.get(position);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(t.timestamp);

                view.set(t.activityName,
                        sdf.format(calendar.getTime()) + DateUtils.getDeviceTimezone(calendar),
                        t.activityValue == null ? null : misc.toCurrency(t.activityValue),
                        null);
                if (t.webTransaction.isZombie() || t.webTransaction.getTryCount() > 0) {
                    view.setAlertVisible(true);
                }
                view.setTag(R.string.const_activity_name, (String) transactions.get(position).activityName);
                view.setTag(R.string.const_web_transaction, (WebTransaction) transactions.get(position).webTransaction);

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

    private final View.OnLongClickListener _view_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null) {
                _listener.onLongClick(v);
                return true;
            }
            return false;
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

    public interface Listener {
        void onLongClick(View v);
    }

}
