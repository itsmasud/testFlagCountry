package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.SearchTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.RightDrawerDialog;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.v2.ui.search.FilterParams;

import java.util.List;

/**
 * Created by mc on 1/3/17.
 */

public class FilterDrawerDialog extends RightDrawerDialog {
    private static final String TAG = "FilterDrawerDialog";

    private static final Double[] DISTANCES = new Double[]{
            10.0, 20.0, 40.0, 60.0, 100.0, 150.0, 200.0, 300.0, 500.0
    };

    // Ui
    private HintSpinner _locationSpinner;
    private EditText _otherLocationEditText;
    private TextView _distanceTextView;
    private HintSpinner _distanceSpinner;
    private Button _closeButton;
    private Button _clearButton;
    private Button _applyButton;

    // Services
    private SimpleGps _simpleGps;

    // Data
    private FilterParams _filterParams;

    public FilterDrawerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_filter_drawer, container, false);

        _locationSpinner = (HintSpinner) v.findViewById(R.id.location_spinner);
        _otherLocationEditText = (EditText) v.findViewById(R.id.otherLocation_edittext);
        _distanceTextView = (TextView) v.findViewById(R.id.distance_textview);
        _distanceSpinner = (HintSpinner) v.findViewById(R.id.distance_spinner);
        _applyButton = (Button) v.findViewById(R.id.apply_button);
        _clearButton = (Button) v.findViewById(R.id.clear_button);
        _closeButton = (Button) v.findViewById(R.id.close_button);

        _simpleGps = new SimpleGps(App.get());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _locationSpinner.setOnItemSelectedListener(_locationSpinner_onItemSelected);
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(getView().getContext(), R.array.search_location, R.layout.view_spinner_item_dark);
        adapter.setDropDownViewResource(R.layout.view_dropdown_item_dark);
        _locationSpinner.setAdapter(adapter);
        _locationSpinner.setSelection(1);

        adapter = HintArrayAdapter.createFromResources(getView().getContext(), R.array.search_distances, R.layout.view_spinner_item_dark);
        adapter.setDropDownViewResource(R.layout.view_dropdown_item_dark);
        _distanceSpinner.setAdapter(adapter);
        _distanceSpinner.setSelection(3);

        _clearButton.setOnClickListener(_clear_onClick);
        _applyButton.setOnClickListener(_apply_onClick);
        _closeButton.setOnClickListener(_close_onClick);

        SearchTracker.onShow(App.get());

        if (!App.get().isLocationEnabled()) {
            _locationSpinner.setSelection(0);
        }

        misc.hideKeyboard(getView());
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        _filterParams = FilterParams.load(payload.getString("listId"));

        populateUi();
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

                    @Override
                    public void onPermissionDenied(SimpleGps simpleGps) {
                    }
                }).start(App.get());
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
    }

    private final AdapterView.OnItemSelectedListener _locationSpinner_onItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 2) // other place
                _otherLocationEditText.setVisibility(View.VISIBLE);
            else
                _otherLocationEditText.setVisibility(View.GONE);

            if (position == 3) { // remote work
                _distanceSpinner.setVisibility(View.GONE);
                _distanceTextView.setVisibility(View.GONE);
            } else {
                _distanceSpinner.setVisibility(View.VISIBLE);
                _distanceTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final View.OnClickListener _apply_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            writeSearch();
            _onOkDispatcher.dispatch(getUid(), _filterParams);
            dismiss(true);
        }
    };

    private final View.OnClickListener _clear_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _filterParams = new FilterParams(_filterParams.listId);
            _filterParams.save();
            _onOkDispatcher.dispatch(getUid(), _filterParams);
            dismiss(true);
        }
    };

    private final View.OnClickListener _close_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, String listId) {
        Bundle bundle = new Bundle();
        bundle.putString("listId", listId);
        Controller.show(context, uid, FilterDrawerDialog.class, bundle);
    }

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(FilterParams filterParams);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((FilterParams) parameters[0]);
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }

}
