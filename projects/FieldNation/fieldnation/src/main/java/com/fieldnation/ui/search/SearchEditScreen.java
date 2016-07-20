package com.fieldnation.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.misc;

/**
 * Created by Michael on 7/14/2016.
 */
public class SearchEditScreen extends RelativeLayout {
    private static final String TAG = "SearchEditScreen";

    private static final Integer[] DISTANCES = new Integer[]{
            10, 20, 40, 60, 100, 150, 200, 300, 500
    };

    private static final WorkorderDataSelector[] SELECTORS = new WorkorderDataSelector[]{
            WorkorderDataSelector.ASSIGNED,
            WorkorderDataSelector.AVAILABLE,
            WorkorderDataSelector.CANCELED,
            WorkorderDataSelector.COMPLETED,
            WorkorderDataSelector.REQUESTED,
            WorkorderDataSelector.ROUTED
    };

    private static final int[] indexLookup = new int[]{1, 4, 0, 3, 2, 5};

    // UI
    private RefreshView _loadingView;
    private SearchEditText _searchEditText;
    private HintSpinner _statusSpinner;
    private HintSpinner _locationSpinner;
    private EditText _otherLocationEditText;
    private HintSpinner _distanceSpinner;
    private Button _actionButton;

    // Services
    private WorkorderClient _workorderClient;

    // Data
    private Listener _listener;

    /*-**********************************************-*/
    /*-                  Life Cycle                  -*/
    /*-**********************************************-*/
    public SearchEditScreen(Context context) {
        super(context);
        init();
    }

    public SearchEditScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchEditScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_edit_search, this);

        if (isInEditMode())
            return;

        _loadingView = (RefreshView) findViewById(R.id.loading_view);

        _searchEditText = (SearchEditText) findViewById(R.id.searchedittext);
        _searchEditText.setListener(_searchEditText_listener);

        _statusSpinner = (HintSpinner) findViewById(R.id.status_spinner);
        _statusSpinner.setOnItemSelectedListener(_statusSpinner_onItemSelected);
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                getContext(), R.array.search_status, R.layout.view_spinner_item);
        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _statusSpinner.setAdapter(adapter);
        _statusSpinner.setSelection(0);

        _locationSpinner = (HintSpinner) findViewById(R.id.location_spinner);
        _locationSpinner.setOnItemSelectedListener(_locationSpinner_onItemSelected);
        adapter = HintArrayAdapter.createFromResources(
                getContext(), R.array.search_location, R.layout.view_spinner_item);
        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _locationSpinner.setAdapter(adapter);
        _locationSpinner.setSelection(0);

        _otherLocationEditText = (EditText) findViewById(R.id.otherLocation_edittext);

        _distanceSpinner = (HintSpinner) findViewById(R.id.distance_spinner);
        _distanceSpinner.setOnItemSelectedListener(_distanceSpinner_onItemSelected);
        adapter = HintArrayAdapter.createFromResources(
                getContext(), R.array.search_distances, R.layout.view_spinner_item);
        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _distanceSpinner.setAdapter(adapter);
        _distanceSpinner.setSelection(3);

        _actionButton = (Button) findViewById(R.id.action_button);
        _actionButton.setOnClickListener(_action_onClick);

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        _statusSpinner.setSelection(indexLookup[App.getLastViewedList().ordinal()]);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void reset() {
        _searchEditText.setText("");
        _statusSpinner.setSelection(indexLookup[App.getLastViewedList().ordinal()]);
    }

    private void doSearch() {
        if (misc.isEmptyOrNull(_searchEditText.getText())) {
            // Run search and results page

        } else {
            doWorkorderLookup();
        }
    }

    private void doWorkorderLookup() {
        try {
            _workorderClient.subGet(Long.parseLong(_searchEditText.getText()));
            WorkorderClient.get(App.get(), Long.parseLong(_searchEditText.getText()), false);
            _loadingView.startRefreshing();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private final View.OnClickListener _action_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doSearch();
        }
    };

    private final SearchEditText.Listener _searchEditText_listener = new SearchEditText.Listener() {
        @Override
        public void startSearch(String searchString) {
            doSearch();
        }

        @Override
        public void onTextChanged(CharSequence s) {
            if (misc.isEmptyOrNull(s.toString())) {
                _distanceSpinner.setEnabled(true);
                _locationSpinner.setEnabled(true);
                _statusSpinner.setEnabled(true);
                _otherLocationEditText.setEnabled(true);
            } else {
                _distanceSpinner.setEnabled(false);
                _locationSpinner.setEnabled(false);
                _statusSpinner.setEnabled(false);
                _otherLocationEditText.setEnabled(false);
            }
        }
    };

    private final AdapterView.OnItemSelectedListener _statusSpinner_onItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final AdapterView.OnItemSelectedListener _locationSpinner_onItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 2)
                _otherLocationEditText.setVisibility(VISIBLE);
            else
                _otherLocationEditText.setVisibility(GONE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final AdapterView.OnItemSelectedListener _distanceSpinner_onItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(long workorderId, Workorder workorder, boolean failed, boolean isCached) {
            _loadingView.refreshComplete();
            _workorderClient.unsubGet(workorderId);
            if (workorder == null || failed) {
                if (_listener != null)
                    _listener.showNotAvailableDialog();
            } else {
                ActivityResultClient.startActivity(App.get(),
                        WorkorderActivity.makeIntentShow(App.get(), workorderId));
            }
        }
    };

    public interface Listener {
        void showNotAvailableDialog();
    }
}
