package com.fieldnation.v2.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFieldCategory;
import com.fieldnation.v2.data.model.CustomFields;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 9/28/17.
 */

public abstract class CustomFieldsAdapter extends RecyclerView.Adapter<CustomFieldViewHolder> {
    private static final String TAG = "CustomFieldsAdapter";

    private List<DataHolder> dataHolders = new LinkedList<>();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CUSTOM_FIELD = 1;

    private static class DataHolder {
        int type;
        Object object;

        public DataHolder(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    public void setCustomFields(CustomFields customFields) {
        dataHolders.clear();
        CustomFieldCategory[] categories = customFields.getResults();
        for (CustomFieldCategory category : categories) {
            if (category.getRole().equals("buyer"))
                continue;

            dataHolders.add(new DataHolder(TYPE_HEADER, category.getName()));
            CustomField[] fields = category.getResults();
            for (CustomField customField : fields) {
                dataHolders.add(new DataHolder(TYPE_CUSTOM_FIELD, customField));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public CustomFieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CustomFieldViewHolder holder = null;
        switch (viewType) {
            case TYPE_HEADER: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new CustomFieldViewHolder(view);
            }
            case TYPE_CUSTOM_FIELD: {
                ListItemTwoVertView view = new ListItemTwoVertView(parent.getContext());
                return new CustomFieldViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(CustomFieldViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object);
                break;
            }
            case TYPE_CUSTOM_FIELD: {
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                CustomField customField = (CustomField) dataHolders.get(position).object;
                view.setOnClickListener(_customField_onClick);
                view.setOnLongClickListener(_customField_onLongClick);
                view.set(
                        (customField.getFlagsSet().contains(CustomField.FlagsEnum.REQUIRED) ? "* " : "")
                                + customField.getName(),
                        misc.isEmptyOrNull(customField.getValue()) ? customField.getTip() : customField.getValue());
                view.setTag(customField);
                view.setActionVisible(false);
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

    private final View.OnClickListener _customField_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onCustomFieldClicked((CustomField) view.getTag());
        }
    };

    private final View.OnLongClickListener _customField_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            CustomField customField = (CustomField) view.getTag();
            ClipboardManager clipboard = (ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(customField.getName(), customField.getValue());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), R.string.toast_copied_to_clipboard, Toast.LENGTH_LONG);
            return true;
        }
    };

    public abstract void onCustomFieldClicked(CustomField customField);
}
