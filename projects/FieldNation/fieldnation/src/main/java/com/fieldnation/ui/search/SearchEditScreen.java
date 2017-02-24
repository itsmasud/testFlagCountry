package com.fieldnation.ui.search;

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
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.savedsearch.SavedSearchClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

import java.util.List;

/**
 * Created by Michael on 7/14/2016.
 */
public class SearchEditScreen extends RelativeLayout {
    private static final String TAG = "SearchEditScreen";

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
    private SavedSearchParams _savedSearchParams;

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

    public void setSavedSearchParams(SavedSearchParams savedSearchParams) {
        _savedSearchParams = savedSearchParams;
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_distanceSpinner == null)
            return;

        if (_savedSearchParams == null)
            return;

        _distanceSpinner.setSelection(_savedSearchParams.uiDistanceSpinner);
        _locationSpinner.setSelection(_savedSearchParams.uiLocationSpinner);
        _otherLocationEditText.setText(_savedSearchParams.uiLocationText);
    }

    private void writeSearch() {
        // Run search and results page
        _savedSearchParams.uiLocationSpinner = _locationSpinner.getSelectedItemPosition();
        _savedSearchParams.uiDistanceSpinner = _distanceSpinner.getSelectedItemPosition();
        _savedSearchParams.radius(DISTANCES[_distanceSpinner.getSelectedItemPosition()]);
        _savedSearchParams.remoteWork = null;

        switch (_locationSpinner.getSelectedItemPosition()) {
            case 0: // profile
                _savedSearchParams.location(null, null);
                SavedSearchClient.save(_savedSearchParams);
                break;
            case 1: // here
                _simpleGps.updateListener(new SimpleGps.Listener() {
                    @Override
                    public void onLocation(SimpleGps simpleGps, Location location) {
                        _savedSearchParams.location(location.getLatitude(), location.getLongitude());
                        _simpleGps.stop();
                        SavedSearchClient.save(_savedSearchParams);
                    }

                    @Override
                    public void onFail(SimpleGps simpleGps) {
                        ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
                    }
                }).start(getContext());
                break;
            case 2: // other
                _savedSearchParams.uiLocationText = _otherLocationEditText.getText().toString();
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
                            _savedSearchParams.location(o.getLatitude(), o.getLongitude());
                        }
                        SavedSearchClient.save(_savedSearchParams);
                    }
                }.executeEx(_otherLocationEditText.getText().toString());
                break;
            case 3: // Remote Work
                _savedSearchParams.remoteWork = true;
                SavedSearchClient.save(_savedSearchParams);
                break;
        }
        SearchTracker.onSearch(App.get(), SearchTracker.Item.SEARCH_BAR, _savedSearchParams);
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

    private final View.OnClickListener _apply_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            writeSearch();
        }
    };

    public interface Listener {
        void onApply();
    }
}