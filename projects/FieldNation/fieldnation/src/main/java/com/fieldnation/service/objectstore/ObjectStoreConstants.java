package com.fieldnation.service.objectstore;

/**
 * Created by Michael Carver on 2/26/2015.
 */
public interface ObjectStoreConstants {
    int WHAT_PUT_OBJECT = 2;
    int WHAT_GET_OBJECT = 3;
    int WHAT_DELETE_OBJECT = 4;
    int WHAT_LIST_OBJECTS = 5;

    String PARAM_PROFILE_ID = "PARAM_PROFILE_ID";
    String PARAM_OBJECT_TYPE = "PARAM_OBJECT_TYPE";
    String PARAM_OBJECT_KEY = "PARAM_OBJECT_KEY";
    String PARAM_IS_FILE = "PARAM_IS_FILE";
    String PARAM_FILE = "PARAM_FILE";
    String PARAM_DATA = "PARAM_DATA";
    String PARAM_LAST_UPDATED = "PARAM_LAST_UPDATED";
    String PARAM_ID = "PARAM_ID";
    String PARAM_EXPIRES = "PARAM_EXPIRES";

    String PARAM_OBJECT_LIST = "PARAM_OBJECT_LIST";
}
