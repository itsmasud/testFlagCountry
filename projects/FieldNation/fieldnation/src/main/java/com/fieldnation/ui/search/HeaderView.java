package com.fieldnation.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.service.data.savedsearch.SavedSearchClient;

/**
 * Created by mc on 12/22/16.
 */

public class HeaderView extends RelativeLayout {

    private SearchEditScreen _searchEditScreen;
    private Button _applyButton;

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

        _searchEditScreen = (SearchEditScreen) findViewById(R.id.searchEditScreen);
        _applyButton = (Button) findViewById(R.id.apply_button);
        _applyButton.setOnClickListener(_apply_onClick);

        populateUi();
    }

    public void setSavedSearchParams(SavedSearchParams savedSearchParams) {
        _savedSearchParams = savedSearchParams;
        populateUi();
    }

    private void populateUi() {
        if (_searchEditScreen == null)
            return;

        if (_savedSearchParams == null)
            return;

        _searchEditScreen.setSavedSearchParams(_savedSearchParams);
    }


    private final View.OnClickListener _apply_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SavedSearchClient.save(_searchEditScreen.getSavedSearchParams());
        }
    };
}
