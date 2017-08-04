package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;

/**
 * Created by mc on 7/11/17.
 */

public class ListItemTwoVertView extends RelativeLayout {
    private static final String TAG = "ListItemTwoVertView";

    // Ui
    private TextView _keyTextView;
    private TextView _valueTextView;
    private TextView _actionTextView;

    // Data
    private String _key;
    private String _value;
    private String _action;
    private boolean _actionVisible = true;

    // Listener
    private OnActionClickListener _actionOnclickListener;

    public ListItemTwoVertView(Context context) {
        super(context);
        init();
    }

    public ListItemTwoVertView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemTwoVertView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_two_line_action, this);

        if (isInEditMode())
            return;

        _keyTextView = findViewById(R.id.key);
        _valueTextView = findViewById(R.id.value);
        _actionTextView = findViewById(R.id.action);
        _actionTextView.setOnClickListener(_action_onClick);

        populateUi();
    }

    public void setActionVisible(boolean visible) {
        _actionVisible = visible;
        populateUi();
    }

    public void setActionString(String action) {
        _action = action;

        populateUi();
    }

    public void setOnActionClickedListener(OnActionClickListener onActionClickedListener) {
        _actionOnclickListener = onActionClickedListener;
    }

    public void set(String key, String value) {
        _key = key;
        _value = value;
        populateUi();
    }

    private void populateUi() {
        if (_keyTextView == null)
            return;

        if (misc.isEmptyOrNull(_key)) {
            _keyTextView.setText("");
        } else {
            _keyTextView.setText(_key);
        }

        if (misc.isEmptyOrNull(_value)) {
            _valueTextView.setText("");
        } else {
            _valueTextView.setText(_value);
        }

        if (_actionVisible) {
            _actionTextView.setVisibility(VISIBLE);
            if (misc.isEmptyOrNull(_action)) {
                _actionTextView.setText(R.string.icon_x);
            } else {
                _actionTextView.setText(_action);
            }
        } else {
            _actionTextView.setVisibility(GONE);
        }
    }

    private final View.OnClickListener _action_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (_actionOnclickListener != null)
                _actionOnclickListener.onClick(ListItemTwoVertView.this, view);
        }
    };

    public interface OnActionClickListener {
        void onClick(View twoLineActionTile, View actionView);
    }
}
