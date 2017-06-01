package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class Problem implements Parcelable {
    private static final String TAG = "Problem";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "author")
    private User _author;

    @Json(name = "comments")
    private String _comments;

    @Json(name = "contact")
    private ContactEnum _contact;

    @Json(name = "created")
    private Date _created;

    @Json(name = "escalate")
    private Boolean _escalate;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "message")
    private ProblemMessage _message;

    @Json(name = "resolution")
    private ProblemResolution _resolution;

    @Json(name = "type")
    private ProblemType _type;

    @Json(name = "watchers")
    private Integer[] _watchers;

    @Source
    private JsonObject SOURCE;

    public Problem() {
        SOURCE = new JsonObject();
    }

    public Problem(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _actions;
    }

    public Problem actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        try {
            if (_author == null && SOURCE.has("author") && SOURCE.get("author") != null)
                _author = User.fromJson(SOURCE.getJsonObject("author"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_author != null && _author.isSet())
        return _author;

        return null;
    }

    public Problem author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
        return this;
    }

    public void setComments(String comments) throws ParseException {
        _comments = comments;
        SOURCE.put("comments", comments);
    }

    public String getComments() {
        try {
            if (_comments == null && SOURCE.has("comments") && SOURCE.get("comments") != null)
                _comments = SOURCE.getString("comments");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _comments;
    }

    public Problem comments(String comments) throws ParseException {
        _comments = comments;
        SOURCE.put("comments", comments);
        return this;
    }

    public void setContact(ContactEnum contact) throws ParseException {
        _contact = contact;
        SOURCE.put("contact", contact.toString());
    }

    public ContactEnum getContact() {
        try {
            if (_contact == null && SOURCE.has("contact") && SOURCE.get("contact") != null)
                _contact = ContactEnum.fromString(SOURCE.getString("contact"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _contact;
    }

    public Problem contact(ContactEnum contact) throws ParseException {
        _contact = contact;
        SOURCE.put("contact", contact.toString());
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        try {
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_created != null && _created.isSet())
        return _created;

        return null;
    }

    public Problem created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setEscalate(Boolean escalate) throws ParseException {
        _escalate = escalate;
        SOURCE.put("escalate", escalate);
    }

    public Boolean getEscalate() {
        try {
            if (_escalate == null && SOURCE.has("escalate") && SOURCE.get("escalate") != null)
                _escalate = SOURCE.getBoolean("escalate");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _escalate;
    }

    public Problem escalate(Boolean escalate) throws ParseException {
        _escalate = escalate;
        SOURCE.put("escalate", escalate);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public Problem id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setMessage(ProblemMessage message) throws ParseException {
        _message = message;
        SOURCE.put("message", message.getJson());
    }

    public ProblemMessage getMessage() {
        try {
            if (_message == null && SOURCE.has("message") && SOURCE.get("message") != null)
                _message = ProblemMessage.fromJson(SOURCE.getJsonObject("message"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_message != null && _message.isSet())
        return _message;

        return null;
    }

    public Problem message(ProblemMessage message) throws ParseException {
        _message = message;
        SOURCE.put("message", message.getJson());
        return this;
    }

    public void setResolution(ProblemResolution resolution) throws ParseException {
        _resolution = resolution;
        SOURCE.put("resolution", resolution.getJson());
    }

    public ProblemResolution getResolution() {
        try {
            if (_resolution == null && SOURCE.has("resolution") && SOURCE.get("resolution") != null)
                _resolution = ProblemResolution.fromJson(SOURCE.getJsonObject("resolution"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_resolution != null && _resolution.isSet())
        return _resolution;

        return null;
    }

    public Problem resolution(ProblemResolution resolution) throws ParseException {
        _resolution = resolution;
        SOURCE.put("resolution", resolution.getJson());
        return this;
    }

    public void setType(ProblemType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
    }

    public ProblemType getType() {
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = ProblemType.fromJson(SOURCE.getJsonObject("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_type != null && _type.isSet())
        return _type;

        return null;
    }

    public Problem type(ProblemType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
        return this;
    }

    public void setWatchers(Integer[] watchers) throws ParseException {
        _watchers = watchers;
        JsonArray ja = new JsonArray();
        for (Integer item : watchers) {
            ja.add(item);
        }
        SOURCE.put("watchers", ja);
    }

    public Integer[] getWatchers() {
        try {
            if (_watchers != null)
                return _watchers;

            if (SOURCE.has("watchers") && SOURCE.get("watchers") != null) {
                JsonArray ja = SOURCE.getJsonArray("watchers");
                _watchers = ja.toArray(new Integer[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _watchers;
    }

    public Problem watchers(Integer[] watchers) throws ParseException {
        _watchers = watchers;
        JsonArray ja = new JsonArray();
        for (Integer item : watchers) {
            ja.add(item);
        }
        SOURCE.put("watchers", ja, true);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ContactEnum {
        @Json(name = "all")
        ALL("all"),
        @Json(name = "buyer")
        BUYER("buyer"),
        @Json(name = "provider")
        PROVIDER("provider"),
        @Json(name = "support")
        SUPPORT("support");

        private String value;

        ContactEnum(String value) {
            this.value = value;
        }

        public static ContactEnum fromString(String value) {
            ContactEnum[] values = values();
            for (ContactEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ContactEnum[] fromJsonArray(JsonArray jsonArray) {
            ContactEnum[] list = new ContactEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "delete")
        DELETE("delete"),
        @Json(name = "edit")
        EDIT("edit"),
        @Json(name = "resolve")
        RESOLVE("resolve"),
        @Json(name = "view_message")
        VIEW_MESSAGE("view_message");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Problem[] array) {
        JsonArray list = new JsonArray();
        for (Problem item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Problem[] fromJsonArray(JsonArray array) {
        Problem[] list = new Problem[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Problem fromJson(JsonObject obj) {
        try {
            return new Problem(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Problem> CREATOR = new Parcelable.Creator<Problem>() {

        @Override
        public Problem createFromParcel(Parcel source) {
            try {
                return Problem.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Problem[] newArray(int size) {
            return new Problem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }

    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            if (getActions() != null) _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }

}
