package com.fieldnation.data.v2.actions;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 9/12/2016.
 */
public class Action {
    private static final String TAG = "Action";

    private ActionType type;

    public Action() {
    }

    public Action(JsonObject object) {
        try {
            this.type = ActionType.fromTypeString(object.getString("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public Action(ActionType type) {
        this.type = type;
    }

    public ActionType getType() {
        return type;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();

        try {
            obj.put("type", this.type.typestring);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return obj;
    }

    public static Action[] parseActions(JsonArray objects) {
        Action[] actions = new Action[objects.size()];

        for (int i = 0; i < objects.size(); i++) {
            actions[i] = parseAction(objects.getJsonObject(i));
        }

        return actions;
    }

    public static Action parseAction(JsonObject object) {
        try {
            if (!object.has("type"))
                return null;

            ActionType type = ActionType.fromTypeString(object.getString("type"));

            switch (type) {
                default:
                    return new Action(object);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public enum ActionType {
        ON_MY_WAY("on_my_way"),
        REPORT_A_PROBLEM("report_a_problem"),
        RUNNING_LATE("running_late"),
        CANCEL("cancel"),
        RESCHEDULE("reschedule"),
        CONFIRM("confirm"),
        UNKNOWN("unknown");

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
