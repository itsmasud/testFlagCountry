package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
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
        if (isInEditMode())
            return;

        setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(-1);
        setWrapSelectorWheel(true);
//        setDividerColor(Color.RED);

    }

    public void setDisplayedValuesAs(int minValue, int maxValue, int range) {
        // TODO need to deal for -ve values
        if (minValue < 0 || maxValue < 0 || range < 0)
            return;

        int numberOfValue = ((maxValue - minValue) / range) + 1;
        setMinValue(0);
        setMaxValue(numberOfValue - 1);

        String[] displayedValues = new String[numberOfValue];
        int value = 0;
        for (int i = 0; i < numberOfValue; i++) {
            value += (i == 0 ? minValue : minValue + range);
            displayedValues[i] = Integer.toString(value);
        }
        setDisplayedValues(displayedValues);
    }


    private void setDividerColor(int color) {
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

    public void setTextSize(int textSize) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = NumberPicker.class.getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);

                    Paint wheelPaint = ((Paint) selectorWheelPaintField.get(this));
//                    wheelPaint.setColor(mTextColor);
                    wheelPaint.setTextSize(spToPixels(getContext(), textSize));

                    EditText editText = ((EditText) child);
//                    editText.setTextColor(mTextColor);
                    editText.setTextSize(textSize);

                    invalidate();
                    break;
                } catch (Exception e) {
                    Log.v(TAG, e);
                }
            }
        }
    }

    private float pixelsToSp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }

    private float spToPixels(Context context, float sp) {
        return sp * context.getResources().getDisplayMetrics().scaledDensity;
    }


}
