package com.fieldnation.v2.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.v2.data.model.SavedList;

/**
 * Created by mc on 12/22/16.
 */

public class HeaderView extends RelativeLayout {
    private static final String TAG = "HeaderView";

    private static final String DIALOG_FILTER_DRAWER = TAG + ".filterDrawerDialog";

    // Ui
    private IconFontTextView _iconFontView;
    private TextView _filtersTextView;

    // Data
    private SavedList _savedList;

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
        _iconFontView.setOnClickListener(_iconFontView_onClick);
        _filtersTextView = (TextView) findViewById(R.id.filter_textview);

        populateUi();
    }

    public void setSavedList(SavedList savedList) {
        _savedList = savedList;
        populateUi();
    }

    private void populateUi() {
        if (_savedList == null)
            return;

        if (_filtersTextView == null)
            return;

        // profile, current, other, remote
/*
        try {
TODO            switch (_savedSearchParams.uiLocationSpinner) {
                case 0:
                    _filtersTextView.setText("Filters: Profile, " + _savedSearchParams.radius.intValue() + "mi");
                    break;
                case 1:
                    _filtersTextView.setText("Filters: Current, " + _savedSearchParams.radius.intValue() + "mi");
                    break;
                case 2:
                    _filtersTextView.setText("Filters: Custom, " + _savedSearchParams.radius.intValue() + "mi");
                    break;
                case 3:
                    _filtersTextView.setText("Filters: Remote");
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _filtersTextView.setText("Filters: None");
        }
*/
    }

    private final OnClickListener _iconFontView_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO FilterDrawerDialog.show(App.get(), DIALOG_FILTER_DRAWER, _savedSearchParams);
        }
    };
}
