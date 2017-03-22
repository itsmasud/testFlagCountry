package com.fieldnation.v2.ui.search;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.SearchTracker;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

import java.util.List;

/**
 * Created by Michael on 7/14/2016.
 */
public class FilterEditScreen extends RelativeLayout {
    private static final String TAG = "FilterEditScreen";

    private static final Double[] DISTANCES = new Double[]{
            10.0, 20.0, 40.0, 60.0, 100.0, 150.0, 200.0, 300.0, 500.0
    };

    // UI
    private HintSpinner _locationSpinner;
    private EditText _otherLocationEditText;
    private TextView _distanceTextView;
    private HintSpinner _distanceSpinner;
    private Button _applyButton;

    // Services
    private SimpleGps _simpleGps;

    // Data
    private FilterParams _filterParams;

    private Listener _listener;

    /*-**********************************************-*/
    /*-                  Life Cycle                  -*/
    /*-**********************************************-*/
    public FilterEditScreen(Context context) {
        super(context);
        init();
    }

    public FilterEditScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FilterEditScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_edit_filter, this);

        if (isInEditMode())
            return;

        _locationSpinner = (HintSpinner) findViewById(R.id.location_spinner);
        _locationSpinner.setOnItemSelectedListener(_locationSpinner_onItemSelected);
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(getContext(), R.array.search_location, R.layout.view_spinner_item_dark);
        adapter.setDropDownViewResource(R.layout.view_dropdown_item_dark);
        _locationSpinner.setAdapter(adapter);
        _locationSpinner.setSelection(1);

        _otherLocationEditText = (EditText) findViewById(R.id.otherLocation_edittext);

        _distanceTextView = (TextView) findViewById(R.id.distance_textview);

        _distanceSpinner = (HintSpinner) findViewById(R.id.distance_spinner);
        adapter = HintArrayAdapter.createFromResources(getContext(), R.array.search_distances, R.layout.view_spinner_item_dark);
        adapter.setDropDownViewResource(R.layout.view_dropdown_item_dark);
        _distanceSpinner.setAdapter(adapter);
        _distanceSpinner.setSelection(3);

        _applyButton = (Button) findViewById(R.id.apply_button);
        _applyButton.setOnClickListener(_apply_onClick);

        if (!App.get().isLocationEnabled()) {
            _locationSpinner.setSelection(0);
        }

        _simpleGps = new SimpleGps(App.get());

        populateUi();

        misc.hideKeyboard(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        misc.hideKeyboard(this);

        super.onDetachedFromWindow();
    }

    public void setFilterParams(FilterParams filterParams) {
        _filterParams = filterParams;
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_distanceSpinner == null)
            return;

        if (_filterParams == null)
            return;

        _distanceSpinner.setSelection(_filterParams.uiDistanceSpinner);
        _locationSpinner.setSelection(_filterParams.uiLocationSpinner);
        _otherLocationEditText.setText(_filterParams.uiLocationText);
    }

    private void writeSearch() {
        // Run search and results page
        _filterParams.uiLocationSpinner = _locationSpinner.getSelectedItemPosition();
        _filterParams.uiDistanceSpinner = _distanceSpinner.getSelectedItemPosition();
        _filterParams.radius = DISTANCES[_distanceSpinner.getSelectedItemPosition()];
        _filterParams.remoteWork = null;

        switch (_locationSpinner.getSelectedItemPosition()) {
            case 0: // profile
                _filterParams.latitude = null;
                _filterParams.longitude = null;
                _filterParams.save();
                break;
            case 1: // here
                _simpleGps.updateListener(new SimpleGps.Listener() {
                    @Override
                    public void onLocation(SimpleGps simpleGps, Location location) {
                        _filterParams.latitude = location.getLatitude();
                        _filterParams.longitude = location.getLongitude();
                        _simpleGps.stop();
                        _filterParams.save();
                    }

                    @Override
                    public void onFail(SimpleGps simpleGps) {
                        ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
                    }
                }).start(getContext());
                break;
            case 2: // other
                _filterParams.uiLocationText = _otherLocationEditText.getText().toString();
                new AsyncTaskEx<String, Object, Address>() {
                    @Override
                    protected Address doInBackground(String... params) {
                        try {
                            List<Address> list = new Geocoder(App.get()).getFromLocationName(params[0], 5);

                            return list.get(0);

                        } catch (Exception ex) {
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Address o) {
                        if (o != null) {
                            _filterParams.latitude = o.getLatitude();
                            _filterParams.longitude = o.getLongitude();
                        }
                        _filterParams.save();
                    }
                }.executeEx(_otherLocationEditText.getText().toString());
                break;
            case 3: // Remote Work
                _filterParams.remoteWork = true;
                _filterParams.save();
                break;
        }
        SearchTracker.onSearch(App.get(), SearchTracker.Item.SEARCH_BAR, _filterParams);
        if (_listener != null)
            _listener.onApply();
    }

    private final AdapterView.OnItemSelectedListener _locationSpinner_onItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 2) // other place
                _otherLocationEditText.setVisibility(VISIBLE);
            else
                _otherLocationEditText.setVisibility(GONE);

            if (position == 3) { // remote work
                _distanceSpinner.setVisibility(GONE);
                _distanceTextView.setVisibility(GONE);
            } else {
                _distanceSpinner.setVisibility(VISIBLE);
                _distanceTextView.setVisibility(VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final OnClickListener _apply_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            writeSearch();
        }
    };

    public interface Listener {
        void onApply();
    }
}