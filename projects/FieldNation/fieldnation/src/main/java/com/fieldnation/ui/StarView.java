package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

        _star = getContext().getResources().getString(R.string.icon_star);
        _leftStars = (TextView) findViewById(R.id.left_stars);
        _leftStars.setOnClickListener(_leftStar_onClick);
        _leftStars.setClickable(false);
        _rightStars = (TextView) findViewById(R.id.right_stars);
        _rightStars.setOnClickListener(_rightStar_onClick);
        _rightStars.setClickable(false);

        setStars(_goldStars, _totalStars);
    }

    public void setStars(int goldStars) {
        setStars(goldStars, _totalStars);
    }

    private Listener _listener;

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setStars(int goldStars, int totalStars) {
        _goldStars = goldStars;
        _totalStars = totalStars;

        if (_goldStars < 0) {
            _goldStars = 0;
        } else if (_goldStars > totalStars) {
            _goldStars = totalStars;
        }

        if (goldStars > 0) {
            _leftStars.setText(misc.repeat(_star, goldStars));
            _leftStars.setVisibility(VISIBLE);
        } else {
            _leftStars.setVisibility(GONE);
        }

        _rightStars.setText(misc.repeat(_star, totalStars - goldStars));
    }

    public void setChangeEnabled(boolean enabled) {
        _leftStars.setClickable(enabled);
        _rightStars.setClickable(enabled);
    }

    public void setStarFontSize(int fontSize) {
        _leftStars.setTextSize(fontSize);
        _rightStars.setTextSize(fontSize);
    }

    public int getNumberOfGoldStar() {
        return _goldStars;
    }

    private final TextView.OnClickListener _leftStar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setStars(_goldStars - 1);
            if (_listener != null)
            _listener.onClick(_goldStars);
        }
    };

    private final TextView.OnClickListener _rightStar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setStars(_goldStars + 1);
            if (_listener != null)
                _listener.onClick(_goldStars);
        }
    };

    public interface Listener {
        void onClick(int goldStar);
    }

}
