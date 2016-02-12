package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.util.Calendar;

/**
 * Created by Michael Carver on 1/25/2016.
 * <p/>
 * This class was created to add some sanity to the setSelection method call
 */
public class FnSpinner extends MaterialAutoCompleteTextView implements AdapterView.OnItemClickListener {
    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;
    private boolean isPopup;

    public FnSpinner(Context context) {
        super(context);
        setOnItemClickListener(this);
    }

    public FnSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        setOnItemClickListener(this);
    }

    public FnSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        setOnItemClickListener(this);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering("", 0);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
            setKeyListener(null);
            dismissDropDown();
        } else {
            isPopup = false;
        }
    }

    @Override
    public void setListSelection(int position) {
        super.setListSelection(position);
        if (getAdapter() != null && getAdapter().getCount() > position && getAdapter().getItem(position) != null) {
            setText(getAdapter().getItem(position).toString());
        }
    }

    public void setSelectedItem(int index) {
        if (getAdapter() != null && getAdapter().getCount() > index && getAdapter().getItem(index) != null) {
            replaceText(getAdapter().getItem(index).toString());
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                    if (isPopup) {
                        dismissDropDown();
                        isPopup = false;
                    } else {
                        requestFocus();
                        showDropDown();
                        isPopup = true;
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        isPopup = false;
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        Drawable dropdownIcon = ContextCompat.getDrawable(getContext(), com.weiwangcn.betterspinner.library.material.R.drawable.ic_expand_more_black_18dp);
        if (dropdownIcon != null) {
            right = dropdownIcon;
            right.mutate().setAlpha(66);
        }
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }
}
