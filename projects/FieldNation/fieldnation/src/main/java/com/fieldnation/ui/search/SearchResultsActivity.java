package com.fieldnation.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.fieldnation.R;
import com.fieldnation.service.data.v2.workorder.SearchParams;
import com.fieldnation.ui.ActionBarDrawerView;
import com.fieldnation.ui.AuthActionBarActivity;

/**
 * Created by Michael on 7/27/2016.
 */
public class SearchResultsActivity extends AuthActionBarActivity {
    private static final String TAG = "SearchResultsActivity";

    // Ui
    private SearchResultScreen _searchResultScreen;

    // State
    private static final String INTENT_SEARCH_PARAMS = "INTENT_SEARCH_PARAMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarDrawerView actionBarView = (ActionBarDrawerView) findViewById(R.id.actionbardrawerview);
        Toolbar toolbar = actionBarView.getToolbar();
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(_toolbarNavication_listener);
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent() != null && getIntent().hasExtra(INTENT_SEARCH_PARAMS)) {
            SearchParams searchParams = getIntent().getParcelableExtra(INTENT_SEARCH_PARAMS);
            _searchResultScreen.startSearch(searchParams);
        }
    }

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public static void runSearch(Context context, SearchParams searchParams) {
        Intent intent = new Intent(context, SearchResultsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(INTENT_SEARCH_PARAMS, searchParams);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_right, 0);
        }
    }
}
