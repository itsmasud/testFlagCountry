package com.fieldnation.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.Signatures;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 12/5/2014.
 */
public class SignatureListView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "SignatureListView";

    // Ui
    private LinearLayout _listView;
    private Button _addButton;
    private TextView _noDataTextView;

    // Data
    private WorkOrder _workOrder;
    private Listener _listener;
    private ForLoopRunnable _forLoop;

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

    private void init() {
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
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;

        populateUI();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUI() {
        setVisibility(View.GONE);
        if (_addButton == null) {
            return;
        }

        if (_workOrder == null) {
            return;
        }

        if (_workOrder.getSignatures() == null)
            return;

        if (_workOrder.getStatus().getId() == 2 || _workOrder.getStatus().getId() == 9)
            return;

        if (!_workOrder.getSignatures().getActionsSet().contains(Signatures.ActionsEnum.ADD)) {
            _addButton.setVisibility(GONE);
        } else {
            _addButton.setVisibility(VISIBLE);
        }

        setVisibility(View.VISIBLE);

        final Signature[] list = _workOrder.getSignatures().getResults();

        if (list == null || list.length == 0) {
            _noDataTextView.setVisibility(View.VISIBLE);
            _listView.setVisibility(GONE);
            return;
        }

        _noDataTextView.setVisibility(View.GONE);
        _listView.setVisibility(VISIBLE);

        if (_forLoop != null) {
            _forLoop.cancel();
            _forLoop = null;
        }

        _forLoop = new ForLoopRunnable(list.length, new Handler()) {
            private final Signature[] _list = list;
            private List<View> views = new LinkedList<>();

            @Override
            public void next(int i) throws Exception {
                SignatureCardView v = new SignatureCardView(getContext());
                views.add(v);
                Signature sig = _list[i];
                v.setSignature(sig);
                v.setOnClickListener(_signature_onClick);
                v.setOnLongClickListener(_signature_onLongClick);
            }

            @Override
            public void finish(int count) throws Exception {
                _listView.removeAllViews();
                for (View v : views) {
                    _listView.addView(v);
                }
            }
        };
        postDelayed(_forLoop, 100);
    }


    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.addSignature();
        }
    };

    private final View.OnClickListener _signature_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SignatureCardView view = (SignatureCardView) v;
            if (_listener != null)
                _listener.signatureOnClick(view, view.getSignature());
        }
    };

    private final View.OnLongClickListener _signature_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            SignatureCardView view = (SignatureCardView) v;
            if (_listener != null)
                return _listener.signatureOnLongClick(view, view.getSignature());
            return false;
        }
    };

    public interface Listener {
        void addSignature();

        void signatureOnClick(SignatureCardView view, Signature signature);

        boolean signatureOnLongClick(SignatureCardView view, Signature signature);
    }

}
