package com.fieldnation.data.v2.actions;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 9/12/2016.
 */
public class Action {
    private static final String TAG = "Action";

    @Json
    private String type;
    @Json
    private String object;
    @Json
    private String id;

    public Action() {
    }

    public Action(ActionType type) {
        this.type = type.typestring;
    }

    public Action(JsonObject json) {
        try {
            this.type = json.getString("type");

            if (json.has("object"))
                object = json.getString("object");

            if (json.has("id")) {
                id = json.getString("id");
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public ActionType getType() {
        return ActionType.fromTypeString(type);
    }

    public String getObject() {
        return object;
    }

    public String getId() {
        return id;
    }


    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Action schedule) {
        try {
            return Serializer.serializeObject(schedule);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Action fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Action.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Action[] fromJson(JsonArray objects) {
        Action[] actions = new Action[objects.size()];

        for (int i = 0; i < objects.size(); i++) {
            actions[i] = fromJson(objects.getJsonObject(i));
        }

        return actions;
    }

    public enum ActionType {
        ACCEPT("accept"),
        CONFIRM("confirm"),
        ON_MY_WAY("on_my_way"),
        PHONE("phone"),
        RUNNING_LATE("running_late"),
        READY("ready"),
        REPORT_PROBLEM("report_problem"),
        VIEW("view"),
        UNKNOWN("unknown");

        //CANCEL("cancel"),
        //RESCHEDULE("reschedule"),


        private String typestring;

        ActionType(String type) {
            this.typestring = type;
        }

        public static ActionType fromTypeString(String typeString) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i].typestring.equals(typeString))
                    return values()[i];
            }
            return UNKNOWN;
        }
    }
}
