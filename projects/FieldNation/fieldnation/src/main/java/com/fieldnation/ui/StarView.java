package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 5/22/2015.
 */
public class StarView extends FrameLayout {

    private TextView _leftStars;
    private TextView _rightStars;

    private int _goldStars = 3;
    private int _totalStars = 5;
    private String _star;

    public StarView(Context context) {
        super(context);
        init();
    }

    public StarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_stars, this);

        if (isInEditMode())
            return;

        _star = getContext().getResources().getString(R.string.icfont_star);
        _leftStars = (TextView) findViewById(R.id.left_stars);
        _rightStars = (TextView) findViewById(R.id.right_stars);

        setStars(_goldStars, _totalStars);
    }

    public void setStars(int goldStars) {
        setStars(goldStars, _totalStars);
    }

    public void setStars(int goldStars, int totalStars) {
        _goldStars = goldStars;
        _totalStars = totalStars;

        if (goldStars > 0) {
            _leftStars.setText(misc.repeat(_star, goldStars));
            _leftStars.setVisibility(VISIBLE);
        } else {
            _leftStars.setVisibility(GONE);
        }

        _rightStars.setText(misc.repeat(_star, totalStars - goldStars));
    }
}
