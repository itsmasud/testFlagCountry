package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.LocationAttribute;
import com.fieldnation.data.bv2.model.LocationNote;
import com.fieldnation.data.bv2.model.StoredLocation;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class LocationsWebApi {
    private static final String TAG = "LocationsWebApi";

    /**
     * Adds a note to a stored location
     *
     * @param locationId Location id
     * @param json       Notes
     */
    public static void addNotes(Context context, int locationId, LocationNote json) {
    }

    /**
     * Adds an attribute to a stored location
     *
     * @param locationId Location id
     * @param attribute  Attribute
     * @param json       JSON Model
     */
    public static void addAttribute(Context context, int locationId, int attribute, LocationAttribute json) {
    }

    /**
     * Removes an attribute from a stored location
     *
     * @param locationId Location id
     * @param attribute  Attribute
     */
    public static void removeAttribute(Context context, int locationId, int attribute) {
    }

    /**
     * Get a list of supported countries for selection
     */
    public static void getCountries(Context context) {
    }

    /**
     * Add a location to company
     *
     * @param json JSON payload
     */
    public static void addLocations(Context context, StoredLocation json) {
    }

    /**
     * Gets stored locations
     */
    public static void getLocations(Context context) {
    }

    /**
     * Removes a note attached to a stored location
     *
     * @param locationId Location id
     * @param noteId     Location note id
     */
    public static void removeNote(Context context, int locationId, int noteId) {
    }

    /**
     * Updates a note attached to a stored location
     *
     * @param locationId Location id
     * @param noteId     Location note id
     * @param json       Notes
     */
    public static void updateNote(Context context, int locationId, int noteId, LocationNote json) {
    }

    /**
     * Soft deletes a stored location
     *
     * @param locationId Location id
     */
    public static void removeLocation(Context context, int locationId) {
    }

    /**
     * Updates a stored location
     *
     * @param locationId Location id
     */
    public static void updateLocation(Context context, int locationId) {
    }

}
