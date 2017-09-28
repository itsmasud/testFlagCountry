package com.fieldnation.v2.ui.workorder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFieldCategory;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTwoHorizView;

/**
 * Created by mc on 9/28/17.
 */

public class BuyerCustomFieldView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "BuyerCustomFieldView";

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
        setOrientation(VERTICAL);

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

        removeAllViews();
        StringBuilder sb = new StringBuilder();
        if (_workOrder.getCustomFields().getResults().length > 0) {
            // we have fields, find the buyer fields
            CustomFieldCategory[] categories = _workOrder.getCustomFields().getResults();
            for (CustomFieldCategory category : categories) {
                if (category.getRole() != null && category.getRole().equals("buyer")) {
                    if (category.getResults().length > 0) {
                        CustomField[] fields = category.getResults();
                        for (CustomField field : fields) {
                            ListItemTwoHorizView view = new ListItemTwoHorizView(getContext());
                            view.setOnLongClickListener(_this_onLongClick);
                            view.set(field.getName(), field.getValue());
                            addView(view);
                        }
                    }
                }
            }
        }

        if (getChildCount() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    private final View.OnLongClickListener _this_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ListItemTwoHorizView v = (ListItemTwoHorizView) view;
            ClipboardManager clipboard = (ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(v.getKey(), v.getValue());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), R.string.toast_copied_to_clipboard, Toast.LENGTH_LONG);
            return true;
        }
    };
}
