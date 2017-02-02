package com.fieldnation.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import com.fieldnation.fnlog.Log;

import java.lang.reflect.Field;

/**
 * Created by Shoaib on 1/1/2017.
 */
public class FnNumberPicker extends NumberPicker {
    private static String TAG = "FnNumberPicker";

    public FnNumberPicker(Context context) {
        super(context);
        init();
    }

    public FnNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FnNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
        }

//        setDividerColor(Color.RED);
    }

    public void setDividerColor(int color) {
        Field[] fields = NumberPicker.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("mSelectionDivider")) {
                field.setAccessible(true);
                try {
                    if (color == -1) {
                        field.set(this, null);
                    } else {
                        ColorDrawable colorDrawable = new ColorDrawable(color);
                        field.set(this, colorDrawable);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
                break;
            }
        }
    }


}
