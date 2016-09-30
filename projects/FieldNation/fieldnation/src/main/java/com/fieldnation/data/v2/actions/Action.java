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
    private String object;
    private String id;
    private String[] ids;

    public Action() {
    }

    public Action(JsonObject json) {
        try {
            this.type = ActionType.fromTypeString(json.getString("type"));
            object = json.getString("object");
            if (json.has("id")) {
                id = json.getString("id");
            }
            if (json.has("ids")) {
                JsonArray ja = json.getJsonArray("ids");
                ids = new String[ja.size()];
                for (int i = 0; i < ja.size(); i++) {
                    ids[i] = ja.getString(i);
                }
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public ActionType getType() {
        return type;
    }

    public String getObject() {
        return object;
    }

    public String getId() {
        return id;
    }

    public String[] getIds() {
        return ids;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();

        try {
            obj.put("type", this.type.typestring);
            obj.put("object", object);
            if (id != null)
                obj.put("id", id);
            if (ids != null && ids.length > 0) {
                JsonArray ja = new JsonArray();
                for (String s : ids) {
                    ja.add(s);
                }
                obj.put("ids", ja);
            }
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

    public static JsonArray toJsonArray(Action[] actions) {
        JsonArray ja = new JsonArray();
        if (actions != null) {
            for (Action action : actions) {
                ja.add(action.toJson());
            }
        }
        return ja;
    }

    public enum ActionType {
        ON_MY_WAY("on_my_way"),
        REPORT_A_PROBLEM("report_a_problem"),
        RUNNING_LATE("running_late"),
        CANCEL("cancel"),
        RESCHEDULE("reschedule"),
        CONFIRM("confirm"),
        READY("ready"),
        VIEW("view"),
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
