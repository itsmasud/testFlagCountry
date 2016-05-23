package com.fieldnation.ui.dialog;

import android.content.Context;

/**
 * Created by Michael on 5/23/2016.
 */
public class DatePickerDialog extends android.app.DatePickerDialog {
    private Object _tag = null;

    public DatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    public DatePickerDialog(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, listener, year, monthOfYear, dayOfMonth);
    }

    public void setTag(Object tag) {
        _tag = tag;
    }

    public Object getTag() {
        return _tag;
    }
}
