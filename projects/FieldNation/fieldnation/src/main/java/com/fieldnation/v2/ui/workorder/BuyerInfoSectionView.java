package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 11/17/17.
 */

public class BuyerInfoSectionView extends LinearLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "BuyerInfoSectionView";

    private List<WorkOrderRenderer> _renderers = new LinkedList<>();
    private String _uiUUID;

    public BuyerInfoSectionView(Context context) {
        super(context);
        init();
    }

    public BuyerInfoSectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BuyerInfoSectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_section_buyer_info, this, true);

        if (isInEditMode()) return;

        _renderers.add((WorkOrderRenderer) findViewById(R.id.companySummary_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.contactSummary_view));

        setVisibility(GONE);
    }

    @Override
    public void setUUID(String uuid) {
        _uiUUID = uuid;
        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            if (workOrderRenderer instanceof UUIDView) {
                ((UUIDView) workOrderRenderer).setUUID(_uiUUID);
            }
        }

        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            workOrderRenderer.setWorkOrder(workOrder);
        }
        populateUi();
    }

    private void populateUi() {
        setVisibility(VISIBLE);
        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            if (((View) workOrderRenderer).getVisibility() == VISIBLE) {
                return;
            }
        }
        setVisibility(GONE);
    }
}
