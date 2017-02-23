package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class ClosingNotesView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ClosingNotesView";

    // UI
    private TextView _noNotesTextView;
    private TextView _notesTextView;
    private Button _addButton;

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

        _noNotesTextView = (TextView) findViewById(R.id.noNotes_textview);

        _notesTextView = (TextView) findViewById(R.id.notes_textview);
        _notesTextView.setOnClickListener(_notes_onClick);

        _addButton = (Button) findViewById(R.id.add_button);
        _addButton.setOnClickListener(_notes_onClick);

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
        if (_workOrder == null) {
            setVisibility(View.GONE);
            return;
        }

        if (!misc.isEmptyOrNull(_workOrder.getClosingNotes())) {
            _notesTextView.setText(_workOrder.getClosingNotes());
            _notesTextView.setVisibility(VISIBLE);
            _noNotesTextView.setVisibility(GONE);
        } else {
            _notesTextView.setVisibility(GONE);
            _noNotesTextView.setVisibility(VISIBLE);
            if (!_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES)) {
                setVisibility(View.GONE);
                return;
            }
        }
        setVisibility(View.VISIBLE);

        if (_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES)) {
            _addButton.setVisibility(View.VISIBLE);
            _notesTextView.setClickable(true);
        } else {
            _addButton.setVisibility(View.GONE);
            _notesTextView.setClickable(false);
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
