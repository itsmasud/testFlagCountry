package com.fieldnation.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.ui.AuthSimpleActivity;

/**
 * Created by Michael on 7/27/2016.
 */
public class SearchResultsActivity extends AuthSimpleActivity {
    private static final String TAG = "SearchResultsActivity";

    // Ui
    private SearchResultScreen _searchResultScreen;

    // State
    private static final String INTENT_SEARCH_PARAMS = "INTENT_SEARCH_PARAMS";

    @Override
    public int getLayoutResource() {
        return R.layout.activity_search_result;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        setTitle("Search Results");
        _searchResultScreen = (SearchResultScreen) findViewById(R.id.searchResultScreen);
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent() != null && getIntent().hasExtra(INTENT_SEARCH_PARAMS)) {
            SavedSearchParams searchParams = getIntent().getParcelableExtra(INTENT_SEARCH_PARAMS);
            _searchResultScreen.startSearch(searchParams);

            setTitle(misc.capitalize(searchParams.woList.getParam()) + " Search");
        }
    }

    @Override
    public void onProfile(Profile profile) {
    }

    public static void runSearch(Context context, SavedSearchParams searchParams) {
        Intent intent = new Intent(context, SearchResultsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(INTENT_SEARCH_PARAMS, searchParams);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}
