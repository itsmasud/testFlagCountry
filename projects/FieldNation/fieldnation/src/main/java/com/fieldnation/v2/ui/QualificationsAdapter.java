package com.fieldnation.v2.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.data.model.SelectionRuleCriteria;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 11/14/17.
 */

public class QualificationsAdapter extends RecyclerView.Adapter<QualificationsViewHolder> {
    private static final String TAG = "QualificationsAdapter";

    private SelectionRuleCriteria[] _qualifications;
    private List<DataHolder> dataHolders = new LinkedList<>();

    private static final int TYPE_DISCLAIMER = 0;
    private static final int TYPE_MATCHED_GROUP = 1;
    private static final int TYPE_UNMATCHED_GROUP = 2;
    private static final int TYPE_QUALIFICATION = 3;

    private static class DataHolder {
        int type;
        Object object;

        public DataHolder(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    public void setQualifications(SelectionRuleCriteria[] qualifications) {
        dataHolders.clear();
        _qualifications = qualifications;

        rebuild();
    }

    private void rebuild() {


        final List<SelectionRuleCriteria> matchedQualifications = new LinkedList<>();
        final List<SelectionRuleCriteria> unmatchedQualifications = new LinkedList<>();

        for (SelectionRuleCriteria qualification : _qualifications) {
            if (qualification.getStatus() == null) continue;

            if (qualification.getStatus().equals(SelectionRuleCriteria.StatusEnum.MATCH)) {
                matchedQualifications.add(qualification);
            } else unmatchedQualifications.add(qualification);
        }


        dataHolders.add(new DataHolder(TYPE_DISCLAIMER, App.get().getResources().getString(R.string.qualifications_statement)));

        if (unmatchedQualifications.size() != 0)
            dataHolders.add(new DataHolder(TYPE_UNMATCHED_GROUP, App.get().getResources().getString(R.string.qualifications_missing)));

        for (int i = 0; i < unmatchedQualifications.size(); i++) {
            dataHolders.add(new DataHolder(TYPE_QUALIFICATION, unmatchedQualifications.get(i)));
        }

        if (matchedQualifications.size() != 0)
            dataHolders.add(new DataHolder(TYPE_MATCHED_GROUP, App.get().getResources().getString(R.string.qualifications_met)));

        for (int i = 0; i < matchedQualifications.size(); i++) {
            dataHolders.add(new DataHolder(TYPE_QUALIFICATION, matchedQualifications.get(i)));
        }


        notifyDataSetChanged();
    }

    @Override
    public QualificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QualificationsViewHolder holder = null;
        switch (viewType) {
            case TYPE_DISCLAIMER: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new QualificationsViewHolder(view);
            }
            case TYPE_UNMATCHED_GROUP: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new QualificationsViewHolder(view);
            }
            case TYPE_MATCHED_GROUP: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new QualificationsViewHolder(view);
            }
            case TYPE_QUALIFICATION: {
                ListItemTwoVertView view = new ListItemTwoVertView(parent.getContext());
                return new QualificationsViewHolder(view);
            }
        }
        return null;

    }

    @Override
    public void onBindViewHolder(QualificationsViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_DISCLAIMER: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object, Gravity.LEFT, ContextCompat.getColor(App.get(), R.color.fn_dark_text));
                view.setDividerVisiblity(View.GONE);
                break;
            }
            case TYPE_UNMATCHED_GROUP: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object, Gravity.LEFT, ContextCompat.getColor(App.get(), R.color.fn_light_text_80));
                view.setIcon(App.get().getResources().getString(R.string.icon_x), ContextCompat.getColor(App.get(), R.color.fn_red));
                break;
            }
            case TYPE_MATCHED_GROUP: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object, Gravity.LEFT, ContextCompat.getColor(App.get(), R.color.fn_light_text_80));
                view.setIcon(App.get().getResources().getString(R.string.icon_checkmark), ContextCompat.getColor(App.get(), R.color.fn_accent_color_medium));
                break;
            }

            case TYPE_QUALIFICATION: {
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                view.set(((SelectionRuleCriteria) dataHolders.get(position).object).getDescription(), null);
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
