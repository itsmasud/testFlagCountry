package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
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

    public static class Controller extends com.fieldnation.fndialog.Controller {
        public Controller(Context context, String uid) {
            super(context, FilterDrawerDialog.class, uid);
        }

        public static void show(Context context, SavedSearchParams savedSearchParams) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("SAVED_SEARCH_PARAMS", savedSearchParams);
            show(context, null, FilterDrawerDialog.class, bundle);
        }
    }

}
