package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;
import com.google.android.gms.maps.GoogleMap;

public class LocationView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.LocationView";

    // UI
    private TextView _addressTextView;
    private TextView _distanceTextView;
    private TextView _contactInfoTextView;
    private TextView _descriptionTextView;

    // Data
    private Workorder _workorder;

    // Google Map
    private GoogleMap _googleMap;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public LocationView(Context context) {
        this(context, null);
    }

    public LocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_wd_location, this);

        if (isInEditMode())
            return;

        _addressTextView = (TextView) findViewById(R.id.address_textview);
        _distanceTextView = (TextView) findViewById(R.id.distance_textview);
        _contactInfoTextView = (TextView) findViewById(R.id.contactinfo_textview);
        _descriptionTextView = (TextView) findViewById(R.id.description_textview);
        setVisibility(View.GONE);

    }

	/*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        Location location = _workorder.getLocation();

        if (location == null) {
            // TODO, EPIC FAIL, AND A BAD SOLUTION, MAKE THIS BETTER
            this.setVisibility(GONE);
            return;
        }

        // Todo should be clickable, load google maps
        String fullAddr = location.getFullAddress();
        if (!misc.isEmptyOrNull(fullAddr)) {
            _addressTextView.setText(fullAddr);
            _addressTextView.setVisibility(View.VISIBLE);
        } else {
            _addressTextView.setVisibility(View.GONE);
        }

        if (_workorder.getDistance() != null) {
            _distanceTextView.setText(_workorder.getDistance() + " mi");
        } else if (location.getDistance() != null) {
            _distanceTextView.setText(location.getDistance() + " mi");
        }

        String contactInfo = "";
        if (!misc.isEmptyOrNull(location.getContactName())) {
            contactInfo += location.getContactName() + "\n";
        }
        if (!misc.isEmptyOrNull(location.getContactEmail())) {
            contactInfo += location.getContactEmail() + "\n";
        }
        if (!misc.isEmptyOrNull(location.getContactPhone())) {
            contactInfo += location.getContactPhone() + "\n";
        }

        contactInfo = contactInfo.trim();

        if (!misc.isEmptyOrNull(contactInfo)) {
            _contactInfoTextView.setText(contactInfo);
            Linkify.addLinks(_contactInfoTextView, Linkify.ALL);
            _contactInfoTextView.setVisibility(View.VISIBLE);
        } else {
            _contactInfoTextView.setVisibility(View.GONE);
        }

        if (!misc.isEmptyOrNull(location.getNotes())) {
            _descriptionTextView.setText(location.getNotes());
            Linkify.addLinks(_descriptionTextView, Linkify.ALL);
            _descriptionTextView.setVisibility(VISIBLE);
        } else {
            _descriptionTextView.setVisibility(GONE);
        }
        setVisibility(View.VISIBLE);

        //@TODO FOR MAP VIEW
        /*
        try {
            // Loading map
            initilizeMap();

            // Changing map type
            _googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Showing / hiding your current location
            _googleMap.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            _googleMap.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            _googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            _googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            _googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            _googleMap.getUiSettings().setZoomGesturesEnabled(true);


            //@TODO Provider Location Data
            //@TODO Workorder Location Data
            //Provider Location info
            double latitude = 17.385044;
            double longitude = 78.486671;

            // random latitude and logitude
            double[] randomLocation = createRandLocation(latitude,
                    longitude);

            // Adding a marker
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(randomLocation[0], randomLocation[1]))
                    .title("Provider");

            // changing marker color
            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            _googleMap.addMarker(marker);


            // Move the camera to last position with a zoom level
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(randomLocation[0],
                            randomLocation[1])).zoom(15).build();

            _googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

        } catch (Exception e) {
            e.printStackTrace();
        }

        */
    }


    /**
     * function to load map If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (_googleMap == null) {
            //@TODO
            /*_googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.location_map)).getMap();*/

            // check if map is created successfully or not
            if (_googleMap == null) {
                Toast.makeText(getContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /*
     * creating random postion around a location for testing purpose only
     */
    private double[] createRandLocation(double latitude, double longitude) {

        return new double[]{latitude + ((Math.random() - 0.5) / 500),
                longitude + ((Math.random() - 0.5) / 500),
                150 + ((Math.random() - 0.5) * 10)};
    }

}
