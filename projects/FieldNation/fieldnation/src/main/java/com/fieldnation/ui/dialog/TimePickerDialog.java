package com.fieldnation.ui.dialog;

import android.content.Context;

/**
 * Created by Michael on 5/23/2016.
 */
public class TimePickerDialog extends android.app.TimePickerDialog {
    private Object _tag = null;

    public TimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context, callBack, hourOfDay, minute, is24HourView);
    }

    public TimePickerDialog(Context context, int theme, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context, theme, callBack, hourOfDay, minute, is24HourView);
    }

    public void setTag(Object tag) {
        _tag = tag;
    }

    public Object getTag() {
        return _tag;
    }
}
