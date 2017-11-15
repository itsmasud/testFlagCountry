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
 * Created by Shoaib on 11/13/17.
 */

public class BonusesAdapter extends RecyclerView.Adapter<BonusViewHolder> {
    private static final String TAG = "BonusesAdapter";

    private PayModifier[] _bonuses;
    private String _valueTitle;
    private String _valueDescription;


    private List<DataHolder> dataHolders = new LinkedList<>();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BONUS = 1;

    private static class DataHolder {
        int type;
        Object object;

        public DataHolder(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    public void setBonuses(PayModifier[] bonuses) {
        dataHolders.clear();
        _bonuses = bonuses;
        dataHolders.add(new DataHolder(TYPE_HEADER, App.get().getResources().getString(R.string.bonuses_statement)));

        for (PayModifier bonus : bonuses) {
            dataHolders.add(new DataHolder(TYPE_BONUS, bonus));
        }

        rebuild();
    }

    private void rebuild() {
        notifyDataSetChanged();
    }

    @Override
    public BonusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BonusViewHolder holder = null;
        switch (viewType) {
            case TYPE_HEADER: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new BonusViewHolder(view);
            }
            case TYPE_BONUS: {
                ListItemTwoHorizTwoVertView view = new ListItemTwoHorizTwoVertView(parent.getContext());
                return new BonusViewHolder(view);
            }
        }
        return null;

    }

    @Override
    public void onBindViewHolder(BonusViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object, Gravity.LEFT, ContextCompat.getColor(App.get(), R.color.fn_dark_text));
                break;
            }
            case TYPE_BONUS: {
                ListItemTwoHorizTwoVertView v = (ListItemTwoHorizTwoVertView) holder.itemView;
                PayModifier bonus = (PayModifier) dataHolders.get(position).object;

                v.setTag(bonus);
                _valueTitle = "+" + misc.toCurrency(bonus.getAmount());
                _valueDescription = bonus.getCalculation().equals(PayModifier.CalculationEnum.FIXED) ?
                        null : (misc.to2Decimal(bonus.getModifier()) + "% of labor");
                v.set(bonus.getName(), bonus.getDescription(), _valueTitle, _valueDescription);
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
