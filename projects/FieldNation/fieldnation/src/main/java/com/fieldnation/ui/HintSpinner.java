package com.fieldnation.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;

import com.fieldnation.fnlog.Log;

/**
 * Created by Michael Carver on 1/25/2016.
 * <p/>
 * This class was created to add some sanity to the setSelection method call
 */
public class HintSpinner extends AppCompatSpinner {
    private static final String TAG = "HintSpinner";

    private String _hint = null;
    private OnItemSelectedListener _listener = null;

    public HintSpinner(Context context) {
        super(context);
        super.setOnItemSelectedListener(_itemSelectedListener);
    }

    public HintSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        handleAttribues(attributeSet);
        super.setOnItemSelectedListener(_itemSelectedListener);
    }

    public HintSpinner(Context context, AttributeSet attributeSet, int arg2) {
        super(context, attributeSet, arg2);
        handleAttribues(attributeSet);
        super.setOnItemSelectedListener(_itemSelectedListener);
    }

    private void handleAttribues(AttributeSet attributeSet) {
        if (isInEditMode())
            return;

        TypedArray ta = getContext().obtainStyledAttributes(attributeSet, new int[]{android.R.attr.hint});
        try {
            setHint(ta.getString(0));
            Log.v(TAG, "handleAttribues: " + ta.getString(0));
        } finally {
            ta.recycle();
        }
    }

    public void setHint(int resId) {
        setHint(getContext().getString(resId));
    }

    public void setHint(String hint) {
        _hint = hint;

        if (getAdapter() != null && getAdapter() instanceof HintArrayAdapter) {
            ((HintArrayAdapter) getAdapter()).setHint(hint);
        }
    }

    public String getHint() {
        return _hint;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof HintArrayAdapter) {
            ((HintArrayAdapter) adapter).setHint(_hint);
        }

        clearSelection();
    }



    public void clearSelection() {
        super.setSelection(getCount() - 1);
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        _listener = listener;
    }

    private final OnItemSelectedListener _itemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (_listener != null) {
                if (position < getCount() - 1)
                    _listener.onItemSelected(parent, view, position, id);
                else
                    _listener.onNothingSelected(parent);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            if (_listener != null)
                _listener.onNothingSelected(parent);
        }
    };
}
