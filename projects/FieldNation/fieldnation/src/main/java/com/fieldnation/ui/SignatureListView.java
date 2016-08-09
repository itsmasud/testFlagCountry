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

import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.detail.WorkorderRenderer;

import java.util.Random;

/**
 * Created by michael.carver on 12/5/2014.
 */
public class SignatureListView extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "SummaryListView";

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
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

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

        if (_workorder == null) {
            return;
        }

        if (!_workorder.canAcceptSignature()) {
            return;
        }

        setVisibility(View.VISIBLE);

        final Signature[] list = _workorder.getSignatureList();

        if (list == null || list.length == 0) {
            _noDataTextView.setVisibility(View.VISIBLE);
            _listView.setVisibility(GONE);
            return;
        }

        _noDataTextView.setVisibility(View.GONE);
        _listView.setVisibility(VISIBLE);

        if (_listView.getChildCount() > list.length) {
            _listView.removeViews(list.length - 1, _listView.getChildCount() - list.length);
        }

        ForLoopRunnable r = new ForLoopRunnable(list.length, new Handler()) {
            private final Signature[] _list = list;

            @Override
            public void next(int i) throws Exception {
                SignatureCardView v = null;
                if (i < _listView.getChildCount()) {
                    v = (SignatureCardView) _listView.getChildAt(i);
                } else {
                    v = new SignatureCardView(getContext());
                    _listView.addView(v);
                }
                Signature sig = _list[i];
                v.setSignature(sig);
                v.setOnClickListener(_signature_onClick);
                v.setOnLongClickListener(_signature_onLongClick);
            }
        };
        postDelayed(r, new Random().nextInt(1000));
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
