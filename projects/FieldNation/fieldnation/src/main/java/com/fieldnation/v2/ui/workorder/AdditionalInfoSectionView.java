package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 11/2/17.
 */

public class AdditionalInfoSectionView extends LinearLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "AdditionalInfoSectionView";

    private List<WorkOrderRenderer> _renderers = new LinkedList<>();
    private String _uiUUID;

    public AdditionalInfoSectionView(Context context) {
        super(context);
        init();
    }

    public AdditionalInfoSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdditionalInfoSectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_section_additional_info, this, true);

        if (isInEditMode()) return;

        _renderers.add((WorkOrderRenderer) findViewById(R.id.signaturesSummaryView));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.shipmentSummaryView));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.attachment_summary_view));
    }

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
