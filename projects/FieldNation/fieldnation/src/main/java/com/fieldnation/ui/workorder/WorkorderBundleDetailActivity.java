package com.fieldnation.ui.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;

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
//    private RelativeLayout _loadingLayout;

    // Data
    private long _workorderId = 0;
    private long _bundleId = 0;
    private WorkorderClient _workorderClient;
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
//        _loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);

//        _loadingLayout.setVisibility(View.VISIBLE);
        // TODO put into wait mode
    }

    @Override
    protected void onResume() {
        super.onResume();
        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        WorkorderClient.getBundle(this, _bundleId);
    }

    @Override
    protected void onPause() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());
        super.onPause();
    }

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClient.subBundle();
        }

        @Override
        public void onGetBundle(com.fieldnation.data.workorder.Bundle bundle, boolean failed) {
            if (bundle == null) {
                ToastClient.toast(WorkorderBundleDetailActivity.this, "Sorry, could not get bundle details.", Toast.LENGTH_LONG);
                finish();
                return;
            }
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
                _dateTextView.setText("Range " + DateUtils.formatDate(ISO8601.toCalendar(_woBundle.getScheduleRange().getStartDate())) + " - " + DateUtils.formatDate(ISO8601.toCalendar(_woBundle.getScheduleRange().getEndDate())));
            } catch (Exception ex) {
            }

            _adapter = new BundleAdapter(_woBundle, _wocard_listener);

            _listview.setAdapter(_adapter);

//            _loadingLayout.setVisibility(View.GONE);
        }
    };

    private final View.OnClickListener _request_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Method Stub: onClick()
            Log.v(TAG, "Method Stub: onClick()");
        }
    };

    private final WorkorderCardView.Listener _wocard_listener = new WorkorderCardView.DefaultListener() {
        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
            Intent intent = new Intent(WorkorderBundleDetailActivity.this, WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
            WorkorderBundleDetailActivity.this.startActivity(intent);
            // Todo set loading here
        }
    };
}

