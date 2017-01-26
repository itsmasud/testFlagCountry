package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.CustomField;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class CustomFieldsWebApi {
    private static final String TAG = "CustomFieldsWebApi";

    /**
     * Updates a work order custom field's visibility for a single client
     *
     * @param customFieldId Custom field id
     * @param clientId      Client id
     * @param visibility    Visibility (visible or hidden)
     */
    public static void updateCustomFieldVisibility(Context context, int customFieldId, int clientId, String visibility) {
    }

    /**
     * Updates a work order custom field's visibility for all projects and clients
     *
     * @param customFieldId Custom field id
     * @param visibility    Visibility (visible or hidden)
     */
    public static void updateCustomFieldVisibility(Context context, int customFieldId, String visibility) {
    }

    /**
     * Removes a work order custom field
     *
     * @param customFieldId Custom field id
     */
    public static void removeCustomField(Context context, int customFieldId) {
    }

    /**
     * Updates a work order custom field
     *
     * @param customFieldId Custom field id
     * @param json          JSON Model
     */
    public static void updateCustomField(Context context, int customFieldId, CustomField json) {
    }

    /**
     * Adds a work order custom field
     *
     * @param json JSON Model
     */
    public static void addCustomField(Context context, CustomField json) {
    }

    /**
     * Gets a list of work order custom fields
     */
    public static void getCustomFields(Context context) {
    }

    /**
     * Updates a work order custom field's visibility for a single project
     *
     * @param customFieldId Custom field id
     * @param projectId     Project id
     * @param visibility    Visibility (visible or hidden)
     */
    public static void updateCustomFieldVisibilityByProject(Context context, int customFieldId, int projectId, String visibility) {
    }

}
