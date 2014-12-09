package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

public class ClosingNotesView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.ClosingNotesView";

    private static final int WEB_SAVE_NOTES = 1;

    // UI
    private TextView _notesTextView;
    private LinearLayout _editLayout;
    private ImageView _editImageView;
    private ProgressBar _progressBar;

    // Data
    private Workorder _workorder;
    private Integer[] woStatus = {5, 6, 7}; //work order status approved, paid, canceled
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

        _editLayout = (LinearLayout) findViewById(R.id.edit_layout);
        _editLayout.setOnClickListener(_notes_onClick);
        _editImageView = (ImageView) findViewById(R.id.edit_imageview);

        _notesTextView = (TextView) findViewById(R.id.notes_textview);
        _notesTextView.setOnClickListener(_notes_onClick);

        _progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        if (!misc.isEmptyOrNull(_workorder.getClosingNotes())) {
            _notesTextView.setText(_workorder.getClosingNotes());
        } else if (!_workorder.canChangeClosingNotes()) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);

        if (_workorder.canChangeClosingNotes()) {
            _editLayout.setVisibility(View.VISIBLE);
            _notesTextView.setClickable(true);
        } else {
            _editLayout.setVisibility(View.GONE);
            _notesTextView.setClickable(false);
        }
    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private View.OnClickListener _notes_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null && _workorder.canChangeClosingNotes()) {
                _listener.onChangeClosingNotes(_workorder.getClosingNotes());
            }
        }
    };


    public interface Listener {
        public void onChangeClosingNotes(String closingNotes);
    }
}
