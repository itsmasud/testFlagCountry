package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by Michael Carver on 1/25/2016.
 * <p/>
 * This class was created to add some sanity to the setSelection method call
 */
public class FnSpinner extends MaterialBetterSpinner {
    public FnSpinner(Context context) {
        super(context);
    }

    public FnSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public FnSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void setSelection(int index) {
        super.setSelection(index);

        setListSelection(index);

        if (getAdapter() != null
                && getAdapter().getCount() > index
                && getAdapter().getItem(index) != null) {
            setText(getAdapter().getItem(index).toString());
        }
    }
}
