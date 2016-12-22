package com.fieldnation.ui.search;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderActivity;

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
    private HintSpinner _distanceSpinner;

    // Services
    private SimpleGps _simpleGps;

    // Data
    private SavedSearchParams _savedSearchParams;

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
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(getContext(), R.array.search_location, R.layout.view_spinner_item);
        adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _locationSpinner.setAdapter(adapter);
        _locationSpinner.setSelection(1);

        _otherLocationEditText = (EditText) findViewById(R.id.otherLocation_edittext);

        _distanceSpinner = (HintSpinner) findViewById(R.id.distance_spinner);
        adapter = HintArrayAdapter.createFromResources(getContext(), R.array.search_distances, R.layout.view_spinner_item);
        adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _distanceSpinner.setAdapter(adapter);
        _distanceSpinner.setSelection(3);

        if (!App.get().isLocationEnabled()) {
            _locationSpinner.setSelection(0);
        }

        _simpleGps = new SimpleGps(App.get());
    }

    public void setSavedSearchParams(SavedSearchParams savedSearchParams) {
        _savedSearchParams = savedSearchParams;
        populateUi();
    }

    public SavedSearchParams getSavedSearchParams() {
        writeSearch();
        return _savedSearchParams;
    }

    private void populateUi() {
// TODO update the UI to match the saved search
    }

    private void writeSearch() {
        // Run search and results page
        _savedSearchParams.radius(DISTANCES[_distanceSpinner.getSelectedItemPosition()]);

        switch (_locationSpinner.getSelectedItemPosition()) {
            case 0: // profile
                _savedSearchParams.location(null, null);
                break;
            case 1: // here
                _simpleGps.updateListener(new SimpleGps.Listener() {
                    @Override
                    public void onLocation(Location location) {
                        _savedSearchParams.location(location.getLatitude(), location.getLongitude());
                        _simpleGps.stop();
                    }

                    @Override
                    public void onFail() {
                        ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
                    }
                }).start(getContext());
                break;
            case 2: // other
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
                    }
                }.executeEx(_otherLocationEditText.getText().toString());
                break;
        }
    }

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
}
