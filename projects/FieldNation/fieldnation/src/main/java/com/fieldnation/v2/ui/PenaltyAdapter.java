package com.fieldnation.v2.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.PayModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 11/07/17.
 */

public class PenaltyAdapter extends RecyclerView.Adapter<PenaltyViewHolder> {
    private static final String TAG = "PenaltyAdapter";

    private PayModifier[] _penalties;
    private String _valueTitle;
    private String _valueDescription;


    private List<DataHolder> dataHolders = new LinkedList<>();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PENALTY = 1;

    private static class DataHolder {
        int type;
        Object object;

        public DataHolder(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    public void setPenalties(PayModifier[] penalties) {
        dataHolders.clear();
        _penalties = penalties;
        dataHolders.add(new DataHolder(TYPE_HEADER, App.get().getResources().getString(R.string.penalty_statement)));

        for (PayModifier penalty : penalties) {
            dataHolders.add(new DataHolder(TYPE_PENALTY, penalty));
        }

        rebuild();
    }

    private void rebuild() {
        notifyDataSetChanged();
    }

    @Override
    public PenaltyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PenaltyViewHolder holder = null;
        switch (viewType) {
            case TYPE_HEADER: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new PenaltyViewHolder(view);
            }
            case TYPE_PENALTY: {
                ListItemTwoHorizTwoVertView view = new ListItemTwoHorizTwoVertView(parent.getContext());
                return new PenaltyViewHolder(view);
            }
        }
        return null;

    }

    @Override
    public void onBindViewHolder(PenaltyViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object, Gravity.LEFT, ContextCompat.getColor(App.get(), R.color.fn_dark_text));
                break;
            }
            case TYPE_PENALTY: {
                ListItemTwoHorizTwoVertView v = (ListItemTwoHorizTwoVertView) holder.itemView;
                PayModifier penalty = (PayModifier) dataHolders.get(position).object;

                v.setTag(penalty);
                _valueTitle = "-" + misc.toCurrency(penalty.getAmount());
                _valueDescription = penalty.getCalculation().equals(PayModifier.CalculationEnum.FIXED) ?
                        null : (misc.to2Decimal(penalty.getModifier()) + "% of labor");
                v.set(penalty.getName(), penalty.getDescription(), _valueTitle, _valueDescription);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataHolders.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataHolders.get(position).type;
    }

}
