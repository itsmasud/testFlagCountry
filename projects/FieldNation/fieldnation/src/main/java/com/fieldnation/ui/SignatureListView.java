package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.detail.WorkorderRenderer;

/**
 * Created by michael.carver on 12/5/2014.
 */
public class SignatureListView extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "ui.SummaryListView";

    // Ui
    private LinearLayout _summaryListView;
    private Button _addButton;

    // Data
    private Workorder _workorder;
    private Listener _listener;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public SignatureListView(Context context) {
        super(context);
        init();
    }

    public SignatureListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_signature_list, this);

        if (isInEditMode())
            return;

        _summaryListView = (LinearLayout) findViewById(R.id.summary_listview);
        _addButton = (Button) findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);

        populateUI();
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;

        populateUI();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUI() {
        setVisibility(View.GONE);

        if (_addButton == null)
            return;

        if (_workorder == null)
            return;

        setVisibility(View.VISIBLE);

        // TODO get the data, and do stuff.

    }


    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.addSignature();
        }
    };

    public interface Listener {
        public void addSignature();
    }

}
