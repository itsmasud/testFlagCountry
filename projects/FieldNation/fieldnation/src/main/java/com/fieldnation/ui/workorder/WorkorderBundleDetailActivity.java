package com.fieldnation.ui.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderDataClient;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.NumberFormat;

public class WorkorderBundleDetailActivity extends AuthActionBarActivity {
    private static final String TAG = "WorkorderBundleDetailActivity";

    public static final String INTENT_FIELD_WORKORDER_ID = "com.fieldnation.ui.workorder.WorkorderBundleDetailActivity:workorder_id";
    public static final String INTENT_FIELD_BUNDLE_ID = "com.fieldnation.ui.workorder.WorkorderBundleDetailActivity:bundle_id";

    private static final int WEB_GET_BUNDLE = 1;

    // UI
    private ListView _listview;
    private TextView _distanceTextView;
    private TextView _dateTextView;
    private Button _requestButton;
    private RelativeLayout _loadingLayout;

    // Data
    private long _workorderId = 0;
    private long _bundleId = 0;
    private WorkorderDataClient _workorderClient;
    private BundleAdapter _adapter;
    private com.fieldnation.data.workorder.Bundle _woBundle;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_bundle_detail;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        if (intent == null) {
            finish();
            return;
        }

        if (intent.hasExtra(INTENT_FIELD_WORKORDER_ID)) {
            _workorderId = intent.getLongExtra(INTENT_FIELD_WORKORDER_ID, -1);
            _bundleId = intent.getLongExtra(INTENT_FIELD_BUNDLE_ID, -1);
        } else {
            finish();
            return;
        }

        if (_workorderId == -1) {
            finish();
            return;
        }

        _listview = (ListView) findViewById(R.id.items_listview);
        _distanceTextView = (TextView) findViewById(R.id.distance_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _requestButton = (Button) findViewById(R.id.request_button);
        _requestButton.setOnClickListener(_request_onClick);
        _loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);

        _loadingLayout.setVisibility(View.VISIBLE);
        // TODO put into wait mode
    }

    @Override
    protected void onResume() {
        super.onResume();
        _workorderClient = new WorkorderDataClient(_workorderClient_listener);
        _workorderClient.connect(this);

        WorkorderDataClient.requestBundle(this, _bundleId);
    }

    @Override
    protected void onPause() {
        _workorderClient.disconnect(this);
        super.onPause();
    }

    private final WorkorderDataClient.Listener _workorderClient_listener = new WorkorderDataClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClient.registerBundle();
        }

        @Override
        public void onGetBundle(com.fieldnation.data.workorder.Bundle bundle) {
            _woBundle = bundle;
            NumberFormat form = NumberFormat.getNumberInstance();
            form.setMinimumFractionDigits(1);
            form.setMaximumFractionDigits(1);

            try {
                _distanceTextView.setText("Average Distance: " + form.format(_woBundle.getAverageDistance()) + " mi");
            } catch (Exception ex) {
            }
            try {
                _requestButton.setText("Request (" + _woBundle.getWorkorder().length + ")");
            } catch (Exception ex) {
            }
            try {
                _dateTextView.setText("Range " + misc.formatDate(ISO8601.toCalendar(_woBundle.getScheduleRange().getStartDate())) + " - " + misc.formatDate(ISO8601.toCalendar(_woBundle.getScheduleRange().getEndDate())));
            } catch (Exception ex) {
            }

            _adapter = new BundleAdapter(_woBundle, _wocard_listener);

            _listview.setAdapter(_adapter);

            _loadingLayout.setVisibility(View.GONE);
        }
    };

    private final View.OnClickListener _request_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Method Stub: onClick()
            Log.v(TAG, "Method Stub: onClick()");
        }
    };

    private WorkorderCardView.Listener _wocard_listener = new WorkorderCardView.Listener() {
        @Override
        public void viewCounter(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: viewCounter()
            Log.v(TAG, "Method Stub: viewCounter()");

        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: onViewPayments()
            Log.v(TAG, "Method Stub: onViewPayments()");

        }

        @Override
        public void onLongClick(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: onLongClick()
            Log.v(TAG, "Method Stub: onLongClick()");
            GoogleAnalyticsTopicClient.dispatchEvent(WorkorderBundleDetailActivity.this,
                    "BundleActivity", GoogleAnalyticsTopicClient.EventAction.LONG_CLICK,
                    "WorkorderCard", 1);
        }

        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
            Intent intent = new Intent(WorkorderBundleDetailActivity.this, WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
            WorkorderBundleDetailActivity.this.startActivity(intent);
            // Todo set loading here
        }

        @Override
        public void actionRequest(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: actionRequest()
            Log.v(TAG, "Method Stub: actionRequest()");

        }

        @Override
        public void actionCheckout(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: actionCheckout()
            Log.v(TAG, "Method Stub: actionCheckout()");

        }

        @Override
        public void actionCheckin(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: actionCheckin()
            Log.v(TAG, "Method Stub: actionCheckin()");

        }

        @Override
        public void actionAssignment(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: actionAssignment()
            Log.v(TAG, "Method Stub: actionAssignment()");

        }

        @Override
        public void actionAcknowledgeHold(WorkorderCardView view, Workorder workorder) {
            // TODO Method Stub: actionAcknowledgeHold()
            Log.v(TAG, "Method Stub: actionAcknowledgeHold()");

        }
    };

}

