package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 1/28/2015.
 */
public class ToolbarTabView extends RelativeLayout {
    private static final String TAG = "ui.TabView";

    // Ui
    private TextView _back;
    private SlideTextView _front;

    public ToolbarTabView(Context context) {
        super(context);
        init();
    }

    public ToolbarTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToolbarTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_toolbar_tab, this, true);

        _back = (TextView) findViewById(R.id.back);
        _front = (SlideTextView) findViewById(R.id.front);
    }


    public void setHighlight() {
        _front.setHighlight();
    }

    public void animateHighlightRight() {
        _front.animateHighlightRight();
    }

    public void animateHighlightLeft() {
        _front.animateHighlightLeft();
    }

    public void animateUnhighlightLeft() {
        _front.animateUnhighlightLeft();
    }


    public void animateUnhighlightRight() {
        _front.animateUnhighlightRight();
    }

    public void setText(CharSequence text) {
        _front.setText(text);
        _back.setText(text);
    }

    public void setText(int resid) {
        _front.setText(resid);
        _back.setText(resid);
    }

    /**
     * Draw the view into a bitmap.
     */
    private static Bitmap getViewBitmap(View v) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}
