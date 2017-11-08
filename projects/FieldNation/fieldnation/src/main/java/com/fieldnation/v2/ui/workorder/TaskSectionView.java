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
 * Created by mc on 11/2/17.
 */

public class TaskSectionView extends LinearLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "TaskSectionView";

    private List<WorkOrderRenderer> _renderers = new LinkedList<>();
    private WorkOrder _workOrder;
    private String _uiUUID;

    public TaskSectionView(Context context) {
        super(context);
        init();
    }

    public TaskSectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskSectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_section_tasks, this, true);

        if (isInEditMode()) return;

        _renderers.add((WorkOrderRenderer) findViewById(R.id.taskwidget_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.closingnotes_view));
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
        _workOrder = workOrder;
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
