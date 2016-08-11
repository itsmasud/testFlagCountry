package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 5/22/2015.
 */
public class StarView extends FrameLayout {
    private static final String TAG = "StarView";
    // Ui
    private List<TextView> _starViews = new LinkedList<>();
    private LinearLayout _starContainer;

    // Data
    private boolean _isChangeable = false;
    private int _goldStars = 3;
    private int _totalStars = 5;
    private float _fontSize = 16F;
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
        _starContainer = (LinearLayout) findViewById(R.id.star_container);

        setStars(_goldStars, _totalStars);
    }

    private IconFontTextView makeStarView(int index) {
        IconFontTextView tv = new IconFontTextView(getContext());
        tv.setTextColor(getResources().getColor(R.color.fn_yellow));
        tv.setText(_star);
        tv.setTextSize(_fontSize);
        tv.setTag(index);
        tv.setOnClickListener(_star_onClick);
        return tv;
    }

    public void setStars(int goldStars) {
        setStars(goldStars, _totalStars);
    }

    private Listener _listener;

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setStars(int goldStars, int totalStars) {
        // remove extra views
        while (_starViews.size() > totalStars)
            _starViews.remove(_starViews.size() - 1);

        // add views
        while (_starViews.size() < totalStars) {
            _starViews.add(makeStarView(_starViews.size()));
        }

        // populate the UI
        if (_starViews.size() != _starContainer.getChildCount()) {
            _starContainer.removeAllViews();
            for (int i = 0; i < _starViews.size(); i++) {
                _starContainer.addView(_starViews.get(i));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _starViews.get(i).getLayoutParams();
                params.leftMargin = 6;
                _starViews.get(i).setLayoutParams(params);
            }
        }

        _goldStars = goldStars;
        _totalStars = totalStars;

        // clamp the numbers
        if (_goldStars < 0) {
            _goldStars = 0;
        } else if (_goldStars > totalStars) {
            _goldStars = totalStars;
        }

        // apply the colors
        for (int i = 0; i < _starViews.size(); i++) {
            if (i < _goldStars) {
                _starViews.get(i).setTextColor(getResources().getColor(R.color.fn_yellow));
            } else {
                _starViews.get(i).setTextColor(getResources().getColor(R.color.fn_light_text));
            }
        }
    }

    public void setChangeEnabled(boolean enabled) {
        _isChangeable = enabled;
    }

    public void setStarFontSize(float fontSize) {
        _fontSize = fontSize;
        for (int i = 0; i < _starViews.size(); i++) {
            _starViews.get(i).setTextSize(_fontSize);
        }
    }

    public int getNumberOfGoldStar() {
        return _goldStars;
    }

    private final TextView.OnClickListener _star_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!_isChangeable)
                return;

            int starIndex = (int) v.getTag();
            setStars(starIndex + 1);

            if (_listener != null)
                _listener.onClick(_goldStars);
        }
    };

    public interface Listener {
        void onClick(int goldStar);
    }
}