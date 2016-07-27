package com.fieldnation.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.service.data.v2.workorder.SearchParams;
import com.fieldnation.service.data.v2.workorder.WorkOrderClient;
import com.fieldnation.ui.ActionBarDrawerView;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.worecycler.BaseHolder;
import com.fieldnation.ui.worecycler.TimeHeaderAdapter;
import com.fieldnation.ui.worecycler.WorkOrderHolder;
import com.fieldnation.ui.workorder.WorkOrderCard;
import com.fieldnation.utils.ISO8601;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Michael on 7/27/2016.
 */
public class SearchResultsActivity extends AuthActionBarActivity {
    private static final String TAG = "EditSearchActivity";

    // Ui
    private OverScrollRecyclerView _recycler;

    // State
    private static final String INTENT_SEARCH_PARAMS = "INTENT_SEARCH_PARAMS";

    // Service
    private WorkOrderClient _workOrderClient;

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

        _recycler = (OverScrollRecyclerView) findViewById(R.id.workOrderList_recyclerView);
        _recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        _recycler.setAdapter(_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _workOrderClient = new WorkOrderClient(_workOrderClient_listener);
        _workOrderClient.connect(App.get());

        if (getIntent() != null && getIntent().hasExtra(INTENT_SEARCH_PARAMS)) {
            SearchParams searchParams = getIntent().getParcelableExtra(INTENT_SEARCH_PARAMS);

            WorkOrderClient.search(App.get(), searchParams);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (_workOrderClient != null && _workOrderClient.isConnected())
            _workOrderClient.disconnect(App.get());
    }

    private final WorkOrderClient.Listener _workOrderClient_listener = new WorkOrderClient.Listener() {
        @Override
        public void onConnected() {
            _workOrderClient.subSearch();
        }

        @Override
        public void onSearch(SearchParams searchParams, List<WorkOrder> workorder) {
            Log.v(TAG, "onSearch");
            _adapter.addObjects(workorder);
        }
    };

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private final TimeHeaderAdapter<WorkOrder> _adapter = new TimeHeaderAdapter<WorkOrder>(WorkOrder.class) {
        @Override
        public void requestPage(int page, boolean allowCache) {

        }

        @Override
        public Comparator<WorkOrder> getTimeComparator() {
            return WorkOrder.getTimeComparator();
        }

        @Override
        public Calendar getObjectTime(WorkOrder object) {
            try {
                return ISO8601.toCalendar(object.getRequirements().getSchedule().getStart());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        public BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType) {
            return new WorkOrderHolder(new WorkOrderCard(parent.getContext()));
        }

        @Override
        public void onBindObjectViewHolder(BaseHolder holder, WorkOrder object) {
            WorkOrderHolder h = (WorkOrderHolder) holder;
            WorkOrderCard v = h.getView();
            v.setData(object);
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
