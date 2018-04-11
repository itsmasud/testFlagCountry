package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionUtils;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.PayModifier;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 10/10/17.
 */

public class DiscountsAdapter extends RecyclerView.Adapter<DiscountViewHolder> {
    private static final String TAG = "DiscountsAdapter";

    private List<Object> objects = new LinkedList<>();
    private PayModifier[] _discounts;
    private List<Tuple> addedDiscounts = new LinkedList<>();
    private Hashtable<Integer, WebTransaction> deletedDiscounts = new Hashtable<>();
    private Listener _listener;
    private int _workOrderId;
    private int _running = 0;
    private boolean _runAgain = false;

    private static class Tuple {
        public WebTransaction webTransaction;
        public PayModifier payModifier;

        public Tuple(WebTransaction webTransaction, PayModifier payModifier) {
            this.webTransaction = webTransaction;
            this.payModifier = payModifier;
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setDiscounts(int workOrderId, PayModifier[] discounts) {
        Log.v(TAG, "setDiscounts");
        this._discounts = discounts;
        this._workOrderId = workOrderId;
        if (_running == 0) {
            _running = 2;
            addedDiscounts.clear();
            deletedDiscounts.clear();
            WebTransactionUtils.setData(_addDiscounts, WebTransactionUtils.KeyType.ADD_DISCOUNT, workOrderId);
            WebTransactionUtils.setData(_deleteDiscounts, WebTransactionUtils.KeyType.DELETE_DISCOUNT, workOrderId);
        } else {
            _runAgain = true;
        }
    }

    private void rebuild() {
        if (_runAgain) {
            _runAgain = false;
            setDiscounts(_workOrderId, _discounts);
            return;
        }

        objects.clear();

        for (PayModifier discount : _discounts) {
            if (deletedDiscounts.containsKey(discount.getId()))
                continue;

            objects.add(discount);
        }

        for (Tuple discount : addedDiscounts) {
            objects.add(discount);
        }

        notifyDataSetChanged();
    }

    @Override
    public DiscountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemTwoHorizView v = new ListItemTwoHorizView(parent.getContext());
        return new DiscountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DiscountViewHolder holder, int position) {
        ListItemTwoHorizView v = (ListItemTwoHorizView) holder.itemView;
        v.setTag(objects.get(position));
        v.setOnLongClickListener(_discount_onLongClick);
        Object object = objects.get(position);
        if (object instanceof Tuple) {
            v.setAlert(true);
            Tuple tuple = (Tuple) object;
            v.set(tuple.payModifier.getName(), misc.toCurrency(tuple.payModifier.getAmount()));
        } else if (object instanceof PayModifier) {
            v.setAlert(false);
            PayModifier payModifier = (PayModifier) object;
            v.set(payModifier.getName(), misc.toCurrency(payModifier.getAmount()));
        }
    }

    private final View.OnLongClickListener _discount_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Object object = v.getTag();
            WebTransaction webTransaction = null;
            PayModifier payModifier = null;
            if (object instanceof Tuple) {
                payModifier = ((Tuple) object).payModifier;
                webTransaction = ((Tuple) object).webTransaction;
            } else if (object instanceof PayModifier) {
                payModifier = (PayModifier) object;
            }
            if (payModifier.getActionsSet().contains(PayModifier.ActionsEnum.DELETE) || webTransaction != null) {
                _listener.onLongClick(v, payModifier, webTransaction);
                return true;
            }
            return false;
        }
    };

    private final WebTransactionUtils.Listener _addDiscounts = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                PayModifier discount = PayModifier.fromJson(new JsonObject(tp.methodParams).getJsonObject("json"));
                addedDiscounts.add(new Tuple(webTransaction, discount));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running--;
            if (_running == 0) rebuild();
        }
    };

    private final WebTransactionUtils.Listener _deleteDiscounts = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                int id = new JsonObject(tp.methodParams).getInt("discountId");
                deletedDiscounts.put(id, webTransaction);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running--;
            if (_running == 0) rebuild();
        }
    };

    @Override
    public int getItemCount() {
        if (objects == null)
            return 0;
        return objects.size();
    }

    public interface Listener {
        void onLongClick(View v, PayModifier discount, WebTransaction webTransaction);
    }
}
