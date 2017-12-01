package com.fieldnation.v2.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 11/21/17.
 */

public class EarnedPayAdapter extends RecyclerView.Adapter<EarnedPayViewHolder> {
    private static final String TAG = "EarnedPayAdapter";

    private String _valueTitle;
    private String _valueDescription;


    private List<DataHolder> dataHolders = new LinkedList<>();

    private static final int TYPE_KEY_VALUE = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_BONUS = 2;
    private static final int TYPE_PENALTY = 3;
    private static final int TYPE_EXPENSE = 4;

    private static class DataHolder {
        int type;
        Object object;

        public DataHolder(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    private static class KeyValueTuple {
        String key;
        String value;

        public KeyValueTuple(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public void setPay(Pay pay) {
        dataHolders.clear();

        // earned pay
        if (pay.getTotal() != null) {
            dataHolders.add(new DataHolder(TYPE_KEY_VALUE, new KeyValueTuple(App.get().getString(R.string.earned_pay), misc.toCurrency(pay.getTotal()))));
        }

        // header for payment breakdown
        dataHolders.add(new DataHolder(TYPE_HEADER, App.get().getResources().getString(R.string.payment_breakdown)));

        // labor earnings
        if (pay.getLaborSum() != null) {
            dataHolders.add(new DataHolder(TYPE_KEY_VALUE, new KeyValueTuple(App.get().getString(R.string.labor_earnings), misc.toCurrency(pay.getLaborSum()))));
        }

        // bonus
        for (PayModifier bonus : pay.getBonuses().getResults()) {
            if (bonus == null || misc.isEmptyOrNull(bonus.getName()) || misc.isEmptyOrNull(bonus.getDescription()) || bonus.getCalculation() == null || bonus.getAmount() == null)
                continue;
            dataHolders.add(new DataHolder(TYPE_BONUS, bonus));
        }
        // penalty
        for (PayModifier penalty : pay.getPenalties().getResults()) {
            if (penalty == null || misc.isEmptyOrNull(penalty.getName()) || misc.isEmptyOrNull(penalty.getDescription()) || penalty.getCalculation() == null || penalty.getAmount() == null)
                continue;
            dataHolders.add(new DataHolder(TYPE_PENALTY, penalty));
        }
        // expenses
        for (Expense expense : pay.getExpenses().getResults()) {
            if (expense == null || expense.getCategory() == null || misc.isEmptyOrNull(expense.getCategory().getName())
                    || misc.isEmptyOrNull(expense.getDescription()) || expense.getAmount() == null)
                continue;

            dataHolders.add(new DataHolder(TYPE_EXPENSE, expense));
        }
        // discount
        for (PayModifier discount : pay.getDiscounts().getResults()) {
            if (discount == null || discount.getName() == null || discount.getAmount() == null)
                continue;
            dataHolders.add(new DataHolder(TYPE_KEY_VALUE, new KeyValueTuple(discount.getName(), "-" + misc.toCurrency(discount.getAmount()))));
        }

        PayModifier[] fees = pay.getFees();
        if (fees != null) {
            for (PayModifier fee : fees) {
                if (fee.getName() != null
                        && fee.getName().equals("insurance")
                        && fee.getAmount() != null
                        && fee.getModifier() != null) {

                    dataHolders.add(new DataHolder(TYPE_KEY_VALUE,
                            new KeyValueTuple(String.format(App.get().getString(R.string.fieldnation_expected_insurance_percentage), String.valueOf(misc.to2Decimal((double) (fee.getModifier() * 100.0)))),
                                    "-" + misc.toCurrency(fee.getAmount()))));
                } else if (fee.getName() != null
                        && fee.getName().equals("provider")
                        && fee.getAmount() != null
                        && fee.getModifier() != null) {
                    dataHolders.add(new DataHolder(TYPE_KEY_VALUE,
                            new KeyValueTuple(String.format(App.get().getString(R.string.fieldnation_expected_fee_percentage), String.valueOf(misc.to2Decimal((double) (fee.getModifier() * 100.0)))),
                                    "-" + misc.toCurrency(fee.getAmount()))));

                }
            }
        }

        rebuild();
    }

    private void rebuild() {
        notifyDataSetChanged();
    }

    @Override
    public EarnedPayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EarnedPayViewHolder holder = null;
        switch (viewType) {
            case TYPE_KEY_VALUE: {
                ListItemTwoHorizView view = new ListItemTwoHorizView(parent.getContext());
                return new EarnedPayViewHolder(view);
            }
            case TYPE_HEADER: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new EarnedPayViewHolder(view);
            }
            case TYPE_BONUS: {
                ListItemTwoHorizTwoVertView view = new ListItemTwoHorizTwoVertView(parent.getContext());
                return new EarnedPayViewHolder(view);
            }
            case TYPE_PENALTY: {
                ListItemTwoHorizTwoVertView view = new ListItemTwoHorizTwoVertView(parent.getContext());
                return new EarnedPayViewHolder(view);
            }
            case TYPE_EXPENSE: {
                ListItemTwoHorizTwoVertView view = new ListItemTwoHorizTwoVertView(parent.getContext());
                return new EarnedPayViewHolder(view);
            }
        }
        return null;

    }

    @Override
    public void onBindViewHolder(EarnedPayViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_KEY_VALUE: {
                ListItemTwoHorizView view = (ListItemTwoHorizView) holder.itemView;
                view.set(((KeyValueTuple) (dataHolders.get(position).object)).key, ((KeyValueTuple) (dataHolders.get(position).object)).value);
                break;
            }
            case TYPE_HEADER: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object, Gravity.LEFT, ContextCompat.getColor(App.get(), R.color.fn_dark_text_50));
                break;
            }
            case TYPE_BONUS: {
                ListItemTwoHorizTwoVertView view = (ListItemTwoHorizTwoVertView) holder.itemView;
                PayModifier bonus = (PayModifier) dataHolders.get(position).object;

                view.setTag(bonus);
                _valueTitle = "+" + misc.toCurrency(bonus.getAmount());
                _valueDescription = bonus.getCalculation().equals(PayModifier.CalculationEnum.FIXED) ?
                        null : (misc.to2Decimal(bonus.getModifier()) + "% of labor");
                view.set(bonus.getName(), bonus.getDescription(), _valueTitle, _valueDescription);
                break;
            }
            case TYPE_PENALTY: {
                ListItemTwoHorizTwoVertView view = (ListItemTwoHorizTwoVertView) holder.itemView;
                PayModifier penalty = (PayModifier) dataHolders.get(position).object;

                view.setTag(penalty);
                _valueTitle = "-" + misc.toCurrency(penalty.getAmount());
                _valueDescription = penalty.getCalculation().equals(PayModifier.CalculationEnum.FIXED) ?
                        null : (misc.to2Decimal(penalty.getModifier()) + "% of labor");
                view.set(penalty.getName(), penalty.getDescription(), _valueTitle, _valueDescription);
                break;
            }
            case TYPE_EXPENSE: {
                ListItemTwoHorizTwoVertView view = (ListItemTwoHorizTwoVertView) holder.itemView;
                Expense expense = (Expense) dataHolders.get(position).object;
                view.setTag(expense);
                _valueTitle = "+" + misc.toCurrency(expense.getAmount());
                view.set(expense.getDescription(), expense.getCategory().getName(), _valueTitle, null);
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
