package com.fieldnation.fnanalytics;

import com.fieldnation.fnjson.JsonObject;

/**
 * Created by Michael on 9/14/2016.
 */
public interface EventContext {

    String getContextName();

    JsonObject toJson();
}
