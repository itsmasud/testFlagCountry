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
        this.type = type.typeString;
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
        ACK_HOLD("ack_hold"),
        ACK_UPDATE("ack_update"),
        CHECK_IN("check_in"),
        CHECK_OUT("check_out"),
        CONFIRM("confirm"),
        DECLINE("decline"),
        MAP("map"),
        MARK_COMPLETE("mark_complete"),
        MARK_INCOMPLETE("mark_incomplete"),
        MESSAGE("message"),
        ON_MY_WAY("on_my_way"),
        PHONE("phone"),
        READY("ready"), // NCNS ready confirm
        READY_TO_GO("ready_to_go"), // normal ready to go system
        REPORT_PROBLEM("report_problem"),
        REQUEST("request"),

        RUNNING_LATE("running_late"),
        VIEW("view"),
        VIEW_BUNDLE("view_bundle"),
        VIEW_PAYMENT("view_payment"),
        WITHDRAW("withdraw"),

        // Push notifications
        NOT_SUPPORTED(null);


        private String typeString;

        ActionType(String type) {
            this.typeString = type;
        }

        public static ActionType fromTypeString(String typeString) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i].typeString.equals(typeString))
                    return values()[i];
            }
            return NOT_SUPPORTED;
        }
    }
}
