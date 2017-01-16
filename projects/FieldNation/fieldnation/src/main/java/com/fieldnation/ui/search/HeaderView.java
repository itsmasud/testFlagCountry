package com.fieldnation.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.dialog.v2.FilterDrawerDialog;

/**
 * Created by mc on 12/22/16.
 */

public class HeaderView extends RelativeLayout {
    private static final String TAG = "HeaderView";

    // Ui
    private IconFontTextView _iconFontView;
    private TextView _filtersTextView;

    // Data
    private SavedSearchParams _savedSearchParams;

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

    public void setSavedSearchParams(SavedSearchParams savedSearchParams) {
        _savedSearchParams = savedSearchParams;
        populateUi();
    }

    private void populateUi() {
        if (_savedSearchParams == null)
            return;

        if (_filtersTextView == null)
            return;

        // profile, current, other, remote
        try {
            switch (_savedSearchParams.uiLocationSpinner) {
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
    }

    private final View.OnClickListener _iconFontView_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            FilterDrawerDialog.show(App.get(), _savedSearchParams);
        }
    };
}
