package com.fieldnation.v2.ui.workorder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.data.gmaps.GmapsDirections;
import com.fieldnation.data.gmaps.GmapsRoute;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.gmaps.GmapsClient;
import com.fieldnation.service.data.gmaps.Marker;
import com.fieldnation.service.data.gmaps.Position;
import com.fieldnation.v2.data.model.Coords;
import com.fieldnation.v2.data.model.Location;
import com.fieldnation.v2.data.model.WorkOrder;

public class LocationView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "LocationView";

    private static final int ACTION_NAVIGATE = 0;
    private static final int ACTION_GPS_SETTINGS = 1;
    private static final int ACTION_MESSAGES = 2;

    // UI
    private TextView _noLocationTextView;
    private RelativeLayout _noLocationLayout;

    private RelativeLayout _mapLayout;
    private ImageView _mapImageView;
    private ProgressBar _loadingProgress;

    private LinearLayout _noMapLayout;
    private TextView _gpsError1TextView;
    private TextView _gpsError2TextView;

    private RelativeLayout _addressLayout;
    private TextView _siteTitleTextView;
    private TextView _addressTextView;

    private TextView _locationTypeTextView;

    private TextView _distanceTextView;
    private View _noteDivider;
    private TextView _noteTextView;

    // Data
    private WorkOrder _workOrder;
    private android.location.Location _userLocation = null;
    private Bitmap _map = null;
    private boolean _mapUnavailable = false;
    private GmapsDirections _directions = null;
    private boolean _invalidAddress = false;
    private int _action = ACTION_NAVIGATE;


    // Services
    private GmapsClient _gmapsClient = null;
    private SimpleGps _simpleGps;

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

        _noLocationTextView = findViewById(R.id.noLocation_textview);
        _noLocationLayout = findViewById(R.id.noLocation_layout);

        _mapLayout = findViewById(R.id.map_layout);
        _mapImageView = findViewById(R.id.mapview);
        _mapImageView.setOnClickListener(_action_onClick);
        _loadingProgress = findViewById(R.id.loading_progress);

        _noMapLayout = findViewById(R.id.noMap_layout);
        _noMapLayout.setOnClickListener(_action_onClick);

        _gpsError1TextView = findViewById(R.id.gpsError1_textview);
        _gpsError2TextView = findViewById(R.id.gpsError2_textview);

        _addressLayout = findViewById(R.id.address_layout);

        _siteTitleTextView = findViewById(R.id.siteTitle_textview);
        _addressTextView = findViewById(R.id.address_textview);
        _addressTextView.setOnLongClickListener(_notes_onLongClick);

        _locationTypeTextView = findViewById(R.id.locationType_textview);

        _distanceTextView = findViewById(R.id.distance_textview);
        _noteTextView = findViewById(R.id.note_textview);
        _noteTextView.setOnLongClickListener(_notes_onLongClick);
        _noteDivider = findViewById(R.id.note_divider);

        _gmapsClient = new GmapsClient(_gmapsClient_listener);
        _gmapsClient.connect(App.get());

        _simpleGps = new SimpleGps(getContext())
                .updateListener(_gpsListener)
                .start(getContext());

        setVisibility(View.GONE);

        getViewTreeObserver().addOnGlobalLayoutListener(_layoutObserver);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener _layoutObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Log.v(TAG, "layoutComplete");
            lookupMap();
            getViewTreeObserver().removeGlobalOnLayoutListener(_layoutObserver);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (_gmapsClient != null) _gmapsClient.disconnect(App.get());
        _simpleGps.stop();
        super.onDetachedFromWindow();
    }

    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;

        if (_gmapsClient != null && _gmapsClient.isConnected()) {
            _gmapsClient.subStaticMapClassic(_workOrder.getId());
            _gmapsClient.subDirections(_workOrder.getId());
            lookupMap();
        }
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null || _mapImageView == null || _workOrder.getLocation().getMode() == null)
            return;

        setVisibility(VISIBLE);

        _action = ACTION_NAVIGATE;

        populateAddressTile();
        populateMap();
        calculateAddressTileVisibility();
    }

    private void calculateAddressTileVisibility() {
        if (_invalidAddress) return;

        if (_workOrder.getLocation().getMode() == null) {
            return;
        }

        // hide stuff that shouldn't be seen
        if (_workOrder.getLocation().getMode() == Location.ModeEnum.REMOTE) {
            _mapLayout.setVisibility(GONE);
            _noLocationLayout.setVisibility(VISIBLE);
            _distanceTextView.setVisibility(GONE);
            _addressLayout.setVisibility(GONE);
        } else {
            _noLocationLayout.setVisibility(GONE);
        }
    }

    private void populateAddressTile() {
        if (_invalidAddress) return;
        // Address info
        Location loc = _workOrder.getLocation();
        if (loc.getMode() == null)
            return;

        // set site title
        String title = "";
        if (loc.getSavedLocation() != null) {
            if (loc.getSavedLocation().getGroup() != null && !misc.isEmptyOrNull(loc.getSavedLocation().getGroup().getName())) {
                title = loc.getSavedLocation().getGroup().getName().trim() + "/";
            }
            if (!misc.isEmptyOrNull(loc.getSavedLocation().getName())) {
                title += loc.getSavedLocation().getName().trim();
            }
        }
        if (!misc.isEmptyOrNull(title)) {
            _siteTitleTextView.setText(title);
            _siteTitleTextView.setVisibility(VISIBLE);
        } else {
            _siteTitleTextView.setVisibility(GONE);
        }

        // set address line
        if (!misc.isEmptyOrNull(loc.getFullAddressOneLine())) {
            _addressTextView.setVisibility(VISIBLE);
            _addressTextView.setText(loc.getFullAddressOneLine());
            _addressTextView.setTag(loc.getFullAddressOneLine());
        } else {
            _addressTextView.setVisibility(GONE);
        }

        // set location type
        if (loc.getType().getId() == null || misc.isEmptyOrNull(loc.getType().getName())) {
            _locationTypeTextView.setVisibility(GONE);
        } else {
            _locationTypeTextView.setText(misc.capitalize(loc.getType().getName()));
            _locationTypeTextView.setVisibility(VISIBLE);
        }

        // distance
        _distanceTextView.setVisibility(VISIBLE);
        if (!_simpleGps.isLocationEnabled()) {
            _distanceTextView.setText("");
        } else if (_directions != null) {
            double miles = 0.0;
            GmapsRoute[] routes = _directions.getRoutes();
            for (GmapsRoute route : routes) {
                miles += route.getDistanceMi();
            }
            _distanceTextView.setText(getResources().getString(R.string.num_mi_driving, misc.to2Decimal(miles)));
        } else if (_userLocation != null && loc.getCoordinates() != null && loc.getCoordinates().getLongitude() != null && loc.getCoordinates().getLatitude() != null) {
            try {
                Position siteLoc = new Position(loc.getCoordinates().getLatitude(), loc.getCoordinates().getLongitude());
                Position myLoc = new Position(_userLocation.getLatitude(), _userLocation.getLongitude());

                _distanceTextView.setText(getResources().getString(R.string.num_mi_straight_line, misc.to2Decimal(myLoc.distanceTo(siteLoc))));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                _distanceTextView.setText("");
            }

        } else if (loc.getCoordinates() == null) {
            _distanceTextView.setText("");
        } else if (_invalidAddress) {
            _distanceTextView.setText("");
        } else {
            _distanceTextView.setText("");
        }

        // display location notes
        if (loc.getNotes() != null
                && loc.getNotes().length > 0
                && loc.getNotes()[0] != null
                && !misc.isEmptyOrNull(loc.getNotes()[0].getText())) {
            _noteTextView.setVisibility(VISIBLE);
            _noteTextView.setTag(loc.getNotes()[0].getText());
            _noteTextView.setText(misc.linkifyHtml(loc.getNotes()[0].getText(), Linkify.ALL));
            _noteTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            _noteTextView.setVisibility(GONE);
            _noteDivider.setVisibility(GONE);
        }
    }

    private void populateMap() {
        if (_invalidAddress) {
            _action = ACTION_MESSAGES;
            return;
        }

        if (_workOrder.getLocation().getMode() == null
                || _workOrder.getLocation().getMode() == Location.ModeEnum.REMOTE) {
            // remote work
            _noMapLayout.setVisibility(GONE);

        } else if (!_simpleGps.isLocationEnabled()) {
            // no gps - !isLocationEnabled()
            _loadingProgress.setVisibility(GONE);
            _mapImageView.setImageResource(R.drawable.no_map);
            _noMapLayout.setVisibility(VISIBLE);
            _action = ACTION_GPS_SETTINGS;
            _gpsError1TextView.setText(R.string.map_not_available);
            _gpsError2TextView.setText(R.string.check_gps_settings);

        } else if (_workOrder.getLocation().getCoordinates().getLongitude() == null
                || _workOrder.getLocation().getCoordinates().getLatitude() == null) {
            // no geo data - ie bad address - _workorder.getLocation() == null || _workord
            _loadingProgress.setVisibility(GONE);
            _mapImageView.setImageResource(R.drawable.no_map);
            _noMapLayout.setVisibility(VISIBLE);
            _gpsError1TextView.setText(R.string.map_not_available);
            _gpsError2TextView.setText(R.string.no_location_provided_by_buyer);

        } else {
            if (_mapUnavailable) {
                _loadingProgress.setVisibility(GONE);
                _mapImageView.setImageResource(R.drawable.no_map);
                _noMapLayout.setVisibility(VISIBLE);
                _gpsError1TextView.setText(R.string.map_not_available);
                _gpsError2TextView.setText(R.string.error_looking_up_map);

                // loading
            } else if (_map == null) { //  !_mapUnavailable
                _loadingProgress.setVisibility(VISIBLE);
                _mapImageView.setImageResource(R.drawable.no_map);
                _noMapLayout.setVisibility(GONE);

                // have all the things
            } else {
                _loadingProgress.setVisibility(GONE);
                _mapImageView.setImageBitmap(_map);
                _noMapLayout.setVisibility(GONE);
            }
        }
    }

    private void lookupMap() {
        Log.v(TAG, "lookupMap");
//        if (_invalidAddress) return;
        if (_gmapsClient == null || !_gmapsClient.isConnected())
            return;

        Log.v(TAG, "lookupMap - 1");
        if (_workOrder == null)
            return;

        Log.v(TAG, "lookupMap - 2");
        if (_userLocation == null)
            return;

        Log.v(TAG, "lookupMap - 3");
        if (_mapImageView == null || _mapLayout.getVisibility() == GONE)
            return;

        Log.v(TAG, "lookupMap - 4");
        if ((_mapImageView.getWidth() == 0 || _mapImageView.getHeight() == 0)
                && (_workOrder != null && _workOrder.getLocation().getMode() != null)) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    lookupMap();
                }
            }, 100);
            return;
        }

        Log.v(TAG, "lookupMap - 5");
        Marker start = new Marker(_userLocation.getLatitude(), _userLocation.getLongitude(),
                getContext().getString(R.string.gmap_startMarkerUrl));
        Position startPos = new Position(_userLocation.getLatitude(), _userLocation.getLongitude());

        Marker end = null;
        Position endPos = null;
        Coords geo = _workOrder.getLocation().getCoordinates();
        if (geo != null && geo.getLongitude() != null && geo.getLatitude() != null) {
            _invalidAddress = false;
            endPos = new Position(geo.getLatitude(), geo.getLongitude());
// TODO               if (geo.getObfuscated() || !geo.getPrecise()) {
//                    end = new Marker(geo.getLatitude(), geo.getLongitude(),
//                            getContext().getString(R.string.gmap_inpreciseMarkerUrl));
//                } else {
//                    GmapsClient.getDirections(App.get(), _workorder.getWorkorderId(), startPos, endPos);
            end = new Marker(geo.getLatitude(), geo.getLongitude(), getContext().getString(R.string.gmap_endMarkerUrl));
//                }
        } else {
            // invalid location
            _invalidAddress = true;
            _loadingProgress.setVisibility(GONE);
            _mapImageView.setImageResource(R.drawable.no_map);
            _noMapLayout.setVisibility(VISIBLE);
            _action = ACTION_MESSAGES;
            _gpsError1TextView.setText(R.string.invalid_address);
            _gpsError2TextView.setText(R.string.contact_wo_manager);
            _distanceTextView.setText("");
            _gpsError1TextView.setVisibility(VISIBLE);
            _gpsError2TextView.setVisibility(VISIBLE);

            return;
        }

        if (end != null) {
            // clamping the height to make the image look nice on all phone screens
            int width = (_mapImageView.getWidth() * 180) / _mapImageView.getHeight();
            int height = 180;

            GmapsClient.getStaticMapClassic(App.get(), _workOrder.getId(), start, end,
                    width, height);
        } else {
            _mapUnavailable = true;
            populateUi();
        }
    }

    private void showMapActivity(Uri geoLocation) {
        Intent _intent = new Intent(Intent.ACTION_VIEW);
        _intent.setData(geoLocation);
        if (_intent.resolveActivity(getContext().getPackageManager()) != null) {
            WorkOrderTracker.directionsEvent(App.get());
            getContext().startActivity(_intent);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final OnLongClickListener _notes_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipboardManager clipboard = (android.content.ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = android.content.ClipData.newPlainText("Copied Text", (String) v.getTag());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), R.string.toast_copied_to_clipboard, Toast.LENGTH_LONG);
            return true;
        }
    };

    private final View.OnClickListener _action_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "_action_onClick");
            switch (_action) {
                case ACTION_GPS_SETTINGS: {
                    final Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ActivityClient.startActivity(intent);
                    break;
                }
                case ACTION_MESSAGES: {
                    ActivityClient.startActivity(WorkOrderActivity.makeIntentMessages(getContext(), _workOrder.getId()));
                    break;
                }
                case ACTION_NAVIGATE: {
                    if (_workOrder != null
                            && _workOrder.getLocation().getMode() != null
                            && _workOrder.getLocation().getMode() != Location.ModeEnum.REMOTE) {
                        Location location = _workOrder.getLocation();
                        try {
                            String _fullAddress = misc.escapeForURL(location.getFullAddressOneLine());
                            String _uriString = "geo:0,0?q=" + _fullAddress;
                            Uri _uri = Uri.parse(_uriString);
                            showMapActivity(_uri);
                        } catch (Exception e) {
                        }
                    }
                    break;
                }
            }
        }
    };

    private final SimpleGps.Listener _gpsListener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, android.location.Location location) {
            Log.v(TAG, "_gpsListener");
            _userLocation = location;
            lookupMap();
            _simpleGps.stop();
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
            ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
            _simpleGps.stop();
        }

        @Override
        public void onPermissionDenied(SimpleGps simpleGps) {
            _mapUnavailable = true;
            _simpleGps.stop();
            populateUi();
        }
    };

    private final GmapsClient.Listener _gmapsClient_listener = new GmapsClient.Listener() {
        @Override
        public void onConnected() {
            if (_workOrder != null) {
                _gmapsClient.subStaticMapClassic(_workOrder.getId());
                _gmapsClient.subDirections(_workOrder.getId());
                lookupMap();
            }
        }

        @Override
        public void onStaticMapClassic(long workorderId, Bitmap bitmap, boolean failed) {
            Log.v(TAG, "onStaticMapClassic");
            if (bitmap == null || failed) {
                _map = null;
                _mapUnavailable = true;
            } else {
                _map = bitmap;
                _mapUnavailable = false;
            }
            populateUi();
        }

        @Override
        public void onDirections(long workorderId, GmapsDirections directions) {
            Log.v(TAG, "onDirections");

            if (directions != null && directions.getCode().equals("Ok")) {
                _directions = directions;
                populateUi();
            }
        }
    };
}
