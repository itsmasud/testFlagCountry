package com.fieldnation.v2.ui.workorder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFieldCategory;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTwoVertView;

/**
 * Created by mc on 9/28/17.
 */

public class BuyerCustomFieldView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "BuyerCustomFieldView";

    // Ui
    private LinearLayout _list;

    // Data
    private WorkOrder _workOrder;

    public BuyerCustomFieldView(Context context) {
        super(context);
        init();
    }

    public BuyerCustomFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BuyerCustomFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_buyer_custom_field_list, this, true);

        if (isInEditMode()) return;

        _list = findViewById(R.id.buyerCustomFieldList);

        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        _list.removeAllViews();
        StringBuilder sb = new StringBuilder();
        if (_workOrder.getCustomFields().getResults().length > 0) {
            // we have fields, find the buyer fields
            CustomFieldCategory[] categories = _workOrder.getCustomFields().getResults();
            for (CustomFieldCategory category : categories) {
                if (category.getRole() != null && category.getRole().equals("buyer")) {
                    if (category.getResults().length > 0) {
                        CustomField[] fields = category.getResults();
                        for (CustomField field : fields) {
                            ListItemTwoVertView view = new ListItemTwoVertView(getContext());
                            view.setOnLongClickListener(_this_onLongClick);
                            view.set(field.getName(), field.getValue());
                            view.setActionVisible(false);
                            _list.addView(view);
                        }
                    }
                }
            }
        }

        if (_list.getChildCount() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    private final View.OnLongClickListener _this_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ListItemTwoVertView v = (ListItemTwoVertView) view;
            if (misc.isEmptyOrNull(v.getKey()) || misc.isEmptyOrNull(v.getValue()))
                return false;

            ClipboardManager clipboard = (ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(v.getKey(), v.getValue());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), R.string.toast_copied_to_clipboard, Toast.LENGTH_LONG);
            return true;
        }
    };
}
