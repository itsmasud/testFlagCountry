package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.WorkOrder;

public class ClosingNotesView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ClosingNotesView";

    // UI
    private TextView _notesTextView;

    // Data
    private WorkOrder _workOrder;
    private Listener _listener;

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

    public void setListener(Listener listener) {
        _listener = listener;
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
            if (_listener != null && _workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES)) {
                _listener.onChangeClosingNotes(_workOrder.getClosingNotes());
            }
        }
    };

    public interface Listener {
        void onChangeClosingNotes(String closingNotes);
    }
}
