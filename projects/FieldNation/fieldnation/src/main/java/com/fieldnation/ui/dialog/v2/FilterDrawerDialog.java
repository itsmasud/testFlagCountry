package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.SearchTracker;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.RightDrawerDialog;
import com.fieldnation.ui.search.SearchEditScreen;

/**
 * Created by mc on 1/3/17.
 */

public class FilterDrawerDialog extends RightDrawerDialog {
    private static final String TAG = "FilterDrawerDialog";

    // Ui
    private SearchEditScreen _searchEditScreen;

    // Data
    private SavedSearchParams _savedSearchParams;

    public FilterDrawerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_filter_drawer, container, false);

        _searchEditScreen = (SearchEditScreen) v.findViewById(R.id.searchEditScreen);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _searchEditScreen.setListener(_searchEditScreen_onApply);
        SearchTracker.onShow(App.get());
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        _savedSearchParams = payload.getParcelable("SAVED_SEARCH_PARAMS");
        _searchEditScreen.setSavedSearchParams(_savedSearchParams);
    }


    private SearchEditScreen.Listener _searchEditScreen_onApply = new SearchEditScreen.Listener() {
        @Override
        public void onApply() {
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, SavedSearchParams savedSearchParams) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("SAVED_SEARCH_PARAMS", savedSearchParams);
        Controller.show(context, null, FilterDrawerDialog.class, bundle);
    }
}
