package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFieldCategory;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Shoaib on 09/24/17.
 */

public class TaskWidgetView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "TaskWidgetView";

    // Ui
    private TextView _previsitCountTextView;
    private TextView _onsiteCountTextView;
    private TextView _postvisitCountTextView;
    private TextView _ftecountTextView;

    // Data
    private WorkOrder _workOrder;

    public TaskWidgetView(Context context) {
        super(context);
        init();
    }

    public TaskWidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskWidgetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_problem_summary, this);

        if (isInEditMode())
            return;

        _previsitCountTextView = findViewById(R.id.previsit_count_textview);
        _onsiteCountTextView = findViewById(R.id.onsite_count_textview);
        _postvisitCountTextView = findViewById(R.id.postvisit_count_textview);
        _ftecountTextView = findViewById(R.id.fte_count_textview);

        _previsitCountTextView.setOnClickListener(_previsit_onClick);
        _onsiteCountTextView.setOnClickListener(_onsite_onClick);
        _postvisitCountTextView.setOnClickListener(_postvisit_onClick);
        _ftecountTextView.setOnClickListener(_fte_onClick);
        setVisibility(GONE);

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

        if (_previsitCountTextView == null)
            return;

        final List<Task> tasks = Arrays.asList(_workOrder.getTasks().getResults());

        ForLoopRunnable r = new ForLoopRunnable(tasks.size(), new Handler()) {
            private int preTotal = 0;
            private int preComplete = 0;
            private int onsTotal = 0;
            private int onsComplete = 0;
            private int postTotal = 0;
            private int postComplete = 0;

            @Override
            public void next(int i) throws Exception {
                Task task = tasks.get(i);

                if ("prep".equals(task.getGroup().getId())) {

                    if (task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                        preComplete++;
                    }
                    preTotal++;

                } else if ("onsite".equals(task.getGroup().getId())) {
                    if (task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                        onsComplete++;
                    }
                    onsTotal++;
                } else if ("post".equals(task.getGroup().getId())) {
                    if (task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                        postComplete++;
                    }
                    postTotal++;
                }
            }

            @Override
            public void finish(int count) throws Exception {

                // published
                if (_workOrder.getStatus().getId() == 2) {
                    _previsitCountTextView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.round_rect_gray));
                    _onsiteCountTextView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.round_rect_gray));
                    _postvisitCountTextView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.round_rect_gray));

                    _previsitCountTextView.setText(String.valueOf(preTotal));
                    _onsiteCountTextView.setText(String.valueOf(onsTotal));
                    _postvisitCountTextView.setText(String.valueOf(postTotal));
                }
                // assigned
                else if (_workOrder.getStatus().getId() == 3) {
                    _previsitCountTextView.setBackgroundDrawable(preTotal > preComplete ? getContext().getResources().getDrawable(R.drawable.round_rect_red)
                            : getContext().getResources().getDrawable(R.drawable.round_rect_green));
                    _onsiteCountTextView.setBackgroundDrawable(onsTotal > onsComplete ? getContext().getResources().getDrawable(R.drawable.round_rect_red)
                            : getContext().getResources().getDrawable(R.drawable.round_rect_green));
                    _postvisitCountTextView.setBackgroundDrawable(postTotal > postComplete ? getContext().getResources().getDrawable(R.drawable.round_rect_red)
                            : getContext().getResources().getDrawable(R.drawable.round_rect_green));

                    _previsitCountTextView.setText(preComplete + "/" + preTotal);
                    _onsiteCountTextView.setText(onsComplete + "/" + onsTotal);
                    _postvisitCountTextView.setText(postComplete + "/" + postTotal);

                } else {
                    _previsitCountTextView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.round_rect_gray));
                    _onsiteCountTextView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.round_rect_gray));
                    _postvisitCountTextView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.round_rect_gray));

                    _previsitCountTextView.setText(preComplete + "/" + preTotal);
                    _onsiteCountTextView.setText(onsComplete + "/" + onsTotal);
                    _postvisitCountTextView.setText(postComplete + "/" + postTotal);

                }

            }
        };
        postDelayed(r, new Random().nextInt(100));


// custom fields
        CustomFieldCategory providerCustomField = null;
        for (CustomFieldCategory category : _workOrder.getCustomFields().getResults()) {
            if (category.getRole().equals("assigned_provider")) {
                providerCustomField = category;
                break;
            }
        }

        final List<CustomField> customFields = Arrays.asList(providerCustomField.getResults());


        ForLoopRunnable r2 = new ForLoopRunnable(customFields.size(), new Handler()) {

            private int fteTotal = 0;
            private int fteComplete = 0;

            @Override
            public void next(int i) throws Exception {
                CustomField cf = customFields.get(i);

                if (!misc.isEmptyOrNull(cf.getValue())) {
                    fteComplete++;
                }
                fteTotal++;
            }

            @Override
            public void finish(int count) throws Exception {

                if (_workOrder.getStatus().getId() == 2) {
                    _ftecountTextView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.round_rect_gray));
                    _ftecountTextView.setText(String.valueOf(fteTotal));

                } else if (_workOrder.getStatus().getId() == 3) {
                    _ftecountTextView.setBackgroundDrawable(fteTotal > fteComplete ? getContext().getResources().getDrawable(R.drawable.round_rect_red)
                            : getContext().getResources().getDrawable(R.drawable.round_rect_gray));
                    _ftecountTextView.setText(fteComplete + "/" + fteTotal);

                } else {
                    _ftecountTextView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.round_rect_gray));
                    _postvisitCountTextView.setText(fteComplete + "/" + fteTotal);
                }

            }
        };
        postDelayed(r2, new Random().nextInt(100));

        setVisibility(VISIBLE);
    }

    private final OnClickListener _previsit_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private final OnClickListener _onsite_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private final OnClickListener _postvisit_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private final OnClickListener _fte_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };


}
