package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.ClosingNotesDialog;

public class ClosingNotesView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ClosingNotesView";

    // UI
    private TextView _notesTextView;

    // Data
    private WorkOrder _workOrder;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public ClosingNotesView(Context context) {
        this(context, null);
    }

    public ClosingNotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_wd_closing_notes, this);

        if (isInEditMode())
            return;

        _notesTextView = findViewById(R.id.notes_textview);
        setOnClickListener(_notes_onClick);

        setVisibility(View.GONE);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null || _workOrder.getActionsSet() == null) {
            setVisibility(View.GONE);
            return;
        }

        if (!misc.isEmptyOrNull(_workOrder.getClosingNotes())) {
            _notesTextView.setText(_workOrder.getClosingNotes());
            _notesTextView.setVisibility(VISIBLE);
        } else {
            _notesTextView.setVisibility(GONE);
        }
        setVisibility(View.VISIBLE);

        if (_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES)) {
            setClickable(true);
        } else {
            setClickable(false);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _notes_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.CLOSING_NOTES);
            if (_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES))
                ClosingNotesDialog.show(App.get(), _workOrder.getId(), _workOrder.getClosingNotes());
        }
    };

}
