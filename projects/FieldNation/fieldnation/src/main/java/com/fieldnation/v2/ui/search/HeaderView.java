package com.fieldnation.v2.ui.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 12/22/16.
 */

public class HeaderView extends RelativeLayout {
    private static final String TAG = "HeaderView";

    // Ui
    private IconFontTextView _iconFontView;
    private TextView _filtersTextView;

    // Data
    private FilterParams _filterParams;

    public HeaderView(Context context) {
        super(context);
        init();
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_work_order_list_header, this);

        if (isInEditMode())
            return;

        _iconFontView = (IconFontTextView) findViewById(R.id.icon_view);
        _filtersTextView = (TextView) findViewById(R.id.filter_textview);

        populateUi();
    }

    public void setFilterParams(FilterParams filterParams) {
        _filterParams = filterParams;
        populateUi();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        _iconFontView.setOnClickListener(l);
    }

    private void populateUi() {
        if (_filtersTextView == null)
            return;

        if (_filterParams == null)
            return;
        // profile, current, other, remote

        try {
            switch (_filterParams.uiLocationSpinner) {
                case 0:
                    _filtersTextView.setText("Filters: Profile, " + _filterParams.radius.intValue() + "mi");
                    break;
                case 1:
                    _filtersTextView.setText("Filters: Current, " + _filterParams.radius.intValue() + "mi");
                    break;
                case 2:
                    _filtersTextView.setText("Filters: Custom, " + _filterParams.radius.intValue() + "mi");
                    break;
                case 3:
                    _filtersTextView.setText("Filters: Remote");
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _filtersTextView.setText("Filters: None");
        }
    }

}
