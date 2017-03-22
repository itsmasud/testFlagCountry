package com.fieldnation.v2.ui.search;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;

/**
 * Created by mc on 3/22/17.
 */

public class FilterParams {
    private static final String TAG = "FilterParams";

    // List
    @Json
    public String listId = "";
    @Json
    public String title;


    // Filters
    @Json
    public Boolean remoteWork = null;
    @Json
    public Double radius = null;
    @Json
    public Double latitude = null;
    @Json
    public Double longitude = null;

    // UI State
    @Json
    public Integer uiLocationSpinner = 1;
    @Json
    public String uiLocationText = "";
    @Json
    public Integer uiDistanceSpinner = 3;

    public FilterParams() {
    }

    public FilterParams(String listId) {
        this.listId = listId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public GetWorkOrdersOptions applyFilter(GetWorkOrdersOptions options) {

        if ((remoteWork == null || !remoteWork)
                && radius != null && latitude != null && longitude != null) {
            options.setFGeo(radius + "," + latitude + "," + longitude);
        } else {
            options.setFGeo(null);
        }

        if (remoteWork != null && remoteWork) {
            // TODO add the remote work thing here
        }

        return options;
    }

    public static JsonObject toJson(FilterParams filterParams) {
        try {
            return Serializer.serializeObject(filterParams);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static FilterParams fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(FilterParams.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    // FIXME make these calls AsyncTasks so the UI thread isn't waiting on disk IO
    public void save() {
        StoredObject.put(App.get(), App.getProfileId(), "FilterParams", listId, toJson().toByteArray());
    }

    public static FilterParams load(String listId) {
        StoredObject so = StoredObject.get(App.get(), App.getProfileId(), "FilterParams", listId);

        if (so == null)
            return new FilterParams(listId);

        try {
            return fromJson(new JsonObject(so.getData()));
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return new FilterParams(listId);
        }
    }
}
