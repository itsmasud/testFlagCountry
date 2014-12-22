package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.detail.WorkorderRenderer;

/**
 * Created by michael.carver on 12/5/2014.
 */
public class SignatureListView extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "ui.SummaryListView";

    // Ui
    private LinearLayout _listView;
    private Button _addButton;
    private TextView _noDataTextView;

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

        _listView = (LinearLayout) findViewById(R.id.listview);

        _noDataTextView = (TextView) findViewById(R.id.nodata_textview);

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

        if (!_workorder.canAcceptSignature())
            return;

        setVisibility(View.VISIBLE);

        Signature[] list = _workorder.getSignatureList();

        if (list == null || list.length == 0) {
            _noDataTextView.setVisibility(View.VISIBLE);
            return;
        }

        _noDataTextView.setVisibility(View.GONE);

        _listView.removeAllViews();
        for (int i = 0; i < list.length; i++) {
            Signature sig = list[i];
            SignatureTileView v = new SignatureTileView(getContext());
            v.setSignature(sig);
            v.setOnClickListener(_signature_onClick);
            _listView.addView(v);
        }

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

    private OnClickListener _signature_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SignatureTileView view = (SignatureTileView) v;
            if (_listener != null)
                _listener.signatureOnClick(view, view.getSignature());

        }
    };

    public interface Listener {
        public void addSignature();

        public void signatureOnClick(SignatureTileView view, Signature signature);

    }

}
