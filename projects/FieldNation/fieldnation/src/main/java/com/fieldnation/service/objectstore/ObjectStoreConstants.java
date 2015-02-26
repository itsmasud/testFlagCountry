package com.fieldnation.service.objectstore;

/**
 * Created by Michael Carver on 2/26/2015.
 */
public interface ObjectStoreConstants {
    public static int WHAT_REPLY_SERVICE = 1;
    public static int WHAT_PUT_OBJECT = 2;
    public static int WHAT_GET_OBJECT = 3;
    public static int WHAT_DELETE_OBJECT = 4;
    public static int WHAT_LIST_OBJECTS = 5;

    public static final String PARAM_OBJECT_TYPE = "PARAM_OBJECT_TYPE";

    public static final String PARAM_OBJECT_ID = "PARAM_OBEJCT_ID";
    public static final String PARAM_IS_FILE = "PARAM_IS_FILE";
    public static final String PARAM_FILE = "PARAM_FILE";
    public static final String PARAM_DATA = "PARAM_DATA";

    public static final String PARAM_RESULT_CODE = "PARAM_RESULT_CODE";

}
