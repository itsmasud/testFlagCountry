package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.PayModifier;

/**
 * Created by mc on 10/10/17.
 */

public class DiscountsAdapter extends RecyclerView.Adapter<DiscountViewHolder> {
    private static final String TAG = "DiscountsAdapter";

    private PayModifier[] discounts;
    private Listener _listener;


    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setDiscounts(PayModifier[] discounts) {
        this.discounts = discounts;
        rebuild();
    }

    private void rebuild() {
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
        v.setTag(discounts[position]);
        v.setOnLongClickListener(_discount_onLongClick);
        v.set(discounts[position].getName(), misc.toCurrency(discounts[position].getAmount()));
    }

    private final View.OnLongClickListener _discount_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            PayModifier payModifier = (PayModifier) v.getTag();
            if (payModifier.getActionsSet().contains(PayModifier.ActionsEnum.DELETE)) {
                _listener.onLongClick(v, payModifier);
                return true;
            }
            return false;
        }
    };

    @Override
    public int getItemCount() {
        if (discounts == null)
            return 0;
        return discounts.length;
    }

    public interface Listener {
        void onLongClick(View v, PayModifier discount);
    }
}
